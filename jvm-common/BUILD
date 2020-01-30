load("@io_bazel_rules_kotlin//kotlin:kotlin.bzl", "kt_jvm_library", "kt_jvm_test")

package(default_visibility = ["//visibility:public"])

java_library(
    name = "grpc-server",
    srcs = ["src/main/java/com/secwager/grpc/GrpcServer.java"],
    visibility = ["//visibility:public"],
    deps = [
        "@maven//:io_grpc_grpc_api",
        "@maven//:io_grpc_grpc_netty_shaded",
    ],
)

kt_jvm_library(
    name = "dto",
    srcs = ["src/main/kotlin/Dto.kt"],
    deps = [],
)

kt_jvm_library(
    name = "proto-to-dto-conversion-utils",
    srcs = ["src/main/kotlin/ProtoToDtoConversionUtils.kt"],
    deps = [
        ":dto",
        "//proto:market_java_proto",
    ],
)

kt_jvm_library(
    name = "serdes",
    srcs = ["src/main/kotlin/Serdes.kt"],
    deps = [
        "//proto:market_java_proto",
        "@maven//:org_apache_kafka_kafka_streams",
    ],
)

kt_jvm_test(
    name = "proto-to-dto-conversion-tests",
    srcs = ["src/test/kotlin/ProtoToDtoConversionUtilsTest.kt"],
    test_class = "com.secwager.utils.ProtoToDtoConversionUtilsTest",
    deps = [
        ":proto-to-dto-conversion-utils",
        "//proto:market_java_proto",
        "@maven//:com_google_truth_truth",
        "@maven//:junit_junit",
    ],
)
