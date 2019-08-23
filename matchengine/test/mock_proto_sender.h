#pragma once

#include "matchengine/proto_sender.h"
#include "gmock/gmock.h"

class MockProtoSender : public ProtoSender {
public:
    MOCK_METHOD(void, send,(const google::protobuf::MessageLite &msg, const std::string &address), (override));
};