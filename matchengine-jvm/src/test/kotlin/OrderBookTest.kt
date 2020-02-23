package com.secwager.matchengine

import org.junit.Test


class OrderBookTest {

    @Test
    fun simpleMatchAgainstRestingBuy() {
        val ob = OrderBook("IBM", 10)
        ob.submit(Order("a", isBuy = true, qtyOnMarket = 50, traderId = "m", price = 6))
        ob.submit(Order("b", isBuy = false, qtyOnMarket = 50, traderId = "i", price = 6))
    }

    fun simpleMatchAgainstRestingSell() {
        val ob = OrderBook("IBM", 10)
        ob.submit(Order("a", isBuy = false, qtyOnMarket = 50, traderId = "m", price = 6))
        ob.submit(Order("b", isBuy = true, qtyOnMarket = 50, traderId = "i", price = 6))
    }
}