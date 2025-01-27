package ee.example.grocerystoreNPTV23.interfaces;

import ee.example.grocerystoreNPTV23.entity.Customer;

public interface CustomerService {
    boolean add();
    boolean edit();
    void print();
    Customer findCustomerById(Long id);
}
