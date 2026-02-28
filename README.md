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

---

# 📚 Question System Documentation

El sistema de preguntas utiliza un diseño flexible basado en **tipos de pregunta** (`QuestionType`) que definen un **schema de configuración** (`config_schema`). Cada pregunta (`Question`) tiene un tipo asociado y su configuración específica (`QuestionConfig`) debe validar contra ese schema.

## 🏗️ Arquitectura del Sistema

```
QuestionType (1) ──── Question (N)
     │                    │
     │ config_schema      │
     │                    │
     └────────────────────┼────────────┐
                          ↓            │
                    QuestionConfig (1) │
                          │            │
                          │ config     │
                          │ (valida    │
                          │  contra    │
                          │  schema)   │
                          └────────────┘
```

## 🔑 Conceptos Clave

| Entidad | Descripción |
|---------|-------------|
| **QuestionType** | Catálogo de tipos de pregunta. Define el `config_schema` (contrato/estructura esperada) |
| **Question** | La pregunta en sí (texto + tipo) |
| **QuestionConfig** | Configuración específica de una pregunta. El `config` debe validar contra el schema del `QuestionType` |

---

## 📋 QuestionTypes Disponibles (Seeders)

Al iniciar la aplicación, se crean automáticamente 2 tipos de pregunta:

### 1️⃣ MULTIPLE_CHOICE (ID: 1)

Preguntas de selección múltiple con una o más respuestas correctas.

#### **Schema (`config_schema`):**

```json
{
  "type": "object",
  "required": ["options", "max_selection"],
  "properties": {
    "options": {
      "type": "array",
      "minItems": 2,
      "items": {
        "type": "object",
        "required": ["id", "text", "is_correct"],
        "properties": {
          "id": { "type": "integer" },
          "text": { "type": "string", "minLength": 1 },
          "is_correct": { "type": "boolean" }
        },
        "additionalProperties": false
      }
    },
    "max_selection": {
      "type": "integer",
      "minimum": 1
    },
    "shuffle_options": {
      "type": "boolean"
    }
  },
  "additionalProperties": false
}
```

#### **Campos requeridos en `config`:**

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `options` | Array | Lista de opciones (mínimo 2) |
| `options[].id` | Integer | Identificador único de la opción |
| `options[].text` | String | Texto visible de la opción |
| `options[].is_correct` | Boolean | Indica si es la respuesta correcta |
| `max_selection` | Integer | Cantidad máxima de opciones seleccionables |

#### **Campos opcionales:**

| Campo | Tipo | Default | Descripción |
|-------|------|---------|-------------|
| `shuffle_options` | Boolean | false | Si es `true`, las opciones se mezclan aleatoriamente |

#### **Ejemplo de `config` VÁLIDO:**

```json
{
  "questionId": 1,
  "config": {
    "options": [
      {
        "id": 1,
        "text": "Java",
        "is_correct": false
      },
      {
        "id": 2,
        "text": "Python",
        "is_correct": true
      },
      {
        "id": 3,
        "text": "JavaScript",
        "is_correct": false
      }
    ],
    "max_selection": 1,
    "shuffle_options": true
  }
}
```

#### **Ejemplo de `config` INVÁLIDO (causa error 400):**

```json
{
  "questionId": 1,
  "config": {
    "options": [
      {
        "id": 1,
        "text": "Java"
      }
    ],
    "max_selection": "uno"
  }
}
```

**Errores:**
- ❌ `options[0]` falta `is_correct` (requerido)
- ❌ `max_selection` es string `"uno"` en lugar de integer
- ❌ Solo hay 1 opción (mínimo requerido: 2)

---

### 2️⃣ BLANK_SPACES (ID: 2)

Preguntas de completar espacios en blanco con múltiples respuestas aceptables.

#### **Schema (`config_schema`):**

```json
{
  "type": "object",
  "required": ["text", "blanks"],
  "properties": {
    "text": {
      "type": "string"
    },
    "blanks": {
      "type": "array",
      "minItems": 1,
      "items": {
        "type": "object",
        "required": ["placeholder", "correct_answers"],
        "properties": {
          "placeholder": { "type": "string" },
          "correct_answers": {
            "type": "array",
            "minItems": 1,
            "items": { "type": "string", "minLength": 1 }
          },
          "case_sensitive": {
            "type": "boolean",
            "default": false
          }
        },
        "additionalProperties": false
      }
    }
  },
  "additionalProperties": false
}
```

#### **Campos requeridos en `config`:**

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `text` | String | Texto completo de la pregunta con espacios en blanco |
| `blanks` | Array | Lista de espacios a completar (mínimo 1) |
| `blanks[].placeholder` | String | Identificador del espacio (ej: `blank1`) |
| `blanks[].correct_answers` | Array | Lista de respuestas aceptables (mínimo 1) |

#### **Campos opcionales:**

| Campo | Tipo | Default | Descripción |
|-------|------|---------|-------------|
| `blanks[].case_sensitive` | Boolean | false | Si es `true`, distingue mayúsculas/minúsculas |

#### **Ejemplo de `config` VÁLIDO:**

```json
{
  "questionId": 2,
  "config": {
    "text": "El perro ___ rápidamente por el parque.",
    "blanks": [
      {
        "placeholder": "blank1",
        "correct_answers": ["corre", "corriendo"],
        "case_sensitive": false
      }
    ]
  }
}
```

