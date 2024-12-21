# Currency Converter - Software Testing Project

## Overview

The Currency Converter project is developed as part of a university course in software testing. This online converter utilizes external APIs to fetch real-time exchange rates, providing a practical environment for honing software testing skills. The primary focus of this project is on testing methodologies, ensuring robust test coverage, and implementing best practices in software testing. While the application serves as a functional currency converter, the emphasis is on the testing process rather than building a production-ready tool. Join us in exploring the world of software testing through this hands-on Currency Converter project.
## Prerequisites

- Maven
- PostgreSQL

## How to Run

### 1. Setup PostgreSQL Database

Before running the program, ensure you have PostgreSQL installed. Create a database named "currenciesdb" and update the username and password in the resources folder.

```bash
# Access PostgreSQL
psql -U your_username -d your_password

# Create a new database
CREATE DATABASE currenciesdb;
```
2. Update Database Configuration

To update the database configuration, edit the following file:
src/main/resources/application.properties
src/test/resources/testRealDB.properties
3.  Run the Application
Open a command prompt in the main directory of the project and run the following Maven command to start the Spring Boot application:
```bash
#Run the application
mvnw spring-boot:run
```

4. Generate Surefire Report

To generate the Surefire report, run the following Maven command:
```bash
mvnw surefire-report:report
```

Additional Notes

    Make sure to replace "your_username" and "your_password" with your actual PostgreSQL username and password.
    Ensure the PostgreSQL server is running before executing the commands.
    For more detailed configuration, refer to the application.properties file in the resources folder.

# -----------------------------
# This project is worked by:   | 
# Moath Hussein                |
# ID :1202862                  | 
# ------------------------------