# 🚀 HƯỚNG DẪN CHẠY NHANH - Shop Mẹ và Bé

## 📋 YÊU CẦU HỆ THỐNG

- ✅ Java 17 hoặc cao hơn (đã test với Java 21)
- ✅ Maven 3.6+ (hoặc sử dụng Maven wrapper `./mvnw`)
- ✅ Kết nối Internet (để tải dependencies lần đầu)

---

## 🎯 CÁCH 1: CHẠY NHANH VỚI H2 DATABASE (KHUYÊN DÙNG)

### ⚡ Cách đơn giản nhất - 1 lệnh:

```bash
./run-dev.sh
```

### Hoặc làm thủ công:

```bash
# Bước 1: Build project
./mvnw clean package -DskipTests

# Bước 2: Chạy với profile dev (H2 database)
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

### 🌐 Truy cập ứng dụng:

- **Website:** http://localhost:8080
- **H2 Console:** http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:file:./data/ShopBabyandMomCute`
  - Username: `sa`
  - Password: *(để trống)*

### ✨ Ưu điểm của H2:

- ✅ Không cần cài đặt database
- ✅ Dữ liệu lưu vào file `./data/ShopBabyandMomCute.mv.db`
- ✅ Có giao diện web H2 Console để quản lý dữ liệu
- ✅ Hoàn hảo cho development và testing

---

## 🏭 CÁCH 2: CHẠY VỚI MARIADB/MYSQL (PRODUCTION)

### Bước 1: Cài đặt MariaDB/MySQL

**Ubuntu/Debian:**
```bash
sudo apt update
sudo apt install mariadb-server -y
sudo systemctl start mariadb
sudo systemctl enable mariadb
```

**macOS:**
```bash
brew install mariadb
brew services start mariadb
```

**Windows:**
- Tải từ: https://mariadb.org/download/
- Hoặc dùng XAMPP: https://www.apachefriends.org/

### Bước 2: Tạo Database

```bash
# Đăng nhập vào MySQL/MariaDB
mysql -u root -p

# Trong MySQL shell, chạy:
CREATE DATABASE shop_me_va_be CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
GRANT ALL PRIVILEGES ON shop_me_va_be.* TO 'root'@'localhost' IDENTIFIED BY 'root';
FLUSH PRIVILEGES;
EXIT;
```

### Bước 3: Cập nhật cấu hình

Mở file `src/main/resources/application.properties` và đảm bảo cấu hình đúng:

```properties
# Database Configuration - MariaDB
spring.datasource.url=jdbc:mariadb://localhost:3306/shop_me_va_be?createDatabaseIfNotExist=true
spring.datasource.driver-class-name=org.mariadb.jdbc.Driver
spring.datasource.username=root
spring.datasource.password=root
```

### Bước 4: Chạy ứng dụng

```bash
# Build và chạy
./mvnw clean package -DskipTests
./mvnw spring-boot:run

# Hoặc dùng script
./start-prod.sh
```

---

## 🛠️ XỬ LÝ LỖI THƯỜNG GẶP

### ❌ Lỗi: `Permission denied: ./mvnw`

```bash
chmod +x mvnw
chmod +x run-dev.sh
chmod +x start-prod.sh
```

### ❌ Lỗi: `Could not resolve dependencies`

```bash
# Xóa cache Maven và tải lại
rm -rf ~/.m2/repository
./mvnw clean install
```

### ❌ Lỗi: `Communications link failure` (Database)

**Với H2:** Không nên xảy ra. Nếu có, xóa thư mục `./data` và chạy lại.

**Với MariaDB/MySQL:**
```bash
# Kiểm tra database có chạy không
sudo systemctl status mariadb
# hoặc
brew services list | grep mariadb

# Khởi động lại database
sudo systemctl restart mariadb
# hoặc
brew services restart mariadb

# Kiểm tra kết nối
mysql -u root -proot -e "SELECT 1;"
```

### ❌ Lỗi: `JSP page not found` hoặc `404 error`

Đảm bảo file JSP nằm đúng vị trí:
```
src/main/webapp/WEB-INF/views/
```

### ❌ Lỗi: `Port 8080 already in use`

