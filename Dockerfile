FROM openjdk:20
WORKDIR /app
COPY ./target/Phone_contacts-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
