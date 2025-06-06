# TaskService API

A modern Spring Boot REST API for task management with LRU caching and comprehensive documentation.

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://openjdk.java.net/projects/jdk/21/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.0-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Maven](https://img.shields.io/badge/Maven-3.6+-blue.svg)](https://maven.apache.org/)
[![Docker](https://img.shields.io/badge/Docker-Enabled-blue.svg)](https://www.docker.com/)

## 📋 Overview

TaskService is a high-performance REST API that provides task management capabilities with:

- **LRU Caching**: Caffeine-based caching with 5-item limit for optimal performance
- **JSON File Storage**: Persistent task storage using JSON files
- **Comprehensive Documentation**: OpenAPI 3.1 with Swagger UI
- **Docker Support**: Containerized deployment with modern Docker practices
- **Production Ready**: Health checks, monitoring, and production profiles

## 🏗️ Architecture

```
📁 TaskService/
├── 📁 taskservice/           # Main application directory
│   ├── 📁 src/main/java/
│   │   └── 📁 com/example/taskservice/
│   │       ├── 📁 config/    # Configuration classes
│   │       ├── 📁 controller/ # REST controllers
│   │       ├── 📁 dto/       # Data Transfer Objects
│   │       ├── 📁 entity/    # Domain entities
│   │       ├── 📁 exception/ # Exception handlers
│   │       ├── 📁 mapper/    # Object mappers
│   │       ├── 📁 repository/ # Data access layer
│   │       └── 📁 service/   # Business logic
│   ├── 📁 src/test/          # Test files
│   ├── 📄 Dockerfile         # Container configuration
│   ├── 📄 docker-compose.yml # Docker Compose setup
│   └── 📄 pom.xml           # Maven dependencies
└── 📁 Tasks/                # JSON file storage
    └── 📄 {uuid}.json       # Individual task files
```

## 🛠️ Technology Stack

- **Java 21** - Latest LTS Java version
- **Spring Boot 3.5.0** - Application framework
- **Spring Cache + Caffeine** - High-performance caching
- **Spring Validation** - Input validation
- **Spring Actuator** - Health monitoring
- **OpenAPI 3.1 + Swagger UI** - API documentation
- **Maven** - Dependency management
- **Docker** - Containerization
- **JUnit 5** - Testing framework

## 🚀 Quick Start

### Prerequisites

- **Java 21** or later
- **Maven 3.6+**
- **Docker** (optional, for containerization)

### 1. Clone the Repository

```bash
git clone <repository-url>
cd TaskService/taskservice
```

### 2. Run Locally

```bash
# Using Maven
./mvnw spring-boot:run

# Or build and run JAR
./mvnw clean package
java -jar target/taskservice-0.0.1-SNAPSHOT.jar
```

### 3. Run with Docker

```bash
# Build and start with Docker Compose
docker compose up --build

# Or manually
docker build -t taskservice .
docker run -p 8080:8080 -v $(pwd)/../Tasks:/app/Tasks taskservice
```

## 📡 API Endpoints

### Base URL
- **Local**: `http://localhost:8080`
- **Docker**: `http://localhost:8080`

### Main Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/getTask/{id}` | Retrieve a task by UUID |
| `GET` | `/actuator/health` | Health check endpoint |
| `GET` | `/swagger-ui.html` | Interactive API documentation |

### Example Usage

```bash
# Get a task
curl http://localhost:8080/getTask/123e4567-e89b-12d3-a456-426614174000

# Response
{
  "Id": "123e4567-e89b-12d3-a456-426614174000",
  "description": "Complete project documentation"
}
```

## 📚 API Documentation

### Swagger UI
Visit `http://localhost:8080/swagger-ui.html` for interactive API documentation.

### Features:
- **Try It Out**: Test endpoints directly from the browser
- **Schema Documentation**: Detailed request/response schemas
- **Example Values**: Sample data for all endpoints
- **Error Handling**: Documented error responses

## 🧪 Testing

The application includes comprehensive test coverage:

```bash
# Run all tests
./mvnw test

# Run with coverage
./mvnw test jacoco:report
```

### Test Categories:
- **Unit Tests**: Service and component testing
- **Integration Tests**: Full application stack testing
- **Cache Tests**: LRU cache behavior verification
- **Validation Tests**: UUID and input validation
- **Exception Tests**: Error handling verification

**Test Results**: 38 tests, 100% success rate

## 🔧 Configuration

### Application Profiles

- **Default** (`application.yml`): Development configuration
- **Production** (`application-prod.yml`): Production optimized settings

### Key Configuration Properties

```yaml
# Cache Configuration
cache:
  task:
    maximum-size: 5

# File Storage
task:
  file:
    base-path: ../Tasks  # Development
    base-path: /app/Tasks  # Production
```

# Run with production profile
docker run -d \
  --name taskservice-prod \
  -p 8080:8080 \
  -v /data/tasks:/app/Tasks \
  -e SPRING_PROFILES_ACTIVE=prod \
  taskservice:1.0.0
```

### Generate Test Data
```bash
# Generate sample tasks (if generate_tasks.py exists)
cd ../Tasks
python3 generate_tasks.py
```