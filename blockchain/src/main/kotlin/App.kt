package com.secwager.blockchain
import org.bitcoinj.core.BlockChain
import org.bitcoinj.core.PeerGroup
import org.bitcoinj.params.TestNet3Params
import org.bitcoinj.store.PostgresFullPrunedBlockStore
import org.bitcoinj.wallet.Wallet
import org.bitcoinj.net.discovery.DnsDiscovery

fun main(){
    val netParams = TestNet3Params.get()
    val blockStore = PostgresFullPrunedBlockStore(netParams, 1000,
            "yb-tservers-hello-ybdb-cluster.rook-yugabytedb.svc.cluster.local:5433",
            "postgres","yugabyte","yugabyte")
    val wallet = Wallet.createBasic(netParams)
    val chain = BlockChain(netParams,blockStore)
    val peerGroup = PeerGroup(netParams,chain)
    peerGroup.setUserAgent("secwager","1.00")
    peerGroup.addWallet(wallet)
    peerGroup.addPeerDiscovery(DnsDiscovery(netParams))
    peerGroup.start()
    peerGroup.downloadBlockChain()
}