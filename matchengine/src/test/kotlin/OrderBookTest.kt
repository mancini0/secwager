package com.secwager.matchengine

import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.secwager.dto.Order
import com.secwager.proto.Market
import com.secwager.proto.Market.Order.*
import com.secwager.proto.Market.Depth

import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*

import com.secwager.utils.ConversionUtils.Companion.orderToProto


class OrderBookTest {


    val orderPublisher = mock(OrderEventPublisher::class.java)
    val tradePublisher = mock(TradePublisher::class.java)
    var book: OrderBook = OrderBook("IBM", tradePublisher, orderPublisher)


    @Before
    fun before() {
        this.book = OrderBook("IBM", tradePublisher, orderPublisher)
    }


    @Test
    fun serialize() {
        val sell = Order("sell",orderType = OrderType.SELL,  qtyOnMarket = 100, traderId="seller",price=6, symbol = "IBM")
        val buy = Order("buy", orderType =  OrderType.BUY,  qtyOnMarket = 50, traderId = "buyer", price = 5, symbol = "IBM")
        book.submit(sell)
        book.submit(buy)
        assertThat(book.serializeBook()).isEqualTo(Market.BookState.newBuilder()
            .setMaxBid(5)
            .setMinAsk(6)
            .putAllBids(mutableMapOf(Pair(5, Market.BookState.RestingOrders.newBuilder().addOrder(orderToProto(buy)).build())))
            .putAllAsks(mutableMapOf(Pair(6, Market.BookState.RestingOrders.newBuilder().addOrder(orderToProto(sell)).build())))
            .putAllArena(mutableMapOf(Pair("buy",
                orderToProto(buy)),
                Pair("sell",
                    orderToProto(sell))
            )).build())
    }

    @Test
    fun cancelBuy(){
        val depthSnapshots : MutableList<Market.Depth> = mutableListOf()
        book.submit(Order("buy", orderType =  OrderType.BUY,  qtyOnMarket = 50, traderId = "buyer", price = 5, symbol = "IBM"))
        depthSnapshots.add(book.measureFullDepth())
        book.submit(Order("buy", orderType =  OrderType.CANCEL,  qtyOnMarket = 50, traderId = "buyer", price = 5, symbol = "IBM"))
        depthSnapshots.add(book.measureFullDepth());
        book.submit(Order("sell",orderType = OrderType.SELL,  qtyOnMarket = 100, traderId="seller",price=2, symbol = "IBM"))
        depthSnapshots.add(book.measureFullDepth());

        assertThat(depthSnapshots)
                .containsExactly(
                        Depth.newBuilder().setIsin("IBM").addBidPrices(5).addBidQtys(50).build(),
                        Depth.newBuilder().setIsin("IBM").build(),
                        Depth.newBuilder().setIsin("IBM").addAskPrices(2).addAskQtys(100).build()
                )
                .inOrder()
        verifyNoInteractions(tradePublisher)
    }


