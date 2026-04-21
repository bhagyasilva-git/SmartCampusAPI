# Smart Campus API (JAX-RS Coursework)

## 📌 Overview

The Smart Campus API is a RESTful web service developed using **JAX-RS (Jersey)** and deployed on **Apache Tomcat**.

It simulates a smart campus management system that handles:

- Room management
- Sensor management
- Sensor readings (historical data)
- Exception handling & validation rules
- Logging and request/response monitoring

The system uses **in-memory data structures (HashMap & ArrayList)** instead of a database, as required by the coursework specification.

---

## 🌐 Base URL


http://localhost:8080/SmartCampus/api/v1


---

## 🏗️ Technology Stack

- Java 8+
- JAX-RS (Jersey)
- Apache Tomcat 9+
- Maven
- In-memory storage:
  - HashMap
  - ArrayList

---

## 📦 Project Architecture



com.smartcampus.smartcampus

│

├── resources

│ ├── RoomResource

│ ├── SensorResource

│ └── SensorReadingResource

│

├── model

│ ├── Room

│ ├── Sensor

│ └── SensorReading

│

├── exception

│ ├── RoomNotEmptyException

│ ├── LinkedResourceNotFoundException

│ ├── SensorUnavailableException

│ ├── DataNotFoundException

│ ├── Exception Mappers (409, 422, 403, 500)

│

└── filter

└── LoggingFilter




---

## 🚀 How to Run the Project

### 1. Open Project
Open the project in **NetBeans IDE**

---

### 2. Clean & Build

mvn clean install


---

### 3. Deploy
Deploy the generated `.war` file to:

- Apache Tomcat 9+

---

### 4. Run Application

http://localhost:8080/SmartCampus/api/v1


---

### 5. Testing

- Use **Postman** for CRUD operations
- Use **Browser** for GET requests only
- IMPORTANT: Database is empty initially → you MUST create rooms before sensors

---

## 📡 API ENDPOINTS

---

# 1.  Discovery Endpoint (HATEOAS)

### GET

/api/v1


### Response
```json
{
  "apiName": "Smart Campus API",
  "version": "v1",
  "contact": "admin@westminster.ac.uk",
  "resources": {
    "rooms": "/api/v1/rooms",
    "sensors": "/api/v1/sensors",
    "readings": "/api/v1/sensors/{sensorId}/readings"
  }
}
2.  Room Management
GET /rooms
[
  {
    "id": "R101",
    "name": "Lecture Hall",
    "capacity": 120,
    "sensorIds": ["S1"]
  }
]
POST /rooms
{
  "id": "R101",
  "name": "Lecture Hall",
  "capacity": 120,
  "sensorIds": []
}
Response
201 Created
GET /rooms/{id}
{
  "id": "R101",
  "name": "Lecture Hall",
  "capacity": 120,
  "sensorIds": ["S1"]
}
DELETE /rooms/{id}

 Success:

204 No Content

 If room has sensors:

{
  "errorMessage": "Room still has active sensors",
  "errorCode": 409,
  "documentation": "http://localhost:8080/api/docs/errors"
}
3.  Sensor Management
GET /sensors
[
  {
    "id": "S1",
    "type": "CO2",
    "roomId": "R101",
    "currentValue": 450,
    "status": "ACTIVE"
  }
]
GET /sensors?type=CO2

Filters sensors using @QueryParam

POST /sensors
{
  "id": "S1",
  "type": "CO2",
  "roomId": "R101",
  "status": "ACTIVE"
}
Response
201 Created

 If room does not exist:

{
  "errorMessage": "Room does not exist",
  "errorCode": 422,
  "documentation": "http://localhost:8080/api/docs/errors"
}
GET /sensors/{id}
{
  "id": "S1",
  "type": "CO2",
  "roomId": "R101",
  "currentValue": 450,
  "status": "ACTIVE"
}
4.  Sensor Readings (Sub-Resource)
GET /sensors/{id}/readings
[
  {
    "value": 450,
    "timestamp": 1713760000000
  }
]
POST /sensors/{id}/readings
{
  "value": 470
}
Response
201 Created

✔ Side Effect:

Automatically updates currentValue in Sensor
5.  Exception Handling

The API uses centralized exception handling via ExceptionMapper.

Implemented Exceptions
Exception	HTTP Code	Description
RoomNotEmptyException	409	Room cannot be deleted if sensors exist
LinkedResourceNotFoundException	422	Invalid roomId in sensor creation
SensorUnavailableException	403	Sensor in maintenance mode
GlobalExceptionMapper	500	Handles unexpected runtime errors
Standard Error Format
{
  "errorMessage": "Description of error",
  "errorCode": 422,
  "documentation": "http://localhost:8080/api/docs/errors"
}
6.  Logging Filter

Implements:

ContainerRequestFilter
ContainerResponseFilter
Logs

Incoming Request:

Method: GET
URI: /api/v1/sensors

Outgoing Response:

Status: 200




✔ Used for debugging and system observability.


🧠 DESIGN HIGHLIGHTS


 RESTful JAX-RS architecture
 Proper HTTP method usage (GET/POST/DELETE)
 QueryParam filtering
 Sub-resource locator pattern
 Exception-driven API design
 Global error safety net (500 handler)
 Logging filter (cross-cutting concern)
 In-memory storage only (HashMap / ArrayList)

📌 KEY DESIGN DECISIONS
✔ Why QueryParam?

Used for flexible filtering without changing URL structure.

✔ Why Sub-Resource Locator?

Improves modularity by separating sensor readings from main sensor logic.

✔ Why ExceptionMapper?

Ensures clean JSON error responses and prevents stack trace leakage.

👨‍💻 AUTHOR

This project was developed for academic purpose.

Module: Client-Server Architecture
Institution: University of Westminster (IIT Sri Lanka)
Programme: BSc (Hons) Computer Science
Academic Year: 2025/2026 (Level 5)
Type: Individual Project
Technology: JAX-RS (Jersey), Apache Tomcat, Maven
