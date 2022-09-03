FROM openjdk:11

LABEL maintainer='Sidney Miranda <github/sidneymiranda>'

WORKDIR /app

ENV SPRING_PROFILES_ACTIVE=prod

COPY target/account-manager.jar /app/account-manager.jar

ENTRYPOINT ["java","-jar", "account-manager.jar"]