    @Test
    fun fillIncomingSellAgainstRestingOrdersAtMultipleLevels(){
        val depthSnapshots: MutableList<Market.Depth> = mutableListOf()
        val callBacks : MutableList<()->Any> = mutableListOf()
        val filledBuyCaptor = argumentCaptor<Market.Order>()
        val filledSellCaptor = argumentCaptor<Market.Order>()

        callBacks.addAll(book.submit(Order("bid6", orderType =  OrderType.BUY,  qtyOnMarket = 50, traderId = "buyer1", price = 6, symbol = "IBM")))
        depthSnapshots.add(book.measureFullDepth())
        callBacks.addAll(book.submit(Order("bid6Later", orderType =  OrderType.BUY, qtyOnMarket = 150, traderId = "buyer2", price = 6, symbol = "IBM")))
        depthSnapshots.add(book.measureFullDepth())
        callBacks.addAll(book.submit(Order("bid7", orderType =  OrderType.BUY,  qtyOnMarket = 25, traderId = "buyer3", price = 7, symbol = "IBM")))
        depthSnapshots.add(book.measureFullDepth())
        callBacks.addAll(book.submit(Order("sell", orderType =  OrderType.SELL,  qtyOnMarket = 225, traderId = "seller", price = 4, symbol = "IBM")))
        depthSnapshots.add(book.measureFullDepth())
        callBacks.forEach {it.invoke()}

        verify(orderPublisher, times(3)).onFill(filledBuyCaptor.capture(), filledSellCaptor.capture())
        assertThat(depthSnapshots)
                .containsExactly(
                        Depth.newBuilder().setIsin("IBM").addBidPrices(6).addBidQtys(50).build(),
                        Depth.newBuilder().setIsin("IBM").addBidPrices(6).addBidQtys(200).build(),
                        Depth.newBuilder().setIsin("IBM").addAllBidPrices(listOf(7,6)).addAllBidQtys(listOf(25,200)).build(),
                        Depth.newBuilder().setIsin("IBM").build() // no resting depth after incoming sell
                )
                .inOrder()

        assertThat(filledBuyCaptor.allValues)
                .containsExactly(
                        Market.Order.newBuilder().setOrderId("bid7")
                                .setIsin("IBM")
                                .setOrderType(OrderType.BUY)
                                
                                .setQtyOnMarket(0)
                                .setQtyFilled(25)
                                .setTraderId("buyer3")
                                .setPrice(7)
                                .setState(State.FILLED)
                                .addMatches(Match.newBuilder().setOrderId("sell").setPrice(7).setQty(25).setTraderId("seller").build())
                                .build(),

                        Market.Order.newBuilder().setOrderId("bid6")
                                .setIsin("IBM")
                                .setOrderType(OrderType.BUY)
                                
                                .setQtyOnMarket(0)
                                .setQtyFilled(50)
                                .setTraderId("buyer1")
                                .setPrice(6)
                                .setState(State.FILLED)
                                .addMatches(Match.newBuilder().setOrderId("sell").setPrice(6).setQty(50).setTraderId("seller").build())
                                .build(),

                        Market.Order.newBuilder().setOrderId("bid6Later")
                                .setIsin("IBM")
                                .setOrderType(OrderType.BUY)
                                
                                .setQtyOnMarket(0)
                                .setQtyFilled(150)
                                .setTraderId("buyer2")
                                .setPrice(6)
                                .setState(State.FILLED)
                                .addMatches(Match.newBuilder().setOrderId("sell").setPrice(6).setQty(150).setTraderId("seller").build())
                                .build()
                )
                .inOrder()


        assertThat(filledSellCaptor.allValues)
                .containsExactly(
                        Market.Order.newBuilder().setOrderId("sell")
                                .setIsin("IBM")
                                .setOrderType(OrderType.SELL)
                                .setQtyOnMarket(200)
                                .setQtyFilled(25)
                                .setTraderId("seller")
                                .setPrice(4)
                                .setState(State.OPEN)
                                .addMatches(Match.newBuilder().setOrderId("bid7").setPrice(7).setQty(25).setTraderId("buyer3").build())
                                .build(),
                        Market.Order.newBuilder().setOrderId("sell")
                                .setIsin("IBM")
                                .setOrderType(OrderType.SELL)
                                .setQtyOnMarket(150)
                                .setQtyFilled(75)
                                .setTraderId("seller")
                                .setPrice(4)
                                .setState(State.OPEN)
                                .addMatches(Match.newBuilder().setOrderId("bid7").setPrice(7).setQty(25).setTraderId("buyer3").build())
                                .addMatches(Match.newBuilder().setOrderId("bid6").setPrice(6).setQty(50).setTraderId("buyer1").build())
                                .build(),
                        Market.Order.newBuilder().setOrderId("sell")
                                .setIsin("IBM")
                                .setOrderType(OrderType.SELL)
                                .setQtyOnMarket(0)
                                .setQtyFilled(225)
                                .setTraderId("seller")
                                .setPrice(4)
                                .setState(State.FILLED)
                                .addMatches(Match.newBuilder().setOrderId("bid7").setPrice(7).setQty(25).setTraderId("buyer3").build())
                                .addMatches(Match.newBuilder().setOrderId("bid6").setPrice(6).setQty(50).setTraderId("buyer1").build())
                                .addMatches(Match.newBuilder().setOrderId("bid6Later").setPrice(6).setQty(150).setTraderId("buyer2").build())
                                .build()
                )
                .inOrder()
    }



