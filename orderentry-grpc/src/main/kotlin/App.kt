package com.secwager.orderentry
import io.grpc.ServerBuilder

import com.secwager.orderentry.di.DaggerOrderEntryComponent
fun main() {
    ServerBuilder.forPort(9085)
           // .intercept(JwtServerInterceptor())
            .addService(DaggerOrderEntryComponent.create().buildOrderEntryService())
            .build().awaitTermination()
}