package com.secwager.orderentry;

import com.secwager.Market.Order;
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

  @Inject
  public OrderEntryServiceImpl(KafkaProducer<String, byte[]> orderProducer) {
    this.orderProducer = orderProducer;
  }

  public void submitOrder(SubmitOrderRequest request,
      io.grpc.stub.StreamObserver<SubmitOrderResponse> responseObserver) {
    Order o = request.getOrder();
    orderProducer.beginTransaction();
    orderProducer.send(new ProducerRecord<>(o.getSymbol(), o.toByteArray()));
    orderProducer.commitTransaction();
    responseObserver.onNext(SubmitOrderResponse.newBuilder().setSuccess(true).build());
  }
}
