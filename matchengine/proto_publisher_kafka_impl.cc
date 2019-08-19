#include "proto_publisher_kafka_impl.h"
#include "proto/matchengine.pb.h"

ProtoPublisherKafkaImpl::ProtoPublisherKafkaImpl(RdKafka::Producer *kafkaProducer,
                                                 RdKafka::Topic *tradeTopic, RdKafka::Topic *depthTopic,
                                                 RdKafka::Topic *orderEventTopic) : kafkaProducer(kafkaProducer) {

    msgTypeToTopic["secwager.DepthBook"] = depthTopic;
    msgTypeToTopic["secwager.LastTrade"] = tradeTopic;
    msgTypeToTopic["secwager.Order"] = orderEventTopic;
}


void ProtoPublisherKafkaImpl::publish(const google::protobuf::MessageLite &msg, const std::string &address) {
    //publish(msg,address,msgTypeToTopic[msg.GetTypeName()]);
}


void ProtoPublisherKafkaImpl::publish(const google::protobuf::MessageLite &msg, const std::string &key,
                                      RdKafka::Topic *topic) {
    int size = msg.ByteSize();
    char *array = new char[size];
    msg.SerializeToArray(array, size);
    kafkaProducer->produce(topic, RdKafka::Topic::PARTITION_UA, RdKafka::Producer::RK_MSG_FREE,
                           array, size, &key,
                           nullptr);
}