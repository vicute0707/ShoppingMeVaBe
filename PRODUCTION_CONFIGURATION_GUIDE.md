# 📋 HƯỚNG DẪN CẤU HÌNH PRODUCTION - SHOP MẸ VÀ BÉ

## 📊 Tổng Quan Đánh Giá

### ✅ Điểm Mạnh Hiện Tại
- ✓ Sử dụng Spring Boot 3.2.5 với Spring Security
- ✓ Có cấu hình MariaDB cho production
- ✓ Tích hợp thanh toán MoMo với signature verification
- ✓ Authentication sử dụng BCrypt password encoding
- ✓ Phân quyền rõ ràng (ADMIN, CUSTOMER)
- ✓ REST API với Swagger/OpenAPI documentation
- ✓ Validation với Bean Validation
- ✓ Email notification service

### ⚠️ Vấn Đề Cần Khắc Phục Ngay

#### 🔴 **CRITICAL** - Vấn Đề Bảo Mật Nghiêm Trọng

1. **Email Credentials Hardcoded** (application.properties:43-44)
   ```properties
   spring.mail.username=nguyenthituongvi2023@gmail.com
   spring.mail.password=alxe raor rzkl ijrx  # ⚠️ NGUY HIỂM!
   ```
   **Giải pháp:** Sử dụng biến môi trường

2. **Database Password Yếu** (application.properties:17)
   ```properties
   spring.datasource.password=root  # ⚠️ NGUY HIỂM!
   ```
   **Giải pháp:** Sử dụng password mạnh và biến môi trường

3. **Logging Level DEBUG trong Production** (application.properties:49-51)
   ```properties
   logging.level.org.springframework.security=DEBUG  # ⚠️ Lộ thông tin nhạy cảm
   logging.level.org.hibernate.SQL=DEBUG
   ```

4. **Thiếu Cấu Hình MoMo**
   - Không có cấu hình `momo.*` trong application.properties
   - MoMoConfig sẽ không hoạt động trong production

---

## 🔧 HƯỚNG DẪN CẤU HÌNH CHI TIẾT

### 1. Cấu Hình MariaDB Production

#### Bước 1: Tạo Database
```bash
# Đăng nhập MariaDB
mysql -u root -p

# Tạo database
CREATE DATABASE shop_me_va_be
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

# Tạo user riêng cho ứng dụng (BẢO MẬT HƠN)
CREATE USER 'shopmevabe_user'@'localhost' IDENTIFIED BY 'YOUR_STRONG_PASSWORD_HERE';

# Phân quyền
GRANT SELECT, INSERT, UPDATE, DELETE ON shop_me_va_be.* TO 'shopmevabe_user'@'localhost';
FLUSH PRIVILEGES;

# Kiểm tra
SHOW DATABASES;
```

#### Bước 2: Import Dữ Liệu Mẫu
```bash
# Import từ file data.sql
mysql -u shopmevabe_user -p shop_me_va_be < src/main/resources/db/data.sql

# Hoặc từ MariaDB console
mysql -u shopmevabe_user -p shop_me_va_be
source /path/to/src/main/resources/db/data.sql;
```

#### Bước 3: Cấu Hình Connection Pool (Khuyến nghị)
Thêm vào `application-prod.properties`:
```properties
# HikariCP Configuration (Default cho Spring Boot)
spring.datasource.hikari.maximum-pool-size=10
spring.datasource.hikari.minimum-idle=5
spring.datasource.hikari.connection-timeout=20000
spring.datasource.hikari.idle-timeout=300000
spring.datasource.hikari.max-lifetime=1200000
```

---

### 2. Cấu Hình MoMo Payment Gateway

#### Bước 1: Đăng Ký MoMo Business
1. Truy cập: https://business.momo.vn/
2. Đăng ký tài khoản doanh nghiệp
3. Lấy thông tin:
   - Partner Code
   - Access Key
   - Secret Key

#### Bước 2: Thêm Cấu Hình MoMo
Tạo file `application-prod.properties`:
```properties
# MoMo Payment Configuration
momo.endpoint=https://payment.momo.vn/v2/gateway/api/create
momo.partner-code=${MOMO_PARTNER_CODE}
momo.access-key=${MOMO_ACCESS_KEY}
momo.secret-key=${MOMO_SECRET_KEY}
momo.redirect-url=${APP_BASE_URL}/payment/momo/callback
momo.ipn-url=${APP_BASE_URL}/payment/momo/ipn
momo.request-type=captureWallet
```

#### Bước 3: Cấu Hình Callback URLs
- **Redirect URL**: URL người dùng được chuyển về sau khi thanh toán
  - Ví dụ: `https://yourdomain.com/payment/momo/callback`
