load("@io_grpc_grpc_java//:java_grpc_library.bzl", "java_grpc_library")
load("@io_bazel_rules_docker//java:image.bzl", "java_image")
load("@io_bazel_rules_docker//container:container.bzl", "container_push")

java_grpc_library(
    name = "marketdata-grpc",
    srcs = ["//proto:market_data_proto"],
    deps = ["//proto:market_data_java_proto"],
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
    name = "marketdata-dao-interface",
    srcs = ["src/main/java/com/secwager/marketdata/dao/InstrumentRepo.java"],
    deps = [
        "//proto:market_data_java_proto",
    ],
)

java_library(
    name = "marketdata-dao-impl",
    srcs = [
        "src/main/java/com/secwager/marketdata/dao/InstrumentRepoIgniteImpl.java",
        "src/main/java/com/secwager/marketdata/dao/Queries.java",
    ],
    deps = [
        ":marketdata-dao-interface",
        "//proto:market_data_java_proto",
        "@maven//:commons_dbutils_commons_dbutils",
        "@maven//:javax_inject_javax_inject",
        "@maven//:org_slf4j_slf4j_api",
    ],
)

java_library(
    name = "marketdata-service",
    srcs = [
        "src/main/java/com/secwager/marketdata/MarketDataServiceImpl.java",
    ],
    exports = [":marketdata-grpc"],
    deps = [
        ":marketdata-dao-interface",
        ":marketdata-grpc",
        "//proto:market_data_java_proto",
        "@maven//:io_grpc_grpc_stub",
        "@maven//:javax_inject_javax_inject",
        "@maven//:org_slf4j_slf4j_api",
    ],
)

java_image(
    name = "marketdata",
    srcs = [
        "src/main/java/com/secwager/marketdata/App.java",
        "src/main/java/com/secwager/marketdata/di/MarketDataComponent.java",
        "src/main/java/com/secwager/marketdata/di/MarketDataModule.java",
    ],
    main_class = "com.secwager.marketdata.App",
    deps = [
        ":dagger_lib",
        ":marketdata-dao-impl",
        ":marketdata-dao-interface",
        ":marketdata-service",
        "//grpc-common:grpc-server",
        "@io_grpc_grpc_java//api",
        "@maven//:ch_qos_logback_logback_classic",
        "@maven//:commons_dbutils_commons_dbutils",
        "@maven//:org_apache_ignite_ignite_core",
        "@maven//:org_apache_kafka_kafka_clients",
    ],
)

container_push(
    name = "push_marketdata",
    format = "Docker",
    image = ":marketdata",
    registry = "us.gcr.io",
    repository = "secwager/marketdata",
    tag = "dev",
)
