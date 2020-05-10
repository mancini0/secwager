load("@io_bazel_rules_kotlin//kotlin:kotlin.bzl", "kt_jvm_library", "kt_jvm_test")

package(default_visibility = ["//visibility:public"])

kt_jvm_library(
    name = "football-data-client",
    srcs = [
        "src/main/kotlin/FootballDataClient.kt",
        "src/main/kotlin/Models.kt",
    ],
    resources = [],
    deps = [
        "@maven//:com_squareup_retrofit2_converter_gson",
        "@maven//:com_squareup_retrofit2_retrofit",
    ],
)

kt_jvm_test(
    name = "test-client",
    srcs = [
        "src/test/kotlin/FootballDataClientTest.kt",
    ],
    resources = ["src/test/resources/serieAweek23.json"],
    test_class = "com.secwager.refdata.FootballDataClientTest",
    deps = [
        ":football-data-client",
        "@maven//:com_google_truth_truth",
        "@maven//:com_squareup_okhttp3_mockwebserver",
        "@maven//:junit_junit",
    ],
)
