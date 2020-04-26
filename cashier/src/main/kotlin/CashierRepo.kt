package com.secwager.cashier

import com.secwager.proto.cashier.CashierOuterClass.Balance
import com.secwager.proto.cashier.CashierOuterClass.CashierActionResult


interface CashierRepo {

    enum class TransactionReason {
        SAFE_DEPOSIT,
        RISKY_DEPOSIT,
        RISKY_DEPOSIT_BECOMES_SAFE,
        WITHDRAWAL,
        BET_LOSS,
        BET_WIN,
        BET_PUSH,
        TRANSFER_TO_FRIEND
    }


    fun getBalance(userId: String): Balance
    fun directDepositIntoEscrow(userId: String, amount: Int): CashierActionResult
    fun directDepositIntoAvailable(userId: String, amount: Int): CashierActionResult
    fun transferEscrowToAvailable(userId: String, amount: Int, reason: TransactionReason): CashierActionResult
    fun transferAvailableToEscrow(userId: String, amount: Int, reason: TransactionReason): CashierActionResult
}