#include <iostream>
#include <memory>
#include <vector>
#include "proto/matchengine.pb.h"
#include "librdkafka/rdkafkacpp.h"
#include "liquibook/depth_order_book.h"
#include "market_data_listener.h"

//typedef std::shared_ptr<secwager::Order> OrderPtr;
//typedef liquibook::book::DepthOrderBook<OrderPtr> DepthBook;
//typedef std::shared_ptr<DepthBook> DepthBookPtr;
//
//class MyDepthListener :
//        public liquibook::book::DepthListener<DepthBook>,
//        public liquibook::book::TradeListener<DepthBook>
//        {
//    void on_depth_change(
//            const DepthBook *book,
//            const DepthBook::DepthTracker *depth) override {
//
//        const liquibook::book::DepthLevel* topOfBookBid = depth->bids();
//        const liquibook::book::DepthLevel* bottomOfBookBid  = depth->last_bid_level();
//
//
//    }
//};


int main() {
//    Since auto commits are performed in a background thread this may result in the offset for the latest message being committed before the application has finished processing the message. If the application was to crash or exit prior to finishing processing, and the offset had been auto committed, the next incarnation of the consumer application would start at the next message, effectively missing the message that was processed when the application crashed. To avoid this scenario the application can disable the automatic offset store by setting enable.auto.offset.store to false and manually storing offsets after processing by calling rd_kafka_offsets_store(). This gives an application fine-grained control on when a message is eligible for committing without having to perform the commit itself. enable.auto.commit should be set to true when using manual offset storing. The latest stored offset will be automatically committed every auto.commit.interval.ms.
    DepthBook b;
    b.set_depth_listener(nullptr);
    OrderPtr o = std::make_shared<secwager::Order>();
    o->set_all_or_none(false);
    o->set_is_limit(true);
    o->set_price(100);
    o->set_immediate_or_cancel(false);
    o->set_order_id("1");
    o->set_is_buy(true);
    o->set_order_qty(50);
//    book->add(o);
}