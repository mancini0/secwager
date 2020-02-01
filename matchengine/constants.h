#pragma once

#include <string>

namespace secwager {
    namespace constants {
        const std::string POD_NAME_ENV_VAR("POD_NAME_WITH_ORDINAL");

        /**kafka config**/
        const std::string KAFKA_BOOTSTRAP_SERVERS("KAFKA_BOOTSTRAP_SERVERS");
        const std::string KAFKA_FETCH_WAIT_MAX_MS("KAFKA_FETCH_WAIT_MAX_MS");
        const std::string KAFKA_DEBUG_COMPONENTS("KAFKA_DEBUG_COMPONENTS");

        /**topic names**/
        const std::string KAFKA_ORDER_INBOUND_TOPIC("order-inbound");
        const std::string KAFKA_ORDER_STATUS_EVENTS_TOPIC("order-status-events");
        const std::string KAFKA_MARKET_DATA_DEPTH_TOPIC("depth");
        const std::string KAFKA_MARKET_DATA_MATCHED_TRADES_TOPIC("trades");
    }
}