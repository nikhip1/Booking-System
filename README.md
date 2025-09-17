# 📘 Booking System (Spring Boot + JWT + RBAC)

## 🚀 Overview
This project is a **RESTful Booking System** built with **Spring Boot 3.x**, *Java 17** and **MySQL**.  
It provides functionality to manage resources (e.g., rooms, vehicles, equipment) and reservations with proper **JWT authentication** and **Role-Based Access Control (RBAC)**.

---

## ✨ Features
- **Authentication & Authorization**
  - JWT-based stateless authentication
  - Role-based access (ADMIN, USER)
  - Password hashing with BCrypt
- **Resource Management**
  - ADMIN: full CRUD
  - USER: read-only
- **Reservation Management**
  - ADMIN: CRUD on all reservations
  - USER: CRUD on own reservations
  - Filtering by `status`, `price range`
  - Pagination & Sorting
- **API Documentation**
  - Swagger UI / OpenAPI 3
  - Postman Collection

---

## 🛠 Tech Stack
- Java 17
- Spring Boot 3.x
- Spring Security + JWT
- Spring Data JPA (Hibernate)
- MySQL 8+
- Lombok
- Springdoc OpenAPI (Swagger)

---

## ⚙️ Setup & Installation

### 1️⃣ Clone the Repository
```bash
git clone https://github.com/nikhip1/Booking-System.git
cd Booking-System

### 2️⃣ Configure Database
Update src/main/resources/application.properties with your MySQL settings:
```bash
spring.datasource.url=jdbc:mysql://localhost:3306/booking_db?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=yourpassword

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

### 3️⃣ Build & Run
```bash
# Build the project
mvn clean install

# Run the application
mvn spring-boot:run

--- 
Application will start at:
👉 http://localhost:8080

---

## 👤 Seed Users
| Role  | Username | Password |
| ----- | -------- | -------- |
| ADMIN | admin    | admin123 |
| USER  | user     | user123  |

🔒 Passwords are stored securely using BCrypt.

---

## 📖 API Endpoints

### 🔑 Authentication
- POST /auth/login → Generate JWT

### 📦 Resources

- GET /resources → List resources (paginated)
- GET /resources/{id} → Get resource by ID
- POST /resources → Create resource (ADMIN only)
- PUT /resources/{id} → Update resource (ADMIN only)
- DELETE /resources/{id} → Delete resource (ADMIN only)

### 📑 Reservations

- GET /reservations → List reservations
  - ADMIN: all reservations 
  - USER: only own reservations 
  - Query params: status, minPrice, maxPrice, page, size, sort
- GET /reservations/{id} → Get reservation by ID
- POST /reservations → Create reservation
  - USER: auto-linked to logged-in user
- PUT /reservations/{id} → Update reservation
- DELETE /reservations/{id} → Cancel reservation

---

## 📖 API Documentation
- Postman collection available in postman/BookingSystem.postman_collection.json
       [Postman Link](https://niks83543-5730876.postman.co/workspace/Nikhil-Patil's-Workspace~32f974fd-1504-41ad-87c0-63b223896e72/collection/48502138-7cac85bb-aa3d-4b9e-9e5b-2476126f4103?action=share&creator=48502138&active-environment=48502138-308efa77-edb8-467f-879f-b5f2000a43de)

---

## 📝 Assumptions & Trade-offs

- ### Authentication & Authorization
   - JWT secret and expiration time are configurable via application.properties.
   - Role-based access is enforced at controller/service layer.

- ### Users & Roles
   - Two roles are supported: ADMIN and USER.
   - User IDs for reservations are derived from the JWT principal (not client-provided).

- ### Reservations
  - Reservation status values: PENDING, CONFIRMED, CANCELLED.
  - overlap prevention is implemented.
  - Price is stored as decimal(10,2) (can be adjusted as needed).

- ### Database
   - Designed for both MySQL and PostgreSQL (tested on MySQL).
   - Uses spring.jpa.hibernate.ddl-auto=update for faster setup.

- ### Error Handling
  - Global exception handler provides standardized error responses (400, 401, 403, 404, 500).
  - Validation errors return descriptive messages via Spring Validation.

- ### Security
  - Passwords stored securely with BCrypt hashing.
  - JWT expiration set to 1 hour by default (can be configured).

- ### Documentation & Testing
  - Postman collection optionally provided for quick testing.
  - Basic tests may be included (time permitting).


