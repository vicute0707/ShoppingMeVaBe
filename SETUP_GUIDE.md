# 🛍️ SHOP MẸ VÀ BÉ - HƯỚNG DẪN SETUP

## 📋 Yêu Cầu Hệ Thống

- Java 17+
- MariaDB 10.6+
- Maven 3.8+
- Ngrok (cho MoMo callback nếu chạy local)

## 🚀 Hướng Dẫn Setup

### 1. Tạo Database MariaDB

```bash
# Đăng nhập MariaDB
mysql -u root -p

# Tạo database
CREATE DATABASE shop_me_va_be
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

# Thoát
exit;
```

### 2. Import Dữ Liệu Mẫu

```bash
# Import dữ liệu từ file data.sql
mysql -u root -psapassword shop_me_va_be < src/main/resources/db/data.sql
```

**Tài khoản mẫu sau khi import:**
- **Admin:** `admin@shopmevabe.com` / `admin123`
- **Customer:** `mai.nguyen@gmail.com` / `admin123`

### 3. Cấu Hình Application

File `src/main/resources/application.properties` đã được cấu hình sẵn với:

```properties
# Server
server.port=8081

# Database
spring.datasource.username=root
spring.datasource.password=sapassword

# Email (Gmail)
spring.mail.username=nguyenthituongvi2023@gmail.com
spring.mail.password=hkpw hzun lrnh xuxv

# MoMo Payment (Test Environment)
momo.partner-code=MOMO
momo.access-key=F8BBA842ECF85
momo.secret-key=K951B6PE1waDMi640xX08PD3vg6EkVlz

# App Base URL (Ngrok)
app.base-url=https://presophomore-adjunctly-margery.ngrok-free.dev
```

### 4. Setup Ngrok (Để Test MoMo Payment)

```bash
# Chạy ngrok
ngrok http 8081

# Copy HTTPS URL và cập nhật vào application.properties
app.base-url=https://your-ngrok-url.ngrok-free.dev
```

### 5. Chạy Application

```bash
# Build
./mvnw clean package

# Run
./mvnw spring-boot:run

# Hoặc chạy JAR
java -jar target/www-0.0.1-SNAPSHOT.jar
```

## 🌐 Truy Cập Ứng Dụng

- **Homepage:** http://localhost:8081/
- **Admin:** http://localhost:8081/admin
- **API Docs:** http://localhost:8081/swagger-ui.html
- **Health Check:** http://localhost:8081/actuator/health

## 🔧 Kiến Trúc & Công Nghệ

### Spring Ecosystem
- **Spring Boot 3.2.5** - Framework chính
- **Spring Security** - Authentication & Authorization
- **Spring Data JPA** - Data access layer
- **Spring Web MVC** - Web framework
- **Spring Mail** - Email notifications

### Database & ORM
- **MariaDB** - Production database
- **Hibernate** - ORM
- **HikariCP** - Connection pooling

### Security
- **BCrypt** - Password encoding
- **Role-based Access Control** - ADMIN, CUSTOMER
- **CSRF Protection** - For form submissions
- **Session Management** - 1 session per user

### Payment Integration
- **MoMo Payment Gateway**
  - HMAC SHA256 signature verification
  - IPN (Instant Payment Notification)
  - Callback URL handling

### API Documentation
- **Swagger/OpenAPI 3.0** - Interactive API docs
- **SpringDoc** - API documentation generator

## 📊 Cấu Trúc Database

### Tables
1. **users** - Người dùng (Admin, Customer)
2. **categories** - Danh mục sản phẩm
3. **products** - Sản phẩm
4. **orders** - Đơn hàng
5. **order_details** - Chi tiết đơn hàng

### Dữ Liệu Mẫu
- **5 users** (1 admin, 4 customers)
- **8 categories** (Sữa bột, Tã bỉm, Đồ chơi, etc.)
- **40 products** (Sản phẩm cho mẹ và bé)
- **4 orders** (Đơn hàng mẫu)

## 🔐 Phân Quyền & Bảo Mật

### Public Endpoints (Guest)
```
GET  /                      - Trang chủ
GET  /products/**          - Xem sản phẩm
GET  /cart/**              - Giỏ hàng
POST /api/auth/register    - Đăng ký
GET  /swagger-ui/**        - API documentation
```

### Customer Endpoints
```
GET  /checkout/**          - Thanh toán
GET  /orders/**            - Xem đơn hàng
POST /payment/momo/create/{orderId} - Tạo thanh toán MoMo
```

### Admin Endpoints
```
GET  /admin/**             - Quản trị
GET  /api/admin/products   - Quản lý sản phẩm
GET  /api/admin/orders     - Quản lý đơn hàng
GET  /api/admin/users      - Quản lý người dùng
```

## 💳 Thanh Toán MoMo

### Flow Thanh Toán
1. Customer tạo đơn hàng → Status: PENDING
2. Click "Thanh toán MoMo" → `/payment/momo/create/{orderId}`
3. Redirect đến trang thanh toán MoMo
4. Customer thanh toán trên MoMo
5. MoMo callback về → `/payment/momo/callback`
6. Cập nhật order status → PROCESSING
7. MoMo IPN notification → `/payment/momo/ipn`

### Security Checks
- ✅ Signature verification (HMAC SHA256)
- ✅ Order ownership validation
- ✅ Order status check (must be PENDING)
- ✅ Amount validation (> 0)
- ✅ Transaction ID tracking

## 📝 Logging

### Log Levels
- **Development:** DEBUG
- **Production:** INFO/WARN

### Important Logs
```bash
# MoMo payment
Creating MoMo payment for order: {orderId} by user: {email}
MoMo payment URL created successfully: {payUrl}
Payment successful for order #{orderId}

# Security
Unauthorized payment creation attempt for order #{orderId}
Invalid MoMo signature
```

## 🧪 Testing

### Test MoMo Payment (Local)
1. Chạy ngrok: `ngrok http 8081`
2. Cập nhật `app.base-url` với ngrok URL
3. Restart application
4. Tạo đơn hàng và test thanh toán
5. Sử dụng MoMo test credentials

### API Testing
- **Swagger UI:** http://localhost:8081/swagger-ui.html
- **Postman:** Import OpenAPI spec từ `/v3/api-docs`

## 🐛 Troubleshooting

### Database Connection Error
```bash
# Kiểm tra MariaDB đã chạy chưa
sudo systemctl status mariadb

# Kiểm tra credentials
mysql -u root -psapassword shop_me_va_be
```

### MoMo Callback Failed
```bash
# Kiểm tra ngrok đang chạy
curl https://your-url.ngrok-free.dev/actuator/health

# Check logs
tail -f logs/application.log
```

### Email Sending Failed
```bash
# Verify Gmail App Password
# Generate new: https://myaccount.google.com/apppasswords
```

## 📞 Support

- **GitHub Issues:** [Project Issues](https://github.com/vicute0707/ShoppingMeVaBe/issues)
- **Email:** nguyenthituongvi2023@gmail.com

---

**Version:** 1.0
**Last Updated:** 2025-11-09
**Author:** Team Shop Mẹ và Bé
