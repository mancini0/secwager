package com.secwager.dto

data class DepthBook(val isin: String, val bids: List<Level> = emptyList(), val asks: List<Level> = emptyList())
data class Level(val price: Int, val qty: Int)

enum class OrderType {
    BUY,
    SELL,
    CANCEL
}

enum class OrderSide {
    BUY, SELL
}

enum class OrderStatus {
    OPEN,
    FILLED,
    CANCELLED
}

enum class RejectReason {
    ALREADY_FILLED,
    ALREADY_CANCELLED,
    ORDER_NOT_FOUND,
}

data class Match(val orderId:String, val traderId: String, val price: Int, val qty: Int)
data class Order(val id: String, val type: OrderType, val side: OrderSide, var fills: MutableList<Match> = mutableListOf(), var status: OrderStatus = OrderStatus.OPEN, var qtyOnMarket: Int, var qtyFilled: Int = 0, val price: Int, val traderId: String)
