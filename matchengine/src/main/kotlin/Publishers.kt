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

interface CallbackExecutor {
    fun executeCallbacks(callbacks:MutableCollection<()->Any>)
}


interface OrderEventPublisher{
    fun onFill(buy:Order, sell:Order)
    fun onAccept(order:Order) //handled by orderEntry service
    fun onReject(order:Order, reason: Order.RejectedReason)
    fun onCancel(order:Order)
    fun onCancelReject(order:Order, reason: Order.RejectedReason)
}