# SaaS Spring Application

This project is a Spring Boot application with functionality similar to Moodle and Duolingo. The architecture follows clean architecture principles with a focus on modularity and maintainability.

## Folder Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── saas/
│   │           └── spring/
│   │               ├── achievement/        # Achievement module
│   │               │   ├── dto/            # Data Transfer Objects
│   │               │   ├── Achievement.java # Entity class
│   │               │   ├── AchievementController.java
│   │               │   ├── AchievementRepository.java
│   │               │   └── AchievementService.java
│   │               └── Application.java    # Main application class
│   └── resources/
│       └── application.properties          # Application configuration
└── test/
    └── java/
        └── com/
            └── saas/
                └── spring/
                    └── achievement/
                        └── AchievementControllerTest.java # Module tests
```

## Important Maven Wrapper (mvnw) Commands

The project uses the Maven Wrapper (mvnw) for consistent builds across different environments. Here are the most important commands:

### Install Dependencies
```bash
./mvnw install
```
Downloads and installs all project dependencies based on the pom.xml configuration.

### Clean Project
```bash
./mvnw clean
```
Removes the target directory and all generated files, cleaning up the project workspace.

### Run Tests
```bash
./mvnw test
```
Executes all unit and integration tests in the project.

### Run Application
```bash
./mvnw spring-boot:run
```
Starts the Spring Boot application locally for development purposes.

### Package Application
```bash
./mvnw package
```
Compiles the code, runs tests, and packages the application into a JAR file in the target directory.


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
