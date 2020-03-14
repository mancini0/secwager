package com.secwager.market

import com.secwager.matchengine.OrderEventPublisher
import com.secwager.proto.Market

class OrderEventPublisherImpl : OrderEventPublisher {


    override fun onFill(buy: Market.Order, sell: Market.Order) {
        TODO("Not yet implemented")
    }

    override fun onAccept(order: Market.Order) {
        TODO("Not yet implemented")
    }

    override fun onReject(order: Market.Order, reason: Market.Order.RejectedReason) {
        TODO("Not yet implemented")
    }

    override fun onCancel(order: Market.Order) {
        TODO("Not yet implemented")
    }

    override fun onCancelReject(orderId: String, reason: Market.Order.RejectedReason) {
        TODO("Not yet implemented")
    }
}