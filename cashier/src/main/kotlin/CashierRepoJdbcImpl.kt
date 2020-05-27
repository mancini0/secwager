package com.secwager.cashier

import com.secwager.proto.cashier.CashierOuterClass.*
import org.apache.commons.dbutils.QueryRunner
import org.apache.commons.dbutils.ResultSetHandler
import org.apache.commons.dbutils.handlers.MapListHandler
import org.slf4j.LoggerFactory
import java.sql.Connection
import java.sql.ResultSet
import java.sql.SQLException
import java.util.*
import javax.inject.Inject

class CashierRepoJdbcImpl @Inject constructor(private val queryRunner: QueryRunner) : CashierRepo {

    companion object {
        val log = LoggerFactory.getLogger(com.secwager.cashier.CashierRepoJdbcImpl::class.java)

        val GET_BALANCE = "SELECT USER_ID, AVAILABLE_BALANCE, ESCROWED_BALANCE FROM ACCT_BALANCE WHERE USER_ID=?"


        val INSERT_TXN_LEDGER = "insert\n" +
                " \t  into\n" +
                " \t  TXN_LEDGER(USER_ID,\n" +
                " \t  TXN_TIME,\n" +
                " \t  TXN_REASON,\n" +
                " \t  RELATED_ENTITY,\n" +
                " \t  AVAILABLE_BALANCE,\n" +
                " \t  ESCROWED_BALANCE)\n" +
                "select\n" +
                " \t  user_id,\n" +
                " \t  current_timestamp,\n" +
                " \t  ?::txn_reason,\n" +
                " \t  ?,\n" +
                " \t  AVAILABLE_BALANCE,\n" +
                " \t  ESCROWED_BALANCE \n" +
                "from\n" +
                " \t  ACCT_BALANCE where user_id=?"

        val RISKY_DEPOSIT = "with cte as (select user_id, ?::int as satoshis from users u where u.p2pkh_addr = ?::text)\n" +
                "insert into acct_balance (user_id ,escrowed_balance) select cte.user_id, cte.satoshis from cte\n" +
                "on conflict(user_id)\n" +
                "do update set escrowed_balance = acct_balance.escrowed_balance +(select cte.satoshis from cte)\n" +
                "returning user_id ,available_balance, escrowed_balance"

        val SAFE_DEPOSIT = "with cte as (select user_id, ?::int as satoshis from users u where u.p2pkh_addr =?)\n" +
                "insert into acct_balance (user_id ,available_balance) select cte.user_id, cte.satoshis from cte\n" +
                "on conflict(user_id)\n" +
                "do update set available_balance = acct_balance.available_balance +(select cte.satoshis from cte)\n" +
                "returning user_id ,available_balance, escrowed_balance"

    }


    override fun getBalance(userId: String): CashierActionResult {
        return queryRunner.query(GET_BALANCE
                , MapListHandler(), userId).map {
            CashierActionResult.newBuilder().setStatus(CashierActionStatus.SUCCESS)
                    .setBalance(
                            Balance.newBuilder()
                                    .setAvailableBalance(it.get("AVAILABLE_BALANCE") as Int)
                                    .setEscrowedBalance(it.get("ESCROWED_BALANCE") as Int))
                    .setUserId(it.get("USER_ID") as String).build()
        }.getOrElse(0) {
            CashierActionResult.newBuilder().setUserId(userId)
                    .setStatus(CashierActionStatus.FAILURE_USER_NOT_FOUND).build()
        }
    }


    override fun directDepositIntoEscrow(p2pkhAddress: String, amount: Int, entityId: String): CashierActionResult {
        return handleDeposit(isSafe = false, p2pkhAddress = p2pkhAddress, amount = amount, entityId = entityId)
    }

    override fun directDepositIntoAvailable(p2pkhAddress: String, amount: Int, entityId: String): CashierActionResult {
        return handleDeposit(isSafe = true, p2pkhAddress = p2pkhAddress, amount = amount, entityId = entityId)
    }

    override fun unlockFunds(userId: String, amount: Int, reason: TransactionReason, entityId: String): CashierActionResult {
        return handleLockingOrUnlockingFunds(userId, amount, reason, locking = false, entityId = entityId);
    }

    override fun lockFunds(userId: String, amount: Int, reason: TransactionReason, entityId: String): CashierActionResult {
        return handleLockingOrUnlockingFunds(userId, amount, reason, locking = true, entityId = entityId);
    }


