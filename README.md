# Clean Architecture Project with Spring Boot

This project follows Clean Architecture principles to ensure separation of concerns, maintainability, and testability. The architecture is organized into distinct layers, each with specific responsibilities.

## Folder Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── saas/
│   │           └── cleanarchitecture/
│   │               ├── application/          # Application layer (use cases)
│   │               │   ├── config/           # Configuration classes
│   │               │   ├── service/          # Application services
│   │               │   ├── controller/       # REST controllers
│   │               │   ├── model/            # DTOs (Data Transfer Objects)
│   │               │   ├── exception/        # Application exceptions
│   │               │   └── util/             # Utility classes
│   │               ├── domain/              # Domain layer (business logic)
│   │               │   ├── model/           # Entity classes
│   │               │   ├── repository/      # Repository interfaces
│   │               │   ├── service/         # Domain services
│   │               │   ├── exception/       # Domain exceptions
│   │               │   └── util/            # Domain utilities
│   │               ├── infrastructure/      # Infrastructure layer (external concerns)
│   │               │   ├── config/          # Infrastructure configurations
│   │               │   ├── repository/      # Repository implementations
│   │               │   ├── controller/      # Infrastructure controllers
│   │               │   ├── model/           # Infrastructure models
│   │               │   ├── exception/       # Infrastructure exceptions
│   │               │   └── util/            # Infrastructure utilities
│   │               └── presentation/        # Presentation layer (UI concerns)
│   │                   ├── config/          # Presentation configurations
│   │                   ├── controller/      # Presentation controllers
│   │                   ├── model/           # Presentation models
│   │                   ├── exception/       # Presentation exceptions
│   │                   └── util/            # Presentation utilities
│   └── resources/
│       ├── application/                     # Application layer resources
│       ├── domain/                          # Domain layer resources
│       ├── infrastructure/                  # Infrastructure layer resources
│       └── presentation/                    # Presentation layer resources
└── test/
    └── java/
        └── com/
            └── saas/
                └── cleanarchitecture/
                    ├── application/         # Application layer tests
                    ├── domain/              # Domain layer tests
                    ├── infrastructure/      # Infrastructure layer tests
                    └── presentation/        # Presentation layer tests
```

## Layer Descriptions

### Domain Layer
The domain layer contains the business logic and entities. This is the core of the application and should be independent of any external frameworks or libraries. It includes:

- **Entities**: Business objects that represent the core concepts of the application
- **Repositories**: Interfaces defining data access contracts (not implementations)
- **Domain Services**: Business logic that doesn't fit naturally in an entity
- **Exceptions**: Domain-specific exceptions

### Application Layer
The application layer orchestrates the use cases of the application. It depends on the domain layer but not on the infrastructure layer. It includes:

- **Use Cases**: Application-specific business rules
- **Application Services**: Services that coordinate between domain and infrastructure
- **Controllers**: REST endpoints that expose application functionality
- **DTOs**: Data Transfer Objects for communication between layers

### Infrastructure Layer
The infrastructure layer implements the interfaces defined in the domain layer and handles external concerns. It includes:

- **Repository Implementations**: Concrete implementations of repository interfaces
- **External Service Integrations**: Third-party API integrations
- **Database Configurations**: JPA/Hibernate configurations
- **Message Queue Implementations**: Event handling and messaging

### Presentation Layer
The presentation layer handles user interface concerns and HTTP request/response processing. It includes:

- **Controllers**: Handle HTTP requests and responses
- **View Models**: Objects specifically designed for UI representation
- **Validation Logic**: Input validation and sanitization

## Typical File Locations

### Controllers
Controllers are typically located in the `presentation` or `application` layer depending on whether they handle pure presentation logic or application orchestration.

### Services
- **Domain Services**: Located in the `domain/service` package
- **Application Services**: Located in the `application/service` package
- **Infrastructure Services**: Located in the `infrastructure/service` package

### Repositories
- **Interfaces**: Located in the `domain/repository` package
- **Implementations**: Located in the `infrastructure/repository` package

### Models/Entities
- **Domain Entities**: Located in the `domain/model` package
- **DTOs**: Located in the `application/model` package
- **Infrastructure Models**: Located in the `infrastructure/model` package

## Testing Strategy

Testing is organized by layer to ensure proper isolation and coverage:

### Domain Layer Tests
Located in `src/test/java/com/saas/cleanarchitecture/domain/`
- Unit tests for business logic
- Entity validation tests
- Domain service tests
- Repository interface contract tests

### Application Layer Tests
Located in `src/test/java/com/saas/cleanarchitecture/application/`
- Integration tests for use cases
- Controller tests (without infrastructure dependencies)
- Service orchestration tests
- DTO mapping tests

### Infrastructure Layer Tests
Located in `src/test/java/com/saas/cleanarchitecture/infrastructure/`
- Repository implementation tests (with database)
- External service integration tests
- Configuration tests
- Database migration tests

### Presentation Layer Tests
Located in `src/test/java/com/saas/cleanarchitecture/presentation/`
- Controller integration tests
- API endpoint tests
- Request/response validation tests
- Security tests

## Benefits of Clean Architecture

1. **Independence of Frameworks**: The business logic is not dependent on external frameworks
2. **Testability**: Business logic can be tested without external dependencies
3. **Independence of UI**: The UI can change without affecting business logic
4. **Independence of Database**: The database can change without affecting business logic
5. **Independence of External Agencies**: Business rules are not affected by external agencies

## Project Idea: Learning Platform

This project aims to develop a comprehensive learning platform inspired by applications like Duolingo and Moodle. The platform will serve as an educational ecosystem where users can engage with various learning materials and interact with others in an academic environment.

### Main Features

#### Educational Content Management
- **Exams and Quizzes**: Creation and management of various types of assessments
- **Courses**: Structured learning paths with modules and lessons
- **Scores Tracking**: Detailed analytics of user performance and progress
- **Questionnaires**: Interactive surveys and knowledge checks

#### User Engagement
- **Learning Streaks**: Track consecutive days of learning activity to encourage consistent study habits
- **Social Features**: User friendships and social connections to enhance motivation
- **Progress Visualization**: Charts and statistics showing learning achievements

#### Collaborative Learning
- **Chat Rooms**: Real-time communication spaces for discussions
- **Classrooms**: Virtual rooms for group learning experiences similar to Google Classroom
- **Peer Interaction**: Tools for students to collaborate and learn from each other

### Functional Requirements

#### User Management
- User registration and authentication
- Profile management with learning statistics
- Friend system for connecting with other learners
- Privacy controls for personal information

#### Learning Content
- Course creation and management by instructors
- Exam and quiz creation with various question types
- Progress tracking for individual courses and overall learning
- Score recording and historical performance analysis

#### Social Features
- Friend request system
- Private and group chat functionality
- Classroom creation and management
- Discussion forums for course-related topics

#### Gamification
- Daily streak tracking to encourage regular learning
- Achievement badges for milestones
- Leaderboards for healthy competition
- Progress visualization tools

#### Analytics
- Personal learning analytics dashboard
- Performance tracking over time
- Course completion statistics
- Time spent on different activities

This learning platform will leverage the Clean Architecture principles implemented in this project structure to ensure scalability, maintainability, and testability as features are added and the user base grows.
