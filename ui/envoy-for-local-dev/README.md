When running the UI locally, the envoy proxy must be running as well, as it handles the gRPC-web proxying. (And the http/1.1 to http/2 conversion) (In a k8s cluster, istio handles this for us (using envoy as well)

run the following commands from this directory:

docker network create mynet
docker build . -t envoy:v1
docker run --rm -d --name envoy --net mynet -p 9901:9901 -p 10000:10000 envoy:v1
bazel build //marketdata 
docker run --rm -d --name marketdata --net mynet -p 9300:9300 bazel/marketdata:marketdata 