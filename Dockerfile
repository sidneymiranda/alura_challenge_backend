FROM openjdk:11.0.1-jre-slim

LABEL maintainer='Sidney Miranda <github/sidneymiranda>'

WORKDIR /app

ENV SPRING_PROFILES_ACTIVE=prod

COPY target/account-manager.jar /app

ENTRYPOINT ["java","-jar", "account-manager.jar"]