package training.devops.inventory;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;
import tools.jackson.databind.json.JsonMapper;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class InventoryApplicationTests {
    @Value("${local.server.port}")
    int port;
    final HttpClient client = HttpClient.newHttpClient();
    final JsonMapper json = new JsonMapper();

    HttpResponse<String> get(String path) throws Exception {
        return client.send(HttpRequest.newBuilder(URI.create("http://localhost:" + port + path)).build(), HttpResponse.BodyHandlers.ofString());
    }

    @Test void rootDescribesService() throws Exception {
        var response = get("/");
        assertEquals(200, response.statusCode());
        assertTrue(response.headers().firstValue("content-type").orElse("").contains("application/json"));
        var body = json.readTree(response.body());
        assertEquals("Inventory Service", body.get("service").asText());
        assertEquals("1.0.0", body.get("version").asText());
    }

    @Test void productsAreStableAndComplete() throws Exception {
        var response = get("/api/products");
        assertEquals(200, response.statusCode());
        assertEquals(json.readTree("[{\"id\":1,\"name\":\"Keyboard\",\"quantity\":12},{\"id\":2,\"name\":\"Mouse\",\"quantity\":25},{\"id\":3,\"name\":\"Monitor\",\"quantity\":7}]"), json.readTree(response.body()));
        for (int id = 1; id <= 3; id++) {
            var product = get("/api/products/" + id);
            assertEquals(200, product.statusCode());
            assertEquals(json.readTree(response.body()).get(id - 1), json.readTree(product.body()));
        }
    }

    @Test void unknownProductReturns404() throws Exception {
        assertEquals(404, get("/api/products/999").statusCode());
        assertEquals(404, get("/api/products/-1").statusCode());
    }

    @Test void invalidIdentifierReturns400() throws Exception {
        assertEquals(400, get("/api/products/nope").statusCode());
    }

    @Test void healthIsUpWithoutDetails() throws Exception {
        var response = get("/actuator/health");
        assertEquals(200, response.statusCode());
        assertEquals(json.readTree("{\"status\":\"UP\"}"), json.readTree(response.body()));
    }

    @Test void managementInfoIsNotExposed() throws Exception {
        assertEquals(404, get("/actuator/info").statusCode());
    }
}
