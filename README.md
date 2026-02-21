# merchant-
Name: Abdulwarith lawal

Analytics on merchant and product activities

This is a Spring Boot application that provides APIs for merchant analytics, including failure rates, top merchants, monthly active merchants, and product adoption metrics.

**Prerequisites**
Java 17 or higher

Maven 3.x
An IDE (optional, e.g., IntelliJ IDEA, Eclipse)
Postman or a browser for testing APIs


Endpoints
http://localhost:8080/analytics/failure-rates
http://localhost:8080/analytics/top-merchant
http://localhost:8080/analytics/monthly-active-merchants
http://localhost:8080/analytics/product-adoption

1. Create CSV folder 
at the root of the project, create a folder named data and place csv files inside

project-root/
├─ src/
├─ data/
│  ├─ events1.csv
│  ├─ events2.csv

2. Ensure application.properties is correctly set up for Postgresql
spring.application.name=merchantapi
spring.datasource.url=jdbc:postgresql://localhost:5432/{your_db}
spring.datasource.username=<your_db_username>
spring.datasource.password=<your_db_password>
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.properties.hibernate.format_sql=true

ddl-auto-create-drop will drop the database schema when the app stops

