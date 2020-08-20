package com.secwager.dao.cashier

import com.github.jasync.sql.db.Connection
import com.github.jasync.sql.db.asSuspending
import com.github.jasync.sql.db.postgresql.PostgreSQLConnectionBuilder
import com.secwager.database.DatabaseInitializer
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.ClassRule
import org.postgresql.ds.PGSimpleDataSource
import org.slf4j.LoggerFactory
import org.testcontainers.containers.PostgreSQLContainer
import org.junit.Test
import com.secwager.proto.cashier.CashierOuterClass.*
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.future.await
import java.time.LocalDateTime

class CashierDaoTest {

    class KPostgreSQLContainer() : PostgreSQLContainer<KPostgreSQLContainer>() {}

    private data class TxnLedgerRecord(val userId: String,
                                       val reason: TransactionReason, val availableBalance: Int,
                                       val escrowedBalanced: Int)

    companion object {
        val log = LoggerFactory.getLogger(CashierDaoTest::class.java)
        private var dbInitialized = false
        private lateinit var cashierDao: CashierDao
        private lateinit var conn: Connection

        @get: ClassRule
        val postgres: KPostgreSQLContainer = KPostgreSQLContainer()
                .withDatabaseName("secwager")
                .withUsername("dbuser")
                .withPassword("dbpass")
    }


    @Before
    fun setup() {
        runBlocking {
            if (!dbInitialized) {
                postgres.start()
                val dataSource = PGSimpleDataSource()
                dataSource.setURL(postgres.jdbcUrl)
                dataSource.user = postgres.username
                dataSource.password = postgres.password
                DatabaseInitializer.initializeDatabase(dataSource)
                conn = PostgreSQLConnectionBuilder.createConnectionPool("${postgres.jdbcUrl}&user=${postgres.username}&password=${postgres.password}")
                cashierDao = CashierDaoJasyncImpl(conn)
                dbInitialized = true
                conn.asSuspending.sendPreparedStatement("insert into users(user_id, pub_key, " +
                        "priv_key, p2pkh_addr) values ('user0','pubk','privk','addr0')")
            }
            conn.asSuspending.sendPreparedStatement("delete from acct_balance")
        }
    }


    @Test
    fun safeDeposit() = runBlocking {
        val result = cashierDao.depositIntoAvailable("addr0", 200, "tx1")
        assertThat(result).isEqualTo(CashierActionResult.newBuilder()
                .setStatus(CashierActionStatus.SUCCESS)
                .setBalance(Balance.newBuilder().setEscrowedBalance(0).setAvailableBalance(200))
                .setUserId("user0")
                .build())
    }


    @Test
    fun riskyDeposit() = runBlocking {
        val result = cashierDao.depositIntoEscrow("addr0", 200, "tx1")
        assertThat(result).isEqualTo(CashierActionResult.newBuilder()
                .setStatus(CashierActionStatus.SUCCESS)
                .setBalance(Balance.newBuilder().setEscrowedBalance(200).setAvailableBalance(0))
                .setUserId("user0")
                .build())
    }

    @Test
    fun lockAllAvailableFundsWhenEscrowEmpty() = runBlocking {
        cashierDao.depositIntoAvailable("addr0", 200, "tx1")
        val result: CashierActionResult = cashierDao.lockFunds("user0", 200, TransactionReason.POST_MARGIN, "order0")
        assertThat(result).isEqualTo(CashierActionResult.newBuilder()
                .setStatus(CashierActionStatus.SUCCESS)
                .setUserId("user0")
                .setBalance(Balance.newBuilder().setAvailableBalance(0).setEscrowedBalance(200)).build())
    }

    @Test
    fun lockSomeAvailableFundsWhenEscrowEmpty() = runBlocking {
        cashierDao.depositIntoAvailable("addr0", 200, "tx1")
        val result: CashierActionResult = cashierDao.lockFunds("user0", 151, TransactionReason.POST_MARGIN, "order0")
        assertThat(result).isEqualTo(CashierActionResult.newBuilder()
                .setStatus(CashierActionStatus.SUCCESS)
                .setUserId("user0")
                .setBalance(Balance.newBuilder().setAvailableBalance(49).setEscrowedBalance(151)).build())
    }

