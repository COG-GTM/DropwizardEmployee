FROM openjdk:8-jre-alpine

# Install curl for health checks
RUN apk add --no-cache curl

# Create app directory and non-root user
RUN addgroup -g 1001 -S appgroup && \
    adduser -u 1001 -S appuser -G appgroup

WORKDIR /app

# Copy application files
COPY target/DropwizardEmployee-1.0.5.jar app.jar
COPY docker-config.yml config.yml
COPY src/main/resources/migrations.xml migrations.xml

# Create directories for database and logs with proper permissions
RUN mkdir -p /app/data /app/logs && \
    chown -R appuser:appgroup /app

# Switch to non-root user
USER appuser

# Expose ports
EXPOSE 8080 8081

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8081/healthcheck || exit 1

# Default command runs migrations then starts server
CMD java -jar app.jar db migrate config.yml && \
    java -jar app.jar server config.yml
