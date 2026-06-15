# Client and Server Project

## Overview

Client and Server Project is a Java networking application that demonstrates communication between a server and multiple clients using TCP sockets. The project was developed to understand the fundamentals of socket programming, multithreading, and client-server architecture.

The server listens for incoming client connections and manages communication between connected clients. Each client can send messages to the server and receive responses through a simple console-based interface.

## Features

* TCP socket communication
* Multi-client support
* Concurrent client handling using threads
* Real-time message exchange
* Client connection and disconnection management
* Configurable server host and port
* Console-based interface
* Lightweight and easy-to-understand architecture

## Technologies Used

* Java 17
* Maven
* Java Socket API
* Multithreading
* ExecutorService
* IntelliJ IDEA

## Project Structure

```text
CLIENT_AND_SERVER_PROJECT
│
├── serverRC
│   ├── src
│   │   ├── main
│   │   │   └── java
│   │   │       ├── ClientApp.java
│   │   │       └── ServerApp.java
│   │
│   └── pom.xml
│
└── README.md
```

## How It Works

### Server

The server creates a ServerSocket and waits for incoming client connections.

When a new client connects:

1. The connection is accepted.
2. A dedicated thread is created for the client.
3. The client can exchange messages with the server.
4. The server continues listening for additional clients.

### Client

The client connects to the server using the specified host and port.

After connecting:

1. The user can type messages in the console.
2. Messages are sent to the server.
3. The client receives responses from the server.
4. Typing `exit` closes the connection.

## Running the Application

### Start the Server

Run:

```text
ServerApp.java
```

The server starts and listens for incoming connections.

### Start the Client

Run:

```text
ClientApp.java
```

By default, the client connects to:

```text
localhost:8080
```

## Concepts Demonstrated

* Client-Server Architecture
* TCP Communication
* Socket Programming
* Concurrent Programming
* Thread Management
* Network Communication
* Java I/O Streams

## Educational Purpose

This project was developed as part of academic coursework to practice networking concepts and gain hands-on experience with Java socket programming and concurrent application development.


## Author

**Damaris**

Computer Science Student
West University of Timișoara
