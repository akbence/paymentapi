# Interview doc
## Short summary:
Build an Instant Payment API service that allows users to send money instantly using REST.
## Focus:
High Availability, Transactional Processing, Error Handling
## Task requirements:
Use Git as a version control system, and upload your code into a public GitHub repository.
As a framework, use Spring Boot with Maven or Gradle, and your preferred Java version.
No UI implementation is required for this task.
Containerize your application, to prepare it for future cloud deployment.

The API should:
- Perform balance checks before processing.
- Handle concurrency (e.g., prevent double spending / double notifications).
- Save the transactions into a PostgreSQL table
- Use Kafka to asynchronously send a notification of the transaction to the recipient.


Implementation extras:
- Implement a fault tolerant system
- Tests to ensure functionality
- Document the REST API

# Notes from me

- Documentation of the api is avaiable through OpenApi at:   localhost:8080/swagger-ui/index.html
- Minor validation added as well
- For testing, I created a postman collection of 3 items. Didnt have the time to make more E2E tests.
- Kafka module fault tolerant: try-retry mechanism
- Pessimistic locks ensures there is no concurent modification at db level
- Docker env contains 1 optional module: adminer, only for checking the postgres.

# Quickstart

0. Install docker
1. Clone the repo, go inside project folder
2. Run './mvnw clean package -DskipTests'
3. Run 'docker-compose up --build'
