# qw\_portal\_services
Water Quality Portal (WQP) Lookup Service

## Local Configuration
This application is configured to be run in a Tomcat container. The development configuration (context.xml and swaggerServices.yml) can be copied to your local Tomcat. Required configuration is:

```
    <Environment name="qwPortalServices/displayHost" type="java.lang.String" value="localhost:8443"/>
    <Environment name="qwPortalServices/displayPath" type="java.lang.String" value="/qw_portal_services"/>
    <Environment name="swaggerServicesConfigFile" type="java.lang.String" value="${catalina.base}/conf/swaggerServices.yml"/>"
    <Resource name="jdbc/WQPQW" 
        .
        .
        .
    />
```

Security can be enabled by adding the following to the Tomcat's context.xml:

```
    <Parameter name="spring.profiles.active" value="default,swagger,internal" />
    <Parameter name="oauthResourceKeyUri" value = "http://localhost:8081/localauth/oauth/token_key"/>
```

## Automated Testing
This application has two flavors of automated tests: unit tests (\*Test.java) and integration tests (\*IT.java) which require a database.

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
