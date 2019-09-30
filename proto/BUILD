package(default_visibility = ["//visibility:public"])

proto_library(
    name = "market_data_proto",
    srcs = ["market_data.proto"],
    deps = [":market_proto"],
)

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

java_proto_library(
    name = "market_data_java_proto",
    deps = [":market_data_proto"],
)

java_proto_library(
    name = "cashier_java_proto",
    deps = [":cashier_proto"],
)
