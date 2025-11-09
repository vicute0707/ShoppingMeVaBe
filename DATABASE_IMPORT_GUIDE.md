# 📊 HƯỚNG DẪN IMPORT DỮ LIỆU MẪU

## Cách 1: Import từ Command Line (Khuyến nghị)

### Bước 1: Đảm bảo database đã tồn tại
```bash
# Đăng nhập MariaDB
mysql -u root -p

# Nhập password: sapassword

# Tạo database (nếu chưa có)
CREATE DATABASE IF NOT EXISTS shop_me_va_be
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

# Kiểm tra
SHOW DATABASES;

# Thoát
exit;
```

### Bước 2: Import file data.sql
```bash
# Cách 1: Import với password trong command
mysql -u root -psapassword shop_me_va_be < src/main/resources/db/data.sql

# Cách 2: Nhập password khi được hỏi (bảo mật hơn)
mysql -u root -p shop_me_va_be < src/main/resources/db/data.sql
# Nhập password: sapassword

# Từ thư mục gốc project
cd D:\ShoppingMomadnBaby
mysql -u root -psapassword shop_me_va_be < src/main/resources/db/data.sql
```

### Bước 3: Kiểm tra dữ liệu đã import
```bash
# Đăng nhập lại MariaDB
mysql -u root -psapassword shop_me_va_be

# Kiểm tra các bảng
SHOW TABLES;

# Kiểm tra số lượng records
SELECT COUNT(*) FROM users;        -- Nên có 5 users
SELECT COUNT(*) FROM categories;   -- Nên có 8 categories
SELECT COUNT(*) FROM products;     -- Nên có 40 products
SELECT COUNT(*) FROM orders;       -- Nên có 4 orders

# Xem users
SELECT id, full_name, email, role, enabled FROM users;

# Thoát
exit;
```

## Cách 2: Import từ MySQL Workbench / HeidiSQL

### MySQL Workbench
1. Mở MySQL Workbench
2. Connect đến MariaDB (localhost:3306, user: root, password: sapassword)
3. Chọn database: `shop_me_va_be`
4. Menu: **Server** → **Data Import**
5. Chọn: **Import from Self-Contained File**
6. Browse đến: `D:\ShoppingMomadnBaby\src\main\resources\db\data.sql`
7. Click **Start Import**

### HeidiSQL
1. Mở HeidiSQL
2. Connect đến MariaDB
3. Chọn database `shop_me_va_be`
4. Menu: **File** → **Load SQL file**
5. Chọn file: `data.sql`
6. Click **Execute** (F9)

## Cách 3: Import từ MariaDB Console (Source command)

```bash
# Đăng nhập MariaDB
mysql -u root -psapassword shop_me_va_be

# Import bằng source command
source D:/ShoppingMomadnBaby/src/main/resources/db/data.sql;

# Hoặc dùng \. (shorthand)
\. D:/ShoppingMomadnBaby/src/main/resources/db/data.sql

# Kiểm tra
SELECT COUNT(*) FROM users;

# Thoát
exit;
```

## ✅ Dữ Liệu Mẫu Sau Khi Import

### 👥 Users (5 người dùng)
| ID | Tên | Email | Role | Password |
|----|-----|-------|------|----------|
| 1 | Admin Shop Mẹ và Bé | admin@shopmevabe.com | ADMIN | admin123 |
| 2 | Nguyễn Thị Mai | mai.nguyen@gmail.com | CUSTOMER | admin123 |
| 3 | Trần Văn Hùng | hung.tran@gmail.com | CUSTOMER | admin123 |
| 4 | Lê Thị Lan | lan.le@gmail.com | CUSTOMER | admin123 |
| 5 | Phạm Minh Tuấn | tuan.pham@gmail.com | CUSTOMER | admin123 |

### 📁 Categories (8 danh mục)
1. Sữa bột cho bé
2. Tã bỉm
3. Đồ chơi trẻ em
4. Quần áo trẻ em
5. Xe đẩy - Nôi - Ghế ngồi
6. Đồ dùng cho mẹ
7. Thực phẩm dinh dưỡng
8. Đồ dùng tắm gội

