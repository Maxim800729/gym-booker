# 1) Сборка JAR
FROM maven:3.9.9-eclipse-temurin-21 AS build

WORKDIR /app

# сначала pom, чтобы кэшировались зависимости
COPY pom.xml .
RUN mvn -q -DskipTests dependency:go-offline

# потом исходники
COPY src ./src

# собираем
RUN mvn -q -DskipTests package

# 2) Лёгкий рантайм
FROM eclipse-temurin:21-jre

WORKDIR /app

# !!! проверь имя JAR в target и поправь путь, если отличается
COPY --from=build /app/target/gym-booker-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
