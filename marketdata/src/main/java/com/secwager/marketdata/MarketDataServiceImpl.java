package com.secwager.marketdata;

import com.secwager.marketdata.MarketData.Instrument;
import com.secwager.marketdata.MarketData.MarketDataRequest;
import com.secwager.marketdata.MarketData.MarketDataResponse;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.inject.Inject;

import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import com.google.auth.oauth2.GoogleCredentials;

public class MarketDataServiceImpl extends MarketDataServiceGrpc.MarketDataServiceImplBase {

  final Logger log = LoggerFactory.getLogger(MarketDataServiceImpl.class);


  @Inject
  public MarketDataServiceImpl(){

  }

  @Override
  public void subscribeToMarketData(com.secwager.marketdata.MarketData.MarketDataRequest request,
      io.grpc.stub.StreamObserver<com.secwager.marketdata.MarketData.MarketDataResponse> responseObserver) {
      responseObserver.onNext(MarketDataResponse.newBuilder().build());
      responseObserver.onCompleted();
  }







}