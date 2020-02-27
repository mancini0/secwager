package com.secwager.matchengine

import org.junit.Test


class OrderBookTest {




//    @Test
//    fun matchAgainstRestingBuyAtSamePrice() {
//        val ob = OrderBook("IBM", 10)
//        ob.submit(Order("a", isBuy = true, qtyOnMarket = 50, traderId = "me", price = 6))
//        ob.submit(Order("b", isBuy = false, qtyOnMarket = 50, traderId = "you", price = 6))
//        println(ob.getRestingOrdersByPrice())
//    }
//
//    @Test
//    fun matchAgainstRestingSellAtSamePrice() {
//        val ob = OrderBook("IBM", 10)
//        ob.submit(Order("a", isBuy = false, qtyOnMarket = 50, traderId = "me", price = 6))
//        ob.submit(Order("b", isBuy = true, qtyOnMarket = 50, traderId = "you", price = 6))
//        println(ob.getRestingOrdersByPrice())
//    }
//
//    @Test
//    fun `matchAgainstRestingSell AtSamePrice WithExcessSellLiquidity`() {
//        val ob = OrderBook("IBM", 10)
//        ob.submit(Order("a", isBuy = false, qtyOnMarket = 75, traderId = "me", price = 6))
//        ob.submit(Order("b", isBuy = true, qtyOnMarket = 50, traderId = "you", price = 6))
//        println(ob.getRestingOrdersByPrice())
//    }


    @Test
    fun matchAgainstRestingSellAtAgreeablePrice() {
        val ob = OrderBook("IBM", 10)
        ob.submit(Order("a", isBuy = false, qtyOnMarket = 50, traderId = "me", price = 5))
        println(ob.getPricePoints())
        ob.submit(Order("b", isBuy = true, qtyOnMarket = 75, traderId = "you", price = 10))
        println(ob.getPricePoints())
        ob.submit(Order("c", isBuy = false, qtyOnMarket = 24, traderId = "you", price = 10))
        println(ob.getPricePoints())
    }

//    @Test
//    fun matchAgainstRestingBuyAtAgreeablePrice() {
//        val ob = OrderBook("IBM", 10)
//        ob.submit(Order("a", isBuy = true, qtyOnMarket = 50, traderId = "me", price = 10))
//        ob.submit(Order("b", isBuy = false, qtyOnMarket = 50, traderId = "you", price = 8))
//        println(ob.getRestingOrdersByPrice())
//    }
}