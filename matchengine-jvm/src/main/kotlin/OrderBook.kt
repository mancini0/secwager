package com.secwager.matchengine

import java.util.*


data class Order(val id: String, val isBuy: Boolean, var qtyOnMarket: Int, var qtyFilled: Int = 0, val price: Int, val traderId: String)

class OrderBook {

    private val symbol: String
    private val pricePoints: LinkedList<MutableList<Order>>
    private var orderArena = mutableMapOf<String, Order>()
    private var maxBid = 0
    private var minAsk = 0


    constructor(symbol: String, maxPrice: Int) {
        this.symbol = symbol
        this.pricePoints = LinkedList()
        (0..maxPrice).forEach { pricePoints.add(mutableListOf()) }
        this.maxBid = 0
        this.minAsk = 0
    }


    private fun executeTrade(buy: Order, sell: Order, price: Int, size: Int) {
        if(size==0) return
        println("trade matched ${buy.id} to ${sell.id}, $size@$price")
        buy.qtyOnMarket -= size
        sell.qtyOnMarket -= size
        buy.qtyFilled += size
        sell.qtyFilled += size
    }

    private fun handleBuy(incomingOrder: Order) {
        if(incomingOrder.price > maxBid || maxBid ==0) maxBid=incomingOrder.price
        if (incomingOrder.price >= minAsk && minAsk > 0) {
            while (incomingOrder.price >= minAsk) {
                val restingHereIterator = pricePoints.get(minAsk).iterator()
                thisLevel@ for (restingOrder in restingHereIterator) {
                    when {
                        restingOrder.isBuy -> continue@thisLevel
                        restingOrder.qtyOnMarket == incomingOrder.qtyOnMarket -> {
                            executeTrade(buy = incomingOrder,
                                    sell = restingOrder, price = minAsk, size = incomingOrder.qtyOnMarket)
                            restingHereIterator.remove()
                            break@thisLevel
                        }
                        restingOrder.qtyOnMarket < incomingOrder.qtyOnMarket -> {
                            executeTrade(buy = incomingOrder,
                                    sell = restingOrder, price = minAsk, size = restingOrder.qtyOnMarket)
                            restingHereIterator.remove()
                            continue@thisLevel
                        }
                        restingOrder.qtyOnMarket > incomingOrder.qtyOnMarket -> {
                            executeTrade(buy = incomingOrder,
                                    sell = restingOrder, price = minAsk, size = incomingOrder.qtyOnMarket)
                            break@thisLevel
                        }
                    }
                }
                if (!restingHereIterator.hasNext()) ++minAsk
            }
        } else {
            pricePoints.get(incomingOrder.price).add(incomingOrder)
            if (maxBid < incomingOrder.price) maxBid = incomingOrder.price;
            orderArena.put(incomingOrder.id, incomingOrder)
        }

    }

    private fun handleSell(incomingOrder: Order) {
        if(incomingOrder.price < minAsk || minAsk ==0) minAsk=incomingOrder.price
        if (incomingOrder.price <= maxBid && maxBid > 0) {
            while (incomingOrder.price <= maxBid && incomingOrder.qtyOnMarket > 0) {
                println("max bid $maxBid")
                val pricePointIterator = pricePoints.descendingIterator().withIndex();
                val restingHereIterator = pricePoints.get(maxBid).iterator()
                thisLevel@ for (restingOrder in restingHereIterator) {
                    when {
                        !restingOrder.isBuy -> continue@thisLevel
                        restingOrder.qtyOnMarket == incomingOrder.qtyOnMarket -> {

                            executeTrade(sell = incomingOrder,
                                    buy = restingOrder, price = maxBid, size = incomingOrder.qtyOnMarket)
                            restingHereIterator.remove()

                            break@thisLevel
                        }
                        restingOrder.qtyOnMarket < incomingOrder.qtyOnMarket -> {
                            executeTrade(sell = incomingOrder,
                                    buy = restingOrder, price = maxBid, size = restingOrder.qtyOnMarket)

                            restingHereIterator.remove()
                            continue@thisLevel
                        }
                        restingOrder.qtyOnMarket > incomingOrder.qtyOnMarket -> {
                            executeTrade(sell = incomingOrder,
                                    buy = restingOrder, price = maxBid, size = incomingOrder.qtyOnMarket)
                            break@thisLevel
                        }
                    }
                }
                if (!restingHereIterator.hasNext()) --maxBid
            }
        } else {
            pricePoints.get(incomingOrder.price).add(incomingOrder)
            if (minAsk > incomingOrder.price) minAsk = incomingOrder.price;
            orderArena.put(incomingOrder.id, incomingOrder)
        }

    }

    fun submit(incomingOrder: Order) {
        if (incomingOrder.isBuy){
            handleBuy((incomingOrder))
        }else {
            handleSell(incomingOrder)
        }
        println("after handle: $pricePoints")
    }

}