package com.secwager.cashier

import com.secwager.proto.cashier.CashierOuterClass.Balance;


interface CashierRepo {

    enum class TXN_TYPE {SAFE_DEPOSIT,
        RISKY_DEPOSIT,
        RISKY_DEPOSIT_BECOMES_SAFE,
        WITHDRAWAL,
        BET_LOSS,
        BET_WIN,
        BET_PUSH,
        TRANSFER_TO_FRIEND}


    fun getBalance(userId: String) : Balance
    fun directDepositIntoEscrow(userId: String, amount: Int) : Balance
    fun directDepositIntoAvailable(userId: String, amount: Int) : Balance
    fun transferEscrowToAvailable(userId: String, amount:Int) : Balance
    fun transferAvailableToEscrow(userId:String, amount:Int) : Balance
}