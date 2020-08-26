package com.secwager.orderentry

import com.nhaarman.mockitokotlin2.mock
import com.secwager.cashier.CashierServiceImpl
import com.secwager.dao.cashier.CashierDao
import com.secwager.emergency.EmergencyService
import com.secwager.proto.cashier.CashierGrpcKt
import io.grpc.ManagedChannel
import io.grpc.Server
import io.grpc.inprocess.InProcessChannelBuilder
import io.grpc.inprocess.InProcessServerBuilder
import io.grpc.testing.GrpcCleanupRule
import kotlinx.coroutines.runBlocking
import org.apache.kafka.clients.producer.KafkaProducer
import org.junit.Rule
import org.junit.Test


class OrderEntryServiceTest {

    @get:Rule
    val grpcCleanup = GrpcCleanupRule()

    private val serverName: String = InProcessServerBuilder.generateName()

    private val emergencyService: EmergencyService = mock()
    private val kafkaProducer:KafkaProducer<String,ByteArray> = mock()
    private val cashierDao : CashierDao = mock()
    private val alt: CashierGrpcKt.CashierCoroutineImplBase = mock()

    private val cashierService = CashierServiceImpl(cashierDao)

    private val channel: ManagedChannel = grpcCleanup.register(InProcessChannelBuilder
            .forName(serverName).directExecutor().build())

    private val cashierStub = CashierGrpcKt.CashierCoroutineStub(channel)

    private val grpcServer = grpcCleanup.register(InProcessServerBuilder
            .forName(serverName).directExecutor()
            .intercept(JwtServerInterceptor())
            .addService(alt)
            .addService(OrderEntryServiceImpl(kafkaProducer,emergencyService,cashierStub))
            .build()).start()

    @Test
    fun f()  {
        runBlocking{
//            val orderStub = OrderEntryServiceGrpcKt.OrderEntryServiceCoroutineStub(channel)
//            orderStub.submitOrder(OrderEntry.SubmitOrderRequest.getDefaultInstance())
        }
    }


}