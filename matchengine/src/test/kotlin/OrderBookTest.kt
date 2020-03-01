package com.secwager.matchengine

import com.secwager.dto.Order
import com.secwager.dto.OrderSide
import com.secwager.dto.OrderStatus
import com.secwager.dto.OrderType
import com.secwager.proto.Market
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*


class OrderBookTest {


    val orderPublisher = mock(OrderEventPublisher::class.java)
    val depthPublisher = mock(DepthPublisher::class.java)
    val tradePublisher = mock(TradePublisher::class.java)
    var ob: OrderBook = OrderBook("IBM",  depthPublisher, tradePublisher,orderPublisher)


    @Before
    fun before(){
        this.ob = OrderBook("IBM",  depthPublisher, tradePublisher,orderPublisher)
    }


    @Test
    fun matchAgainstRestingBuyAtSamePrice() {


        ob.submit(Order("o1", type = OrderType.BUY,  side = OrderSide.BUY, qtyOnMarket = 50, traderId = "me", price = 6))
        verify(depthPublisher).onDepthChange(Market.Depth.newBuilder().setIsin("IBM").addBidPrices(6).addBidQtys(50).build())
        ob.submit(Order("o2", type= OrderType.SELL, side = OrderSide.SELL, qtyOnMarket = 50, traderId = "you", price = 6))

        verify(orderPublisher, times(1)).onFill(
                Order("o1", side = OrderSide.BUY, qtyOnMarket = 0, qtyFilled = 50, traderId = "me", price = 6,
                        status=OrderStatus.FILLED, type = OrderType.BUY),
                Order("o2", side = OrderSide.SELL, qtyOnMarket = 0, qtyFilled = 50, traderId = "you", price = 6,
                        status=OrderStatus.FILLED, type = OrderType.SELL),price=6, size=50)

    }

    @Test
    fun matchAgainstRestingSellAtSamePrice() {

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