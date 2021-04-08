package(default_visibility = ["//visibility:public"])

load("@io_bazel_rules_kotlin//kotlin:kotlin.bzl", "kt_jvm_binary", "kt_jvm_library", "kt_jvm_test")

kt_jvm_test(
    name = "cashier-dao-test",
    srcs = ["src/test/kotlin/CashierDaoTest.kt"],
    test_class = "com.secwager.dao.cashier.CashierDaoTest",
    deps = [
        ":cashier-dao-impl-jasync",
        "//infra:db-init",
        "@maven//:ch_qos_logback_logback_classic",
        "@maven//:com_github_jasync_sql_jasync_postgresql",
        "@maven//:com_google_truth_truth",
        "@maven//:com_nhaarman_mockitokotlin2_mockito_kotlin",
        "@maven//:com_zaxxer_HikariCP",
        "@maven//:junit_junit",
        "@maven//:org_jetbrains_kotlinx_kotlinx_coroutines_core",
        "@maven//:org_mockito_mockito_core",
        "@maven//:org_postgresql_postgresql",
        "@maven//:org_testcontainers_postgresql",
        "@maven//:org_testcontainers_testcontainers",
    ],
)

kt_jvm_library(
    name = "cashier-dao",
    srcs = ["src/main/kotlin/CashierDao.kt"],
    deps = ["//proto:cashier_java_proto"],
)

kt_jvm_library(
    name = "cashier-dao-impl-jasync",
    srcs = ["src/main/kotlin/CashierDaoJasyncImpl.kt"],
    deps = [
        ":cashier-dao",
        "@maven//:com_github_jasync_sql_jasync_postgresql",
        "@maven//:javax_inject_javax_inject",
        "@maven//:org_jetbrains_kotlinx_kotlinx_coroutines_core",
        "@maven//:org_jetbrains_kotlinx_kotlinx_coroutines_jdk8",
        "@maven//:org_slf4j_slf4j_api",
    ],
)
