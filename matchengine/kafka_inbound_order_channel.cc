#include <spdlog/spdlog.h>
#include "kafka_inbound_order_channel.h"


KafkaInboundOrderChannel::KafkaInboundOrderChannel(RdKafka::KafkaConsumer *kafkaConsumer,
                                                   boost::fibers::unbuffered_channel<OrderPtr> *orderInboundChannel)
        :
        kafkaConsumer(kafkaConsumer), orderInboundChannel(orderInboundChannel) {}


void KafkaInboundOrderChannel::start() {
    isPolling = true;

    while (isPolling) {
        RdKafka::Message *message = kafkaConsumer->consume(5000);
        switch (message->err()) {
            case RdKafka::ERR__TIMED_OUT:
            case RdKafka::ERR__PARTITION_EOF:
                break;

            case RdKafka::ERR_NO_ERROR: {
                std::shared_ptr<secwager::Order> order = std::make_shared<secwager::Order>();
                order->ParseFromArray(message->payload(), message->len());
                //for now, commit before the order is accepted into the book.
                //The commit should take place in the depth book callback.
                kafkaConsumer->commitAsync(message);
                delete message;
                break;
            }
            default:
                spdlog::error("error while consuming order topic: {}", message->errstr());
        }


    }

}