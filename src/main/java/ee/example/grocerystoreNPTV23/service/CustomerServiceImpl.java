package ee.example.grocerystoreNPTV23.services;

import ee.example.grocerystoreNPTV23.entity.Customer;
import ee.example.grocerystoreNPTV23.helpers.CustomerHelper;
import ee.example.grocerystoreNPTV23.interfaces.CustomerService;
import ee.example.grocerystoreNPTV23.interfaces.Input;
import ee.example.grocerystoreNPTV23.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final Input input;

    public CustomerServiceImpl(CustomerRepository customerRepository, Input input) {
        this.customerRepository = customerRepository;
        this.input = input;
    }

    @Override
    public boolean add() {
        try {
            System.out.print("Введите имя покупателя: ");
            String firstName = input.getString();
            System.out.print("Введите фамилию покупателя: ");
            String lastName = input.getString();
            System.out.print("Введите баланс покупателя: ");
            double balance = input.getDouble();
            Customer customer = CustomerHelper.createCustomer(firstName, lastName, balance);
            customerRepository.save(customer);
            System.out.println("Покупатель успешно добавлен!");
            return true;
        } catch (Exception e) {
            System.out.println("Ошибка при добавлении покупателя: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean edit() {
        try {
            System.out.print("Введите ID покупателя для редактирования: ");
            Long customerId = input.getLong();
            Customer customer = customerRepository.findById(customerId)
                    .orElseThrow(() -> new IllegalArgumentException("Покупатель с таким ID не найден."));
            System.out.print("Введите новое имя (или оставьте пустым): ");
            String newFirstName = input.getString();
            if (!newFirstName.isEmpty()) {
                customer.setFirstName(newFirstName);
            }
            System.out.print("Введите новую фамилию (или оставьте пустым): ");
            String newLastName = input.getString();
            if (!newLastName.isEmpty()) {
                customer.setLastName(newLastName);
            }
            System.out.print("Введите новый баланс (или -1, чтобы пропустить): ");
            double newBalance = input.getDouble();
            if (newBalance >= 0) {
                customer.setBalance(newBalance);
            }
            customerRepository.save(customer);
            System.out.println("Покупатель успешно обновлён!");
            return true;
        } catch (Exception e) {
            System.out.println("Ошибка при редактировании покупателя: " + e.getMessage());
            return false;
        }
    }

    @Override
    public void print() {
        List<Customer> customers = customerRepository.findAll();
        if (customers.isEmpty()) {
            System.out.println("Нет покупателей в базе данных.");
            return;
        }
        var formatted = CustomerHelper.formatCustomers(customers);
        System.out.println("Список всех покупателей:");
        formatted.forEach(System.out::println);
    }

    @Override
    public Customer findCustomerById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Покупатель с таким ID не найден."));
    }
}
