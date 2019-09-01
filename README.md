# secwager
Liquibook / gRPC / Kafka based, cloud native demo financial exchange

This repo demonstrates what I've found to be the most productive framework to develop cloud native microservices:

1. Keep all of your code in a monorepo. CI/CD becomes trivial in a monorepo when using Bazel as your build system - see this [talk](https://www.youtube.com/watch?v=DTvXa-iqrfA). 
2. Define your microservices as gRPC services (see /proto directory).
    - protobuf / gRPC enjoys a very concise syntax, compared to alternatives such as openapi.
    - gRPC services use HTTP/2  and are performant. Protobuf allows much faster serialization compared to json, and is typesafe.
    - gRPC services are [easy to test](https://github.com/mancini0/bazel-grpc-playground/blob/9651f7912f4d46da7c13bb96019aa1dfc52bbf3d/capitalization/src/test/java/com/example/capitalization/CapitalizationServiceTest.java#L34)
    - The proto compiler, protoc,  can transform your protobuf message definitions / gRPC service definitions into clients and libraries in several languages. Bazel will handle this for you!
3. Build your code using Bazel. Bazel offers you the following luxuries:
    - it provides fast, reproducible builds.
    - it builds only what needs to be rebuilt (modified code, or any code that is dependent on modified code)
    - it allows you develop different pieces of your codebase in different languages
    - it converts your protobuf messages and grpc definitions to library code in several languages
    - using [rules_docker](https://github.com/bazelbuild/rules_docker), Bazel can containerize your service by simply specifying a java_image target instead of java_binary target (or cc_image, go_image, etc). The developer need not specify a Dockerfile, or even have Docker installed locally! 
    - rules_docker also handles pushing your images to your container registry, and only rebuilds / repushes your image when the image has been affected by a code change.
    - [rules_k8s](https://github.com/bazelbuild/rules_k8s) handles deploying your services to your kubernetes cluster whenever they change
4. Favor Dagger 2 over Spring - the former is lightweight, boots up very quickly, and catches dependency injection errors at compile time, not run time like spring!
5. Embrace [Istio](https://istio.io) as your service mesh. Let Istio handle your services authentication, service discovery, retry, load balancing, and other concerns - you just focus on business logic!
    
