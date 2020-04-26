package com.secwager.blockchain;

import org.bitcoinj.core.Address
import org.bitcoinj.core.Coin
import org.bitcoinj.core.NetworkParameters
import org.bitcoinj.core.Transaction
import org.bitcoinj.wallet.SendRequest
import org.bitcoinj.wallet.Wallet
import org.bitcoinj.wallet.listeners.WalletCoinsReceivedEventListener
import org.slf4j.LoggerFactory
import javax.inject.Inject

class CoinReceiver @Inject constructor(val netParams: NetworkParameters) : WalletCoinsReceivedEventListener {

    companion object{
        private val log = LoggerFactory.getLogger(com.secwager.blockchain.CoinReceiver::class.java)
    }
    override fun onCoinsReceived(wallet: Wallet, tx: Transaction, prevBalance: Coin,  newBalance: Coin) {
        val minBlocks = Integer.parseInt(System.getenv("MIN_BLOCKS_BEFORE_SAFE") ?: "1")
        val confirmed = tx.hasConfidence() && tx.confidence.depthInBlocks >= minBlocks
        if(!confirmed){

        }
        tx.outputs.filter {wallet.isAddressMine(it.scriptPubKey.getToAddress(netParams))}.forEach{

        }
        val faucetAddress = Address.fromString(netParams, "mkHS9ne12qx9pS9VojpwU5xtRd4T7X7ZUt")
        wallet.sendCoins(SendRequest.to(faucetAddress,newBalance-prevBalance)).broadcastComplete.get()
    }
}