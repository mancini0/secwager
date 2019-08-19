workspace(name = "secwager")

load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")
load("@bazel_tools//tools/build_defs/repo:git.bzl", "new_git_repository")
load("@bazel_tools//tools/build_defs/repo:git.bzl", "git_repository")

http_archive(
    name = "io_bazel_rules_docker",
    sha256 = "e513c0ac6534810eb7a14bf025a0f159726753f97f74ab7863c650d26e01d677",
    strip_prefix = "rules_docker-0.9.0",
    urls = ["https://github.com/bazelbuild/rules_docker/archive/v0.9.0.tar.gz"],
)

http_archive(
    name = "com_github_grpc_grpc",
    sha256 = "11ac793c562143d52fd440f6549588712badc79211cdc8c509b183cb69bddad8",
    strip_prefix = "grpc-1.22.0",
    urls = [
        "https://github.com/grpc/grpc/archive/v1.22.0.tar.gz",
    ],
)

all_content = """filegroup(name = "all", srcs = glob(["**"]), visibility = ["//visibility:public"])"""

http_archive(
    name = "rules_foreign_cc",
    strip_prefix = "rules_foreign_cc-master",
    url = "https://github.com/bazelbuild/rules_foreign_cc/archive/master.zip",
)

load("@rules_foreign_cc//:workspace_definitions.bzl", "rules_foreign_cc_dependencies")
rules_foreign_cc_dependencies()


http_archive(
    name = "kafka",
    build_file_content = all_content,
    sha256 = "123b47404c16bcde194b4bd1221c21fdce832ad12912bd8074f88f64b2b86f2b",
    strip_prefix = "librdkafka-1.1.0",
    urls = [
        "https://github.com/edenhill/librdkafka/archive/v1.1.0.tar.gz",
    ],
)


git_repository(
    name = "boringssl",
    commit = "edb6488b63995693c2a6155e0b4e41c27d33b6c6",
    remote = "https://boringssl.googlesource.com/boringssl",
)

new_git_repository(
    name = "liquibook",
    build_file_content = """cc_library(
                                name = "liquibook",
                                hdrs = glob(["src/book/*.h"]),
                                strip_include_prefix ="src/book",
                                include_prefix="liquibook",
                                visibility = ["//visibility:public"],
                            )""",
    commit = "828a771d715438f487db15bbc6d6c310a952e26d",
    remote = "https://github.com/objectcomputing/liquibook.git",
    shallow_since = "1562647708 -0500",
)

load("@com_github_grpc_grpc//bazel:grpc_deps.bzl", "grpc_deps")

grpc_deps()

load(
    "@io_bazel_rules_docker//repositories:repositories.bzl",
    container_repositories = "repositories",
)

container_repositories()

load(
    "@io_bazel_rules_docker//cc:image.bzl",
    _cc_image_repos = "repositories",
)

_cc_image_repos()


http_archive(
        name = "boost",
        build_file_content = all_content,
        strip_prefix = "boost_1_70_0",
        sha256 = "882b48708d211a5f48e60b0124cf5863c1534cd544ecd0664bb534a4b5d506e9",
        urls = ["https://dl.bintray.com/boostorg/release/1.70.0/source/boost_1_70_0.tar.gz"],
    )