FROM maven:3.9.9-eclipse-temurin-21 AS build

WORKDIR /workspace

COPY pom.xml .
COPY src ./src

RUN mvn -B clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine AS runtime

RUN addgroup -S -g 10001 mecanica \
    && adduser -S -D -H -u 10001 -G mecanica mecanica

WORKDIR /app

COPY --from=build --chown=10001:10001 /workspace/target/*.jar app.jar

USER 10001:10001

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
