package ee.example.grocerystoreNPTV23.service;

import ee.example.grocerystoreNPTV23.entity.Customer;
import ee.example.grocerystoreNPTV23.helpers.CustomerHelper;
import ee.example.grocerystoreNPTV23.interfaces.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    // Добавление нового покупателя
    public void addCustomer(String firstName, String lastName, double balance) {
        Customer customer = CustomerHelper.createCustomer(firstName, lastName, balance);
        customerRepository.save(customer);
    }

    // Редактирование покупателя
    public void editCustomer(Long id, String firstName, String lastName, Double balance) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Покупатель с таким ID не найден."));
        if (firstName != null && !firstName.trim().isEmpty()) {
            customer.setFirstName(firstName.trim());
        }
        if (lastName != null && !lastName.trim().isEmpty()) {
            customer.setLastName(lastName.trim());
        }
        if (balance != null && balance >= 0) {
            customer.setBalance(balance);
        }
        customerRepository.save(customer);
    }

    // Получить всех покупателей в читабельном формате
    public String getAllCustomersFormatted() {
        StringBuilder formattedCustomers = new StringBuilder();
        List<Customer> customers = customerRepository.findAll();
        List<String> formattedList = CustomerHelper.formatCustomers(customers);
        formattedList.forEach(s -> formattedCustomers.append(s).append("\n"));
        return formattedCustomers.toString();
    }

    // Получить всех покупателей
    public Iterable<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    // Получить покупателя по ID
    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Покупатель с таким ID не найден."));
    }

    // Уменьшить баланс покупателя
    public void decreaseCustomerBalance(Long id, double amount) {
        Customer customer = getCustomerById(id);
        if (customer.getBalance() < amount) {
            throw new IllegalArgumentException("Недостаточно средств на счету покупателя.");
        }
        customer.setBalance(customer.getBalance() - amount);
        customerRepository.save(customer);
    }
}
