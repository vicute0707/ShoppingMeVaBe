# 🗄️ Hướng dẫn cài đặt MariaDB cho Shop Mẹ và Bé

## Cài đặt MariaDB trên Ubuntu/Debian

```bash
# Cập nhật package list
sudo apt update

# Cài đặt MariaDB Server
sudo apt install mariadb-server mariadb-client -y

# Khởi động MariaDB
sudo systemctl start mariadb
sudo systemctl enable mariadb

# Kiểm tra trạng thái
sudo systemctl status mariadb
```

## Cài đặt MariaDB trên macOS

```bash
# Sử dụng Homebrew
brew install mariadb

# Khởi động MariaDB
brew services start mariadb
```

## Cài đặt MariaDB trên Windows

1. Tải MariaDB từ: https://mariadb.org/download/
2. Chạy installer và làm theo hướng dẫn
3. Đặt mật khẩu root là: **root**

## Cấu hình Database

### 1. Đăng nhập vào MariaDB

```bash
# Linux/Mac
sudo mysql -u root

# Hoặc nếu đã có mật khẩu
mysql -u root -p
```

### 2. Tạo Database và User

```sql
-- Tạo database
CREATE DATABASE IF NOT EXISTS shop_me_va_be
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

-- Tạo user (nếu chưa có)
CREATE USER IF NOT EXISTS 'root'@'localhost' IDENTIFIED BY 'root';

-- Cấp quyền
GRANT ALL PRIVILEGES ON shop_me_va_be.* TO 'root'@'localhost';
FLUSH PRIVILEGES;

-- Kiểm tra
SHOW DATABASES;
USE shop_me_va_be;
```

### 3. Kiểm tra kết nối

```bash
mysql -u root -proot -e "SELECT 'Connection successful!' as status;"
```

## Xử lý lỗi thường gặp

### Lỗi: Access denied for user 'root'@'localhost'

```bash
# Reset mật khẩu root
sudo mysql
ALTER USER 'root'@'localhost' IDENTIFIED BY 'root';
FLUSH PRIVILEGES;
EXIT;
```

### Lỗi: Can't connect to local MySQL server

```bash
# Khởi động lại MariaDB
sudo systemctl restart mariadb
```

### Lỗi: Port 3306 already in use

```bash
# Kiểm tra process đang dùng port
sudo lsof -i :3306
# Hoặc
sudo netstat -tulpn | grep 3306

# Kill process cũ hoặc thay đổi port trong application.properties
```

## Sau khi cài đặt xong

Chạy ứng dụng với MariaDB:
```bash
./mvnw spring-boot:run
```

Hoặc sử dụng script:
```bash
./start-prod.sh
```
