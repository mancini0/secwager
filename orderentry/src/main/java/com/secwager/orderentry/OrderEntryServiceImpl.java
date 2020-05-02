package com.secwager.orderentry;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.secwager.dao.order.OrderRepo;
import com.secwager.orderentry.OrderEntry.SubmitOrderResponse;
import com.secwager.proto.Market.Order;
import com.secwager.proto.cashier.CashierGrpc.CashierStub;
import com.secwager.proto.cashier.CashierOuterClass.CashierActionResult;
import com.secwager.proto.cashier.CashierOuterClass.CashierRequest;
import com.secwager.proto.cashier.CashierOuterClass.TransactionReason;
import io.grpc.stub.StreamObserver;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import javax.inject.Inject;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OrderEntryServiceImpl extends OrderEntryServiceGrpc.OrderEntryServiceImplBase {

  final Logger log = LoggerFactory.getLogger(OrderEntryServiceImpl.class);
  private final KafkaProducer<String, byte[]> orderProducer;
  private final CashierStub cashierStub;
  private final int SATOSHIS_PER_CONTRACT = 100000;
  private final int MAX_PRICE_OF_BINARY_OPTION = 100;
  private final FirebaseAuth firebaseAuth;

  @Inject
  public OrderEntryServiceImpl(KafkaProducer<String, byte[]> orderProducer,
      OrderRepo orderRepo,
      CashierStub cashierStub) {
    this.orderProducer = orderProducer;
    this.cashierStub = cashierStub;
    this.firebaseAuth = FirebaseAuth.getInstance();
  }

  @Override
  public void submitOrder(OrderEntry.SubmitOrderRequest request,
      io.grpc.stub.StreamObserver<OrderEntry.SubmitOrderResponse> responseObserver) {

    CompletableFuture.supplyAsync(() -> {
      try {
        return firebaseAuth.verifyIdToken(request.getAuthToken(),  /**check revoked**/true);
      } catch (FirebaseAuthException e) {
        log.warn("Could not verify user token: {}", e);
        return null;
      }
    }).thenAccept(firebaseToken -> {
      if (firebaseToken == null) {
        responseObserver.onNext(SubmitOrderResponse.newBuilder()
            .setSuccess(false)
            .setMessage("The user's identity could not be verified.")
            .build());
        responseObserver.onCompleted();
        return;
      }

      Order order = request.getOrder().toBuilder()
          .setOrderId(UUID.randomUUID().toString())
          .build();
      int marginAmount =
          order.getQtyOnMarket() * SATOSHIS_PER_CONTRACT * MAX_PRICE_OF_BINARY_OPTION;

      CashierRequest lockFundsRequest = CashierRequest.newBuilder()
          .setUserId(firebaseToken.getUid())
          .setAmount(marginAmount)
          .setReason(TransactionReason.POST_MARGIN)
          .setRelatedEntityId(order.getOrderId())
          .build();

      cashierStub.lockFunds(lockFundsRequest, new StreamObserver<CashierActionResult>() {
        @Override
        public void onNext(CashierActionResult cashierActionResult) {

        }

        @Override
        public void onError(Throwable throwable) {

        }

        @Override
        public void onCompleted() {

        }
      });

//      try {
//        orderProducer.beginTransaction();
//        orderProducer.send(new ProducerRecord<>(order.getIsin(), order.toByteArray()));
//        orderProducer.commitTransaction();
//        responseObserver
//            .onNext(OrderEntry.SubmitOrderResponse.newBuilder().setSuccess(true).build());
//        responseObserver.onCompleted();
//        return;
//      } catch (Exception e) {
//        orderProducer.abortTransaction();
//        log.error("oops: {}", e);
//        responseObserver.onError(e);
//      }
    });
  }
}
