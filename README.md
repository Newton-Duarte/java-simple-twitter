# Simple Twitter REST API

A lightweight Twitter-like REST API built with Java 21 and Spring Boot that provides core Twitter functionality including authentication, user management, and tweet operations.

## Overview

Simple Twitter is a REST API that demonstrates the fundamental features of a social media platform like Twitter. It implements secure authentication, role-based authorization, user management, and basic tweet functionality.

## Features

- **Authentication**: Complete sign-in and sign-up functionality
- **Authorization**: Role-based access control with ADMIN and USER roles
- **User Management**: Administrative endpoint to retrieve user information
- **Tweet Operations**: Create tweets and view personalized tweet feeds

## Technology Stack

- **Java 21**: Latest Java version with modern language features
- **Spring Boot**: Application framework for simplified configuration
- **Spring Security**: Authentication and authorization framework
- **PostgreSQL**: Robust relational database
- **Maven**: Dependency management and build automation

## API Endpoints

### Authentication

- `POST /auth/sign-up` - Register a new user
- `POST /auth/sign-in` - Authenticate an existing user

### User Management (Admin only)

- `GET /users` - Get a list of all users (requires ADMIN role)

### Tweet Operations

- `POST /tweets` - Create a new tweet
- `GET /tweets/feed` - Get personalized tweet feed

## Getting Started

### Prerequisites

- Java 21 JDK
- PostgreSQL
- Maven

### Installation

1. Clone the repository:
   ```
   git clone https://github.com/newton-duarte/java-simple-twitter.git
   cd java-simple-twitter
   ```

2. Configure database connection in `application.properties`:
   ```
   # Database Connection
   spring.datasource.url=jdbc:postgresql://localhost:5432/simpletwitterapidb
   spring.datasource.username=docker
   spring.datasource.password=docker
   spring.datasource.driver-class-name=org.postgresql.Driver
   
   # Enables the data.sql for DATABASE
   spring.sql.init.mode=always
   
   # JPA Configuration
   spring.jpa.hibernate.ddl-auto=update
   spring.jpa.show-sql=true
   spring.jpa.properties.hibernate.format_sql=true
   spring.jpa.properties.hibernate.check_nullability=true
   spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
   
   # Jwt Config
   jwt.public.key=classpath:app.pub
   jwt.private.key=classpath:app.key
   ```

3. Build the project:
   ```
   mvn clean install
   ```

4. Run the application:
   ```
   mvn spring-boot:run
   ```

The API will be available at `http://localhost:8080`

## Security Implementation

This project implements a JWT-based authentication system:

1. Users register with name and password
2. Upon successful authentication, a JWT token is issued
3. The token must be included in the Authorization header for protected endpoints
4. Endpoints are secured based on user roles (ADMIN or USER)

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── simpletwitter/
│   │           ├── config/
│   │           │   └── SecurityConfig.java
│   │           ├── controller/
│   │           │   ├── AuthController.java
│   │           │   ├── TweetController.java
│   │           │   └── UserController.java
│   │           ├── model/
│   │           │   ├── Role.java
│   │           │   ├── Tweet.java
│   │           │   └── User.java
│   │           ├── repository/
│   │           ├── security/
│   │           ├── service/
│   │           └── SimpleTwitterApplication.java
│   └── resources/
│       └── application.properties
└── test/
```

## Future Enhancements

- Follow/Unfollow functionality
- Like and comment on tweets
- Profile customization

## Contributing

1. Fork the project
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

---

Made with ❤️ by Newton Duarte