# Use an official OpenJDK runtime as a parent image
FROM openjdk:19-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the built JAR file from the target directory into the container
# Make sure to build the application first using `mvn clean package`
COPY target/gabriel_tour_app_backend-1.0-SNAPSHOT.jar /app/app.jar

# Expose the port on which the Spring Boot app will run (default: 8080)
EXPOSE 9090

# Set the command to run the JAR file
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
