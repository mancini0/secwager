#include "market.h"


Market::Market(boost::fibers::unbuffered_channel<OrderPtr> *inboundOrderChannel,
               MarketDataPublisher *marketDataPublisher, OrderStatusPublisher *orderStatusPublisher)
        : inboundOrderChannel(inboundOrderChannel),
          marketDataPublisher(marketDataPublisher),
          orderStatusPublisher(orderStatusPublisher) {}

void Market::listenForOrders() {
    for (OrderPtr order: *inboundOrderChannel) {
        if (symbolToBooks.size()) {
            std::cout << "1";
        } else {
            std::cout << "2";
        }
    }

}