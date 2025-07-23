# Device API

This project implements a RESTful API for managing devices.


## 🧱 Architecture

The project is structured following a layered architecture:

```
device-api/
├── controller        # REST endpoints
├── dto              # Data transfer objects
├── entity           # JPA entities and enums
├── exception        # Custom exceptions and handlers
├── mapper           # MapStruct interfaces
├── repository       # Spring Data JPA repositories
├── service          # Business logic (interface + impl)
├── config           # OpenAPI configuration
└── resources         # application.yml and configurations
```

## 🛠️ Technologies

- Java 21
- Spring Boot 3
- Spring Data JPA
- PostgreSQL
- MapStruct
- Lombok
- Springdoc OpenAPI 3
- Problem Spring Web
- JUnit 5 + Mockito
- Testcontainers

## 🧪 Testing

- 100% unit test coverage for services using JUnit + Mockito
- Integration tests using Testcontainers for PostgreSQL
- Tests for both success and edge cases

## 🐳 Build App and Docker Setup

Start the app and database with:
```bash
./mvnw clean package or
mvnw.cmd clean package
```
```bash
docker-compose up --build
```

Access Swagger UI at: [http://localhost:8080/swagger-ui/index.html]

🚧 Future Improvements
Add pagination and filtering support to the GET /api/devices endpoint.

Improve validation messages with localized responses.

Implement role-based access control (RBAC) using Spring Security.

Add caching for read-heavy endpoints (e.g., GET by brand/state).