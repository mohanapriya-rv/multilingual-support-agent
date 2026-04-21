# Multilingual Fintech Support Agent

A dynamic, AI-powered customer support chatbot for fintech platforms that understands queries in **10+ Indian languages** including Hindi, Tamil, Telugu, Kannada, Malayalam, Bengali, Marathi, Gujarati, Punjabi, and English.

**Problem:** Over 75% of internet users in India prefer regional languages, but fintech customer support systems are primarily English-centric, creating accessibility gaps and poor user experience.

**Solution:** Built a multilingual AI support agent that provides instant responses to KYC status, transactions, mutual funds, account information, and intelligently escalates complex issues to human agents.

![Languages](https://img.shields.io/badge/Languages-10%2B-green)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2-brightgreen)
![Kotlin](https://img.shields.io/badge/Kotlin-1.9-purple)
![Claude AI](https://img.shields.io/badge/AI-Claude-blue)

## 🚀 Features

- **10+ Languages Supported**: Hindi, Tamil, Telugu, Kannada, Malayalam, Bengali, Marathi, Gujarati, Punjabi, English
- **Dynamic Query Understanding**: No predefined templates — handles any question in any phrasing
- **Two-Stage Claude AI**: Intent extraction + Response formatting
- **Real Database Queries**: Actual data from PostgreSQL, not mock responses
- **Smart Escalation**: Automatically detects fraud, anger, legal threats and escalates
- **Context-Aware Suggestions**: Dynamic suggestions based on KYC/transaction status
- **Analytics Dashboard**: Real-time monitoring of escalations, language trends, query volume
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

**Example 1: KYC Status Check (Hindi)**
```json
{
  "userId": "USR001",
  "message": "मेरा KYC status क्या है?",
  "sessionId": "sess_abc123",
  "conversationHistory": [],
  "inputType": "text"
}
```

**Response:**
```json
{
  "sessionId": "sess_abc123",
  "response": "नमस्ते राजेश जी! आपका KYC verified है. आपके द्वारा जमा किए गए दस्तावेज़: aadhaar, pan, address_proof",
  "detectedLanguage": "hindi",
  "intentCategory": "kyc",
  "intentType": "status_check",
  "escalated": false,
  "escalationId": null,
  "confidence": 0.95,
  "suggestions": ["Check KYC status", "Upload documents", "KYC pending?", "Update PAN details", "Link Aadhaar"],
  "inputType": "text"
}
```

**Example 2: Transaction Query (Tamil)**
```json
{
  "userId": "USR001",
  "message": "என் Swiggy பேமெண்ட் fail ஆனது",
  "sessionId": "sess_def456",
  "conversationHistory": [],
  "inputType": "text"
}
```

**Response:**
```json
{
  "sessionId": "sess_def456",
  "response": "உங்கள் Swiggy பேமெண்ட் தோல்வியடைந்தது. காரணம்: Insufficient balance. பணம் சேர்த்த பிறகு மீண்டும் முயற்சி செய்யவும்.",
  "detectedLanguage": "tamil",
  "intentCategory": "transaction",
  "intentType": "failure_reason",
  "escalated": false,
  "escalationId": null,
  "confidence": 0.92,
  "suggestions": ["Retry payment", "Why did it fail?", "Check bank", "Refund status", "Contact support"],
  "inputType": "text"
}
```

**Example 3: Balance Check (Telugu)**
```json
{
  "userId": "USR002",
  "message": "నా బ్యాలెన్స్ చూపించు",
  "sessionId": "sess_ghi789",
  "conversationHistory": [],
  "inputType": "text"
}
```

**Response:**
```json
{
  "sessionId": "sess_ghi789",
  "response": "మీ బ్యాలెన్స్ ₹12,500.50 ఉంది. మీ ఖాతా యాక్టివ్ స్థితిలో ఉంది.",
  "detectedLanguage": "telugu",
  "intentCategory": "account",
  "intentType": "balance_check",
  "escalated": false,
  "escalationId": null,
  "confidence": 0.89,
  "suggestions": ["Check balance", "Update mobile", "Account statement", "Reset PIN", "Unlock account"],
  "inputType": "text"
}
```

**Example 4: Mutual Fund Portfolio (English)**
```json
{
  "userId": "USR001",
  "message": "Show my mutual fund portfolio",
  "sessionId": "sess_jkl012",
  "conversationHistory": [],
  "inputType": "text"
}
```

**Response:**
```json
{
  "sessionId": "sess_jkl012",
  "response": "Your mutual fund portfolio: Axis Bluechip Fund - ₹50,000, HDFC Small Cap Fund - ₹30,000. Total investment: ₹80,000. Current value: ₹85,500. Unrealized gains: ₹5,500.",
  "detectedLanguage": "english",
  "intentCategory": "mutual_fund",
  "intentType": "portfolio",
  "escalated": false,
  "escalationId": null,
  "confidence": 0.94,
  "suggestions": ["View portfolio", "Check SIP status", "Start new SIP", "Redeem funds", "Fund returns"],
  "inputType": "text"
}
```

**Example 5: Fraud Escalation (Kannada)**
```json
{
  "userId": "USR003",
  "message": "ನನ್ನ account ನಿಂದ fraud transaction ಆಗಿದೆ",
  "sessionId": "sess_mno345",
  "conversationHistory": [],
  "inputType": "text"
}
```

**Response:**
```json
{
  "sessionId": "sess_mno345",
  "response": "ನಿಮ್ಮ ಖಾತೆಯಿಂದ ವಂಚನೆ ವ್ಯವಹಾರವನ್ನು ಪತ್ತೆಹಚ್ಚಿದ್ದೇವೆ. ತಕ್ಷಣವೇ ನಮ್ಮ ವಿಶೇಷ ತಂಡಕ್ಕೆ ವರದಿ ಮಾಡಲಾಗಿದೆ. ನಿಮ್ಮ ಖಾತೆಯನ್ನು ತಾತ್ಕಾಲಿಕವಾಗಿ ನಿಲ್ಲಿಸಲಾಗಿದೆ.",
  "detectedLanguage": "kannada",
  "intentCategory": "transaction",
  "intentType": "fraud_report",
  "escalated": true,
  "escalationId": "ESC-2024-001",
  "confidence": 0.98,
  "suggestions": ["Talk to agent", "File complaint", "Report fraud", "Urgent help needed", "Call me back"],
  "inputType": "text"
}
```

### GET /api/analytics/dashboard

Get analytics data with optional date range.

**Request:**
```
GET /api/analytics/dashboard?startDate=2024-01-01&endDate=2024-01-31
```

**Response:**
```json
{
  "totalConversations": 1250,
  "totalQueries": 3420,
  "allTimeQueries": 15000,
  "allTimeConversations": 5800,
  "todayQueries": 150,
  "kycQueries": 850,
  "investmentQueries": 1200,
  "transactionQueries": 1370,
  "escalations": 45,
  "escalationRate": 1.32,
  "languageDistribution": {
    "hindi": 1200,
    "tamil": 800,
    "telugu": 600,
    "english": 500,
    "kannada": 320
  },
  "intentDistribution": {
    "kyc": 850,
    "transaction": 1370,
    "mutual_fund": 1200
  },
  "successRate": 98.68,
  "startDate": "2024-01-01",
  "endDate": "2024-01-31"
}
```

### GET /api/escalations

List all escalated issues (for admin/judges demo).

### GET /api/health

Health check endpoint.

## 🧪 Demo Scenarios

### 1. KYC Status Check (Hindi)
**User:** USR001
**Query:** `मेरा KYC status क्या है?`
**Intent:** kyc/status_check
**Response:** KYC verified status with document details

### 2. Transaction Failure (Tamil)
**User:** USR001
**Query:** `என் Swiggy பேமெண்ட் fail ஆனது`
**Intent:** transaction/failure_reason
**Response:** Failure reason and next steps

### 3. Balance Check (Telugu)
**User:** USR002
**Query:** `నా బ్యాలెన్స్ చూపించు`
**Intent:** account/balance_check
**Response:** Current balance and account status

### 4. Mutual Fund Portfolio (English)
**User:** USR001
**Query:** `Show my mutual fund portfolio`
**Intent:** mutual_fund/portfolio
**Response:** Fund holdings, investment value, gains

### 5. Fraud Escalation (Kannada)
**User:** USR003
**Query:** `ನನ್ನ account ನಿಂದ fraud transaction ಆಗಿದೆ`
**Intent:** transaction/fraud_report
**Response:** Escalation triggered, account frozen

### 6. SIP Status (Malayalam)
**User:** USR004
**Query:** `എന്റെ SIP സ്റ്റാറ്റസ് പരിശോധിക്കുക`
**Intent:** mutual_fund/sip_status
**Response:** SIP details and next installment date

### 7. PAN Update (Gujarati)
**User:** USR005
**Query:** `મારું PAN અપડેટ કરવું છે`
**Intent:** kyc/pan_update
**Response:** PAN update process and requirements

### 8. Account Statement (Marathi)
**User:** USR006
**Query:** `खाते विधान डाउनलोड करा`
**Intent:** account/statement
**Response:** Statement download link and instructions

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
