load("@io_bazel_rules_kotlin//kotlin:kotlin.bzl", "kt_jvm_library", "kt_jvm_test")

kt_jvm_test(
    name = "orderbook-test",
    srcs = [
        "src/test/kotlin/OrderBookTest.kt",
    ],
    test_class = "com.secwager.matchengine.OrderBookTest",
    deps = [
        ":orderbook-lib",
        "@maven//:junit_junit",
    ],
)

kt_jvm_library(
    name = "orderbook-lib",
    srcs = [
        "src/main/kotlin/OrderBook.kt",
    ],
    deps = [],
)
