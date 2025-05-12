FROM maven:3.9.4-eclipse-temurin-17 AS build

WORKDIR /app

# Копируем файлы Maven-проекта
COPY pom.xml .
RUN mvn dependency:go-offline

# Копируем исходный код
COPY src ./src

# Собираем проект
RUN mvn package -DskipTests

# Финальный образ только с JDK, без Maven
FROM openjdk:17-jdk-slim
WORKDIR /app

# Копируем собранный jar
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
