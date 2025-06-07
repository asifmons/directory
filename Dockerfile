# Dockerfile
FROM openjdk:17-jdk-slim
COPY target/*.jar app.jar
ENTRYPOINT ["java", "-XX:+UseZGC", "-Xmx512m", "-Xms512m", "-Xlog:gc", "-jar", "app.jar"]