    private fun handleLockingOrUnlockingFunds(userId: String, amount: Int, reason: TransactionReason, locking: Boolean, entityId: String): CashierActionResult {
        val resultBuilder = CashierActionResult.newBuilder().setUserId(userId)
        val conn = queryRunner.dataSource.connection
        //TODO use kotlin's 'use' facilities upon resolution of https://github.com/bazelbuild/rules_kotlin/issues/333
        // to try-with-resources stmt, conn, resultSet, etc.
        try {
            conn.autoCommit = false
            val stmt = conn.prepareStatement(GET_BALANCE, ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE, ResultSet.CLOSE_CURSORS_AT_COMMIT)
            stmt.setString(1, userId)
            val resultSet = stmt.executeQuery()
            if (resultSet.next()) {
                var available = resultSet.getInt("AVAILABLE_BALANCE")
                var escrowed = resultSet.getInt("ESCROWED_BALANCE")
                if (locking && available >= amount) {
                    available = available - amount
                    escrowed = escrowed + amount
                    resultSet.updateInt("AVAILABLE_BALANCE", available)
                    resultSet.updateInt("ESCROWED_BALANCE", escrowed)
                    queryRunner.execute(conn, INSERT_TXN_LEDGER, reason.name, entityId, userId)
                    conn.commit()
                    resultBuilder.setStatus(CashierActionStatus.SUCCESS)
                } else if (!locking && escrowed >= amount) {
                    available = available + amount
                    escrowed = escrowed - amount
                    resultSet.updateInt("AVAILABLE_BALANCE", available)
                    resultSet.updateInt("ESCROWED_BALANCE", escrowed)
                    queryRunner.execute(conn, INSERT_TXN_LEDGER, reason.name, entityId, userId)
                    conn.commit()
                    resultBuilder.setStatus(CashierActionStatus.SUCCESS)
                } else {
                    resultBuilder.setStatus(CashierActionStatus.FAILURE_INSUFFICIENT_FUNDS)
                }
                resultBuilder.setBalance(Balance.newBuilder()
                        .setAvailableBalance(available)
                        .setEscrowedBalance(escrowed))
            } else {
                resultBuilder.setStatus(CashierActionStatus.FAILURE_USER_NOT_FOUND)
            }
            resultSet.close()
            stmt.close()
        } catch (e: SQLException) {
            log.error("Exception encountered while attempting to lock/unlock funds: {}", e)
            resultBuilder.setStatus(CashierActionStatus.FAILURE_INTERNAL_ERROR)
            conn.rollback()
        } finally {
            conn.close()
        }
        return resultBuilder.build()
    }

    private fun handleDeposit(isSafe: Boolean, p2pkhAddress: String, amount: Int, entityId: String): CashierActionResult {
        val resultBuilder = CashierActionResult.newBuilder()
        val conn = queryRunner.dataSource.connection
        try {
            conn.autoCommit = false
            val result = queryRunner.query(conn, if (isSafe) SAFE_DEPOSIT else RISKY_DEPOSIT, MapListHandler(), amount, p2pkhAddress)
            when (result.size) {
                0 -> {
                    resultBuilder.setStatus(CashierActionStatus.FAILURE_USER_NOT_FOUND)
                }
                1 -> {
                    val reason = if (isSafe) TransactionReason.SAFE_DEPOSIT else TransactionReason.RISKY_DEPOSIT
                    queryRunner.execute(conn, INSERT_TXN_LEDGER, reason.name, entityId, p2pkhAddress)
                    val row = result.first()
                    resultBuilder
                            .setStatus(CashierActionStatus.SUCCESS)
                            .setBalance(Balance.newBuilder()
                                    .setAvailableBalance(row["AVAILABLE_BALANCE"] as Int)
                                    .setEscrowedBalance(row["ESCROWED_BALANCE"] as Int))
                            .setUserId(row["USER_ID"] as String)
                    conn.commit()
                }
                else -> {
                    throw IllegalStateException("impossible - deposit query returned multiple rows")
                }
            }
        } catch (e: Exception) {
            log.error("Exception encountered while handling deposit: {}", e)
            conn.rollback()
            resultBuilder.setStatus(CashierActionStatus.FAILURE_INTERNAL_ERROR);
        } finally {
            conn.close()
        }
        return resultBuilder.build()
    }
}