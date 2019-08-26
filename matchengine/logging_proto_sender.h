#pragma once

#include "proto_sender.h"

class LoggingProtoSender : public ProtoSender {
    void send(const google::protobuf::MessageLite &msg, const std::string &address) override;

};

