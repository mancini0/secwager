load("@io_bazel_rules_docker//cc:image.bzl", "cc_image")

cc_library(
    name = "protosender",
    srcs = [
        "kafka_proto_sender.cc",
        "logging_proto_sender.cc",
    ],
    hdrs = [
        "kafka_proto_sender.h",
        "logging_proto_sender.h",
        "proto_sender.h",
    ],
    deps = [
        "//proto:matchengine_cc_proto",
        "//third_party/kafka:librdkafka",
        "//third_party/spdlog",
    ],
)

cc_library(
    name = "market",
    srcs = [
        "market.cc",
        "market_data_publisher.cc",
        "market_data_publisher.h",
        "order_status_publisher.cc",
        "order_status_publisher.h",
    ],
    hdrs = [
        "market.h",
    ],
    visibility = ["//matchengine/test:__pkg__"],
    deps = [
        ":protosender",
        "//proto:matchengine_cc_proto",
        "//third_party/boost:boost_fiber",
        "//third_party/spdlog",
        "@liquibook",
    ],
)

cc_binary(
    name = "matchengine",
    srcs = [
        "constants.h",
        "main.cc",
    ],
    deps = [
        ":market",
    ],
)
