proto_library(
    name = "market_proto",
    srcs = ["market.proto"],
    visibility = ["//visibility:public"],
)

proto_library(
    name = "orderentry_proto",
    srcs = ["order_entry.proto"],
    visibility = ["//visibility:public"],
    deps = [":market_proto"],
)

cc_proto_library(
    name = "market_cc_proto",
    visibility = ["//visibility:public"],
    deps = [":market_proto"],
)

java_proto_library(
    name = "market_java_proto",
    visibility = ["//visibility:public"],
    deps = [":market_proto"],
)

java_proto_library(
    name = "orderentry_java_proto",
    visibility = ["//visibility:public"],
    deps = [
        ":orderentry_proto",
    ],
)
