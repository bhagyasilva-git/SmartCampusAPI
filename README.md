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
- [📘 Report Questions](#-report-questions)
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

clean and build 

**3. Deploy to Tomcat**

start Tomcat.

**4. Access the API**

Run the project

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

## 📘 Report Questions

This section contains the written answers to the conceptual questions specified in each part of the coursework brief. It is intentionally limited to conceptual explanations only, as required.

Part 1: Service Architecture & Setup
Q1. Default lifecycle of a JAX-RS Resource class and its impact on in-memory data management

In this coursework, JAX-RS resource classes use the default per-request lifecycle, meaning a new instance of the resource class is created for every incoming HTTP request. Resource classes are not treated as singletons unless explicitly annotated.

This lifecycle improves thread safety at the resource-instance level because instance variables are not shared across concurrent requests. However, the Smart Campus API stores Rooms, Sensors, and Sensor Readings in shared in-memory collections (such as HashMap and ArrayList) so that data persists across requests.

Because these collections are shared by multiple request threads, care must be taken to avoid race conditions and inconsistent state. Therefore, all operations that modify shared data (such as deleting rooms or assigning sensors) are validated and controlled to ensure data integrity. This demonstrates the importance of careful in-memory data management when using per-request JAX-RS resources.

Q2. Why Hypermedia (HATEOAS) is a hallmark of advanced RESTful design

Hypermedia (HATEOAS) is considered a hallmark of advanced RESTful design because it enables clients to discover available resources and actions dynamically through links embedded within API responses.

In this coursework, the Discovery endpoint (GET /api/v1) provides metadata and links to the primary resource collections, such as Rooms and Sensors. This allows clients to navigate the API without relying on static documentation.

Compared to static documentation, HATEOAS:

Reduces tight coupling between client and server
Allows the API to evolve without breaking clients
Makes workflows self-descriptive and easier to understand

This results in a more flexible and maintainable RESTful API.

Part 2: Room Management
Q3. Implications of returning only Room IDs vs full Room objects

Returning only Room IDs reduces response size and network bandwidth usage, which can be beneficial for large datasets. However, it requires clients to make additional requests to retrieve full room details, increasing latency and client-side complexity.

Returning full Room objects increases payload size but allows clients to obtain all required data in a single request, simplifying client logic.

In this coursework, full Room objects are returned to prioritise clarity and ease of use for API consumers.

Q4. Is the DELETE operation idempotent in this implementation?

Yes, the DELETE operation is idempotent.

When a DELETE request is sent for a room:

The first request removes the room if it exists and has no assigned sensors
Subsequent identical DELETE requests do not change the system state further

Although later requests may return a 404 Not Found, the final state of the system remains unchanged. Therefore, the DELETE operation satisfies the REST requirement for idempotency.

Part 3: Sensor Operations & Linking
Q5. Effect of @Consumes(MediaType.APPLICATION_JSON) when receiving other formats

The @Consumes(MediaType.APPLICATION_JSON) annotation explicitly restricts an endpoint to accept only JSON payloads.

If a client sends data in another format (such as text/plain or application/xml), the JAX-RS runtime:

Fails content negotiation
Automatically returns HTTP 415 Unsupported Media Type
Prevents the request from reaching application logic

This ensures strict API contract enforcement and protects the system from invalid input.

Q6. Why query parameters are superior to path parameters for filtering

Query parameters are better suited for filtering because they represent optional constraints applied to an existing collection.

Using GET /api/v1/sensors?type=CO2:

Preserves the identity of the base resource
Allows multiple filters to be combined easily
Scales well for search and filtering use cases

Embedding filters in the URL path incorrectly implies a hierarchical resource structure. Therefore, query parameters align better with REST semantics.

Part 4: Deep Nesting with Sub-Resources
Q7. Benefits of the Sub-Resource Locator pattern

The Sub-Resource Locator pattern delegates responsibility for nested resources to dedicated resource classes.

In this coursework, sensor readings are managed through a separate SensorReadingResource, accessed via a sub-resource locator. This approach:

Improves separation of concerns
Reduces complexity in resource classes
Improves readability and maintainability
Makes the API easier to extend and test

This pattern scales effectively for large APIs with deep resource hierarchies.

Part 5: Advanced Error Handling & Logging
Q8. Why HTTP 422 is more semantically accurate than 404 for missing linked resources

HTTP 422 Unprocessable Entity is appropriate when a request contains valid JSON but references a related resource that does not exist (such as an invalid roomId).

A 404 Not Found implies that the endpoint itself does not exist, which is not the case here. HTTP 422 accurately communicates that the server understood the request but could not process it due to a semantic error in the payload.

Q9. Cybersecurity risks of exposing Java stack traces

Exposing Java stack traces can leak sensitive internal information, including class names, package structures, file paths, and internal logic.

Attackers can use this information to identify vulnerabilities or perform targeted attacks. In this coursework, a global exception mapper ensures that stack traces are never exposed to clients, improving overall API security.

Q10. Why JAX-RS filters are better for logging than manual log statements

JAX-RS filters provide a centralised mechanism for handling cross-cutting concerns such as logging.

Using filters:

Eliminates duplicated logging code
Ensures consistent logging across all endpoints
Improves separation of concerns
Simplifies maintenance and future changes

This results in cleaner resource classes and a more professional API design.

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
