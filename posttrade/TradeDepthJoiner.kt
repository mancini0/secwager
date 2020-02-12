package secwager.posttrade.quotes

import com.secwager.Market
import com.secwager.serdes.*
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.KafkaStreams
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.Topology
import org.apache.kafka.streams.kstream.Consumed
import org.apache.kafka.streams.kstream.KTable
import org.apache.kafka.streams.kstream.Produced
import java.util.*


fun main() {
    val streams = KafkaStreams(topology(), props())
    streams.start()
}


fun props(): Properties {
    val props = Properties()
    props["bootstrap.servers"] = System.getenv("bootstrap.servers") ?: "localhost:9092"
    props["application.id"] = "marketdata-joiner"
    props["commit.interval.ms"] = System.getenv("commit.interval.ms") ?: 1000
    return props
}


fun topology(): Topology {
    val streamsBuilder = StreamsBuilder()

    val depth: KTable<String, Market.DepthBook> = streamsBuilder
            .table<String, Market.DepthBook>("depth", Consumed.with(Serdes.String(),
                    Serdes.serdeFrom(DepthBookProtoSerializer(), DepthBookProtoDeserializer())))

    val trades: KTable<String, Market.LastTrade> = streamsBuilder
            .table<String, Market.LastTrade>("trades", Consumed.with(Serdes.String(),
                    Serdes.serdeFrom(LastTradeProtoSerializer(), LastTradeProtoDeserializer())))

    depth.outerJoin(trades, { depth, trade -> Market.Quote.newBuilder().setSymbol(depth.symbol).setDepth(depth).setLastTrade(trade).build() })
            .toStream().to("quotes", Produced.with(Serdes.String(), Serdes.serdeFrom(QuoteProtoSerializer(), QuoteProtoDeserializer())))

    return streamsBuilder.build()

}
