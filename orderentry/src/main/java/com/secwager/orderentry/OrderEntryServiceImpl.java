package com.secwager.orderentry;

import com.secwager.Market.Order;
import com.secwager.cashier.CashierGrpc.CashierBlockingStub;
import com.secwager.cashier.CashierOuterClass.EscrowRequest;
import com.secwager.cashier.CashierOuterClass.CashierActionResult;

import javax.inject.Inject;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class OrderEntryServiceImpl extends OrderEntryServiceGrpc.OrderEntryServiceImplBase {

  final Logger log = LoggerFactory.getLogger(OrderEntryServiceImpl.class);
  private final KafkaProducer<String, byte[]> orderProducer;
  private final CashierBlockingStub cashierBlockingStub;

  @Inject
  public OrderEntryServiceImpl(KafkaProducer<String, byte[]> orderProducer,
      CashierBlockingStub cashierBlockingStub) {
    this.orderProducer = orderProducer;
    this.cashierBlockingStub = cashierBlockingStub;
  }

  public void submitOrder(OrderEntry.SubmitOrderRequest request,
                          io.grpc.stub.StreamObserver<OrderEntry.SubmitOrderResponse> responseObserver) {
    Order o = request.getOrder();
    log.info("order submission: {}", o.toString());
    int maxPrice =
        100 * 100;
    int escrowAmount =  o.getOrderQty() * maxPrice;
    EscrowRequest req = EscrowRequest.newBuilder().setAmount(escrowAmount)
        .setUserId("todo-derive-from-token").build();
    try {
      log.info("cashier authority: {}", cashierBlockingStub.getChannel().authority());
      CashierActionResult cashierActionResult = cashierBlockingStub.escrow(req).getEscrowStatus();
      orderProducer.beginTransaction();
      orderProducer.send(new ProducerRecord<>(o.getSymbol(), o.toByteArray()));
      orderProducer.commitTransaction();
      responseObserver.onNext(OrderEntry.SubmitOrderResponse.newBuilder().setSuccess(true).build());
      responseObserver.onCompleted();
      return;
    }catch (Exception e){
      log.error("oops: {}", e);
      responseObserver.onError(e);
    }
  }
}
