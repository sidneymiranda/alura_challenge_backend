FROM openjdk:11-jre-slim AS jre

LABEL maintainer='Sidney Miranda <github/sidneymiranda>'

WORKDIR /app

COPY . .

ENTRYPOINT ["java", "-jar", "account-manager.jar"]