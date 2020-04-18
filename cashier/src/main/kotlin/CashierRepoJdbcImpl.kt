package com.secwager.cashier

import com.secwager.proto.cashier.CashierOuterClass.Balance
import org.apache.commons.dbutils.QueryRunner
import javax.inject.Inject

class CashierRepoJdbcImpl @Inject constructor(private val queryRunner: QueryRunner) : CashierRepo {

    override fun getBalance(userId: String): Balance {
        return Balance.getDefaultInstance()
    }

}