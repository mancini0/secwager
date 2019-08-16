#pragma once

#include "liquibook/depth_order_book.h"
#include "proto/matchengine.pb.h"
#include "librdkafka/rdkafkacpp.h"

typedef std::shared_ptr<secwager::Order> OrderPtr;
typedef liquibook::book::DepthOrderBook<OrderPtr> DepthBook;

class MarketDataKafkaPublisher :
        public liquibook::book::TradeListener<DepthBook>,
        public liquibook::book::DepthListener<DepthBook> {

public:
    MarketDataKafkaPublisher(RdKafka::Producer *kafkaProducer,
                             RdKafka::Topic *tradesTopic, RdKafka::Topic *depthTopic);

    void on_depth_change(const DepthBook *book, const DepthBook::DepthTracker *depth) override;

    void on_trade(const DepthBook *book,
                  liquibook::book::Quantity qty,
                  liquibook::book::Cost cost) override;

private:
    RdKafka::Producer *kafkaProducer;
    RdKafka::Topic *tradesTopic;
    RdKafka::Topic *depthTopic;
    void publish(const google::protobuf::MessageLite& msg, const std::string& key, RdKafka::Topic* topic );

};


