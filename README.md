# qw\_portal\_services
Water Quality Portal (WQP) Lookup Service

## Local Configuration
This application is configured to be run as a jar. It can also be run using the command ``` mvn spring-boot:run ``` in the project root directory.
 
To run in a development environment, create an application.yml file in
the project root directory containing the following (shown are example values):

```$yml
WQP_DATABASE_ADDRESS: <127.0.0.1>
WQP_DATABASE_PORT: <5435>
WQP_DB_READ_ONLY_USERNAME: <wqp_user>
WQP_DB_READ_ONLY_PASSWORD: <changeMe>
WQP_DATABASE_NAME: <wqp_db>

SERVER_PORT: 8082
SERVER_CONTEXT_PATH: /Codes

SWAGGER_DISPLAY_HOST: localHost:8082
SWAGGER_DISPLAY_PATH: /Codes
SWAGGER_DISPLAY_PROTOCOL: http
SWAGGER_SERVICES_CORE_URL:
SWAGGER_SERVICES_LOOKUPS_URL:

ROOT_LOG_LEVEL: INFO
```

## Testing
This project contains JUnit 5 tests. Maven can be used to run them (in addition to the capabilities of your IDE).

To run the unit tests of the application use:

```shell
mvn package
```

To additionally start up a Docker database and run the integration tests of the application use:

```shell
mvn verify -DTESTING_DATABASE_PORT=5437 -DTESTING_DATABASE_ADDRESS=localhost -DTESTING_DATABASE_NETWORK=wqp
```

## Building the docker images
You will need to provide a secrets.env that has valid values for the values in secrets.env.sample. This file
plus config.env will contain the environment variables for the application. The docker image builds the jar and then runs
the application. The application will be available at localhost:8080/qw_portal_services
