package com.secwager.market

import com.secwager.matchengine.OrderBook
import com.secwager.proto.Market
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.TopicPartition
import java.time.Duration
import java.util.*

fun main() {
    val props = Properties()
    props.put("bootstrap.servers", System.getenv("bootstrap.servers") ?: "localhost:9092");
    props.put("group.id", "secwager-market");
    props.put("enable.auto.commit", "false");
    props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
    props.put("value.deserializer", "com.secwager.serdes.OrderProtoDeserializer");

    val orderConsumer = KafkaConsumer<String, Market.Order>(props)
    val instanceNumber = Integer.parseInt(System.getenv("instance.number"))
    val orderTopic = TopicPartition("order-inbound", instanceNumber)
    orderConsumer.assign(setOf(orderTopic))

}

