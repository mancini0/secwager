load("@io_bazel_rules_kotlin//kotlin:kotlin.bzl", "kt_jvm_binary", "kt_jvm_library")
load("@io_bazel_rules_docker//kotlin:image.bzl", "kt_jvm_image")

kt_jvm_image(
    name = "market",
    srcs = [
        "App.kt",
        "CallbackExecutorImpl.kt",
        "MarketAnnotations.kt",
        "MarketDataPublisher.kt",
        "OrderEventPublisherImpl.kt",
    ],
    main_class = "com.secwager.market.AppKt",
    deps = [
        "//entity-common:proto-conversion-utils",
        "//entity-common:serdes",
        "//matchengine:orderbook",
        "@maven//:com_google_guava_guava",
        "@maven//:org_apache_kafka_kafka_clients",
    ],
)
