package com.secwager.orderentry;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.secwager.dao.order.OrderRepo;
import com.secwager.orderentry.OrderEntry.SubmitOrderResponse;
import com.secwager.proto.Market.Order;
import com.secwager.proto.cashier.CashierGrpc.CashierStub;
import com.secwager.proto.cashier.CashierOuterClass.CashierActionResult;
import com.secwager.proto.cashier.CashierOuterClass.CashierActionStatus;
import com.secwager.proto.cashier.CashierOuterClass.CashierRequest;
import com.secwager.proto.cashier.CashierOuterClass.TransactionReason;
import io.grpc.stub.StreamObserver;
import java.util.UUID;
import javax.inject.Inject;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OrderEntryServiceImpl extends OrderEntryServiceGrpc.OrderEntryServiceImplBase {

  final Logger log = LoggerFactory.getLogger(OrderEntryServiceImpl.class);
  protected final String MSG_INVALID_USER_TOKEN = "The user's identity could not be verified.";
  protected final String MSG_INTERNAL_ERROR = "Unable to process transaction due to an internal error.";
  protected final String MSG_INSUFFICIENT_FUNDS = "You have insufficent funds available to place the trade.";
  protected final String MSG_SUCCESSFUL_ORDER = "Your order has been placed.";

  private final KafkaProducer<String, byte[]> orderProducer;
  private final CashierStub cashierStub;
  private final int SATOSHIS_PER_CONTRACT = 100000;
  private final int MAX_PRICE_OF_BINARY_OPTION = 100;
  private final FirebaseAuth firebaseAuth;
  private final OrderRepo orderRepo;

  @Inject
  public OrderEntryServiceImpl(KafkaProducer<String, byte[]> orderProducer,
      OrderRepo orderRepo,
      CashierStub cashierStub) {
    this.orderProducer = orderProducer;
    this.cashierStub = cashierStub;
    this.firebaseAuth = FirebaseAuth.getInstance();
    this.orderRepo = orderRepo;
  }

  @Override
  public void submitOrder(OrderEntry.SubmitOrderRequest request,
      io.grpc.stub.StreamObserver<OrderEntry.SubmitOrderResponse> responseObserver) {
    SubmitOrderResponse.Builder submitOrderResponse = SubmitOrderResponse.newBuilder();
    FirebaseToken firebaseToken;
    try {
      firebaseToken = firebaseAuth
          .verifyIdToken(request.getAuthToken(),  /**check revoked**/true);
    } catch (FirebaseAuthException e) {
      log.warn("Could not verify user token: {}", e);
      responseObserver.onNext(submitOrderResponse
          .setSuccess(false)
          .setMessage(MSG_INVALID_USER_TOKEN)
          .build());
      responseObserver.onCompleted();
      return;
    }

    String userId = firebaseToken.getUid();
    Order order = request.getOrder().toBuilder()
        .setOrderId(UUID.randomUUID().toString())
        .build();
    int marginAmount =
        order.getQtyOnMarket() * SATOSHIS_PER_CONTRACT * MAX_PRICE_OF_BINARY_OPTION;

    try {
      orderRepo.insertOrder(order, firebaseToken.getUid());

      CashierRequest lockFundsRequest = CashierRequest.newBuilder()
          .setUserId(userId)
          .setAmount(marginAmount)
          .setReason(TransactionReason.POST_MARGIN)
          .setRelatedEntityId(order.getOrderId())
          .build();

      cashierStub.lockFunds(lockFundsRequest, new StreamObserver<CashierActionResult>() {
        @Override
        public void onNext(CashierActionResult cashierActionResult) {
          switch (cashierActionResult.getStatus()) {
            case SUCCESS:
              orderProducer.beginTransaction();
              orderProducer
                  .send(new ProducerRecord<>(order.getIsin(), order.toByteArray()));
              orderProducer.commitTransaction();
              responseObserver.onNext(SubmitOrderResponse.newBuilder().setSuccess(true)
                  .setMessage(MSG_SUCCESSFUL_ORDER).build());
              responseObserver.onCompleted();
              break;
            case FAILURE_INTERNAL_ERROR:
            case FAILURE_INSUFFICIENT_FUNDS:
              orderRepo.deleteOrder(order);
              submitOrderResponse.setSuccess(false)
                  .setMessage(cashierActionResult.getStatus()
                      == CashierActionStatus.FAILURE_INSUFFICIENT_FUNDS
                      ? MSG_INSUFFICIENT_FUNDS
                      : MSG_INTERNAL_ERROR);
              responseObserver.onNext(submitOrderResponse.build());
              responseObserver.onCompleted();
              break;
            default:
              log.warn("unhandled cashier response: {}", cashierActionResult.getStatus());
              orderRepo.deleteOrder(order);
              submitOrderResponse.setSuccess(false)
                  .setMessage(MSG_INTERNAL_ERROR);
              responseObserver.onNext(submitOrderResponse.build());
              responseObserver.onCompleted();
          }
        }

        @Override
        public void onError(Throwable t) {
          log.error("Exception encountered calling cashier.lockFunds(): {}", t);
          orderRepo.deleteOrder(order);
          submitOrderResponse.setSuccess(false)
              .setMessage(MSG_INTERNAL_ERROR);
          responseObserver.onNext(submitOrderResponse.build());
          responseObserver.onCompleted();

        }

        @Override
        public void onCompleted() {

        }
      });
    } catch (Exception e) {
      log.warn("Exception encountered during order placement: {}", e);
      cashierStub.unlockFunds(CashierRequest.newBuilder().setUserId(userId).setAmount(marginAmount)
              .setReason(TransactionReason.RESET_MARGIN).setRelatedEntityId(order.getOrderId()).build(),
          new StreamObserver<CashierActionResult>() {
            @Override
            public void onNext(CashierActionResult value) {

            }

            @Override
            public void onError(Throwable t) {
              //oh boy, distributed transactions are too hard.
            }

            @Override
            public void onCompleted() {

            }
          });
      orderRepo.deleteOrder(order);
      orderProducer.abortTransaction();
    }
  }
}