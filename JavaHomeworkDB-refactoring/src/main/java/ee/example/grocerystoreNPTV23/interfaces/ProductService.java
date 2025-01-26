package ee.example.grocerystoreNPTV23.interfaces;

import ee.example.grocerystoreNPTV23.entity.Product;
import java.util.List;

public interface ProductService {
    void addProduct(String name, double price, int quantity);
    void editProduct(Long id, String name, double price, int quantity);
    String getAllProductsFormatted();
    List<Product> getAllProducts();
    Product getProductById(Long id);
    void decreaseProductQuantity(Long id, int quantity);
}
