FROM openjdk:11-jre-slim AS jre

LABEL maintainer='Sidney Miranda <github/sidneymiranda>'

ENV SPRING_PROFILES_ACTIVE=prod

COPY target/account-manager.jar ./

ENTRYPOINT ["java", "-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE}" ,"-jar", "/account-manager.jar"]