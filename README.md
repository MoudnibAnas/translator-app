# Tarjmaan - Moroccan Darija Translator


**Tarjmaan** is a comprehensive translation platform that translates English text and audio to Moroccan Darija (الدارجة المغربية) using Google's Gemini AI. The 
project includes a Spring Boot backend, React web application, and Chrome extension for seamless translation across different contexts.

## 🎥 Tarjmaan Architecture Diagram
[**View the image here**](https://ibb.co/mrZfMWJV)

## 🎥 Video Demo
[**Watch My Loom video demo here**](https://www.loom.com/share/f68caacc7c324b12806be6e1e7a4f89f)

## 🎯 Features

### Text Translation
- **Real-time Translation**: Instant translation from English to Moroccan Darija
- **Smart Input**: Support for both typed text and audio input
- **Chat Interface**: Conversational UI for natural interaction

### Audio Translation
- **Live Recording**: Record audio directly in the browser
- **File Upload**: Upload existing audio files (MP3, WAV, OGG, WebM)
- **Speech-to-Text**: Automatic transcription and translation
- **Audio Preview**: Listen to uploaded recordings before translation

### User Management
- **Authentication**: JWT-based secure authentication
- **Registration**: User registration and login system
- **Protected Routes**: Secure access to translation features

### Browser Integration
- **Chrome Extension**: Translate text directly in web pages
- **Side Panel**: Convenient access to translation features
- **Content Script**: Highlight and translate selected text

### Technical Features
- **RESTful API**: Clean API endpoints for translation services
- **Real-time Updates**: Live status indicators and typing animations
- **Responsive Design**: Mobile-friendly interface
- **Error Handling**: Graceful error handling and user feedback

## 🏗️ Architecture

### Backend (Spring Boot)
```
darijaa-translator/
├── src/main/java/org/mql/genai/darijaatranslator/
│   ├── controller/          # REST API endpoints
│   │   ├── AuthController.java      # Authentication endpoints
│   │   └── TranslationController.java # Translation endpoints
│   ├── service/             # Business logic
│   │   ├── AuthenticationService.java
│   │   ├── TranslationService.java
│   │   ├── AudioTranslationService.java
│   │   └── GeminiTranslationService.java
│   ├── models/              # Data models
│   │   ├── User.java
│   │   ├── AuthRequest.java
│   │   ├── AuthResponse.java
│   │   ├── TranslationRequest.java
│   │   ├── TranslationResponse.java
│   │   ├── AudioRequest.java
│   │   └── AudioTranslationResponse.java
│   ├── repository/          # Database layer
│   │   └── UserRepository.java
│   ├── config/              # Configuration classes
│   │   ├── SecurityConfig.java
│   │   ├── JwtAuthenticationFilter.java
│   │   ├── JwtUtil.java
│   │   └── RestTemplateConfig.java
│   └── DarijaaTranslatorApplication.java
└── src/main/resources/
    ├── application.properties
    └── static/
```

### Frontend (React)
```
React-client/darija-translator/
├── src/
│   ├── App.js              # Main application component
│   ├── context/            # React context for auth
│   │   └── AuthContext.js
│   ├── components/         # React components
│   │   ├── Login.js
│   │   ├── Register.js
│   │   ├── Logout.js
│   │   ├── ProtectedRoute.js
│   │   ├── AudioRecorder.js
│   │   ├── MessageBubble.js
│   │   ├── TranslationResult.js
│   │   ├── StatusIndicator.js
│   │   └── css/            # Component styles
│   └── index.js
└── public/
```

### Chrome Extension
```
chrome-extension/
├── manifest.json           # Extension configuration
├── background.js           # Background service worker
├── content-script.js       # Injects into web pages
├── sidepanel.html          # Side panel interface
├── sidepanel.js            # Side panel logic
└── icons/                  # Extension icons
```

## 🚀 Quick Start

### Prerequisites
- Java 17+
- Node.js 16+
- Google Gemini API Key
- Chrome Browser (for extension)

### Installation

#### 1. Backend Setup
```bash
# Navigate to backend directory
cd darijaa-translator

# Install dependencies
./mvnw clean install

# Configure environment
cp src/main/resources/application.properties.example src/main/resources/application.properties
```

#### 2. Frontend Setup
```bash
# Navigate to frontend directory
cd React-client/darija-translator

# Install dependencies
npm install

# Start development server
npm start
```

#### 3. Chrome Extension Setup
```bash
# Navigate to extension directory
cd chrome-extension

# Load extension in Chrome:
# 1. Open Chrome and go to chrome://extensions/
# 2. Enable "Developer mode"
# 3. Click "Load unpacked" and select the chrome-extension folder
```

#### 3. Configuration

**Backend Configuration** (`src/main/resources/application.properties`):
```properties
# Server Configuration
server.port=8080

# Gemini AI Configuration
gemini.api.key=your_gemini_api_key_here
gemini.model.url=https://generativelanguage.googleapis.com/v1/models/${gemini.model.name}:generateContent?key=${gemini.api.key}
gemini.model.name=gemini-2.5-flash
gemini.prompt=Translate English to Moroccan Arabic (Darija) directly without explanations.\nEnglish sentence ? Moroccan Darija sentence.\n
gemini.audio.prompt=Listen to this English audio and translate it directly to Moroccan Arabic (Darija). Provide ONLY the Darija translation. If you cannot understand the audio, return: 'Could not understand audio'. If the audio is not in English, return: 'Not English audio'.

# File Upload Configuration
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB
```

**Frontend Configuration** (`.env`):
```env
REACT_APP_API_BASE_URL=http://localhost:8080
```

### Running the Application

#### Development Mode
```bash
# Start backend
cd darijaa-translator
./mvnw spring-boot:run

# In another terminal, start frontend
cd React-client/darija-translator
npm start

# Extension is loaded in Chrome as described above
```

#### Production Build
```bash
# Build backend JAR
cd darijaa-translator
./mvnw clean package

# Build frontend
cd React-client/darija-translator
npm run build

# Run backend
java -jar target/darijaa-translator-0.0.1-SNAPSHOT.jar
```

## 📚 API Documentation

### Authentication Endpoints

#### Register User
```http
POST /api/auth/register
Content-Type: application/json

{
  "username": "user@example.com",
  "password": "password123"
}
```

#### Login User
```http
POST /api/auth/login
Content-Type: application/json

{
  "username": "user@example.com",
  "password": "password123"
}
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "username": "user@example.com"
}
```

### Translation Endpoints

#### Text Translation
```http
POST /api/translate
Authorization: Bearer <token>
Content-Type: application/json

{
  "englishText": "Hello, how are you?"
}
```

**Response:**
```json
{
  "originalText": "Hello, how are you?",
  "translatedText": "مرحبا، ازاكم؟",
  "modelUsed": "gemini-2.5-flash"
}
```

#### Audio Translation
```http
POST /api/audio/translate
Authorization: Bearer <token>
Content-Type: application/json

{
  "audioBase64": "UklGRiQAAABXQVZFZm10IBAAAAABAAEAQB8AAEAfAAABAAgAZGF0YQ...",
  "audioFormat": "webm"
}
```

**Response:**
```json
{
  "transcribedText": "Hello, how are you?",
  "translatedText": "مرحبا، ازاكم؟",
  "modelUsed": "gemini-2.5-flash"
}
```

#### File Upload Translation
```http
POST /api/audio/upload
Authorization: Bearer <token>
Content-Type: multipart/form-data

file: [audio file]
```


## 🔧 Technologies Used

### Backend
- **Spring Boot 3.2.4** - Web framework
- **Spring Security** - Authentication and authorization
- **JWT** - Token-based authentication
- **Spring Data JPA** - Database ORM
- **H2 Database** - Development database
- **Google Gemini API** - AI translation service

### Frontend
- **React 19** - UI framework
- **React Router** - Client-side routing
- **React Context** - State management
- **CSS3** - Styling with animations
- **Web Audio API** - Audio recording functionality

### Browser Extension
- **Chrome Extension API** - Browser integration
- **Manifest V3** - Extension configuration
- **Content Scripts** - Web page interaction

### Development Tools
- **Maven** - Java dependency management
- **npm** - JavaScript dependency management
- **Git** - Version control

## 📦 Project Structure

### Directory Structure
```
Transaltor/
├── README.md                    # This file
├── ARCHITECTURE.md             # Detailed architecture documentation
├── AUTHENTICATION_README.md    # Authentication documentation
├── darijaa-translator/         # Spring Boot backend
│   ├── pom.xml                 # Maven configuration
│   ├── src/main/java/          # Java source code
│   ├── src/main/resources/     # Configuration files
│   └── target/                 # Compiled artifacts
├── React-client/               # React frontend
│   └── darija-translator/      # React application
│       ├── package.json        # npm dependencies
│       ├── src/                # React source code
│       └── public/             # Static assets
├── chrome-extension/           # Chrome browser extension
│   ├── manifest.json           # Extension manifest
│   ├── background.js           # Background script
│   ├── content-script.js       # Content script
│   ├── sidepanel.html          # Side panel HTML
│   ├── sidepanel.js            # Side panel script
│   └── icons/                  # Extension icons
└── docs/                       # Documentation (future)
```

## 🤝 Contributing

We welcome contributions! Please follow these steps:

1. **Fork the repository**
2. **Create a feature branch**: `git checkout -b feature/amazing-feature`
3. **Commit your changes**: `git commit -m 'Add amazing feature'`
4. **Push to the branch**: `git push origin feature/amazing-feature`
5. **Open a Pull Request**

### Development Guidelines

- Follow existing code style and naming conventions
- Write tests for new features
- Update documentation for API changes
- Ensure all tests pass before submitting PR


## 🐛 Bug Reports

If you find a bug, please open an issue with:
- Clear description of the problem
- Steps to reproduce
- Expected behavior vs actual behavior
- Screenshots if applicable
- Browser and OS information

## 💡 Feature Requests

We're always looking for ways to improve! Submit feature requests by:
1. Checking existing issues to avoid duplicates
2. Creating a new issue with detailed description
3. Explaining the use case and benefits

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- **Google Gemini AI** - For providing the translation API
- **Spring Framework** - For the robust backend framework
- **React Community** - For the excellent frontend ecosystem
- **Moroccan Language Community** - For preserving and promoting Darija

**Made with ❤️ for the Moroccan community**
