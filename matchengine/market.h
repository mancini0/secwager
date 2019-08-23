#pragma once

#include "market_data_publisher.h"
#include "proto/matchengine.pb.h"
#include "order_status_publisher.h"
#include "boost/fiber/unbuffered_channel.hpp"
#include <unordered_map>

class Market {
public:
    Market(boost::fibers::unbuffered_channel<OrderPtr> *inboundOrderChannel,
           MarketDataPublisher *marketDataPublisher, OrderStatusPublisher *orderStatusPublisher);

    void start();


private:
    boost::fibers::unbuffered_channel<OrderPtr> *inboundOrderChannel;
    MarketDataPublisher *marketDataPublisher;
    OrderStatusPublisher *orderStatusPublisher;
    std::unordered_map<std::string, DepthBook *> symbolToBooks;

};

