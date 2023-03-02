FROM openjdk:11
ADD /web/target/web-0.0.1-SNAPSHOT.jar certificates.jar
ENTRYPOINT ["java", "-jar", "certificates.jar"]