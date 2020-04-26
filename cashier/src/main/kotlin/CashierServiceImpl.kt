package com.secwager.cashier

import com.secwager.proto.cashier.CashierGrpc.CashierImplBase
import com.secwager.proto.cashier.CashierOuterClass.*
import io.grpc.stub.StreamObserver
import org.slf4j.LoggerFactory
import javax.inject.Inject


class CashierServiceImpl @Inject constructor(private val cashierRepo: CashierRepo) : CashierImplBase() {
    companion object {
        private val log = LoggerFactory.getLogger(CashierServiceImpl::class.java)
    }


    override fun lockFunds(userAmount: UserAmount,
                           responseObserver: StreamObserver<CashierActionResult>) {
        TODO("NOT IMPLEMENTED")
    }

    override fun unlockFunds(userAmount: UserAmount,
                             responseObserver: StreamObserver<CashierActionResult>) {
        TODO("NOT IMPLEMENTED")
    }

    override fun depositRisky(userAmount: UserAmount, responseObserver: StreamObserver<CashierActionResult>) {
        TODO("NOT IMPLEMENTED")
    }

    override fun depositSafe(userAmount: UserAmount, responseObserver: StreamObserver<CashierActionResult>) {
        TODO("NOT IMPLEMENTED")
    }

    override fun streamBalance(depositRequest: BalanceRequest,
                               responseObserver: StreamObserver<Balance>) {
        TODO("NOT IMPLEMENTED")
    }

}