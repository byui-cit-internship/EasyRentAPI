FROM maven:3.6.1-jdk-8-alpine AS MAVEN_BUILD

WORKDIR /opt/easyrent

COPY . ./

RUN mvn clean package

FROM openjdk:8-jre-alpine3.9

COPY --from=MAVEN_BUILD /opt/easyrent/target/EasyRentAPI-1.0-SNAPSHOT.jar /easyrent.jar

ENTRYPOINT ["java", "-jar", "/easyrent.jar"]

EXPOSE 4567

