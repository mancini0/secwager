load("@io_grpc_grpc_java//:java_grpc_library.bzl", "java_grpc_library")

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
        "//proto:market_java_proto",
        "//proto:orderentry_java_proto",
        "@maven//:io_grpc_grpc_stub",
        "@maven//:javax_inject_javax_inject",
        "@maven//:org_apache_kafka_kafka_clients",
        "@maven//:org_slf4j_slf4j_api",
    ],
)

java_binary(
    name = "orderentry",
    srcs = [
        "src/main/java/com/secwager/orderentry/App.java",
        "src/main/java/com/secwager/orderentry/di/OrderEntryComponent.java",
        "src/main/java/com/secwager/orderentry/di/OrderEntryModule.java",
    ],
    main_class = "com.secwager.orderentry.App",
    deps = [
        ":dagger_lib",
        ":orderentry-service",
        "//grpc-common:grpc-server",
        "@maven//:org_apache_kafka_kafka_clients",
    ],
)
