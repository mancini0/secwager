load("@io_bazel_rules_docker//cc:image.bzl", "cc_image")

cc_binary(
    name = "matchengine",
    srcs = [
        "constants.h",
        "kafka_proto_sender.cc",
        "kafka_proto_sender.h",
        "main.cc",
        "market.h",
        "market_data_publisher.cc",
        "market_data_publisher.h",
        "order_status_publisher.cc",
        "order_status_publisher.h",
        "proto_sender.h",
    ],
    deps = [
        "//proto:matchengine_cc_proto",
        "//third_party/boost:boost_fiber",
        "//third_party/kafka:librdkafka",
        "//third_party/spdlog",
        "@liquibook",
    ],
)
