package com.secwager.orderentry.di

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.secwager.emergency.EmergencyService
import com.secwager.emergency.EmergencyServiceLoggingImpl

import com.secwager.proto.cashier.CashierGrpcKt
import dagger.Module
import dagger.Provides
import io.grpc.Channel
import io.grpc.ManagedChannelBuilder
import org.apache.kafka.clients.producer.KafkaProducer
import java.util.*
import javax.inject.Named
import javax.inject.Singleton

@Module
class OrderEntryModule {
    @Singleton
    @Provides
    @Named("cashierChannel")
    fun cashierGrpcChannel(): Channel {
        return ManagedChannelBuilder
                .forAddress("cashier-grpc", 9305)
                .usePlaintext()
                .build()
    }

    @Singleton
    @Provides
    fun provideKafkaOrderProducer(): KafkaProducer<String, ByteArray> {
        val props = Properties()
        props["bootstrap.servers"] = System.getenv("KAFKA_BOOTSTRAP_SERVERS")
        props["key.serializer"] = "org.apache.kafka.common.serialization.StringSerializer"
        props["value.serializer"] = "org.apache.kafka.common.serialization.ByteArraySerializer"
        props["transactional.id"] = System.getenv("POD_NAME_WITH_ORDINAL")
        val orderProducer = KafkaProducer<String, ByteArray>(props)
        orderProducer.initTransactions()
        return orderProducer
    }

    @Provides
    @Singleton
    fun provideCashierClient(@Named("cashierChannel") channel: Channel): CashierGrpcKt.CashierCoroutineStub {
        return CashierGrpcKt.CashierCoroutineStub(channel)
    }

    @Provides
    @Singleton
    fun provideEmergencyService(): EmergencyService {
        return EmergencyServiceLoggingImpl()
    }

    @Provides
    @Singleton
    fun provideFirebase(): FirebaseApp {
        return try {
            FirebaseApp.initializeApp(FirebaseOptions.builder()
                    .setProjectId("secwager")
                    .setCredentials(GoogleCredentials.getApplicationDefault())
                    .setDatabaseUrl("https://secwager.firebaseio.com")
                    .build())
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }
}