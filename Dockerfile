# Use OpenJDK as base image
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Copy and build the application
COPY target/*.jar app.jar

# Expose the port
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "app.jar"]
