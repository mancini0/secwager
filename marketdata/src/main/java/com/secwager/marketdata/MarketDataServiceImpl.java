package com.secwager.marketdata;

import com.secwager.marketdata.MarketData.Instrument;
import com.secwager.marketdata.MarketData.InstrumentResponse;

import com.secwager.marketdata.dao.InstrumentRepo;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.inject.Inject;

public class MarketDataServiceImpl extends MarketDataServiceGrpc.MarketDataServiceImplBase {

  final Logger log = LoggerFactory.getLogger(MarketDataServiceImpl.class);
  private final InstrumentRepo instrumentRepo;

  @Inject
  public MarketDataServiceImpl(InstrumentRepo instrumentRepo) {
    this.instrumentRepo = instrumentRepo;
  }

  @Override
  public void getInstruments(com.secwager.marketdata.MarketData.InstrumentRequest request,
      io.grpc.stub.StreamObserver<com.secwager.marketdata.MarketData.InstrumentResponse> responseObserver) {
      log.info("hello my friend");
      Set<Instrument> instruments = instrumentRepo.findAllActiveInstruments();
      responseObserver.onNext(InstrumentResponse.newBuilder().addAllInstruments(instruments).build());
      responseObserver.onCompleted();
  }







}