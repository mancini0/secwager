workspace(
    name = "secwager",
)

load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")
load("@bazel_tools//tools/build_defs/repo:git.bzl", "new_git_repository")
load("@bazel_tools//tools/build_defs/repo:git.bzl", "git_repository")

all_content = """filegroup(name = "all", srcs = glob(["**"],exclude = ["libs/wave/test/**/*"]), visibility = ["//visibility:public"])"""

rules_jvm_external_tag = "2.10"

rules_jvm_external_sha = "1bbf2e48d07686707dd85357e9a94da775e1dbd7c464272b3664283c9c716d26"

dagger_version = "2.23.2"

grpc_version = "1.26.0"

ktor_version = "1.2.5"

vertx_version = "3.8.4"

kafka_version = "2.4.0"

http_archive(
    name = "rules_jvm_external",
    sha256 = rules_jvm_external_sha,
    strip_prefix = "rules_jvm_external-%s" % rules_jvm_external_tag,
    url = "https://github.com/bazelbuild/rules_jvm_external/archive/%s.zip" % rules_jvm_external_tag,
)

load("@rules_jvm_external//:defs.bzl", "maven_install")
load("@rules_jvm_external//:specs.bzl", "maven")

maven_install(
    name = "maven",
    artifacts = [
        "junit:junit:4.13",
        "org.apache.kafka:kafka-streams-test-utils:%s" % kafka_version,
        "com.google.truth:truth:1.0.1",
        "com.google.guava:guava:28.2-jre",
        "org.apache.kafka:kafka-streams:%s" % kafka_version,
        "org.apache.kafka:kafka-clients:%s" % kafka_version,
        "org.apache.kafka:kafka-streams-test-utils:%s" % kafka_version,
        "org.springframework.kafka:spring-kafka:2.1.10.RELEASE",
        "javax.annotation:javax.annotation-api:1.3.2",
        "org.apache.ignite:ignite-core:2.7.6",
        "io.grpc:grpc-netty-shaded:%s" % grpc_version,
        "io.grpc:grpc-api:%s" % grpc_version,
        "io.grpc:grpc-testing:%s" % grpc_version,
        "io.grpc:grpc-core:%s" % grpc_version,
        "io.grpc:grpc-stub:%s" % grpc_version,
        "com.google.dagger:dagger:%s" % dagger_version,
        "com.google.dagger:dagger-compiler:%s" % dagger_version,
        "javax.inject:javax.inject:1",
        "junit:junit:4.12",
        "org.slf4j:slf4j-api:1.7.25",
        "ch.qos.logback:logback-classic:1.2.3",
        "commons-dbutils:commons-dbutils:1.7",
        "org.jetbrains.kotlinx:kotlinx-coroutines-core:jar:1.3.2",
        "com.google.code.findbugs:jsr305:3.0.2",
        maven.artifact(
            group = "com.google.firebase",
            artifact = "firebase-admin",
            version = "6.12.0",
            exclusions = [
                "io.grpc:grpc-core",
                "io.grpc:grpc-api",
                "io.grpc:grpc-netty-shaded",
            ],
        ),
    ],
    repositories = [
        "https://jcenter.bintray.com/",
        "https://repo1.maven.org/maven2",
    ],
)

http_archive(
    name = "io_bazel_rules_docker",
    #sha256 = "e513c0ac6534810eb7a14bf025a0f159726753f97f74ab7863c650d26e01d677",
    strip_prefix = "rules_docker-0.12.1",
    urls = ["https://github.com/bazelbuild/rules_docker/releases/download/v0.12.1/rules_docker-v0.12.1.tar.gz"],
)

http_archive(
    name = "rules_foreign_cc",
    #sha256 = "045a24ac29402074fd20cba3fd472578cf1861e0b3d5585652e4b0dd249e92d6",
    strip_prefix = "rules_foreign_cc-master",
    url = "https://github.com/bazelbuild/rules_foreign_cc/archive/master.zip",
)

