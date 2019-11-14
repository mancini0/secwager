import React from 'react';

const About = (props) => (
    <React.Fragment>
        <p>This site aims to securitize sports wagers as binary options. It was made as a project to explore
        modern cloud-native DevOps practices. <b>All services <u>and infrastructure</u> backing this application run on <a href='https://kubernetes.io/'>Kubernetes.</a></b>The service mesh layer is provided by <a href='http://istio.io'><b>Istio</b></a>,
                                                                                                                                            which handles concerns such as mTLS, routing, service discovery, tracing, metrics collection, rate-limiting, grpc-web proxying, etc, allowing me to focus on business logic
    without worrying about these tedious concerns in my application code.</p>
        <p>The application makes heavy use of various Kubernetes Operators to simplify the deployment and management of various components. In particular,
            I use the following operators:
        <ul>
                <li>the <a href='http://istio.io/docs/setup/install/operator/'><b>Istio Operator</b></a> easily installs the service mesh</li>
                <li>assorted <a href='http://rook.io'><b>Rook Operators</b></a> spin up a replicated <a href='https://ceph.io/'><b>Ceph</b></a> storage cluster and a highly available <a href='https://www.cockroachlabs.com/'><b>Cockroach DB</b></a> cluster. Rook <b><i>dramatically</i></b> simplifies persistance orchestration and management in a Kubernetes environment!</li>
                <li>the <a href='http://strimzi.io'><b>Strimzi Operator</b></a> installs a Kafka and Zookeeper cluster onto the Kubernetes cluster. It adds k8s CRDs that define Kafka entities such as topics, users, etc. </li>
            </ul>
        </p>
        <p>The application is backed by various <a href='https://grpc.io/'><b>gRPC</b></a> microservices. Kafka is used as a communication bus
        between the services. Where necessary, (for example during order entry), Kafka's transactional API is used to ensure exactly-once delivery. To stream data directly to the UI (for example market data),
        <a href='https://github.com/grpc/grpc-web'><b> gRPC-web</b></a> is used. Conveninetly, Istio handles the conversion of HTTP/1.1 requests from the browser
                                                                                                        into HTTP/2 requests expected by gRPC.
        </p>
        <p>All backing code is kept in a monorepo and built by <a href="http://bazel.build"><b>Bazel</b></a>. The backing services are written in
        various languages (Java, Kotlin, c++). When a piece of the codebase changes, Bazel rebuilds all of the code which
        depends on the changes (even if theses dependent pieces differ in language), allowing for extremely fast incremental builds. In addition,
        various Bazel rules also handle the following aspects:
            <ul>
                <li><a href='https://github.com/bazelbuild/rules_docker'><b>rules_docker</b></a> handle containerizing my Java/c++/whatever services (without a Dockerfile!).
                These rules also allow me to push the containers to a container registry.</li>
                <li><a href='https://github.com/bazelbuild/rules_proto'><b>rules_proto</b></a> provides support for compiling protobuf definitions into various languages</li>
                <li><a href='https://github.com/bazelbuild/rules_foreign_cc'><b>rules_foreign_cc</b></a> allows me to easily pull in non-Bazel built external c++ dependencies (for example, librdkafka)</li>
            </ul>
        </p>
    </React.Fragment>);

export default About;