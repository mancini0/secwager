load("@io_bazel_rules_kotlin//kotlin:kotlin.bzl", "kt_jvm_binary", "kt_jvm_test")

kt_jvm_binary(
    name = "trade-depth-joiner",
    srcs = [
        "TradeDepthJoiner.kt",
    ],
    main_class = "secwager.posttrade.quotes.TradeDepthJoinerKt",
    resources = [],
    deps = [
        "//jvm-common:serdes",
        "//proto:market_java_proto",
        "@maven//:ch_qos_logback_logback_classic",
        "@maven//:org_apache_kafka_kafka_streams",
    ],
)

kt_jvm_test(
    name = "trade-depth-joiner-test",
    srcs = [
        "TradeDepthJoinerTest.kt",
    ],
    test_class = "secwager.posttrade.quotes.TradeDepthJoinerTest",
    deps = [
        ":trade-depth-joiner",
        "//jvm-common:serdes",
        "//proto:market_java_proto",
        "@maven//:junit_junit",
        "@maven//:org_apache_kafka_kafka_streams",
        "@maven//:org_apache_kafka_kafka_streams_test_utils",
    ],
)
