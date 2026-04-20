# Multilingual Support Agent

A dynamic, AI-powered customer support chatbot that understands queries in **Hindi**, **Tamil**, **Telugu**, **Kannada**, and **English**.

![Languages](https://img.shields.io/badge/Languages-5-green)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2-brightgreen)
![Kotlin](https://img.shields.io/badge/Kotlin-1.9-purple)
![Claude AI](https://img.shields.io/badge/AI-Claude-blue)

## 🚀 Features

- **Dynamic Query Understanding**: No predefined templates — handles any question in any phrasing
- **5 Languages Supported**: Hindi, Tamil, Telugu, Kannada, English (including mixed language/Hinglish)
- **Two-Stage Claude AI**: Intent extraction + Response formatting
- **Real Database Queries**: Actual data from H2/PostgreSQL, not mock responses
- **Smart Escalation**: Automatically detects fraud, anger, legal threats and escalates
- **Beautiful UI**: Glassmorphism chat interface with language badges

## 📁 Project Structure

```
multilingual-support-agent/
├── backend/                    # Spring Boot Kotlin API
│   ├── src/main/kotlin/
│   │   └── com/support/agent/
│   │       ├── controller/     # REST endpoints
│   │       ├── service/        # Business logic
│   │       ├── repository/     # Database access
│   │       ├── entity/         # JPA entities
│   │       ├── model/          # DTOs
│   │       └── config/         # Configuration
│   └── src/main/resources/
│       ├── application.yml     # Configuration
│       └── data.sql           # Seed data
├── frontend/
│   └── index.html             # Glossy chat UI
├── docker-compose.yml
└── README.md
```

## 🛠️ Quick Start

### Prerequisites

- Java 17+
- Gradle 8+ (or use wrapper)
- Claude API Key from [Anthropic](https://console.anthropic.com/)

### 1. Set API Key

```bash
export ANTHROPIC_API_KEY=your_api_key_here
```

### 2. Run Backend

```bash
cd backend
./gradlew bootRun
```

Backend runs at: `http://localhost:8080`

### 3. Open Frontend

Simply open `frontend/index.html` in a browser, or serve it:

```bash
cd frontend
python -m http.server 3000
```

Frontend runs at: `http://localhost:3000`

## 🐳 Docker Deployment

```bash
# Set your API key
export ANTHROPIC_API_KEY=your_api_key_here

# Build and run
docker-compose up --build
```

- Backend: `http://localhost:8080`
- Frontend: `http://localhost:3000`

## ☁️ Cloud Deployment

### Railway.app (Recommended)

1. Push code to GitHub
2. Create new project on Railway
3. Connect your repo
4. Add environment variable: `ANTHROPIC_API_KEY`
5. Deploy!

### Render.com

1. Create Web Service from GitHub
2. Set build command: `cd backend && ./gradlew bootJar`
3. Set start command: `java -jar backend/build/libs/*.jar`
4. Add `ANTHROPIC_API_KEY` env variable

## 📡 API Endpoints

### POST /api/chat

Send a message to the chatbot.

**Request:**
```json
{
  "userId": "USR001",
  "message": "मेरा KYC status क्या है?",
  "sessionId": "sess_abc123",
  "conversationHistory": []
}
```

**Response:**
```json
{
  "sessionId": "sess_abc123",
  "response": "नमस्ते राजेश जी! आपका KYC verified है...",
  "detectedLanguage": "hindi",
  "intentCategory": "kyc",
  "intentType": "status_check",
  "escalated": false,
  "confidence": 0.95
}
```

### GET /api/escalations

List all escalated issues (for admin/judges demo).

### GET /api/health

Health check endpoint.

## 🧪 Demo Scenarios

### 1. Hindi KYC Query (USR001)
```
मेरा KYC status क्या है?
```

### 2. Tamil Payment Failure (USR001)
```
என் Swiggy பேமெண்ட் fail ஆனது
```

### 3. Telugu Balance Check (USR002)
```
నా బ్యాలెన్స్ చూపించు
```

### 4. Kannada Escalation (USR003)
```
ನನ್ನ account ನಿಂದ fraud transaction ಆಗಿದೆ
```

### 5. English Out of Scope
```
Can I get a loan?
```

## 💰 Cost Estimate

Claude Haiku pricing (~$0.002-0.003 per message):
- Hackathon demo: **< $1.00 total**
- 100 daily users: **~$6-9/month**

## 🏗️ Architecture

```
User Message → Intent Extraction (Claude #1)
                     ↓
              Extract: language, intent, entities
                     ↓
              Database Query (Spring Data JPA)
                     ↓
              Response Formatting (Claude #2)
                     ↓
              Natural response in user's language
```

## 📝 Database Schema

| Table | Purpose |
|-------|---------|
| `users` | User profiles, balance, status |
| `kyc_records` | KYC verification status |
| `transactions` | Payment history |
| `escalations` | Escalated issues |

## 🔧 Configuration

Edit `backend/src/main/resources/application.yml`:

```yaml
claude:
  api-key: ${ANTHROPIC_API_KEY}
  model: claude-haiku-4-5    # Fastest & cheapest
  max-tokens: 1024

spring:
  datasource:
    url: jdbc:h2:mem:supportdb  # H2 for dev
    # url: jdbc:postgresql://localhost:5432/supportdb  # Postgres for prod
```

## 📄 License

MIT License - feel free to use for hackathons and projects!

---

Built with ❤️ for multilingual India
