package com.secwager.orderentry

import com.secwager.emergency.EmergencyService
import com.secwager.orderentry.JwtServerInterceptor.Companion.UID_CTX_KEY
import com.secwager.orderentry.OrderEntry.OrderSubmissionStatus
import com.secwager.orderentry.OrderEntry.OrderSubmissionStatus.FAILURE_INVALID_ORDER
import com.secwager.orderentry.OrderEntry.SubmitOrderResponse
import com.secwager.proto.Market
import com.secwager.proto.Market.Order.OrderType.*
import com.secwager.proto.cashier.CashierGrpcKt
import com.secwager.proto.cashier.CashierOuterClass.CashierActionStatus.*
import com.secwager.proto.cashier.CashierOuterClass.CashierRequest
import com.secwager.proto.cashier.CashierOuterClass.TransactionReason
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord
import org.slf4j.LoggerFactory
import java.time.LocalDateTime
import java.util.*
import javax.inject.Inject

class OrderEntryServiceImpl @Inject constructor(private val kafkaProducer: KafkaProducer<String, ByteArray>,
                                                private val emergencyService: EmergencyService,
                                                private val cashierStub: CashierGrpcKt.CashierCoroutineStub) : OrderEntryServiceGrpcKt.OrderEntryServiceCoroutineImplBase() {

    companion object {
        private val log = LoggerFactory.getLogger(OrderEntryServiceImpl::class.java)
        private const val SATOSHIS_PER_CONTRACT = 100000
        private const val MAX_PRICE_OF_BINARY_OPTION = 100
    }

    override suspend fun submitOrder(request: OrderEntry.SubmitOrderRequest):
            SubmitOrderResponse {
        val uid: String = UID_CTX_KEY.get()
        val responseBuilder = SubmitOrderResponse.newBuilder()
        val order = request.order
        if (!isOrderValid(order)) {
            return responseBuilder.setOrderSubmissionStatus(FAILURE_INVALID_ORDER).build()
        }
        when (order.orderType) {
            BUY, SELL -> {
                /**for new orders (not 'cancels' or 'modifies'), we need to post margin and generate
                an order id.**/
                val orderId = UUID.randomUUID().toString()
                val marginAmount = order.qtyOnMarket *
                        SATOSHIS_PER_CONTRACT * MAX_PRICE_OF_BINARY_OPTION
                val lockFundsResult = cashierStub.lockFunds(CashierRequest.newBuilder()
                        .setUserId(uid)
                        .setRelatedEntityId(orderId)
                        .setReason(TransactionReason.POST_MARGIN)
                        .setAmount(marginAmount).build())
                when (lockFundsResult.status) {
                    SUCCESS -> {
                        return submit(order.toBuilder().setOrderId(orderId)
                                .setTraderId(uid).build())
                    }
                    FAILURE_USER_NOT_FOUND -> {
                        return responseBuilder
                                .setOrderSubmissionStatus(OrderSubmissionStatus.FAILURE_INTERNAL_ERROR)
                                .build()
                    }
                    FAILURE_INSUFFICIENT_FUNDS -> {
                        return responseBuilder
                                .setOrderSubmissionStatus(OrderSubmissionStatus.FAILURE_INSUFFICIENT_FUNDS)
                                .build()
                    }
                    else -> {
                        return responseBuilder
                                .setOrderSubmissionStatus(OrderSubmissionStatus.FAILURE_INTERNAL_ERROR)
                                .build()
                    }
                }
            }
            CANCEL -> {
                return submit(order.toBuilder().setTraderId(uid).build())
            }
            else -> {
                log.warn("Unhandled order type: {}", order.orderType)
                return responseBuilder
                        .setOrderSubmissionStatus(OrderSubmissionStatus.FAILURE_INTERNAL_ERROR)
                        .build()
            }
        }
    }


    private suspend fun submit(order: Market.Order): SubmitOrderResponse {
        val responseBuilder = SubmitOrderResponse.newBuilder()
        kotlin.runCatching {
            kafkaProducer.beginTransaction()
            kafkaProducer.send(ProducerRecord(order.isin, order.toByteArray()))
            kafkaProducer.commitTransaction()
            responseBuilder.setOrderSubmissionStatus(OrderSubmissionStatus.SUCCESS)
        }.onFailure {
            responseBuilder.setOrderSubmissionStatus(OrderSubmissionStatus.FAILURE_INTERNAL_ERROR)
            kotlin.runCatching {
                val unlockResult = cashierStub.unlockFunds(CashierRequest.newBuilder()
                        .setUserId(order.traderId).setRelatedEntityId(order.orderId).setReason(TransactionReason.RESET_MARGIN).build())
                if (!unlockResult.status.equals(SUCCESS)) throw RuntimeException("the unlock request" +
                        "to reset margin for failed order ${order.orderId} returned ${unlockResult.status.name} instead of " +
                        "${SUCCESS.name}")
            }.onFailure {
                log.error("could not unlock funds after order publish failed...requesting manual intervention")
                emergencyService.requestIntervention("could not unlock funds after order publish failed",
                        LocalDateTime.now(), order.traderId, it)
            }
            kotlin.runCatching {
                kafkaProducer.abortTransaction()
            }
        }
        return responseBuilder.build()
    }

    //TODO add proper validation
    private fun isOrderValid(o: Market.Order): Boolean {
        return o.qtyOnMarket > 0
    }

}