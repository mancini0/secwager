package com.secwager.dto

import com.secwager.proto.Market.Order.OrderSide
import com.secwager.proto.Market.Order.State


data class DepthBook(val isin: String, val bids: List<Level> = emptyList(), val asks: List<Level> = emptyList())
data class Level(val price: Int, val qty: Int)


data class Match(val orderId: String, val traderId: String, val price: Int, val qty: Int)
data class Order(val id: String, val side: OrderSide, val symbol: String, var fills: MutableList<Match> = mutableListOf(), var status: State = State.OPEN, var qtyOnMarket: Int, var qtyFilled: Int = 0, val price: Int, val traderId: String)
