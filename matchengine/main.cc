#include "boost/fiber/unbuffered_channel.hpp"
#include <thread>
#include "market.h"
#include "logging_proto_sender.h"


int main() {
    MockProtoSender mockProtoSender;
    MarketDataPublisher marketDataPublisher(&mockProtoSender);
    OrderStatusPublisher orderStatusPublisher(&mockProtoSender);
    boost::fibers::unbuffered_channel<OrderPtr> orderInboundChannel;
    Market m(&orderInboundChannel, &marketDataPublisher, &orderStatusPublisher);

    std::thread marketThread([&m]() { m.start(); });

    std::thread orderProducerThread([&orderInboundChannel]() {
        OrderPtr o = std::make_shared<secwager::Order>();
        o->set_all_or_none(false);
        o->set_is_limit(true);
        o->set_price(100);
        o->set_immediate_or_cancel(false);
        o->set_order_id("1");
        o->set_is_buy(true);
        o->set_order_qty(50);
        o->set_symbol("IBM");
        orderInboundChannel.push(o);
    });

    marketThread.join();
    orderProducerThread.join();

}


