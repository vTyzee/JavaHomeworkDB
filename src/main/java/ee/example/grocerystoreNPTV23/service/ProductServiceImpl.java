package ee.example.grocerystoreNPTV23.services;

import ee.example.grocerystoreNPTV23.entity.Product;
import ee.example.grocerystoreNPTV23.helpers.ProductHelper;
import ee.example.grocerystoreNPTV23.interfaces.Input;
import ee.example.grocerystoreNPTV23.interfaces.ProductService;
import ee.example.grocerystoreNPTV23.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final Input input;

    public ProductServiceImpl(ProductRepository productRepository, Input input) {
        this.productRepository = productRepository;
        this.input = input;
    }

    @Override
    public boolean add() {
        try {
            System.out.print("Введите название продукта: ");
            String name = input.getString();
            System.out.print("Введите цену продукта: ");
            double price = input.getDouble();
            System.out.print("Введите количество продукта: ");
            int quantity = input.getInt();
            Product product = ProductHelper.createProduct(name, price, quantity);
            productRepository.save(product);
            System.out.println("Продукт успешно добавлен!");
            return true;
        } catch (Exception e) {
            System.out.println("Ошибка при добавлении продукта: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean edit() {
        try {
            System.out.print("Введите ID продукта для редактирования: ");
            Long productId = input.getLong();
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new IllegalArgumentException("Продукт с таким ID не найден."));
            System.out.print("Введите новое название (или оставьте пустым): ");
            String newName = input.getString();
            if (!newName.isEmpty()) {
                product.setName(newName);
            }
            System.out.print("Введите новую цену (или 0, чтобы пропустить): ");
            double newPrice = input.getDouble();
            if (newPrice > 0) {
                product.setPrice(newPrice);
            }
            System.out.print("Введите новое количество (или -1, чтобы пропустить): ");
            int newQuantity = input.getInt();
            if (newQuantity >= 0) {
                product.setQuantity(newQuantity);
            }
            productRepository.save(product);
            System.out.println("Продукт успешно обновлён!");
            return true;
        } catch (Exception e) {
            System.out.println("Ошибка при редактировании продукта: " + e.getMessage());
            return false;
        }
    }

    @Override
    public void print() {
        List<Product> products = productRepository.findAll();
        if (products.isEmpty()) {
            System.out.println("Нет продуктов в базе данных.");
            return;
        }
        var formatted = ProductHelper.formatProducts(products);
        System.out.println("Список всех продуктов:");
        formatted.forEach(System.out::println);
    }

    @Override
    public Product findProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Продукт с таким ID не найден."));
    }
}
