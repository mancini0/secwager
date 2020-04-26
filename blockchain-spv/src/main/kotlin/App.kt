package com.secwager.blockchain

import com.secwager.proto.cashier.CashierGrpc
import org.bitcoinj.core.Address
import org.bitcoinj.core.BlockChain
import org.bitcoinj.core.ECKey
import org.bitcoinj.core.PeerGroup
import org.bitcoinj.net.discovery.DnsDiscovery
import org.bitcoinj.params.TestNet3Params
import org.bitcoinj.script.Script
import org.bitcoinj.store.SPVBlockStore
import org.bitcoinj.wallet.Wallet
import org.slf4j.LoggerFactory
import java.io.File
import java.lang.invoke.MethodHandles

private val log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass().`package`.name)


fun main() {
    val netParams = TestNet3Params.get()
    val blockStore = SPVBlockStore(netParams, File("./blockStore"))
    val wallet = Wallet.createBasic(netParams)
    wallet.isAcceptRiskyTransactions = true
    val key = ECKey()
    wallet.importKey(key);
    log.info("pay me: {}", Address.fromKey(netParams, key, Script.ScriptType.P2PKH));
    Thread.sleep(30000)
    val chain = BlockChain(netParams, blockStore)
    val peerGroup = PeerGroup(netParams, chain)
    wallet.addCoinsReceivedEventListener(CoinReceiver(netParams, CashierGrpc.CashierStub()))
    peerGroup.setUserAgent("hs", "1.00")
    peerGroup.addWallet(wallet)
    peerGroup.addPeerDiscovery(DnsDiscovery(netParams))
    peerGroup.start()
    peerGroup.downloadBlockChain()
    Thread.sleep(Long.MAX_VALUE)

}