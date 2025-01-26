package ee.example.grocerystoreNPTV23.services;

import ee.example.grocerystoreNPTV23.entity.Customer;
import ee.example.grocerystoreNPTV23.helpers.CustomerHelper;
import ee.example.grocerystoreNPTV23.interfaces.CustomerService;
import ee.example.grocerystoreNPTV23.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public void addCustomer(String firstName, String lastName, double balance) {
        var customer = CustomerHelper.createCustomer(firstName, lastName, balance);
        customerRepository.save(customer);
    }

    @Override
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

    @Override
    public String getAllCustomersFormatted() {
        List<Customer> customers = customerRepository.findAll();
        var formattedList = CustomerHelper.formatCustomers(customers);
        return String.join("\n", formattedList);
    }

    @Override
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Покупатель с таким ID не найден."));
    }

    @Override
    public void decreaseCustomerBalance(Long id, double amount) {
        Customer customer = getCustomerById(id);
        if (customer.getBalance() < amount) {
            throw new IllegalArgumentException("Недостаточно средств на счету покупателя.");
        }
        customer.setBalance(customer.getBalance() - amount);
        customerRepository.save(customer);
    }
}
