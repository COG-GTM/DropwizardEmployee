# Testing DropwizardEmployee

## Build
```bash
mvn clean package
```

## Run Migrations
```bash
java -jar target/DropwizardEmployee-1.0.5.jar db migrate example.yml
```

## Start Server
```bash
java -jar target/DropwizardEmployee-1.0.5.jar server example.yml
```
- App port: 8080
- Admin port: 8081

## Test Endpoints

### Create employee
```bash
curl -s -H "Content-Type: application/json" -X POST \
  -d '{"firstName":"Test","lastName":"User","jobTitle":"Engineer"}' \
  http://localhost:8080/employee
```
Expect: HTTP 200, JSON with `id`, `firstName`, `lastName`, `jobTitle`

### List employees
```bash
curl -s http://localhost:8080/employee
```
Expect: HTTP 200, JSON array of employees

### Health check
```bash
curl -s http://localhost:8081/healthcheck
```
Expect: HTTP 200, `hibernate` and `deadlocks` both `healthy: true`

## Stack
- Dropwizard 2.1.12 (Jetty + Jersey + Jackson + Hibernate)
- H2 embedded database
- Liquibase for schema migrations
- Config: `example.yml`

## Auth
No authentication required. H2 database uses `sa`/`sa` credentials configured in `example.yml`.
