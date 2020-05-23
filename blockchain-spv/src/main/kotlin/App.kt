package com.secwager.blockchain

import com.google.cloud.ServiceOptions
import com.google.cloud.pubsub.v1.Subscriber
import com.google.pubsub.v1.ProjectSubscriptionName
import com.secwager.blockchain.di.DaggerAppComponent

fun main() {
    val appComponent = DaggerAppComponent.create()
    val peerGroup = appComponent.buildPeerGroup()
    peerGroup.start()
    peerGroup.downloadBlockChain()
    val subscriber = Subscriber.newBuilder(ProjectSubscriptionName.of(
            ServiceOptions.getDefaultProjectId(), "ec-key-subscription"), appComponent.buildUserECKeyReceiver()).build()
    subscriber.startAsync().awaitRunning()
    subscriber.awaitTerminated()
}