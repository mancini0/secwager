package com.secwager.cashier

import com.secwager.proto.cashier.CashierOuterClass.Balance;


interface CashierRepo {

    fun getBalance(userId: String) : Balance
}