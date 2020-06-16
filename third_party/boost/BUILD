load("@rules_foreign_cc//tools/build_defs:boost_build.bzl", "boost_build")
package(default_visibility = ["//visibility:public"])

boost_build(
    name = "boost_atomic",
    lib_source = "@boost//:all",
    static_libraries = ["libboost_atomic.a"],
    user_options = ["--with-atomic"],
)

boost_build(
    name = "boost_thread",
    lib_source = "@boost//:all",
    static_libraries = ["libboost_thread.a"],
    user_options = ["--with-thread"],
)

boost_build(
    name = "boost_system",
    lib_source = "@boost//:all",
    static_libraries = ["libboost_system.a"],
    user_options = ["--with-system"],
)

boost_build(
    name = "boost_date_time",
    lib_source = "@boost//:all",
    static_libraries = ["libboost_date_time.a"],
    user_options = ["--with-date_time"],
)

boost_build(
    name = "boost_regex",
    lib_source = "@boost//:all",
    static_libraries = ["libboost_regex.a"],
    user_options = ["--with-regex"],
)

boost_build(
    name = "boost_filesystem",
    lib_source = "@boost//:all",
    static_libraries = ["libboost_filesystem.a"],
    user_options = ["--with-filesystem"],
)

boost_build(
    name = "boost_random",
    lib_source = "@boost//:all",
    static_libraries = ["libboost_random.a"],
    user_options = ["--with-random"],
)

boost_build(
    name = "boost_chrono",
    lib_source = "@boost//:all",
    static_libraries = ["libboost_chrono.a"],
    user_options = ["--with-chrono"],
)

boost_build(
    name = "boost_serialization",
    lib_source = "@boost//:all",
    static_libraries = ["libboost_serialization.a"],
    user_options = ["--with-serialization"],
)
