package com.secwager.refdata.contractpublisher.di


import com.secwager.refdata.RefData.Fixture
import dagger.Provides
import dagger.Module
import javax.inject.Singleton
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.common.serialization.BytesSerializer
import org.apache.kafka.common.serialization.StringSerializer
import java.util.Properties

@Module
class ContractPublisherModule {


    @Provides
    @Singleton
    fun provideProducer(): KafkaProducer<String, Fixture> {
        val props = Properties()
        props["bootstrap.servers"] = ""
        props["key.serializer"] = StringSerializer::class.java.canonicalName
        props["value.serializer"] = BytesSerializer::class.java.canonicalName
        return KafkaProducer(props)
    }
}