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

        if (isinToBooks.find(order->isin()) == isinToBooks.end()) {
            DepthBook *book = new DepthBook(order->isin());
            book->set_depth_listener(marketDataPublisher);
            book->set_order_listener(orderStatusPublisher);
            isinToBooks.insert(std::make_pair(order->isin(), book));
            spdlog::info("created new depth book for isin {}", order->isin());
        }
        isinToBooks[order->isin()]->add(order);
        spdlog::info("added [id, symbol, type, price, qty]=[{},{},{},{},{}] to book.", order->order_id(),
                     order->isin(),
                     order->order_type(),
                     order->price(),
                     order->order_qty()
        );
    }

}