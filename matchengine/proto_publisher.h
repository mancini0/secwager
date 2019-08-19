#pragma once
#include <google/protobuf/message_lite.h>

class ProtoPublisher {
public:
    virtual void publish(const google::protobuf::MessageLite& msg, const std::string& address) =0;
    virtual ~ProtoPublisher() = default;
};

