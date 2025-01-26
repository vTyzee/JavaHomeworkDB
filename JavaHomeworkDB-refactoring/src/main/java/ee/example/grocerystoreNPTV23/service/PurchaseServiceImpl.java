package ee.example.grocerystoreNPTV23.services;

import ee.example.grocerystoreNPTV23.entity.Customer;
import ee.example.grocerystoreNPTV23.entity.Product;
import ee.example.grocerystoreNPTV23.entity.Purchase;
import ee.example.grocerystoreNPTV23.helpers.PurchaseHelper;
import ee.example.grocerystoreNPTV23.interfaces.CustomerService;
import ee.example.grocerystoreNPTV23.interfaces.ProductService;
import ee.example.grocerystoreNPTV23.interfaces.PurchaseService;
import ee.example.grocerystoreNPTV23.repository.PurchaseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class PurchaseServiceImpl implements PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final CustomerService customerService;
    private final ProductService productService;

    public PurchaseServiceImpl(PurchaseRepository purchaseRepository,
                               CustomerService customerService,
                               ProductService productService) {
        this.purchaseRepository = purchaseRepository;
        this.customerService = customerService;
        this.productService = productService;
    }

    @Transactional
    @Override
    public void purchaseProduct(Long customerId, Long productId, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Количество должно быть положительным.");
        }
        // Получаем сущности
        Customer customer = customerService.getCustomerById(customerId);
        Product product = productService.getProductById(productId);

        // Проверка баланса/количества
        double totalPrice = product.getPrice() * quantity;
        if (customer.getBalance() < totalPrice) {
            throw new IllegalArgumentException("Недостаточно средств для покупки.");
        }
        if (product.getQuantity() < quantity) {
            throw new IllegalArgumentException("Недостаточно товара на складе.");
        }

        // Обновляем
        customerService.decreaseCustomerBalance(customerId, totalPrice);
        productService.decreaseProductQuantity(productId, quantity);

        // Записываем покупку
        Purchase purchase = new Purchase(customer, product, quantity, LocalDateTime.now());
        purchaseRepository.save(purchase);
    }

    @Override
    public String getAllPurchasesFormatted() {
        List<Purchase> purchases = purchaseRepository.findAll();
        var formattedList = PurchaseHelper.formatPurchases(purchases);
        return String.join("\n", formattedList);
    }

    @Override
    public double getIncome(LocalDate start, LocalDate end) {
        LocalDateTime startDateTime = start.atStartOfDay();
        LocalDateTime endDateTime = end.atTime(LocalTime.MAX);
        List<Purchase> purchases =
                purchaseRepository.findByPurchaseDateBetween(startDateTime, endDateTime);
        return purchases.stream()
                .mapToDouble(p -> p.getProduct().getPrice() * p.getQuantity())
                .sum();
    }
}
