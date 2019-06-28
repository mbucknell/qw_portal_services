# qw\_portal\_services
Water Quality Portal (WQP) Lookup Service

## Local Configuration
This application is configured to be run as a jar. It can also be run using the command ``` mvn spring-boot:run ``` in the project root directory.
 
To run in a development environment, create an application.yml file in
the project root directory containing the following (shown are example values):

```$yml
WQP_DB_HOST: <127.0.0.1>
WQP_DB_PORT: <5435>
WQP_DB_READ_ONLY_USERNAME: <wqp_user>
WQP_DB_READ_ONLY_PASSWORD: <changeMe>
WQP_DB_NAME: <wqp_db>

SERVER_PORT: 8082
SERVER_CONTEXT_PATH: /Codes

SWAGGER_DISPLAY_HOST: localHost:8082
SWAGGER_DISPLAY_PATH: /Codes
SWAGGER_DISPLAY_PROTOCOL: http
SWAGGER_SERVICES_CORE_URL:
SWAGGER_SERVICES_LOOKUPS_URL:

ROOT_LOG_LEVEL: INFO

QW_PORTAL_SERVICES_IPV4:
```

## Automated Testing

### Set up a local database
This application has two flavors of automated tests: unit tests (\*Test.java) and integration tests (\*IT.java) which require a database.
The docker-compose commands in https://github.com/NWQMC/schema-wqp-core can be used to create the CI database

### Testing with an IDE or command line

Use the command ```mvn test``` in the project root directory to run the unit tests.

Use the command ```mvn verify``` in the project root directory to to run integration tests.

Configuration information will be pulled from your local Maven settings.xml file. Add the following profile to the settings.xml file:
```$xml
<profile>
  <activation>
      <activeByDefault>true</activeByDefault>
  </activation>
    <id>it</id>
    <properties>
        <wqpCore.url>jdbc:postgresql://127.0.0.1:5435/wqp_db</wqpCore.url>
        <wqpCore.username>wqp_core</wqpCore.username>
        <wqpCore.password>changeMe</wqpCore.password>
    </properties>
</profile>
```
Where the ```<properties>``` values should be substituted to reflect your local CI database setup.

## Building the docker images
You will need to provide a secrets.env that has valid values for the values in secrets.env.sample. This file
plus config.env will contain the environment variables for the application. The docker image builds the jar and then runs
the application. The application will be available at localhost:8080/qw_portal_services
