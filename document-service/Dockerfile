FROM maven:3.6-jdk-8-alpine as build
ADD ./ app/documentservice/
WORKDIR /app/documentservice
RUN mvn install -DskipTests

FROM openjdk:8-jdk-alpine
COPY --from=build /app/documentservice/target/*.jar documentserviceapp.jar
EXPOSE 8081
ENTRYPOINT ["java","-jar","-Dspring.profiles.active=dev","documentserviceapp.jar"]
