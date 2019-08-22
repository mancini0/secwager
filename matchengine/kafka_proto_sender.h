#pragma once

#include "proto_sender.h"
#include "librdkafka/rdkafkacpp.h"
#include <memory>
#include <google/protobuf/message_lite.h>

class KafkaProtoSender : public ProtoSender {

public:
    KafkaProtoSender(RdKafka::Producer *kafkaProducer, const std::unordered_map<std::string, RdKafka::Topic *> *
    topicsByMsgType);

    void send(const google::protobuf::MessageLite &msg, const std::string &address) override;

private:
    void send(const google::protobuf::MessageLite &msg, const std::string &key, RdKafka::Topic *topic);

    const std::unordered_map<std::string, RdKafka::Topic *> *msgTypeToTopic;
    RdKafka::Producer *kafkaProducer;
};