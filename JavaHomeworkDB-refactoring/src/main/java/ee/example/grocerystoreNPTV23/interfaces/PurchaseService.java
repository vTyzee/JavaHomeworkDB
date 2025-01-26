package ee.example.grocerystoreNPTV23.interfaces;

import java.time.LocalDate;

public interface PurchaseService {
    void purchaseProduct(Long customerId, Long productId, int quantity);
    String getAllPurchasesFormatted();
    double getIncome(LocalDate start, LocalDate end);
}
