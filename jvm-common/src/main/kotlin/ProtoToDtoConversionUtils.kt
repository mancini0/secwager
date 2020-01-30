package com.secwager.utils;
import com.secwager.dto.Level

class ProtoToDtoConversionUtils {

    companion object{
        fun protoToDtoDepth(depthBook: com.secwager.Market.DepthBook) : com.secwager.dto.DepthBook {
            return com.secwager.dto.DepthBook(isin=depthBook.symbol, bids=depthBook.bidPricesList.zip(depthBook.bidQtysList){price,qty-> Level(price, qty)},
                    asks=depthBook.askPricesList.zip(depthBook.askQtysList){price,qty-> Level(price, qty)})
        }
    }
}