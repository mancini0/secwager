package com.secwager.cashier.di

import com.secwager.cashier.CashierRepo
import com.secwager.cashier.CashierRepoJdbcImpl
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import dagger.Module
import dagger.Provides
import org.apache.commons.dbutils.QueryRunner
import org.postgresql.ds.PGSimpleDataSource
import java.util.*
import javax.inject.Singleton
import javax.sql.DataSource

@Module
class CashierModule {
    @Provides
    @Singleton
    fun provideDataSource(): DataSource {
        val props = Properties()
        props.setProperty("dataSourceClassName", "org.postgresql.ds.PGSimpleDataSource")
        props.setProperty("dataSource.user", "yugabyte")
        props.setProperty("dataSource.password", "yugabyte")
        props.setProperty("dataSource.databaseName", "secwager")
        props.setProperty("dataSource.serverName", "localhost")
        props.setProperty("dataSource.portNumber", "5433")
        val config = HikariConfig(props)
        config.isAutoCommit = false
        val ds = HikariDataSource(config)
        return ds
    }

    @Provides
    @Singleton
    fun provideQueryRunner(dataSource: DataSource?): QueryRunner {
        return QueryRunner(dataSource)
    }

    @Provides
    @Singleton
    fun provideCashierRepo(queryRunner: QueryRunner): CashierRepo {
        return CashierRepoJdbcImpl(queryRunner)
    }


}