### e-RATOS (backend+compiled front-end)

[See WiKi](https://github.com/Popov85/ratos3/wiki)

e-RATOS (Embeddable Remote Automatised Teaching and Controlling System)
This is a web-based application that provides a rich extensive toolset for students' knowledge control 
via classical tests. The app can be used by medium and large educational organisations like schools, colleges or universities 
in multiple or mixed knowledge domains: health care, linguistics, natural sciences, etc. with significant loading 
like 10000+ learning sessions per day, with 1000+ simultaneous active sessions of 20-200 questions, accessed from
within any LTI 1.x-compatible [LMS](https://en.wikipedia.org/wiki/Learning_management_system) like [edX](https://github.com/edx), [Moodle](https://moodle.org/), [Sakai](https://github.com/sakaiproject/sakai), etc. 
as well as directly from within Intranet or Internet network via a browser.

#### Stack of technologies:
##### Backend:

- Java 17;
- Spring Boot 2.5.x;
- Hibernate;
- RDBMS (MySql 8.x);
- Maven.

##### Frontend:
- React.js (sources [here](https://github.com/Popov85/ratos3-frontend))

#### Hardware config:
Varies depending on the scale of your organization: quantity of simultaneous users and quantity of questions.
E.g. for an organization of 10000 concurrent students, and staff of 500 people with questions base of 500000+, 
minimal requirement is the following:
- RAM 16Gb, with heap size > 8Gb;
- Single quad core CPU;

#### Most recent Docker build:

[Here](https://hub.docker.com/repository/docker/gelever85/ratos3)<br>

**Warning!**

You need to create a Docker hub account to access the image.

#### Build and push fresh docker image

Docker file creation with Jib Maven plugin info:

 - Set application.yml -> profile=stage/prod
 - `./mvnw clean compile`  [Jib requires compiled project]
 - `./mvnw jib:build -Djib.to.auth.username=${DOCKER_HUB_USERNAME} -Djib.to.auth.password=${DOCKER_HUB_PASSWORD}`

(Specify you credentials from DockerHub)

#### Local run

##### IDE Intellij:

- Make sure to use JDK 17 in the project;
- Install [EnvFile](https://github.com/Ashald/EnvFile/blob/develop/README.md) plugin;

**SpringBoot config**
- Add SpringBoot configuration
- Enable env. variable file
- Add env file as the .env file
- Set up MySql database locally (v 8.x)
- Run locally

**Docker-compose config [info](https://www.jetbrains.com/help/idea/docker-compose.html):**
- Add Docker-compose configuration
- Specify docker-compose.yml
- Specify .env variables file
- Run locally

##### IDE agnostic:

- Install Docker [runtime](https://docs.docker.com/engine/)
- Install [Docker-compose](https://github.com/docker/compose/releases)
- Put .env file with all required environment variables into the same folder where docker-compose.yaml is located.
- Run `docker-compose up -d` to start the app;
- Run `docker-compose down` to stop the app;

#### Run tests

(Make sure to use Java 17 as default system JDK)

`export JAVA_HOME=/usr/lib/jvm/java-${JDK_VERSION_PACKAGE}`

`export PATH=$JAVA_HOME/bin:$PATH`

`java -version`

**Unit tests**

`./mvnw clean test`

**IT tests**

`./mvnw clean verify`

**Local Terminal run**

 - Export env. variables from .env file

`export $(grep -v '^#' .env | xargs)`

 - Run the app via Maven

`./mvnw spring-boot:run`

**Important!**

By default, h2 profile is used to launch IT tests.
To execute IT tests with MySql RDBMS, change the profile to mysql in core application.yml in test section.

Additionally, change the value of ua.edu.ratos.ActiveProfile NOW=mysql


#### Run DB operations

`./mvnw flyway:clean -Dflyway.user=${DATABASE_USER} -Dflyway.password=${DATABASE_PASSWORD}` - clean local DB

`./mvnw flyway:migrate -Dflyway.user=${DATABASE_USER} -Dflyway.password=${DATABASE_PASSWORD}` - migrate local DB

`./mvnw flyway:validate -Dflyway.user=${DATABASE_USER} -Dflyway.password=${DATABASE_PASSWORD}` - validate local DB

(Specify you credentials from DB)

Alternatively, locate settings.xml (with DB credentials) in .m2 folder on local PC
