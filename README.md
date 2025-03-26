# 📋 TaskManager

**TaskManager** is a full-stack task management application built with Spring Boot.  
It features secure user authentication using **JWT tokens stored in cookies**, a clean architecture, and a RESTful API for managing tasks.

---

## Features

-  User registration and login
-  JWT-based authentication via `HttpOnly` cookies
-  Secure logout with token invalidation
-  JWT **token blacklist** after logout
-  Full CRUD operations for tasks
-  Input validation using `Jakarta Validation`
-  Global exception handling
-  Clean, modular code structure following best practices

---

## 🧰 Tech Stack

| Technology              | Description                     |
|-------------------------|---------------------------------|
| **Java 17+**            | Programming language            |
| **Spring Boot 3**       | Backend framework               |
| **Spring Security**     | Authentication & authorization |
| **JWT (via jjwt)**      | Token-based authentication      |
| **Jakarta Validation**  | Input validation                |
| **H2 Database**         | In-memory development DB        |
| **SLF4J + Logback**     | Logging                         |
| **Maven**               | Build tool                      |
| **Lombok** *(optional)* | Less boilerplate in models      |

---

## 📦 Getting Started

Follow these steps to set up and run the **TaskManager** application locally:

---

### 1. **Clone the repository**

```bash
git clone https://github.com/your-username/taskmanager.git
cd taskmanager
```

---

### 2. **Import the project into your IDE**

You can use any Java IDE like:

- [IntelliJ IDEA](https://www.jetbrains.com/idea/)
- [Eclipse](https://www.eclipse.org/)
- [VS Code](https://code.visualstudio.com/) with Java extension

> Make sure your IDE supports **Java 17** and **Maven projects**.

---

### 3. **Build the project**

If you're using the terminal or IDE terminal:

```bash
./mvnw clean install
```

Or if Maven is installed globally:

```bash
mvn clean install
```

---

### 4. **Run the application**

You can run the app via your IDE (right-click `TaskManagerApplication.java` → Run), or via terminal:

```bash
./mvnw spring-boot:run
```

The application will start on the default port:

```
http://localhost:8080
```

---

### 5. **Test the API**

Use a REST client like **Postman**, **Insomnia**, or your browser (for `GET` requests) to interact with the API.

Example endpoints:

- `POST /api/auth/signup` – Register a new user
- `POST /api/auth/login` – Login (JWT is set in HttpOnly cookie)
- `POST /api/auth/logout` – Logout (cookie is cleared)
- `GET /api/tasks` – Get all tasks *(requires login)*

---

## 🔐 Authentication API

| Endpoint             | Method | Description        |
|----------------------|--------|--------------------|
| `/api/auth/signup`   | POST   | Register user      |
| `/api/auth/login`    | POST   | Login user         |
| `/api/auth/logout`   | POST   | Logout user        |
| `/api/tasks`         | CRUD   | Task management    |

---

## 📁 Project Structure (example)

```
src/
├── controller/
│   └── rest/
├── service/
├── model/
├── dto/
├── security/
├── exception/
└── util/
```

---

## 🛡️ Security Highlights

- JWT tokens stored securely in `HttpOnly` cookies
- Logout mechanism clears cookie and blacklists the token
- Exception handling via `@ControllerAdvice`
- JWT filter verifies token and sets authentication context

---

## 👨‍💼 Author

**Denis  Ergenekon**
My personal website: https://e-denis.sk