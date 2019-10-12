package com.secwager.marketdata;

import com.secwager.marketdata.di.DaggerMarketDataComponent;
import com.secwager.common.GrpcServer;

class App {

  public static void main(String[] args){
    GrpcServer server = new GrpcServer(9300,
        DaggerMarketDataComponent.create().buildMarketDataService());
    server.start();
  }
}