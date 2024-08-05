FROM openjdk:17-jdk
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar

# PEM 파일을 Docker 이미지에 복사
COPY fullchain.pem /app/fullchain.pem
COPY privkey.pem /app/privkey.pem

ENTRYPOINT ["java", "-Dspring.profiles.active=docker", "-jar", "app.jar"]