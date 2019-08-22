#include "proto/matchengine.pb.h"
#include "librdkafka/rdkafkacpp.h"
#include "liquibook/depth_order_book.h"
#include "market_data_publisher.h"
#include "proto_publisher_kafka_impl.h"
#include "boost/fiber/unbuffered_channel.hpp"
#include <thread>
#include "spdlog/spdlog.h"
#include <stdexcept>
#include <cstdlib>

int main() {
    const char *podName = std::getenv("FOO");
    spdlog::info("[{}]", podName);
    if (!podName) {
        spdlog::error("The POD_NAME_WITH_ORDINAL environment variable must be defined, having format podname-index"
                      ",where index is an integer number representing the ordinal number of the StatefulSet this instance is associated with."
                      " (This instance uses this number to know which order-inbound kafka partition to consume from).");
        return -1;
    }
    int ordinalIndex;
    try {
        ordinalIndex = std::stoi(&std::string(podName).back());
    }
    catch (const std::invalid_argument &e) {
        spdlog::error("The StatefulSet ordinal index could not be derived from the pod name '{}'. Please ensure your"
                      "StatefulSet k8s definition is configured to set the POD_NAME_WITH_ORDINAL environment variable as follows: {}",
                      podName, "\n\n\n    spec:\n"
                               "      containers:\n"
                               "      - name: matchengine\n"
                               "        image: us.gcr.io/some-image\n"
                               "        env:\n"
                               "          - name: POD_NAME_WITH_ORDINAL\n"
                               "            valueFrom:\n"
                               "              fieldRef:\n"
                               "                fieldPath: metadata.name");
        return -2;
    }

    std::unordered_map<std::string, RdKafka::Topic *> msgTypeToTopic;
    std::string errString;
    RdKafka::Conf *producerConfig = RdKafka::Conf::create(RdKafka::Conf::CONF_GLOBAL);
    RdKafka::Conf *consumerConfig = RdKafka::Conf::create(RdKafka::Conf::CONF_GLOBAL);
    //make these k8s props
    producerConfig->set("bootstrap.servers", "secwager-kafka-bootstrap", errString);
    producerConfig->set("group.id", "secwager_engine", errString);
    producerConfig->set("fetch.wait.max.ms", "100", errString);
    producerConfig->set("auto.commit.enable", "true", errString);
    producerConfig->set("queue.buffering.max.ms", "100", errString);
    producerConfig->set("request.required.acks", "0", errString);

    RdKafka::Producer *producer = RdKafka::Producer::create(producerConfig, errString);
    RdKafka::KafkaConsumer *consumer = RdKafka::KafkaConsumer::create(producerConfig, errString);
    secwager::Order order;
    secwager::DepthBook depth;
    secwager::LastTrade lastTrade;


    msgTypeToTopic[order.GetTypeName()] = nullptr;
    msgTypeToTopic[depth.GetTypeName()] = nullptr;
    msgTypeToTopic[lastTrade.GetTypeName()] = nullptr;


    ProtoPublisher *protoPublisher = new ProtoPublisherKafkaImpl(nullptr, &msgTypeToTopic);
    MarketDataPublisher marketDataPublisher(protoPublisher);

    DepthBook b;
    b.set_symbol("IBM");
    b.set_depth_listener(&marketDataPublisher);

    OrderPtr o = std::make_shared<secwager::Order>();
    o->set_all_or_none(false);
    o->set_is_limit(true);
    o->set_price(100);
    o->set_immediate_or_cancel(false);
    o->set_order_id("1");
    o->set_is_buy(true);
    o->set_order_qty(50);

    boost::fibers::unbuffered_channel<OrderPtr> orderInboundChannel;
    std::thread t([&orderInboundChannel, &o]() {
        std::cout << "producing..." << std::endl;
        orderInboundChannel.push(o);
    });
    for (OrderPtr o : orderInboundChannel) {
        std::cout << o->order_qty() << std::endl;
    }
    b.add(o);
}


