package com.secwager.matchengine

class RejectReason{}

interface TradePublisher {
    fun onTrade(symbol:String, qty: Int, price:Int)
}


interface DepthPublisher {
   fun  onDepthChange(symbol:String, bidPrices: List<Int>, bidQtys: List<Int>)
}


interface OrderEventPublisher{
    fun onFill(order:Order, matchedOrder:Order, qty: Int, price:Int)
    fun onAccept(order:Order)
    fun onReject(order:Order, reason:RejectReason)
    fun onCancel(order:Order)
    fun onCancelReject(order:Order, reason : RejectReason)
    fun onReplace(order:Order)
    fun onReplaceReject(order:Order, reason: RejectReason)
}