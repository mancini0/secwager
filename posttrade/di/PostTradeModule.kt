package secwager.posttrade.di

import dagger.Module
import org.apache.kafka.clients.consumer.KafkaConsumer
import java.util.Properties

@Module
class PostTradeModule {

    fun provideKafkaClient() : KafkaConsumer<String,ByteArray> {
        val props = Properties()
        props.setProperty("bootstrap.servers", "localhost:9092")
        props.setProperty("group.id", "test")
        props.setProperty("enable.auto.commit", "true")
        props.setProperty("auto.commit.interval.ms", "1000")
        props.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
        props.setProperty("value.deserializer", "org.apache.kafka.common.serialization.ByteArrayDeserializer")
        return  KafkaConsumer(props)

    }


}