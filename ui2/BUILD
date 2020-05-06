load("@npm//http-server:index.bzl", "http_server")
load("@npm//webpack-cli:index.bzl", webpack = "webpack_cli")

webpack(
    name = "bundle",
    outs = ["public/app.bundle.js"],
    args = [
        "$(execpath index.js)",
        "--config",
        "$(execpath webpack.config.js)",
        "-o",
        "$@",
    ],
    data = [
        ".babelrc",
        "index.js",
        "webpack.config.js",
        "@npm//:node_modules",
    ],
)

http_server(
    name = "server",
    data = [
        "public/app.bundle.js",
        "public/index.html",
    ],
)
