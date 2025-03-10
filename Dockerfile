# Stage 1: Dependency Resolution (Caching)
FROM maven:3.9.9-amazoncorretto-17 AS dependency
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Stage 2: Build the Application
FROM maven:3.9.9-amazoncorretto-17 AS builder
WORKDIR /app
COPY --from=dependency /root/.m2 /root/.m2
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests -B

# Stage 3: Runtime Image
FROM openjdk:17-jdk-slim AS runtime
WORKDIR /app
COPY --from=builder /app/target/community-service-management-2-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
RUN useradd -m springuser && chown springuser:springuser /app
USER springuser
ENTRYPOINT ["java", "-jar", "app.jar"]