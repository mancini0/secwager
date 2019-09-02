package com.secwager.orderentry;

import com.secwager.common.GrpcServer;
import com.secwager.orderentry.di.DaggerOrderEntryComponent;

public class App {


  public static void main(String[] args) throws Exception {
    GrpcServer server = new GrpcServer(9085,
        DaggerOrderEntryComponent.create().buildOrderEntryService());
    server.start();
  }
}
