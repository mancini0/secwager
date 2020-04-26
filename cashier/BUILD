load("@io_grpc_grpc_java//:java_grpc_library.bzl", "java_grpc_library")
load("@io_bazel_rules_docker//kotlin:image.bzl", "kt_jvm_image")
load("@io_bazel_rules_docker//container:container.bzl", "container_push")
load("@io_bazel_rules_k8s//k8s:object.bzl", "k8s_object")
load("@io_bazel_rules_kotlin//kotlin:kotlin.bzl", "kt_jvm_library", "kt_jvm_test")
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
    name = "cashier-repository",
    srcs = ["src/main/kotlin/CashierRepo.kt"],
    deps = ["//proto:cashier_java_proto"],
)

kt_jvm_library(
    name = "cashier-repository-jdbc",
    srcs = ["src/main/kotlin/CashierRepoJdbcImpl.kt"],
    deps = [
        ":cashier-repository",
        "@maven//:commons_dbutils_commons_dbutils",
        "@maven//:javax_inject_javax_inject",
        "@maven//:org_slf4j_slf4j_api",
    ],
)

kt_jvm_library(
    name = "cashier-service",
    srcs = [
        "src/main/kotlin/CashierServiceImpl.kt",
    ],
    exports = ["//proto:cashier-grpc"],
    deps = [
        "//proto:cashier-grpc",
        "//proto:cashier_java_proto",
        "@maven//:commons_dbutils_commons_dbutils",
        "@maven//:io_grpc_grpc_stub",
        "@maven//:javax_inject_javax_inject",
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
        "//grpc-server",
        "@maven//:ch_qos_logback_logback_classic",
        "@maven//:com_zaxxer_HikariCP",
        "@maven//:commons_dbutils_commons_dbutils",
        "@maven//:org_postgresql_postgresql",
    ],
)
