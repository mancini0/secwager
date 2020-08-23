load("@io_bazel_rules_kotlin//kotlin:kotlin.bzl", "kt_jvm_library", "kt_jvm_test")

package(default_visibility = ["//visibility:public"])

kt_jvm_library(
    name = "emergency-service",
    srcs = [
        "EmergencyService.kt",
    ],
    deps = [
    ],
)

kt_jvm_library(
    name = "emergency-service-logging-impl",
    srcs = [
        "EmergencyServiceLoggingImpl.kt",
    ],
    deps = [
        ":emergency-service",
        "@maven//:org_slf4j_slf4j_api",
    ],
)