    @Test
    fun partialfillIncomingSellAgainstRestingOrdersAtMultipleLevels(){
        val depthSnapshots : MutableList<Market.Depth> = mutableListOf()
        val callbacks : MutableList<()->Any> = mutableListOf();

        val filledBuyCaptor = argumentCaptor<Market.Order>()
        val filledSellCaptor = argumentCaptor<Market.Order>()
        callbacks.addAll(book.submit(Order("bid6", orderType =  OrderType.BUY,  qtyOnMarket = 50, traderId = "buyer1", price = 6, symbol = "IBM")))
        depthSnapshots.add(book.measureFullDepth())
        callbacks.addAll(book.submit(Order("bid6Later", orderType =  OrderType.BUY, qtyOnMarket = 150, traderId = "buyer2", price = 6, symbol = "IBM")))
        depthSnapshots.add(book.measureFullDepth())
        callbacks.addAll(book.submit(Order("bid7", orderType =  OrderType.BUY,  qtyOnMarket = 25, traderId = "buyer3", price = 7, symbol = "IBM")))
        depthSnapshots.add(book.measureFullDepth())
        callbacks.addAll(book.submit(Order("sell", orderType =  OrderType.SELL,  qtyOnMarket = 226, traderId = "seller", price = 4, symbol = "IBM")))
        depthSnapshots.add(book.measureFullDepth())
        callbacks.forEach { it.invoke() };
        verify(orderPublisher, times(3)).onFill(filledBuyCaptor.capture(), filledSellCaptor.capture())
        assertThat(depthSnapshots)
                .containsExactly(
                        Depth.newBuilder().setIsin("IBM").addBidPrices(6).addBidQtys(50).build(),
                        Depth.newBuilder().setIsin("IBM").addBidPrices(6).addBidQtys(200).build(),
                        Depth.newBuilder().setIsin("IBM").addAllBidPrices(listOf(7,6)).addAllBidQtys(listOf(25,200)).build(),
                        Depth.newBuilder().setIsin("IBM").addAskPrices(4).addAskQtys(1).build() // no resting depth after incoming sell
                )
                .inOrder()

        assertThat(filledBuyCaptor.allValues)
                .containsExactly(
                        Market.Order.newBuilder().setOrderId("bid7")
                                .setIsin("IBM")
                                .setOrderType(OrderType.BUY)
                                
                                .setQtyOnMarket(0)
                                .setQtyFilled(25)
                                .setTraderId("buyer3")
                                .setPrice(7)
                                .setState(State.FILLED)
                                .addMatches(Match.newBuilder().setOrderId("sell").setPrice(7).setQty(25).setTraderId("seller").build())
                                .build(),

                        Market.Order.newBuilder().setOrderId("bid6")
                                .setIsin("IBM")
                                .setOrderType(OrderType.BUY)
                                
                                .setQtyOnMarket(0)
                                .setQtyFilled(50)
                                .setTraderId("buyer1")
                                .setPrice(6)
                                .setState(State.FILLED)
                                .addMatches(Match.newBuilder().setOrderId("sell").setPrice(6).setQty(50).setTraderId("seller").build())
                                .build(),

                        Market.Order.newBuilder().setOrderId("bid6Later")
                                .setIsin("IBM")
                                .setOrderType(OrderType.BUY)
                                
                                .setQtyOnMarket(0)
                                .setQtyFilled(150)
                                .setTraderId("buyer2")
                                .setPrice(6)
                                .setState(State.FILLED)
                                .addMatches(Match.newBuilder().setOrderId("sell").setPrice(6).setQty(150).setTraderId("seller").build())
                                .build()
                )
                .inOrder()


        assertThat(filledSellCaptor.allValues)
                .containsExactly(
                        Market.Order.newBuilder().setOrderId("sell")
                                .setIsin("IBM")
                                .setOrderType(OrderType.SELL)
                                
                                .setQtyOnMarket(201)
                                .setQtyFilled(25)
                                .setTraderId("seller")
                                .setPrice(4)
                                .setState(State.OPEN)
                                .addMatches(Match.newBuilder().setOrderId("bid7").setPrice(7).setQty(25).setTraderId("buyer3").build())
                                .build(),
                        Market.Order.newBuilder().setOrderId("sell")
                                .setIsin("IBM")
                                .setOrderType(OrderType.SELL)
                                
                                .setQtyOnMarket(151)
                                .setQtyFilled(75)
                                .setTraderId("seller")
                                .setPrice(4)
                                .setState(State.OPEN)
                                .addMatches(Match.newBuilder().setOrderId("bid7").setPrice(7).setQty(25).setTraderId("buyer3").build())
                                .addMatches(Match.newBuilder().setOrderId("bid6").setPrice(6).setQty(50).setTraderId("buyer1").build())
                                .build(),
                        Market.Order.newBuilder().setOrderId("sell")
                                .setIsin("IBM")
                                .setOrderType(OrderType.SELL)
                                
                                .setQtyOnMarket(1)
                                .setQtyFilled(225)
                                .setTraderId("seller")
                                .setPrice(4)
                                .setState(State.OPEN)
                                .addMatches(Match.newBuilder().setOrderId("bid7").setPrice(7).setQty(25).setTraderId("buyer3").build())
                                .addMatches(Match.newBuilder().setOrderId("bid6").setPrice(6).setQty(50).setTraderId("buyer1").build())
                                .addMatches(Match.newBuilder().setOrderId("bid6Later").setPrice(6).setQty(150).setTraderId("buyer2").build())
                                .build()
                )
                .inOrder()
    }



