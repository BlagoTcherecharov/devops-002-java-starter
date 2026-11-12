# DEVOPS-002 Inventory Service

Starter for [Repair, Build and Containerize a Java Application](https://merqreal.github.io/devops-training-projects/projects/build-and-release-engineering/java-inventory/).

Use JDK 17 and the Maven Wrapper (`./mvnw clean package`, or `mvnw.cmd clean package` on Windows). The build needs network access to Maven Central. The initial build is intentionally unsuccessful. Diagnose build and runtime behavior; Java source and tests must remain unchanged.

The service uses static data, listens on port 8080, and exposes JSON:

- `GET /`: service name and version.
- `GET /api/products`: three products (Keyboard, Mouse, Monitor).
- `GET /api/products/{id}`: product or HTTP 404; invalid numeric identifiers return HTTP 400.
- `GET /actuator/health`: HTTP 200 with `{"status":"UP"}`.

## Running with the image from GHCR

To run the service from the image published to GitHub Container Registry instead of building it locally, use the GHCR compose file:

```bash
docker compose -f docker-compose-with-ghcr.yaml up -d
```

Plain `docker compose up` still uses `docker-compose.yml`, which builds the image from the local source.

Create your own Dockerfile and Compose configuration. Build from source, run the packaged JAR with `java -jar`, bind only `127.0.0.1:8080`, and add a container health check against `/actuator/health`. No database, credentials, or external services are needed. Record build output, container logs, investigation, corrections, endpoint checks and cleanup in your troubleshooting report. Do not commit generated JARs or logs containing sensitive data.
