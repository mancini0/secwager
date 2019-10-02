package com.secwager.marketdata;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.inject.Inject;

public class MarketDataServiceImpl extends MarketDataServiceGrpc.MarketDataServiceImplBase {

  final Logger log = LoggerFactory.getLogger(MarketDataServiceImpl.class);
  private final QueryRunner queryRunner;

  @Inject
  public MarketDataServiceImpl(QueryRunner queryRunner) {
    this.queryRunner=queryRunner;
    try{
      List<Map<String, Object>> results = queryRunner.query("SELECT * FROM INSTRUMENT", new MapListHandler());
      results.forEach(row->log.info("{}", row.toString()));
    }catch(Exception e){
      throw new RuntimeException(e);
    }
  }

  @Override
  public void getInstruments(com.secwager.marketdata.MarketData.InstrumentRequest request,
      io.grpc.stub.StreamObserver<com.secwager.marketdata.MarketData.InstrumentResponse> responseObserver) {

  }







}