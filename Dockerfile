FROM openjdk:11-jre-slim AS jre

LABEL maintainer='Sidney Miranda <github/sidneymiranda>'

WORKDIR /app

COPY . .

CMD cd app | ls

ENTRYPOINT ["java", "-jar", "account-manager.jar"]