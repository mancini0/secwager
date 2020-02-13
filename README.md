# secwager
Liquibook / gRPC / Kafka based financial exchange composed of various microservices running on Kubernetes which aims to securitize sports wagers as tradeable binary option instruments.

This repo demonstrates what I've found to be the most productive framework to develop cloud native microservices:

1. Keep all of your code in a monorepo. CI/CD is easier in a monorepo when using Bazel as your build system - see this [talk](https://www.youtube.com/watch?v=DTvXa-iqrfA). 
2. Define your microservices as [gRPC](https://grpc.io) services (see /proto directory for examples).
    - protobuf / gRPC enjoys a very concise syntax, compared to alternatives such as openapi.
    - gRPC services use HTTP/2  and are performant. Protobuf allows faster serialization compared to json, and is typesafe.
    - gRPC services are [easy to test](https://github.com/mancini0/bazel-grpc-playground/blob/9651f7912f4d46da7c13bb96019aa1dfc52bbf3d/capitalization/src/test/java/com/example/capitalization/CapitalizationServiceTest.java#L34)
    - The proto compiler, protoc,  can transform your protobuf message definitions / gRPC service definitions into clients and libraries in several languages. Bazel will handle this for you!
3. Build your code using Bazel. Bazel offers you the following luxuries:
    - it provides fast, reproducible builds.
    - it builds only what needs to be rebuilt (modified code, or any code that is dependent on modified code or dependencies)
    - it allows you develop different pieces of your codebase in different languages
    - it converts your protobuf messages and grpc definitions to library code in several languages
    - using [rules_docker](https://github.com/bazelbuild/rules_docker), Bazel can containerize your service by simply specifying a java_image target instead of java_binary target (or cc_image, go_image, etc). The developer need not specify a Dockerfile, or even have Docker installed locally! 
    - rules_docker also handles pushing your images to your container registry, and only rebuilds / repushes your image when the image has been affected by a code change.
    - [rules_k8s](https://github.com/bazelbuild/rules_k8s) handles deploying your services to your kubernetes cluster whenever they change
    
4. Embrace [Istio](https://istio.io) as your service mesh. Let Istio handle your services authentication, authorization, service discovery, retry, load balancing, and other concerns - you just focus on business logic! Istio provides a million other benefits as well, such as easy canary deployments, transparent upgrading of HTTP/1.1 traffic to gRPC protocol - allowing front-end clients to directly consume backend gRPC services(see [grpc-web](https://github.com/grpc/grpc-web).), tracing, etc.)