### 🛍️ Products (40 sản phẩm)
- **Sữa bột:** Enfamil, Aptamil, Similac, Vinamilk, NAN (5 sản phẩm)
- **Tã bỉm:** Bobby, Pampers, Merries, Moony, Huggies (5 sản phẩm)
- **Đồ chơi:** Xúc xắc, Âm nhạc, Lego, Xe máy điện (5 sản phẩm)
- **Quần áo:** Body suit, Áo liền quần, Thu đông, Váy (5 sản phẩm)
- **Xe đẩy/Nôi:** Seebaby, Mamakids, Mastela, Aprica (5 sản phẩm)
- **Đồ cho mẹ:** Máy hút sữa, Túi trữ, Áo lót, Gối bầu (5 sản phẩm)
- **Thực phẩm:** Bột ăn dặm, Cháo, Bánh, Sữa chua (5 sản phẩm)
- **Tắm gội:** Kodomo, Johnson, Lactacyd, Khăn tắm (5 sản phẩm)

### 📦 Orders (4 đơn hàng mẫu)
- Order #1: Nguyễn Thị Mai - Đã giao (DELIVERED) - MoMo paid
- Order #2: Trần Văn Hùng - Đang giao (SHIPPED) - COD
- Order #3: Lê Thị Lan - Đang xử lý (PROCESSING) - MoMo paid
- Order #4: Phạm Minh Tuấn - Chờ xử lý (PENDING) - COD

## ⚠️ Lưu Ý Quan Trọng

### Nếu Gặp Lỗi "Duplicate Entry"
```sql
-- Xóa dữ liệu cũ trước khi import lại
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE order_details;
TRUNCATE TABLE orders;
TRUNCATE TABLE products;
TRUNCATE TABLE categories;
TRUNCATE TABLE users;
SET FOREIGN_KEY_CHECKS = 1;

-- Sau đó import lại data.sql
```

### Nếu Ứng Dụng Tự Tạo Tables
- File `data.sql` đã có lệnh `SET FOREIGN_KEY_CHECKS = 0;`
- File sẽ TRUNCATE các bảng trước khi insert
- **An toàn:** Không ảnh hưởng đến structure, chỉ clear data

### Password Đã Mã Hóa
Tất cả password trong database đều đã được mã hóa bằng **BCrypt**:
- Password thật: `admin123`
- Trong DB: `$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy`

## 🚀 Sau Khi Import

1. **Khởi động ứng dụng:**
```bash
cd D:\ShoppingMomadnBaby
mvnw spring-boot:run
```

2. **Truy cập ứng dụng:**
- Homepage: http://localhost:8081/
- Admin: http://localhost:8081/admin

3. **Đăng nhập:**
- **Admin:** admin@shopmevabe.com / admin123
- **Customer:** mai.nguyen@gmail.com / admin123

4. **Kiểm tra:**
- Xem danh sách sản phẩm
- Thêm vào giỏ hàng
- Tạo đơn hàng
- Test thanh toán MoMo

## 📝 Troubleshooting

### Lỗi: "Access denied for user 'root'@'localhost'"
```bash
# Kiểm tra password
mysql -u root -p
# Nhập password: sapassword

# Nếu sai password, reset trong application.properties
spring.datasource.password=your_actual_password
```

### Lỗi: "Unknown database 'shop_me_va_be'"
```bash
# Tạo database trước
mysql -u root -psapassword -e "CREATE DATABASE shop_me_va_be CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"
```

### Lỗi: "Table 'shop_me_va_be.users' doesn't exist"
```bash
# Để Spring Boot tự tạo tables
# Chạy ứng dụng lần đầu (ddl-auto=update sẽ tạo tables)
mvnw spring-boot:run

# Sau khi tables đã được tạo, import data
mysql -u root -psapassword shop_me_va_be < src/main/resources/db/data.sql
```

---

**Lưu ý:** File `data.sql` được thiết kế để chạy nhiều lần mà không gây lỗi duplicate, vì nó có lệnh TRUNCATE ở đầu file.
