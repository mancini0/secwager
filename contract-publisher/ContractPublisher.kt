package com.secwager.refdata

import java.time.Duration
import org.apache.kafka.clients.producer.KafkaProducer
import javax.inject.Inject

class ContractPublisher {
    val footballDataClient : FootballDataClient
    val publishInterval : Duration
    val leagues : List<Int>
    val kafkaProducer : KafkaProducer<String,String>

    @Inject
    constructor(footballDataClient : FootballDataClient, leagues: List<Int>, publishInterval : Duration,
                kafkaProducer : KafkaProducer<String,String> ){
        this.footballDataClient=footballDataClient
        this.publishInterval=publishInterval
        this.leagues =leagues
        this.kafkaProducer = kafkaProducer
    }


    fun start(){


    }






}