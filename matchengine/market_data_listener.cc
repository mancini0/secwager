#include "market_data_listener.h"

MarketDataKafkaPublisher::MarketDataKafkaPublisher(RdKafka::Producer *kafkaProducer, RdKafka::Topic *tradesTopic,
                                                   RdKafka::Topic *depthTopic) : kafkaProducer(kafkaProducer),
                                                                                 tradesTopic(tradesTopic),
                                                                                 depthTopic(depthTopic) {}


void MarketDataKafkaPublisher::on_trade(const DepthBook *book,
                                        liquibook::book::Quantity qty,
                                        liquibook::book::Cost cost) {


}

void MarketDataKafkaPublisher::on_depth_change(const DepthBook *book, const DepthBook::DepthTracker *depth) {
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

    publish(depthBook, depthBook.symbol(), depthTopic);

}


void MarketDataKafkaPublisher::publish(const google::protobuf::MessageLite &msg, const std::string &key,
                                       RdKafka::Topic *topic) {
    int size = msg.ByteSize();
    char *array = new char[size];
    msg.SerializeToArray(array, size);
    kafkaProducer->produce(topic, RdKafka::Topic::PARTITION_UA, RdKafka::Producer::RK_MSG_FREE,
                           array, size, &key,
                           nullptr);
}