package com.secwager.cashier.di

import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.CacheWriter
import com.github.benmanes.caffeine.cache.Caffeine
import com.github.benmanes.caffeine.cache.RemovalCause
import com.github.jasync.sql.db.Connection
import com.github.jasync.sql.db.postgresql.PostgreSQLConnectionBuilder
import com.secwager.dao.cashier.CashierDao
import com.secwager.dao.cashier.CashierDaoJasyncImpl
import com.secwager.proto.cashier.CashierOuterClass.Balance

import dagger.Module
import dagger.Provides
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.transform
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Singleton
import javax.sql.DataSource


@Module
class CashierModule {
    @Provides
    @Singleton
    fun provideConnectionPool(): Connection {
        return PostgreSQLConnectionBuilder.createConnectionPool {
            password = ""
            host = ""
            username = ""
            database = "secwager"
            connectionCreateTimeout = 10000
            port = 77
        }
    }

    
    @Provides
    @Singleton
    fun provideCashierDao(connection: Connection): CashierDao {
        return CashierDaoJasyncImpl(connection)
    }


}