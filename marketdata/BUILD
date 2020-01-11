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
    name = "marketdata-service",
    srcs = [
        "src/main/java/com/secwager/marketdata/MarketDataServiceImpl.java",
        "src/main/java/com/secwager/marketdata/ObserverNotifyingMarketDataEventListener.java",
    ],
    exports = [
        ":marketdata-grpc",
        "@maven//:com_google_auth_google_auth_library_oauth2_http",
        "@maven//:com_google_cloud_google_cloud_firestore",
        "@maven//:com_google_firebase_firebase_admin",
        "@maven//:org_slf4j_slf4j_api",
    ],
    deps = [
        ":marketdata-grpc",
        "//proto:market_data_java_proto",
        "//proto:market_java_proto",
        "//proto:market_proto",
        "@maven//:com_google_auth_google_auth_library_oauth2_http",
        "@maven//:com_google_cloud_google_cloud_firestore",
        "@maven//:com_google_code_findbugs_jsr305",
        "@maven//:com_google_firebase_firebase_admin",
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
        "src/main/java/com/secwager/marketdata/di/MarketDataFirebaseModule.java",
    ],
    main_class = "com.secwager.marketdata.App",
    resources = [
        "src/main/resources/logback.xml",
        "src/main/resources/service-key.json",
    ],
    deps = [
        ":dagger_lib",
        ":marketdata-service",
        "//grpc-common:grpc-server",
        "//proto:market_data_java_proto",
        "@io_grpc_grpc_java//api",
        "@maven//:ch_qos_logback_logback_classic",
        "@maven//:javax_annotation_javax_annotation_api",
    ],
)

container_push(
    name = "push",
    format = "Docker",
    image = ":marketdata",
    registry = "us.gcr.io",
    repository = "secwager/marketdata",
    tag = "dev",
)
