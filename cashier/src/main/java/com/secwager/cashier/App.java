package com.secwager.cashier;

import com.secwager.cashier.di.DaggerCashierComponent;
import com.secwager.common.GrpcServer;

public class App {

  public static void main(String[] args) throws Exception {
    GrpcServer server = new GrpcServer(9305,
        DaggerCashierComponent.create().buildCashierService());
    server.start();
  }
}
