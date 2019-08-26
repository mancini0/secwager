#include "boost/fiber/unbuffered_channel.hpp"
#include <thread>
#include <librdkafka/rdkafkacpp.h>
#include "market.h"
#include "logging_proto_sender.h"
#include "librdkafka/rdkafkacpp.h"
#include "constants.h"
#include "spdlog/spdlog.h"
#include "config_utils.h"
#include "kafka_inbound_order_channel.h"

int main() {

    int statefulSetIndex = ConfigUtils::getStatefulSetIndexOrThrow();
    RdKafka::Conf *consumerConfig = ConfigUtils::createBaseKafkaConfigFromEnvironmentVars();
    ConfigUtils::setOrThrow(consumerConfig, "group.id", "matchengine-" + std::to_string(statefulSetIndex));
    ConfigUtils::setOrThrow(consumerConfig, "enable.auto.commit", "false");
    ConfigUtils::setOrThrow(consumerConfig, "queue.buffering.max.ms", "250");
    ConfigUtils::setOrThrow(consumerConfig, "request.required.acks", "0"); //for now
    std::string err;

    RdKafka::KafkaConsumer *orderConsumer = RdKafka::KafkaConsumer::create(consumerConfig, err);
    if (err.length()) {
        spdlog::error("Could not construct  kafka consumer due to error {}", err);
        return -1;
    }
    RdKafka::TopicPartition *orderTopicPartition = RdKafka::TopicPartition::create(
            secwager::constants::KAFKA_ORDER_INBOUND_TOPIC,
            statefulSetIndex);
    orderConsumer->assign({orderTopicPartition});

    boost::fibers::unbuffered_channel<OrderPtr> orderInboundChannel;
    KafkaInboundOrderChannel kafkaInboundOrderChannel(orderConsumer, &orderInboundChannel);

    LoggingProtoSender mockProtoSender;
    MarketDataPublisher marketDataPublisher(&mockProtoSender);
    OrderStatusPublisher orderStatusPublisher(&mockProtoSender);
    Market market(&orderInboundChannel, &marketDataPublisher, &orderStatusPublisher);
    std::thread marketThread([&market]() { market.start(); });
    std::thread inboundOrderThread([&kafkaInboundOrderChannel]() { kafkaInboundOrderChannel.start(); });
    marketThread.join();
    inboundOrderThread.join();
}

