package `quote-maker`

import com.secwager.Market.*
import com.secwager.serdes.DepthBookProtoSerializer
import com.secwager.serdes.LastTradeProtoSerializer
import com.secwager.serdes.QuoteProtoDeserializer
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.TopologyTestDriver
import org.junit.Assert.assertEquals
import org.junit.Test

class TradeDepthJoinerTest {

    companion object {
        val testDriver  = TopologyTestDriver(topology(), props())
        val tradesTopic = testDriver.createInputTopic("trades", Serdes.String().serializer(),
                LastTradeProtoSerializer());
        val depthTopic = testDriver.createInputTopic("depth", Serdes.String().serializer(),
                DepthBookProtoSerializer());

        val outputTopic = testDriver.createOutputTopic("quotes", Serdes.String().deserializer(),
                QuoteProtoDeserializer());

    }

    @Test
    fun latestTradeShouldJoinToDepth() {
        val earlierTrade = LastTrade.newBuilder().setIsin("IBM").setPrice(135).setQty(7).build()
        val latestTrade = LastTrade.newBuilder().setIsin("IBM").setPrice(137).setQty(10).build()
        val currentDepth = DepthBook.newBuilder().setIsin("IBM")
                .addAllAskPrices(listOf(100,101,102)).addAllAskQtys(listOf(500,250,1000)).build()

        tradesTopic.pipeInput("IBM", earlierTrade )
        tradesTopic.pipeInput("IBM", latestTrade)
        depthTopic.pipeInput("IBM",currentDepth)
        val expectedQuote = Quote.newBuilder().setIsin("IBM").setLastTrade(latestTrade)
                .setDepth(currentDepth).build()
        assertEquals(outputTopic.readKeyValuesToMap()["IBM"], expectedQuote)
    }


}