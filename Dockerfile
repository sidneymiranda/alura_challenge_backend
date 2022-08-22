FROM openjdk:11-jre-slim AS jre

LABEL maintainer = 'Sidney Miranda<github/sidneymiranda>'

ENV SERVER_PORT=8080

WORKDIR /app

COPY ./target/account-manager.jar .

EXPOSE $SERVER_PORT

ENTRYPOINT ["java", "-jar", "account-manager.jar"]