package ee.example.grocerystoreNPTV23.helpers;

import ee.example.grocerystoreNPTV23.entity.Product;

import java.util.List;
import java.util.stream.Collectors;

public class ProductHelper {

    public static List<String> formatProducts(List<Product> products) {
        return products.stream()
                .map(p -> String.format(
                        "ID: %d, Название: %s, Цена: %.2f, Количество: %d",
                        p.getId(),
                        p.getName(),
                        p.getPrice(),
                        p.getQuantity()
                ))
                .collect(Collectors.toList());
    }

    public static boolean isValidProduct(Product product) {
        return product != null
                && product.getName() != null && !product.getName().trim().isEmpty()
                && product.getPrice() > 0
                && product.getQuantity() >= 0;
    }

    public static Product createProduct(String name, double price, int quantity) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Название продукта должно быть заполнено.");
        }
        if (price <= 0) {
            throw new IllegalArgumentException("Цена должна быть положительной.");
        }
        if (quantity < 0) {
            throw new IllegalArgumentException("Количество не может быть отрицательным.");
        }
        return new Product(name, price, quantity);
    }
}
