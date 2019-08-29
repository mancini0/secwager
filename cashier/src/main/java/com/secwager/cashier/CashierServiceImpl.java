package com.secwager.cashier;


import com.secwager.cashier.CashierOuterClass.CashierActionResult;
import com.secwager.cashier.CashierOuterClass.EscrowResponse;
import javax.inject.Inject;
import javax.sql.DataSource;
import org.apache.commons.dbutils.QueryRunner;

public class CashierServiceImpl extends CashierGrpc.CashierImplBase{
  private final QueryRunner queryRunner;

  @Inject
  public CashierServiceImpl(QueryRunner queryRunner){
    this.queryRunner=queryRunner;
  }

  @Override
  public void escrow(com.secwager.cashier.CashierOuterClass.EscrowRequest request,
      io.grpc.stub.StreamObserver<com.secwager.cashier.CashierOuterClass.EscrowResponse> responseObserver) {

    responseObserver.onNext(EscrowResponse.newBuilder().setEscrowStatus(CashierActionResult.SUCCESS).build());
    responseObserver.onCompleted();
  }


  @Override
  public void withdrawal(com.secwager.cashier.CashierOuterClass.WithdrawalRequest request,
      io.grpc.stub.StreamObserver<com.secwager.cashier.CashierOuterClass.WithdrawalResponse> responseObserver) {

  }


  @Override
  public void transfer(com.secwager.cashier.CashierOuterClass.TransferRequest request,
      io.grpc.stub.StreamObserver<com.secwager.cashier.CashierOuterClass.TransferResponse> responseObserver) {

  }
}
