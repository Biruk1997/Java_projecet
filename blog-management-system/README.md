# Blog Management System - Java Spring Boot

A complete enterprise-level blog platform converted from MERN stack to Java Spring Boot with Thymeleaf frontend.

## 🚀 Technology Stack

### Backend
- **Java 17**
- **Spring Boot 3.2.5**
- **Spring Security** with JWT Authentication
- **Spring Data JPA** with Hibernate
- **PostgreSQL** Database
- **Flyway** for database migrations
- **Maven** for dependency management

### Frontend
- **Thymeleaf** template engine
- **Bootstrap 5** for UI components
- **JavaScript** for interactivity

### Additional Tools
- **Swagger/OpenAPI** for API documentation
- **Lombok** for reducing boilerplate code
- **MapStruct** for DTO mapping
- **Docker** for containerization

## 📁 Project Structure

```
blog-management-system/
├── src/
│   ├── main/
│   │   ├── java/com/blogapp/
│   │   │   ├── config/          # Configuration classes
│   │   │   ├── controller/      # REST Controllers
│   │   │   ├── dto/             # Data Transfer Objects
│   │   │   ├── entity/          # JPA Entities
│   │   │   ├── exception/       # Exception handling
│   │   │   ├── repository/      # JPA Repositories
│   │   │   ├── security/        # Security & JWT
│   │   │   ├── service/         # Business logic
│   │   │   └── BlogApplication.java
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── db/migration/    # Flyway migrations
│   │       ├── static/          # CSS, JS, images
│   │       └── templates/       # Thymeleaf templates
│   └── test/                    # Unit & Integration tests
├── pom.xml
├── Dockerfile
├── docker-compose.yml
└── README.md
```

## 🔧 Prerequisites

- Java 17 or higher
- Maven 3.6+
- PostgreSQL 12+ (or use Docker)
- Docker & Docker Compose (optional)

## 🛠️ Setup Instructions

### Option 1: Local Setup

1. **Clone the repository**
```bash
git clone <repository-url>
cd blog-management-system
```

2. **Configure PostgreSQL**
```bash
# Create database
createdb blogdb

# Or using psql
psql -U postgres
CREATE DATABASE blogdb;
```

3. **Update application.properties**
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/blogdb
spring.datasource.username=your_username
spring.datasource.password=your_password
jwt.secret=your-256-bit-secret-key-change-this
```

4. **Build the project**
```bash
mvn clean install
```

5. **Run the application**
```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

### Option 2: Docker Setup

1. **Build and run with Docker Compose**
```bash
docker-compose up --build
```

This will:
- Start PostgreSQL container
- Build and start the Spring Boot application
- Expose the app on `http://localhost:8080`

2. **Stop the containers**
```bash
docker-compose down
```

## 📊 Database Migration

The application uses Flyway for database migrations. Migrations run automatically on startup.

Migration files are located in: `src/main/resources/db/migration/`

To create a new migration:
1. Create a new file: `V{version}__Description.sql`
2. Add your SQL statements
3. Restart the application

## 🔐 Authentication

The application uses JWT (JSON Web Tokens) for authentication.

### Signup
```bash
POST /api/auth/signup
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "password123",
  "username": "johndoe",
  "displayName": "John Doe"
}
```

### Login
```bash
POST /api/auth/login
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "password123"
}
```

Response includes JWT token:
```json
{
  "success": true,
  "message": "Login successful",
  "data": {
    "user": { ... },
    "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
  }
}
```

### Using the Token
Include the token in the Authorization header:
```
Authorization: Bearer <your-jwt-token>
```

## 📚 API Documentation

### Swagger UI
Access interactive API documentation at:
```
http://localhost:8080/swagger-ui.html
```

### OpenAPI JSON
```
http://localhost:8080/api-docs
```

## 🔑 Key API Endpoints

### Authentication
- `POST /api/auth/signup` - Create account
- `POST /api/auth/login` - Login

### Posts
- `GET /api/posts` - Get all published posts
- `GET /api/posts/{id}` - Get single post
- `POST /api/posts` - Create post (auth required)
- `PUT /api/posts/{id}` - Update post (auth required)
- `DELETE /api/posts/{id}` - Delete post (auth required)
- `POST /api/posts/{id}/publish` - Publish draft (auth required)
- `GET /api/posts/search?q=keyword` - Search posts
- `GET /api/posts/user/drafts` - Get user drafts (auth required)

### Comments
- `GET /api/comments/{postId}` - Get comments for post
- `POST /api/comments/{postId}` - Add comment (auth required)
- `PUT /api/comments/{id}` - Update comment (auth required)
- `DELETE /api/comments/{id}` - Delete comment (auth required)

### Claps (Likes)
- `POST /api/claps/{postId}` - Add clap (auth required)
- `DELETE /api/claps/{postId}` - Remove clap (auth required)
- `GET /api/claps/{postId}/count` - Get claps count
- `GET /api/claps/{postId}/user` - Check if user clapped (auth required)

### Bookmarks
- `POST /api/bookmarks/{postId}` - Bookmark post (auth required)
- `DELETE /api/bookmarks/{postId}` - Remove bookmark (auth required)
- `GET /api/bookmarks` - Get user bookmarks (auth required)
- `GET /api/bookmarks/{postId}/check` - Check bookmark status (auth required)

