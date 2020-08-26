package com.secwager.cashier

import com.secwager.dao.cashier.CashierDao
import com.secwager.proto.cashier.CashierGrpcKt
import com.secwager.proto.cashier.CashierOuterClass.*
import org.slf4j.LoggerFactory
import javax.inject.Inject


class CashierServiceImpl @Inject constructor(private val cashierDao: CashierDao) : CashierGrpcKt.CashierCoroutineImplBase() {
    companion object {
        private val log = LoggerFactory.getLogger(CashierServiceImpl::class.java)
    }

    override suspend fun lockFunds(req: CashierRequest): CashierActionResult {
        if (isInvalidForLockOrUnlock(req)) {
            return CashierActionResult.newBuilder()
                    .setStatus(CashierActionStatus.FAILURE_MALFORMED_REQUEST).build()
        }
        return cashierDao.lockFunds(req.userId, req.amount,
                req.reason, req.relatedEntityId)
    }

    override suspend fun unlockFunds(req: CashierRequest): CashierActionResult {
        if (isInvalidForLockOrUnlock(req)) {
            return CashierActionResult.newBuilder()
                    .setStatus(CashierActionStatus.FAILURE_MALFORMED_REQUEST).build()
        }
        return cashierDao.unlockFunds(req.userId, req.amount,
                req.reason, req.relatedEntityId)
    }

    override suspend fun depositRisky(req: CashierRequest): CashierActionResult {
        return if (isInvalidForDeposit(req)) CashierActionResult.newBuilder()
                .setStatus(CashierActionStatus.FAILURE_MALFORMED_REQUEST).build()
        else
            cashierDao.depositIntoEscrow(req.p2PkhAddress, req.amount, req.relatedEntityId)
    }

    override suspend fun depositSafe(req: CashierRequest): CashierActionResult {
        return if (isInvalidForDeposit(req)) CashierActionResult.newBuilder()
                .setStatus(CashierActionStatus.FAILURE_MALFORMED_REQUEST).build()
        else
            cashierDao.depositIntoAvailable(req.p2PkhAddress, req.amount, req.relatedEntityId)
    }


    private fun isInvalidForLockOrUnlock(req: CashierRequest) = isInvalidForDeposit(req) ||
            req.reason == TransactionReason.REASON_UNSPECIFIED

    private fun isInvalidForDeposit(req: CashierRequest) = req.p2PkhAddress.isNullOrBlank() || req.amount <= 0
            || req.relatedEntityId.isNullOrEmpty()
}

