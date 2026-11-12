FROM eclipse-temurin:17-jdk AS build
WORKDIR /app
COPY . .
RUN sh mvnw -B clean package -DskipTests

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/inventory-service.jar inventory-service.jar
EXPOSE 8080
CMD ["java", "-jar", "inventory-service.jar"]
