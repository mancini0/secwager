package com.secwager.market

import com.secwager.matchengine.CallbackExecutor
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.jvm.reflect

class CallbackExecutorImpl : CallbackExecutor{
    private val endReplayOffset : Int = 0

    override fun executeCallbacks(callbacks: MutableCollection<() -> Any>) {

    }


}