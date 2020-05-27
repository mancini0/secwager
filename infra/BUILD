load("@io_bazel_rules_k8s//k8s:object.bzl", "k8s_object")
load("@io_bazel_rules_k8s//k8s:objects.bzl", "k8s_objects")
load("//:defs.bzl", "K8S_CLUSTER")

k8s_object(
    name = "kafka",
    cluster = K8S_CLUSTER,
    template = ":kafka.yaml",
)

k8s_object(
    name = "ingress",
    cluster = K8S_CLUSTER,
    template = ":ingress.yaml",
)

java_library(
    name = "db-init",
    srcs = ["src/main/java/com/secwager/database/DatabaseInitializer.java"],
    resources = ["src/main/resources/db/migration/V1__initial_ddl.sql"],
    visibility = ["//visibility:public"],
    deps = [
        "@maven//:org_flywaydb_flyway_core",
        "@maven//:org_slf4j_slf4j_api",
    ],
)

#k8s_objects(
#    name = "everything",
#    objects = [
#        #"//cashier:k8s",
#        ":kafka",
#        ":ingress",
#        "//market:k8s",
#        "//orderentry:k8s",
#    ],
#)
