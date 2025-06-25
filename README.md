# Numdev Project

![NumDev Logo](./ressources/images/numdev.png)

**MySQL Database:**

Connect to your MySQL Server instance.

Create a new database for your application and add all the tables to your database:

```sql
CREATE TABLE `TEACHERS` (
`id` INT PRIMARY KEY AUTO_INCREMENT,
`last_name` VARCHAR(40),
`first_name` VARCHAR(40),
`created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
`updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE `SESSIONS` (
`id` INT PRIMARY KEY AUTO_INCREMENT,
`name` VARCHAR(50),
`description` VARCHAR(2000),
`date` TIMESTAMP,
`teacher_id` int,
`created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
`updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE `USERS` (
`id` INT PRIMARY KEY AUTO_INCREMENT,
`last_name` VARCHAR(40),
`first_name` VARCHAR(40),
`admin` BOOLEAN NOT NULL DEFAULT false,
`email` VARCHAR(255),
`password` VARCHAR(255),
`created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
`updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE `PARTICIPATE` (
`user_id` INT,
`session_id` INT
);

ALTER TABLE `SESSIONS` ADD FOREIGN KEY (`teacher_id`) REFERENCES `TEACHERS` (`id`);
ALTER TABLE `PARTICIPATE` ADD FOREIGN KEY (`user_id`) REFERENCES `USERS` (`id`);
ALTER TABLE `PARTICIPATE` ADD FOREIGN KEY (`session_id`) REFERENCES `SESSIONS` (`id`);

INSERT INTO TEACHERS (first_name, last_name)
VALUES ('Margot', 'DELAHAYE'),
('Hélène', 'THIERCELIN');


INSERT INTO USERS (first_name, last_name, admin, email, password)
VALUES ('Admin', 'Admin', true, 'yoga@studio.com', '$2a$10$.Hsa/ZjUVaHqi0tp9xieMeewrnZxrZ5pQRzddUXE/WjDu2ZThe6Iq');
```

## Installation procedure

**Cloning the project:**
To clone this repository from GitHub, run the following command: `git clone https://github.com/Protazer/Testez-une-application-full-stack.`

### Front-End

1. Install the dependencies:

To start the Angular Front-End project, follow these steps:

- Navigate to the Front-End directory in your terminal:

```shell
cd front
```

- Install project dependencies using yarn:

```shell
yarn install
```

2. Starting the server

- After the dependencies are installed, you can start the development server by running:

```shell
yarn start
```

This command will compile the Angular application and start a development server.
You can then access the application in your browser at `http://localhost:4200`.

### Back-End

1. Configure the application in the `application.properties` file

Once you have cloned the repository, you'll need to check the `application.properties` file on the `back/src/main/resources/` folder containing these properties:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/yourDatabase?allowPublicKeyRetrieval=true
spring.datasource.username=dbusername
spring.datasource.password=dbpassword

spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL5InnoDBDialect
spring.jpa.hibernate.naming.physical-strategy=org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl
spring.jpa.show-sql=true
oc.app.jwtSecret=openclassrooms
oc.app.jwtExpirationMs=86400000
```

2. Install the project dependencies using the following command: `mvn clean install`

3. Run the application using your IDE or by running `mvn spring-boot:run` in the project directory.

4. To generate the code coverage of the back-end, run the following command: `mvn clean test`

## Code coverage reports

### Front-End

Get Test Code Coverage for Front-End (Angular):

- To run test and get code coverage for the Angular Front-End, you can use this command:

```shell
 yarn test
```

- To get the Cypress E2E tests coverage, use the following command:

```shell
yarn e2e 
```
and in another terminal :

```shell
yarn cypress:run
```
 you can now launch this command for watch the updated coverage
```shell
yarn e2e:coverage
```

Upon completion, the terminal displays the tests outcome (pass/fail), accompanied by a comprehensive table showing the code coverage %

### Back-End

Get Test Code Coverage for Back-End (Spring Boot):

- Run the following command in the terminal to execute tests and generate a coverage report using JaCoCo for the Spring Boot backend:

```shell
mvn clean site
```
Following successful execution, locate and open in your browser the `index.html` file for the coverage report under the `target/site/jacoco` directory in the project.
