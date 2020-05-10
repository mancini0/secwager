package com.secwager.cashier

import com.secwager.cashier.di.DaggerCashierComponent;
import com.secwager.grpc.GrpcServer;

fun main() {
    val server = GrpcServer(9305,
            DaggerCashierComponent.create().buildCashierService())
    server.start()
}