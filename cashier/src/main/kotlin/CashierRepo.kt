package com.secwager.cashier

import com.secwager.proto.cashier.CashierOuterClass.*


interface CashierRepo {

    fun getBalance(userId: String): Balance
    fun directDepositIntoEscrow(userId: String, amount: Int): CashierActionResult
    fun directDepositIntoAvailable(userId: String, amount: Int): CashierActionResult
    fun lockFunds(userId: String, amount: Int, reason: TransactionReason): CashierActionResult
    fun unlockFunds(userId: String, amount: Int, reason: TransactionReason): CashierActionResult
}