    @Test
    fun fillPartiallyFilledSell(){
        val depthSnapshots : MutableList<Market.Depth> = mutableListOf()
        val callbacks : MutableList<()->Any> = mutableListOf()
        val filledBuyCaptor = argumentCaptor<Market.Order>()
        val filledSellCaptor = argumentCaptor<Market.Order>()
        callbacks.addAll(book.submit(Order("firstBuy", orderType =  OrderType.BUY,  qtyOnMarket = 50, traderId = "buyer1", price = 6, symbol = "IBM")))
        depthSnapshots.add(book.measureFullDepth())
        callbacks.addAll(book.submit(Order("sell", orderType =  OrderType.SELL,  qtyOnMarket = 55, traderId = "seller", price = 4, symbol = "IBM")))
        depthSnapshots.add(book.measureFullDepth())
        callbacks.addAll(book.submit(Order("secondBuy", orderType =  OrderType.BUY,  qtyOnMarket = 35, traderId = "buyer2", price = 8, symbol = "IBM")))
        depthSnapshots.add(book.measureFullDepth())
        callbacks.forEach { it.invoke() }
        verify(orderPublisher, times(2)).onFill(filledBuyCaptor.capture(), filledSellCaptor.capture())
        assertThat(depthSnapshots)
                .containsExactly(
                        Depth.newBuilder().setIsin("IBM").addBidPrices(6).addBidQtys(50).build(),
                        Depth.newBuilder().setIsin("IBM").addAskPrices(4).addAskQtys(5).build(),
                        Depth.newBuilder().setIsin("IBM").addBidPrices(8).addBidQtys(30).build()
                )
                .inOrder()

        assertThat(filledBuyCaptor.allValues)
                .containsExactly(
                        Market.Order.newBuilder().setOrderId("firstBuy")
                                .setIsin("IBM")
                                .setOrderType(OrderType.BUY)
                                
                                .setQtyOnMarket(0)
                                .setQtyFilled(50)
                                .setTraderId("buyer1")
                                .setPrice(6)
                                .setState(State.FILLED)
                                .addMatches(Match.newBuilder().setOrderId("sell").setPrice(6).setQty(50).setTraderId("seller").build())
                                .build(),

                        Market.Order.newBuilder().setOrderId("secondBuy")
                                .setIsin("IBM")
                                .setOrderType(OrderType.BUY)
                                
                                .setQtyOnMarket(30)
                                .setQtyFilled(5)
                                .setTraderId("buyer2")
                                .setPrice(8)
                                .setState(State.OPEN)
                                .addMatches(Match.newBuilder().setOrderId("sell").setPrice(4).setQty(5).setTraderId("seller").build())
                                .build())
                .inOrder()


        assertThat(filledSellCaptor.allValues)
                .containsExactly(
                        Market.Order.newBuilder().setOrderId("sell")
                                .setIsin("IBM")
                                .setOrderType(OrderType.SELL)
                                
                                .setQtyOnMarket(5)
                                .setQtyFilled(50)
                                .setTraderId("seller")
                                .setPrice(4)
                                .setState(State.OPEN)
                                .addMatches(Match.newBuilder().setOrderId("firstBuy").setPrice(6).setQty(50).setTraderId("buyer1").build())
                                .build(),

                        Market.Order.newBuilder().setOrderId("sell")
                                .setIsin("IBM")
                                .setOrderType(OrderType.SELL)
                                
                                .setQtyOnMarket(0)
                                .setQtyFilled(55)
                                .setTraderId("seller")
                                .setPrice(4)
                                .setState(State.FILLED)
                                .addMatches(Match.newBuilder().setOrderId("firstBuy").setPrice(6).setQty(50).setTraderId("buyer1").build())
                                .addMatches(Match.newBuilder().setOrderId("secondBuy").setPrice(4).setQty(5).setTraderId("buyer2").build())
                                .build())
                .inOrder()

    }
}