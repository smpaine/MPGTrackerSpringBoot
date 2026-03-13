# MPGTracker services in Spring Boot
Backend services for Angular 7+ MPGTracker application.

Utilizes MySQL for database storage using JNDI data source.

Runs in Apache Tomcat 9 (https://tomcat.apache.org/). Requires Java 11.

## Building
Generate WAR file in target folder: `./mvnw clean install`

Generate Production WAR file in target folder: `./mvn package -Pprod`

### Acknowledgements
Spring Boot (https://spring.io/projects/spring-boot)

Started out being based off of spring-petclinic sample project (https://projects.spring.io/spring-petclinic)

JWT & Authentication (https://www.techgeeknext.com/spring/spring-boot-security-token-authentication-jwt)

Exception Handler (https://stackoverflow.com/a/37953633)

Passkey implementation - Claude Code - Yubico

