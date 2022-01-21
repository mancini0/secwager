package com.secwager.market

import com.secwager.matchengine.DepthPublisher
import com.secwager.matchengine.TradePublisher
import com.secwager.proto.Market
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord


class MarketDataPublisher : DepthPublisher, TradePublisher {

    private val kafkaProducer: KafkaProducer<String,Market.Quote>
    val marketData : MutableMap<String, Market.Quote>

    //The producer should be set to conflate market data changes by symbol (i.e, via linger.ms)
    constructor(kafkaProducer: KafkaProducer<String,Market.Quote>){
        this.kafkaProducer=kafkaProducer
        this.marketData = mutableMapOf()
    }


    override fun onTrade(lastTrade: Market.LastTrade) {
        marketData.put(lastTrade.isin,marketData.get(lastTrade.isin)?.toBuilder()?.setLastTrade(lastTrade)?.build() ?:
        Market.Quote.newBuilder().setIsin(lastTrade.isin).setLastTrade(lastTrade).build())
        kafkaProducer.send(ProducerRecord("market-data", lastTrade.isin, marketData[lastTrade.isin]))
    }

    override fun onDepthChange(depth: Market.Depth) {
        marketData.put(depth.isin, marketData.get(depth.isin)?.toBuilder()?.setDepth(depth)?.build() ?:
        Market.Quote.newBuilder().setIsin(depth.isin).setDepth(depth).build())
        kafkaProducer.send(ProducerRecord("market-data", depth.isin, marketData[depth.isin]))
    }

}