exports_files(["LICENSE"])

load("@rules_foreign_cc//tools/build_defs:cmake.bzl", "cmake_external")

cmake_external(
    name = "spdlog",
    cache_entries = {
        "SPDLOG_BUILD_TESTS": "OFF",
        "SPDLOG_BUILD_BENCH": "OFF",
    },
    headers_only = 1,
    lib_source = "@spdlog//:all",
    visibility = ["//visibility:public"],
)
