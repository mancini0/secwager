package com.secwager.market

import com.secwager.dto.Order
import com.secwager.matchengine.OrderBook
import com.secwager.proto.Market
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.common.TopicPartition
import java.time.Duration
import java.util.*

fun main() {
    val instanceNumber = Integer.parseInt(System.getenv("POD_NAME_WITH_ORDINAL")?.takeLast(1))
    val bootstrapServers =System.getenv("bootstrap.servers") ?: "localhost:9092";

    val orderConsumerProps = Properties()
    orderConsumerProps.put("bootstrap.servers", bootstrapServers);
    orderConsumerProps.put("group.id", "secwager-market")
    orderConsumerProps.put("enable.auto.commit", "false");
    orderConsumerProps.put("isolation.level", "read_committed");
    orderConsumerProps.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
    orderConsumerProps.put("value.deserializer", "com.secwager.serdes.OrderProtoDeserializer");

    val marketDataProducerProps = Properties()
    marketDataProducerProps.put("bootstrap.servers", bootstrapServers);
    marketDataProducerProps.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
    marketDataProducerProps.put("value.serializer", "com.secwager.serdes.QuoteProtoSerializer");
    marketDataProducerProps.put("acks",0)
    marketDataProducerProps.put("linger.ms",500)

    val orderEventProducerProps = Properties()
    orderEventProducerProps.put("bootstrap.servers", bootstrapServers);
    orderEventProducerProps.put("transactional.id", "secwager-ordereventproducer-${instanceNumber}")
    orderEventProducerProps.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
    orderEventProducerProps.put("value.serializer", "com.secwager.serdes.OrderProtoSerializer");
    marketDataProducerProps.put("linger.ms",500)


    val orderKafkaConsumer = KafkaConsumer<String, Market.Order>(orderConsumerProps)
    val orderTopic = TopicPartition("order-inbound", instanceNumber)
    orderKafkaConsumer.assign(setOf(orderTopic))

    val marketDataKafkaProducer = KafkaProducer<String,Market.Quote>(marketDataProducerProps)
    val orderEventKafkaProducer = KafkaProducer<String,Market.Order>(orderEventProducerProps)
    orderEventKafkaProducer.initTransactions();

    val booksBySymbol = mutableMapOf<String,OrderBook>()
    val marketDataPublisher = MarketDataPublisher(marketDataKafkaProducer)
    val orderEventPublisher = OrderEventPublisherImpl(orderEventKafkaProducer)

        for(o in orderKafkaConsumer.poll(Duration.ofSeconds(5))){
            val book = booksBySymbol.getOrPut(o.key(),
                    {OrderBook(tradePublisher =marketDataPublisher,
                            orderEventPublisher =orderEventPublisher, symbol=o.key())})
            val orderProto = o.value()
            val orderDto = Order( id=orderProto.orderId, orderType= orderProto.orderType, symbol=orderProto.isin,
                qtyOnMarket=orderProto.qtyOnMarket, price=orderProto.price, traderId=orderProto.traderId)
            book.submit(orderDto)
        }
    }