- **IPN URL**: URL nhận notification từ MoMo server
  - Ví dụ: `https://yourdomain.com/payment/momo/ipn`
  - ⚠️ **Lưu ý:** URL này phải public, không được localhost

#### Bước 4: Test MoMo Integration
```bash
# Test environment
momo.endpoint=https://test-payment.momo.vn/v2/gateway/api/create

# Production
momo.endpoint=https://payment.momo.vn/v2/gateway/api/create
```

**Kiểm Tra Logic MoMo:**
- ✅ Signature generation (HMAC SHA256) - `MoMoService.java:167-180`
- ✅ Signature verification - `MoMoService.java:131-158`
- ✅ Order ID extraction - `MoMoService.java:187-196`
- ✅ Callback handling - `PaymentController.java:73-158`
- ✅ IPN handling - `PaymentController.java:164-206`

---

### 3. Cấu Hình Application Properties cho Production

#### Tạo file `application-prod.properties`
```properties
# ==========================================
# PRODUCTION CONFIGURATION
# Shop Mẹ và Bé - Production Environment
# ==========================================

spring.application.name=ShopMeVaBeCute

# Server Configuration
server.port=${SERVER_PORT:8080}
server.servlet.session.timeout=30m

# Application Info
app.name=Cửa Hàng Mẹ và Bé
app.name.english=Shop Baby & Mom Cute
app.description=Chuyên cung cấp sản phẩm chất lượng cho mẹ và bé yêu
app.slogan=Yêu thương mẹ - Chăm sóc bé

# ==========================================
# DATABASE CONFIGURATION - MariaDB
# ==========================================
spring.datasource.url=jdbc:mariadb://${DB_HOST:localhost}:${DB_PORT:3306}/${DB_NAME:shop_me_va_be}?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Ho_Chi_Minh
spring.datasource.driver-class-name=org.mariadb.jdbc.Driver
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}

# HikariCP Configuration
spring.datasource.hikari.maximum-pool-size=10
spring.datasource.hikari.minimum-idle=5
spring.datasource.hikari.connection-timeout=20000

# ==========================================
# JPA/HIBERNATE CONFIGURATION
# ==========================================
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.format_sql=false
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MariaDBDialect
spring.jpa.properties.hibernate.jdbc.time_zone=Asia/Ho_Chi_Minh

# ==========================================
# JSP CONFIGURATION
# ==========================================
spring.mvc.view.prefix=/WEB-INF/views/
spring.mvc.view.suffix=.jsp

# ==========================================
# EMAIL CONFIGURATION
# ==========================================
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=${MAIL_USERNAME}
spring.mail.password=${MAIL_PASSWORD}
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
spring.mail.properties.mail.smtp.starttls.required=true

# ==========================================
# MOMO PAYMENT CONFIGURATION
# ==========================================
momo.endpoint=https://payment.momo.vn/v2/gateway/api/create
momo.partner-code=${MOMO_PARTNER_CODE}
momo.access-key=${MOMO_ACCESS_KEY}
momo.secret-key=${MOMO_SECRET_KEY}
momo.redirect-url=${APP_BASE_URL}/payment/momo/callback
momo.ipn-url=${APP_BASE_URL}/payment/momo/ipn
momo.request-type=captureWallet

# ==========================================
# LOGGING CONFIGURATION - PRODUCTION
# ==========================================
logging.level.root=INFO
logging.level.iuh.student.www=INFO
logging.level.org.springframework.security=WARN
logging.level.org.hibernate.SQL=WARN
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=WARN

# Log file
logging.file.name=/var/log/shopmevabe/application.log
logging.file.max-size=10MB
logging.file.max-history=30

# ==========================================
# SECURITY CONFIGURATION
# ==========================================
# Disable H2 Console in production
spring.h2.console.enabled=false

# ==========================================
# ACTUATOR (Optional - Monitoring)
# ==========================================
management.endpoints.web.exposure.include=health,info,metrics
management.endpoint.health.show-details=when-authorized
```

---

### 4. Cấu Hình Biến Môi Trường

#### Cách 1: Sử dụng .env file (Development/Staging)
Tạo file `.env`:
```bash
# Database
DB_HOST=localhost
DB_PORT=3306
DB_NAME=shop_me_va_be
DB_USERNAME=shopmevabe_user
DB_PASSWORD=YourStrongPassword123!@#

# Email
MAIL_USERNAME=your-email@gmail.com
MAIL_PASSWORD=your-app-specific-password

# MoMo
MOMO_PARTNER_CODE=MOMOXXX
MOMO_ACCESS_KEY=your_access_key
MOMO_SECRET_KEY=your_secret_key
APP_BASE_URL=https://yourdomain.com

# Server
SERVER_PORT=8080
```

