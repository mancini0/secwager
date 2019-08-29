package(default_visibility = ["//visibility:public"])

load("@npm_bazel_typescript//:index.bzl", "ts_library", "ts_proto_library")

proto_library(
    name = "market_proto",
    srcs = ["market.proto"],
)

proto_library(
    name = "cashier_proto",
    srcs = ["cashier.proto"],
)

proto_library(
    name = "orderentry_proto",
    srcs = ["order_entry.proto"],
    deps = [":market_proto"],
)

cc_proto_library(
    name = "market_cc_proto",
    deps = [":market_proto"],
)

java_proto_library(
    name = "market_java_proto",
    deps = [":market_proto"],
)

java_proto_library(
    name = "orderentry_java_proto",
    deps = [
        ":orderentry_proto",
    ],
)

ts_proto_library(
    name = "orderentry_ts_proto",
    deps = [":orderentry_proto"],
)

java_proto_library(
    name = "cashier_java_proto",
    deps = [":cashier_proto"],
)
