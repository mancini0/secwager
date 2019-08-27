package com.secwager.orderentry;

import com.secwager.Market.Order;
import com.secwager.cashier.CashierGrpc.CashierBlockingStub;
import com.secwager.cashier.CashierOuterClass.AttemptEscrowRequest;
import com.secwager.cashier.CashierOuterClass.CashierActionResult;
import com.secwager.orderentry.OrderEntryOuterClass.SubmitOrderRequest;
import com.secwager.orderentry.OrderEntryOuterClass.SubmitOrderResponse;
import javax.inject.Inject;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class OrderEntryServiceImpl extends OrderEntryGrpc.OrderEntryImplBase {

  final Logger log = LoggerFactory.getLogger(OrderEntryServiceImpl.class);
  private final KafkaProducer<String, byte[]> orderProducer;
  private final CashierBlockingStub cashierBlockingStub;

  @Inject
  public OrderEntryServiceImpl(KafkaProducer<String, byte[]> orderProducer,
      CashierBlockingStub cashierBlockingStub) {
    this.orderProducer = orderProducer;
    this.cashierBlockingStub = cashierBlockingStub;
  }

  public void submitOrder(SubmitOrderRequest request,
      io.grpc.stub.StreamObserver<SubmitOrderResponse> responseObserver) {
    Order o = request.getOrder();
    int maxPrice =
        100 * 100; //quote in pennies to avoid rounding issues (100 dollars * 100 pennies)
    int escrowAmount = o.getIsLimit() ? o.getOrderQty() * o.getPrice() : o.getOrderQty() * maxPrice;
    AttemptEscrowRequest req = AttemptEscrowRequest.newBuilder().setAmount(escrowAmount)
        .setUserId("todo-derive-from-token").build();
    if (cashierBlockingStub.attemptEscrow(req).getEscrowStatus()
        .equals(CashierActionResult.SUCCESS)) {
      orderProducer.beginTransaction();
      orderProducer.send(new ProducerRecord<>(o.getSymbol(), o.toByteArray()));
      orderProducer.commitTransaction();
      responseObserver.onNext(SubmitOrderResponse.newBuilder().setSuccess(true).build());
      responseObserver.onCompleted();
      return;
    }
    responseObserver
        .onNext(SubmitOrderResponse.newBuilder().setSuccess(false)
            .setMessage("todo").build());
  }
}
