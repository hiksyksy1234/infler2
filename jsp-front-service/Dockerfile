FROM openjdk:17-ea-11-jdk-slim
VOLUME /tmp
COPY target/jsp-front-service-0.0.1-SNAPSHOT.war JspService.war
ENTRYPOINT ["java", "-jar", "JspService.war"]