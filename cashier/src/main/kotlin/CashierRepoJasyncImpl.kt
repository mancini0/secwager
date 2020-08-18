package com.secwager.cashier

import com.github.jasync.sql.db.Connection
import com.github.jasync.sql.db.asSuspending
import com.secwager.proto.cashier.CashierOuterClass.*
import kotlinx.coroutines.future.await

class CashierRepoJasyncImpl(val conn: Connection) : CashierRepo {
    companion object {
        val GET_BALANCE = "SELECT USER_ID, AVAILABLE_BALANCE, ESCROWED_BALANCE FROM ACCT_BALANCE WHERE USER_ID=?"
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

        val UNLOCK_FUNDS = "with cte as (\n" +
                "select\n" +
                "\t  available_balance as available , escrowed_balance as escrowed, ?::int as satoshis \n" +
                "from\n" +
                "\t  acct_balance\n" +
                "where\n" +
                "\t  user_id = ?)\n" +
                "update\n" +
                "\t  acct_balance\n" +
                "set\n" +
                "\t available_balance =\n" +
                "\t case\n" +
                "\t \t when cte.escrowed >= cte.satoshis then available_balance + cte.satoshis \n" +
                "\t \t else available_balance\n" +
                "\t end,\n" +
                "\t escrowed_balance =\n" +
                "\t case\n" +
                "\t \t when cte.escrowed >= cte.satoshis then escrowed_balance - cte.satoshis\n" +
                "\t \t else escrowed_balance\n" +
                "\t end\n" +
                "from\n" +
                "\t cte returning user_id,\n" +
                "\t available_balance,\n" +
                "\t escrowed_balance,\n" +
                "\t case when cte.escrowed >= cte.satoshis then true else false end as had_sufficent_funds"

        val LOCK_FUNDS = "with cte as (\n" +
                "select\n" +
                "\t  available_balance as available , escrowed_balance as escrowed, ?::int as satoshis \n" +
                "from\n" +
                "\t  acct_balance\n" +
                "where\n" +
                "\t  user_id = ?)\n" +
                "update\n" +
                "\t  acct_balance\n" +
                "set\n" +
                "\t available_balance =\n" +
                "\t case\n" +
                "\t \t when cte.available >= cte.satoshis then available_balance - cte.satoshis \n" +
                "\t \t else available_balance\n" +
                "\t end,\n" +
                "\t escrowed_balance =\n" +
                "\t case\n" +
                "\t \t when cte.available >= cte.satoshis then escrowed_balance + cte.satoshis\n" +
                "\t \t else escrowed_balance\n" +
                "\t end\n" +
                "from\n" +
                "\t cte returning user_id,\n" +
                "\t available_balance,\n" +
                "\t escrowed_balance,\n" +
                "\t case when cte.available >= cte.satoshis then true else false end as had_sufficent_funds"

        val INSERT_TXN_LEDGER = "insert\n" +
                " \t   into\n" +
                " \t   TXN_LEDGER(USER_ID,\n" +
                " \t   TXN_TIME,\n" +
                " \t   TXN_REASON,\n" +
                " \t   RELATED_ENTITY,\n" +
                " \t   AVAILABLE_BALANCE,\n" +
                " \t   ESCROWED_BALANCE)\n" +
                "select\n" +
                " \t   user_id,\n" +
                " \t   current_timestamp,\n" +
                " \t   ?::txn_reason,\n" +
                " \t   ?,\n" +
                " \t   AVAILABLE_BALANCE,\n" +
                " \t   ESCROWED_BALANCE \n" +
                "from\n" +
                " \t   ACCT_BALANCE where user_id=?"
    }

    override suspend fun getBalance(userId: String): CashierActionResult {
        return conn.sendPreparedStatement(GET_BALANCE, listOf(userId))
                .thenApply {
                    if (it.rows.isEmpty()) {
                        CashierActionResult.newBuilder().setUserId(userId)
                                .setStatus(CashierActionStatus.FAILURE_USER_NOT_FOUND).build()
                    } else {
                        val row = it.rows.single()
                        CashierActionResult.newBuilder().setUserId(userId)
                                .setStatus(CashierActionStatus.SUCCESS)
                                .setBalance(Balance.newBuilder()
                                        .setEscrowedBalance(row.getInt("ESCROWED_BALANCE") ?: 0)
                                        .setAvailableBalance(row.getInt("AVAILABLE_BALANCE") ?: 0)
                                        .build())
                                .build()
                    }
                }
                .exceptionally {
                    CashierActionResult.newBuilder().setUserId(userId)
                            .setStatus(CashierActionStatus.FAILURE_INTERNAL_ERROR).build()
                }
                .await()
    }

