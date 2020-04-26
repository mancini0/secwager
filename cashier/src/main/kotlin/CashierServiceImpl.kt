package com.secwager.cashier

import com.secwager.proto.cashier.CashierGrpc.CashierImplBase
import com.secwager.proto.cashier.CashierOuterClass.*
import io.grpc.stub.StreamObserver
import org.apache.commons.dbutils.QueryRunner
import org.slf4j.LoggerFactory
import javax.inject.Inject

/**
 * rpc LockFunds (UserAmount) returns (LockFundsResponse);
 * rpc UnlockFunds (UserAmount) returns (UnlockFundsResponse);
 * rpc DepositRisky (DepositRequest) returns (DepositResponse);
 * rpc DepositSafe (DepositRequest) returns (DepositResponse);
 * rpc GetBalance (BalanceRequest) returns (stream Balance);
 * rpc Withdrawal (WithdrawalRequest) returns (WithdrawalResponse) **/

class CashierServiceImpl @Inject constructor(private val queryRunner: QueryRunner) : CashierImplBase() {
    companion object {
        private val log = LoggerFactory.getLogger(CashierServiceImpl::class.java)
    }

    override fun lockFunds(userAmount: UserAmount,
                           responseObserver: StreamObserver<LockFundsResponse>) {
        TODO("NOT IMPLEMENTED")
    }

    override fun unlockFunds(userAmount: UserAmount,
                             responseObserver: StreamObserver<UnlockFundsResponse>) {
        TODO("NOT IMPLEMENTED")
    }

    override fun depositRisky(depositRequest: DepositRequest,
                              responseObserver: StreamObserver<DepositResponse>) {
        TODO("NOT IMPLEMENTED")
    }

    override fun depositSafe(deposit: DepositRequest, responseObserver: StreamObserver<DepositResponse>) {
        TODO("NOT IMPLEMENTED")
    }

    override fun getBalance(depositRequest: BalanceRequest,
                            responseObserver: StreamObserver<Balance>) {
        TODO("NOT IMPLEMENTED")
    }

}