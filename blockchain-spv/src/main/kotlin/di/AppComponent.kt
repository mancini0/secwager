package com.secwager.blockchain.di

import com.secwager.blockchain.UserECKeyReceiver
import dagger.Component
import org.bitcoinj.core.PeerGroup
import javax.inject.Singleton


@Singleton
@Component(modules = [BlockchainListenerModule::class])
interface AppComponent {
    fun buildUserECKeyReceiver(): UserECKeyReceiver
    fun buildPeerGroup(): PeerGroup
}