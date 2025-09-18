# Multi-stage Dockerfile for DropwizardEmployee service
# Build stage: Maven + OpenJDK 8 for compilation
FROM openjdk:8-jdk-alpine AS build

# Install Maven
RUN apk add --no-cache maven

# Set working directory
WORKDIR /app

# Copy Maven configuration files
COPY pom.xml .
COPY dependency-reduced-pom.xml* ./

# Download dependencies (for better layer caching)
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Runtime stage: OpenJDK 8 JRE for execution
FROM openjdk:8-jre-alpine AS runtime

# Install curl for health checks
RUN apk add --no-cache curl

# Create non-root user for security
RUN addgroup -g 1001 dropwizard && \
    adduser -D -s /bin/sh -u 1001 -G dropwizard dropwizard

# Set working directory
WORKDIR /app

# Create directories for data and logs with proper permissions
RUN mkdir -p /app/data /app/logs && \
    chown -R dropwizard:dropwizard /app

# Copy JAR file from build stage
COPY --from=build /app/target/DropwizardEmployee-1.0.5.jar ./DropwizardEmployee-1.0.5.jar

# Copy configuration files
COPY example.yml ./example.yml
COPY example.keystore ./example.keystore

# Copy scripts
COPY docker-entrypoint.sh ./docker-entrypoint.sh
COPY healthcheck.sh ./healthcheck.sh

# Make scripts executable and set ownership
RUN chmod +x docker-entrypoint.sh healthcheck.sh && \
    chown -R dropwizard:dropwizard /app

# Switch to non-root user
USER dropwizard

# Expose application and admin ports
EXPOSE 8080 8081

# Configure health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD ./healthcheck.sh

# Set entrypoint
ENTRYPOINT ["./docker-entrypoint.sh"]
