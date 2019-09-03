FROM alpine:3.10
RUN apk update && apk add \
    curl \
    git \
    tar \
    g++ \
    cmake \
    bash \
    zip \
    unzip \
    python2 \
    python3 \
    linux-headers \
    openjdk8

ARG EXTRA_BAZEL_ARGS="--host_javabase=@local_jdk//:jdk"
ENV JAVA_HOME=/usr/lib/jvm/java-1.8-openjdk
RUN \
 curl -L  https://github.com/bazelbuild/bazel/releases/download/0.29.0/bazel-0.29.0-dist.zip --output bazel-0.29.0-dist.zip \
 && unzip bazel-0.29.0-dist.zip -d bazel-0.29.0 \
 && rm bazel-0.29.0-dist.zip \
 && cd bazel-0.29.0 \
 && bash ./compile.sh \
 && mv /bazel-0.29.0/output/bazel /bin/
ENTRYPOINT ["bazel"]