FROM openjdk:11
ADD /target/*.jar certificates.jar
ENTRYPOINT ["java", "-jar", "*.jar"]