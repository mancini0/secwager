package com.secwager.blockchain.di


import com.secwager.blockchain.CoinsReceivedEventListenerImpl
import com.secwager.proto.cashier.CashierGrpc
import com.secwager.proto.cashier.CashierGrpc.CashierStub
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import dagger.Module
import dagger.Provides
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import org.apache.commons.dbutils.QueryRunner
import org.bitcoinj.core.BlockChain
import org.bitcoinj.core.NetworkParameters
import org.bitcoinj.core.PeerGroup
import org.bitcoinj.core.Sha256Hash
import org.bitcoinj.net.discovery.DnsDiscovery
import org.bitcoinj.params.MainNetParams
import org.bitcoinj.params.TestNet3Params
import org.bitcoinj.store.BlockStore
import org.bitcoinj.store.SPVBlockStore
import org.bitcoinj.wallet.Wallet
import org.bitcoinj.wallet.listeners.WalletCoinsReceivedEventListener
import org.postgresql.ds.PGSimpleDataSource
import java.io.File
import java.util.*
import javax.inject.Named
import javax.inject.Singleton
import javax.sql.DataSource

@Module
class BlockchainListenerModule {


    @Provides
    @Singleton
    fun provideDataSource(): DataSource {
        val props = Properties()
        props.setProperty("dataSourceClassName", "org.postgresql.ds.PGSimpleDataSource")
        props.setProperty("dataSource.user", "yugabyte")
        props.setProperty("dataSource.password", "yugabyte")
        props.setProperty("dataSource.databaseName", "secwager")
        props.setProperty("dataSource.serverName", "localhost")
        props.setProperty("dataSource.portNumber", "5433")
        val config = HikariConfig(props)
        config.isAutoCommit = false
        val ds = HikariDataSource(config)
        return ds;
    }

    @Provides
    @Singleton
    fun provideQueryRunner(dataSource: DataSource): QueryRunner {
        return QueryRunner(dataSource)
    }

    @Singleton
    @Provides
    @Named("cashierChannel")
    fun cashierGrpcChannel(): ManagedChannel {
        return ManagedChannelBuilder
                .forAddress(System.getenv("CASHIER_SVC_HOST"), Integer.parseInt(System.getenv("CASHIER_SVC_PORT")))
                .usePlaintext()
                .build()
    }


    @Provides
    @Singleton
    fun provideCashierClient(@Named("cashierChannel") channel: ManagedChannel): CashierStub {
        return CashierGrpc.newStub(channel)
    }

    @Provides
    @Singleton
    fun provideWallet(queryRunner: QueryRunner, networkParameters: NetworkParameters, coinsReceivedListener: WalletCoinsReceivedEventListener): Wallet {
        val walletBytes: ByteArray = queryRunner.query("select bytes from wallet_data where save_time = (select max(save_time) from wallet_data)"
        ) { resultSet -> if (resultSet.next()) Base64.getDecoder().decode(resultSet.getString("bytes")) else ByteArray(0) }
        val wallet = if (walletBytes.isNotEmpty()) Wallet.loadFromFileStream(walletBytes.inputStream()) else Wallet.createBasic(networkParameters)
        if (!wallet.isConsistent) throw IllegalStateException("wallet is not consistent!")
        wallet.isAcceptRiskyTransactions = true
        wallet.addCoinsReceivedEventListener(coinsReceivedListener)
        return wallet;
    }


    @Provides
    @Singleton
    fun provideCoinsReceivedEventListener(queryRunner: QueryRunner, networkParameters: NetworkParameters, cashierStub: CashierStub): WalletCoinsReceivedEventListener {
        return CoinsReceivedEventListenerImpl(queryRunner, networkParameters, cashierStub)
    }


    @Provides
    @Singleton
    fun provideNetworkParameters(): NetworkParameters {
        return when (System.getenv("BITCOIN_NETWORK")) {
            "MAIN" -> MainNetParams.get()
            else -> TestNet3Params.get()
        }
    }


    @Provides
    @Singleton
    fun provideBlockStore(networkParameters: NetworkParameters): BlockStore {
        return SPVBlockStore(networkParameters, File("./blockstore"))
    }

    @Provides
    @Singleton
    fun providePeerGroup(networkParameters: NetworkParameters, blockStore: BlockStore, wallet: Wallet): PeerGroup {
        val peerGroup = PeerGroup(networkParameters, BlockChain(networkParameters, blockStore))
        peerGroup.setUserAgent(UUID.randomUUID().toString(), "1.00")
        peerGroup.addWallet(wallet)
        peerGroup.addPeerDiscovery(DnsDiscovery(networkParameters))
        return peerGroup;
    }


}