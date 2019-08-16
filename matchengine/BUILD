load("@io_bazel_rules_docker//cc:image.bzl", "cc_image")

cc_binary(
    name = "matchengine",
    srcs = [
        "main.cc",
        "market_data_listener.cc",
        "market_data_listener.h",
    ],
    deps = [
        "//proto:matchengine_cc_proto",
        "//third_party/kafka:librdkafka",
        "@liquibook",
    ],
)
