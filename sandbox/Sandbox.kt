package com.secwager.helloworld

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class Sandbox : GreeterGrpcKt.GreeterCoroutineImplBase() {

    override fun sayHello(request: HelloRequest): Flow<HelloReply> {
        return flowOf(HelloReply.newBuilder().build());
    }
}