FROM openjdk:11-jre-slim AS jre

LABEL maintainer='Sidney Miranda <github/sidneymiranda>'

WORKDIR /app

COPY ./target/account-manager.jar .

ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "account-manager.jar"]