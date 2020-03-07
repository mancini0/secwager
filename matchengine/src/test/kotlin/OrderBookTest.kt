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

class OrderBookTest {


    val orderPublisher = mock(OrderEventPublisher::class.java)
    val depthPublisher = mock(DepthPublisher::class.java)
    val tradePublisher = mock(TradePublisher::class.java)
    var book: OrderBook = OrderBook("IBM", depthPublisher, tradePublisher, orderPublisher)


    @Before
    fun before() {
        this.book = OrderBook("IBM", depthPublisher, tradePublisher, orderPublisher)
    }



    @Test
    fun fillIncomingSellAgainstRestingOrdersAtMultipleLevels(){
        val depthCaptor = argumentCaptor<Depth>()
        val filledBuyCaptor = argumentCaptor<Market.Order>()
        val filledSellCaptor = argumentCaptor<Market.Order>()
        book.submit(Order("bid6", type = OrderType.BUY, isBuy = true, qtyOnMarket = 50, traderId = "buyer1", price = 6, symbol = "IBM"))
        book.submit(Order("bid6Later", type = OrderType.BUY, isBuy =true, qtyOnMarket = 150, traderId = "buyer2", price = 6, symbol = "IBM"))
        book.submit(Order("bid7", type = OrderType.BUY, isBuy = true, qtyOnMarket = 25, traderId = "buyer3", price = 7, symbol = "IBM"))
        book.submit(Order("sell", type = OrderType.SELL, isBuy = false, qtyOnMarket = 225, traderId = "seller", price = 4, symbol = "IBM"))
        verify(depthPublisher, times(4)).onDepthChange(depthCaptor.capture())
        verify(orderPublisher, times(3)).onFill(filledBuyCaptor.capture(), filledSellCaptor.capture())
        assertThat(depthCaptor.allValues)
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
                                .setIsBuy(true)
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
                                .setIsBuy(true)
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
                                .setIsBuy(true)
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
                                .setIsBuy(false)
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
                                .setIsBuy(false)
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
                                .setIsBuy(false)
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
}