# Docker Containerization Guide for DropwizardEmployee

This guide explains how to build, run, and deploy the DropwizardEmployee service using Docker containers.

## Overview

The DropwizardEmployee service has been containerized using a multi-stage Docker approach:
- **Build stage**: Uses OpenJDK 8 + Maven to compile the application
- **Runtime stage**: Uses OpenJDK 8 JRE for minimal runtime footprint
- **Database**: H2 database with persistent volume mounting
- **Security**: Runs as non-root user
- **Health checks**: Built-in health monitoring

## Quick Start

### Using Docker Compose (Recommended)

1. **Start the service with all dependencies:**
   ```bash
   docker-compose up -d
   ```

2. **View logs:**
   ```bash
   docker-compose logs -f dropwizard-employee
   ```

3. **Stop the service:**
   ```bash
   docker-compose down
   ```

### Using Docker directly

1. **Build the image:**
   ```bash
   docker build -t dropwizard-employee .
   ```

2. **Run the container:**
   ```bash
   docker run -d \
     --name dropwizard-employee \
     -p 8080:8080 \
     -p 8081:8081 \
     -v employee_data:/app/data \
     -v employee_logs:/app/logs \
     -e DW_DEFAULT_NAME="Docker User" \
     dropwizard-employee
   ```

## Testing the Service

### REST API Endpoints

1. **Create an employee:**
   ```bash
   curl -X POST \
     -H "Content-Type: application/json" \
     -d '{"firstName":"John","lastName":"Doe","jobTitle":"Software Engineer"}' \
     http://localhost:8080/employee
   ```

2. **List all employees:**
   ```bash
   curl http://localhost:8080/employee
   ```

### Admin Interface

1. **Health check:**
   ```bash
   curl http://localhost:8081/healthcheck
   ```

2. **Metrics:**
   ```bash
   curl http://localhost:8081/metrics
   ```

3. **Admin dashboard:**
   Open http://localhost:8081 in your browser

## Configuration

### Environment Variables

The service supports the following environment variables:

| Variable | Description | Default |
|----------|-------------|---------|
| `DW_DEFAULT_NAME` | Default name for template rendering | `Stranger` |
| `GRAPHITE_HOST` | Graphite server hostname for metrics | `localhost` |

### Volume Mounts

| Path | Purpose | Description |
|------|---------|-------------|
| `/app/data` | Database storage | H2 database files for persistence |
| `/app/logs` | Application logs | Log files with rotation |

### Port Mapping

| Container Port | Purpose | Description |
|----------------|---------|-------------|
| `8080` | Application | REST API endpoints |
| `8081` | Admin | Health checks, metrics, admin interface |

## Database Persistence

The H2 database is configured to persist data in `/app/data/example.mv.db`. This directory should be mounted as a volume to ensure data survives container restarts.

**Important**: The database migration runs automatically on container startup, so the first startup may take a few extra seconds.

## Logging

Application logs are written to `/app/logs/application.log` with automatic rotation:
- Maximum file size: 10MB
- Archived files: 7 days retention
- Pattern: `/app/logs/application-YYYY-MM-DD-N.log.gz`

## Health Monitoring

The container includes built-in health checks:
- **Endpoint**: `http://localhost:8081/healthcheck`
- **Interval**: 30 seconds
- **Timeout**: 10 seconds
- **Start period**: 60 seconds (allows for startup time)
- **Retries**: 3 attempts before marking unhealthy

## Metrics Collection

The service is configured to send metrics to Graphite:
- **Default host**: `localhost:2003`
- **Configurable via**: `GRAPHITE_HOST` environment variable
- **Prefix**: `example`
- **Frequency**: 1 minute

When using Docker Compose, a Graphite service is included for local development.

## Security Considerations

### Container Security
- Runs as non-root user (`dropwizard:dropwizard`)
- Uses minimal Alpine Linux base image
- No unnecessary packages installed

### SSL/HTTPS
The service includes SSL configuration but it's commented out by default. To enable HTTPS:

1. Uncomment the HTTPS connector in `example.yml`
2. Ensure the `example.keystore` file is properly configured
3. Update port mappings to include 8443

### Production Recommendations
- Use external database (PostgreSQL/MySQL) instead of embedded H2
- Set up proper log aggregation (ELK stack, Fluentd, etc.)
- Use external metrics collection (Prometheus, DataDog, etc.)
- Implement proper secrets management
- Use reverse proxy for SSL termination

## Troubleshooting

### Common Issues

1. **Container fails to start:**
   - Check logs: `docker logs dropwizard-employee`
   - Verify port availability: `netstat -tulpn | grep :8080`

2. **Database migration fails:**
   - Ensure `/app/data` volume has proper permissions
   - Check for existing database locks

3. **Health check fails:**
   - Verify admin port (8081) is accessible
   - Check if application finished starting (may take 30-60 seconds)

4. **API not responding:**
   - Confirm port mapping: `docker port dropwizard-employee`
   - Test admin interface first: `curl http://localhost:8081/ping`

### Debug Mode

To run with debug logging:
```bash
docker run -it --rm \
  -p 8080:8080 -p 8081:8081 \
  -v employee_data:/app/data \
  dropwizard-employee
```

### Accessing Container Shell

```bash
docker exec -it dropwizard-employee sh
```

## Development Workflow

### Local Development with Docker

1. **Make code changes**
2. **Rebuild image:**
   ```bash
   docker-compose build
   ```
3. **Restart service:**
   ```bash
   docker-compose up -d
   ```

### Testing Changes

```bash
# Run tests
mvn test

# Build and test container
docker build -t dropwizard-employee-test .
docker run --rm -p 8080:8080 -p 8081:8081 dropwizard-employee-test
```

## Production Deployment

### Docker Swarm

```yaml
version: '3.8'
services:
  dropwizard-employee:
    image: dropwizard-employee:latest
    deploy:
      replicas: 3
      restart_policy:
        condition: on-failure
    ports:
      - "8080:8080"
      - "8081:8081"
    volumes:
      - employee_data:/app/data
    environment:
      - DW_DEFAULT_NAME=Production
      - GRAPHITE_HOST=metrics.company.com
```

### Kubernetes

See the Kubernetes deployment examples in the `k8s/` directory (if available).

### Environment-Specific Configuration

Consider using different configuration files for different environments:
- `example-dev.yml` for development
- `example-staging.yml` for staging  
- `example-prod.yml` for production

## Support

For issues related to containerization, please check:
1. This documentation
2. Container logs: `docker logs dropwizard-employee`
3. Application health: `curl http://localhost:8081/healthcheck`
4. GitHub issues in the repository

For application-specific issues, refer to the main README.md file.