```bash
# Tìm process đang dùng port 8080
lsof -i :8080
# hoặc
netstat -ano | findstr :8080  # Windows

# Kill process
kill -9 <PID>

# Hoặc thay đổi port trong application.properties
server.port=8081
```

---

## 🎨 CẤU TRÚC DỰ ÁN

```
ShoppingMeVaBe/
├── src/
│   ├── main/
│   │   ├── java/iuh/student/www/
│   │   │   ├── controllers/      # REST Controllers
│   │   │   ├── models/           # JPA Entities
│   │   │   ├── repositories/     # Data Access Layer
│   │   │   ├── services/         # Business Logic
│   │   │   ├── config/           # Configuration
│   │   │   └── ShoppingMomadnBabyApplication.java
│   │   ├── resources/
│   │   │   ├── application.properties         # MariaDB config (default)
│   │   │   ├── application-dev.properties     # H2 config (development)
│   │   │   ├── application-prod.properties    # MySQL config (production)
│   │   │   └── static/           # CSS, JS, Images
│   │   └── webapp/
│   │       └── WEB-INF/views/    # JSP files
│   └── test/                     # Unit tests
├── pom.xml                       # Maven dependencies
├── run-dev.sh                    # Development script (H2)
├── start-prod.sh                 # Production script (MariaDB/MySQL)
└── README.md
```

---

## 📚 PROFILES SPRING BOOT

Ứng dụng hỗ trợ 3 profiles:

### 1. **dev** - Development với H2
```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
# hoặc
./run-dev.sh
```

### 2. **prod** - Production với MySQL
```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=prod
# hoặc
./start-prod.sh
```

### 3. **default** - MariaDB (application.properties)
```bash
./mvnw spring-boot:run
```

---

## 🔐 THÔNG TIN ĐĂNG NHẬP MẶC ĐỊNH

### Database H2 Console:
- URL: http://localhost:8080/h2-console
- JDBC URL: `jdbc:h2:file:./data/ShopBabyandMomCute`
- Username: `sa`
- Password: *(để trống)*

### Application Admin:
- Username: `admin`
- Password: `admin123`

*(Lưu ý: Thông tin này có thể thay đổi tùy vào cấu hình trong code)*

---

## 📞 HỖ TRỢ

### Tài liệu thêm:
- `DATABASE_SETUP.md` - Hướng dẫn chi tiết về database
- `SETUP_MARIADB.md` - Hướng dẫn cài đặt MariaDB
- `PRODUCTION_SETUP.md` - Cấu hình production
- `README_MOMO.md` - Tích hợp MoMo Payment

### Kiểm tra logs:
```bash
# Logs của ứng dụng
tail -f logs/spring.log

# Hoặc xem trong console khi chạy
./mvnw spring-boot:run
```

---

## 🎯 CHECKLIST KHỞI ĐỘNG

- [ ] Java 17+ đã cài đặt (`java -version`)
- [ ] Maven đã cài đặt (`mvn -version`) hoặc dùng `./mvnw`
- [ ] Port 8080 không bị chiếm dụng
- [ ] Quyền thực thi cho các script (`chmod +x *.sh`)
- [ ] Internet connection (lần đầu chạy để tải dependencies)

### Với H2 (Dev):
- [ ] Thư mục `./data` có thể tạo được

### Với MariaDB/MySQL (Prod):
- [ ] Database server đang chạy
- [ ] Database `shop_me_va_be` đã được tạo
- [ ] Username/password trong application.properties đúng

---

## 🚀 QUICK COMMANDS

```bash
# Chạy nhanh nhất (H2 - Development)
./run-dev.sh

# Chạy production (MariaDB/MySQL)
./start-prod.sh

# Build without tests
./mvnw clean package -DskipTests

# Run tests
./mvnw test

# Clean build
./mvnw clean install

# Generate JAR file
./mvnw clean package
# Output: target/www-0.0.1-SNAPSHOT.jar

# Run JAR directly
java -jar target/www-0.0.1-SNAPSHOT.jar
```

---

## ✅ HOÀN TẤT!

Sau khi chạy thành công, truy cập:
- 🌐 **Website**: http://localhost:8080
- 🗄️ **H2 Console**: http://localhost:8080/h2-console (nếu dùng H2)
- 📖 **API Docs**: http://localhost:8080/swagger-ui.html

Chúc bạn code vui vẻ! 🍼👶
