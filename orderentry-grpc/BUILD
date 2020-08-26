load("@io_bazel_rules_docker//kotlin:image.bzl", "kt_jvm_image")
load("@io_bazel_rules_docker//container:container.bzl", "container_push")
load("@io_bazel_rules_k8s//k8s:object.bzl", "k8s_object")
load("@io_bazel_rules_kotlin//kotlin:kotlin.bzl", "kt_jvm_binary", "kt_jvm_library", "kt_jvm_test")
load("//:defs.bzl", "K8S_CLUSTER")

#k8s_object(
#    name = "k8s",
#    cluster = K8S_CLUSTER,
#    images = {
#        "us.gcr.io/secwager/orderentry:dev": ":orderentry",
#    },
#    template = ":k8s.yaml",
#    visibility = ["//infra:__pkg__"],
#)

kt_jvm_library(
    name = "orderentry-service",
    srcs = [
        "src/main/kotlin/JwtServerInterceptor.kt",
        "src/main/kotlin/OrderEntryServiceImpl.kt",
    ],
    deps = [
        "//emergency:emergency-service",
        "//proto:cashier_java_proto",
        "//proto:kt_grpc_cashier",
        "//proto:kt_grpc_order_entry",
        "@io_grpc_grpc_java//context",
        "@maven//:com_google_firebase_firebase_admin",
        "@maven//:javax_inject_javax_inject",
        "@maven//:org_apache_kafka_kafka_clients",
        "@maven//:org_slf4j_slf4j_api",
    ],
)

kt_jvm_test(
    name = "orderentry-service-test",
    srcs = ["src/test/kotlin/OrderEntryServiceTest.kt"],
    test_class = "com.secwager.orderentry.OrderEntryServiceTest",
    deps = [
        ":orderentry-service",
        "//cashier-grpc:cashier-service",
        "//proto:kt_grpc_order_entry",
        "@com_github_grpc_grpc_kotlin//stub/src/main/java/io/grpc/kotlin:stub",
        "@io_grpc_grpc_java//core:inprocess",
        "@maven//:com_nhaarman_mockitokotlin2_mockito_kotlin",
        "@maven//:io_grpc_grpc_testing",
        "@maven//:junit_junit",
        "@maven//:org_apache_kafka_kafka_clients",
        "@maven//:org_mockito_mockito_core",
    ],
)

kt_jvm_image(
    name = "orderentry-grpc",
    srcs = [
        "src/main/kotlin/App.kt",
        "src/main/kotlin/di/OrderEntryComponent.kt",
        "src/main/kotlin/di/OrderEntryModule.kt",
    ],
    main_class = "com.secwager.orderentry.AppKt",
    resources = ["src/main/resources/logback.xml"],
    deps = [
        ":orderentry-service",
        "//:dagger",
        "//emergency:emergency-service-logging-impl",
        "//proto:kt_grpc_cashier",
        "//proto:market_java_proto",
        "@maven//:ch_qos_logback_logback_classic",
        "@maven//:com_google_auth_google_auth_library_oauth2_http",
        "@maven//:com_google_cloud_google_cloud_firestore",
        "@maven//:com_google_firebase_firebase_admin",
        "@maven//:org_apache_kafka_kafka_clients",
    ],
)

#container_push(
#    name = "push",
#    format = "Docker",
#    image = ":orderentry-grpc",
#    registry = "us.gcr.io",
#    repository = "secwager/orderentry",
#    tag = "dev",
#)
