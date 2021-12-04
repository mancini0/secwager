load("@io_bazel_rules_kotlin//kotlin:kotlin.bzl", "kt_jvm_binary", "kt_jvm_library")
load("@io_bazel_rules_docker//kotlin:image.bzl", "kt_jvm_image")
load("@io_bazel_rules_k8s//k8s:object.bzl", "k8s_object")
load("//:defs.bzl", "K8S_CLUSTER")

k8s_object(
    name = "k8s",
    cluster = K8S_CLUSTER,
    images = {
        "us.gcr.io/secwager/market:dev": ":market",
    },
    template = ":k8s.yaml",
    visibility = ["//infra:__pkg__"],
)

kt_jvm_image(
    name = "market",
    srcs = [
        "App.kt",
        "CallbackExecutorImpl.kt",
        "MarketDataPublisher.kt",
        "OrderEventPublisherImpl.kt",
    ],
    main_class = "com.secwager.market.AppKt",
    deps = [
        "//entity-common:proto-conversion-utils",
        "//matchengine:orderbook",
        "@maven//:com_google_guava_guava",
        "@maven//:org_apache_kafka_kafka_clients",
    ],
)
