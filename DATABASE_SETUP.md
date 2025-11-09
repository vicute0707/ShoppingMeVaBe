# 🗄️ Hướng Dẫn Cài Đặt Database - Shop Mẹ và Bé

## 📋 Yêu Cầu Hệ Thống

- MariaDB Server 10.5 hoặc cao hơn
- MySQL Workbench hoặc DBeaver (hoặc công cụ quản lý database tương tự)
- Java 17 hoặc cao hơn
- Maven 3.6+

---

## 🚀 Cách 1: Import SQL Files (Khuyến Nghị)

### Bước 1: Cài Đặt MariaDB

#### Trên Windows:
```bash
# Download và cài đặt từ: https://mariadb.org/download/
# Hoặc dùng Chocolatey:
choco install mariadb
```

#### Trên macOS:
```bash
brew install mariadb
brew services start mariadb
```

#### Trên Linux (Ubuntu/Debian):
```bash
sudo apt update
sudo apt install mariadb-server
sudo systemctl start mariadb
sudo mysql_secure_installation
```

### Bước 2: Đăng Nhập MariaDB

```bash
# Đăng nhập với user root
mysql -u root -p
```

### Bước 3: Tạo Database và User

```sql
-- Tạo database
CREATE DATABASE IF NOT EXISTS shop_me_va_be
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

-- Tạo user và cấp quyền (Optional - nếu không dùng root)
CREATE USER IF NOT EXISTS 'shopmevabe_user'@'localhost' IDENTIFIED BY 'YourStrongPassword123!';
GRANT ALL PRIVILEGES ON shop_me_va_be.* TO 'shopmevabe_user'@'localhost';
FLUSH PRIVILEGES;

-- Chọn database
USE shop_me_va_be;
```

### Bước 4: Import Schema và Data

```bash
# Import schema (tạo cấu trúc bảng)
mysql -u root -p shop_me_va_be < src/main/resources/db/schema.sql

# Import data (dữ liệu mẫu)
mysql -u root -p shop_me_va_be < src/main/resources/db/data.sql
```

**Hoặc trong MySQL/MariaDB console:**

```sql
USE shop_me_va_be;

-- Import schema
SOURCE /path/to/your/project/src/main/resources/db/schema.sql;

-- Import data
SOURCE /path/to/your/project/src/main/resources/db/data.sql;
```

### Bước 5: Kiểm Tra Data

```sql
-- Kiểm tra các bảng đã tạo
SHOW TABLES;

-- Kiểm tra dữ liệu
SELECT COUNT(*) as total_users FROM users;
SELECT COUNT(*) as total_categories FROM categories;
SELECT COUNT(*) as total_products FROM products;
SELECT COUNT(*) as total_orders FROM orders;

-- Xem danh sách sản phẩm
SELECT p.id, p.name, p.price, c.name as category
FROM products p
JOIN categories c ON p.category_id = c.id
LIMIT 10;
```

---

## ⚙️ Cách 2: Để JPA Tự Động Tạo Database (Development)

Nếu bạn muốn để Spring Boot JPA tự động tạo schema (không khuyến nghị cho production):

### Bước 1: Cấu Hình application.properties

```properties
# Chỉ tạo database, không import dữ liệu mẫu
spring.jpa.hibernate.ddl-auto=create
# Hoặc update để giữ dữ liệu cũ
# spring.jpa.hibernate.ddl-auto=update
```

### Bước 2: Chạy Application

```bash
mvn spring-boot:run
```

**Lưu ý:** Cách này sẽ KHÔNG có dữ liệu mẫu. Bạn cần tự tạo admin account và sản phẩm.

---

## 🔧 Cấu Hình Application

### File: `src/main/resources/application.properties`

```properties
# Database Configuration - MariaDB
spring.datasource.url=jdbc:mariadb://localhost:3306/shop_me_va_be?createDatabaseIfNotExist=true&useUnicode=true&characterEncoding=UTF-8
spring.datasource.driver-class-name=org.mariadb.jdbc.Driver
spring.datasource.username=root
spring.datasource.password=root  # ⚠️ ĐỔI PASSWORD NÀY!

# JPA Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MariaDBDialect
```

**⚠️ LƯU Ý BẢO MẬT:**
- Đổi `spring.datasource.password` thành password thực của bạn
- KHÔNG commit password thật lên Git
- Dùng biến môi trường cho production:
  ```properties
  spring.datasource.username=${DB_USERNAME:root}
  spring.datasource.password=${DB_PASSWORD:root}
  ```

---

## 📊 Cấu Trúc Database

### Các Bảng Chính:

1. **users** - Quản lý người dùng (Admin, Customer)
2. **categories** - Danh mục sản phẩm (8 danh mục)
3. **products** - Sản phẩm (40 sản phẩm mẫu)
4. **orders** - Đơn hàng
5. **order_details** - Chi tiết đơn hàng

