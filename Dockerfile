FROM maven:3.9.9 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests && ls -l /app/target

FROM openjdk:8
COPY --from=build /app/target/wiremock-docker-1.0-SNAPSHOT.jar /app/wiremock.jar
COPY --from=build /app/src/test/resources /app/resources
COPY --from=build /app/target/lib /app/lib
CMD ["java", "-cp", "/app/lib/*:/app/wiremock.jar", "com.hill.WireMockPresetStubs"]