# Use Maven to build the JAR before running the application
FROM maven:3.8.6-openjdk-17 AS build
WORKDIR /app

# Copy source code and build
COPY . .
RUN mvn clean package -DskipTests

# Use OpenJDK for running the application
FROM openjdk:17-jdk-slim
WORKDIR /app

# Copy built JAR from the build stage
COPY --from=build /app/target/broadcast_server-0.0.1-SNAPSHOT.jar app.jar

# Expose the port
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "app.jar"]
