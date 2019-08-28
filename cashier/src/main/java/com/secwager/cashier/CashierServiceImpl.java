package com.secwager.cashier;

import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

public class CashierServiceImpl extends CashierGrpc.CashierImplBase{

  public void escrow(com.secwager.cashier.CashierOuterClass.EscrowRequest request,
      io.grpc.stub.StreamObserver<com.secwager.cashier.CashierOuterClass.EscrowResponse> responseObserver) {

  }

  /**
   */
  public void withdrawal(com.secwager.cashier.CashierOuterClass.WithdrawalRequest request,
      io.grpc.stub.StreamObserver<com.secwager.cashier.CashierOuterClass.WithdrawalResponse> responseObserver) {

  }

  /**
   */
  public void transfer(com.secwager.cashier.CashierOuterClass.TransferRequest request,
      io.grpc.stub.StreamObserver<com.secwager.cashier.CashierOuterClass.TransferResponse> responseObserver) {
    
  }
}
