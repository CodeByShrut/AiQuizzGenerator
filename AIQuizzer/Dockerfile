FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY target/AIQuizzer-*.jar app.jar

EXPOSE 8081

ENTRYPOINT ["sh", "-c", "exec java -jar app.jar --spring.profiles.active=${SPRING_PROFILE:-default}"]