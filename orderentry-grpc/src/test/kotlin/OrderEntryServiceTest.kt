package com.secwager.orderentry


import com.secwager.cashier.CashierServiceImpl
import com.secwager.emergency.EmergencyService
import com.secwager.proto.Market
import com.secwager.proto.cashier.CashierGrpcKt
import com.secwager.proto.cashier.CashierOuterClass
import io.grpc.ManagedChannel
import io.grpc.Metadata
import io.grpc.inprocess.InProcessChannelBuilder
import io.grpc.inprocess.InProcessServerBuilder
import io.grpc.stub.MetadataUtils
import io.grpc.testing.GrpcCleanupRule
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.runBlocking
import org.apache.kafka.clients.producer.KafkaProducer
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.*


class OrderEntryServiceTest {


    @get:Rule
    val grpcCleanup = GrpcCleanupRule()


    private val orderEntryServerName: String = InProcessServerBuilder.generateName()
    private val cashierServerName: String = InProcessServerBuilder.generateName()

    private val cashierService = spyk<CashierGrpcKt.CashierCoroutineImplBase>();
    private val kafkaProducer = mockk<KafkaProducer<String,ByteArray>>()
    private val emergencyService = mockk<EmergencyService>()


    private val orderChannel: ManagedChannel = grpcCleanup.register(InProcessChannelBuilder
            .forName(orderEntryServerName).directExecutor().build())

    private val cashierChannel: ManagedChannel = grpcCleanup.register(InProcessChannelBuilder
            .forName(cashierServerName).directExecutor().build())

    private val cashierStub = CashierGrpcKt.CashierCoroutineStub(cashierChannel)
    private val orderEntryStub = OrderEntryServiceGrpcKt.OrderEntryServiceCoroutineStub(orderChannel)

    private val orderEntryServer = grpcCleanup.register(InProcessServerBuilder
            .forName(orderEntryServerName).directExecutor()
            .intercept(JwtServerInterceptor())
            .addService(OrderEntryServiceImpl(kafkaProducer, emergencyService, cashierStub))
            .build()).start()

    private val cashierServer = grpcCleanup.register(InProcessServerBuilder
            .forName(cashierServerName).directExecutor()
            .addService(cashierService)
            .build()).start()

    private lateinit var orderStubWithHeaders: OrderEntryServiceGrpcKt.OrderEntryServiceCoroutineStub


    @Before
    fun attachHeaders() {
        val headers = Metadata()
        val key = Metadata.Key.of(JwtServerInterceptor.ENCODED_JWT_PAYLOAD_KEY, Metadata.ASCII_STRING_MARSHALLER)
        val jwtJson = "{\"sub\":\"testuser\"}"
        headers.put(key,
                Base64.getEncoder().encodeToString(jwtJson.toByteArray()))
        orderStubWithHeaders = MetadataUtils.attachHeaders(orderEntryStub, headers)
    }

    @Test
    fun insufficientFunds() {
        runBlocking {
        coEvery { cashierService.lockFunds(any()) } returns CashierOuterClass
                .CashierActionResult.newBuilder()
                .setStatus(CashierOuterClass.CashierActionStatus.SUCCESS).build()


            val result = orderStubWithHeaders.submitOrder(OrderEntry.SubmitOrderRequest
                    .newBuilder().setOrder(Market.Order.newBuilder()
                            .setOrderType(Market.Order.OrderType.BUY)
                            .setIsin("IBM")
                            .setQtyOnMarket(100).build()).build())
            println(result)
        }
    }
}
