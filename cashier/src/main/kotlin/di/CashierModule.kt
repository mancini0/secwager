package com.secwager.cashier.di

import com.secwager.cashier.CashierRepo
import com.secwager.cashier.CashierRepoJdbcImpl
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import dagger.Module
import dagger.Provides
import org.apache.commons.dbutils.QueryRunner
import org.postgresql.ds.PGSimpleDataSource
import javax.inject.Singleton
import javax.sql.DataSource

@Module
class CashierModule {
    @Provides
    @Singleton
    fun provideDataSource(): DataSource {
        val config = HikariConfig()
        config.isAutoCommit = false
        config.dataSourceClassName = PGSimpleDataSource::class.java.name
        config.jdbcUrl = System.getenv("JDBC_URL")
        config.username = System.getenv("DB_USER")
        config.password = System.getenv("DB_PASSWORD")
        return HikariDataSource(config)
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