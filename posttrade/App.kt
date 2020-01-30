package secwager.posttrade

import com.secwager.Market
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.kstream.Consumed
import org.apache.kafka.common.serialization.*;
import org.apache.kafka.streams.kstream.KTable
import com.secwager.serdes.*
import org.apache.kafka.streams.KafkaStreams
import org.apache.kafka.streams.kstream.Materialized
import java.util.*


fun main() {
    val streamsBuilder = StreamsBuilder()
    val depthTable: KTable<String, Market.DepthBook> = streamsBuilder
            .table<String, Market.DepthBook>("market-data-depth", Consumed.with(Serdes.String(),
                    Serdes.serdeFrom(DepthBookProtoSerializer(),DepthBookProtoDeserializer())), Materialized.`as`("depth-table"))

    val props = Properties()
    props["bootstrap.servers"] = "localhost:9092"
    props["application.id"] = "posttrade"
    val streams = KafkaStreams(streamsBuilder.build(), props)
    streams.start()
    println("store name:" +depthTable.queryableStoreName())
}