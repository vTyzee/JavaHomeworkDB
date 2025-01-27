package ee.example.grocerystoreNPTV23.helpers;

import ee.example.grocerystoreNPTV23.entity.Customer;
import java.util.List;
import java.util.stream.Collectors;

public class CustomerHelper {
    public static List<String> formatCustomers(List<Customer> customers) {
        return customers.stream()
                .map(c -> String.format("ID: %d, Имя: %s %s, Баланс: %.2f",
                        c.getId(), c.getFirstName(), c.getLastName(), c.getBalance()))
                .collect(Collectors.toList());
    }

    public static Customer createCustomer(String firstName, String lastName, double balance) {
        if (firstName == null || firstName.trim().isEmpty() || lastName == null || lastName.trim().isEmpty()) {
            throw new IllegalArgumentException("Имя и фамилия клиента должны быть заполнены.");
        }
        if (balance < 0) {
            throw new IllegalArgumentException("Баланс не может быть отрицательным.");
        }
        return new Customer(firstName, lastName, balance);
    }
}
