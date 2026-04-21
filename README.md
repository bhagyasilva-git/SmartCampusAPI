Smart Campus API (JAX-RS Coursework)

📌 Overview


The Smart Campus API is a RESTful web service developed using JAX-RS (Jersey) and deployed on a lightweight servlet container (Apache Tomcat).


It simulates a campus management system that handles:


Rooms

Sensors

Sensor Readings (historical data)

Exception handling and validation rules

Logging and request/response monitoring


The system is built using in-memory data structures (HashMap & ArrayList) instead of a database, as required by the coursework specification.



🌐 Base URL
http://localhost:8080/SmartCampus/api/v1

🏗️ Technology Stack

Java 8+

JAX-RS (Jersey)

Apache Tomcat 9+

Maven

In-memory storage:
HashMap
ArrayList

📦 Project Architecture

com.smartcampus.smartcampus
│
├── resources
│   ├── RoomResource
│   ├── SensorResource
│   └── SensorReadingResource
│
├── model
│   ├── Room
│   ├── Sensor
│   └── SensorReading
│
├── exception
│   ├── RoomNotEmptyException
│   ├── LinkedResourceNotFoundException
│   ├── SensorUnavailableException
│   ├── DataNotFoundException
│   ├── Exception Mappers (409, 422, 403, 500)
│
└── filter
    └── LoggingFilter
    
🚀 How to Run the Project

1. Clone Repository
git clone https://github.com/YOUR_USERNAME/smart-campus-api.git

3. Open Project

Open in:


NetBeans OR IntelliJ IDEA

3. Build Project
mvn clean install

5. Deploy on Server


Deploy WAR file to:

Apache Tomcat 9+

5. Run Application
http://localhost:8080/SmartCampus/api/v1

📡 API ENDPOINTS

1. Discovery Endpoint (HATEOAS)
2. 
GET /api/v1


Returns API metadata and navigation links.


Response:
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

2. Room Management
   
➤ GET /rooms

[
  {
    "id": "R101",
    "name": "Lecture Hall",
    "capacity": 120,
    "sensorIds": ["S1"]
  }
]

➤ POST /rooms


Request:

{
  "id": "R101",
  "name": "Lecture Hall",
  "capacity": 120,
  "sensorIds": []
}


Response:

201 Created

➤ GET /rooms/{id}

{
  "id": "R101",
  "name": "Lecture Hall",
  "capacity": 120,
  "sensorIds": ["S1"]
}

➤ DELETE /rooms/{id}


Success:

204 No Content


Error (if sensors exist):

{
  "errorMessage": "Room still has active sensors",
  "errorCode": 409,
  "documentation": "http://localhost:8080/api/docs/errors"
}

📡 3. Sensor Management

➤ GET /sensors

[
  {
    "id": "S1",
    "type": "CO2",
    "roomId": "R101",
    "currentValue": 450,
    "status": "ACTIVE"
  }
]

➤ GET /sensors?type=CO2


Filters sensors dynamically using @QueryParam.


➤ POST /sensors


Request:

{
  "id": "S1",
  "type": "CO2",
  "roomId": "R101",
  "status": "ACTIVE"
}


Success:

201 Created


Error (invalid room):

{
  "errorMessage": "Room does not exist",
  "errorCode": 422,
  "documentation": "http://localhost:8080/api/docs/errors"
}

➤ GET /sensors/{id}

{
  "id": "S1",
  "type": "CO2",
  "roomId": "R101",
  "currentValue": 450,
  "status": "ACTIVE"
}

4. Sensor Readings (Sub-Resource)
   
➤ GET /sensors/{id}/readings
[
  {
    "value": 450,
    "timestamp": 1713760000000
  }
]

➤ POST /sensors/{id}/readings


Request:

{
  "value": 470
}


Response:

201 Created


✔ Side Effect:

Automatically updates currentValue in Sensor

5. Exception Handling


The API implements centralized exception handling using ExceptionMapper.


✔ Implemented Exceptions

Exception	HTTP Code	Description

RoomNotEmptyException	409	Room cannot be deleted if sensors exist

LinkedResourceNotFoundException	422	Invalid roomId in sensor creation

SensorUnavailableException	403	Sensor in maintenance mode

GlobalExceptionMapper	500	Handles unexpected runtime errors

📌 Error Response Format

{
  "errorMessage": "Description of error",
  "errorCode": 422,
  "documentation": "http://localhost:8080/api/docs/errors"
}

6. Logging Filter


A global filter is implemented using:


ContainerRequestFilter

ContainerResponseFilter

Logs:

Incoming Request:

Method: GET

URI: /api/v1/sensors

Outgoing Response:

Status: 200


✔ Ensures API observability and debugging support.


🧠 DESIGN HIGHLIGHTS 


✔ RESTful architecture (JAX-RS standard)

✔ Proper use of HTTP methods (GET, POST, DELETE)

✔ QueryParam filtering for scalability

✔ Sub-resource locator pattern (/sensors/{id}/readings)

✔ Exception-driven design (no raw error responses)

✔ Global exception safety net (500 handler)

✔ Logging filter for cross-cutting concerns

✔ In-memory storage (HashMap / ArrayList only)


📌 IMPORTANT DESIGN DECISIONS

✔ Why QueryParam instead of PathParam?


QueryParam is used for filtering because it allows flexible and scalable queries without changing the API structure.


✔ Why Sub-Resource Locator?


It separates concerns by delegating sensor readings to a dedicated resource, improving maintainability and scalability.


✔ Why ExceptionMapper?


Ensures clean API responses and prevents exposure of internal stack traces.


👨‍💻 Author

This project was developed for educational purpose.

- Module: Client-Server Architecture
- Institution: University of Westminster (IIT SriLanka)  
- Work Type: Individual Project  
- Academic Year: 2025/2026 (L5)
