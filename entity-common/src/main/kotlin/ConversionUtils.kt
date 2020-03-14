package com.secwager.utils;
import com.secwager.dto.Level
import com.secwager.dto.Order
import com.secwager.proto.Market

class ConversionUtils {

    companion object{

        fun orderToProto(order: Order) : Market.Order {
            return Market.Order.newBuilder().setOrderId(order.id)
                    .setSide(order.side)
                    .setIsin(order.symbol)
                    .setTraderId(order.traderId)
                    .setPrice(order.price)
                    .setQtyOnMarket(order.qtyOnMarket)
                    .setQtyFilled(order.qtyFilled)
                    .setState(order.status)
                    .addAllMatches(order.fills.map{Market.Order.Match.newBuilder().setPrice(it.price).setQty(it.qty).setOrderId(it.orderId).setTraderId(it.traderId).build()})
                    .build()
        }

        fun protoToDepth(depthBook: Market.Depth) : com.secwager.dto.DepthBook {
            return com.secwager.dto.DepthBook(isin=depthBook.isin, bids=depthBook.bidPricesList.zip(depthBook.bidQtysList){price,qty-> Level(price, qty)},
                    asks=depthBook.askPricesList.zip(depthBook.askQtysList){price,qty-> Level(price, qty)})
        }
    }
}


