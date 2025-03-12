# Use official Maven image to build the application
FROM maven:3.8.7-openjdk-17 AS build
WORKDIR /app

# Copy the source code and build the project
COPY . .
RUN mvn clean package -DskipTests

# Use lightweight OpenJDK to run the application
FROM openjdk:17-jdk-slim
WORKDIR /app

# Copy the JAR from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose port
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "app.jar"]
