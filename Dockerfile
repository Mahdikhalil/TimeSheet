FROM openjdk:8-jdk-alpine
ARG DEFAULT_PATH=target/Timesheet-spring-boot-core-data-jpa-mvc-REST-1-0.0.1-SNAPSHOT.jar
WORKDIR /
ENV JAR_PATH=$DEFAULT_PATH
COPY ${JAR_PATH} app.jar

EXPOSE 8086
CMD ["java", "-jar", "-Dspring.profiles.active=prod", "app.jar"]
