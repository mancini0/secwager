package com.secwager.cashier

import com.secwager.proto.cashier.CashierGrpc.CashierImplBase
import com.secwager.proto.cashier.CashierOuterClass.*
import io.grpc.stub.StreamObserver
import org.slf4j.LoggerFactory
import javax.inject.Inject


class CashierServiceImpl @Inject constructor(private val cashierRepo: CashierRepo) : CashierImplBase() {
    companion object {
        private val log = LoggerFactory.getLogger(CashierServiceImpl::class.java)
        val INVALID_TRANSFER_MSG = "Requests to lock funds must contain a non-empty user id, " +
                "the amount must be greater than 0, a valid transaction reason must be specifed, and a related entity id must be provided."
        val INVALID_DEPOSIT_MSG = "Requests to deposit funds must contain a non-empty user id, " +
                "the amount must be greater than 0, and a related entity id must be provided."
    }

    override fun lockFunds(cashierRequest: CashierRequest,
                           responseObserver: StreamObserver<CashierActionResult>) {
        if (isInvalidForLockOrUnlock(cashierRequest)) {
            responseObserver.onError(IllegalArgumentException(INVALID_TRANSFER_MSG))
            responseObserver.onCompleted()
            return
        }
        responseObserver.onNext(cashierRepo.lockFunds(cashierRequest.userId, cashierRequest.amount, cashierRequest.reason, cashierRequest.relatedEntityId))
        responseObserver.onCompleted()
    }

    override fun unlockFunds(cashierRequest: CashierRequest,
                             responseObserver: StreamObserver<CashierActionResult>) {
        if (isInvalidForLockOrUnlock(cashierRequest)) {
            responseObserver.onError(IllegalArgumentException(INVALID_TRANSFER_MSG))
            responseObserver.onCompleted()
            return
        }
        responseObserver.onNext(cashierRepo.unlockFunds(cashierRequest.userId, cashierRequest.amount, cashierRequest.reason, cashierRequest.relatedEntityId))
        responseObserver.onCompleted()
    }

    override fun depositRisky(cashierRequest: CashierRequest, responseObserver: StreamObserver<CashierActionResult>) {
        if (isInvalidForDeposit(cashierRequest)) {
            responseObserver.onError(IllegalArgumentException(INVALID_DEPOSIT_MSG))
            responseObserver.onCompleted()
            return
        }

        responseObserver.onNext(cashierRepo.directDepositIntoEscrow(cashierRequest.userId, cashierRequest.amount, entityId = cashierRequest.relatedEntityId))
        responseObserver.onCompleted()
    }

    override fun depositSafe(cashierRequest: CashierRequest, responseObserver: StreamObserver<CashierActionResult>) {
        if (isInvalidForDeposit(cashierRequest)) {
            responseObserver.onError(IllegalArgumentException(INVALID_DEPOSIT_MSG))
            responseObserver.onCompleted()
            return
        }
        responseObserver.onNext(cashierRepo.directDepositIntoAvailable(cashierRequest.userId, cashierRequest.amount, entityId = cashierRequest.relatedEntityId))
        responseObserver.onCompleted()
    }

    override fun streamBalance(balanceRequest: BalanceRequest,
                               responseObserver: StreamObserver<Balance>) {

    }

    private fun isInvalidForLockOrUnlock(cashierRequest: CashierRequest) = isInvalidForDeposit(cashierRequest) ||
            cashierRequest.reason == TransactionReason.REASON_UNSPECIFIED

    private fun isInvalidForDeposit(cashierRequest: CashierRequest) = cashierRequest.userId.isNullOrBlank() || cashierRequest.amount <= 0
            || cashierRequest.relatedEntityId.isNullOrEmpty()
}

