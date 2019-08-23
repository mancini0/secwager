cc_test(
    name = "market_test",
    srcs = [
        "market_test.cc",
        "mock_proto_sender.h",
    ],
    copts = ["-Iexternal/gtest/include"],
    linkstatic = 1,
    deps = [
        "//matchengine:market",
        "@gtest//:gtest_main",
    ],
)
