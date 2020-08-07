package com.secwager.cashier

import com.secwager.cashier.di.DaggerCashierComponent;
import io.grpc.*


fun main() {
    ServerBuilder.forPort(9305)
            .addService(DaggerCashierComponent.create().buildCashierService())
            .build()
            .awaitTermination();
}