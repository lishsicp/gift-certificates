# REST API Basics
### *Task*
Business requirements:
1. Develop web service for Gift Certificates system with the following entities (many-to-many):
    - `CreateDate`, `LastUpdateDate` - format [ISO 8601](https://en.wikipedia.org/wiki/ISO_8601). Example: 2018-08-29T06:12:15.156.
    - `Duration` - in days (expiration period)
2. The system should expose REST APIs to perform the following operations:
- CRUD operations for GiftCertificate. If new tags are passed during creation/modification – they should be created in the DB. For update operation - update only fields, that pass in request, others should not be updated. Batch insert is out of scope.
- CRD operations for Tag.
- Get certificates with tags (all params are optional and can be used in conjunction):
    - by tag name (ONE tag)
    - search by part of name/description (can be implemented, using DB function call)
    - sort by date or by name ASC/DESC (extra task: implement ability to apply both sort type at the same time).

Application requirements:
- JDK version: 8 – use Streams, java.time.*, etc. where it is possible.
- Any widely-used connection pool could be used.
- JDBC / Spring JDBC Template should be used for data access.
- Use transactions where it’s necessary.
- Java Code Convention is mandatory (exception: margin size – 120 chars).
- Build tool: Maven/Gradle, latest version. Multi-module project.
- Web server: Apache Tomcat/Jetty.
- Application container: Spring IoC. Spring Framework, the latest version.
- Database: PostgreSQL/MySQL, latest version.
- Testing: JUnit 5.+, Mockito.
- Service layer should be covered with unit tests not less than 80%.
- Repository layer should be tested using integration tests with an in-memory embedded database (all operations with certificates).

General requirements:
- Code should be clean and should not contain any “developer-purpose” constructions.
- App should be designed and written with respect to OOP and SOLID principles.
- Code should contain valuable comments where appropriate.
- Public APIs should be documented (Javadoc).
- Clear layered structure should be used with responsibilities of each application layer defined.
- JSON should be used as a format of client-server communication messages.
- Convenient error/exception handling mechanism should be implemented: all errors should be meaningful and localized on backend side.
- Abstraction should be used everywhere to avoid code duplication.
- Several configurations should be implemented (at least two - dev and prod).

Application restrictions:
- Spring Boot.
- Spring Data Repositories.
- JPA.
- Powermock (your application should be testable).