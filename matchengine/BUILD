load("@io_bazel_rules_docker//cc:image.bzl", "cc_image")

cc_image(
    name = "matchengine",
    srcs = [
        "main.cc",
        "market_data_publisher.cc",
        "market_data_publisher.h",
        "proto_publisher.h",
        "proto_publisher_kafka_impl.cc",
        "proto_publisher_kafka_impl.h",
    ],
    deps = [
        "//proto:matchengine_cc_proto",
        "//third_party/boost:boost_fiber",
        "//third_party/kafka:librdkafka",
        "//third_party/spdlog",
        "@liquibook",
    ],
)
