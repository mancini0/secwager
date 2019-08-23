#pragma once

#include "proto_sender.h"

class MockProtoSender : public ProtoSender {
    void send(const google::protobuf::MessageLite &msg, const std::string &address) override;

};

