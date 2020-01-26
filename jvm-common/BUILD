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
    name = "utils",
    srcs = ["src/main/kotlin/SecwagerUtils.kt"],
    deps = [
        ":dto",
        "//proto:market_java_proto",
    ],
)

kt_jvm_test(
    name = "utils-test",
    srcs = ["src/test/kotlin/UtilsTest.kt"],
    test_class = "com.secwager.utils.UtilsTest",
    deps = [
        ":utils",
        "//proto:market_java_proto",
        "@maven//:com_google_truth_truth",
        "@maven//:junit_junit",
    ],
)
