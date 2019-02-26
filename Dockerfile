FROM maven:3.6.0-jdk-8 AS build

LABEL maintainer=gs-w_eto2@usgs.gov

# Add pom.xml and install dependencies
COPY pom.xml /build/pom.xml
WORKDIR /build
RUN mvn clean

# Add source code and (by default) build the jar
COPY src /build/src
ARG BUILD_COMMAND="mvn package -Dmaven.test.skip=true"
RUN ${BUILD_COMMAND}


FROM usgswma/wma-spring-boot-base:8-jre-slim

ENV HEALTHY_RESPONSE_CONTAINS='{"status":"UP"}'
COPY --from=build /build/target/qw_portal_services.jar app.jar
HEALTHCHECK --interval=30s --timeout=3s \
  CMD curl -k "http://127.0.0.1:8080/actuator/health" | grep -q ${HEALTHY_RESPONSE_CONTAINS} || exit 1