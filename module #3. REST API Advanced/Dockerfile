FROM maven:latest AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml package spring-boot:repackage -DskipTests

FROM openjdk:11
COPY --from=build /home/app/target/gift-certificates-0.0.1-SNAPSHOT.jar /usr/local/lib/certificates.jar
ENTRYPOINT ["java", "-jar", "/usr/local/lib/certificates.jar"]