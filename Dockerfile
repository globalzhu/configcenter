FROM $REGISTRY_URL/cosmos/ci:openjdk-18

RUN mkdir /data
WORKDIR /data

COPY ./configcenter-assemble/target/configcenter-exec.jar /data/configcenter-exec.jar

CMD ["/bin/sh", "-c", "java $JAVA_OPTS -jar configcenter-exec.jar"]
