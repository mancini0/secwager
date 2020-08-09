load("@io_bazel_rules_k8s//k8s:object.bzl", "k8s_object")
load("//:defs.bzl", "K8S_CLUSTER")
load("@io_bazel_rules_kotlin//kotlin:kotlin.bzl", "kt_jvm_binary", "kt_jvm_library")
load("@com_github_grpc_grpc_kotlin//:kt_jvm_grpc.bzl", "kt_jvm_grpc_library")

kt_jvm_library(
    name = "sandbox",
    srcs = ["Sandbox.kt"],
    deps = [
        ":kt_grpc_hello",
        "@com_github_grpc_grpc_kotlin//stub/src/main/java/io/grpc/kotlin:stub",
        "@maven//:org_jetbrains_kotlinx_kotlinx_coroutines_core",
    ],
)

proto_library(
    name = "hello_proto",
    srcs = ["hello.proto"],
)

java_proto_library(
    name = "java_hello_proto",
    deps = [":hello_proto"],
)

kt_jvm_grpc_library(
    name = "kt_grpc_hello",
    srcs = [":hello_proto"],
    deps = [
        ":java_hello_proto",
    ],
)
