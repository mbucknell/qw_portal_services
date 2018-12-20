# qw\_portal\_services
Water Quality Portal (WQP) Lookup Service

## Local Configuration
This application is configured to be run as a jar. It can also be run using the command ``` mvn spring-boot:run ``` . 
To run in a development environment, create an application.yml file in
the project's home directory with the following:

```
wqpDbHost: 127.0.0.1
wqpDbPort: 5434
wqpDbUsername: wqp_user
wqpDbUserPassword: wqp_user_password

serverPort: 8082
serverContextPath: /qw_portal_services

qwDisplayHost: localHost:8082
qwDisplayPath: /qw_portal_services
qwDisplayProtocol: http
```


## Automated Testing
This application has two flavors of automated tests: unit tests (\*Test.java) and integration tests (\*IT.java) which require a database.
The docker-compose commands in https://github.com/NWQMC/schema-wqp-core can be used to create the CI database

### Testing with an IDE
An application-it.yml file needs to be created in the project root directory in order to run the integration tests. It should contain:

```
wqpUrl: <<url for ci database>>
wqpOwnerUsername: <<database username>>
wqpOwnerPassword: <<database password>>
```

### Testing with Maven
The unit tests can be run in a terminal with the maven command ```mvn test``` in the project's root directory.

The integration tests can be run in a terminal with the maven command ```mvn verify``` in the project's root directory.
When run this way, configuration information will be pulled from the maven setting.xml file. It will need to contain the following profile:
```
  <profile>
    <id>it</id>
    <properties>
      <wqpUrl><<url for ci database>></wqpUrl>
      <wqpOwnerUsername><<database username>></wqpOwnerUsername>
      <wqpOwnerPassword><<database password>></wqpOwnerPassword>
    </properties>
  </profile>
```

## Building the docker images
You will need to provide a secrets.env that has valid values for the values in secrets.env.sample. This file
plus config.env will contain the environment variables for the application. The docker image builds the jar and then runs
the application. The application will be available at localhost:8080/qw_portal_services
