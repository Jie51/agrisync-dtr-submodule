# AgriSync Daily Time Record (DTR) Module - Deployment Guide

This guide provides explicit instructions for setting up the local MySQL database environment and running the standalone AgriSync DTR Spring Boot application.

---

## 1. Database Environment Configuration

The application persists transactional records directly to a local relational MySQL instance. Follow these steps to configure the repository layer before booting the application.

### Step 1: Create the Target Schema
Log into your local MySQL instance (via CLI or a management GUI like MySQL Workbench / DBeaver) and initialize the target schema directory:
```sql
CREATE DATABASE mauswag DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE mauswag;
```

Initialize Database Table Structural Layouts
Execute the following DDL statements to construct the required database entities, tracking fields, and application relational indexing parameters:

SQL
-- 1. Create the Employee Registry Table
CREATE TABLE employee (
    emp_id INT AUTO_INCREMENT PRIMARY KEY,
    emp_name VARCHAR(255) NOT NULL UNIQUE,
    emp_role VARCHAR(50) NOT NULL,
    password VARCHAR(255) NULL
);

-- 2. Create the Attendance Tracking Transaction Table
CREATE TABLE attendance (
    attendance_id INT AUTO_INCREMENT PRIMARY KEY,
    emp_id INT NOT NULL,
    attendance_date DATE NOT NULL,
    time_in DATETIME NULL,
    time_out DATETIME NULL,
    ot_hours DOUBLE NOT NULL DEFAULT 0.00,
    CONSTRAINT fk_attendance_employee FOREIGN KEY (emp_id) REFERENCES employee(emp_id) ON DELETE CASCADE
);

-- 3. Create the Labor Rate Log Audit Table
CREATE TABLE labor_rate_log (
    rate_id INT AUTO_INCREMENT PRIMARY KEY,
    emp_id INT NOT NULL,
    hourly_rate DECIMAL(10,2) NOT NULL,
    effective_date DATE NOT NULL,
    changed_by_admin_id INT NULL,
    CONSTRAINT fk_rate_employee FOREIGN KEY (emp_id) REFERENCES employee(emp_id) ON DELETE CASCADE
);

Seed Administrative Control Profiles
Insert initial records to allow testing of the authorization token gateway routines:

SQL
INSERT INTO employee (emp_name, emp_role, password) VALUES 
('System Admin', 'Admin', 'admin123'),
('Harvester Supervisor', 'Harvester Admin', 'supervisor123');

2. Application Properties Configuration
Navigate to src/main/resources/application.properties (or application.yml) and adjust the local datasource connection strings to match your environment variables:

Properties
# Server Port Configuration
server.port=8080

# Spring Datasource Configurations
spring.datasource.url=jdbc:mysql://localhost:3306/mauswag?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=YOUR_MYSQL_USERNAME
spring.datasource.password=YOUR_MYSQL_PASSWORD
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# Hibernate / JPA Optimizations
spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
3. How to Compile and Run the Application
The system targets Java 17 (or newer) and compiles via Maven. Ensure your development environment satisfies these runtime conditions.

Option A: Running via Command Line (Terminal)
Navigate to the root directory of the project (where the pom.xml file is located) and execute:

Bash
# Clean previous targets and build the deployment package
./mvnw clean package

# Boot the Spring Boot embedded Tomcat container instance
./mvnw spring-boot:run
(On Windows command prompts, replace ./mvnw with mvnw.cmd)

Option B: Running within an AI-Assisted IDE (Cursor / Windsurf / IntelliJ)
Open the root folder in your IDE.

Allow the IDE to import the Maven dependencies declared inside your pom.xml.

Open the core entry point file: src/main/java/com/example/mauswag/MauswagApplication.java.

Click Run or Debug directly over the main method loop.

Once successfully initialized, the standard browser endpoint will become actively responsive at:
http://localhost:8080/attendance.html

4. Verification & Testing the Ingestion Pipeline
Open the application interface in your web browser.

Login using one of the seeded administrative profiles to capture an authentication security token into your browser's volatile sessionStorage memory cache.

Use the Drag-and-Drop file landing zone to load your target CSV workforce shift logs.

Verify that the client parser engine correctly detects unmapped names (flagging them with red .row-invalid alerts) or processing anomalies (amber .row-duplicate alerts) before committing data blocks to the repository layers.
