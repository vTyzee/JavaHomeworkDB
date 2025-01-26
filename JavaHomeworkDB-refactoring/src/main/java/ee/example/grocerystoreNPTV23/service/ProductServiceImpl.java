package ee.example.grocerystoreNPTV23.services;

import ee.example.grocerystoreNPTV23.entity.Product;
import ee.example.grocerystoreNPTV23.helpers.ProductHelper;
import ee.example.grocerystoreNPTV23.interfaces.ProductService;
import ee.example.grocerystoreNPTV23.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void addProduct(String name, double price, int quantity) {
        var product = ProductHelper.createProduct(name, price, quantity);
        productRepository.save(product);
    }

    @Override
    public void editProduct(Long id, String name, double price, int quantity) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Продукт с таким ID не найден."));

        if (name != null && !name.trim().isEmpty()) {
            product.setName(name.trim());
        }
        if (price > 0) {
            product.setPrice(price);
        }
        if (quantity >= 0) {
            product.setQuantity(quantity);
        }
        productRepository.save(product);
    }

    @Override
    public String getAllProductsFormatted() {
        List<Product> products = productRepository.findAll();
        var formattedList = ProductHelper.formatProducts(products);
        return String.join("\n", formattedList);
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Продукт с таким ID не найден."));
    }

    @Override
    public void decreaseProductQuantity(Long id, int quantity) {
        Product product = getProductById(id);
        if (product.getQuantity() < quantity) {
            throw new IllegalArgumentException("Недостаточно товара на складе.");
        }
        product.setQuantity(product.getQuantity() - quantity);
        productRepository.save(product);
    }
}
