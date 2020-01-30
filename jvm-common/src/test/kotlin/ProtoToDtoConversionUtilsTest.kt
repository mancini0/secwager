package com.secwager.utils

import org.junit.Test
import com.google.common.truth.Truth.assertThat
import com.secwager.Market.DepthBook
import com.secwager.dto.Level
import com.secwager.utils.ProtoToDtoConversionUtils.Companion.protoToDtoDepth


class ProtoToDtoConversionUtilsTest {


    @Test
    fun `test empty`() {
        assertThat(protoToDtoDepth(DepthBook.newBuilder().build())).isEqualTo(com.secwager.dto.DepthBook(isin=""))
    }

    @Test
    fun testBids() {
        assertThat(protoToDtoDepth(DepthBook.newBuilder()
                .setSymbol("abc")
                .addAllBidQtys(listOf(100, 150))
                .addAllBidPrices(listOf(9900, 9800))
                .build())).isEqualTo(com.secwager.dto.DepthBook(isin="abc",bids = listOf(Level(qty = 100, price = 9900), Level(qty = 150, price = 9800))))
    }

    @Test
    fun testAsks() {
        assertThat(protoToDtoDepth(DepthBook.newBuilder()
                .setSymbol("abc")
                .addAllAskQtys(listOf(100, 150))
                .addAllAskPrices(listOf(9900, 9800))
                .build())).isEqualTo(com.secwager.dto.DepthBook(isin="abc",asks = listOf(Level(qty = 100, price = 9900), Level(qty = 150, price = 9800))))
    }

}