### Sơ Đồ Quan Hệ:

```
users (1) -----> (N) orders (1) -----> (N) order_details (N) <----- (1) products
                                                                              |
                                                                              |
                                                                         categories
```

---

## 👤 Tài Khoản Mẫu

### Admin Account:
- **Email:** `admin@shopmevabe.com`
- **Password:** `admin123`
- **Role:** ADMIN

### Customer Accounts:
| Email | Password | Tên |
|-------|----------|-----|
| mai.nguyen@gmail.com | admin123 | Nguyễn Thị Mai |
| hung.tran@gmail.com | admin123 | Trần Văn Hùng |
| lan.le@gmail.com | admin123 | Lê Thị Lan |
| tuan.pham@gmail.com | admin123 | Phạm Minh Tuấn |

---

## 🛍️ Dữ Liệu Mẫu

### 8 Danh Mục Sản Phẩm:
1. Sữa bột cho bé (5 sản phẩm)
2. Tã bỉm (5 sản phẩm)
3. Đồ chơi trẻ em (5 sản phẩm)
4. Quần áo trẻ em (5 sản phẩm)
5. Xe đẩy - Nôi - Ghế ngồi (5 sản phẩm)
6. Đồ dùng cho mẹ (5 sản phẩm)
7. Thực phẩm dinh dưỡng (5 sản phẩm)
8. Đồ dùng tắm gội (5 sản phẩm)

### 4 Đơn Hàng Mẫu với các trạng thái khác nhau:
- PENDING - Chờ xử lý
- PROCESSING - Đang xử lý
- SHIPPED - Đã giao vận chuyển
- DELIVERED - Đã giao hàng

---

## 🔍 Các Lệnh SQL Hữu Ích

### Xem thống kê tổng quan:
```sql
-- Tổng số sản phẩm theo danh mục
SELECT c.name as category, COUNT(p.id) as total_products, SUM(p.stock_quantity) as total_stock
FROM categories c
LEFT JOIN products p ON c.id = p.category_id
GROUP BY c.id, c.name;

-- Doanh thu theo khách hàng
SELECT u.full_name, u.email, COUNT(o.id) as total_orders, SUM(o.total_amount) as total_spent
FROM users u
LEFT JOIN orders o ON u.id = o.user_id
WHERE u.role = 'CUSTOMER'
GROUP BY u.id, u.full_name, u.email
ORDER BY total_spent DESC;

-- Sản phẩm bán chạy nhất
SELECT p.name, SUM(od.quantity) as total_sold, SUM(od.subtotal) as revenue
FROM products p
JOIN order_details od ON p.id = od.product_id
GROUP BY p.id, p.name
ORDER BY total_sold DESC
LIMIT 10;
```

### Reset dữ liệu:
```sql
-- XÓA TẤT CẢ DỮ LIỆU (⚠️ CẨN THẬN!)
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE order_details;
TRUNCATE TABLE orders;
TRUNCATE TABLE products;
TRUNCATE TABLE categories;
TRUNCATE TABLE users;
SET FOREIGN_KEY_CHECKS = 1;

-- Sau đó import lại data.sql
SOURCE /path/to/data.sql;
```

---

## 🐛 Xử Lý Lỗi Thường Gặp

### Lỗi: "Access denied for user 'root'@'localhost'"
```bash
# Reset password MariaDB
sudo mysql
ALTER USER 'root'@'localhost' IDENTIFIED BY 'new_password';
FLUSH PRIVILEGES;
```

### Lỗi: "Unknown database 'shop_me_va_be'"
```sql
-- Tạo lại database
CREATE DATABASE shop_me_va_be CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### Lỗi: "Communications link failure"
```bash
# Kiểm tra MariaDB có đang chạy không
sudo systemctl status mariadb  # Linux
brew services list  # macOS
```

### Lỗi: "Table already exists"
```sql
-- Drop database và tạo lại
DROP DATABASE IF EXISTS shop_me_va_be;
CREATE DATABASE shop_me_va_be CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE shop_me_va_be;
SOURCE schema.sql;
SOURCE data.sql;
```

---

## 📚 Tài Liệu Tham Khảo

- [MariaDB Documentation](https://mariadb.com/kb/en/documentation/)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [Hibernate Documentation](https://hibernate.org/orm/documentation/)

---

## 🎉 Hoàn Thành!

Sau khi hoàn tất các bước trên, bạn có thể:

1. Chạy ứng dụng:
   ```bash
   mvn spring-boot:run
   ```

2. Truy cập:
   - **Web:** http://localhost:8080
   - **Admin Panel:** http://localhost:8080/admin
   - **API Docs:** http://localhost:8080/swagger-ui.html

3. Đăng nhập với tài khoản admin hoặc customer đã tạo ở trên

**Happy Coding! 🚀👶🍼**
