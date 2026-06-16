# Dockerfile for QualiTrack Platform
# Builds and runs the Spring Boot application using Java 26.

FROM eclipse-temurin:26-jdk AS build

WORKDIR /app

COPY .mvn .mvn
COPY mvnw .
COPY pom.xml .

RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline

COPY src ./src

RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:26-jre AS runtime

ENV SPRING_PROFILES_ACTIVE=prod

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]