# Get latest java
FROM eclipse-temurin:17-jdk-jammy AS build

# Set the working directory
WORKDIR /app

# Copy the build.gradle and settings.gradle files
COPY build.gradle .
COPY settings.gradle .
COPY gradlew .
COPY gradle ./gradle

# Copy the src directory
COPY src ./src

# Build the application without running the tests and with stacktrace
RUN ./gradlew clean build -x test --stacktrace

# Expose port 8080
EXPOSE 8080

# Run the application
ENTRYPOINT ["java","-jar","/app/build/libs/java-springdata-quickstart-0.0.1-SNAPSHOT.jar"]

# docker build -t java-springdata-quickstart . 
# docker run -d --name springdata-container -p 9440:8080 java-springdata-quickstart