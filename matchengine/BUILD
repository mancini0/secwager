load("@io_bazel_rules_kotlin//kotlin:kotlin.bzl", "kt_jvm_library", "kt_jvm_test")

package(default_visibility = ["//visibility:public"])

kt_jvm_test(
    name = "test",
    srcs = [
        "src/test/kotlin/OrderBookTest.kt",
    ],
    test_class = "com.secwager.matchengine.OrderBookTest",
    deps = [
        ":orderbook",
        "@maven//:com_google_truth_extensions_truth_proto_extension",
        "@maven//:com_google_truth_truth",
        "@maven//:com_nhaarman_mockitokotlin2_mockito_kotlin",
        "@maven//:junit_junit",
        "@maven//:org_mockito_mockito_core",
    ],
)

kt_jvm_library(
    name = "orderbook",
    srcs = [
        "src/main/kotlin/OrderBook.kt",
        "src/main/kotlin/Publishers.kt",
    ],
    deps = [
        "//entity-common:dto",
        "//entity-common:proto-conversion-utils",
        "//proto:market_java_proto",
        "@maven//:org_slf4j_slf4j_api",
    ],
)