#### Cách 2: Export trong Linux/MacOS
```bash
export DB_HOST=localhost
export DB_USERNAME=shopmevabe_user
export DB_PASSWORD="YourStrongPassword123!@#"
export MAIL_USERNAME="your-email@gmail.com"
export MAIL_PASSWORD="your-app-password"
export MOMO_PARTNER_CODE="MOMOXXX"
export MOMO_ACCESS_KEY="your_access_key"
export MOMO_SECRET_KEY="your_secret_key"
export APP_BASE_URL="https://yourdomain.com"
```

#### Cách 3: Sử dụng systemd service (Linux Production)
Tạo file `/etc/systemd/system/shopmevabe.service`:
```ini
[Unit]
Description=Shop Me Va Be Application
After=mariadb.service

[Service]
Type=simple
User=shopmevabe
WorkingDirectory=/opt/shopmevabe
ExecStart=/usr/bin/java -jar \
  -Dspring.profiles.active=prod \
  /opt/shopmevabe/app.jar

Environment="DB_HOST=localhost"
Environment="DB_USERNAME=shopmevabe_user"
Environment="DB_PASSWORD=YourStrongPassword"
Environment="MAIL_USERNAME=your-email@gmail.com"
Environment="MAIL_PASSWORD=your-app-password"
Environment="MOMO_PARTNER_CODE=MOMOXXX"
Environment="MOMO_ACCESS_KEY=your_key"
Environment="MOMO_SECRET_KEY=your_secret"
Environment="APP_BASE_URL=https://yourdomain.com"

Restart=always
RestartSec=10

[Install]
WantedBy=multi-user.target
```

---

### 5. Authentication & Authorization - Phân Tích

#### ✅ **Authentication Logic - ĐÚNG**

**CustomUserDetailsService** (`src/main/java/iuh/student/www/security/CustomUserDetailsService.java`):
```java
- Load user by email (not username) ✓
- Check user enabled status ✓
- Use BCrypt password encoder ✓
- Map roles correctly with ROLE_ prefix ✓
```

**Quy Trình Đăng Nhập:**
1. User submit email + password → `/perform-login`
2. Spring Security gọi `CustomUserDetailsService.loadUserByUsername(email)`
3. Verify password với BCrypt
4. Tạo Authentication object với authorities
5. Redirect đến `/login-success`

#### ✅ **Authorization - Phân Quyền ĐÚNG**

**SecurityConfig.java** - Phân quyền chi tiết:

| Endpoint | Access Level | Ghi Chú |
|----------|--------------|---------|
| `/`, `/home`, `/products/**` | Public | Guest có thể xem |
| `/cart/**` | Public | Guest có thể thêm vào cart |
| `/register`, `/login` | Public | Authentication pages |
| `/checkout/**`, `/orders/**` | ROLE_CUSTOMER | Chỉ customer đã login |
| `/admin/**` | ROLE_ADMIN | Chỉ admin |
| `/api/public/**` | Public | REST API công khai |
| `/api/customer/**` | CUSTOMER, ADMIN | Customer & Admin |
| `/api/admin/**` | ROLE_ADMIN | Chỉ admin |

**User Entity** - 2 Roles:
- `CUSTOMER` - Khách hàng (default)
- `ADMIN` - Quản trị viên

**Tài Khoản Mẫu** (theo data.sql):
```
Admin: admin@shopmevabe.com / admin123
Customer: mai.nguyen@gmail.com / admin123
```

---

### 6. Spring Ecosystem - Đánh Giá Kiến Trúc

#### ✅ **Architecture Pattern: Layered Architecture**

```
┌─────────────────────────────────────┐
│   Presentation Layer (Controllers)   │
│  - Web Controllers (JSP)             │
│  - REST Controllers (API)            │
│  - @Controller, @RestController      │
└─────────────────────────────────────┘
              ↓
┌─────────────────────────────────────┐
│      Service Layer (Business)        │
│  - UserService                       │
│  - ProductService                    │
│  - OrderService                      │
│  - MoMoService                       │
│  - @Service                          │
└─────────────────────────────────────┘
              ↓
┌─────────────────────────────────────┐
│   Persistence Layer (Data Access)    │
│  - JPA Repositories                  │
│  - @Repository                       │
└─────────────────────────────────────┘
              ↓
┌─────────────────────────────────────┐
│       Database (MariaDB)             │
└─────────────────────────────────────┘
```

#### ✅ **Spring Boot Features Used**

1. **Spring Boot Starters:**
   - `spring-boot-starter-web` - Web MVC & REST API
   - `spring-boot-starter-data-jpa` - JPA/Hibernate
   - `spring-boot-starter-security` - Authentication & Authorization
   - `spring-boot-starter-mail` - Email notifications
   - `spring-boot-starter-validation` - Bean Validation

