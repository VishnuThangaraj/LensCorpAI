# LensCorpAI Assessment

# Spring Boot JWT Authentication Project

## Description

This repository contains a Spring Boot project that implements JWT (JSON Web Token) authentication. The project provides functionalities for user registration, login, logout, and viewing the profile of the currently logged-in user.

## Features

- **User Registration**: New users can register by providing the required details.
- **User Login**: Registered users can log in using their credentials. Upon successful login, a JWT is generated.
- **JWT Authentication**: All protected routes are secured with JWT authentication. The server verifies the JWT sent in the request header before granting access.
- **User Logout**: Logged-in users can log out. Upon logout, the user's token is added to a blacklist to prevent reuse.
- **View Profile**: Logged-in users can view their profile information.

## Requirements

This project requires the following technologies:

- **Java**: Version 17 or higher is required.
- **Spring Boot**: This project is based on Spring Boot. Version 3.2 or higher is required.
- **PostgreSQL**: This project uses PostgreSQL for data storage. Version 4 or higher is required.
- **Maven**: Maven is used for project management and build automation.
- **Postman**: Version 9 or higher of Postman is recommended for testing the API endpoints.
- **IntelliJ IDEA**: The latest version of IntelliJ IDEA is recommended for development.

Please ensure that you have these technologies installed and properly configured before running the project.


## Installation

1. Clone this repository: `git clone https://github.com/VishnuThangaraj/LensCorpAI.git`
2. Navigate to the project directory: `cd LensCorpAI`
3. Install dependencies: `mvn install`
4. Run the application: `mvn spring-boot:run`

## Usage

After running the application, you can use the following endpoints:

- `/api/auth/register`: To register a new user.
- `/api/auth/authenticate`: To log in as a user.
- `/api/auth/logout`: To log out as a user.
- `/api/app/profile`: To view the profile of the currently logged-in user.
- `/swagger-ui/index.html` : To view Swagger-UI Documentation

Please note that you need to include the JWT in the `Authorization` header for the `/api/auth/logout` and `/api/app/profile` routes.

## Download

- **Project Zip File**: [Download Here](http)
- **PostMan Collection File** [Download Here](https://drive.google.com/file/d/1RbkJgMm2uI3UVSIjaQfKOiWJ2oGK6Uxc/view?usp=drive_link)
- **Installation Guide**: [Download Here](https://drive.google.com/file/d/1PsDF_-bqyOR7uuX0rvFjqSIneqnuX1os/view?usp=drive_link)

## Contributing

Contributions are welcome. Please feel free to submit a pull request or open an issue.

## License

This project is licensed under the terms of the GNU General Public License v3.0.
