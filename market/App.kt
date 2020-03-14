package com.secwager.market

import com.secwager.dto.Order
import com.secwager.matchengine.OrderBook
import com.secwager.matchengine.OrderEventPublisher
import com.secwager.proto.Market
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.producer.KafkaProducer
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

    val marketDataProducerProps = Properties()
    marketDataProducerProps.put("bootstrap.servers", "localhost:9092");
    marketDataProducerProps.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
    marketDataProducerProps.put("value.serializer", "com.secwager.serdes.QuoteProtoSerializer");

    val orderEventProducerProps = Properties()
    orderEventProducerProps.put("bootstrap.servers", "localhost:9092");
    orderEventProducerProps.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
    orderEventProducerProps.put("value.serializer", "com.secwager.serdes.QuoteProtoSerializer");


    val orderConsumer = KafkaConsumer<String, Market.Order>(props)

    val marketDataProducer = KafkaProducer<String,Market.Quote>(marketDataProducerProps)
    val orderEventProducer = KafkaProducer<String,Market.Quote>(orderEventProducerProps)

    val instanceNumber = Integer.parseInt(System.getenv("instance.number"))
    val orderTopic = TopicPartition("order-inbound", instanceNumber)
    orderConsumer.assign(setOf(orderTopic))
    val lastCommit = orderConsumer.committed(setOf(orderTopic))[orderTopic]?.offset() ?: -1
    val booksBySymbol = mutableMapOf<String,OrderBook>()
    val marketDataPublisher = MarketDataPublisher(marketDataProducer)
    val orderEventPublisher = OrderEventPublisherImpl()
        for(o in orderConsumer.poll(Duration.ofSeconds(1))){
            val book = booksBySymbol.getOrPut(o.key(),
                    {OrderBook(callbackExecutor = CallbackExecutorImpl(), tradePublisher =marketDataPublisher,
                            depthPublisher = marketDataPublisher, orderEventPublisher =orderEventPublisher, symbol=o.key())})
            val orderProto = o.value()
            val orderDto = Order( id=orderProto.orderId, type=orderProto.orderType, symbol=orderProto.isin, qtyOnMarket=orderProto.qtyOnMarket, price=orderProto.price, traderId=orderProto.traderId)

            book.submit(orderDto)
        }
    }
}


