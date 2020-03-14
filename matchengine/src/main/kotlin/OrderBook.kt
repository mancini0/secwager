package com.secwager.matchengine

import com.secwager.dto.*
import com.secwager.dto.Order
import com.secwager.proto.Market
import com.secwager.proto.Market.Order.OrderType.*
import com.secwager.proto.Market.Order.RejectedReason.*
import com.secwager.proto.Market.Order.State.*
import com.secwager.proto.Market.Depth
import com.secwager.utils.ConversionUtils.Companion.orderToProto
import java.util.*

class OrderBook {
    private val symbol: String
    private val restingBuys: TreeMap<Int, MutableList<Order>>
    private val restingSells: TreeMap<Int, MutableList<Order>>
    private var orderArena: MutableMap<String, Order>
    private var maxBid = 0
    private var minAsk = 0
    private val orderEventPublisher: OrderEventPublisher
    private val tradePublisher: TradePublisher
    private val depthPublisher: DepthPublisher
    private var callBacks: MutableList<() -> Any>
    private val callbackExecutor: CallbackExecutor
    private var needsMarketDataPublish: Boolean = false;

    constructor(symbol: String,
                depthPublisher: DepthPublisher,
                tradePublisher: TradePublisher,
                orderEventPublisher: OrderEventPublisher,
                callbackExecutor: CallbackExecutor) {
        this.symbol = symbol
        this.restingBuys = TreeMap(Collections.reverseOrder())
        this.restingSells = TreeMap()
        this.maxBid = 0
        this.orderArena = mutableMapOf()
        this.minAsk = 0
        this.orderEventPublisher = orderEventPublisher
        this.tradePublisher = tradePublisher
        this.depthPublisher = depthPublisher
        this.callBacks = mutableListOf()
        this.callbackExecutor=callbackExecutor
    }

    private fun executeTrade(buy: com.secwager.dto.Order, sell: com.secwager.dto.Order, price: Int, size: Int) {
        if (size == 0) return
        for (order in listOf(buy, sell)) {
            order.qtyOnMarket -= size
            order.qtyFilled += size
            order.status = if (order.qtyOnMarket > 0) OPEN else FILLED
        }
        buy.fills.add(Match(orderId = sell.id,traderId = sell.traderId,price=price, qty=size))
        sell.fills.add(Match(orderId = buy.id,traderId = buy.traderId,price=price, qty=size))

        val buyProto = orderToProto(buy)
        val sellProto = orderToProto(sell)
        callBacks.add { orderEventPublisher.onFill(buy = buyProto, sell = sellProto) }
        needsMarketDataPublish=true
        callBacks.add { tradePublisher.onTrade(Market.LastTrade.newBuilder().setIsin(this.symbol).setPrice(price).setQty(size).build())}
    }

    private fun handleBuy(incomingOrder: Order) {
        if (incomingOrder.price > maxBid || maxBid == 0) maxBid = incomingOrder.price
        val agreeableAsks = restingSells.navigableKeySet().tailSet(minAsk).iterator()
        prices@ for (price in agreeableAsks) {
            val ordersThisLevel = restingSells.get(price)?.iterator() ?: continue
            price@ for (restingOrder in ordersThisLevel) {
                when {
                    restingOrder.qtyOnMarket == incomingOrder.qtyOnMarket -> {
                        executeTrade(buy = incomingOrder,
                                sell = restingOrder, price = price, size = incomingOrder.qtyOnMarket)
                        ordersThisLevel.remove()
                        if (!ordersThisLevel.hasNext()) agreeableAsks.remove()
                        break@price
                    }
                    restingOrder.qtyOnMarket < incomingOrder.qtyOnMarket -> {
                        executeTrade(buy = incomingOrder,
                                sell = restingOrder, price = price, size = restingOrder.qtyOnMarket)
                        ordersThisLevel.remove()
                        if (!ordersThisLevel.hasNext()) agreeableAsks.remove()
                        continue@price
                    }
                    restingOrder.qtyOnMarket > incomingOrder.qtyOnMarket -> {
                        executeTrade(buy = incomingOrder,
                                sell = restingOrder, price = price, size = incomingOrder.qtyOnMarket)
                        break@price
                    }
                }
            }
            if (incomingOrder.qtyOnMarket == 0) break@prices
        }
        if (incomingOrder.qtyOnMarket > 0) {
            restingBuys.getOrPut(incomingOrder.price, { mutableListOf() }).add(incomingOrder)
            needsMarketDataPublish=true;
        }
        orderArena.put(incomingOrder.id, incomingOrder)
    }

