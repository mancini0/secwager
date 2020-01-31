package com.secwager.serdes
import com.secwager.Market.LastTrade
import org.apache.kafka.common.serialization.Serializer
import com.secwager.Market.DepthBook
import org.apache.kafka.common.serialization.Deserializer


class DepthBookProtoSerializer : Serializer<DepthBook> {

    override fun serialize(topic:String?, depthBook: DepthBook) : ByteArray {
        return depthBook.toByteArray()
    }
}

class DepthBookProtoDeserializer : Deserializer<DepthBook> {

    override fun deserialize(topic: String?, bytes: ByteArray): DepthBook {
        return DepthBook.parseFrom(bytes)
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