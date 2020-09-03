package com.secwager.orderentry

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.spy
import com.secwager.emergency.EmergencyService
import com.secwager.proto.cashier.CashierGrpcKt
import io.grpc.ManagedChannel
import io.grpc.Metadata
import io.grpc.inprocess.InProcessChannelBuilder
import io.grpc.inprocess.InProcessServerBuilder
import io.grpc.stub.MetadataUtils
import io.grpc.testing.GrpcCleanupRule
import kotlinx.coroutines.runBlocking
import org.apache.kafka.clients.producer.KafkaProducer
import org.junit.Rule
import org.junit.Test
import java.util.*


class OrderEntryServiceTest {


    @get:Rule
    val grpcCleanup = GrpcCleanupRule()

    private val serverName: String = InProcessServerBuilder.generateName()

    private val emergencyService: EmergencyService = mock()
    private val kafkaProducer: KafkaProducer<String, ByteArray> = mock()
    private val mockCashierService: CashierGrpcKt.CashierCoroutineImplBase = spy()

    private val channel: ManagedChannel = grpcCleanup.register(InProcessChannelBuilder
            .forName(serverName).intercept().directExecutor().build())

    private val cashierStub = CashierGrpcKt.CashierCoroutineStub(channel)
    private val orderEntryStub = OrderEntryServiceGrpcKt.OrderEntryServiceCoroutineStub(channel)
    private val grpcServer = grpcCleanup.register(InProcessServerBuilder
            .forName(serverName).directExecutor()
            .intercept(JwtServerInterceptor())
            .addService(mockCashierService)
            .addService(OrderEntryServiceImpl(kafkaProducer, emergencyService, cashierStub))
            .build()).start()


    @Test
    fun f() {
        runBlocking {

            val headers = Metadata()
            val key = Metadata.Key.of(JwtServerInterceptor.ENCODED_JWT_PAYLOAD_KEY, Metadata.ASCII_STRING_MARSHALLER)
            val jwtJson = "{\"sub\":\"myuserid\"}"
            headers.put(key,
                    Base64.getEncoder().encodeToString(jwtJson.toByteArray()))
            val stubWithHeaders = MetadataUtils.attachHeaders(orderEntryStub, headers);
            val result = stubWithHeaders.submitOrder(OrderEntry.SubmitOrderRequest.getDefaultInstance())


        }
    }


}