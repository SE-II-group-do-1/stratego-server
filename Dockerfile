# JAVA runtime as base image
FROM openjdk:17-jdk-alpine

WORKDIR /app

COPY build/libs/stratego-server-1.0-SNAPSHOT.jar /app/stratego-server-1.0-SNAPSHOT.jar

CMD ["java", "-jar", "/app/stratego-server-1.0-SNAPSHOT.jar"]

