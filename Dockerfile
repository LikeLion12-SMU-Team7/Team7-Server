FROM openjdk:17-jdk
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
#COPY src/main/resources/keystore.p12 /app/keystore.p12
ENTRYPOINT ["java", "-Dspring.profiles.active=docker", "-jar", "app.jar"]