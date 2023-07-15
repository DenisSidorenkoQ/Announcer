FROM maven:3.6.0-jdk-11-slim AS builder
WORKDIR /app
COPY src /app/src
COPY pom.xml /app
RUN mvn -f /app/pom.xml clean package

FROM adoptopenjdk/openjdk11
WORKDIR /app
COPY --from=builder /app/target/mail-service-1.0-SNAPSHOT.jar /usr/local/lib/mail-service.jar
EXPOSE 8081
ENTRYPOINT ["java","-jar","/usr/local/lib/mail-service.jar"]