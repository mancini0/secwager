package com.secwager.utils

import org.junit.Test
import com.google.common.truth.Truth.assertThat
import com.secwager.proto.Market.Depth
import com.secwager.dto.Level
import com.secwager.utils.ConversionUtils.Companion.protoToDepth


class ProtoToDtoConversionUtilsTest {


    @Test
    fun `test empty`() {
        assertThat(protoToDepth(Depth.newBuilder().build())).isEqualTo(com.secwager.dto.DepthBook(isin=""))
    }

    @Test
    fun testBids() {
        assertThat(protoToDepth(Depth.newBuilder()
                .setIsin("abc")
                .addAllBidQtys(listOf(100, 150))
                .addAllBidPrices(listOf(9900, 9800))
                .build())).isEqualTo(com.secwager.dto.DepthBook(isin="abc",bids = listOf(Level(qty = 100, price = 9900), Level(qty = 150, price = 9800))))
    }

    @Test
    fun testAsks() {
        assertThat(protoToDepth(Depth.newBuilder()
                .setIsin("abc")
                .addAllAskQtys(listOf(100, 150))
                .addAllAskPrices(listOf(9900, 9800))
                .build())).isEqualTo(com.secwager.dto.DepthBook(isin="abc",asks = listOf(Level(qty = 100, price = 9900), Level(qty = 150, price = 9800))))
    }

}