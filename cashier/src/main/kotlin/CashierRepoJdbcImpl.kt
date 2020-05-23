package com.secwager.cashier

import com.secwager.proto.cashier.CashierOuterClass.*
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
                " \t  into\n" +
                " \t  TXN_LEDGER(USER_ID,\n" +
                " \t  TXN_TIME,\n" +
                " \t  TXN_REASON,\n" +
                " \t  RELATED_ENTITY,\n" +
                " \t  AVAILABLE_BALANCE,\n" +
                " \t  ESCROWED_BALANCE)\n" +
                "select\n" +
                " \t  (user_id,\n" +
                " \t  current_timestamp,\n" +
                " \t  ?,\n" +
                " \t  ?,\n" +
                " \t  AVAILABLE_BALANCE,\n" +
                " \t  ESCROWED_BALANCE)\n" +
                "from\n" +
                " \t  ACCT_BALANCE where user_id=?"

        val RISKY_DEPOSIT = "with cte as (select user_id, ? as satoshis from users u where u.p2pkh_addr = ?)\n" +
                "insert into acct_balance (user_id ,escrowed_balance) select cte.user_id, cte.satoshis from cte\n" +
                "on conflict(user_id)\n" +
                "do update set escrowed_balance = acct_balance.escrowed_balance +(select cte.satoshis from cte)\n" +
                "returning user_id ,available_balance, escrowed_balance"

        val SAFE_DEPOSIT = "with cte as (select user_id, ? as satoshis from users u where u.p2pkh_addr =?)\n" +
                "insert into acct_balance (user_id ,available_balance) select cte.user_id, cte.satoshis from cte\n" +
                "on conflict(user_id)\n" +
                "do update set available_balance = acct_balance.available_balance +(select cte.satoshis from cte)\n" +
                "returning user_id ,available_balance, escrowed_balance"

        //cte strategy unusable until https://github.com/YugaByte/yugabyte-db/issues/738 is fixed
        val LOCK_FUNDS_CTE = "with cte as (\n" +
                "select\n" +
                " \t ? as val)\n" +
                "update\n" +
                " \t acct_balance\n" +
                "set\n" +
                " \t available_balance =\n" +
                " \t case\n" +
                " \t  \t when AVAILABLE_BALANCE >= cte.val then available_balance - cte.val\n" +
                " \t  \t else available_balance end,\n" +
                " \t  \t escrowed_balance =\n" +
                " \t  \t case\n" +
                " \t  \t  \t when available_balance >= cte.val then escrowed_balance + cte.val\n" +
                " \t  \t  \t else escrowed_balance end\n" +
                " \t  \t from\n" +
                " \t  \t  \t cte returning user_id, available_balance,\n" +
                " \t  \t  \t escrowed_balance, case when AVAILABLE_BALANCE >= cte.val then TRUE else FALSE end had_funds where user_id=?"

        /** cant use a CTE in yugabyte (yet) so I have to pass the jdbc param an excessive amount of times **/
        val LOCK_FUNDS = "update\n" +
                " \t acct_balance\n" +
                "set\n" +
                " \t available_balance =\n" +
                " \t case\n" +
                " \t  \t when AVAILABLE_BALANCE >= ? then available_balance - ?\n" +
                " \t  \t else available_balance end,\n" +
                " \t  \t escrowed_balance =\n" +
                " \t  \t case\n" +
                " \t  \t  \t when available_balance >= ? then escrowed_balance + ?\n" +
                " \t  \t  \t else escrowed_balance end returning user_id, available_balance,\n" +
                " \t  \t  \t escrowed_balance,\n" +
                " \t  \t  \t case\n" +
                " \t  \t  \t  \t when AVAILABLE_BALANCE >= ? then TRUE\n" +
                " \t  \t  \t  \t else FALSE end had_funds where user_id = ?"

        val UNLOCK_FUNDS = "update\n" +
                " \t acct_balance\n" +
                "set\n" +
                " \t available_balance =\n" +
                " \t case\n" +
                " \t  \t when ESCROWED_BALANCE >= ? then AVAILABLE_BALANCE + ?\n" +
                " \t  \t else AVAILABLE_BALANCE end,\n" +
                " \t  \t ESCROWED_BALANCE =\n" +
                " \t  \t case\n" +
                " \t  \t  \t when ESCROWED_BALANCE >= ? then ESCROWED_BALANCE - ?\n" +
                " \t  \t  \t else ESCROWED_BALANCE end returning user_id, available_balance,\n" +
                " \t  \t  \t escrowed_balance,\n" +
                " \t  \t  \t case\n" +
                " \t  \t  \t  \t when ESCROWED_BALANCE >= ? then TRUE\n" +
                " \t  \t  \t  \t else FALSE end had_funds where user_id = ?"

    }

    object BalanceResultSetHandler : ResultSetHandler<Balance> {
        override fun handle(rs: ResultSet?): Balance {
            return Balance.newBuilder()
                    .setAvailableBalance(rs?.getInt("AVAILABLE_BALANCE") ?: 0)
                    .setEscrowedBalance(rs?.getInt("ESCROWED_BALANCE") ?: 0)
                    .build()
        }
    }

    object ChangeAwareBalanceResultHandler : ResultSetHandler<Pair<Balance, Boolean>> {
        override fun handle(rs: ResultSet?): Pair<Balance, Boolean> {
            return Pair(Balance.newBuilder()
                    .setAvailableBalance(rs?.getInt("AVAILABLE_BALANCE") ?: 0)
                    .setEscrowedBalance(rs?.getInt("ESCROWED_BALANCE") ?: 0)
                    .build(), rs?.getBoolean("HAD_FUNDS") ?: false)
        }
    }

    override fun getBalance(userId: String): Balance {
        return queryRunner.query(GET_BALANCE
                , BalanceResultSetHandler, userId);
    }


    override fun directDepositIntoEscrow(userId: String, amount: Int, entityId: String): CashierActionResult {
        return handleDeposit(isSafe = false, userId = userId, amount = amount, entityId = entityId)
    }

    override fun directDepositIntoAvailable(userId: String, amount: Int, entityId: String): CashierActionResult {
        return handleDeposit(isSafe = true, userId = userId, amount = amount, entityId = entityId)
    }

    override fun unlockFunds(userId: String, amount: Int, reason: TransactionReason, entityId: String): CashierActionResult {
        return handleLockingOrUnlockingFunds(userId, amount, reason, locking = false, entityId = entityId);
    }

    override fun lockFunds(userId: String, amount: Int, reason: TransactionReason, entityId: String): CashierActionResult {
        return handleLockingOrUnlockingFunds(userId, amount, reason, locking = true, entityId = entityId);
    }


    private fun handleLockingOrUnlockingFunds(userId: String, amount: Int, reason: TransactionReason, locking: Boolean, entityId: String): CashierActionResult {
        val resultBuilder = CashierActionResult.newBuilder()
        try {
            val query = if (locking) LOCK_FUNDS else UNLOCK_FUNDS
            val balanceChangedPairs = queryRunner.execute(query, ChangeAwareBalanceResultHandler, amount, amount, amount, amount, userId)
            when (balanceChangedPairs.size) {
                0 -> {
                    resultBuilder.setStatus(CashierActionStatus.FAILURE_USER_NOT_FOUND)
                }
                1 -> {
                    val (balance, hadSufficientFunds) = balanceChangedPairs.first()
                    resultBuilder.setBalance(balance)
                    if (hadSufficientFunds) {
                        resultBuilder.setStatus(CashierActionStatus.SUCCESS)
                        queryRunner.execute(INSERT_TXN_LEDGER, reason.name, entityId, userId)
                        queryRunner.dataSource.connection.commit()
                    } else {
                        resultBuilder.setStatus(CashierActionStatus.FAILURE_INSUFFICIENT_FUNDS)
                    }
                }
            }
        } catch (e: SQLException) {
            log.error("Exception encountered while attempting to lock funds: {}", e)
            resultBuilder.setStatus(CashierActionStatus.FAILURE_INTERNAL_ERROR)
            queryRunner.dataSource.connection.rollback()
        }
        return resultBuilder.build()
    }

    private fun handleDeposit(isSafe: Boolean, userId: String, amount: Int, entityId: String): CashierActionResult {
        val resultBuilder = CashierActionResult.newBuilder()
        try {
            val balances = queryRunner.execute(if (isSafe) SAFE_DEPOSIT else RISKY_DEPOSIT, BalanceResultSetHandler, amount, userId)
            when (balances.size) {
                0 -> {
                    resultBuilder.setStatus(CashierActionStatus.FAILURE_USER_NOT_FOUND)
                }
                1 -> {
                    val reason = if (isSafe) TransactionReason.SAFE_DEPOSIT else TransactionReason.RISKY_DEPOSIT
                    queryRunner.execute(INSERT_TXN_LEDGER, reason.name, entityId, userId)
                    queryRunner.dataSource.connection.commit()
                    val balance = balances.first()
                    resultBuilder.setStatus(CashierActionStatus.SUCCESS)
                            .setBalance(balance)
                }
            }
        } catch (e: SQLException) {
            log.error("Exception encountered while handling risky deposit: {}", e)
            queryRunner.dataSource.connection.rollback()
            resultBuilder.setStatus(CashierActionStatus.FAILURE_INTERNAL_ERROR);
        }
        return resultBuilder.build()
    }

}