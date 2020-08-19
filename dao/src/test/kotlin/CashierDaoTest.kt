package com.secwager.dao.cashier

import com.github.jasync.sql.db.Connection
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

class CashierDaoTest {

    class KPostgreSQLContainer() : PostgreSQLContainer<KPostgreSQLContainer>() {}

    companion object {
        val log = LoggerFactory.getLogger(CashierDaoTest::class.java)
        private var dbInitialized = false
        private lateinit var cashierDao: CashierDao
        private lateinit var conn: Connection
        @get: ClassRule
        val postgres: KPostgreSQLContainer = KPostgreSQLContainer()
                .withDatabaseName("secwager")
    }


    @Before
    fun setup() {
        runBlocking{
            if (!dbInitialized) {
                postgres.start()
                val dataSource = PGSimpleDataSource();
                dataSource.setURL(postgres.jdbcUrl)
                dataSource.user = postgres.username
                dataSource.password = postgres.password
                DatabaseInitializer.initializeDatabase(dataSource)

                conn = PostgreSQLConnectionBuilder.createConnectionPool(
                        postgres.jdbcUrl)

                cashierDao = CashierDaoJasyncImpl(conn)

                dbInitialized = true
            }
            conn.sendPreparedStatement("delete from acct_balance")
            conn.sendPreparedStatement("delete from txn_ledger")

        }
    }


    @Test
    fun safeDeposit() {
        runBlocking {
            val result = cashierDao.depositIntoAvailable("addr0", 200, "tx1")
            println("deposited")
            assertThat(result).isEqualTo(CashierActionResult.newBuilder()
                    .setStatus(CashierActionStatus.SUCCESS)
                    .setBalance(Balance.newBuilder().setEscrowedBalance(0).setAvailableBalance(200))
                    .setUserId("user0")
                    .build())
        }
    }

//    @Test
//    fun riskyDeposit() {
//        val result = cashierRepo.directDepositIntoEscrow("addr0", 200, "tx1")
//        assertThat(result).isEqualTo(CashierActionResult.newBuilder()
//                .setStatus(CashierActionStatus.SUCCESS)
//                .setBalance(Balance.newBuilder().setEscrowedBalance(200).setAvailableBalance(0))
//                .setUserId("user0")
//                .build())
//    }
//
//    @Test
//    fun lockAllAvailableFundsWhenEscrowEmpty() {
//        cashierRepo.directDepositIntoAvailable("addr0", 200, "tx1")
//        val result: CashierActionResult = cashierRepo.lockFunds("user0", 200, TransactionReason.POST_MARGIN, "order0")
//        assertThat(result).isEqualTo(CashierActionResult.newBuilder()
//                .setStatus(CashierActionStatus.SUCCESS)
//                .setUserId("user0")
//                .setBalance(Balance.newBuilder().setAvailableBalance(0).setEscrowedBalance(200)).build())
//    }
//
//    @Test
//    fun lockSomeAvailableFundsWhenEscrowEmpty() {
//        cashierRepo.directDepositIntoAvailable("addr0", 200, "tx1")
//        val result: CashierActionResult = cashierRepo.lockFunds("user0", 151, TransactionReason.POST_MARGIN, "order0")
//        assertThat(result).isEqualTo(CashierActionResult.newBuilder()
//                .setStatus(CashierActionStatus.SUCCESS)
//                .setUserId("user0")
//                .setBalance(Balance.newBuilder().setAvailableBalance(49).setEscrowedBalance(151)).build())
//    }
//
//    @Test
//    fun lockSomeAvailableFundsWhenEscrowedFundsExist() {
//        cashierRepo.directDepositIntoAvailable("addr0", 200, "tx1")
//        cashierRepo.directDepositIntoEscrow("addr0", 150, "tx1")
//        val result: CashierActionResult = cashierRepo.lockFunds("user0", 5, TransactionReason.POST_MARGIN, "order0")
//        assertThat(result).isEqualTo(CashierActionResult.newBuilder()
//                .setStatus(CashierActionStatus.SUCCESS)
//                .setUserId("user0")
//                .setBalance(Balance.newBuilder().setAvailableBalance(195).setEscrowedBalance(155)).build())
//    }
//
//    @Test
//    fun lockFundsWhenInsufficientFundsAvailable() {
//        cashierRepo.directDepositIntoAvailable("addr0", 100, "tx1")
//        val result: CashierActionResult = cashierRepo.lockFunds("user0", 101, TransactionReason.POST_MARGIN, "order0")
//        assertThat(result).isEqualTo(CashierActionResult.newBuilder()
//                .setStatus(CashierActionStatus.FAILURE_INSUFFICIENT_FUNDS)
//                .setUserId("user0")
//                .setBalance(Balance.newBuilder().setAvailableBalance(100).setEscrowedBalance(0)).build())
//    }
//
//
//    @Test
//    fun unlockFunds() {
//        cashierRepo.directDepositIntoEscrow("addr0", 100, "tx1")
//        val result: CashierActionResult = cashierRepo.unlockFunds("user0", 44, TransactionReason.BET_WIN, "order0")
//        assertThat(result).isEqualTo(CashierActionResult.newBuilder()
//                .setStatus(CashierActionStatus.SUCCESS)
//                .setUserId("user0")
//                .setBalance(Balance.newBuilder().setAvailableBalance(44).setEscrowedBalance(56)).build())
//    }
//
//    @Test
//    fun unlockFundsWhenAvailableExists() {
//        cashierRepo.directDepositIntoEscrow("addr0", 100, "tx1")
//        cashierRepo.directDepositIntoAvailable("addr0", 30, "tx1")
//        val result: CashierActionResult = cashierRepo.unlockFunds("user0", 44, TransactionReason.BET_WIN, "order0")
//        assertThat(result).isEqualTo(CashierActionResult.newBuilder()
//                .setStatus(CashierActionStatus.SUCCESS)
//                .setUserId("user0")
//                .setBalance(Balance.newBuilder().setAvailableBalance(74).setEscrowedBalance(56)).build())
//    }
//
//
//    @Test
//    fun unlockFundsWhenInsufficientEscrowed() {
//        cashierRepo.directDepositIntoEscrow("addr0", 100, "tx1")
//        cashierRepo.directDepositIntoAvailable("addr0", 20, "tx1")
//        val result: CashierActionResult = cashierRepo.unlockFunds("user0", 105, TransactionReason.BET_WIN, "order0")
//        assertThat(result).isEqualTo(CashierActionResult.newBuilder()
//                .setStatus(CashierActionStatus.FAILURE_INSUFFICIENT_FUNDS)
//                .setUserId("user0")
//                .setBalance(Balance.newBuilder().setAvailableBalance(20).setEscrowedBalance(100)).build())
//    }


}
