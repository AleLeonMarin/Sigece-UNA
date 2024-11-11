# SigeceUNA

## Overview

**SigeceUNA** is a comprehensive communications system designed for enterprise environments. It provides robust functionality for managing roles, facilitating real-time chats, sending mass emails, and handling advanced task management within a customizable and user-friendly interface.

## Features

- **Role Management**: Administer user roles and permissions.
- **Messaging**: Real-time chat feature to improve communication.
- **Mass Email**: Efficiently send bulk emails.
- **Task Management**: A highly customizable dashboard to monitor and manage various tasks.
- **Customizable Interface**: Personalize the system layout to suit specific requirements.

## Technologies Used

The project leverages a modern tech stack to ensure reliability and performance:

- **Java 21**: Core programming language.
- **JavaFX 21**: For a rich graphical user interface.
- **Payara Server 6**: Application server for Java EE applications.
- **Oracle 21c XE (Docker)**: Database management (Oracle can also be set up directly on your local machine).
- **IDE**: VSCode and NetBeans for development.
- **SQL**: Database queries and operations.
- **JPA**: Java Persistence API for ORM.
- **Jasper Reports**: To generate reports.
- **Apache POI**: To handle Excel file generation.
- **Assembly Jar Maker**: For compiling and packaging the project.
- **REST**: RESTful web services.
- **Java Enterprise 10**: Ensures enterprise-grade performance and security.

## Installation and Setup

Follow these steps to set up and run **SigeceUNA**:

1. **Install Java JDK 21**: Ensure that JDK 21 is installed on your machine.
2. **Install and Configure Payara Server**:

   - Download and set up Payara Server 6.
   - Configure a connection pool named `SigecePool`.
   - Set up a JDBC resource named `jdbc/Sigece`.

3. **Set Up Oracle Database**:

   - Install Oracle 21c XE, either on your local machine or as a Docker container.
   - Ensure it is accessible by Payara Server for database operations.

4. **Run the Application**:
   - Build the project and package it as a JAR file.
   - Run the JAR files directly to start the application.

## Contributors and Instructor

- **Developers**:
  - Alejandro León Marín
  - Kendall Fonseca
- **Instructor**:
  - MSc. Carlos Carranza Blanco
