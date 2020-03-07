package com.secwager.matchengine

import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.secwager.dto.Order
import com.secwager.proto.Market
import com.secwager.proto.Market.Order.OrderType
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
    fun fillIncomingSellAgainstRestingOrdersAtMultipleLevels(){
        val depthCaptor = argumentCaptor<Depth>()
        val filledBuyCaptor = argumentCaptor<Market.Order>()
        val filledSellCaptor = argumentCaptor<Market.Order>()
        val priceCaptor = argumentCaptor<Int>()
        val qtyCaptor = argumentCaptor<Int>()
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

 //       assertThat(filledSellCaptor.allValues).isEmpty()
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