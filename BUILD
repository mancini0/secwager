package(default_visibility = ["//visibility:public"])

load("@rules_foreign_cc//for_workspace:cmake_build.bzl", "cmake_tool")
load("@rules_foreign_cc//tools/build_defs:boost_build.bzl", "boost_build")

java_plugin(
    name = "dagger_plugin",
    processor_class = "dagger.internal.codegen.ComponentProcessor",
    deps = [
        "@maven//:com_google_dagger_dagger_compiler",
    ],
)

java_library(
    name = "dagger",
    exported_plugins = ["dagger_plugin"],
    exports = [
        "@maven//:com_google_dagger_dagger",
        "@maven//:javax_inject_javax_inject",
    ],
)

toolchain(
    name = "built_cmake_toolchain",
    toolchain = "@rules_foreign_cc//tools/build_defs/native_tools:built_cmake",
    toolchain_type = "@rules_foreign_cc//tools/build_defs:cmake_toolchain",
)
