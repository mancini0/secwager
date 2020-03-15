package com.secwager.market

import com.secwager.matchengine.CallbackExecutor
import kotlin.reflect.jvm.reflect

class CallbackExecutorImpl : CallbackExecutor {
    private val endReplayOffset: Long

    constructor(endReplayOffset: Long) {
        this.endReplayOffset = endReplayOffset
    }


    var currentOffset: Long = 0;
    override fun executeCallbacks(callbacks: MutableCollection<() -> Any>) {
        callbacks.forEach { println("offset ${currentOffset} name: ${it.reflect()?.name}, annots: ${it.reflect()?.annotations}") }

    }

}