FROM openjdk:11-jre-slim AS jre

LABEL maintainer='Sidney Miranda <github/sidneymiranda>'

ENV SPRING_PROFILES_ACTIVE=prod

WORKDIR /app

COPY target/account-manager.jar /app/account-manager.jar

ENTRYPOINT ["java","-jar", "account-manager.jar"]