# Airline Ticket Booking Website

This project is an online airline ticket booking system that allows users to search for flights, book tickets, make payments, and manage flight information easily. The website supports features such as searching for flights by date, departure, destination, booking tickets, online payment, user account management, and admin system management.

## Main Features
- Search for flights with various criteria
- Book airline tickets online
- Secure and fast payment
- Manage booking information and user accounts
- Admin functions: manage flights, tickets, users

## Technologies
- Java 21+
- Maven
- Spring Boot (optional, for REST API)

## Installation Guide
1. Clone the repository to your machine
2. Install required packages
3. Configure the database connection
4. Run the application

## Project Structure
```
banvemaybay/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── example/
│   │   │           └── banvemaybay/
│   │   │               ├── controller/   # Handle requests, API/web routing
│   │   │               ├── service/      # Business logic layer
│   │   │               ├── repository/   # Database interaction
│   │   │               ├── model/        # Entity definitions
│   │   │               ├── dtos/         # Data Transfer Objects
│   │   │               ├── responses/    # Response objects for client
│   │   │               ├── utils/        # Utility/helper functions
│   │   │               ├── components/   # Spring components (beans, configs, etc.)
│   │   │               ├── config/       # Application configuration (security, CORS, etc.)
│   │   │               └── ...           # Other packages if any
│   │   ├── resources/
│   │   │   ├── static/       # Static resources (images, css, js)
│   │   │   ├── templates/    # Thymeleaf templates
│   │   │   └── application.properties # Spring Boot configuration file
│   └── test/                # Test source code
├── pom.xml                  # Maven configuration file
└── README.md                # Project documentation
```

## Build and Run

### Install dependencies
```bash
mvn install
```

### Run the application in development mode
```bash
mvn spring-boot:run
```

### Build the application for production
```bash
mvn package
```
