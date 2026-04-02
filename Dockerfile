# Étape 1 : Compilation (On utilise Maven + Java 21 car c'est stable)
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -Dmaven.test.skip=true

# Étape 2 : Exécution (Image légère)
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
# On récupère le JAR généré à l'étape précédente
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]