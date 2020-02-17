load("@io_bazel_rules_kotlin//kotlin:kotlin.bzl", "kt_jvm_binary", "kt_jvm_library", "kt_jvm_test")

kt_jvm_binary(
    name = "contract-publisher",
    srcs = [
        "App.kt",
        "di/ContractPublisherModule.kt",
    ],
    main_class = "com.secwager.refdata.AppKt",
    deps = [
        "//third_party/dagger",
        "@maven//:org_apache_kafka_kafka_clients",
    ],
)

kt_jvm_library(
    name = "lib",
    srcs = [
        "ContractPublisher.kt",
    ],
    resources = [],
    deps = [
        "//football-data:football-data-client",
        "//proto:market_java_proto",
        "@maven//:ch_qos_logback_logback_classic",
        "@maven//:org_apache_kafka_kafka_clients",
    ],
)
