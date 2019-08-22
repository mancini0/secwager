#include "kafka_proto_sender.h"
#include "proto/matchengine.pb.h"

KafkaProtoSender::KafkaProtoSender(RdKafka::Producer *kafkaProducer,
                                   const std::unordered_map<std::string, RdKafka::Topic *> *msgTypeToTopic)
        : kafkaProducer(kafkaProducer), msgTypeToTopic(msgTypeToTopic) {}


void KafkaProtoSender::send(const google::protobuf::MessageLite &msg, const std::string &address) {
    send(msg, address, msgTypeToTopic->at(msg.GetTypeName()));
}


void KafkaProtoSender::send(const google::protobuf::MessageLite &msg, const std::string &key,
                            RdKafka::Topic *topic) {
    int size = msg.ByteSize();
    char *array = new char[size];
    msg.SerializeToArray(array, size);
    kafkaProducer->produce(topic, RdKafka::Topic::PARTITION_UA, RdKafka::Producer::RK_MSG_FREE,
                           array, size, &key,
                           nullptr);
}