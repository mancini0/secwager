import React from 'react';

const About = (props) => (
    <React.Fragment>
        <p>This site aims to securitize sports wagers as binary options. It was made as a project to explore
        modern cloud-native DevOps practices. All services <b>and infrastructure</b> backing this application run on Kubernetes. Istio provides the service mesh,
                                                            handling concerns such as mTLS, routing, service discovery, rate-limiting, grpc-web proxying, etc, allowing me to focus on business logic
    without worrying about these tedious concerns in my application code.</p>
        <p>The application makes heavy use of various Kubernetes Operators to simplify the deployment and management of various components. In particular,
            I use the following operators:
        <ul>
                <li>the <a href='http://istio.io/docs/setup/install/operator/'>Istio Operator</a> easily installs the service mesh</li>
                <li>the <a href='http://rook.io'>Rook Operator</a> spins up a replicated Ceph storage cluster and a highly available Cockroach DB cluster. Rook dramatically simplifies persistance orchestration and management in a Kubernetes environment!</li>
                <li>the <a href='http://strimzi.io'>Strimzi Operator</a> installs a Kafka and Zookeeper cluster onto the Kubernetes cluster. It adds k8s CRDs that define Kafka entities such as topics, users, etc. </li>
            </ul>
        </p>
        <p>The application is backed by various gRPC microservices. Kafka is used as a communication bus
        between the services. Where necessary, (for example during order entry), Kafka's transactional API is used to ensure exactly-once delivery. To stream data directly to the UI (for example market data),
        gRPC-web is used. Conveninetly, Istio handles the conversion of HTTP/1.1 requests from the browser
        into HTTP/2 requests expected by gRPC.
        </p>
        <p>All backing code is kept in a monorepo and built by <a href="http://bazel.build">Bazel</a>. The backing services are written in
        various languages (Java, Kotlin, C++). When a piece of the codebase changes, Bazel rebuilds all of the code which
        depends on the changes (even if theses dependent pieces differ in language), allowing for extremely fast incremental builds. In addition,
        various Bazel rules also handle the following aspects:
            <ul>
                <li><a href='https://github.com/bazelbuild/rules_docker'>rules_docker</a> handle containerizing my Java / C++ / whatever services (without a Dockerfile!).
                These rules also allow me to push the containers to a container registry.</li>
                <li><a href='https://github.com/bazelbuild/rules_proto'>rules_proto</a> provides support for compiling protobuf definitions into various languages</li>
                <li><a href='https://github.com/bazelbuild/rules_foreign_cc'>rules_foreign_cc</a> allows me to easily pull in non-Bazel built external c++ dependencies (for example, librdkafka)</li>
            </ul>
        </p>
    </React.Fragment>);

export default About;