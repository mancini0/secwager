package com.secwager.refdata

import org.apache.kafka.clients.producer.KafkaProducer

import javax.inject.Inject

class ContractPublisher @Inject
constructor(val footballDataClient: FootballDataClient,
            val kafkaProducer: KafkaProducer<String, String>) {


    fun start() {


    }


}