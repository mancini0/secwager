package com.secwager.serdes

import com.secwager.proto.Market.Depth
import com.secwager.proto.Market.Order
import com.secwager.proto.Market.Quote
import com.secwager.proto.Market.LastTrade
import org.apache.kafka.common.serialization.Deserializer
import org.apache.kafka.common.serialization.Serializer

//So redundant

class DepthProtoSerializer : Serializer<Depth> {

    override fun serialize(topic:String?, depth: Depth) : ByteArray {
        return depth.toByteArray()
    }
}

class DepthProtoDeserializer : Deserializer<Depth> {

    override fun deserialize(topic: String?, bytes: ByteArray): Depth {
        return Depth.parseFrom(bytes)
    }
}


class OrderProtoSerializer : Serializer<Order> {

    override fun serialize(topic:String?, order: Order) : ByteArray {
        return order.toByteArray()
    }
}

class OrderProtoDeserializer : Deserializer<Order> {

    override fun deserialize(topic: String?, bytes: ByteArray): Order {
        return Order.parseFrom(bytes)
    }
}


class LastTradeProtoSerializer : Serializer<LastTrade> {

    override fun serialize(topic:String?, lastTrade: LastTrade) : ByteArray {
        return lastTrade.toByteArray()
    }
}

class LastTradeProtoDeserializer : Deserializer<LastTrade> {

    override fun deserialize(topic: String?, bytes: ByteArray): LastTrade {
        return LastTrade.parseFrom(bytes)
    }
}


class QuoteProtoSerializer : Serializer<Quote> {

    override fun serialize(topic:String?, quote: Quote) : ByteArray {
        return quote.toByteArray()
    }
}

class QuoteProtoDeserializer : Deserializer<Quote> {

    override fun deserialize(topic: String?, bytes: ByteArray): Quote {
        return Quote.parseFrom(bytes)
    }
}