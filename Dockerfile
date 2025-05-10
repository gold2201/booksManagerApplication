# Dockerfile
FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
COPY target/bookmanagmentapplication-0.0.2.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]