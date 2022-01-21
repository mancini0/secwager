package com.secwager.matchengine

import com.secwager.dto.*
import com.secwager.dto.Order
import com.secwager.proto.Market
import com.secwager.proto.Market.BookState
import com.secwager.proto.Market.Order.OrderType.*
import com.secwager.proto.Market.Order.RejectedReason.*
import com.secwager.proto.Market.Order.State.*
import com.secwager.proto.Market.Depth
import com.secwager.utils.ConversionUtils
import com.secwager.utils.ConversionUtils.Companion.orderToProto
import java.awt.print.Book
import java.util.*
import kotlin.math.absoluteValue

/**OrderBook implementation that does NOT inform the order submitter of depth changes via callbacks
 * instead, a measureFullDepth() function is provided which is intended to be called as frequently (or infrequently)
 * as needed (e.g, after a large batch of orders are submitted)
 */
class OrderBook(
    private val symbol: String,
    private val tradePublisher: TradePublisher,
    private val orderEventPublisher: OrderEventPublisher
) {
    private val restingBuys: TreeMap<Int, MutableList<Order>> = TreeMap(Collections.reverseOrder())
    private val restingSells: TreeMap<Int, MutableList<Order>> = TreeMap()
    private var orderArena: MutableMap<String, Order> = mutableMapOf()
    private var maxBid = 0
    private var minAsk = 0


    private fun executeTrade(buy: com.secwager.dto.Order, sell: com.secwager.dto.Order, price: Int, size: Int,
                            callBacks: MutableList<() -> Any>) {

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
        callBacks.add { tradePublisher.onTrade(Market.LastTrade.newBuilder().setIsin(this.symbol).setPrice(price).setQty(size).build())}
    }

    private fun handleBuy(incomingOrder: Order, callBacks: MutableList<() -> Any>) {
        if (incomingOrder.price > maxBid || maxBid == 0) maxBid = incomingOrder.price
        val agreeableAsks = restingSells.navigableKeySet().headSet(incomingOrder.price+1).iterator()
        prices@ for (price in agreeableAsks) {
            val ordersThisLevel = restingSells.get(price)?.iterator() ?: continue
            price@ for (restingOrder in ordersThisLevel) {
                when {
                    restingOrder.qtyOnMarket == incomingOrder.qtyOnMarket -> {
                        executeTrade(buy = incomingOrder,
                                sell = restingOrder, price = price, size = incomingOrder.qtyOnMarket, callBacks = callBacks)
                        ordersThisLevel.remove()
                        if (!ordersThisLevel.hasNext()) agreeableAsks.remove()
                        break@price
                    }
                    restingOrder.qtyOnMarket < incomingOrder.qtyOnMarket -> {
                        executeTrade(buy = incomingOrder,
                                sell = restingOrder, price = price, size = restingOrder.qtyOnMarket, callBacks = callBacks)
                        ordersThisLevel.remove()
                        if (!ordersThisLevel.hasNext()) agreeableAsks.remove()
                        continue@price
                    }
                    restingOrder.qtyOnMarket > incomingOrder.qtyOnMarket -> {
                        executeTrade(buy = incomingOrder,
                                sell = restingOrder, price = price, size = incomingOrder.qtyOnMarket, callBacks = callBacks)
                        break@price
                    }
                }
            }
            if (incomingOrder.qtyOnMarket == 0) break@prices
        }
        if (incomingOrder.qtyOnMarket > 0) {
            restingBuys.getOrPut(incomingOrder.price, { mutableListOf() }).add(incomingOrder)
        }
        orderArena.put(incomingOrder.id, incomingOrder)
    }

    private fun handleSell(incomingOrder: Order, callBacks: MutableList<() -> Any>) {
        if (incomingOrder.price < minAsk || minAsk == 0) minAsk = incomingOrder.price
        val agreeableBids = restingBuys.navigableKeySet().tailSet(maxBid).iterator()
        prices@ for (price in agreeableBids) {
            val ordersThisLevel = restingBuys.get(price)?.iterator() ?: continue
            price@ for (restingOrder in ordersThisLevel) {
                when {
                    restingOrder.qtyOnMarket == incomingOrder.qtyOnMarket -> {
                        executeTrade(buy = restingOrder,
                                sell = incomingOrder, price = price, size = incomingOrder.qtyOnMarket, callBacks = callBacks)
                        ordersThisLevel.remove()
                        if (!ordersThisLevel.hasNext()) agreeableBids.remove()
                        break@price
                    }
                    restingOrder.qtyOnMarket < incomingOrder.qtyOnMarket -> {
                        executeTrade(buy = restingOrder,
                                sell = incomingOrder, price = price, size = restingOrder.qtyOnMarket, callBacks = callBacks)
                        ordersThisLevel.remove()
                        if (!ordersThisLevel.hasNext()) agreeableBids.remove()
                        continue@price
                    }
                    restingOrder.qtyOnMarket > incomingOrder.qtyOnMarket -> {
                        executeTrade(buy = restingOrder,
                                sell = incomingOrder, price = price, size = incomingOrder.qtyOnMarket, callBacks = callBacks)
                        break@price
                    }
                }
            }
            if (incomingOrder.qtyOnMarket == 0) break@prices
        }
        if (incomingOrder.qtyOnMarket > 0) {
            restingSells.getOrPut(incomingOrder.price, { mutableListOf() }).add(incomingOrder)
        }
        orderArena.put(incomingOrder.id, incomingOrder)
    }

    fun measureFullDepth() : Depth {
        val depthBuilder = Depth.newBuilder().setIsin(this.symbol)
        restingBuys.entries.forEach{depthBuilder.addBidPrices(it.key); depthBuilder.addBidQtys(it.value.sumBy { it.qtyOnMarket})}
        restingSells.entries.forEach{depthBuilder.addAskPrices(it.key); depthBuilder.addAskQtys(it.value.sumBy { it.qtyOnMarket})}
        return depthBuilder.build()
    }



    private fun handleCancel(orderId: String, callBacks: MutableList<() -> Any>) {
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

    /**assumes order has already been validated by upstream order submission service -
    e.g, qty is > 0 so we know every order submission will require a depth publish**/
    fun submit(order: Order) : List<()->Any> {
        val callbacks = mutableListOf<()->Any>()
        when (order.orderType) {
            BUY -> handleBuy(order,callbacks)
            SELL -> handleSell(order, callbacks)
            CANCEL -> handleCancel(order.id, callbacks)
        }
        minAsk = if(!restingSells.isEmpty()) restingSells.firstKey() else 0
        maxBid = if(!restingBuys.isEmpty()) restingBuys.firstKey() else 0
        return callbacks
    }



    fun serializeBook(): BookState{
        return BookState.newBuilder().setMinAsk(this.minAsk)
        .setMaxBid(this.maxBid)
        .putAllArena(this.orderArena.mapValues{ orderToProto(it.value)})
        .putAllBids(this.restingBuys.mapValues{
            BookState.RestingOrders.newBuilder().addAllOrder(it.value.map{orderToProto(it)}).build()
            })
        .putAllAsks(this.restingSells.mapValues{
            BookState.RestingOrders.newBuilder().addAllOrder(it.value.map{orderToProto(it)}).build()
        }).build()
    }


    fun deserializeBook(bookState: BookState) {
        this.maxBid = bookState.maxBid;
        this.minAsk = bookState.minAsk;

        bookState.bidsMap.forEach {
            this.restingBuys[it.key] = it.value.orderList.mapTo(mutableListOf()) { o ->
                Order(
                    id = o.orderId, traderId = o.traderId,
                    symbol = o.isin, status = o.state, fills = o.matchesList.mapTo(mutableListOf()) { m ->
                        Match(
                            orderId = m.orderId,
                            traderId = m.traderId, qty = m.qty, price = m.price
                        )
                    }, price = o.price, orderType = o.orderType,
                    qtyFilled = o.qtyFilled, qtyOnMarket = o.qtyOnMarket
                ).also { orderArena.put(it.id, it) }

            }
        }


        bookState.asksMap.forEach {
            this.restingSells[it.key] = it.value.orderList.mapTo(mutableListOf()) { o ->
                Order(
                    id = o.orderId, traderId = o.traderId,
                    symbol = o.isin, status = o.state, fills = o.matchesList.mapTo(mutableListOf()) { m ->
                        Match(
                            orderId = m.orderId,
                            traderId = m.traderId, qty = m.qty, price = m.price
                        )
                    }, price = o.price, orderType = o.orderType,
                    qtyFilled = o.qtyFilled, qtyOnMarket = o.qtyOnMarket
                ).also { orderArena.put(it.id, it) }
            }
            return
        }
    }



}