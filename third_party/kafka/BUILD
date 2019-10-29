# Description:
#   Kafka C/C++ (librdkafka) client library

licenses(["notice"])  # 2-clause BSD license

exports_files(["LICENSE"])

load("@rules_foreign_cc//tools/build_defs:cmake.bzl", "cmake_external")

cmake_external(
    name = "librdkafka",
    cache_entries = {
        "RDKAFKA_BUILD_STATIC": "ON",
        #istio will take care of ssl for us, eventually.
        "WITH_SSL": "OFF",
        "WITH_ZSTD": "OFF",
        "WITH_SASL": "OFF",
        "ENABLE_LZ4_EXT": "OFF",
        "WITH_LIBDL": "OFF",
        "WITH_LIBZ": "OFF",
        "WITH_ZLIB": "OFF",
        "WITH_LIBCRC": "OFF",
        "RDKAFKA_BUILD_TESTS": "OFF",
        "RDKAFKA_BUILD_EXAMPLES": "OFF",
    },
    lib_source = "@kafka//:all",
    #out_lib_dir = "lib64",
    static_libraries = [
        "librdkafka++.a",
        "librdkafka.a",
    ],
    visibility = ["//visibility:public"],
    #deps = ["@boringssl//:ssl"], wait for istio to handle ssl instead.
)
