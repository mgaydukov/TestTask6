FROM openjdk:17-jdk-slim
WORKDIR /app

COPY target/moysklad-0.0.1.jar moysklad-0.0.1.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "moysklad-0.0.1.jar"]
