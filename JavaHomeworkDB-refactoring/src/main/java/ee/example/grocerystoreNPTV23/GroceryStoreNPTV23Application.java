package ee.example.grocerystoreNPTV23;

import ee.example.grocerystoreNPTV23.interfaces.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDate;

@SpringBootApplication
public class GroceryStoreNPTV23Application implements CommandLineRunner {

	@Autowired
	private Input input;
	@Autowired
	private CustomerService customerService;
	@Autowired
	private ProductService productService;
	@Autowired
	private PurchaseService purchaseService;

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
					case 1 -> addProduct();
					case 2 -> showProducts();
					case 3 -> editProduct();
					case 4 -> addCustomer();
					case 5 -> showCustomers();
					case 6 -> editCustomer();
					case 7 -> purchaseProduct();
					case 8 -> showIncome();
					default -> System.out.println("Выберите задачу из списка!");
				}
				System.out.println("----------------------------------------");
			} catch (Exception e) {
				System.out.println("Ошибка: " + e.getMessage());
				e.printStackTrace(); // Для отладки
			}
		}
		System.out.println("До свидания :)");
	}

	private void addProduct() {
		try {
			System.out.print("Введите название продукта: ");
			String name = input.getString();
			System.out.print("Введите цену продукта: ");
			double price = input.getDouble();
			System.out.print("Введите количество продукта: ");
			int quantity = input.getInt();
			productService.addProduct(name, price, quantity);
			System.out.println("Продукт успешно добавлен!");
		} catch (IllegalArgumentException e) {
			System.out.println("Ошибка: " + e.getMessage());
		}
	}

	private void showProducts() {
		try {
			System.out.println("Список всех продуктов:");
			System.out.println(productService.getAllProductsFormatted());
		} catch (Exception e) {
			System.out.println("Ошибка: " + e.getMessage());
		}
	}

	private void editProduct() {
		try {
			System.out.print("Введите ID продукта для редактирования: ");
			Long productId = input.getLong();
			System.out.print("Введите новое название (или оставьте пустым): ");
			String name = input.getString();
			System.out.print("Введите новую цену (или введите 0 для пропуска): ");
			double price = input.getDouble();
			System.out.print("Введите новое количество (или введите -1 для пропуска): ");
			int quantity = input.getInt();

			productService.editProduct(
					productId,
					name.isEmpty() ? null : name,
					price,
					quantity
			);
			System.out.println("Продукт успешно обновлён!");
		} catch (IllegalArgumentException e) {
			System.out.println("Ошибка: " + e.getMessage());
		}
	}

	private void addCustomer() {
		try {
			System.out.print("Введите имя покупателя: ");
			String firstName = input.getString();
			System.out.print("Введите фамилию покупателя: ");
			String lastName = input.getString();
			System.out.print("Введите баланс покупателя: ");
			double balance = input.getDouble();
			customerService.addCustomer(firstName, lastName, balance);
			System.out.println("Покупатель успешно добавлен!");
		} catch (IllegalArgumentException e) {
			System.out.println("Ошибка: " + e.getMessage());
		}
	}

	private void showCustomers() {
		try {
			System.out.println("Список всех покупателей:");
			System.out.println(customerService.getAllCustomersFormatted());
		} catch (Exception e) {
			System.out.println("Ошибка: " + e.getMessage());
		}
	}

	private void editCustomer() {
		try {
			System.out.print("Введите ID покупателя для редактирования: ");
			Long customerId = input.getLong();
			System.out.print("Введите новое имя (или оставьте пустым): ");
			String firstName = input.getString();
			System.out.print("Введите новую фамилию (или оставьте пустым): ");
			String lastName = input.getString();
			System.out.print("Введите новый баланс (или введите -1 для пропуска): ");
			double balanceInput = input.getDouble();
			Double balance = balanceInput >= 0 ? balanceInput : null;

			customerService.editCustomer(
					customerId,
					firstName.isEmpty() ? null : firstName,
					lastName.isEmpty() ? null : lastName,
					balance
			);
			System.out.println("Покупатель успешно обновлён!");
		} catch (IllegalArgumentException e) {
			System.out.println("Ошибка: " + e.getMessage());
		}
	}

	private void purchaseProduct() {
		try {
			System.out.print("Введите ID покупателя: ");
			Long customerId = input.getLong();
			System.out.print("Введите ID продукта: ");
			Long productId = input.getLong();
			System.out.print("Введите количество для покупки: ");
			int quantity = input.getInt();
			purchaseService.purchaseProduct(customerId, productId, quantity);
			System.out.println("Покупка успешно завершена!");
		} catch (IllegalArgumentException e) {
			System.out.println("Ошибка: " + e.getMessage());
		}
	}

	private void showIncome() {
		try {
			System.out.print("Введите год (например, 2025): ");
			int year = input.getInt();
			System.out.print("Введите месяц (1-12): ");
			int month = input.getInt();
			System.out.print("Введите день (1-31): ");
			int day = input.getInt();
			LocalDate date = LocalDate.of(year, month, day);

			double dailyIncome = purchaseService.getIncome(date, date);
			System.out.printf("Доход за %s: %.2f%n", date, dailyIncome);

			// Месяц
			System.out.print("Введите месяц для расчёта дохода (1-12): ");
			month = input.getInt();
			LocalDate startOfMonth = LocalDate.of(year, month, 1);
			LocalDate endOfMonth = startOfMonth.withDayOfMonth(startOfMonth.lengthOfMonth());
			double monthlyIncome = purchaseService.getIncome(startOfMonth, endOfMonth);
			System.out.printf("Доход за %d/%d: %.2f%n", month, year, monthlyIncome);

			// Год
			System.out.print("Введите год для расчёта дохода (например, 2025): ");
			year = input.getInt();
			LocalDate startOfYear = LocalDate.of(year, 1, 1);
			LocalDate endOfYear = LocalDate.of(year, 12, 31);
			double yearlyIncome = purchaseService.getIncome(startOfYear, endOfYear);
			System.out.printf("Доход за %d: %.2f%n", year, yearlyIncome);

		} catch (Exception e) {
			System.out.println("Ошибка: " + e.getMessage());
		}
	}
}
