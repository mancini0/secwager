package com.secwager.orderentry


import com.google.common.truth.Truth.assertThat
import com.secwager.intervention.InterventionService
import com.secwager.proto.Market.*
import com.secwager.proto.Market.Order.OrderType
import com.secwager.proto.cashier.CashierGrpcKt
import com.secwager.proto.cashier.CashierOuterClass.*
import io.grpc.ManagedChannel
import io.grpc.Metadata
import io.grpc.inprocess.InProcessChannelBuilder
import io.grpc.inprocess.InProcessServerBuilder
import io.grpc.stub.MetadataUtils
import io.grpc.testing.GrpcCleanupRule
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.runBlocking
import org.apache.kafka.clients.producer.MockProducer
import org.apache.kafka.clients.producer.ProducerRecord
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.*


class OrderEntryServiceTest {


    @get:Rule
    val grpcCleanup = GrpcCleanupRule()


    private val orderEntryServerName: String = InProcessServerBuilder.generateName()
    private val cashierServerName: String = InProcessServerBuilder.generateName()

    private val cashierService = spyk<CashierGrpcKt.CashierCoroutineImplBase>()

    private val kafkaProducer  = MockProducer<String,ByteArray>().apply {
        this.initTransactions()
    }
    private val interventionService = mockk<InterventionService>()


    private val orderChannel: ManagedChannel = grpcCleanup.register(InProcessChannelBuilder
            .forName(orderEntryServerName).directExecutor().build())

    private val cashierChannel: ManagedChannel = grpcCleanup.register(InProcessChannelBuilder
            .forName(cashierServerName).directExecutor().build())

    private val cashierStub = CashierGrpcKt.CashierCoroutineStub(cashierChannel)
    private val orderEntryStubNoHeaders = OrderEntryServiceGrpcKt.OrderEntryServiceCoroutineStub(orderChannel)

    private val orderEntryServer = grpcCleanup.register(InProcessServerBuilder
            .forName(orderEntryServerName).directExecutor()
            .intercept(JwtServerInterceptor())
            .addService(OrderEntryServiceImpl(kafkaProducer, interventionService, cashierStub))
            .build()).start()

    private val cashierServer = grpcCleanup.register(InProcessServerBuilder
            .forName(cashierServerName).directExecutor()
            .addService(cashierService)
            .build()).start()

    private lateinit var orderStub: OrderEntryServiceGrpcKt.OrderEntryServiceCoroutineStub


    @Before
    fun attachHeaders() {
        val headers = Metadata()
        val key = Metadata.Key.of(JwtServerInterceptor.ENCODED_JWT_PAYLOAD_KEY, Metadata.ASCII_STRING_MARSHALLER)
        val jwtJson = "{\"sub\":\"testuser\"}"
        headers.put(key,
                Base64.getEncoder().encodeToString(jwtJson.toByteArray()))
        orderStub = MetadataUtils.attachHeaders(orderEntryStubNoHeaders, headers)
    }

    @Test
    fun insufficientFunds(){
        runBlocking {
            coEvery { cashierService.lockFunds(any()) } returns
                    CashierActionResult.newBuilder()
                            .setStatus(CashierActionStatus.FAILURE_INSUFFICIENT_FUNDS).build()

            val result = orderStub.submitOrder(OrderEntry.SubmitOrderRequest
                    .newBuilder().setOrder(Order.newBuilder()
                            .setOrderType(OrderType.BUY)
                            .setIsin("IBM")
                            .setQtyOnMarket(100).build()).build())

            assertThat(result.orderSubmissionStatus)
                    .isEqualTo(OrderEntry.OrderSubmissionStatus.FAILURE_INSUFFICIENT_FUNDS)

            assertThat(kafkaProducer.history()).isEmpty()
        }
    }

    @Test
            /**When a valid order is submitted and the customer has sufficient funds to post
             * margin, the application should publish the trade to kafka's order-entry topic.
             * The key of the message should be equal to the instruments symbol.
             */

    fun success() {
        runBlocking {
        coEvery { cashierService.lockFunds(any()) } returns
                CashierActionResult.newBuilder()
                .setStatus(CashierActionStatus.SUCCESS).build()

            val result = orderStub.submitOrder(OrderEntry.SubmitOrderRequest
                    .newBuilder().setOrder(Order.newBuilder()
                            .setOrderType(OrderType.BUY)
                            .setIsin("IBM")
                            .setQtyOnMarket(100).build()).build())


            assertThat(kafkaProducer.history()).hasSize(1)
            assertThat(kafkaProducer.history().get(0).topic()).isEqualTo("order-entry")
            assertThat(kafkaProducer.history().get(0).key()).isEqualTo("IBM")
            assertThat(Order.parseFrom(kafkaProducer.history().get(0).value()).orderType)
                    .isEqualTo(OrderType.BUY)
            assertThat(result.orderSubmissionStatus)
                    .isEqualTo(OrderEntry.OrderSubmissionStatus.SUCCESS)
        }
    }
}
