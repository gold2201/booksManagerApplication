# Этап сборки
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Этап запуска
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# ОБЯЗАТЕЛЬНО указываем порт и слушаем на всех интерфейсах
ENV SERVER_PORT=8080
ENV JAVA_OPTS="-Dserver.port=8080 -Dserver.address=0.0.0.0"

EXPOSE 8080
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
