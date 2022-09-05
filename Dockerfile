FROM openjdk:11

LABEL maintainer='Sidney Miranda <github/sidneymiranda>'

RUN mkdir /app

WORKDIR /app

ENV SPRING_PROFILES_ACTIVE=prod

COPY target/account-manager.jar /app

ENTRYPOINT ["java","-jar", "account-manager.jar"]