package com.secwager.orderentry;

import com.secwager.proto.Market.Order;
import com.secwager.proto.cashier.CashierGrpc.CashierBlockingStub;
import com.secwager.proto.cashier.CashierOuterClass.CashierActionResult;
import com.secwager.proto.cashier.CashierOuterClass.CashierRequest;
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

  @Override
  public void submitOrder(OrderEntry.SubmitOrderRequest request,
      io.grpc.stub.StreamObserver<OrderEntry.SubmitOrderResponse> responseObserver) {
    Order o = request.getOrder();
    log.info("order submission: {}", o.toString());
    int maxPrice =
        100 * 100;
    int escrowAmount = o.getQtyOnMarket() * maxPrice;
    CashierRequest cashierRequest = CashierRequest.newBuilder().setAmount(escrowAmount)
        .setUserId("todo-derive-from-token").build();
    try {
      CashierActionResult cashierActionResult = cashierBlockingStub.lockFunds(cashierRequest);
      orderProducer.beginTransaction();
      orderProducer.send(new ProducerRecord<>(o.getIsin(), o.toByteArray()));
      orderProducer.commitTransaction();
      responseObserver.onNext(OrderEntry.SubmitOrderResponse.newBuilder().setSuccess(true).build());
      responseObserver.onCompleted();
      return;
    } catch (Exception e) {
      orderProducer.abortTransaction();
      log.error("oops: {}", e);
      responseObserver.onError(e);
    }
  }
}
