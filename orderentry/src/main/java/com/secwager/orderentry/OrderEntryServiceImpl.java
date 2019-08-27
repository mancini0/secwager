package com.secwager.orderentry;

import com.secwager.orderentry.Orderentry.SubmitOrderResponse;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.apache.kafka.clients.KafkaClient;

@Singleton
public class OrderEntryServiceImpl extends OrderEntryGrpc.OrderEntryImplBase {

  @Inject
  public OrderEntryServiceImpl(KafkaClient kafkaClient) {

  }

  public void submitOrder(com.secwager.orderentry.Orderentry.SubmitOrderRequest request,
      io.grpc.stub.StreamObserver<com.secwager.orderentry.Orderentry.SubmitOrderResponse> responseObserver) {

    responseObserver.onNext(SubmitOrderResponse.newBuilder().setSuccess(true).build());

  }
}
