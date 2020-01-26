package com.secwager.marketdata;



import com.secwager.marketdata.MarketData.MarketDataRequest;
import com.secwager.marketdata.MarketData.MarketDataResponse;

import io.grpc.stub.ServerCallStreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.inject.Inject;



public class MarketDataServiceImpl extends MarketDataServiceGrpc.MarketDataServiceImplBase {

  private final Logger log = LoggerFactory.getLogger(MarketDataServiceImpl.class);
  private final ObserverNotifyingMarketDataEventListener eventListener;

  @Inject
  public MarketDataServiceImpl(ObserverNotifyingMarketDataEventListener eventListener){
    this.eventListener = eventListener;
  }

  @Override
  public void subscribeToMarketData(MarketDataRequest request,
      io.grpc.stub.StreamObserver<MarketDataResponse> responseObserver) {
    ServerCallStreamObserver<MarketDataResponse> observer = (ServerCallStreamObserver<MarketDataResponse>) responseObserver;
    observer.setOnCancelHandler(()->{
      log.info("removing observer!");
      eventListener.removeObserver(observer);
      observer.onCompleted();
    });
    eventListener.addObserver(observer);
  }







}