#### **Ejemplo de `config` INVÁLIDO (causa error 400):**

```json
{
  "questionId": 2,
  "config": {
    "text": "El cielo es ___",
    "blanks": []
  }
}
```

**Errores:**
- ❌ `blanks` está vacío (mínimo requerido: 1)

---

## 🔧 Endpoints de la API

### QuestionTypes

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/question_types` | Obtener todos los tipos disponibles |
| `GET` | `/question_types/{id}` | Obtener un tipo por ID |

### Questions

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/questions` | Obtener todas las preguntas |
| `GET` | `/questions/{id}` | Obtener una pregunta por ID |
| `POST` | `/questions` | Crear una nueva pregunta |
| `PATCH` | `/questions/{id}` | Actualizar una pregunta |
| `DELETE` | `/questions/{id}` | Eliminar una pregunta |

### QuestionConfigs

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/question_configs` | Obtener todas las configuraciones |
| `GET` | `/question_configs/{id}` | Obtener una configuración por ID |
| `POST` | `/question_configs` | **Crear configuración (valida contra schema)** |
| `PATCH` | `/question_configs/{id}` | **Actualizar configuración (valida contra schema)** |
| `DELETE` | `/question_configs/{id}` | Eliminar una configuración |

---

## 📝 Ejemplos de Uso con CURL

### 1. Crear una pregunta MULTIPLE_CHOICE

```bash
curl -X POST http://localhost:8080/questions \
  -H "Content-Type: application/json" \
  -d '{
    "text": "¿Cuál de los siguientes es un lenguaje orientado a objetos?",
    "questionTypeId": 1
  }'
```

**Respuesta:**
```json
{
  "id": 1,
  "text": "¿Cuál de los siguientes es un lenguaje orientado a objetos?",
  "questionTypeId": 1
}
```

### 2. Crear config para MULTIPLE_CHOICE

```bash
curl -X POST http://localhost:8080/question_configs \
  -H "Content-Type: application/json" \
  -d '{
    "questionId": 1,
    "config": {
      "options": [
        {"id": 1, "text": "Java", "is_correct": false},
        {"id": 2, "text": "Python", "is_correct": true},
        {"id": 3, "text": "JavaScript", "is_correct": false}
      ],
      "max_selection": 1,
      "shuffle_options": true
    }
  }'
```

**Respuesta (201 Created):**
```json
{
  "questionId": 1,
  "config": {
    "options": [...],
    "max_selection": 1,
    "shuffle_options": true
  }
}
```

### 3. Crear una pregunta BLANK_SPACES

```bash
curl -X POST http://localhost:8080/questions \
  -H "Content-Type: application/json" \
  -d '{
    "text": "El perro ___ rápidamente por el parque.",
    "questionTypeId": 2
  }'
```

### 4. Crear config para BLANK_SPACES

```bash
curl -X POST http://localhost:8080/question_configs \
  -H "Content-Type: application/json" \
  -d '{
    "questionId": 2,
    "config": {
      "text": "El perro ___ rápidamente por el parque.",
      "blanks": [
        {
          "placeholder": "blank1",
          "correct_answers": ["corre", "corriendo"],
          "case_sensitive": false
        }
      ]
    }
  }'
```

---

## ⚠️ Validación de Schema

Al crear o actualizar un `QuestionConfig`, el sistema:

1. **Obtiene el `QuestionType`** asociado a la pregunta
2. **Recupera el `config_schema`** definido para ese tipo
3. **Valida el `config`** enviado contra ese schema usando JSON Schema Validator
4. **Si es válido** → Guarda la configuración
5. **Si no es válido** → Lanza excepción `InvalidConfigSchemaException` con HTTP 400

### Respuesta de Error (400 Bad Request)

```json
{
  "timestamp": "2026-02-28T14:30:00.000+00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "El config no cumple con el schema del QuestionType: $.options[0]: required property 'is_correct' not found; $.max_selection: expected type 'integer', found 'string'",
  "path": "/question_configs"
}
```

---

## 🎯 Cómo Agregar Nuevos QuestionTypes

Para agregar un nuevo tipo de pregunta (ej: `TRUE_FALSE`):

### 1. Definir el schema

```json
{
  "type": "object",
  "required": ["correct_answer"],
  "properties": {
    "correct_answer": { "type": "boolean" },
    "explanation": { "type": "string" }
  },
  "additionalProperties": false
}
```

### 2. Insertar en la BD (o agregar al seeder)

```java
QuestionType trueFalseType = QuestionType.builder()
    .name("TRUE_FALSE")
    .config_schema(schema)
    .build();
questionTypeRepository.save(trueFalseType);
```

### 3. Usar el nuevo tipo

```bash
curl -X POST http://localhost:8080/questions \
  -H "Content-Type: application/json" \
  -d '{
    "text": "Java es un lenguaje interpretado.",
    "questionTypeId": 3
  }'
```

```bash
curl -X POST http://localhost:8080/question_configs \
  -H "Content-Type: application/json" \
  -d '{
    "questionId": 3,
    "config": {
      "correct_answer": false,
      "explanation": "Java es un lenguaje compilado a bytecode que se ejecuta en la JVM."
    }
  }'
```

---

## 🧪 Tests

Los tests del sistema están en:
- `src/test/java/com/saas/spring/questionConfig/QuestionConfigControllerTest.java`

Para ejecutarlos:
```bash
./mvnw test -Dtest=QuestionConfigControllerTest
```
