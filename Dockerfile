FROM openjdk:17-ea-slim
WORKDIR /app
COPY . .
RUN sh mvnw clean package
EXPOSE 8080
CMD ["java", "-jar", "target/inventory-service.jar"]
