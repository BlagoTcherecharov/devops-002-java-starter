package training.devops.inventory;

import java.util.Map;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class InventoryApplication {
    public static void main(String[] args) {
        SpringApplication.run(InventoryApplication.class, args);
    }

    @Bean
    InfoContributor inventoryInfo() {
        return builder -> builder.withDetail("inventory", Map.of("storage", "in-memory", "products", 3));
    }
}
