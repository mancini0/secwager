package com.secwager.utils;
import com.secwager.dto.Level
class SecwagerUtils {

    companion object{
        fun protoToDtoDepth(depthBook: com.secwager.Market.DepthBook) : com.secwager.dto.DepthBook {
            return com.secwager.dto.DepthBook(bids=depthBook.bidPricesList.zip(depthBook.bidQtysList){price,qty-> Level(price, qty)},
                    asks=depthBook.askPricesList.zip(depthBook.askQtysList){price,qty-> Level(price, qty)})
        }
    }
}