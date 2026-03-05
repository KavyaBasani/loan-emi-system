# Loan EMI Management System

A full-stack Spring Boot application to manage customers, loans, and EMIs with an interactive dashboard.  
This project demonstrates backend logic with Spring Boot and frontend integration using HTML, CSS, and JavaScript.

---

## Features
- **Customer Management:** Create and manage customer details.
- **Loan Management:** Create loans for customers with automatic EMI generation.
- **EMI Tracking:** View all EMIs for each loan and mark them as paid individually or in bulk.
- **Interactive Dashboard:** User-friendly interface with color-coded EMIs (green for paid, red for unpaid).
- **Responsive Design:** Works across devices with a clean layout.

---

## Tech Stack
- **Backend:** Java, Spring Boot, Spring Data JPA
- **Database:** MySQL
- **Frontend:** HTML, CSS, Vanilla JavaScript
- **Build Tool:** Maven
- **Version Control:** Git & GitHub

---

## Setup & Run

1. Clone the repository:
git clone https://github.com/KavyaBasani/loan-emi-system.git
2. Import Project
Open the project in Spring Tools / IntelliJ IDEA.
3. Database Configuration
Edit application.properties with your MySQL credentials:

spring.datasource.url=jdbc:mysql://localhost:3306/your_db_name
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update

4. Run the Backend
 Run `LoanEmiSystemApplication.java` to start the backend.
5. Open Dashboard
Open `index.html` in the browser to access the dashboard.
