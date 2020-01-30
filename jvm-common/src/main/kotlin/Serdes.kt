package com.secwager.serdes
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