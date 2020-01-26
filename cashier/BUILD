load("@io_grpc_grpc_java//:java_grpc_library.bzl", "java_grpc_library")
load("@io_bazel_rules_docker//java:image.bzl", "java_image")
load("@io_bazel_rules_docker//container:container.bzl", "container_push")

java_grpc_library(
    name = "cashier-grpc",
    srcs = ["//proto:cashier_proto"],
    visibility = ["//orderentry:__pkg__"],
    deps = ["//proto:cashier_java_proto"],
)

java_plugin(
    name = "dagger_plugin",
    processor_class = "dagger.internal.codegen.ComponentProcessor",
    deps = [
        "@maven//:com_google_dagger_dagger_compiler",
    ],
)

java_library(
    name = "dagger_lib",
    exported_plugins = ["dagger_plugin"],
    exports = [
        "@maven//:com_google_dagger_dagger",
        "@maven//:javax_inject_javax_inject",
    ],
)

java_library(
    name = "cashier-service",
    srcs = [
        "src/main/java/com/secwager/cashier/CashierServiceImpl.java",
    ],
    exports = [":cashier-grpc"],
    deps = [
        ":cashier-grpc",
        "//proto:cashier_java_proto",
        "@maven//:commons_dbutils_commons_dbutils",
        "@maven//:io_grpc_grpc_stub",
        "@maven//:javax_inject_javax_inject",
        "@maven//:org_slf4j_slf4j_api",
    ],
)

java_image(
    name = "cashier",
    srcs = [
        "src/main/java/com/secwager/cashier/App.java",
        "src/main/java/com/secwager/cashier/di/CashierComponent.java",
        "src/main/java/com/secwager/cashier/di/CashierModule.java",
    ],
    main_class = "com.secwager.cashier.App",
    resources = ["src/main/resources/logback.xml"],
    deps = [
        ":cashier-service",
        ":dagger_lib",
        "//jvm-common:grpc-server",
        "@maven//:ch_qos_logback_logback_classic",
        "@maven//:commons_dbutils_commons_dbutils",
        "@maven//:org_apache_ignite_ignite_core",
    ],
)

container_push(
    name = "push",
    format = "Docker",
    image = ":cashier",
    registry = "us.gcr.io",
    repository = "secwager/cashier",
    tag = "dev",
)
