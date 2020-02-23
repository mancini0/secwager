#pragma once

#include <memory>
#include "librdkafka/rdkafkacpp.h"
#include "boost/fiber/unbuffered_channel.hpp"
#include "proto/market.pb.h"

class KafkaInboundOrderChannel {
    typedef std::shared_ptr<secwager::Order> OrderPtr;
public:

    KafkaInboundOrderChannel() = delete;

    KafkaInboundOrderChannel(RdKafka::KafkaConsumer *kafkaConsumer,
                             boost::fibers::unbuffered_channel<OrderPtr> *orderInboundChannel);

    void start();


private:
    boost::fibers::unbuffered_channel<OrderPtr> *orderInboundChannel;
    RdKafka::KafkaConsumer *kafkaConsumer;
    bool isPolling;


};


