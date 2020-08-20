package com.secwager.cashier.di

import com.github.jasync.sql.db.Connection
import com.github.jasync.sql.db.postgresql.PostgreSQLConnectionBuilder
import com.secwager.dao.cashier.CashierDao
import com.secwager.dao.cashier.CashierDaoJasyncImpl
import com.secwager.dao.cashier.CashierDaoTest

import dagger.Module
import dagger.Provides
import java.util.*
import javax.inject.Singleton
import javax.sql.DataSource


@Module
class CashierModule {
    @Provides
    @Singleton
    fun provideConnectionPool(): Connection {
        return PostgreSQLConnectionBuilder.createConnectionPool{
            password=""
            host=""
            username=""
            database="secwager"
            connectionCreateTimeout=
            port=0
        }
    }


    @Provides
    @Singleton
    fun provideCashierDao(connection: Connection): CashierDao {
        return CashierDaoJasyncImpl(connection)
    }


}