http_archive(
    name = "cmake",
    build_file_content = all_content,
    strip_prefix = "CMake-3.12.1",
    urls = [
        "https://github.com/Kitware/CMake/archive/v3.12.1.tar.gz",
    ],
)

load("@rules_foreign_cc//:workspace_definitions.bzl", "rules_foreign_cc_dependencies")

rules_foreign_cc_dependencies(["//:built_cmake_toolchain"])

http_archive(
    name = "io_grpc_grpc_java",
    strip_prefix = "grpc-java-%s" % grpc_version,
    url = "https://github.com/grpc/grpc-java/archive/v%s.zip" % grpc_version,
)

http_archive(
    name = "com_google_protobuf",
    strip_prefix = "protobuf-3.10.0",
    urls = ["https://github.com/google/protobuf/archive/v3.10.0.zip"],
)

load("@com_google_protobuf//:protobuf_deps.bzl", "protobuf_deps")

protobuf_deps()

load("@io_grpc_grpc_java//:repositories.bzl", "grpc_java_repositories")

grpc_java_repositories(omit_com_google_protobuf = True)

http_archive(
    name = "kafka",
    build_file_content = all_content,
    #sha256 = "123b47404c16bcde194b4bd1221c21fdce832ad12912bd8074f88f64b2b86f2b",
    strip_prefix = "librdkafka-1.3.0",
    urls = [
        "https://github.com/edenhill/librdkafka/archive/v1.3.0.tar.gz",
    ],
)

http_archive(
    name = "spdlog",
    build_file_content = all_content,
    sha256 = "160845266e94db1d4922ef755637f6901266731c4cb3b30b45bf41efa0e6ab70",
    strip_prefix = "spdlog-1.3.1",
    urls = [
        "https://github.com/gabime/spdlog/archive/v1.3.1.tar.gz",
    ],
)

http_archive(
    name = "rules_cc",
    strip_prefix = "rules_cc-master",
    urls = ["https://github.com/bazelbuild/rules_cc/archive/master.zip"],
)

git_repository(
    name = "gtest",
    #tag = "release-1.8.1",
    commit = "ed2eef654373c17b96bf5a007bb481a6e96ba629",
    remote = "https://github.com/google/googletest.git",
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

load(
    "@io_bazel_rules_docker//toolchains/docker:toolchain.bzl",
    docker_toolchain_configure = "toolchain_configure",
)

#I shouldn't need this call.00 - defaults should be sufficient, but docker is not found on ubuntu 18.04 for some reason.
docker_toolchain_configure(
    name = "docker_config",
    docker_path = "/snap/bin/docker",
)

load(
    "@io_bazel_rules_docker//repositories:repositories.bzl",
    container_repositories = "repositories",
)

container_repositories()

load(
    "@io_bazel_rules_docker//java:image.bzl",
    _java_image_repos = "repositories",
)
load(
    "@io_bazel_rules_docker//cc:image.bzl",
    _cc_image_repos = "repositories",
)

_java_image_repos()

_cc_image_repos()

http_archive(
    name = "boost",
    build_file_content = all_content,
    strip_prefix = "boost_1_71_0",
    urls = ["https://dl.bintray.com/boostorg/release/1.71.0/source/boost_1_71_0.tar.gz"],
)

rules_kotlin_version = "legacy-1.3.0-rc4"

#rules_kotlin_sha = "9de078258235ea48021830b1669bbbb678d7c3bdffd3435f4c0817c921a88e42"

http_archive(
    name = "io_bazel_rules_kotlin",
    #sha256 = rules_kotlin_sha,
    strip_prefix = "rules_kotlin-%s" % rules_kotlin_version,
    type = "zip",
    urls = ["https://github.com/bazelbuild/rules_kotlin/archive/%s.zip" % rules_kotlin_version],
)

load("@io_bazel_rules_kotlin//kotlin:kotlin.bzl", "kotlin_repositories", "kt_register_toolchains")

kotlin_repositories()

kt_register_toolchains()
