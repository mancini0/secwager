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

    class CallBackRunner : CallbackExecutor {
        override fun executeCallbacks(callbacks: MutableCollection<() -> Any>) {
            callbacks.forEach{it.invoke()}
            callbacks.clear()
        }
    }

    val orderPublisher = mock(OrderEventPublisher::class.java)
    val depthPublisher = mock(DepthPublisher::class.java)
    val tradePublisher = mock(TradePublisher::class.java)
    var book: OrderBook = OrderBook("IBM", depthPublisher, tradePublisher, orderPublisher, CallBackRunner())


    @Before
    fun before() {
        this.book = OrderBook("IBM", depthPublisher, tradePublisher, orderPublisher, CallBackRunner() )
    }


    @Test
    fun cancelBuy(){
        val depthCaptor = argumentCaptor<Depth>()
        book.submit(Order("buy", orderType =  OrderType.BUY,  qtyOnMarket = 50, traderId = "buyer", price = 5, symbol = "IBM"))
        book.submit(Order("buy", orderType =  OrderType.CANCEL,  qtyOnMarket = 50, traderId = "buyer", price = 5, symbol = "IBM"))
        book.submit(Order("sell",orderType = OrderType.SELL,  qtyOnMarket = 100, traderId="seller",price=2, symbol = "IBM"))
        verify(depthPublisher, times(3)).onDepthChange(depthCaptor.capture())
        assertThat(depthCaptor.allValues)
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
        val depthCaptor = argumentCaptor<Depth>()
        val filledBuyCaptor = argumentCaptor<Market.Order>()
        val filledSellCaptor = argumentCaptor<Market.Order>()
        book.submit(Order("bid6", orderType =  OrderType.BUY,  qtyOnMarket = 50, traderId = "buyer1", price = 6, symbol = "IBM"))
        book.submit(Order("bid6Later", orderType =  OrderType.BUY, qtyOnMarket = 150, traderId = "buyer2", price = 6, symbol = "IBM"))
        book.submit(Order("bid7", orderType =  OrderType.BUY,  qtyOnMarket = 25, traderId = "buyer3", price = 7, symbol = "IBM"))
        book.submit(Order("sell", orderType =  OrderType.SELL,  qtyOnMarket = 225, traderId = "seller", price = 4, symbol = "IBM"))
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
        val depthCaptor = argumentCaptor<Depth>()
        val filledBuyCaptor = argumentCaptor<Market.Order>()
        val filledSellCaptor = argumentCaptor<Market.Order>()
        book.submit(Order("bid6", orderType =  OrderType.BUY,  qtyOnMarket = 50, traderId = "buyer1", price = 6, symbol = "IBM"))
        book.submit(Order("bid6Later", orderType =  OrderType.BUY, qtyOnMarket = 150, traderId = "buyer2", price = 6, symbol = "IBM"))
        book.submit(Order("bid7", orderType =  OrderType.BUY,  qtyOnMarket = 25, traderId = "buyer3", price = 7, symbol = "IBM"))
        book.submit(Order("sell", orderType =  OrderType.SELL,  qtyOnMarket = 226, traderId = "seller", price = 4, symbol = "IBM"))
        verify(depthPublisher, times(4)).onDepthChange(depthCaptor.capture())
        verify(orderPublisher, times(3)).onFill(filledBuyCaptor.capture(), filledSellCaptor.capture())
        assertThat(depthCaptor.allValues)
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
        val depthCaptor = argumentCaptor<Depth>()
        val filledBuyCaptor = argumentCaptor<Market.Order>()
        val filledSellCaptor = argumentCaptor<Market.Order>()
        book.submit(Order("firstBuy", orderType =  OrderType.BUY,  qtyOnMarket = 50, traderId = "buyer1", price = 6, symbol = "IBM"))
        book.submit(Order("sell", orderType =  OrderType.SELL,  qtyOnMarket = 55, traderId = "seller", price = 4, symbol = "IBM"))
        book.submit(Order("secondBuy", orderType =  OrderType.BUY,  qtyOnMarket = 35, traderId = "buyer2", price = 8, symbol = "IBM"))
        verify(depthPublisher, times(3)).onDepthChange(depthCaptor.capture())
        verify(orderPublisher, times(2)).onFill(filledBuyCaptor.capture(), filledSellCaptor.capture())
        assertThat(depthCaptor.allValues)
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