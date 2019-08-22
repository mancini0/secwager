#pragma once

#include <google/protobuf/message_lite.h>

class ProtoSender {
public:
    virtual void send(const google::protobuf::MessageLite &msg, const std::string &address) = 0;

    virtual ~ProtoSender() = default;
};

