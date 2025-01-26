package ee.example.grocerystoreNPTV23.interfaces;

import ee.example.grocerystoreNPTV23.entity.Customer;
import java.util.List;

public interface CustomerService {
    void addCustomer(String firstName, String lastName, double balance);
    void editCustomer(Long id, String firstName, String lastName, Double balance);
    String getAllCustomersFormatted();
    List<Customer> getAllCustomers();
    Customer getCustomerById(Long id);
    void decreaseCustomerBalance(Long id, double amount);
}
