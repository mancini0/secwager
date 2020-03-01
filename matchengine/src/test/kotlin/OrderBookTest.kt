package com.secwager.matchengine

import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.secwager.dto.Order
import com.secwager.dto.OrderSide
import com.secwager.dto.OrderStatus
import com.secwager.dto.OrderType
import com.secwager.proto.Market.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*

class OrderBookTest {


    val orderPublisher = mock(OrderEventPublisher::class.java)
    val depthPublisher = mock(DepthPublisher::class.java)
    val tradePublisher = mock(TradePublisher::class.java)
    var ob: OrderBook = OrderBook("IBM", depthPublisher, tradePublisher, orderPublisher)


    @Before
    fun before() {
        this.ob = OrderBook("IBM", depthPublisher, tradePublisher, orderPublisher)
    }


    @Test
    fun matchAgainstRestingBuyAtSamePriceAndQty() {
        val depthCaptor = argumentCaptor<Depth>()

        ob.submit(Order("buy", type = OrderType.BUY, side = OrderSide.BUY, qtyOnMarket = 50, traderId = "buyer", price = 6))
        ob.submit(Order("sell", type = OrderType.SELL, side = OrderSide.SELL, qtyOnMarket = 50, traderId = "seller", price = 6))

        verify(depthPublisher, times(2)).onDepthChange(depthCaptor.capture())
        verify(orderPublisher, times(1)).onFill(
                Order("buy", side = OrderSide.BUY, qtyOnMarket = 0, qtyFilled = 50, traderId = "buyer", price = 6,
                        status = OrderStatus.FILLED, type = OrderType.BUY),
                Order("sell", side = OrderSide.SELL, qtyOnMarket = 0, qtyFilled = 50, traderId = "seller", price = 6,
                        status = OrderStatus.FILLED, type = OrderType.SELL), price = 6, size = 50)

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

        ob.submit(Order("sell", type = OrderType.SELL, side = OrderSide.SELL, qtyOnMarket = 50, traderId = "me", price = 6))
        ob.submit(Order("buy", type = OrderType.BUY, side = OrderSide.BUY, qtyOnMarket = 50, traderId = "you", price = 6))

        verify(depthPublisher, times(2)).onDepthChange(depthCaptor.capture())
        verify(orderPublisher, times(1)).onFill(
                Order("buy", side = OrderSide.BUY, qtyOnMarket = 0, qtyFilled = 50, traderId = "you", price = 6,
                        status = OrderStatus.FILLED, type = OrderType.BUY),
                Order("sell", side = OrderSide.SELL, qtyOnMarket = 0, qtyFilled = 50, traderId = "me", price = 6,
                        status = OrderStatus.FILLED, type = OrderType.SELL),price=6, size=50)



        assertThat(depthCaptor.allValues)
                .containsExactly(Depth.newBuilder().setIsin("IBM").addAskPrices(6).addAskQtys(50).build(),
                        Depth.newBuilder().setIsin("IBM").build())
                .inOrder()

    }

    @Test
    fun `matchAgainstRestingSell AtSamePrice WithExcessSellLiquidity`() {

    }


    @Test
    fun matchAgainstRestingSellAtAgreeablePrice() {

    }

    @Test
    fun matchAgainstRestingBuyAtAgreeablePrice() {

    }
}