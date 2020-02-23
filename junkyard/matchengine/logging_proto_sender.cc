#include "logging_proto_sender.h"
#include "spdlog/spdlog.h"

void LoggingProtoSender::send(const google::protobuf::MessageLite &msg, const std::string &address) {
    spdlog::info("sending {} to {}", msg.GetTypeName(), address);
}
