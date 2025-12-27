# Darija Translator Application Architecture

## System Overview
A comprehensive translation platform for translating English text and audio to Moroccan Darija (Darija) with JWT-based authentication. The system includes a Spring Boot backend, React web application, and Chrome browser extension for seamless translation across different contexts.

## Architecture Description for AI Design

### 1. Frontend Architecture (React.js)

**Main Components:**
- **App Component**: Main router with AuthProvider wrapper
- **Login Component**: Authentication form with username/password
- **Register Component**: User registration with validation
- **ProtectedRoute Component**: Route guard for authenticated access
- **Main Translation Interface**: Chat-style UI with message bubbles
- **AudioRecorder Component**: WebRTC audio recording functionality
- **TranslationResult Component**: Displays translation results

**State Management:**
- **AuthContext**: Global authentication state (user, token, loading)
- **Local State**: Messages, input text, audio files, translation results

**Styling:**
- CSS modules for component-specific styles
- Responsive design with gradient backgrounds
- Modern UI with smooth animations

### 3. Chrome Extension Architecture

**Main Components:**
- **Manifest V3**: Extension configuration and permissions
- **Background Service Worker**: Handles API communication and authentication
- **Content Script**: Injects translation functionality into web pages
- **Side Panel**: Provides translation interface within browser
- **Popup Interface**: Quick access to extension features

**Functionality:**
- Text selection and translation on web pages
- Side panel for detailed translation interface
- Integration with backend API for authenticated requests
- Local storage for user preferences and tokens

### 2. Backend Architecture (Spring Boot)

**Core Layers:**
- **Controller Layer**: REST API endpoints
- **Service Layer**: Business logic and authentication
- **Repository Layer**: Database operations
- **Security Layer**: JWT authentication and authorization

**Key Components:**
- **AuthController**: `/api/auth/register`, `/api/auth/login`
- **TranslationController**: `/api/translate`, `/api/audio/translate`
- **AuthenticationService**: User registration and login logic
- **JwtUtil**: Token generation and validation
- **SecurityConfig**: Spring Security configuration with JWT filter

**Database:**
- **User Entity**: Stores username, email, hashed password
- **H2 Database**: In-memory database for development
- **JPA Repository**: Data access layer

### 3. Authentication Flow

**Registration Process:**
1. User fills registration form (username, email, password)
2. Frontend validates input (password length, confirmation match)
3. POST request to `/api/auth/register` with user data
4. Backend hashes password with BCrypt
5. JWT token generated and returned with user data
6. Frontend stores token in localStorage and redirects to main app

**Login Process:**
1. User enters username and password
2. POST request to `/api/auth/login`
3. Backend validates credentials against database
4. JWT token generated and returned
5. Frontend stores token and redirects to main app

**Protected Access:**
1. All translation requests include JWT in Authorization header
2. JWT filter validates token on each request
3. ProtectedRoute component checks authentication before rendering
4. Automatic logout on token expiration

### 4. Translation Flow

**Text Translation:**
1. User types English text in input field
2. POST request to `/api/translate` with text and JWT token
3. Backend calls Gemini API for translation
4. Response returned with translated text
5. Frontend displays result in chat interface

**Audio Translation:**
1. User records audio using WebRTC
2. Audio converted to Base64 and sent to `/api/audio/translate`
3. Backend processes audio through Gemini API
4. Response includes transcribed and translated text
5. Results displayed in chat interface

### 5. Data Flow Diagram

```
User → React Frontend → Spring Boot Backend → Gemini API
      ← Auth Context ← JWT Token ← Database
      ← Translation Results ← Translation Service

User → Chrome Extension → Spring Boot Backend → Gemini API
      ← Local Storage ← JWT Token ← Database
      ← Translation Results ← Translation Service
```

### 6. Security Architecture

**Authentication:**
- JWT tokens with 24-hour expiration
- BCrypt password hashing
- Token-based stateless authentication

**Authorization:**
- Protected routes (all translation endpoints)
- Public routes (login, register)
- Automatic token validation on each request

**Data Protection:**
- Secure token storage in localStorage
- HTTPS recommended for production
- Input validation on both frontend and backend

### 7. Technology Stack

**Frontend:**
- React.js with functional components
- React Router for client-side routing
- Context API for state management
- CSS-in-JS for styling
- WebRTC for audio recording

**Backend:**
- Spring Boot with Spring Security
- Spring Data JPA with H2 Database
- JWT for authentication
- RestTemplate for external API calls
- Maven for dependency management

**Browser Extension:**
- Chrome Extension Manifest V3
- Service Workers for background processing
- Content Scripts for DOM manipulation
- Side Panel API for UI integration

**External Services:**
- Google Gemini API for translation
- WebRTC for audio recording

### 8. Component Relationships

**Frontend Dependencies:**
- App → AuthContext, Routes
- Login/Register → AuthContext
- ProtectedRoute → AuthContext
- Main App → AuthContext (for token in API calls)

**Backend Dependencies:**
- Controllers → Services
- Services → Repositories, JWT Utils
- Security Config → JWT Filter
- JWT Filter → JWT Utils

**Database Relationships:**
- User entity with unique constraints on username and email
- No complex relationships (single entity system)

### 9. API Endpoints

**Public Endpoints:**
- `POST /api/auth/register` - User registration
- `POST /api/auth/login` - User login

**Protected Endpoints:**
- `POST /api/translate` - Text translation
- `POST /api/audio/translate` - Audio translation
- `POST /api/audio/upload` - Audio file upload

### 10. Error Handling

**Frontend:**
- Form validation with user feedback
- Network error handling with user messages
- Loading states during API calls

**Backend:**
- Global exception handling
- Input validation with appropriate error responses
- JWT token validation errors

This architecture provides a secure, scalable, and user-friendly translation application with modern authentication practices.