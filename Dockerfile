FROM openjdk:11-jre-slim AS jre

LABEL maintainer='Sidney Miranda <github/sidneymiranda>'

WORKDIR /app

COPY . .

CMD ["java", "-Dspring-boot.run.profiles=${SPRING_PROFILE}", "-jar", "account-manager.jar"]