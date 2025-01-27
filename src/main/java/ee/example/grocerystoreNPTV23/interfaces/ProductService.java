package ee.example.grocerystoreNPTV23.interfaces;

import ee.example.grocerystoreNPTV23.entity.Product;

public interface ProductService {
    boolean add();
    boolean edit();
    void print();
    Product findProductById(Long id);
}
