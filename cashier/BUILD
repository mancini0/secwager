load("@io_bazel_rules_docker//kotlin:image.bzl", "kt_jvm_image")
load("@io_bazel_rules_docker//container:container.bzl", "container_push")
load("@io_bazel_rules_k8s//k8s:object.bzl", "k8s_object")
load("@io_bazel_rules_kotlin//kotlin:kotlin.bzl", "kt_jvm_binary", "kt_jvm_library", "kt_jvm_test")
load("//:defs.bzl", "K8S_CLUSTER")

k8s_object(
    name = "k8s",
    cluster = K8S_CLUSTER,
    images = {
        "us.gcr.io/secwager/cashier:dev": ":cashier",
    },
    template = ":k8s.yaml",
    visibility = ["//infra:__pkg__"],
)

kt_jvm_library(
    name = "cashier-service",
    srcs = [
        "src/main/kotlin/CashierServiceImpl.kt",
    ],
    deps = [
        "//dao:cashier-dao",
        "//proto:kt_grpc_cashier",
        "@com_github_grpc_grpc_kotlin//stub/src/main/java/io/grpc/kotlin:stub",
        "@maven//:com_github_ben_manes_caffeine_caffeine",
        "@maven//:javax_inject_javax_inject",
        "@maven//:org_jetbrains_kotlinx_kotlinx_coroutines_core",
        "@maven//:org_slf4j_slf4j_api",
    ],
)

kt_jvm_image(
    name = "cashier",
    srcs = [
        "src/main/kotlin/App.kt",
        "src/main/kotlin/di/CashierComponent.kt",
        "src/main/kotlin/di/CashierModule.kt",
    ],
    main_class = "com.secwager.cashier.AppKt",
    resources = ["src/main/resources/logback.xml"],
    deps = [
        ":cashier-service",
        "//:dagger",
        "//dao:cashier-dao-impl-jasync",
        "@maven//:ch_qos_logback_logback_classic",
        "@maven//:org_postgresql_postgresql",
    ],
)
