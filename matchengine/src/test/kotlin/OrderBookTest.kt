package com.secwager.matchengine

import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.secwager.dto.Order
import com.secwager.dto.OrderSide
import com.secwager.dto.OrderStatus
import com.secwager.dto.OrderType
import com.secwager.proto.Market
import com.secwager.proto.Market.Depth

import com.secwager.proto.Market.*
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
    fun matchAgainstRestingBuyAtSamePriceAndQty() {
        val depthCaptor = argumentCaptor<Depth>()

        book.submit(Order("buy", type = OrderType.BUY, side = OrderSide.BUY, qtyOnMarket = 50, traderId = "buyer", price = 6))
        book.submit(Order("sell", type = OrderType.SELL, side = OrderSide.SELL, qtyOnMarket = 50, traderId = "seller", price = 6))

        verify(depthPublisher, times(2)).onDepthChange(depthCaptor.capture())
//        verify(orderPublisher, times(1)).onFill(
//                Order("buy", side = OrderSide.BUY, qtyOnMarket = 0, qtyFilled = 50, traderId = "buyer", price = 6,
//                        status = OrderStatus.FILLED, type = OrderType.BUY),
//                Order("sell", side = OrderSide.SELL, qtyOnMarket = 0, qtyFilled = 50, traderId = "seller", price = 6,
//                        status = OrderStatus.FILLED, type = OrderType.SELL), price = 6, size = 50)

        //the depth associated with the first bid should be published, the next depth publish should not
        // show any resting liquidity since all orders on the book have filled.
        assertThat(depthCaptor.allValues)
                .containsExactly(Depth.newBuilder().setIsin("IBM").addBidPrices(6).addBidQtys(50).build(),
                        Depth.newBuilder().setIsin("IBM").build())
                .inOrder()

    }

    @Test
    fun matchAgainstRestingSellAtSamePriceAndQty() {
        val depthCaptor = argumentCaptor<Depth>()

        book.submit(Order("sell", type = OrderType.SELL, side = OrderSide.SELL, qtyOnMarket = 50, traderId = "me", price = 6))
        book.submit(Order("buy", type = OrderType.BUY, side = OrderSide.BUY, qtyOnMarket = 50, traderId = "you", price = 6))

        verify(depthPublisher, times(2)).onDepthChange(depthCaptor.capture())
//        verify(orderPublisher, times(1)).onFill(
//                Order("buy", side = OrderSide.BUY, qtyOnMarket = 0, qtyFilled = 50, traderId = "you", price = 6,
//                        status = OrderStatus.FILLED, type = OrderType.BUY),
//                Order("sell", side = OrderSide.SELL, qtyOnMarket = 0, qtyFilled = 50, traderId = "me", price = 6,
//                        status = OrderStatus.FILLED, type = OrderType.SELL),price=6, size=50)

        assertThat(depthCaptor.allValues)
                .containsExactly(Depth.newBuilder().setIsin("IBM").addAskPrices(6).addAskQtys(50).build(),
                        Depth.newBuilder().setIsin("IBM").build())
                .inOrder()

    }

    @Test
    fun `matchAgainstRestingSell AtSamePrice WithExcessSellLiquidity`() {
        val depthCaptor = argumentCaptor<Depth>()

        book.submit(Order("sell", type = OrderType.SELL, side = OrderSide.SELL, qtyOnMarket = 75, traderId = "seller", price = 6))
        book.submit(Order("buy", type = OrderType.BUY, side = OrderSide.BUY, qtyOnMarket = 50, traderId = "buyer", price = 6))

        verify(depthPublisher, times(2)).onDepthChange(depthCaptor.capture())
//        verify(orderPublisher, times(1)).onFill(
//                Order("buy", side = OrderSide.BUY, qtyOnMarket = 0, qtyFilled = 50, traderId = "buyer", price = 6,
//                        status = OrderStatus.FILLED, type = OrderType.BUY),
//                Order("sell", side = OrderSide.SELL, qtyOnMarket = 25, qtyFilled = 50, traderId = "seller", price = 6,
//                        status = OrderStatus.OPEN, type = OrderType.SELL),price=6, size=50)

        assertThat(depthCaptor.allValues)
                .containsExactly(Depth.newBuilder().setIsin("IBM").addAskPrices(6).addAskQtys(75).build(),
                        Depth.newBuilder().setIsin("IBM").addAskPrices(6).addAskQtys(25).build())
                .inOrder()
    }

    @Test
    fun higherBidShouldMatch(){
        val depthCaptor = argumentCaptor<Depth>()
        val filledBuyCaptor = argumentCaptor<Market.Order>()
        val filledSellCaptor = argumentCaptor<Market.Order>()
        val priceCaptor = argumentCaptor<Int>()
        val qtyCaptor = argumentCaptor<Int>()
        book.submit(Order("bid6", type = OrderType.BUY, side = OrderSide.BUY, qtyOnMarket = 50, traderId = "buyer1", price = 6))
        book.submit(Order("bid6Later", type = OrderType.BUY, side = OrderSide.BUY, qtyOnMarket = 150, traderId = "buyer2", price = 6))
        book.submit(Order("bid7", type = OrderType.BUY, side = OrderSide.BUY, qtyOnMarket = 25, traderId = "buyer3", price = 7))
        book.submit(Order("sell", type = OrderType.SELL, side = OrderSide.SELL, qtyOnMarket = 20, traderId = "seller", price = 4))
        verify(depthPublisher, times(4)).onDepthChange(depthCaptor.capture())
        verify(orderPublisher, times(1)).onFill(filledBuyCaptor.capture(), filledSellCaptor.capture(), priceCaptor.capture(), qtyCaptor.capture())

        assertThat(depthCaptor.allValues)
                .containsExactly(
                        Depth.newBuilder().setIsin("IBM").addBidPrices(6).addBidQtys(50).build(),
                        Depth.newBuilder().setIsin("IBM").addBidPrices(6).addBidQtys(200).build(),
                        Depth.newBuilder().setIsin("IBM").addAllBidPrices(listOf(7,6)).addAllBidQtys(listOf(25,200)).build(),
                        Depth.newBuilder().setIsin("IBM").addAllBidPrices(listOf(7,6)).addAllBidQtys(listOf(5,200)).build()
                )
                .inOrder()
    }

    @Test
    fun fillIncomingSellAgainstRestingOrdersAtMultipleLevels(){
        val depthCaptor = argumentCaptor<Depth>()
        val filledBuyCaptor = argumentCaptor<Market.Order>()
        val filledSellCaptor = argumentCaptor<Market.Order>()
        val priceCaptor = argumentCaptor<Int>()
        val qtyCaptor = argumentCaptor<Int>()
        book.submit(Order("bid6", type = OrderType.BUY, side = OrderSide.BUY, qtyOnMarket = 50, traderId = "buyer1", price = 6))
        book.submit(Order("bid6Later", type = OrderType.BUY, side = OrderSide.BUY, qtyOnMarket = 150, traderId = "buyer2", price = 6))
        book.submit(Order("bid7", type = OrderType.BUY, side = OrderSide.BUY, qtyOnMarket = 25, traderId = "buyer3", price = 7))
        book.submit(Order("sell", type = OrderType.SELL, side = OrderSide.SELL, qtyOnMarket = 225, traderId = "seller", price = 4))
        verify(depthPublisher, times(4)).onDepthChange(depthCaptor.capture())
        verify(orderPublisher, times(3)).onFill(filledBuyCaptor.capture(), filledSellCaptor.capture(), priceCaptor.capture(), qtyCaptor.capture())

        assertThat(depthCaptor.allValues)
                .containsExactly(
                        Depth.newBuilder().setIsin("IBM").addBidPrices(6).addBidQtys(50).build(),
                        Depth.newBuilder().setIsin("IBM").addBidPrices(6).addBidQtys(200).build(),
                        Depth.newBuilder().setIsin("IBM").addAllBidPrices(listOf(7,6)).addAllBidQtys(listOf(25,200)).build(),
                        Depth.newBuilder().setIsin("IBM").build() // no resting depth after incoming sell
                )
                .inOrder()

        assertThat(filledSellCaptor.allValues).isEmpty()
//
//        assertThat(filledBuyCaptor.allValues)
//                .containsExactly(
//                        Order("bid7", type = OrderType.BUY, side = OrderSide.BUY, qtyOnMarket = 0, qtyFilled = 25, traderId = "buyer3", price = 7, status = OrderStatus.FILLED),
//                        Order("bid6", type = OrderType.BUY, side = OrderSide.BUY, qtyOnMarket = 0, qtyFilled = 50, traderId = "buyer1", price = 6, status = OrderStatus.FILLED),
//                        Order("bid6Later", type = OrderType.BUY, side = OrderSide.BUY, qtyOnMarket = 0, qtyFilled = 150, traderId = "buyer2", price = 6, status = OrderStatus.FILLED)
//                )
//                .inOrder()
//
//        assertThat(filledSellCaptor.allValues)
//                .containsExactly(
//                        Order("sell", type = OrderType.SELL, side = OrderSide.SELL, qtyOnMarket = 200, qtyFilled = 25, traderId = "seller", price = 7, status = OrderStatus.OPEN),
//                        Order("sell", type = OrderType.SELL, side = OrderSide.SELL, qtyOnMarket = 150, qtyFilled = 75, traderId = "seller", price = 6, status = OrderStatus.OPEN),
//                        Order("sell", type = OrderType.SELL, side = OrderSide.SELL, qtyOnMarket = 0, qtyFilled = 225, traderId = "seller", price = 6, status = OrderStatus.FILLED)
//                )
//                .inOrder()
    }
}