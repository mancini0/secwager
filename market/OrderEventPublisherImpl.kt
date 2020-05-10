package com.secwager.market

import com.secwager.matchengine.OrderEventPublisher
import com.secwager.proto.Market
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord

class OrderEventPublisherImpl : OrderEventPublisher {

    private val kafkaProducer: KafkaProducer<String,Market.Order>

    constructor(kafkaProducer: KafkaProducer<String, Market.Order>){
        this.kafkaProducer=kafkaProducer
    }



    override fun onFill(buy: Market.Order, sell: Market.Order) {
        kafkaProducer.beginTransaction()
        kafkaProducer.send(ProducerRecord("order-events", buy.orderId, buy))
        kafkaProducer.send(ProducerRecord("order-events", sell.orderId, sell))
        kafkaProducer.commitTransaction()
    }

    override fun onAccept(order: Market.Order) {
        TODO("Not yet implemented")
    }

    override fun onReject(order: Market.Order, reason: Market.Order.RejectedReason) {
        TODO("Not yet implemented")
    }

    override fun onCancel(order: Market.Order) {
        TODO("Not yet implemented")
    }

    override fun onCancelReject(orderId: String, reason: Market.Order.RejectedReason) {
        TODO("Not yet implemented")
    }
}