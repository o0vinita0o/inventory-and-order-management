
# Inventory & Order Management System

A full-stack monorepo application designed to handle product catalog browsing, inventory tracking, and concurrent order processing with transactional integrity.

---

## Tech Stack

* **Backend:** Java 21, Spring Boot 3.4.3, Spring Data JPA, Flyway, Maven
* **Database:** PostgreSQL 15
* **Frontend:** React, TypeScript, Vite
* **CI/CD & DevOps:** GitHub Actions, Docker Compose

---

## Key Architecture & Design Highlights

* **Monorepo Architecture:** Clean separation of concerns with isolated `backend/` and `frontend/` services.
* **Concurrency & Stock Integrity:** Implements pessimistic write locking (`PESSIMISTIC_WRITE`) at the database level during order checkout to prevent race conditions and stock overselling.
* **Schema Evolution:** Managed with version-controlled Flyway migrations (`V1__Init_Schema.sql`) rather than automatic JPA generation.
* **Automated CI Pipeline:** GitHub Actions workflow running on pull requests and pushes to `main`, running test suites against an ephemeral PostgreSQL service container.
* **Slice & Unit Testing:** Comprehensive testing using MockMvc (`@WebMvcTest`) for controller isolation and Mockito for transactional service logic.

---

## Project Structure

```text
inventory-and-order-management/
├── .github/
│   └── workflows/
│       └── ci.yml               # GitHub Actions CI workflow
├── backend/                     # Spring Boot API service
│   ├── src/
│   ├── pom.xml
│   └── mvnw
├── frontend/                    # React + Vite client
│   ├── src/
│   └── package.json
├── docker-compose.yml           # Local infrastructure orchestration
└── README.md

```

---

## Getting Started

### Prerequisites

* [JDK 21](https://adoptium.net/)
* [Node.js (v18+)](https://nodejs.org/) & npm
* [Docker Desktop](https://www.docker.com/)

---

### 1. Start Infrastructure (PostgreSQL)

From the project root, start the local database container:

```bash
docker compose up -d

```

*(To stop and clean up volumes later: `docker compose down -v`)*

---

### 2. Run the Backend

Navigate to the `backend/` folder and boot the Spring application:

```bash
cd backend
./mvnw spring-boot:run

```

*On Windows PowerShell:*

```powershell
cd backend
.\mvnw.cmd spring-boot:run

```

* The backend runs on `http://localhost:8080`.
* Interactive API documentation (Swagger UI) is available at: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)


---

### 3. Run the Frontend

In a separate terminal, navigate to the `frontend/` folder, install dependencies, and launch the development server:

```bash
cd frontend
npm install
npm run dev

```

* The client application will be accessible at: [http://localhost:5173/](http://localhost:5173/)


---

## Running Tests

Run the backend unit and integration test suite via the Maven wrapper:

```bash
cd backend
./mvnw clean test

```
