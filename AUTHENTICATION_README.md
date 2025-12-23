# Authentication Implementation for Darija Translator

This document describes the authentication system implemented for the Darija Translator application.

## Overview

The application now requires users to create an account and log in before accessing the translation functionality. The authentication system uses JWT (JSON Web Tokens) for secure stateless authentication.

## Backend Implementation (Spring Boot)

### Dependencies Added
- Spring Security
- JWT (jjwt-api, jjwt-impl, jjwt-jackson)
- Spring Data JPA
- H2 Database (for development)

### Key Components

1. **User Entity** (`src/main/java/org/mql/genai/darijaatranslator/models/User.java`)
   - Stores user information (username, email, password)
   - Uses BCrypt for password hashing

2. **User Repository** (`src/main/java/org/mql/genai/darijaatranslator/repository/UserRepository.java`)
   - JPA repository for database operations
   - Provides methods to find users by username/email

3. **Authentication Service** (`src/main/java/org/mql/genai/darijaatranslator/service/AuthenticationService.java`)
   - Handles user registration and login
   - Generates JWT tokens upon successful authentication

4. **JWT Utilities** (`src/main/java/org/mql/genai/darijaatranslator/config/JwtUtil.java`)
   - Token generation and validation
   - Token expiration handling

5. **Security Configuration** (`src/main/java/org/mql/genai/darijaatranslator/config/SecurityConfig.java`)
   - Configures Spring Security
   - Sets up JWT authentication filter
   - Defines protected endpoints

6. **Authentication Controller** (`src/main/java/org/mql/genai/darijaatranslator/controller/AuthController.java`)
   - `/api/auth/register` - User registration
   - `/api/auth/login` - User login

### Protected Endpoints
- All `/api/translate/*` endpoints now require authentication
- Authentication is done via Bearer token in the Authorization header

## Frontend Implementation (React)

### Authentication Context
- **AuthContext** (`src/context/AuthContext.js`) - Manages authentication state
- Provides login, register, logout functionality
- Stores token and user data in localStorage

### Components
1. **Login Component** (`src/components/Login.js`)
   - Login form with username/password
   - Error handling and loading states

2. **Register Component** (`src/components/Register.js`)
   - Registration form with validation
   - Password confirmation check

3. **ProtectedRoute Component** (`src/components/ProtectedRoute.js`)
   - Wraps protected routes
   - Redirects to login if not authenticated

4. **Logout Component** (`src/components/Logout.js`)
   - Logout button with confirmation

### Routing
- `/login` - Login page (public)
- `/register` - Registration page (public)
- `/` - Main application (protected)

### API Integration
- All translation requests now include JWT token in Authorization header
- Automatic token management and storage

## Usage

### 1. User Registration
1. Navigate to `/register`
2. Fill in username, email, and password
3. Password must be at least 6 characters
4. Confirm password must match

### 2. User Login
1. Navigate to `/login`
2. Enter username and password
3. On successful login, user is redirected to main application

### 3. Using the Application
1. After login, the main translation interface is accessible
2. All translation requests are automatically authenticated
3. User can logout using the logout button in the header

### 4. Development Setup
1. Start the Spring Boot backend on port 8080
2. Start the React frontend on port 3000
3. The frontend is configured to communicate with the backend at `http://localhost:8080`

## Security Features

1. **Password Hashing**: Passwords are hashed using BCrypt before storage
2. **JWT Tokens**: Stateless authentication with configurable expiration
3. **Protected Routes**: Frontend routes are protected client-side
4. **Token Storage**: Tokens are securely stored in localStorage
5. **Input Validation**: Frontend and backend validation for user inputs

## Database Configuration

The application uses H2 database for development. To switch to a production database:

1. Add the appropriate database dependency in `pom.xml`
2. Update `application.properties` with database connection details
3. Remove H2 dependency if not needed

## Testing the Authentication Flow

1. **Register a new user**:
   ```
   POST /api/auth/register
   {
     "username": "testuser",
     "email": "test@example.com",
     "password": "password123"
   }
   ```

2. **Login**:
   ```
   POST /api/auth/login
   {
     "username": "testuser",
     "password": "password123"
   }
   ```

3. **Make authenticated requests**:
   ```
   POST /api/translate
   Authorization: Bearer <your-jwt-token>
   {
     "englishText": "Hello"
   }
   ```

## Future Enhancements

1. Password reset functionality
2. Email verification
3. Role-based access control
4. Refresh token mechanism
5. Account lockout after failed attempts
6. Two-factor authentication