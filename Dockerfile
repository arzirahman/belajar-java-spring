From maven:3.9.6-eclipse-temurin-21-alpine
COPY . /app
WORKDIR /app
RUN mvn clean install
ENTRYPOINT ["java", "-jar", "target/ordermakanan-0.0.1.jar"]