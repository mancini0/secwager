package com.secwager.orderentry

import com.secwager.proto.cashier.CashierGrpcKt
import io.grpc.testing.GrpcCleanupRule
import io.grpc.inprocess.InProcessServerBuilder
import io.grpc.inprocess.InProcessChannelBuilder
import org.apache.kafka.clients.producer.KafkaProducer
import org.junit.Rule
import org.junit.Test

class OrderEntryServiceTest {

    @Rule
    val grpcCleanup = GrpcCleanupRule()

    private val serverName: String = InProcessServerBuilder.generateName()

    private val serverBuilder: InProcessServerBuilder = InProcessServerBuilder
            .forName(serverName).intercept(JwtServerInterceptor()).directExecutor()

    private val channelBuilder: InProcessChannelBuilder = InProcessChannelBuilder
            .forName(serverName)

    @Test
    fun f() {
    }


}