#pragma once

#include "proto_publisher.h"
#include "librdkafka/rdkafkacpp.h"
#include <memory>
#include <google/protobuf/message_lite.h>

class ProtoPublisherKafkaImpl : public ProtoPublisher {

public:
    ProtoPublisherKafkaImpl(RdKafka::Producer *kafkaProducer, const std::unordered_map<std::string, RdKafka::Topic *> *
    topicsByMsgType);

    void publish(const google::protobuf::MessageLite &msg, const std::string &address) override;

private:
    void publish(const google::protobuf::MessageLite &msg, const std::string &key, RdKafka::Topic *topic);

    const std::unordered_map<std::string, RdKafka::Topic *> *msgTypeToTopic;
    RdKafka::Producer *kafkaProducer;
};