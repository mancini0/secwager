load("@npm//webpack-cli:index.bzl", webpack = "webpack_cli")
load("@io_bazel_rules_docker//nodejs:image.bzl", "nodejs_image")
load("@build_bazel_rules_nodejs//:index.bzl", "nodejs_binary")

webpack(
    name = "bundle",
    outs = ["public/app.bundle.js"],
    args = [
        "$(execpath src/index.js)",
        "--config",
        "$(execpath webpack.config.js)",
        "-o",
        "$@",
    ],
    data = glob(["src/**"]) + [
        ".babelrc",
        "webpack.config.js",
        "//proto:orderentry_js_proto",
        "@npm//:node_modules",
    ],
)

nodejs_binary(
    name = "ui",
    args = [
        "ui2/public",  #server base path
    ],
    data = [
        "public/app.bundle.js",
        "public/index.html",
        "@npm//http-server",
    ],
    entry_point = "@npm//:node_modules/http-server/bin/http-server",
)
