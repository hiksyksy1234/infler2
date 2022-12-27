FROM openjdk:17-ea-11-jdk-slim
VOLUME /tmp
COPY target/discoveryservice-0.0.1-SNAPSHOT.jar Discoveryservice.jar
ENTRYPOINT ["java", "-jar", "Discoveryservice.jar"]