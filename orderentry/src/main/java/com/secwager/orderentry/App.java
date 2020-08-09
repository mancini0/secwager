package com.secwager.orderentry;

import com.secwager.orderentry.di.DaggerOrderEntryComponent;
import io.grpc.ServerBuilder;

public class App {


  public static void main(String[] args) throws Exception {
    ServerBuilder.forPort(9085)
        .addService(DaggerOrderEntryComponent.create().buildOrderEntryService())
        .build().awaitTermination();
  }
}
