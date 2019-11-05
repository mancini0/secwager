#!/bin/bash
bazel run //marketdata -- --norun
bazel run //orderentry  -- --norun
cd ~/secwager/ui/envoy-for-local-dev && docker build . -t envoy:v1
docker rm -f envoy &>/dev/null && echo 'Removed old envoy container, if it existed.'
docker rm -f marketdata &>/dev/null && echo 'Removed old marketdata container, if it existed'
docker rm -f orderentry &>/dev/null && echo 'Removed old orderentry container, if it existed'
docker run --rm -d --name envoy --net mynet -p 9901:9901 -p 10000:10000 envoy:v1
docker run --rm -d --name marketdata --net mynet -p 9300:9300 bazel/marketdata:marketdata 
docker run --rm -d --name orderentry --net mynet -p 9085:9085 bazel/orderentry:orderentry
docker ps