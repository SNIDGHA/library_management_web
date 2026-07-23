# 📚 Library Management System

A full-stack Library Management System built using **Java, Spring Boot, Spring Security, JWT, MySQL, and Vanilla JavaScript**. The application provides secure role-based access for **Students** and **Librarians**, making it easy to manage books, borrowing, returns, fines, and email notifications.

This project was built to simulate a real-world library workflow with authentication, authorization, and automated business logic.

---

# 🚀 Features

## 🔐 Secure Authentication

- JWT-based authentication
- BCrypt password encryption
- Role-Based Access Control (RBAC)

### Roles

**👨‍🎓 Student**
- Register and Login
- View available books
- Borrow books
- Return books
- View personal borrow history

**👩‍💼 Librarian**
- Add books
- Update book details
- Delete books
- View all registered users
- View all borrow records

---

# 📖 Book Management

- Add new books
- Update existing books
- Delete books
- View complete catalogue
- View only available books

Book availability is automatically updated whenever a book is borrowed or returned.

---

# 📚 Borrow & Return System

Students can borrow available books with a single API call.

When a book is borrowed:

- Borrow date is recorded
- Due date is automatically set to 14 days later
- Book availability changes to **Unavailable**

When a book is returned:

- Return date is stored
- Book becomes available again
- Fine is calculated automatically if returned after the due date

Fine Policy:

```
₹10 per day after the due date
```

---

# 📧 Email Notifications

The system automatically sends emails to students.

### ✅ Borrow Receipt

As soon as a book is borrowed, the student receives an email containing:

- Student Name
- Book Title
- Borrow Date
- Due Date
- Library instructions

---

### ⏰ Due Date Reminder

Students receive reminder emails before the due date so they don't forget to return the book on time.

---

### 💰 Late Return Notification

If a book is returned after the due date, an email is sent containing:

- Book Details
- Borrow Date
- Return Date
- Due Date
- Number of Late Days
- Fine Amount

This works like a digital receipt for every transaction.

---

# 🛡 Security

- Spring Security
- JWT Authentication
- Stateless Sessions
- Password Encryption using BCrypt
- Protected REST APIs
- Role-Based Authorization

---

# ✅ Input Validation

The application validates incoming requests using Jakarta Bean Validation.

Examples include:

- Valid email addresses
- Required fields
- Password length validation
- Empty value checks

---

# ⚠ Global Exception Handling

A centralized exception handler returns meaningful error messages with proper HTTP status codes.

Examples:

- 400 Bad Request
- 401 Unauthorized
- 403 Forbidden
- 404 Not Found

---

# 📑 API Documentation

Swagger/OpenAPI has been integrated for testing APIs directly from the browser.

```
http://localhost:8080/swagger-ui.html
```

---

# 🛠 Tech Stack

### Backend

- Java 17
- Spring Boot
- Spring Security
- Spring Data JPA
- Hibernate
- JWT
- Lombok
- Maven

### Database

- MySQL

### Frontend

- HTML
- CSS
- Bootstrap 5
- Vanilla JavaScript

### Documentation

- Swagger (SpringDoc OpenAPI)

### Email Service

- Spring Mail
- Gmail SMTP

---

# 📂 Project Structure

```
src
├── main
│   ├── java/com/example/demo
│   │   ├── config
│   │   ├── controller
│   │   ├── dto
│   │   ├── entity
│   │   ├── exception
│   │   ├── repository
│   │   ├── security
│   │   ├── service
│   │   └── DemoApplication.java
│   │
│   └── resources
│       ├── static
│       └── application.properties
```

---

# ⚙ Database Configuration

Configure your database inside:

```
src/main/resources/application.properties
```

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/librarydb
spring.datasource.username=YOUR_USERNAME
spring.datasource.password=YOUR_PASSWORD
spring.jpa.hibernate.ddl-auto=update
```

Create a database named:

```
librarydb
```

before starting the application.

---

# ▶ Running the Project

### Prerequisites

- Java 17+
- Maven
- MySQL Server

### Clone the repository

```bash
git clone <repository-url>
```

### Move into the project

```bash
cd demo
```

### Build

```bash
./mvnw clean package
```

Windows:

```bash
mvnw.cmd clean package
```

### Run

```bash
./mvnw spring-boot:run
```

---

# 🌐 Access the Application

Frontend

```
http://localhost:8080/
```

Swagger

```
http://localhost:8080/swagger-ui.html
```

---

# 📌 API Overview

## Authentication

- Register User
- Login User

## Books

- Get All Books
- Get Available Books
- Add Book
- Update Book
- Delete Book

## Borrow

- Borrow Book
- Return Book
- Borrow History
- View All Borrow Records

## Users

- View All Users

---

# 💡 Future Improvements

- Book Reservation System
- Online Renewal of Borrowed Books
- Search & Filter Books
- QR Code based Book Issue/Return
- Dashboard Analytics
- SMS Notifications
- PDF Receipt Generation
- Docker & CI/CD Deployment

---

# 👨‍💻 About the Project

This project was built to strengthen my understanding of backend development using Spring Boot and to simulate a real-world library management workflow. It focuses on secure authentication, role-based authorization, clean REST APIs, database management, automated email notifications, and business logic such as borrowing, returning, and fine calculation.
