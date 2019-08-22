#include "market_data_publisher.h"

MarketDataPublisher::MarketDataPublisher(ProtoSender *protoSender) : protoSender(protoSender) {}


void MarketDataPublisher::on_trade(const DepthBook *book,
                                   liquibook::book::Quantity qty,
                                   liquibook::book::Cost cost) {

    secwager::LastTrade lastTrade;
    lastTrade.set_symbol(book->symbol());
    lastTrade.set_qty(qty);
    lastTrade.set_price(cost / qty);
    protoSender->send(lastTrade, lastTrade.symbol());
}

void MarketDataPublisher::on_depth_change(const DepthBook *book, const DepthBook::DepthTracker *depth) {
    secwager::DepthBook depthBook;
    depthBook.set_symbol(book->symbol());
    const liquibook::book::DepthLevel *thisBid = depth->bids();
    const liquibook::book::DepthLevel *bidBottomOfBook = depth->last_bid_level();

    while (thisBid != bidBottomOfBook) {
        if (thisBid->aggregate_qty() != 0) {
            depthBook.add_bid_qtys(thisBid->aggregate_qty());
            depthBook.add_bid_prices(thisBid->price());
        }
        ++thisBid;
    }

    const liquibook::book::DepthLevel *thisAsk = depth->asks();
    const liquibook::book::DepthLevel *askBottomOfBook = depth->last_ask_level();

    while (thisAsk != askBottomOfBook) {
        if (thisAsk->aggregate_qty() != 0) {
            depthBook.add_ask_qtys(thisAsk->aggregate_qty());
            depthBook.add_ask_prices(thisAsk->price());
        }
        ++thisAsk;
    }
    protoSender->send(depthBook, depthBook.symbol());
}
