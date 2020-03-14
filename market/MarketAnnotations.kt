package com.secwager.market

@Target(AnnotationTarget.FUNCTION)
annotation class SkipOnBookReconstruction

@Target(AnnotationTarget.FUNCTION)
annotation class RewindStateOnFailure