package com.secwager.matchengine

import com.secwager.proto.Market.Order
import com.secwager.proto.Market.Depth
import com.secwager.proto.Market.LastTrade

interface TradePublisher {
    fun onTrade(lastTrade: LastTrade)
}

interface DepthPublisher {
   fun  onDepthChange(depth: Depth)
}

interface OrderEventPublisher{
    fun onFill(buy:Order, sell:Order)
    fun onAccept(order:Order)
    fun onReject(order:Order, reason: Order.RejectedReason)
    fun onCancel(order:Order)
    fun onCancelReject(orderId :String, reason: Order.RejectedReason)
}