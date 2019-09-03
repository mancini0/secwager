FROM ubuntu:18.04

RUN \
    apt-get update && \
    apt-get -y install \
        python \
        python3 \
        cmake \
        build-essential \
        openjdk-8-jdk \
        zip \
        unzip \
        bash \
        curl

ARG EXTRA_BAZEL_ARGS="--host_javabase=@local_jdk//:jdk"
RUN curl -L  https://github.com/bazelbuild/bazel/releases/download/0.29.0/bazel-0.29.0-dist.zip --output bazel-0.29.0-dist.zip
RUN unzip bazel-0.29.0-dist.zip -d bazel-0.29.0 && cd bazel-0.29.0 && bash./compile.sh && cp /bazel-0.29.0/output/bazel /bin/ && bazel version
ENTRYPOINT ["bazel"]
