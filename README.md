# Plan2Go Post Server
This repository contains the backend server for storing and managing posts. It is built using the Spring Boot framework and utilizes a MongoDB database for data storage. Additionally, WebSocket is implemented to provide notification functionality.

## Technologies Used
The following technologies were used to develop this project:

- **Spring Boot**: Spring Boot is a framework that simplifies the development of Java-based applications. It provides a ready-to-use infrastructure for building robust and scalable web applications.

- **MongoDB**: MongoDB is a NoSQL database that offers high scalability and flexibility. It stores data in a JSON-like format, making it suitable for handling unstructured or semi-structured data.

- **WebSocket**: WebSocket is a communication protocol that provides full-duplex communication channels over a single TCP connection. It enables real-time communication between clients and servers, making it suitable for implementing notification functionality.

## Launch & Deployment
Download your IDE of choice: (e.g., Eclipse, IntelliJ), Visual Studio Code and make sure Java 15 is installed on your system (for Windows-users, please make sure your JAVA_HOME environment variable is set to the correct version of Java).

1. File -> Open... -> Plan2Go-post-server
2. Accept to import the project as a gradle project

To build, right click the `build.gradle` file and choose `Run Build`

### Building with Gradle
- macOS: `./gradlew`
- Linux: `./gradlew`
- Windows: `./gradlew.bat`

For more information about Gradle Wrapper and Gradle, please refer to the [official documentation](https://docs.gradle.org).

### Build
```bash
./gradlew build
```

### Run
```bash
./gradlew bootRun
```

### Test
```
./gradlew test
```