    private fun handleSell(incomingOrder: Order) {
        if (incomingOrder.price < minAsk || minAsk == 0) minAsk = incomingOrder.price
        val agreeableBids = restingBuys.navigableKeySet().tailSet(maxBid).iterator()
        prices@ for (price in agreeableBids) {
            val ordersThisLevel = restingBuys.get(price)?.iterator() ?: continue
            price@ for (restingOrder in ordersThisLevel) {
                when {
                    restingOrder.qtyOnMarket == incomingOrder.qtyOnMarket -> {
                        executeTrade(buy = restingOrder,
                                sell = incomingOrder, price = price, size = incomingOrder.qtyOnMarket)
                        ordersThisLevel.remove()
                        if (!ordersThisLevel.hasNext()) agreeableBids.remove()
                        break@price
                    }
                    restingOrder.qtyOnMarket < incomingOrder.qtyOnMarket -> {
                        executeTrade(buy = restingOrder,
                                sell = incomingOrder, price = price, size = restingOrder.qtyOnMarket)
                        ordersThisLevel.remove()
                        if (!ordersThisLevel.hasNext()) agreeableBids.remove()
                        continue@price
                    }
                    restingOrder.qtyOnMarket > incomingOrder.qtyOnMarket -> {
                        executeTrade(buy = restingOrder,
                                sell = incomingOrder, price = price, size = incomingOrder.qtyOnMarket)
                        break@price
                    }
                }
            }
            if (incomingOrder.qtyOnMarket == 0) break@prices
        }
        if (incomingOrder.qtyOnMarket > 0) {
            restingSells.getOrPut(incomingOrder.price, { mutableListOf() }).add(incomingOrder)
            needsMarketDataPublish=true
        }
        orderArena.put(incomingOrder.id, incomingOrder)
    }

    private fun measureDepth() : Depth {
        val depthBuilder = Depth.newBuilder().setIsin(this.symbol)
        restingBuys.entries.forEach{depthBuilder.addBidPrices(it.key); depthBuilder.addBidQtys(it.value.sumBy { it.qtyOnMarket})}
        restingSells.entries.forEach{depthBuilder.addAskPrices(it.key); depthBuilder.addAskQtys(it.value.sumBy { it.qtyOnMarket})}
        return depthBuilder.build()
    }



    private fun handleCancel(orderId: String) {
        orderArena.get(orderId)?.let {
            when (it.status) {
                OPEN -> {
                    it.status = CANCELLED
                    it.qtyOnMarket = 0
                    val resting = if (it.orderType==BUY) restingBuys else restingSells
                    resting.get(it.price)?.removeIf { it.id == orderId }
                    resting.get(it.price)?.run { if (this.isEmpty()) resting.remove(it.price) }
                    val cancelledProto = orderToProto(it);
                    callBacks.add { orderEventPublisher.onCancel(cancelledProto) }
                    needsMarketDataPublish=true
                }
                FILLED -> {
                    callBacks.add { orderEventPublisher.onCancelReject(orderId, ALREADY_FILLED)}
                }
                CANCELLED -> {
                    callBacks.add { orderEventPublisher.onCancelReject(orderId, ALREADY_CANCELLED) }
                }
            }
            return
        }
        callBacks.add { orderEventPublisher.onCancelReject(orderId, ORDER_NOT_FOUND) }
    }


    fun submit(order: Order) {
        needsMarketDataPublish=false;
        when (order.orderType) {
            BUY -> handleBuy(order)
            SELL -> handleSell(order)
            CANCEL -> handleCancel(order.id)
        }
        doHousekeeping()
    }


    private fun doHousekeeping(){
        if(!restingSells.isEmpty()) minAsk = restingSells.firstKey() else minAsk=0
        if(!restingBuys.isEmpty()) maxBid = restingBuys.firstKey() else maxBid=0
        if(needsMarketDataPublish){
            callBacks.add{depthPublisher.onDepthChange(measureDepth())}
            callBacks.add{depthPublisher.onMarketDataChange(this.symbol)}
        }
        callbackExecutor.executeCallbacks(callBacks)
        callBacks.clear()
    }

}