package com.secwager.cashier

import com.secwager.proto.cashier.CashierOuterClass.*


interface CashierRepo {

    fun getBalance(userId: String): CashierActionResult
    fun directDepositIntoEscrow(p2pkhAddress: String, amount: Int, entityId: String): CashierActionResult
    fun directDepositIntoAvailable(p2pkhAddress: String, amount: Int, entityId: String): CashierActionResult
    fun lockFunds(userId: String, amount: Int, reason: TransactionReason, entityId: String): CashierActionResult
    fun unlockFunds(userId: String, amount: Int, reason: TransactionReason, entityId: String): CashierActionResult
}