package training.devops.inventory;

import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController {
    public record Product(long id, String name, int quantity) {}

    private static final List<Product> PRODUCTS = List.of(
        new Product(1, "Keyboard", 12),
        new Product(2, "Mouse", 25),
        new Product(3, "Monitor", 7)
    );

    @GetMapping("/")
    public Map<String, String> index() {
        return Map.of("service", "Inventory Service", "version", "1.0.0");
    }

    @GetMapping("/api/products")
    public List<Product> products() {
        return PRODUCTS;
    }

    @GetMapping("/api/products/{id}")
    public ResponseEntity<Product> product(@PathVariable("id") long id) {
        return PRODUCTS.stream().filter(product -> product.id() == id)
            .findFirst().map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
