package com.secwager.cashier

import org.junit.*
import org.testcontainers.containers.ExecInContainerPattern.execInContainer
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.PostgreSQLContainer
import java.sql.DriverManager

class CashierRepoTest {

    //workaround for Kotlin TestContainers compatibility
    // class KPostgreSQLContainer : PostgreSQLContainer<KPostgreSQLContainer>() {}

    //@get: ClassRule
    //val postgres: KPostgreSQLContainer = KPostgreSQLContainer().withDatabaseName("secwager")


    @Test
    fun foo() {
        val jdbcUrl = "jdbc:tc:postgresql:12.3:///secwager?TC_INITFUNCTION=com.secwager.database.Initializer::initializeDatabase"
        Class.forName("org.postgresql.Driver")
        val conn = DriverManager.getConnection(jdbcUrl)
        val stmt = conn.prepareStatement("SELECT * FROM foo");
        val rs = stmt.executeQuery()
        println(rs);

    }
}
