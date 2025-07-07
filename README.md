# Auth Service - README

This microservice handles user authentication and JWT token generation for the Hotel Reservation System.

## Features

- User registration
- User login
- Password hashing using BCrypt
- JWT token generation and validation
- Role-based access control
- Integration with Spring Security
- PostgreSQL + Flyway for schema migration
- Integration and unit tests with Testcontainers

## Technologies Used

- Java 17
- Spring Boot 3
- Spring Security
- Spring Data JPA
- PostgreSQL
- Flyway
- JWT (jjwt)
- Testcontainers
- JUnit 5
- Mockito

## Running the Service

### Prerequisites

- Docker & Docker Compose
- Java 17+
- Maven

### Start with Docker Compose

```
docker-compose up --build auth-service
```

### Local Development

You can run the service locally with:

```
./mvnw spring-boot:run
```

## API Endpoints

### Register User

**POST** `/api/v1/auth/register`

Request Body:

```
{
  "email": "user@example.com",
  "password": "securePassword"
}
```

Response:

```
{
  "token": "JWT_TOKEN"
}
```

### Login

**POST** `/api/v1/auth/login`

Request Body:

```
{
  "email": "user@example.com",
  "password": "securePassword"
}
```

Response:

```
{
  "token": "JWT_TOKEN"
}
```

## Running Tests

### Unit Tests

```
./mvnw test
```

### Integration Tests

Integration tests use Testcontainers. Ensure Docker is running.

```
./mvnw verify -Dspring.profiles.active=test
```

## Notes

- Tokens are signed using a shared secret defined in `application.yml` as `jwt.secret`.
- The role is embedded in the token as a claim.
- Passwords are hashed using BCrypt and never stored as plaintext.

---
