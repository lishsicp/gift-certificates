FROM maven:3.8.5-openjdk-17-slim AS build

# oauth2 server
COPY oauth2-server/src /home/app/auth/src
COPY oauth2-server/pom.xml /home/app/auth
RUN mvn -f /home/app/auth/pom.xml package spring-boot:repackage -DskipTests

FROM eclipse-temurin:17-jre-alpine AS auth
COPY --from=build /home/app/auth/target/oauth2-server-0.0.1-SNAPSHOT.jar /usr/local/lib/auth-server.jar
ENTRYPOINT ["java", "-jar", "/usr/local/lib/auth-server.jar"]