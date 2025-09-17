# 📘 Booking System (Spring Boot + JWT + RBAC)

## 🚀 Overview
This project is a **RESTful Booking System** built with **Spring Boot 3.x** and **MySQL**.  
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
git clone https://github.com/<your-username>/booking-system.git
cd booking-system
