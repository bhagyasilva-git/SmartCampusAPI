# 🏛️ Smart Campus API

> A RESTful web service for smart campus management, built with JAX-RS (Jersey) and deployed on Apache Tomcat.

---

## 📋 Table of Contents

- [Overview](#overview)
- [Technology Stack](#technology-stack)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
- [API Reference](#api-reference)
  - [Discovery](#1-discovery-endpoint)
  - [Rooms](#2-room-management)
  - [Sensors](#3-sensor-management)
  - [Sensor Readings](#4-sensor-readings)
- [Exception Handling](#exception-handling)
- [Logging](#logging)
- [Design Decisions](#design-decisions)
- [Academic Context](#academic-context)

---

## Overview

The **Smart Campus API** simulates a smart campus management system, providing endpoints to manage rooms, sensors, and sensor readings. The system is designed around RESTful principles with a focus on clean error handling, structured logging, and in-memory persistence.

**Key capabilities:**

- Room lifecycle management (create, retrieve, delete)
- Sensor management with room association and type-based filtering
- Historical sensor reading storage with automatic current-value updates
- Centralized exception handling with structured JSON error responses
- Request/response logging as a cross-cutting concern

> **Note:** The system uses in-memory data structures (`HashMap`, `ArrayList`) rather than a database, as per coursework requirements. All data is reset on server restart.

---

## Technology Stack

| Layer | Technology |
|---|---|
| Language | Java 8+ |
| Web Framework | JAX-RS (Jersey) |
| Server | Apache Tomcat 9+ |
| Build Tool | Maven |
| Storage | In-memory (`HashMap`, `ArrayList`) |
| Testing | Postman / Browser |

---

## Project Structure

```
com.smartcampus.smartcampus
│
├── resources/                  # JAX-RS Resource classes (REST endpoints)
│   ├── RoomResource
│   ├── SensorResource
│   └── SensorReadingResource
│
├── model/                      # Domain model (POJOs)
│   ├── Room
│   ├── Sensor
│   └── SensorReading
│
├── exception/                  # Custom exceptions & global mapper
│   ├── RoomNotEmptyException
│   ├── LinkedResourceNotFoundException
│   ├── SensorUnavailableException
│   ├── DataNotFoundException
│   └── GlobalExceptionMapper
│
└── filter/                     # Cross-cutting concerns
    └── LoggingFilter
```

---

## Getting Started

### Prerequisites

- Java 8 or higher
- Apache Tomcat 9+
- Maven 3+
- NetBeans IDE (recommended) or any Maven-compatible IDE

### Installation & Deployment

**1. Clone and open the project**

Open the project in NetBeans IDE (or import as a Maven project in your preferred IDE).

**2. Build the project**

```bash
mvn clean install
```

**3. Deploy to Tomcat**

Copy the generated `.war` file from the `target/` directory to your Tomcat `webapps/` folder, then start Tomcat.

**4. Access the API**

```
http://localhost:8080/SmartCampus/api/v1
```

### Testing

- **GET requests** — use any browser or Postman
- **POST / DELETE requests** — use [Postman](https://www.postman.com/)

> ⚠️ **Important:** The in-memory store is empty on startup. Rooms must be created before sensors can be added.

---

## API Reference

**Base URL:** `http://localhost:8080/SmartCampus/api/v1`

---

### 1. Discovery Endpoint

Provides a HATEOAS-style entry point to discover available API resources.

```
GET /api/v1
```

**Response — `200 OK`**

```json
{
  "version": "v1",
  "resources": {
    "rooms": "/api/v1/rooms",
    "sensors": "/api/v1/sensors"
  }
}
```

---

### 2. Room Management

#### Get All Rooms

```
GET /api/v1/rooms
```

**Response — `200 OK`**

```json
[
  {
    "id": "LIB-301",
    "name": "Library Quiet Study",
    "capacity": 120
  }
]
```

---

#### Create a Room

```
POST /api/v1/rooms
```

**Request Body**

```json
{
  "id": "LIB-301",
  "name": "Library Quiet Study",
  "capacity": 120
}
```

**Response — `201 Created`**

---

#### Get Room by ID

```
GET /api/v1/rooms/{id}
```

**Response — `200 OK`**

```json
{
  "id": "LIB-301",
  "name": "Library Quiet Study",
  "capacity": 120
}
```

---

#### Delete a Room

```
DELETE /api/v1/rooms/{id}
```

| Scenario | Status | Response |
|---|---|---|
| Room deleted successfully | `204 No Content` | _(empty body)_ |
| Room has active sensors | `409 Conflict` | JSON error body |

**Error Response — `409 Conflict`**

```json
{
  "error": "RoomNotEmpty",
  "message": "Room ENG-105 cannot be deleted because it has active sensors."
}
```

---

### 3. Sensor Management

#### Get All Sensors

```
GET /api/v1/sensors
```

**Response — `200 OK`**

```json
[
  {
    "id": "TEMP-ENG-01",
    "type": "Temperature",
    "status": "ACTIVE",
    "currentValue": 21.0,
    "roomId": "ENG-101"
  }
]
```

---

#### Filter Sensors by Type

```
GET /api/v1/sensors?type=Temperature
```

Uses `QueryParam`-based filtering for flexible, scalable querying.

---

#### Create a Sensor

```
POST /api/v1/sensors
```

**Request Body**

```json
{
  "id": "TEMP-ENG-01",
  "type": "Temperature",
  "status": "ACTIVE",
  "currentValue": 21.0,
  "roomId": "ENG-101"
}
```

**Response — `201 Created`**

**Error — Room does not exist (`400 Bad Request`)**

```json
{
  "errorMessage": "Room does not exist",
  "errorCode": 400
}
```

---

### 4. Sensor Readings

Sensor readings are modelled as a sub-resource of sensors, keeping reading logic cleanly separated from sensor management.

#### Get All Readings for a Sensor

```
GET /api/v1/sensors/{sensorId}/readings
```

**Response — `200 OK`**

```json
[
  {
    "value": 23.5,
    "timestamp": "2025-03-15T10:30:00Z"
  }
]
```

---

#### Add a Sensor Reading

```
POST /api/v1/sensors/{sensorId}/readings
```

**Request Body**

```json
{
  "value": 23.5
}
```

**Response — `201 Created`**

> ✅ **Side Effect:** Posting a new reading automatically updates the sensor's `currentValue` field.

---

## Exception Handling

All exceptions are handled centrally via JAX-RS `ExceptionMapper`, ensuring consistent, structured JSON error responses without exposing internal stack traces.

| Exception | HTTP Status | Description |
|---|---|---|
| `RoomNotEmptyException` | `409 Conflict` | Room deletion blocked due to active sensors |
| `LinkedResourceNotFoundException` | `400` / `422` | Invalid `roomId` provided during sensor creation |
| `SensorUnavailableException` | `403 Forbidden` | Sensor is currently under maintenance |
| `DataNotFoundException` | `404 Not Found` | Requested resource does not exist |
| `GlobalExceptionMapper` | `500 Internal Server Error` | Catch-all for unexpected runtime errors |

**Standard Error Format**

```json
{
  "errorMessage": "Description of the error",
  "errorCode": 400
}
```

---

## Logging

The `LoggingFilter` implements both `ContainerRequestFilter` and `ContainerResponseFilter` to log all incoming requests and outgoing responses.

**Example Log Output**

```
Incoming Request:
  Method : GET
  URI    : /api/v1/sensors

Outgoing Response:
  Status : 200
```

This provides visibility for debugging and monitoring without requiring a dedicated logging framework.

---

## Design Decisions

| Decision | Rationale |
|---|---|
| **Query Parameters for filtering** | Provides flexible, scalable, and URL-friendly filtering without additional endpoints |
| **Sub-resource pattern for readings** | Cleanly separates reading logic from sensor management, following REST resource hierarchy |
| **Centralized `ExceptionMapper`** | Guarantees uniform error responses and prevents accidental stack trace exposure to clients |
| **Global 500 handler** | Ensures no unexpected exception results in a non-JSON or empty response |
| **In-memory storage** | Keeps the system self-contained and deployable without any database configuration |

---

## Academic Context

| Field | Detail |
|---|---|
| **Module** | Client–Server Architectures |
| **Institution** | University of Westminster (IIT Sri Lanka) |
| **Programme** | BSc (Hons) Computer Science |
| **Academic Year** | 2025 / 2026 — Level 5 |
| **Type** | Individual Coursework |

---

*This project was developed for academic purposes.*
