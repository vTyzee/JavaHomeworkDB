package ee.example.grocerystoreNPTV23.services;

import ee.example.grocerystoreNPTV23.entity.Customer;
import ee.example.grocerystoreNPTV23.entity.Product;
import ee.example.grocerystoreNPTV23.entity.Purchase;
import ee.example.grocerystoreNPTV23.helpers.PurchaseHelper;
import ee.example.grocerystoreNPTV23.interfaces.CustomerService;
import ee.example.grocerystoreNPTV23.interfaces.Input;
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
    private final Input input;

    public PurchaseServiceImpl(
            PurchaseRepository purchaseRepository,
            CustomerService customerService,
            ProductService productService,
            Input input
    ) {
        this.purchaseRepository = purchaseRepository;
        this.customerService = customerService;
        this.productService = productService;
        this.input = input;
    }

    @Transactional
    @Override
    public boolean purchase() {
        try {
            System.out.print("Введите ID покупателя: ");
            Long customerId = input.getLong();
            System.out.print("Введите ID продукта: ");
            Long productId = input.getLong();
            System.out.print("Введите количество для покупки: ");
            int quantity = input.getInt();
            if (quantity <= 0) {
                throw new IllegalArgumentException("Количество должно быть положительным.");
            }
            Customer customer = customerService.findCustomerById(customerId);
            Product product = productService.findProductById(productId);
            double totalPrice = product.getPrice() * quantity;
            if (customer.getBalance() < totalPrice) {
                throw new IllegalArgumentException("Недостаточно средств для покупки.");
            }
            if (product.getQuantity() < quantity) {
                throw new IllegalArgumentException("Недостаточно товара на складе.");
            }
            customer.setBalance(customer.getBalance() - totalPrice);
            product.setQuantity(product.getQuantity() - quantity);
            Purchase purchase = new Purchase(customer, product, quantity, LocalDateTime.now());
            purchaseRepository.save(purchase);
            System.out.println("Покупка успешно завершена!");
            return true;
        } catch (Exception e) {
            System.out.println("Ошибка при покупке: " + e.getMessage());
            return false;
        }
    }

    @Override
    public void showIncome() {
        try {
            System.out.print("Введите год (например, 2025): ");
            int year = input.getInt();
            System.out.print("Введите месяц (1-12): ");
            int month = input.getInt();
            System.out.print("Введите день (1-31): ");
            int day = input.getInt();
            LocalDate date = LocalDate.of(year, month, day);
            double dailyIncome = getIncome(date, date);
            System.out.printf("Доход за %s: %.2f%n", date, dailyIncome);
            System.out.print("Введите месяц для расчёта дохода (1-12): ");
            month = input.getInt();
            LocalDate startOfMonth = LocalDate.of(year, month, 1);
            LocalDate endOfMonth = startOfMonth.withDayOfMonth(startOfMonth.lengthOfMonth());
            double monthlyIncome = getIncome(startOfMonth, endOfMonth);
            System.out.printf("Доход за %d/%d: %.2f%n", month, year, monthlyIncome);
            System.out.print("Введите год для расчёта дохода (например, 2025): ");
            year = input.getInt();
            LocalDate startOfYear = LocalDate.of(year, 1, 1);
            LocalDate endOfYear = LocalDate.of(year, 12, 31);
            double yearlyIncome = getIncome(startOfYear, endOfYear);
            System.out.printf("Доход за %d: %.2f%n", year, yearlyIncome);
        } catch (Exception e) {
            System.out.println("Ошибка при вычислении дохода: " + e.getMessage());
        }
    }

    private double getIncome(LocalDate start, LocalDate end) {
        LocalDateTime s = start.atStartOfDay();
        LocalDateTime e = end.atTime(LocalTime.MAX);
        List<Purchase> purchases = purchaseRepository.findByPurchaseDateBetween(s, e);
        return purchases.stream().mapToDouble(p -> p.getProduct().getPrice() * p.getQuantity()).sum();
    }

    public String getAllPurchasesFormatted() {
        List<Purchase> purchases = purchaseRepository.findAll();
        var formattedList = PurchaseHelper.formatPurchases(purchases);
        return String.join("\n", formattedList);
    }
}