### Follow
- `POST /api/follow/users/{userId}` - Follow user (auth required)
- `DELETE /api/follow/users/{userId}` - Unfollow user (auth required)
- `GET /api/follow/users/{userId}/followers` - Get followers
- `GET /api/follow/users/{userId}/following` - Get following
- `POST /api/follow/topics/{topicId}` - Follow topic (auth required)
- `DELETE /api/follow/topics/{topicId}` - Unfollow topic (auth required)

### Topics
- `GET /api/topics` - Get all topics
- `GET /api/topics/trending` - Get trending topics
- `GET /api/topics/{slug}` - Get topic by slug
- `GET /api/topics/{id}/posts` - Get posts by topic
- `POST /api/topics` - Create topic (auth required)

### Users
- `GET /api/users/{id}` - Get user profile
- `GET /api/users/username/{username}` - Get user by username
- `GET /api/users/{id}/posts` - Get user posts
- `PUT /api/users/{id}` - Update profile (auth required)
- `PUT /api/users/{id}/password` - Change password (auth required)

### Notifications
- `GET /api/notifications` - Get notifications (auth required)
- `GET /api/notifications/unread-count` - Get unread count (auth required)
- `PUT /api/notifications/{id}/read` - Mark as read (auth required)
- `PUT /api/notifications/read-all` - Mark all as read (auth required)

## 🧪 Testing

### Run all tests
```bash
mvn test
```

### Run specific test class
```bash
mvn test -Dtest=AuthServiceTest
```

### Run with coverage
```bash
mvn clean test jacoco:report
```

## 🚀 Deployment

### Production Configuration

1. **Update application-prod.properties**
```properties
spring.datasource.url=${DATABASE_URL}
spring.datasource.username=${DATABASE_USERNAME}
spring.datasource.password=${DATABASE_PASSWORD}
jwt.secret=${JWT_SECRET}
spring.jpa.show-sql=false
spring.thymeleaf.cache=true
```

2. **Build production JAR**
```bash
mvn clean package -DskipTests
```

3. **Run with production profile**
```bash
java -jar target/blog-management-system-1.0.0.jar --spring.profiles.active=prod
```

### Docker Deployment

```bash
# Build image
docker build -t blog-management-system:1.0.0 .

# Run container
docker run -d \
  -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://host:5432/blogdb \
  -e SPRING_DATASOURCE_USERNAME=postgres \
  -e SPRING_DATASOURCE_PASSWORD=password \
  -e JWT_SECRET=your-secret-key \
  blog-management-system:1.0.0
```

## 🔄 Migration from MERN Stack

### Key Changes

| MERN Component | Java Equivalent |
|----------------|-----------------|
| Node.js/Express | Spring Boot |
| MongoDB | PostgreSQL |
| Mongoose | Spring Data JPA/Hibernate |
| JWT (jsonwebtoken) | JJWT Library |
| bcrypt | BCryptPasswordEncoder |
| Express middleware | Spring Security Filters |
| React Components | Thymeleaf Templates |
| React Router | Spring MVC |
| Axios/Fetch | RestTemplate/WebClient |

### Data Migration Steps

1. **Export data from MongoDB**
```bash
mongoexport --db=blogdb --collection=users --out=users.json
mongoexport --db=blogdb --collection=posts --out=posts.json
```

2. **Transform and import to PostgreSQL**
- Use provided migration scripts in `/migration-scripts/`
- Or manually transform JSON to SQL INSERT statements

3. **Verify data integrity**
```bash
psql -U postgres -d blogdb -c "SELECT COUNT(*) FROM users;"
psql -U postgres -d blogdb -c "SELECT COUNT(*) FROM posts;"
```

## 🛡️ Security Features

- JWT-based authentication
- Password encryption with BCrypt
- CORS configuration
- SQL injection prevention (JPA/Hibernate)
- XSS protection
- CSRF protection (for form submissions)
- Role-based access control (USER, ADMIN)
- Input validation

## 📈 Performance Optimization

- Database indexing on frequently queried columns
- Lazy loading for entity relationships
- Connection pooling (HikariCP)
- Query optimization with JPA
- Caching with Spring Cache (optional)

## 🐛 Troubleshooting

### Database Connection Issues
```bash
# Check PostgreSQL is running
pg_isready -h localhost -p 5432

# Check connection
psql -U postgres -d blogdb
```

### Port Already in Use
```bash
# Find process using port 8080
lsof -i :8080

# Kill the process
kill -9 <PID>
```

### Flyway Migration Errors
```bash
# Repair Flyway schema
mvn flyway:repair

# Clean and re-run migrations (WARNING: deletes all data)
mvn flyway:clean flyway:migrate
```

## 📝 License

This project is licensed under the MIT License.

## 👥 Contributors

- Development Team

## 📞 Support

For issues and questions:
- Create an issue on GitHub
- Email: support@blogapp.com

## 🎯 Future Enhancements

- [ ] OAuth2 integration (Google, GitHub)
- [ ] Email verification
- [ ] Password reset functionality
- [ ] Rich text editor integration
- [ ] Image upload to cloud storage
- [ ] Real-time notifications with WebSocket
- [ ] Full-text search with Elasticsearch
- [ ] Redis caching
- [ ] Rate limiting
- [ ] Admin dashboard
- [ ] Analytics and reporting
