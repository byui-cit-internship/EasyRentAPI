FROM maven:3.6.3-adoptopenjdk-14 AS MAVEN_BUILD

WORKDIR /opt/easyrent

COPY . ./

RUN mvn clean package

FROM adoptopenjdk/openjdk14

COPY --from=MAVEN_BUILD /opt/easyrent/target/EasyRentAPI-1.0-SNAPSHOT.jar /easyrent.jar

ENTRYPOINT ["java", "-jar", "/easyrent.jar"]

EXPOSE 4567

