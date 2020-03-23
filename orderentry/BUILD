load("@io_grpc_grpc_java//:java_grpc_library.bzl", "java_grpc_library")
load("@io_bazel_rules_docker//java:image.bzl", "java_image")
load("@io_bazel_rules_docker//container:container.bzl", "container_push")
load("@io_bazel_rules_k8s//k8s:object.bzl", "k8s_object")
load("//:defs.bzl", "K8S_CLUSTER")

k8s_object(
    name = "k8s",
    cluster = K8S_CLUSTER,
    images = {
        "us.gcr.io/secwager/orderentry:dev": ":orderentry",
    },
    template = ":k8s.yaml",
    visibility = ["//infra:__pkg__"],
)

java_grpc_library(
    name = "orderentry-grpc",
    srcs = ["//proto:orderentry_proto"],
    deps = ["//proto:orderentry_java_proto"],
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
    name = "orderentry-service",
    srcs = [
        "src/main/java/com/secwager/orderentry/OrderEntryServiceImpl.java",
    ],
    exports = [":orderentry-grpc"],
    deps = [
        ":orderentry-grpc",
        "//cashier:cashier-grpc",
        "//proto:cashier_java_proto",
        "//proto:market_java_proto",
        "//proto:orderentry_java_proto",
        "@maven//:io_grpc_grpc_stub",
        "@maven//:javax_inject_javax_inject",
        "@maven//:org_apache_kafka_kafka_clients",
        "@maven//:org_slf4j_slf4j_api",
    ],
)

java_image(
    name = "orderentry",
    srcs = [
        "src/main/java/com/secwager/orderentry/App.java",
        "src/main/java/com/secwager/orderentry/di/OrderEntryComponent.java",
        "src/main/java/com/secwager/orderentry/di/OrderEntryModule.java",
    ],
    main_class = "com.secwager.orderentry.App",
    resources = ["src/main/resources/logback.xml"],
    deps = [
        ":dagger_lib",
        ":orderentry-service",
        "//cashier:cashier-grpc",
        "//grpc-server",
        "@io_grpc_grpc_java//api",
        "@maven//:ch_qos_logback_logback_classic",
        "@maven//:org_apache_kafka_kafka_clients",
    ],
)

container_push(
    name = "push",
    format = "Docker",
    image = ":orderentry",
    registry = "us.gcr.io",
    repository = "secwager/orderentry",
    tag = "dev",
)
