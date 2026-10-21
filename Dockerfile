FROM maven:3.9-eclipse-temurin-17 AS build

WORKDIR /build

COPY pom.xml .

RUN --mount=type=cache,target=/root/.m2 \
    mvn -B -q dependency:go-offline

COPY src ./src

RUN --mount=type=cache,target=/root/.m2 \
    mvn -B -q clean package -DskipTests

FROM tomcat:11.0-jre17

LABEL org.opencontainers.image.title="UrMine backend" \
      org.opencontainers.image.description="Spring Boot STOMP drawing relay"


RUN rm -rf /usr/local/tomcat/webapps/*

COPY --from=build /build/target/urmine-0.0.1-SNAPSHOT.war \
     /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080
