workspace(
    name = "secwager",
    managed_directories = {
        "@ui2_npm": ["ui2/node_modules"],
    },
)

load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")
load("@bazel_tools//tools/build_defs/repo:git.bzl", "new_git_repository")
load("@bazel_tools//tools/build_defs/repo:git.bzl", "git_repository")
load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_file")

rules_jvm_external_tag = "2.10"

rules_jvm_external_sha = "1bbf2e48d07686707dd85357e9a94da775e1dbd7c464272b3664283c9c716d26"

dagger_version = "2.23.2"

grpc_version = "1.28.1"

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
        "org.bitcoinj:bitcoinj-core:0.15.8",
        "com.github.jasync-sql:jasync-postgresql:1.0.17",
        "org.mockito:mockito-core:3.3.1",
        "junit:junit:4.13",
        "com.github.ben-manes.caffeine:caffeine:2.8.2",
        "javax.inject:javax.inject:1",
        "com.squareup.okhttp3:mockwebserver:4.3.1",
        "com.squareup.retrofit2:converter-gson:2.7.1",
        "com.squareup.retrofit2:retrofit:2.7.1",
        "org.apache.kafka:kafka-streams-test-utils:%s" % kafka_version,
        "com.google.truth:truth:1.0.1",
        "com.google.guava:guava:28.2-jre",
        "org.apache.kafka:kafka-streams:%s" % kafka_version,
        "org.apache.kafka:kafka-clients:%s" % kafka_version,
        "org.apache.kafka:kafka-streams-test-utils:%s" % kafka_version,
        "javax.annotation:javax.annotation-api:1.3.2",
        "org.postgresql:postgresql:42.2.11",
        "com.zaxxer:HikariCP:3.4.2",
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
        "com.google.auth:google-auth-library-oauth2-http:0.20.0",
        "com.google.cloud:google-cloud-firestore:1.33.0",
        "com.google.api:api-common:1.9.0",
        maven.artifact(
            group = "com.nhaarman.mockitokotlin2",
            artifact = "mockito-kotlin",
            version = "2.2.0",
            exclusions = ["org.mockito:mockito-core"],
        ),
        maven.artifact(
            group = "com.google.firebase",
            artifact = "firebase-admin",
            version = "6.12.2",
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

git_repository(
    name = "io_bazel_rules_docker",
    branch = "master",
    remote = "https://github.com/bazelbuild/rules_docker.git",
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

_java_image_repos()

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

grpc_java_repositories()

rules_kotlin_version = "legacy-1.3.0"

http_archive(
    name = "io_bazel_rules_kotlin",
    #sha256 = rules_kotlin_sha,
    strip_prefix = "rules_kotlin-%s" % rules_kotlin_version,
    type = "zip",
    urls = ["https://github.com/bazelbuild/rules_kotlin/archive/%s.zip" % rules_kotlin_version],
)

load("@io_bazel_rules_kotlin//kotlin:kotlin.bzl", "kotlin_repositories", "kt_register_toolchains")

KOTLIN_VERSION = "1.3.70"

KOTLINC_RELEASE_SHA = "709d782ff707a633278bac4c63bab3026b768e717f8aaf62de1036c994bc89c7"

KOTLINC_RELEASE = {
    "urls": [
        "https://github.com/JetBrains/kotlin/releases/download/v{v}/kotlin-compiler-{v}.zip".format(v = KOTLIN_VERSION),
    ],
    "sha256": KOTLINC_RELEASE_SHA,
}

kotlin_repositories(compiler_release = KOTLINC_RELEASE)

kt_register_toolchains()

http_archive(
    name = "io_bazel_rules_k8s",
    sha256 = "cc75cf0d86312e1327d226e980efd3599704e01099b58b3c2fc4efe5e321fcd9",
    strip_prefix = "rules_k8s-0.3.1",
    urls = ["https://github.com/bazelbuild/rules_k8s/releases/download/v0.3.1/rules_k8s-v0.3.1.tar.gz"],
)

load("@io_bazel_rules_k8s//k8s:k8s.bzl", "k8s_repositories")

k8s_repositories()

load("@io_bazel_rules_k8s//k8s:k8s_go_deps.bzl", k8s_go_deps = "deps")

k8s_go_deps()

http_archive(
    name = "build_bazel_rules_nodejs",
    sha256 = "f9e7b9f42ae202cc2d2ce6d698ccb49a9f7f7ea572a78fd451696d03ef2ee116",
    urls = ["https://github.com/bazelbuild/rules_nodejs/releases/download/1.6.0/rules_nodejs-1.6.0.tar.gz"],
)

load("@build_bazel_rules_nodejs//:index.bzl", "yarn_install")

yarn_install(
    name = "ui2_npm",
    package_json = "//ui2:package.json",
    yarn_lock = "//ui2:yarn.lock",
)

#https://github.com/grpc/grpc-kotlin.git

git_repository(
    name = "io_grpc_grpc_kotlin",
    branch = "master",
    remote = "https://github.com/grpc/grpc-kotlin.git",
)
