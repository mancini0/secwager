load("@io_bazel_rules_kotlin//kotlin:kotlin.bzl", "kt_jvm_binary", "kt_jvm_library", "kt_jvm_test")

kt_jvm_binary(
    name = "contract-publisher",
    srcs = [
        "App.kt",
        "di/ContractPublisherModule.kt",
    ],
    main_class = "com.secwager.refdata.AppKt",
    deps = [
        "//:dagger",
        "//proto:refdata_java_proto",
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
        "//football-data-client",
        "//proto:market_java_proto",
        "//proto:refdata_java_proto",
        "@maven//:ch_qos_logback_logback_classic",
        "@maven//:javax_inject_javax_inject",
        "@maven//:org_apache_kafka_kafka_clients",
    ],
)
