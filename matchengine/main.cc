#include "proto/matchengine.pb.h"
#include "librdkafka/rdkafkacpp.h"
#include "liquibook/depth_order_book.h"
#include "market_data_publisher.h"
#include "kafka_proto_sender.h"
#include "boost/fiber/unbuffered_channel.hpp"
#include <thread>
#include "spdlog/spdlog.h"
#include "constants.h"
#include <stdexcept>
#include "market.h"
#include <cstdlib>


RdKafka::Conf *createCommonKafkaConfig() {
    std::string err;
    RdKafka::Conf *commonConfig = RdKafka::Conf::create(RdKafka::Conf::CONF_GLOBAL);

    std::string bootstrapServers = std::getenv(
            (secwager::constants::KAFKA_BOOTSTRAP_SERVERS.c_str(), "localhost:9092"));
    commonConfig->set("bootstrap.servers", bootstrapServers, err);
    if (err.length()) {
        throw std::invalid_argument("Could not set bootstrap.servers, error: " + err);
    }

    commonConfig->set("debug", std::getenv((secwager::constants::KAFKA_DEBUG_COMPONENTS.c_str(), "")), err);
    if (err.length()) {
        throw std::invalid_argument("Could not set kafka debug, error: " + err);
    }

    std::string waitMaxMs = std::getenv((secwager::constants::KAFKA_FETCH_WAIT_MAX_MS.c_str(), "100"));
    commonConfig->set("fetch.wait.max.ms", waitMaxMs, err);
    if (err.length()) {
        throw std::invalid_argument("Could not set fetch.wait.max.ms, error: " + err);
    }

    return commonConfig;
}


int main() {


    const char *pod = std::getenv(secwager::constants::POD_NAME_ENV_VAR.c_str());
    if (!pod) {
        spdlog::error("The {} environment variable must be defined, having format podname-index"
                      ",where index is an integer number representing the ordinal number of the StatefulSet this instance is associated with."
                      " (This instance uses this number to know which order-inbound kafka partition to consume from).",
                      secwager::constants::POD_NAME_ENV_VAR);
        return -100;
    }
    int ordinalIndex;
    std::string podName(pod);
    try {
        int found = podName.find_last_of("-");
        ordinalIndex = std::stoi(podName.substr(found + 1));
    }
    catch (const std::invalid_argument &e) {
        spdlog::error("The StatefulSet ordinal index could not be derived from the pod name '{}'. Please ensure your"
                      "StatefulSet k8s definition is configured to set the {} environment variable as follows: {}",
                      podName, secwager::constants::POD_NAME_ENV_VAR, "\n\n\n    spec:\n"
                                                                      "      containers:\n"
                                                                      "      - name: matchengine\n"
                                                                      "        image: us.gcr.io/some-image\n"
                                                                      "        env:\n"
                                                                      "          - name: POD_NAME_WITH_ORDINAL\n"
                                                                      "            valueFrom:\n"
                                                                      "              fieldRef:\n"
                                                                      "                fieldPath: metadata.name");
        return -200;
    }

    spdlog::info("starting {}...", podName);
    std::unordered_map<std::string, RdKafka::Topic *> msgTypeToTopic;
    std::string errString;

//    RdKafka::Producer *producer = RdKafka::Producer::create(producerConfig, errString);
//
//    RdKafka::KafkaConsumer *consumer = RdKafka::KafkaConsumer::create(producerConfig, errString);
//    secwager::Order order;
//    secwager::DepthBook depth;
//    secwager::LastTrade lastTrade;
//
//
//    msgTypeToTopic[order.GetTypeName()] = nullptr;
//    msgTypeToTopic[depth.GetTypeName()] = nullptr;
//    msgTypeToTopic[lastTrade.GetTypeName()] = nullptr;
//
//
//    ProtoSender *protoPublisher = new KafkaProtoSender(nullptr, &msgTypeToTopic);
//    MarketDataPublisher marketDataPublisher(protoPublisher);
//
//
//    boost::fibers::unbuffered_channel<OrderPtr> orderInboundChannel;
//    std::thread t([&orderInboundChannel, &o]() {
//        std::cout << "producing..." << std::endl;
//        //orderInboundChannel.push(o);
//    });
//
//    for (OrderPtr o : orderInboundChannel) {
//        std::cout << o->order_qty() << std::endl;
//    }
}


