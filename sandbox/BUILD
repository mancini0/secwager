load("@io_grpc_grpc_java//:java_grpc_library.bzl", "java_grpc_library")
load("@io_bazel_rules_docker//java:image.bzl", "java_image")
load("@io_bazel_rules_docker//container:container.bzl", "container_push")
load("@io_bazel_rules_k8s//k8s:object.bzl", "k8s_object")
load("//:defs.bzl", "K8S_CLUSTER")
load("@io_bazel_rules_kotlin//kotlin:kotlin.bzl", "kt_jvm_binary", "kt_jvm_library")
load("@com_github_grpc_grpc_kotlin//:kt_jvm_grpc.bzl", "kt_jvm_grpc_library")

kt_jvm_library(
    name = "sandbox",
    srcs = ["Sandbox.kt"],
    deps = [
        ":kt_grpc_hello",
        "@io_grpc_grpc_java//netty",
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
    deps = [":java_hello_proto"],
)
