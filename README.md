# 🎯 AIQuizzer Backend

AIQuizzer Backend is a **Spring Boot microservice** that generates, evaluates, and manages AI-powered quizzes. It integrates with **OpenRouter AI models** to generate questions, evaluate answers, and suggest improvements. The system is fully secured with **JWT authentication** and supports quiz retry history, email notifications, and Redis caching.

---

## 🚀 Features
- **User Authentication** (JWT-based login & signup)
- **AI Quiz Generation** (fill-in-the-blank style questions with hints)
- **Quiz Submission & Evaluation** (automatic scoring + AI suggestions)
- **Quiz Retry Support** (stores multiple attempts per quiz)
- **Quiz History Retrieval** (filterable by subject, marks, date, etc.)
- **Email Notifications** (sends scorecards & AI feedback to user’s email)
- **Redis Caching** (for performance optimization)
- **PostgreSQL Database** integration
- **Dockerized** for easy deployment

---

## 🛠️ Tech Stack
- **Backend:** Java 21, Spring Boot 3
- **Database:** PostgreSQL
- **Caching:** Redis
- **AI API:** OpenRouter (GPT-based models)
- **Security:** Spring Security with JWT
- **Deployment:** Docker

---

## 📂 Project Structure
```

com.assignment.playpowerlabs.AIQuizzer
│── config/               # Redis & Security configuration
│── controller/           # REST APIs (Auth, Quiz, History, Retry, Evaluation)
│── entity/               # JPA entities (User, Quiz, Attempt, QuestionResponse)
│── repository/           # Spring Data JPA repositories
│── security/             # JWT authentication & filters
│── service/              # Business logic (Quiz generation, evaluation, email, etc.)
│── dto/                  # Data Transfer Objects
│── util/                 # Helper utilities (JwtUtil, SpecBuilder, etc.)

````

---

## ⚙️ Setup & Installation

### 1️⃣ Clone the Repository
```bash
git clone https://github.com/your-username/AiquizzerBackend.git
cd AiquizzerBackend
````

### 2️⃣ Configure Database

Update your `application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/aiquizzer
spring.datasource.username=yourusername
spring.datasource.password=yourpassword
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

### 3️⃣ Configure Redis

```properties
spring.data.redis.host=localhost
spring.data.redis.port=6379
```

### 4️⃣ Configure AI API (OpenRouter)

```properties
openrouter.api.url=https://openrouter.ai/api/v1/chat/completions
openrouter.api.key=your_api_key_here
```

### 5️⃣ Build & Run

```bash
mvn clean install
mvn spring-boot:run
```

---

## 🐳 Docker Deployment

### Build Docker image

```bash
docker build -t aiquizzer-app-backend .
```

### Run with Docker Compose

```yaml
version: '3.8'
services:
  postgres:
    image: postgres:17
    environment:
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: postgres
      POSTGRES_DB: aiquizzer
    ports:
      - "5432:5432"
  redis:
    image: redis:latest
    ports:
      - "6379:6379"
  backend:
    build: .
    depends_on:
      - postgres
      - redis
    ports:
      - "8080:8080"
```

```bash
docker-compose up
```

---

## 🔑 Authentication

* User signs up via `/api/auth/signup`
* Logs in via `/api/auth/login`
* JWT is issued and stored in **HTTP-only cookie**
* All quiz APIs require authentication

---

## 📡 API Endpoints

### 🔐 Auth

* `POST /api/auth/signup` → Register new user
* `POST /api/auth/login` → Login, returns JWT in cookie

### 📝 Quiz

* `POST /api/quiz/generate` → Generate new AI quiz
* `GET /api/quiz/history` → Get past quizzes (with filters)
* `POST /api/quiz/submit` → Submit answers & get evaluation
* `POST /api/quiz/retry/{quizId}` → Retry a quiz

### 📊 Quiz Attempts

* `GET /api/quiz/attempts/{quizId}` → Retrieve quiz attempts with submitted answers

---

## 📧 Email Notifications

* After evaluation, user receives an **email scorecard** with:

  * Marks obtained
  * AI-generated suggestions
  * Attempt summary

---

## 🧪 Testing

Use **Postman** or **cURL**:

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser","password":"password"}' \
  -c cookies.txt
```

Then use `-b cookies.txt` for authenticated requests.

---

## 🤝 Contributing

1. Fork this repo
2. Create a feature branch (`feature/new-feature`)
3. Commit your changes
4. Push and create a PR

---

## 📜 License

MIT License © 2025 AIQuizzer

```

---

✅ Now you can copy the whole thing in one go and save it as `README.md`.  

Do you also want me to **add UML diagrams (architecture + DB ERD)** in the README (as PlantUML/mermaid code blocks) so your GitHub looks even more professional?
