FROM openjdk:17-jdk
WORKDIR /app
COPY target/data-processing-service-0.0.1-SNAPSHOT.jar /app/data-processing-service.jar
ENTRYPOINT ["java", "-jar", "data-processing-service.jar"]
