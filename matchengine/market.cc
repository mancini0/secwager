#include "market.h"
#include <memory>
#include "spdlog/spdlog.h"

Market::Market(boost::fibers::unbuffered_channel<OrderPtr> *inboundOrderChannel,
               MarketDataPublisher *marketDataPublisher, OrderStatusPublisher *orderStatusPublisher)
        : inboundOrderChannel(inboundOrderChannel),
          marketDataPublisher(marketDataPublisher),
          orderStatusPublisher(orderStatusPublisher) {}

void Market::start() {
    for (OrderPtr order: *inboundOrderChannel) {
        if (symbolToBooks.find(order->symbol()) == symbolToBooks.end()) {
            DepthBook *book = new DepthBook(order->symbol());
            book->set_depth_listener(marketDataPublisher);
            book->set_order_listener(orderStatusPublisher);
            symbolToBooks.insert(std::make_pair(order->symbol(), book));
            spdlog::info("created new depth book for symbol {}", order->symbol());
        }
        symbolToBooks[order->symbol()]->add(order);
        spdlog::info("Added [id, symbol, type, price, qty]=[{},{},{},{},{}] to book.", order->order_id(),
                     order->symbol(),
                     order->order_type(),
                     order->price(),
                     order->order_qty()
        );
    }

}