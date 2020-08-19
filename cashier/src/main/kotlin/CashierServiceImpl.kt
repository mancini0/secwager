package com.secwager.cashier

import com.secwager.proto.cashier.CashierGrpcKt
import com.secwager.proto.cashier.CashierOuterClass.*
import org.slf4j.LoggerFactory
import javax.inject.Inject


class CashierServiceImpl @Inject constructor(private val cashierDao: CashierDao) : CashierGrpcKt.CashierCoroutineImplBase() {
    companion object {
        private val log = LoggerFactory.getLogger(CashierServiceImpl::class.java)
    }

    override suspend fun lockFunds(cashierRequest: CashierRequest): CashierActionResult {
        if (isInvalidForLockOrUnlock(cashierRequest)) {
            return CashierActionResult.newBuilder()
                    .setStatus(CashierActionStatus.FAILURE_MALFORMED_REQUEST).build()
        }
        return CashierActionResult.getDefaultInstance()
    }


    private fun isInvalidForLockOrUnlock(cashierRequest: CashierRequest) = isInvalidForDeposit(cashierRequest) ||
            cashierRequest.reason == TransactionReason.REASON_UNSPECIFIED

    private fun isInvalidForDeposit(cashierRequest: CashierRequest) = cashierRequest.userId.isNullOrBlank() || cashierRequest.amount <= 0
            || cashierRequest.relatedEntityId.isNullOrEmpty()
}

