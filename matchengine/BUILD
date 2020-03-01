load("@io_bazel_rules_kotlin//kotlin:kotlin.bzl", "kt_jvm_library", "kt_jvm_test")

kt_jvm_test(
    name = "test",
    srcs = [
        "src/test/kotlin/OrderBookTest.kt",
    ],
    test_class = "com.secwager.matchengine.OrderBookTest",
    deps = [
        ":orderbook-lib",
        "@maven//:com_google_truth_truth",
        "@maven//:com_nhaarman_mockitokotlin2_mockito_kotlin",
        "@maven//:junit_junit",
        "@maven//:org_mockito_mockito_core",
    ],
)

kt_jvm_library(
    name = "orderbook-lib",
    srcs = [
        "src/main/kotlin/OrderBook.kt",
        "src/main/kotlin/Publishers.kt",
    ],
    deps = [
        "//jvm-common:dto",
        "//proto:market_java_proto",
    ],
)
