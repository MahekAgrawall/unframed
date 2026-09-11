# Unframed

Unframed is a full-stack art-sharing platform where artists can showcase their work, discover other artists, and interact with the creative community.

The project is being built as a full-stack application with a Java/Spring Boot backend and a modern web frontend.

## 🚧 Project Status

**In Development**

### Current progress

- User registration and validation
- BCrypt password hashing
- JWT authentication
- Protected API endpoints
- User profile retrieval
- User profile updates
- User account deletion
- Artwork creation
- Artwork retrieval
- Artwork update and deletion
- PostgreSQL persistence

### Planned features

- Artist profiles
- Artwork feed and discovery
- Likes
- Comments
- Following system
- Search
- Image upload and storage
- Frontend application
- Responsive UI
- Deployment

## 🛠️ Tech Stack

### Backend

- Java 17
- Spring Boot
- Spring Security
- JWT
- Spring Data JPA
- Hibernate
- PostgreSQL
- Maven

### Frontend

- React
- Modern CSS/UI
- REST API integration

## 🏗️ Backend Architecture

The backend follows a layered architecture:

```text
Controller
    ↓
Service
    ↓
Repository
    ↓
PostgreSQL

Authentication uses:

Client
  ↓
Login
  ↓
JWT
  ↓
Authorization: Bearer <token>
  ↓
Spring Security
  ↓
Protected API
🔐 Security
Passwords are hashed using BCrypt.
JWT is used for stateless authentication.
Protected endpoints require a valid Bearer token.
Users can only modify their own profiles and artworks.
Database credentials and JWT secrets are kept outside version control.
📁 Project Structure
src/
└── main/
    └── java/
        └── com/
            └── unframed/
                ├── config/
                ├── controller/
                ├── dto/
                ├── entity/
                ├── exception/
                ├── repository/
                └── service/
🚀 Running Locally
Prerequisites
Java 17
Maven
PostgreSQL

Create a PostgreSQL database named:

unframed_db

Configure your local database credentials and JWT secret in:

src/main/resources/application.properties

This file is intentionally excluded from Git.

📌 API
Authentication
POST /api/users/register
POST /api/users/login
User
GET    /api/users/me
PUT    /api/users/me
DELETE /api/users/me
Artwork
POST   /api/artworks
GET    /api/artworks
GET    /api/artworks/{id}
PUT    /api/artworks/{id}
DELETE /api/artworks/{id}
📄 License

This project is currently being developed as a portfolio project.