FROM openjdk:17-ea-11-jdk-slim
VOLUME /tmp
COPY target/comment-service-0.0.1-SNAPSHOT.jar CommentService.jar
ENTRYPOINT ["java", "-jar", "CommentService.jar"]