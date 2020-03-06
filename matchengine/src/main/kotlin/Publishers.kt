package com.secwager.matchengine

import com.secwager.proto.Market.Order
import com.secwager.dto.RejectReason
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
    fun onFill(buy:Order, sell:Order, price:Int,  size: Int)
    fun onAccept(order:Order) //handled by orderEntry service
    fun onReject(order:Order, reason: RejectReason)
    fun onCancel(order:Order)
    fun onCancelReject(order:Order, reason : RejectReason)
}