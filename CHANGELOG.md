# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com/en/1.0.0/)
and this project adheres to [Semantic Versioning](http://semver.org/spec/v2.0.0.html). (Patch version X.Y.0 is implied if not specified.)

## [Unreleased]

### Added
- CHANGELOG.md
- README.md
- Converted project to Spring Boot 2.
- OAuth2 Security for Internal App.
- Dockerized project in preparation for building a docker image suitable for deployment.
- pipeline.yml and two Jenkinsfiles

### Changes
- Changed the pom to create a jar.
- Updated Jenkinsfiles to use new pipeline library and added new variables to pipeline.yml
- Updated Dockerfile to use new openJDK image, and changed RUN command to comply with best practices


[Unreleased]: https://github.com/NWQMC/qw_portal_services/compare/qw_portal_services-1.4...master
