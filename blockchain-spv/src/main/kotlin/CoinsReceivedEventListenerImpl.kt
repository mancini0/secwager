package com.secwager.blockchain;

import com.google.common.util.concurrent.FutureCallback
import com.google.common.util.concurrent.Futures
import com.secwager.blockchain.constants.Queries
import com.secwager.proto.cashier.CashierGrpc
import com.secwager.proto.cashier.CashierOuterClass
import com.secwager.proto.cashier.CashierOuterClass.TransactionReason;
import com.secwager.proto.cashier.CashierOuterClass.CashierRequest;
import com.secwager.proto.cashier.CashierOuterClass.CashierActionStatus.*
import io.grpc.stub.StreamObserver
import org.apache.commons.dbutils.QueryRunner
import org.bitcoinj.core.*
import org.bitcoinj.wallet.SendRequest
import org.bitcoinj.wallet.Wallet
import org.bitcoinj.wallet.listeners.WalletCoinsReceivedEventListener
import org.slf4j.LoggerFactory
import java.io.BufferedOutputStream
import java.io.ByteArrayOutputStream
import java.util.*
import javax.inject.Inject

class CoinsReceivedEventListenerImpl @Inject constructor(val queryRunner: QueryRunner, val netParams: NetworkParameters, val cashierStub: CashierGrpc.CashierStub) : WalletCoinsReceivedEventListener {

    companion object {
        private val log = LoggerFactory.getLogger(CoinsReceivedEventListenerImpl::class.java)
        private val minBlocks = Integer.parseInt(System.getenv("MIN_BLOCKS_BEFORE_SAFE") ?: "1")
    }


    override fun onCoinsReceived(wallet: Wallet, tx: Transaction, prevBalance: Coin, newBalance: Coin) {
        val outputStream = ByteArrayOutputStream();
        wallet.saveToFileStream(outputStream)
        queryRunner.execute(Queries.SAVE_WALLET, Base64.getEncoder().encodeToString(outputStream.toByteArray()))
        queryRunner.dataSource.connection.commit()
        val txConfirmed = tx.hasConfidence() && tx.confidence.depthInBlocks >= minBlocks
        val relevantOutputs = tx.outputs.filter { wallet.isAddressMine(it.scriptPubKey.getToAddress(netParams)) }
        relevantOutputs.forEach {
            val addr = it.scriptPubKey.getToAddress(wallet.params)
            val cashierRequest = CashierRequest.newBuilder()
                    .setP2PkhAddress(addr.toString())
                    .setAmount(it.value.value.toInt())
                    .setRelatedEntityId(it.parentTransactionHash.toString()).build()
            if (txConfirmed) {
                cashierStub.depositSafe(cashierRequest, DepositResponseObserver(txo = it, wasRiskyAtTimeOfDeposit = false, cashierStub = cashierStub, minBlocks = minBlocks))
            } else {
                cashierStub.depositRisky(cashierRequest, DepositResponseObserver(txo = it, wasRiskyAtTimeOfDeposit = true, cashierStub = cashierStub, minBlocks = minBlocks))
            }
        }
    }
}