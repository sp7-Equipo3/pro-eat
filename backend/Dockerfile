FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY spring/pom.xml spring/
COPY spring/src spring/src
RUN mvn clean install -f spring/pom.xml -DskipTests

FROM eclipse-temurin:17-jre-jammy
VOLUME /tmp
COPY --from=build /app/spring/target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]