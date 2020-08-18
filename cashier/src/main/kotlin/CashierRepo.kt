package com.secwager.cashier

import com.secwager.proto.cashier.CashierOuterClass.CashierActionResult
import com.secwager.proto.cashier.CashierOuterClass.TransactionReason


interface CashierRepo {

    suspend fun getBalance(userId: String): CashierActionResult
    suspend fun depositIntoEscrow(p2pkhAddress: String, amount: Int, entityId: String): CashierActionResult
    suspend fun depositIntoAvailable(p2pkhAddress: String, amount: Int, entityId: String): CashierActionResult
    suspend fun lockFunds(userId: String, amount: Int, reason: TransactionReason, entityId: String): CashierActionResult
    suspend fun unlockFunds(userId: String, amount: Int, reason: TransactionReason, entityId: String): CashierActionResult
}