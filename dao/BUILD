package(default_visibility = ["//visibility:public"])

java_library(
    name = "order-repo",
    srcs = ["src/main/java/com/secwager/dao/order/OrderRepo.java"],
    deps = ["//proto:market_java_proto"],
)

java_library(
    name = "order-repo-jdbc",
    srcs = ["src/main/java/com/secwager/dao/order/OrderRepoImpl.java"],
    deps = [
        ":order-repo",
        "//proto:market_java_proto",
        "@maven//:commons_dbutils_commons_dbutils",
        "@maven//:javax_inject_javax_inject",
        "@maven//:org_slf4j_slf4j_api",
    ],
)
