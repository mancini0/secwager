package secwager.posttrade

import com.secwager.Market.*
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

    val depth: KTable<String, DepthBook> = streamsBuilder
            .table<String, DepthBook>("market-data-depth", Consumed.with(Serdes.String(),
                    Serdes.serdeFrom(DepthBookProtoSerializer(),DepthBookProtoDeserializer())), Materialized.`as`("depth"))

    val trades: KTable<String, LastTrade> = streamsBuilder
            .table<String, LastTrade>("market-data-matched-trades", Consumed.with(Serdes.String(),
                    Serdes.serdeFrom(LastTradeProtoSerializer(),LastTradeProtoDeserializer())), Materialized.`as`("trades"))

    depth.outerJoin(trades,  {depth, trade ->  Quote.newBuilder().setSymbol(depth.symbol).setDepth(depth).setLastTrade(trade).build()})

    val props = Properties()
    props["bootstrap.servers"] = "localhost:9092"
    props["application.id"] = "posttrade-marketdata"
    val streams = KafkaStreams(streamsBuilder.build(), props)
    streams.start()
}