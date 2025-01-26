package ee.example.grocerystoreNPTV23.helpers;

import ee.example.grocerystoreNPTV23.entity.Purchase;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class PurchaseHelper {

    public static List<String> formatPurchases(List<Purchase> purchases) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return purchases.stream()
                .map(p -> String.format(
                        "ID: %d, Клиент: %s %s, Продукт: %s, Количество: %d, Дата: %s",
                        p.getId(),
                        p.getCustomer().getFirstName(),
                        p.getCustomer().getLastName(),
                        p.getProduct().getName(),
                        p.getQuantity(),
                        p.getPurchaseDate().format(formatter)
                ))
                .collect(Collectors.toList());
    }
}
