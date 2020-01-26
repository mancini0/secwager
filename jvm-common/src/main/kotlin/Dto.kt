package com.secwager.dto
data class DepthBook(val bids: List<Level> = emptyList(), val asks: List<Level> = emptyList())
data class Level(val price: Int, val qty: Int)