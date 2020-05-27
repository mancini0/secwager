package com.secwager.cashier

import com.secwager.database.DatabaseInitializer
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.apache.commons.dbutils.QueryRunner
import org.apache.commons.dbutils.handlers.MapListHandler
import org.junit.Before
import org.junit.ClassRule
import org.junit.Test
import org.postgresql.ds.PGSimpleDataSource
import org.slf4j.LoggerFactory
import org.testcontainers.containers.PostgreSQLContainer


class CashierRepoTest {

    class KPostgreSQLContainer() : PostgreSQLContainer<KPostgreSQLContainer>() {}
    companion object {
        val log = LoggerFactory.getLogger(CashierRepoTest::class.java)

        @get: ClassRule
        val postgres: KPostgreSQLContainer = KPostgreSQLContainer()
                .withDatabaseName("secwager")
    }

    private var dbInitialized = false
    private lateinit var cashierRepo: CashierRepo
    private lateinit var queryRunner: QueryRunner


    @Before
    fun setup() {
        if (!dbInitialized) {
            postgres.start()
            val dataSource = PGSimpleDataSource();
            dataSource.setURL(postgres.jdbcUrl)
            dataSource.user = postgres.username
            dataSource.password = postgres.password
            DatabaseInitializer.initializeDatabase(dataSource)
            
            queryRunner = QueryRunner(dataSource)
            cashierRepo = CashierRepoJdbcImpl(queryRunner)
            dbInitialized = true
        }

        queryRunner.update("delete from txn_ledger")
        queryRunner.update("delete from acct_balance")
        queryRunner.update("delete from users")
    }


    @Test
    fun safeDeposit() {
        queryRunner.execute("insert into users(user_id, pub_key, priv_key, p2pkh_addr) values ('user1','pubk','privk','addr1')")
        log.info("uuu {}", queryRunner.query("SELECT * FROM USERS", MapListHandler()))
        val result = cashierRepo.directDepositIntoAvailable("addr1", 200, "tx1")
        log.info("res is: {}", result)
    }
}
