package com.secwager.dto
data class DepthBook(val isin: String , val bids: List<Level> = emptyList(), val asks: List<Level> = emptyList())
data class Level(val price: Int, val qty: Int)