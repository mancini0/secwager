load("@io_bazel_rules_docker//kotlin:image.bzl", "kt_jvm_image")
load("@io_bazel_rules_k8s//k8s:object.bzl", "k8s_object")
load("//:defs.bzl", "K8S_CLUSTER")

k8s_object(
    name = "k8s",
    cluster = K8S_CLUSTER,
    images = {
        "us.gcr.io/secwager/blockchain:dev": ":blockchain",
    },
    template = ":k8s.yaml",
    visibility = ["//infra:__pkg__"],
)

kt_jvm_image(
    name = "blockchain",
    srcs = [
        "src/main/kotlin/App.kt",
    ],
    main_class = "com.secwager.blockchain.AppKt",
    resources = ["src/main/resources/logback.xml"],
    deps = [
        "@maven//:ch_qos_logback_logback_classic",
        "@maven//:org_bitcoinj_bitcoinj_core",
        "@maven//:org_postgresql_postgresql",
    ],
)
