FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY . .
RUN sh mvnw clean package
EXPOSE 8080
CMD ["java", "-jar", "target/inventory-service.jar"]
