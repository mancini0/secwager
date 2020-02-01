package secwager.posttrade.quotes

import com.secwager.Market.*
import com.secwager.serdes.DepthBookProtoSerializer
import com.secwager.serdes.LastTradeProtoSerializer
import com.secwager.serdes.QuoteProtoDeserializer
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.KeyValue
import org.apache.kafka.streams.TopologyTestDriver
import org.junit.Test
import org.junit.Assert.assertEquals

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
    fun latestTradesShouldJoinToDepth() {
        val latestTrade = LastTrade.newBuilder().setSymbol("IBM").setPrice(137).setQty(10).build()
        val earlierTrade = LastTrade.newBuilder().setSymbol("IBM").setPrice(135).setQty(7).build()
        val currentDepth = DepthBook.newBuilder().setSymbol("IBM")
                .addAllAskPrices(listOf(100,101,102)).addAllAskQtys(listOf(500,250,1000)).build()
        tradesTopic.pipeInput("IBM", earlierTrade )
        tradesTopic.pipeInput("IBM", latestTrade)
        depthTopic.pipeInput("IBM",currentDepth)
        println(outputTopic.queueSize)
        assertEquals(outputTopic.readKeyValue(), KeyValue("IBM", Quote.newBuilder().setLastTrade(latestTrade)
                .setDepth(currentDepth).build()))
    }


}