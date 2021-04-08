package com.secwager.cashier

import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.CacheWriter
import com.github.benmanes.caffeine.cache.Caffeine
import com.github.benmanes.caffeine.cache.RemovalCause
import com.secwager.dao.cashier.CashierDao
import com.secwager.proto.cashier.CashierGrpcKt
import com.secwager.proto.cashier.CashierOuterClass.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject


class CashierServiceImpl
@Inject constructor(private val cashierDao: CashierDao) : CashierGrpcKt.CashierCoroutineImplBase() {

    private val subscribers: MutableMap<String, MutableStateFlow<Balance>> = ConcurrentHashMap()

    private val balanceCache: Cache<String, Balance> = Caffeine.newBuilder()
            .writer(object : CacheWriter<String, Balance> {
                override fun write(uid: String, balance: Balance) {
                    subscribers.get(uid)?.value = balance
                }

                override fun delete(uid: String, balance: Balance?, removalCause: RemovalCause) {
                }

            }).build()

    companion object {
        private val log = LoggerFactory.getLogger(CashierServiceImpl::class.java)
    }

    override fun streamBalance(request: BalanceRequest): MutableStateFlow<Balance> {
        val balance: Balance = balanceCache.get(request.userId) { user ->
            runBlocking {
                val result = cashierDao.getBalance(user)
                result.balance
            }
        } ?: Balance.getDefaultInstance()
        val flow = MutableStateFlow(balance)
        subscribers[request.userId] = flow
        return flow
    }

    override suspend fun lockFunds(req: CashierRequest): CashierActionResult {
        if (isInvalidForLockOrUnlock(req)) {
            return CashierActionResult.newBuilder()
                    .setStatus(CashierActionStatus.FAILURE_MALFORMED_REQUEST).build()
        }

        return cashierDao.lockFunds(req.userId, req.amount,
                req.reason, req.relatedEntityId).also {
            if (it.status.equals(CashierActionStatus.SUCCESS)) {
                balanceCache.put(it.userId, it.balance)
            }
        }


    }

    override suspend fun unlockFunds(req: CashierRequest): CashierActionResult {
        if (isInvalidForLockOrUnlock(req)) {
            return CashierActionResult.newBuilder()
                    .setStatus(CashierActionStatus.FAILURE_MALFORMED_REQUEST).build()
        }
        return cashierDao.unlockFunds(req.userId, req.amount,
                req.reason, req.relatedEntityId).also {
            if (it.status == CashierActionStatus.SUCCESS) {
                balanceCache.put(it.userId, it.balance)
            }
        }
    }

    override suspend fun depositRisky(req: CashierRequest): CashierActionResult {
        return if (isInvalidForDeposit(req)) CashierActionResult.newBuilder()
                .setStatus(CashierActionStatus.FAILURE_MALFORMED_REQUEST).build()
        else
            cashierDao.depositIntoEscrow(req.p2PkhAddress, req.amount, req.relatedEntityId).also {
                if (it.status.equals(CashierActionStatus.SUCCESS)) {
                    balanceCache.put(it.userId, it.balance)
                }
            }
    }

    override suspend fun depositSafe(req: CashierRequest): CashierActionResult {
        return if (isInvalidForDeposit(req)) CashierActionResult.newBuilder()
                .setStatus(CashierActionStatus.FAILURE_MALFORMED_REQUEST).build()
        else
            cashierDao.depositIntoAvailable(req.p2PkhAddress, req.amount, req.relatedEntityId).also {
                if (it.status.equals(CashierActionStatus.SUCCESS)) {
                    balanceCache.put(it.userId, it.balance)
                }
            }
    }


    private fun isInvalidForLockOrUnlock(req: CashierRequest) = isInvalidForDeposit(req) ||
            req.reason == TransactionReason.REASON_UNSPECIFIED

    private fun isInvalidForDeposit(req: CashierRequest) = req.p2PkhAddress.isNullOrBlank() || req.amount <= 0
            || req.relatedEntityId.isNullOrEmpty()
}

