load("@io_bazel_rules_kotlin//kotlin:kotlin.bzl", "kt_jvm_binary")

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

kt_jvm_binary(
    name = "posttrade",
    srcs = [
        "App.kt",
        "InstrumentDao.kt",
        "InstrumentDaoImpl.kt",
        "PosttradeService.kt",
    ],
    main_class = "secwager.posttrade.AppKt",
    resources = [],
    deps = [
        ":dagger_lib",
        "//jvm-common:serdes",
        "//proto:market_java_proto",
        "@maven//:ch_qos_logback_logback_classic",
        "@maven//:com_google_guava_guava",
        "@maven//:org_apache_kafka_kafka_streams",
        "@maven//:org_jetbrains_kotlinx_kotlinx_coroutines_core",
    ],
)
