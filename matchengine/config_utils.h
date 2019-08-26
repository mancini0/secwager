#pragma once

#include <stdexcept>
#include "librdkafka/rdkafkacpp.h"
#include "constants.h";
#include "spdlog/spdlog.h"

namespace ConfigUtils {

    std::string getEnvironmentVariableOrDefault(const std::string &variable_name,
                                                const std::string &default_value) {
        const char *value = std::getenv(variable_name.c_str());
        return value ? value : default_value;
    }

    void setOrThrow(RdKafka::Conf *config, const std::string &key, const std::string &value) {
        std::string err;
        config->set(key, value, err);
        if (err.length()) {
            throw std::invalid_argument("Could not set " + key + "=" + value + " due to error: " + err);
        }
    }

    RdKafka::Conf *createBaseKafkaConfigFromEnvironmentVars() {
        std::string err;
        RdKafka::Conf *commonConfig = RdKafka::Conf::create(RdKafka::Conf::CONF_GLOBAL);
        ConfigUtils::setOrThrow(commonConfig, "bootstrap.servers",
                                getEnvironmentVariableOrDefault("bootstrap.servers", "localhost:9092"));
        return commonConfig;
    }


    int getStatefulSetIndexOrThrow() {
        const char *pod = std::getenv(secwager::constants::POD_NAME_ENV_VAR.c_str());

        if (!pod) {
            spdlog::error("The {} environment variable must be defined, having format podname-index"
                          ",where index is an integer number representing the ordinal number of the StatefulSet this instance is associated with."
                          " (This instance uses this number to know which order-inbound kafka partition to consume from).",
                          secwager::constants::POD_NAME_ENV_VAR);
            throw std::invalid_argument("could not determine the stateful set index, see logs for details");
        }
        std::string podName(pod);
        try {
            int found = podName.find_last_of("-");
            int ordinalIndex = std::stoi(podName.substr(found + 1));
            spdlog::info("The ordinal index was determined to be {}", ordinalIndex);
            return ordinalIndex;
        }
        catch (const std::invalid_argument &e) {
            spdlog::error(
                    "The StatefulSet ordinal index could not be derived from the pod name '{}'. Please ensure your"
                    "StatefulSet k8s definition is configured to set the {} environment variable as follows: {}",
                    podName, secwager::constants::POD_NAME_ENV_VAR, "\n\n\n    spec:\n"
                                                                    "      containers:\n"
                                                                    "      - name: matchengine\n"
                                                                    "        image: us.gcr.io/some-image\n"
                                                                    "        env:\n"
                                                                    "          - name: POD_NAME_WITH_ORDINAL\n"
                                                                    "            valueFrom:\n"
                                                                    "              fieldRef:\n"
                                                                    "                fieldPath: metadata.name");
            throw e;
        }

    }
}