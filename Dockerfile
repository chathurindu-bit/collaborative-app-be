# syntax=docker/dockerfile:1

# ---------------------------------------------------------------------------
# UrMine backend
#
# The project is packaged as a WAR with spring-boot-starter-tomcat marked
# "provided", so it is built here and deployed to a standalone Tomcat
# container. Spring Boot 4.1 requires Servlet 6.1, which is Tomcat 11.0.x.
#
# Build from the repository root:
#   docker build -f urmine/Dockerfile -t urmine-be ./urmine
#
# Run:
#   docker run --rm -p 8080:8080 urmine-be
#
# The frontend connects to ws://<host>:8080/drawing
# ---------------------------------------------------------------------------


# --- Stage 1: build the WAR -------------------------------------------------
FROM maven:3.9-eclipse-temurin-17 AS build

WORKDIR /build

# Copy the POM on its own first. This layer only invalidates when the
# dependencies change, so Docker keeps the downloaded Maven repository
# cached across source-only edits.
COPY pom.xml .

RUN --mount=type=cache,target=/root/.m2 \
    mvn -B -q dependency:go-offline

COPY src ./src

# -DskipTests: the test context load needs no database here, but skipping
# keeps the image build fast and independent of test fixtures.
RUN --mount=type=cache,target=/root/.m2 \
    mvn -B -q clean package -DskipTests


# --- Stage 2: runtime --------------------------------------------------------
FROM tomcat:11.0-jre17

LABEL org.opencontainers.image.title="UrMine backend" \
      org.opencontainers.image.description="Spring Boot STOMP drawing relay"

# Remove the bundled example webapps so they are not deployed alongside
# the application.
RUN rm -rf /usr/local/tomcat/webapps/*

# ROOT.war deploys at the context root "/", so the STOMP endpoint is
# reachable at ws://host:8080/drawing with no extra path segment.
COPY --from=build /build/target/urmine-0.0.1-SNAPSHOT.war \
     /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080

# The base image already runs "catalina.sh run" and ships no entrypoint,
# which is exactly what is needed here.