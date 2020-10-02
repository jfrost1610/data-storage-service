FROM maven:3.6-jdk-8-alpine as build
ADD ./ app/storageservice/
WORKDIR /app/storageservice
RUN mvn install -DskipTests

FROM openjdk:8-jdk-alpine
COPY --from=build /app/storageservice/target/*.jar storageserviceapp.jar
EXPOSE 8082
ENTRYPOINT ["java","-jar","-Dspring.profiles.active=dev","storageserviceapp.jar"]
