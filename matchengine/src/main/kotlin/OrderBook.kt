package com.secwager.matchengine

import java.util.*


data class Order(val id: String, val isBuy: Boolean, var qtyOnMarket: Int, var qtyFilled: Int = 0, val price: Int, val traderId: String)
data class Level(val price: Int, val restingBids: MutableList<Order> = mutableListOf(), val restingAsks: MutableList<Order> = mutableListOf())


class OrderBook {

    private val symbol: String
    private val pricePoints: TreeMap<Int, MutableList<Order>>
    private var orderArena: MutableMap<String, Order>
    private var maxBid = 0
    private var minAsk = 0
    private val minTick = 1
    private val orderEventPublisher: OrderEventPublisher


    constructor(symbol: String, maxPrice: Int,
                depthPublisher: DepthPublisher,
                tradePublisher: TradePublisher,
                orderEventPublisher: OrderEventPublisher) {
        this.symbol = symbol
        this.pricePoints = TreeMap()
        this.maxBid = 0
        this.orderArena = mutableMapOf()
        this.minAsk = 0
        this.orderEventPublisher = orderEventPublisher
    }



    fun getPricePoints(): Map<Int, List<Order>> {
        return pricePoints
    }

    private fun executeTrade(buy: Order, sell: Order, price: Int, size: Int) {
        if (size == 0) return
        buy.qtyOnMarket -= size
        sell.qtyOnMarket -= size
        buy.qtyFilled += size
        sell.qtyFilled += size
        orderEventPublisher.onFill(order=buy, matchedOrder=sell, price=price, size=size)

    }

    private fun handleBuy(incomingOrder: Order) {
        if (incomingOrder.price > maxBid || maxBid == 0) maxBid = incomingOrder.price
        val agreeableAsks = pricePoints.navigableKeySet().tailSet(minAsk).iterator()
        prices@ for (price in agreeableAsks) {
            val ordersThisLevel = pricePoints.get(price)?.iterator() ?: continue
            price@ for (restingOrder in ordersThisLevel) {
                when {
                    restingOrder.isBuy -> continue@price
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
            pricePoints.getOrPut(incomingOrder.price, { mutableListOf() }).add(incomingOrder)
            orderArena.put(incomingOrder.id, incomingOrder)
        }
    }

    private fun handleSell(incomingOrder: Order) {
        if (incomingOrder.price < minAsk || minAsk == 0) minAsk = incomingOrder.price
        val agreeableBids = pricePoints.navigableKeySet().headSet(maxBid + minTick).iterator()
        prices@ for (price in agreeableBids) {
            val ordersThisLevel = pricePoints.get(price)?.iterator() ?: continue
            price@ for (restingOrder in ordersThisLevel) {
                when {
                    !restingOrder.isBuy -> continue@price
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
            pricePoints.getOrPut(incomingOrder.price, { mutableListOf() }).add(incomingOrder)
            orderArena.put(incomingOrder.id, incomingOrder)
        }
    }


    fun submit(incomingOrder: Order) {
        if (incomingOrder.isBuy) {
            handleBuy((incomingOrder))
        } else {
            handleSell(incomingOrder)
        }
    }

}