# ===== Stage 1: Build the application =====
FROM maven:3.9.6-eclipse-temurin-21 AS builder
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# ===== Stage 2: Runtime container =====
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copy jar from the builder stage
COPY --from=builder /app/target/taskservice-0.0.1-SNAPSHOT.jar app.jar

# Create mount point for tasks
VOLUME ["/app/Tasks"]

# Expose container port
EXPOSE 8080

# Run the application with profile (default to 'prod')
ENV SPRING_PROFILES_ACTIVE=prod
ENTRYPOINT ["java", "-jar", "app.jar"]
