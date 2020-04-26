package(default_visibility = ["//visibility:public"])

load("@io_grpc_grpc_java//:java_grpc_library.bzl", "java_grpc_library")

proto_library(
    name = "market_proto",
    srcs = ["market.proto"],
    deps = [":refdata_proto"],
)

proto_library(
    name = "refdata_proto",
    srcs = ["ref_data.proto"],
    deps = [],
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
    name = "refdata_java_proto",
    deps = [":refdata_proto"],
)

java_proto_library(
    name = "orderentry_java_proto",
    deps = [
        ":orderentry_proto",
    ],
)

java_proto_library(
    name = "cashier_java_proto",
    deps = [":cashier_proto"],
)

java_grpc_library(
    name = "cashier-grpc",
    srcs = [":cashier_proto"],
    visibility = [
        "//blockchain-spv:__pkg__",
        "//cashier:__pkg__",
        "//orderentry:__pkg__",
    ],
    deps = [":cashier_java_proto"],
)

java_grpc_library(
    name = "orderentry-grpc",
    srcs = ["//proto:orderentry_proto"],
    visibility = [
        "//orderentry:__pkg__",
    ],
    deps = ["//proto:orderentry_java_proto"],
)
