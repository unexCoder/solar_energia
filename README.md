


# Solar ⚡ Energía
## v.0.0.1
©2025 unexcoder
## web-store-app

Solar Energía is a web-based store application built with **Spring Boot** and **Thymeleaf**. It allows users to manage articles, manufacturers, and images while providing secure user authentication and role-based access control.

---

## Table of Contents
- [Features](#features)
- [Technologies Used](#technologies-used)
- [Database Structure](#database-structure)
- [Setup Instructions](#setup-instructions)
- [Usage](#usage)
- [License](#license)

---

## Features
- **User Management**: Register, edit profiles, and manage user roles (Admin/User).
- **Article Management**: Create, edit, and list articles with associated manufacturers and images.
- **Manufacturer Management**: Manage manufacturers and their associated data.
- **Image Uploads**: Upload and display images for articles and manufacturers.
- **Secure Authentication**: Role-based access control using Spring Security.
- **Responsive Design**: User-friendly interface built with Thymeleaf templates.

---

## Technologies Used
- **Backend**: Spring Boot 3.4.3
  - Spring Boot Starter Web
  - Spring Boot Starter Data JPA
  - Spring Boot Starter Security
- **Frontend**: Thymeleaf with Spring Security Extras
- **Database**: MySQL
- **Build Tool**: Maven
- **Java Version**: 17
- **Other Libraries**:
  - Lombok
  - Jakarta Validation API

---

## Database Structure

```
+-------------------------+
| Tables_in_solar_energia |
+-------------------------+
| articulo                |
| fabrica                 |
| imagen                  |
| usuario                 |
+-------------------------+

+-----------------+---------------+------+-----+---------+-------+
| Field           | Type          | Null | Key | Default | Extra |
+-----------------+---------------+------+-----+---------+-------+
| id              | binary(16)    | NO   | PRI | NULL    |       |
| descripcion     | varchar(1000) | YES  |     | NULL    |       |
| nombre_articulo | varchar(255)  | NO   |     | NULL    |       |
| nro_articulo    | int           | NO   | UNI | NULL    |       |
| fabrica_id      | binary(16)    | NO   | MUL | NULL    |       |
| imagen_id       | binary(16)    | YES  | UNI | NULL    |       |
+-----------------+---------------+------+-----+---------+-------+

+-----------+--------------+------+-----+---------+-------+
| Field     | Type         | Null | Key | Default | Extra |
+-----------+--------------+------+-----+---------+-------+
| id        | binary(16)   | NO   | PRI | NULL    |       |
| nombre    | varchar(100) | NO   |     | NULL    |       |
| imagen_id | binary(16)   | YES  | UNI | NULL    |       |
+-----------+--------------+------+-----+---------+-------+

+-----------+----------------------+------+-----+---------+-------+
| Field     | Type                 | Null | Key | Default | Extra |
+-----------+----------------------+------+-----+---------+-------+
| id        | binary(16)           | NO   | PRI | NULL    |       |
| apellido  | varchar(100)         | NO   |     | NULL    |       |
| email     | varchar(255)         | NO   | UNI | NULL    |       |
| nombre    | varchar(100)         | NO   |     | NULL    |       |
| password  | varchar(255)         | NO   |     | NULL    |       |
| rol       | enum('ADMIN','USER') | NO   |     | NULL    |       |
| imagen_id | binary(16)           | YES  | UNI | NULL    |       |
+-----------+----------------------+------+-----+---------+-------+

+-------------+--------------+------+-----+---------+-------+
| Field       | Type         | Null | Key | Default | Extra |
+-------------+--------------+------+-----+---------+-------+
| id          | binary(16)   | NO   | PRI | NULL    |       |
| contenido   | longblob     | NO   |     | NULL    |       |
| descripcion | varchar(500) | YES  |     | NULL    |       |
| mime        | varchar(100) | NO   |     | NULL    |       |
| nombre      | varchar(255) | NO   |     | NULL    |       |
+-------------+--------------+------+-----+---------+-------+
```

## Setup Instructions

### Prerequisites
- Java 17 or higher
- Maven
- MySQL database

### Steps
1. Clone the repository:
   ```bash
   git clone https://github.com/your-username/solar-energia.git
   cd solar-energia