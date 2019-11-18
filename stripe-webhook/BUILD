load("@io_bazel_rules_kotlin//kotlin:kotlin.bzl", "kt_jvm_binary")

kt_jvm_binary(
    name = "stripe-webhook",
    srcs = ["src/main/kotlin/StripeWebhook.kt"],
    main_class = "secwager.stripe.StripeWebhookKt",
    resources = [
        "src/main/resources/application.conf",
        "src/main/resources/logback.xml",
    ],
    deps = [
        "@maven//:ch_qos_logback_logback_classic",
        "@maven//:com_github_jasync_sql_jasync_postgresql",
        "@maven//:io_ktor_ktor_gson",
        "@maven//:io_ktor_ktor_server_netty",
        "@maven//:org_jetbrains_kotlinx_kotlinx_coroutines_core",
    ],
)
