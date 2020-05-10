load("@io_bazel_rules_docker//kotlin:image.bzl", "kt_jvm_image")
load("@io_bazel_rules_k8s//k8s:object.bzl", "k8s_object")
load("@io_bazel_rules_kotlin//kotlin:kotlin.bzl", "kt_jvm_binary", "kt_jvm_library")
load("//:defs.bzl", "K8S_CLUSTER")

kt_jvm_library(
    name = "coin-receiver",
    srcs = [
        "src/main/kotlin/CoinReceiver.kt",
    ],
    resources = ["src/main/resources/logback.xml"],
    deps = [
        "//proto:cashier-grpc",
        "@maven//:ch_qos_logback_logback_classic",
        "@maven//:com_google_cloud_google_cloud_pubsub",
        "@maven//:javax_inject_javax_inject",
        "@maven//:org_bitcoinj_bitcoinj_core",
    ],
)

kt_jvm_binary(
    name = "blockchain-spv",
    srcs = [
        "src/main/kotlin/App.kt",
    ],
    main_class = "com.secwager.blockchain.AppKt",
    resources = ["src/main/resources/logback.xml"],
    deps = [
        ":coin-receiver",
        "@maven//:ch_qos_logback_logback_classic",
    ],
)
