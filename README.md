# NBP-Tim6 Travel Planner API

A comprehensive REST API for planning and managing travel trips with accommodations, activities, bookings, and integrated Stripe payments.

## Quick Start

### Prerequisites
- Java 17+
- Maven 3.6+
- Oracle Database

### Build
```bash
mvn clean install -DskipTests
```

### Run
```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## Features
- User authentication with JWT tokens
- Trip planning and management
- Accommodations and bookings
- Activities and reviews
- Stripe payment integration
- Complete REST API with 50+ endpoints

## Documentation

### Interactive API Documentation
While the application is running, open:
- **Swagger UI**: `http://localhost:8080/swagger-ui.html`
- **OpenAPI JSON**: `http://localhost:8080/v3/api-docs`
- **OpenAPI YAML**: `http://localhost:8080/v3/api-docs.yaml`

### Offline Documentation
Open `NBP-Tim6-API-Documentation.html` in any web browser (no internet required)

## Project Structure
```
src/main/java/ba/unsa/etf/nbp_tim6/backend
├── controller/     # REST endpoints
├── service/        # Business logic
├── repository/     # Database access
├── model/          # Entity classes
├── dto/            # Data transfer objects
└── config/         # Configuration (Security, Swagger, etc)
```

## Technologies
- Spring Boot 3.5.12
- Spring Security with JWT
- Flyway Database Migrations
- SpringDoc OpenAPI 2.5.0
- Stripe API
- SendGrid Email

## Database
Oracle Database migrations are in `src/main/resources/db/migration/`
