package iuh.student.www.config;

import iuh.student.www.entity.*;
import iuh.student.www.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DataInitializer DISABLED
 * Sử dụng dữ liệu từ file data.sql thay vì tạo dữ liệu mẫu trong code
 *
 * Để import dữ liệu:
 * mysql -u root -p shop_me_va_be < src/main/resources/db/data.sql
 *
 * Tài khoản mẫu từ data.sql:
 * - Admin: admin@shopmevabe.com / admin123
 * - Customer: mai.nguyen@gmail.com / admin123
 */
// @Component  // DISABLED - Using data.sql instead
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            log.info("Initializing sample data...");

            // Create Admin User
            User admin = User.builder()
                    .fullName("Admin User")
                    .email("admin@shopping.com")
                    .password(passwordEncoder.encode("admin123"))
                    .phone("0123456789")
                    .address("123 Admin Street")
                    .role(User.Role.ADMIN)
                    .enabled(true)
                    .build();
            userRepository.save(admin);
            log.info("Created admin user: admin@shopping.com / admin123");

            // Create Customer Users
            User customer1 = User.builder()
                    .fullName("John Doe")
                    .email("john@example.com")
                    .password(passwordEncoder.encode("password123"))
                    .phone("0987654321")
                    .address("456 Customer Avenue")
                    .role(User.Role.CUSTOMER)
                    .enabled(true)
                    .build();
            userRepository.save(customer1);

            User customer2 = User.builder()
                    .fullName("Jane Smith")
                    .email("jane@example.com")
                    .password(passwordEncoder.encode("password123"))
                    .phone("0912345678")
                    .address("789 Customer Boulevard")
                    .role(User.Role.CUSTOMER)
                    .enabled(true)
                    .build();
            userRepository.save(customer2);
            log.info("Created customer users: john@example.com / password123, jane@example.com / password123");

            // Create Categories
            Category electronics = Category.builder()
                    .name("Electronics")
                    .description("Electronic devices and accessories")
                    .active(true)
                    .build();
            categoryRepository.save(electronics);

            Category clothing = Category.builder()
                    .name("Clothing")
                    .description("Fashion and apparel")
                    .active(true)
                    .build();
            categoryRepository.save(clothing);

            Category books = Category.builder()
                    .name("Books")
                    .description("Books and literature")
                    .active(true)
                    .build();
            categoryRepository.save(books);

            Category homeGarden = Category.builder()
                    .name("Home & Garden")
                    .description("Home improvement and gardening supplies")
                    .active(true)
                    .build();
            categoryRepository.save(homeGarden);

            log.info("Created categories");

            // Create Products - Electronics
            Product laptop = Product.builder()
                    .name("Dell Laptop XPS 13")
                    .description("High-performance laptop with 13-inch display, Intel Core i7, 16GB RAM, 512GB SSD")
                    .price(1299.99)
                    .stockQuantity(50)
                    .imageUrl("https://via.placeholder.com/400x300/0066cc/ffffff?text=Dell+XPS+13")
                    .category(electronics)
                    .active(true)
                    .build();
            productRepository.save(laptop);

            Product smartphone = Product.builder()
                    .name("Samsung Galaxy S23")
                    .description("Latest Samsung flagship smartphone with 6.1-inch display, 5G connectivity")
                    .price(899.99)
                    .stockQuantity(100)
                    .imageUrl("https://via.placeholder.com/400x300/000000/ffffff?text=Galaxy+S23")
                    .category(electronics)
                    .active(true)
                    .build();
            productRepository.save(smartphone);

            Product headphones = Product.builder()
                    .name("Sony WH-1000XM5 Headphones")
                    .description("Premium noise-cancelling wireless headphones")
                    .price(399.99)
                    .stockQuantity(75)
                    .imageUrl("https://via.placeholder.com/400x300/333333/ffffff?text=Sony+Headphones")
                    .category(electronics)
                    .active(true)
                    .build();
            productRepository.save(headphones);

            Product smartwatch = Product.builder()
                    .name("Apple Watch Series 9")
                    .description("Advanced smartwatch with health tracking and fitness features")
                    .price(429.99)
                    .stockQuantity(60)
                    .imageUrl("https://via.placeholder.com/400x300/999999/000000?text=Apple+Watch")
                    .category(electronics)
                    .active(true)
                    .build();
            productRepository.save(smartwatch);

            // Create Products - Clothing
            Product tshirt = Product.builder()
                    .name("Classic Cotton T-Shirt")
                    .description("Comfortable 100% cotton t-shirt, available in multiple colors")
                    .price(19.99)
                    .stockQuantity(200)
                    .imageUrl("https://via.placeholder.com/400x300/ff6b6b/ffffff?text=T-Shirt")
                    .category(clothing)
                    .active(true)
                    .build();
            productRepository.save(tshirt);

            Product jeans = Product.builder()
                    .name("Levi's 501 Jeans")
                    .description("Original fit jeans, premium denim quality")
                    .price(79.99)
                    .stockQuantity(150)
                    .imageUrl("https://via.placeholder.com/400x300/4ecdc4/000000?text=Levis+Jeans")
                    .category(clothing)
                    .active(true)
                    .build();
            productRepository.save(jeans);

            Product jacket = Product.builder()
                    .name("North Face Winter Jacket")
                    .description("Waterproof and insulated winter jacket")
                    .price(199.99)
                    .stockQuantity(80)
                    .imageUrl("https://via.placeholder.com/400x300/45b7d1/ffffff?text=Winter+Jacket")
                    .category(clothing)
                    .active(true)
                    .build();
            productRepository.save(jacket);

            // Create Products - Books
            Product book1 = Product.builder()
                    .name("Clean Code by Robert Martin")
                    .description("A handbook of agile software craftsmanship")
                    .price(39.99)
                    .stockQuantity(120)
                    .imageUrl("https://via.placeholder.com/400x300/96ceb4/000000?text=Clean+Code")
                    .category(books)
                    .active(true)
                    .build();
            productRepository.save(book1);

            Product book2 = Product.builder()
                    .name("The Pragmatic Programmer")
                    .description("Your journey to mastery in software development")
                    .price(44.99)
                    .stockQuantity(90)
                    .imageUrl("https://via.placeholder.com/400x300/ffeaa7/000000?text=Pragmatic+Programmer")
                    .category(books)
                    .active(true)
                    .build();
            productRepository.save(book2);

            // Create Products - Home & Garden
            Product plant = Product.builder()
                    .name("Indoor Plant Collection")
                    .description("Set of 3 easy-care indoor plants")
                    .price(49.99)
                    .stockQuantity(65)
                    .imageUrl("https://via.placeholder.com/400x300/81ecec/000000?text=Indoor+Plants")
                    .category(homeGarden)
                    .active(true)
                    .build();
            productRepository.save(plant);

            Product lamp = Product.builder()
                    .name("Modern LED Desk Lamp")
                    .description("Adjustable LED desk lamp with touch control")
                    .price(59.99)
                    .stockQuantity(110)
                    .imageUrl("https://via.placeholder.com/400x300/74b9ff/000000?text=Desk+Lamp")
                    .category(homeGarden)
                    .active(true)
                    .build();
            productRepository.save(lamp);

            log.info("Created products");

            // Create Sample Order for customer1
            Order order1 = Order.builder()
                    .user(customer1)
                    .orderDate(LocalDateTime.now().minusDays(5))
                    .totalAmount(1699.98)
                    .status(Order.OrderStatus.DELIVERED)
                    .shippingAddress("456 Customer Avenue")
                    .phone("0987654321")
                    .notes("Please deliver before 5 PM")
                    .orderDetails(new ArrayList<>())
                    .build();

            OrderDetail detail1 = OrderDetail.builder()
                    .order(order1)
                    .product(laptop)
                    .quantity(1)
                    .unitPrice(laptop.getPrice())
                    .subtotal(laptop.getPrice())
                    .build();

            OrderDetail detail2 = OrderDetail.builder()
                    .order(order1)
                    .product(headphones)
                    .quantity(1)
                    .unitPrice(headphones.getPrice())
                    .subtotal(headphones.getPrice())
                    .build();

            order1.getOrderDetails().add(detail1);
            order1.getOrderDetails().add(detail2);
            orderRepository.save(order1);

            // Update stock
            laptop.setStockQuantity(laptop.getStockQuantity() - 1);
            headphones.setStockQuantity(headphones.getStockQuantity() - 1);
            productRepository.save(laptop);
            productRepository.save(headphones);

            log.info("Created sample orders");
            log.info("Sample data initialization completed!");
            log.info("===========================================");
            log.info("Login credentials:");
            log.info("Admin: admin@shopping.com / admin123");
            log.info("Customer: john@example.com / password123");
            log.info("Customer: jane@example.com / password123");
            log.info("===========================================");
        }
    }
}
