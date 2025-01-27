package ee.example.grocerystoreNPTV23;

import ee.example.grocerystoreNPTV23.interfaces.CustomerService;
import ee.example.grocerystoreNPTV23.interfaces.ProductService;
import ee.example.grocerystoreNPTV23.interfaces.PurchaseService;
import ee.example.grocerystoreNPTV23.interfaces.Input;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GroceryStoreNPTV23Application implements CommandLineRunner {

    private final Input input;
    private final CustomerService customerService;
    private final ProductService productService;
    private final PurchaseService purchaseService;

    public GroceryStoreNPTV23Application(
            Input input,
            CustomerService customerService,
            ProductService productService,
            PurchaseService purchaseService
    ) {
        this.input = input;
        this.customerService = customerService;
        this.productService = productService;
        this.purchaseService = purchaseService;
    }

    public static void main(String[] args) {
        SpringApplication.run(GroceryStoreNPTV23Application.class, args);
    }

    @Override
    public void run(String... args) {
        System.out.println("------ Продуктовый магазин ------");
        boolean repeat = true;
        while (repeat) {
            try {
                System.out.println("Список задач:");
                System.out.println("0. Выйти из программы");
                System.out.println("1. Добавить продукт");
                System.out.println("2. Список продуктов");
                System.out.println("3. Редактировать атрибуты продукта");
                System.out.println("4. Добавить покупателя");
                System.out.println("5. Список покупателей");
                System.out.println("6. Редактировать атрибуты покупателя");
                System.out.println("7. Купить продукт");
                System.out.println("8. Доход магазина за период");
                System.out.print("Введите номер задачи: ");
                int task = input.getInt();
                switch (task) {
                    case 0 -> repeat = false;
                    case 1 -> productService.add();
                    case 2 -> productService.print();
                    case 3 -> productService.edit();
                    case 4 -> customerService.add();
                    case 5 -> customerService.print();
                    case 6 -> customerService.edit();
                    case 7 -> purchaseService.purchase();
                    case 8 -> purchaseService.showIncome();
                    default -> System.out.println("Выберите задачу из списка!");
                }
                System.out.println("----------------------------------------");
            } catch (Exception e) {
                System.out.println("Ошибка: " + e.getMessage());
                e.printStackTrace();
            }
        }
        System.out.println("До свидания :)");
    }
}
