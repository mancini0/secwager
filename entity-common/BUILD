load("@io_bazel_rules_kotlin//kotlin:kotlin.bzl", "kt_jvm_library", "kt_jvm_test")

package(default_visibility = ["//visibility:public"])

kt_jvm_library(
    name = "dto",
    srcs = ["src/main/kotlin/Dto.kt"],
    deps = [
        "//proto:market_java_proto",
    ],
)

kt_jvm_library(
    name = "proto-conversion-utils",
    srcs = ["src/main/kotlin/ConversionUtils.kt"],
    deps = [
        ":dto",
        "//proto:market_java_proto",
    ],
)


kt_jvm_test(
    name = "proto-to-dto-conversion-tests",
    srcs = ["src/test/kotlin/ProtoToDtoConversionUtilsTest.kt"],
    test_class = "com.secwager.utils.ProtoToDtoConversionUtilsTest",
    deps = [
        ":proto-conversion-utils",
        "//proto:market_java_proto",
        "@maven//:com_google_truth_truth",
        "@maven//:junit_junit",
    ],
)
