package com.secwager.blockchain

import com.google.common.util.concurrent.FutureCallback
import com.google.common.util.concurrent.Futures
import com.secwager.proto.cashier.CashierGrpc
import com.secwager.proto.cashier.CashierOuterClass.*
import io.grpc.stub.StreamObserver
import org.bitcoinj.core.TransactionConfidence
import org.bitcoinj.core.TransactionOutput
import org.slf4j.LoggerFactory
import java.util.concurrent.ForkJoinPool

class DepositResponseObserver(val txo: TransactionOutput, val wasRiskyAtTimeOfDeposit: Boolean, val cashierStub: CashierGrpc.CashierStub, val minBlocks: Int) : StreamObserver<CashierActionResult> {
    companion object {
        private val log = LoggerFactory.getLogger(DepositResponseObserver::class.java)
    }

    override fun onCompleted() {
    }

    override fun onNext(result: CashierActionResult) {
        when (result.status) {
            CashierActionStatus.SUCCESS -> {
                log.debug("deposited fine:  {}", result)
                if (wasRiskyAtTimeOfDeposit) {
                    Futures.addCallback(txo.parentTransaction?.confidence?.getDepthFuture(1), object : FutureCallback<TransactionConfidence> {
                        override fun onSuccess(confidence: TransactionConfidence?) {
                            if (confidence?.depthInBlocks ?: 0 >= minBlocks) {
                                cashierStub.unlockFunds(CashierRequest.newBuilder()
                                        .setUserId(result.userId)
                                        .setAmount(txo.value.longValue().toInt())
                                        .setReason(TransactionReason.RISKY_DEPOSIT_BECOMES_SAFE)
                                        .setRelatedEntityId(txo.parentTransactionHash.toString())
                                        .build(), object : StreamObserver<CashierActionResult> {
                                    override fun onNext(result: CashierActionResult) {
                                        when (result.status) {
                                            CashierActionStatus.SUCCESS -> log.debug("Successfully unlocked funds associated with tx {} index {}",
                                                    txo.parentTransactionHash, txo.index)

                                            else -> log.warn("todo -  handle this unlock failure scenario: {}", result);
                                        }
                                    }

                                    override fun onError(t: Throwable) {
                                        log.error("exception encountered unlocking escrowed funds: {}", t)
                                    }

                                    override fun onCompleted() {
                                        TODO("Not yet implemented")
                                    }
                                });
                            }
                        }

                        override fun onFailure(t: Throwable) {
                            log.error("Error in tx confidence callback: {}", t)
                        }

                    }, ForkJoinPool.commonPool())
                }
            }
            else -> log.warn("handle this scenario: {}", result);
        }

    }

    override fun onError(t: Throwable?) {
        log.error("exception calling cashier.deposit: {}", t);
    }
}