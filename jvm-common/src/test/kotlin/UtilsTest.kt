package com.secwager.utils

import org.junit.Test
import com.google.common.truth.Truth.assertThat
import com.secwager.Market.DepthBook
import com.secwager.utils.SecwagerUtils.Companion.protoToDtoDepth


class UtilsTest {

    companion object {
        val EMPTY_PROTO_DEPTH = DepthBook.newBuilder().build()
    }

    @Test
    fun `empty proto depth to dto depth`() {
        assertThat(protoToDtoDepth(EMPTY_PROTO_DEPTH)).isEqualTo(com.secwager.dto.DepthBook())
    }

}