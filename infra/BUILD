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

k8s_objects(
    name = "lfg",
    objects = [
        "//cashier:k8s",
        "//market:k8s",
        "//orderentry:k8s",
    ],
)