2. **Web Services:**
   - ✅ **REST API** với JSON responses
   - ✅ **Swagger/OpenAPI** documentation (springdoc-openapi)
   - ✅ **DTO Pattern** cho data transfer
   - ✅ **Exception Handling** với @ControllerAdvice (cần kiểm tra)

3. **Security Features:**
   - ✅ Form-based authentication
   - ✅ BCrypt password encoding
   - ✅ CSRF protection (disabled cho API)
   - ✅ Session management (max 1 session/user)
   - ✅ Role-based access control

#### 📊 **API Endpoints Overview**

**Guest APIs:**
```
POST /api/auth/register - Đăng ký tài khoản
GET  /api/public/products - Xem sản phẩm
GET  /api/public/categories - Xem danh mục
```

**Customer APIs:**
```
POST /api/customer/orders - Tạo đơn hàng
GET  /api/customer/orders - Xem đơn hàng của mình
POST /api/customer/cart - Thêm vào giỏ hàng
```

**Admin APIs:**
```
GET    /api/admin/products - Quản lý sản phẩm
POST   /api/admin/products
PUT    /api/admin/products/{id}
DELETE /api/admin/products/{id}
GET    /api/admin/orders - Quản lý đơn hàng
GET    /api/admin/users - Quản lý người dùng
```

---

## 🚀 HƯỚNG DẪN DEPLOY PRODUCTION

### Bước 1: Build Application
```bash
# Clean và build với Maven
./mvnw clean package -DskipTests

# Hoặc với tests
./mvnw clean package

# JAR file sẽ ở: target/www-0.0.1-SNAPSHOT.jar
```

### Bước 2: Chuẩn Bị Server
```bash
# Tạo user cho application
sudo useradd -r -s /bin/false shopmevabe

# Tạo thư mục
sudo mkdir -p /opt/shopmevabe
sudo mkdir -p /var/log/shopmevabe

# Copy JAR file
sudo cp target/www-0.0.1-SNAPSHOT.jar /opt/shopmevabe/app.jar

# Set permissions
sudo chown -R shopmevabe:shopmevabe /opt/shopmevabe
sudo chown -R shopmevabe:shopmevabe /var/log/shopmevabe
```

### Bước 3: Run với Production Profile
```bash
# Run trực tiếp
java -jar -Dspring.profiles.active=prod app.jar

# Hoặc với systemd
sudo systemctl daemon-reload
sudo systemctl enable shopmevabe
sudo systemctl start shopmevabe
sudo systemctl status shopmevabe
```

### Bước 4: Nginx Reverse Proxy (Khuyến nghị)
```nginx
server {
    listen 80;
    server_name yourdomain.com;

    # Redirect to HTTPS
    return 301 https://$server_name$request_uri;
}

server {
    listen 443 ssl http2;
    server_name yourdomain.com;

    ssl_certificate /etc/ssl/certs/yourdomain.crt;
    ssl_certificate_key /etc/ssl/private/yourdomain.key;

    location / {
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
}
```

---

## 📝 CHECKLIST PRODUCTION

### Bảo Mật
- [ ] Thay đổi tất cả credentials hardcoded bằng biến môi trường
- [ ] Sử dụng HTTPS (SSL/TLS certificate)
- [ ] Tắt H2 console
- [ ] Set logging level = WARN/INFO
- [ ] Enable firewall (chỉ mở port 80, 443)
- [ ] Database user riêng với quyền hạn chế
- [ ] Backup database định kỳ

### Cấu Hình
- [ ] Tạo application-prod.properties
- [ ] Cấu hình biến môi trường
- [ ] Cấu hình MoMo credentials
- [ ] Test MoMo payment flow
- [ ] Cấu hình email SMTP
- [ ] Set timezone = Asia/Ho_Chi_Minh

### Database
- [ ] Import data.sql vào MariaDB
- [ ] Tạo database user riêng
- [ ] Test connection pool
- [ ] Set ddl-auto=validate (không auto-update)

### Monitoring
- [ ] Setup logging to file
- [ ] Configure log rotation
- [ ] Enable Actuator endpoints
- [ ] Monitor memory/CPU usage

---

## 🔍 KẾT LUẬN

### Điểm Mạnh
✅ Kiến trúc rõ ràng với Spring MVC + REST API
✅ Security configuration đúng chuẩn
✅ MoMo integration với signature verification
✅ Phân quyền chi tiết và logic
✅ Sử dụng DTO pattern và validation

### Cần Cải Thiện
⚠️ **CRITICAL:** Remove hardcoded credentials
⚠️ **HIGH:** Add MoMo configuration
⚠️ **MEDIUM:** Add global exception handler
⚠️ **MEDIUM:** Add API rate limiting
⚠️ **LOW:** Add caching cho products

---

**Tác giả:** Claude AI
**Ngày tạo:** 2025-11-09
**Version:** 1.0
