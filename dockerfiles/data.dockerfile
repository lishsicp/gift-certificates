FROM maven:3.8.5-openjdk-17-slim AS build

COPY data-generator/src /home/app/data/src
COPY data-generator/pom.xml /home/app/data/pom.xml
RUN mvn -f /home/app/data/pom.xml package spring-boot:repackage -DskipTests

FROM eclipse-temurin:17-jre-alpine AS data
COPY --from=build /home/app/data/target/data-generator-0.0.1-SNAPSHOT.jar /usr/local/lib/data/data.jar
ENTRYPOINT ["java", "-jar", "/usr/local/lib/data/data.jar"]