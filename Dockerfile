FROM eclipse-temurin:17-jdk AS build
WORKDIR /app

# Build files first: dependencies are downloaded into this layer,
# which is reused until pom.xml or the Maven wrapper changes
COPY mvnw pom.xml ./
COPY .mvn .mvn
RUN sh mvnw -B dependency:go-offline

# Source last: a code change only re-runs this part
COPY src src
RUN sh mvnw -B package -DskipTests

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/inventory-service.jar inventory-service.jar
EXPOSE 8080
CMD ["java", "-jar", "inventory-service.jar"]
