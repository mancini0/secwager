package com.secwager.cashier

import com.secwager.proto.cashier.CashierOuterClass.Balance
import org.apache.commons.dbutils.QueryRunner
import org.apache.commons.dbutils.ResultSetHandler
import org.slf4j.LoggerFactory
import java.sql.ResultSet
import java.sql.SQLException
import javax.inject.Inject

class CashierRepoJdbcImpl @Inject constructor(private val queryRunner: QueryRunner) : CashierRepo {

    companion object {
        val log = LoggerFactory.getLogger(com.secwager.cashier.CashierRepoJdbcImpl::class.java)

        val GET_BALANCE = "SELECT AVAILABLE_BALANCE, ESCROWED_BALANCE FROM ACCT_BALANCE WHERE USER_ID=?"

        val INSERT_TXN_LEDGER = "insert\n" +
                "\t into\n" +
                "\t TXN_LEDGER(USER_ID,\n" +
                "\t TXN_TIME,\n" +
                "\t TXN_TYPE,\n" +
                "\t AVAILABLE_BALANCE,\n" +
                "\t ESCROWED_BALANCE)\n" +
                "select\n" +
                "\t (?,\n" +
                "\t current_timestamp,\n" +
                "\t ?,\n" +
                "\t AVAILABLE_BALANCE,\n" +
                "\t ESCROWED_BALANCE)\n" +
                "from\n" +
                "\t ACCT_BALANCE;"

        val ESCROW_RISKY_DEPOSIT = "update\n" +
                "\t ACCT_BALANCE\n" +
                "set\n" +
                "\t ESCROWED_BALANCE = coalesce(ESCROWED_BALANCE, 0) + ?\n" +
                "where\n" +
                "\t USER_ID =? returning AVAILABLE_BALANCE,\n" +
                "\t ESCROWED_BALANCE;"
    }

    object BalanceResultSetHandler : ResultSetHandler<Balance> {
        override fun handle(rs: ResultSet?): Balance {
            return Balance.newBuilder()
                    .setAvailableBalance(rs?.getInt("AVAILABLE_BALANCE") ?: 0)
                    .setEscrowedBalance(rs?.getInt("ESCROWED_BALANCE") ?: 0)
                    .build()
        }

    }

    override fun getBalance(userId: String): Balance {
        return queryRunner.execute(GET_BALANCE
                , BalanceResultSetHandler, userId).first();
    }


    override fun directDepositIntoEscrow(userId: String, amount: Int): Balance {
        try {
            val balances = queryRunner.execute(ESCROW_RISKY_DEPOSIT, BalanceResultSetHandler, amount, userId)
            when (balances.size) {
                0 -> {
                    log.info("User does not exist")
                }
                1 -> {
                    queryRunner.execute(INSERT_TXN_LEDGER, userId, CashierRepo.TXN_TYPE.RISKY_DEPOSIT)
                    queryRunner.dataSource.connection.commit()
                    return balances.first()
                }
            }
        } catch (e: SQLException) {
            log.error("Exception encountered while handling risky deposit: {}", e)
            queryRunner.dataSource.connection.rollback()
        }
        return Balance.getDefaultInstance();
    }

    override fun directDepositIntoAvailable(userId: String, amount: Int): Balance {
        return queryRunner.execute("UPDATE ACCT_BALANCE SET AVAILABLE_BALANCE = coalesce(AVAILABLE_BALANCE,0) + ? WHERE USER_ID=? RETURNING AVAILABLE_BALANCE, ESCROWED_BALANCE"
                , BalanceResultSetHandler, amount, userId).first();
    }

    override fun transferEscrowToAvailable(userId: String, amount: Int): Balance {
        TODO("Not yet implemented")
    }

    override fun transferAvailableToEscrow(userId: String, amount: Int): Balance {
        TODO("Not yet implemented")
    }

}