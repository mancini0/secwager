load("@io_bazel_rules_kotlin//kotlin:kotlin.bzl", "kt_jvm_library", "kt_jvm_test")

package(default_visibility = ["//visibility:public"])

kt_jvm_library(
    name = "intervention-service",
    srcs = [
        "InterventionService.kt",
    ],
    deps = [
    ],
)

kt_jvm_library(
    name = "intervention-service-logging-impl",
    srcs = [
        "InterventionServiceLoggingImpl.kt",
    ],
    deps = [
        ":intervention-service",
        "@maven//:org_slf4j_slf4j_api",
    ],
)
