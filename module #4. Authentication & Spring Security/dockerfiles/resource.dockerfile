FROM maven:3.8.5-openjdk-17-slim AS build

# gift certificate resource server
COPY oauth2-resource-server/src /home/app/resource/src
COPY oauth2-resource-server/pom.xml /home/app/resource/pom.xml
RUN mvn -f /home/app/resource/pom.xml package spring-boot:repackage -DskipTests

COPY ../start.sh ../start.sh

FROM eclipse-temurin:17-jre-alpine AS resource
COPY --from=build ../start.sh /usr/local/lib/start.sh
COPY --from=build /home/app/resource/target/gift-certificates-oauth2-resource-server-0.0.1-SNAPSHOT.jar /usr/local/lib/resource/certificates-resource-server.jar
ENTRYPOINT ["sh", "/usr/local/lib/start.sh"]