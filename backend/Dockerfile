FROM eclipse-temurin:17-jdk-jammy
VOLUME /tmp
ARG JAR_FILE=spring/target/spring-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]