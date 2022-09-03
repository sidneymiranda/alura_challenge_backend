FROM openjdk:11-jre-slim AS jre

LABEL maintainer='Sidney Miranda <github/sidneymiranda>'

ENV SPRING_PROFILES_ACTIVE=prod

ADD target/account-manager.jar .

ENTRYPOINT ["java","-jar", "account-manager.jar"]