    override suspend fun depositIntoEscrow(p2pkhAddress: String, amount: Int, entityId: String): CashierActionResult {
        return handleDeposit(shouldEscrow = true, p2pkhAddress = p2pkhAddress, amount = amount, entityId = entityId)
    }

    override suspend fun depositIntoAvailable(p2pkhAddress: String, amount: Int, entityId: String): CashierActionResult {
        return handleDeposit(shouldEscrow = false, p2pkhAddress = p2pkhAddress, amount = amount, entityId = entityId);
    }


    private suspend fun handleDeposit(shouldEscrow: Boolean, p2pkhAddress: String, amount: Int, entityId: String): CashierActionResult {
        return conn.asSuspending.inTransaction { conn ->
            val depositResult = conn.sendPreparedStatement(if (shouldEscrow) RISKY_DEPOSIT else SAFE_DEPOSIT,
                    listOf(amount, p2pkhAddress))
            if (depositResult.rows.isEmpty()) {
                CashierActionResult.newBuilder()
                        .setStatus(CashierActionStatus.FAILURE_USER_NOT_FOUND).build()
            } else {
                val depositRow = depositResult.rows.single()
                val reason = if (shouldEscrow) TransactionReason.RISKY_DEPOSIT else TransactionReason.SAFE_DEPOSIT
                conn.sendPreparedStatement(INSERT_TXN_LEDGER, listOf(reason.name, entityId, p2pkhAddress))
                        .rows.single()
                CashierActionResult.newBuilder()
                        .setStatus(CashierActionStatus.SUCCESS)
                        .setBalance(Balance.newBuilder()
                                .setAvailableBalance(depositRow["AVAILABLE_BALANCE"] as Int)
                                .setEscrowedBalance(depositRow["ESCROWED_BALANCE"] as Int))
                        .setUserId(depositRow["USER_ID"] as String).build()
            }
        }
    }


    override suspend fun lockFunds(userId: String, amount: Int, reason: TransactionReason, entityId: String): CashierActionResult {
        return lockOrUnlock(isLock = true, userId = userId, amount = amount, reason = reason, entityId = entityId)
    }

    override suspend fun unlockFunds(userId: String, amount: Int, reason: TransactionReason, entityId: String): CashierActionResult {
        return lockOrUnlock(isLock = false, userId = userId, amount = amount, reason = reason, entityId = entityId)
    }


    private suspend fun lockOrUnlock(isLock: Boolean, userId: String, amount: Int, reason: TransactionReason, entityId: String): CashierActionResult {
        return conn.asSuspending.inTransaction {
            val queryResult = it.sendPreparedStatement(if (isLock) LOCK_FUNDS else UNLOCK_FUNDS, listOf(amount, userId))
            if (queryResult.rows.isEmpty()) {
                CashierActionResult.newBuilder()
                        .setStatus(CashierActionStatus.FAILURE_USER_NOT_FOUND).build()
            } else {
                val row = queryResult.rows.single()
                val resultBuilder = CashierActionResult
                        .newBuilder()
                        .setUserId(userId)
                        .setBalance(Balance.newBuilder()
                                .setAvailableBalance(row["AVAILABLE_BALANCE"] as Int)
                                .setEscrowedBalance(row["ESCROWED_BALANCE"] as Int))

                if (!(row["HAD_SUFFICIENT_FUNDS"] as Boolean)) {
                    resultBuilder.setStatus(CashierActionStatus.FAILURE_INSUFFICIENT_FUNDS).build()
                } else {
                    it.sendPreparedStatement(INSERT_TXN_LEDGER, listOf(reason, entityId, userId))
                            .rows.single()//roll everything back if ledger could not be updated
                    resultBuilder
                            .setStatus(CashierActionStatus.SUCCESS).build()
                }
            }
        }
    }
}