    @Test
    fun lockSomeAvailableFundsWhenEscrowedFundsExist() = runBlocking {
        cashierDao.depositIntoAvailable("addr0", 200, "tx1")
        cashierDao.depositIntoEscrow("addr0", 150, "tx1")
        val result: CashierActionResult = cashierDao.lockFunds("user0", 5, TransactionReason.POST_MARGIN, "order0")
        assertThat(result).isEqualTo(CashierActionResult.newBuilder()
                .setStatus(CashierActionStatus.SUCCESS)
                .setUserId("user0")
                .setBalance(Balance.newBuilder().setAvailableBalance(195).setEscrowedBalance(155)).build())
    }

    @Test
    fun lockFundsWhenInsufficientFundsAvailable() = runBlocking {
        cashierDao.depositIntoAvailable("addr0", 100, "tx1")
        val result: CashierActionResult = cashierDao.lockFunds("user0", 101, TransactionReason.POST_MARGIN, "order0")
        assertThat(result).isEqualTo(CashierActionResult.newBuilder()
                .setStatus(CashierActionStatus.FAILURE_INSUFFICIENT_FUNDS)
                .setUserId("user0")
                .setBalance(Balance.newBuilder().setAvailableBalance(100).setEscrowedBalance(0)).build())
    }


    @Test
    fun unlockFunds() = runBlocking {
        cashierDao.depositIntoEscrow("addr0", 100, "tx1")
        val result: CashierActionResult = cashierDao.unlockFunds("user0", 44, TransactionReason.BET_WIN, "order0")
        assertThat(result).isEqualTo(CashierActionResult.newBuilder()
                .setStatus(CashierActionStatus.SUCCESS)
                .setUserId("user0")
                .setBalance(Balance.newBuilder().setAvailableBalance(44).setEscrowedBalance(56)).build())
    }

    @Test
    fun unlockFundsWhenAvailableExists() = runBlocking {
        cashierDao.depositIntoEscrow("addr0", 100, "tx1")
        cashierDao.depositIntoAvailable("addr0", 30, "tx1")
        val result: CashierActionResult = cashierDao.unlockFunds("user0", 44, TransactionReason.BET_WIN, "order0")
        assertThat(result).isEqualTo(CashierActionResult.newBuilder()
                .setStatus(CashierActionStatus.SUCCESS)
                .setUserId("user0")
                .setBalance(Balance.newBuilder().setAvailableBalance(74).setEscrowedBalance(56)).build())
    }


    @Test
    fun unlockFundsWhenInsufficientEscrowed() = runBlocking {
        cashierDao.depositIntoEscrow("addr0", 100, "tx1")
        cashierDao.depositIntoAvailable("addr0", 20, "tx1")
        val result: CashierActionResult = cashierDao.unlockFunds("user0", 105, TransactionReason.BET_WIN, "order0")
        assertThat(result).isEqualTo(CashierActionResult.newBuilder()
                .setStatus(CashierActionStatus.FAILURE_INSUFFICIENT_FUNDS)
                .setUserId("user0")
                .setBalance(Balance.newBuilder().setAvailableBalance(20).setEscrowedBalance(100)).build())
    }

    @Test
    fun verifyLedger() = runBlocking {
        cashierDao.depositIntoEscrow("addr0", 100, "tx1") //risky deposit
        cashierDao.depositIntoAvailable("addr0", 20, "tx1")//safe deposit
        cashierDao.unlockFunds("user0", 35, TransactionReason.BET_WIN, "bet0")
        cashierDao.lockFunds("user0", 15, TransactionReason.POST_MARGIN, "bet0")
        val txnHistory = conn.asSuspending
                .sendPreparedStatement("select * from txn_ledger order by txn_time asc")

        assertThat(txnHistory.rows.map {
            TxnLedgerRecord(userId = it["user_id"] as String, escrowedBalanced = it["escrowed_balance"] as Int,
                    availableBalance = it["available_balance"] as Int, reason = TransactionReason.valueOf(it["txn_reason"] as String))
        }).containsExactly(
                TxnLedgerRecord(userId = "user0", escrowedBalanced = 100,
                        availableBalance = 0, reason = TransactionReason.RISKY_DEPOSIT),
                TxnLedgerRecord(userId = "user0", escrowedBalanced = 100,
                        availableBalance = 20, reason = TransactionReason.SAFE_DEPOSIT),
                TxnLedgerRecord(userId = "user0", escrowedBalanced = 65,
                        availableBalance = 55, reason = TransactionReason.BET_WIN),
                TxnLedgerRecord(userId = "user0", escrowedBalanced = 80,
                        availableBalance = 40, reason = TransactionReason.POST_MARGIN)
        )
        return@runBlocking
    }

}
