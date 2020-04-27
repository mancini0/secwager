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


    override fun lockFunds(cashierRequest: CashierRequest,
                           responseObserver: StreamObserver<CashierActionResult>) {

        TODO()
    }

    override fun unlockFunds(cashierRequest: CashierRequest,
                             responseObserver: StreamObserver<CashierActionResult>) {
        TODO("NOT IMPLEMENTED")
    }

    override fun depositRisky(cashierRequest: CashierRequest, responseObserver: StreamObserver<CashierActionResult>) {
        TODO("NOT IMPLEMENTED")
    }

    override fun depositSafe(cashierRequest: CashierRequest, responseObserver: StreamObserver<CashierActionResult>) {
        TODO("NOT IMPLEMENTED")
    }

    override fun streamBalance(balanceRequest: BalanceRequest,
                               responseObserver: StreamObserver<Balance>) {
        TODO("NOT IMPLEMENTED")
    }

}