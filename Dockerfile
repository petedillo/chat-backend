# Stage 1: Build the Maven project
FROM maven:3.9.9-amazoncorretto-23-debian-bookworm AS build-stage
WORKDIR /app

COPY pom.xml .
COPY src/main/java/ /app/src/main/java/
COPY src/main/resources /app/src/main/resources

# Cache dependencies and build the project
RUN mvn dependency:go-offline
RUN mvn package -DskipTests

# Stage 2: Debug stage (for development/debugging)
FROM openjdk:24-ea-22-jdk-slim-bullseye AS debug-stage
WORKDIR /app

# Copy only the final jar file from the build stage
COPY --from=build-stage /app/target/*.jar app.jar

# Expose the application and debug ports
EXPOSE 8080 5005

# Enable remote debugging with JDWP
CMD ["java", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=:5005", "-jar", "app.jar"]

# Stage 3: Create the final production image
FROM openjdk:24-ea-22-jdk-slim-bullseye AS run-stage
WORKDIR /app

# Copy only the final jar file from the build stage
COPY --from=build-stage /app/target/*.jar app.jar

# Expose the application port
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "app.jar"]