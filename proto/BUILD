package(default_visibility = ["//visibility:public"])

load("@io_grpc_grpc_java//:java_grpc_library.bzl", "java_grpc_library")
load("@npm_bazel_labs//:index.bzl", "ts_proto_library")
load("@com_github_grpc_grpc_kotlin//:kt_jvm_grpc.bzl", "kt_jvm_grpc_library")

proto_library(
    name = "market_proto",
    srcs = ["market.proto"],
    deps = [":refdata_proto"],
)

proto_library(
    name = "refdata_proto",
    srcs = ["reference_data.proto"],
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

java_proto_library(
    name = "market_java_proto",
    deps = [":market_proto"],
)

java_proto_library(
    name = "refdata_java_proto",
    deps = [":refdata_proto"],
)

ts_proto_library(
    name = "orderentry_js_proto",
    proto = ":orderentry_proto",
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

kt_jvm_grpc_library(
    name = "kt_grpc_cashier",
    srcs = [":cashier_proto"],
    deps = [
        ":cashier_java_proto",
    ],
)

kt_jvm_grpc_library(
    name = "kt_grpc_order_entry",
    srcs = [":orderentry_proto"],
    visibility = [
        "//orderentry-grpc:__pkg__",
    ],
    deps = [":orderentry_java_proto"],
)
