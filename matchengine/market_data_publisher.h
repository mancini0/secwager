#pragma once

#include "liquibook/depth_order_book.h"
#include "proto/matchengine.pb.h"
#include "proto_sender.h"

typedef std::shared_ptr<secwager::Order> OrderPtr;
typedef liquibook::book::DepthOrderBook<OrderPtr> DepthBook;

class MarketDataPublisher :
        public liquibook::book::TradeListener<DepthBook>,
        public liquibook::book::DepthListener<DepthBook> {

public:
    MarketDataPublisher(ProtoSender *protoSender);

    void on_depth_change(const DepthBook *book, const DepthBook::DepthTracker *depth) override;

    void on_trade(const DepthBook *book,
                  liquibook::book::Quantity qty,
                  liquibook::book::Cost cost) override;

private:
    ProtoSender *protoSender;
};


