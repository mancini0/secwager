package com.secwager.cashier

import com.secwager.proto.cashier.CashierGrpc.CashierImplBase
import com.secwager.proto.cashier.CashierOuterClass.*
import io.grpc.stub.StreamObserver
import org.apache.commons.dbutils.QueryRunner
import org.slf4j.LoggerFactory
import javax.inject.Inject


class CashierServiceImpl @Inject constructor(private val queryRunner: QueryRunner) : CashierImplBase() {
    companion object {
        private val log = LoggerFactory.getLogger(CashierServiceImpl::class.java)
    }

    override fun escrow(request: EscrowRequest,
                        responseObserver: StreamObserver<EscrowResponse>) {
        responseObserver.onNext(EscrowResponse.newBuilder().setEscrowStatus(CashierActionResult.SUCCESS).build())
        responseObserver.onCompleted()



    }

    override fun withdrawal(request: WithdrawalRequest,
                            responseObserver: StreamObserver<WithdrawalResponse>) {
    }

    override fun transfer(request: TransferRequest,
                          responseObserver: StreamObserver<TransferResponse>) {
    }

}