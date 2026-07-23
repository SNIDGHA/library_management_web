# Library Management System 📚

A comprehensive, production-grade Library Management System built with Spring Boot, Spring Security, JWT stateless authentication, Spring Data JPA, and MySQL. It features role-based access control (RBAC) for Librarians and Students, input validations, structured global exception handling, and a clean, responsive single-page frontend.

---

## Features

### Authentication & Authorization
- **JWT Authentication**: Secure stateless authentication using JSON Web Tokens.
- **BCrypt Password Hashing**: Secure password storage using strong hashing.
- **Role-Based Access Control**:
  - **LIBRARIAN**: Can add, update, and delete books, view all users, and view all borrow records.
  - **STUDENT**: Can view books, borrow books, return books, and view their own borrow history.

### Core Business Logic
- **Book Catalog**: CRUD APIs to list, add, update, and delete books.
- **Borrow & Return Flow**: Students can borrow available books (automatically sets availability to false) and return them.
- **Fine Calculation**: Automated fine calculation upon book return (₹10/day for late returns past the 14-day threshold).

### Quality & Operational Standards
- **Global Exception Handling**: Returns clean, consistent error responses with appropriate HTTP status codes (e.g., 404 for Not Found, 400 for Validation failures).
- **Bean Validation**: Strict input validation using standard Jakarta annotations (`@NotBlank`, `@Email`, `@Size`, etc.).
- **SLF4J Logging**: Logging of business-critical operations like user registration, login, book additions, borrow transactions, and returns.
- **Swagger Integration**: Interactive API documentation exposed at `/swagger-ui/index.html`.

---

## Tech Stack

- **Backend**: Java 17, Spring Boot, Spring Security, Spring Data JPA, Hibernate, SLF4J, Lombok.
- **Database**: MySQL.
- **API Documentation**: SpringDoc OpenAPI.
- **Frontend**: HTML5, Vanilla JavaScript, Bootstrap 5.

---

## Folder Structure

```text
src/main/java/com/example/demo/
├── config/             # Spring configuration (Security configuration)
├── controller/         # REST API Controllers (endpoints)
├── dto/                # Data Transfer Objects (request/response validation schemas)
├── entity/             # JPA Database Entities
├── exception/          # Custom exceptions and GlobalExceptionHandler
├── repository/         # Spring Data JPA repositories
├── security/           # JWT security filters, Token Provider, UserDetailsService
├── service/            # Core business logic services
└── DemoApplication.java
```

---

## MySQL Configuration

Update your database credentials in `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/librarydb
spring.datasource.username=YOUR_MYSQL_USERNAME
spring.datasource.password=YOUR_MYSQL_PASSWORD
spring.jpa.hibernate.ddl-auto=update
```

*Note: Ensure you create a database named `librarydb` in your MySQL server before running the application.*

---

## How to Run

### Prerequisite
- Java 17 or higher
- Maven 3.x (or use the included wrapper)
- MySQL Server

### Commands

1. **Clone and Navigate**:
   ```bash
   cd demo
   ```

2. **Build the Application**:
   ```bash
   ./mvnw clean package
   ```
   *(Use `mvnw.cmd` on Windows)*

3. **Run the Application**:
   ```bash
   ./mvnw spring-boot:run
   ```

4. **Access the Portals**:
   - **Frontend Dashboard**: [http://localhost:8080/](http://localhost:8080/)
   - **Swagger OpenAPI Docs**: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

---

## API Documentation Summary

### Auth Endpoints
- `POST /users/register`: Register a new user (`STUDENT` or `LIBRARIAN`).
- `POST /users/login`: Authenticate email and password, returns JWT token.

### Book Endpoints
- `GET /books`: Get all books. (Authenticated)
- `GET /books/available`: Get all available books. (Authenticated)
- `POST /books`: Add a new book. (Librarian only)
- `PUT /books/{id}`: Update a book's details. (Librarian only)
- `DELETE /books/{id}`: Delete a book. (Librarian only)

### Borrow Endpoints
- `POST /borrow/{userId}/{bookId}`: Borrow a book. (Student only, restricted to own user ID)
- `POST /borrow/return/{borrowId}`: Return a book and calculate fines. (Student/Librarian)
- `GET /borrow/history/{userId}`: View borrow history of a specific student. (Student owner / Librarian)
- `GET /borrow/records`: View all borrow records. (Librarian only)

### User Endpoints
- `GET /users`: View all registered users. (Librarian only)

---

## Screenshots Placeholder

*Place screenshots here to showcase your dashboard view, admin panel, and Swagger docs!*

---

## Future Improvements
- **Book Reservation**: Allow students to put hold reservations on currently borrowed books.
- **Notification Service**: Automated email reminders for books approaching their due date.
- **Renewals**: Enable students to extend the borrow duration online.
- **Soft Deletion**: Keep records of books after removal from active circulation.
