# 🔒 BẢO MẬT - CÁC VẤN ĐỀ VÀ GIẢI PHÁP

## ⚠️ CÁC VẤN ĐỀ BẢO MẬT PHÁT HIỆN

### 🔴 CRITICAL - Ưu tiên cao nhất

#### 1. Hardcoded Email Credentials
**File:** `src/main/resources/application.properties` (lines 43-44)

**Vấn đề:**
```properties
spring.mail.username=nguyenthituongvi2023@gmail.com
spring.mail.password=alxe raor rzkl ijrx
```
- Thông tin đăng nhập email được lưu trực tiếp trong code
- Có thể bị lộ khi push lên Git
- Vi phạm nguyên tắc bảo mật

**Giải pháp:**
```properties
spring.mail.username=${MAIL_USERNAME}
spring.mail.password=${MAIL_PASSWORD}
```

#### 2. Database Password Yếu
**File:** `src/main/resources/application.properties` (line 17)

**Vấn đề:**
```properties
spring.datasource.password=root
```
- Sử dụng password mặc định "root"
- Dễ bị tấn công brute force

**Giải pháp:**
```properties
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
```

#### 3. Thiếu Cấu Hình MoMo
**Vấn đề:**
- MoMoConfig class cần các properties `momo.*`
- Hiện tại không có trong application.properties
- Ứng dụng sẽ fail khi khởi động với profile production

**Giải pháp:** Xem file `application-prod.properties` đã tạo

#### 4. Logging Level DEBUG trong Production
**File:** `src/main/resources/application.properties` (lines 49-51)

**Vấn đề:**
```properties
logging.level.org.springframework.security=DEBUG
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE
```
- Log quá chi tiết có thể lộ thông tin nhạy cảm
- Ảnh hưởng performance
- File log quá lớn

**Giải pháp:**
```properties
logging.level.org.springframework.security=WARN
logging.level.org.hibernate.SQL=WARN
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=WARN
```

---

## ✅ HƯỚNG DẪN KHẮC PHỤC

### Bước 1: Cập Nhật application.properties

Thay thế `src/main/resources/application.properties` với version an toàn:

```properties
spring.application.name=ShopMeVaBeCute

# Server Configuration
server.port=8080
server.servlet.session.timeout=30m

# Application Info - Cửa Hàng Mẹ và Bé 🍼👶
app.name=Cửa Hàng Mẹ và Bé
app.name.english=Shop Baby & Mom Cute
app.description=Chuyên cung cấp sản phẩm chất lượng cho mẹ và bé yêu
app.slogan=Yêu thương mẹ - Chăm sóc bé

# Database Configuration - MariaDB (Production)
spring.datasource.url=jdbc:mariadb://localhost:3306/shop_me_va_be?createDatabaseIfNotExist=true&useUnicode=true&characterEncoding=UTF-8
spring.datasource.driver-class-name=org.mariadb.jdbc.Driver
spring.datasource.username=${DB_USERNAME:root}
spring.datasource.password=${DB_PASSWORD:root}

# Alternative H2 Configuration (Comment out MariaDB above and uncomment below for H2)
#spring.datasource.url=jdbc:h2:file:./data/ShopBabyandMomCute
#spring.datasource.driver-class-name=org.h2.Driver
#spring.datasource.username=sa
#spring.datasource.password=

# H2 Console (Only for H2 database)
spring.h2.console.enabled=false
spring.h2.console.path=/h2-console

# JPA/Hibernate Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MariaDBDialect
spring.jpa.properties.hibernate.jdbc.time_zone=UTC

# JSP Configuration
spring.mvc.view.prefix=/WEB-INF/views/
spring.mvc.view.suffix=.jsp

# Email Configuration (Gmail)
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=${MAIL_USERNAME:your-email@gmail.com}
spring.mail.password=${MAIL_PASSWORD:your-app-password}
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true

# MoMo Payment Configuration
momo.endpoint=${MOMO_ENDPOINT:https://test-payment.momo.vn/v2/gateway/api/create}
momo.partner-code=${MOMO_PARTNER_CODE:MOMO}
momo.access-key=${MOMO_ACCESS_KEY:}
momo.secret-key=${MOMO_SECRET_KEY:}
momo.redirect-url=${MOMO_REDIRECT_URL:http://localhost:8080/payment/momo/callback}
momo.ipn-url=${MOMO_IPN_URL:http://localhost:8080/payment/momo/ipn}
momo.request-type=${MOMO_REQUEST_TYPE:captureWallet}

# Logging Configuration
logging.level.root=INFO
logging.level.iuh.student.www=INFO
logging.level.org.springframework.security=INFO
logging.level.org.hibernate.SQL=INFO
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=INFO
```

### Bước 2: Tạo File .env

Copy file `.env.example` thành `.env`:
```bash
cp .env.example .env
```

Sau đó chỉnh sửa `.env` với thông tin thực tế:
```bash
# Database
DB_USERNAME=shopmevabe_user
DB_PASSWORD=YourRealPassword123!

# Email
MAIL_USERNAME=your-real-email@gmail.com
MAIL_PASSWORD=your-real-app-password

# MoMo (Development)
MOMO_ENDPOINT=https://test-payment.momo.vn/v2/gateway/api/create
MOMO_PARTNER_CODE=MOMO
MOMO_ACCESS_KEY=your_test_access_key
MOMO_SECRET_KEY=your_test_secret_key
```

### Bước 3: Load Environment Variables

#### Cách 1: IDE (IntelliJ IDEA / Eclipse)
1. Cài đặt plugin: EnvFile (IntelliJ) hoặc Properties Editor
2. Configure Run Configuration → Environment Variables
3. Load từ file `.env`

#### Cách 2: Terminal
```bash
# Export tất cả variables
export DB_USERNAME=shopmevabe_user
export DB_PASSWORD=YourPassword123
export MAIL_USERNAME=your-email@gmail.com
export MAIL_PASSWORD=your-app-password

# Hoặc load từ file
set -a
source .env
set +a

# Chạy ứng dụng
./mvnw spring-boot:run
```

#### Cách 3: Spring Boot Plugin
```bash
# Tạo file .env
# Chạy với spring-boot:run
./mvnw spring-boot:run
```

---

## 🔐 CHECKLIST BẢO MẬT

### Trước khi Deploy Production

- [ ] **Remove hardcoded credentials** từ application.properties
- [ ] **Create .env file** với credentials thực tế
- [ ] **Verify .env is in .gitignore** (không commit .env lên Git)
- [ ] **Use strong database password** (ít nhất 12 ký tự, có số, chữ hoa, chữ thường, ký tự đặc biệt)
- [ ] **Enable HTTPS** (SSL/TLS certificate)
- [ ] **Set logging level = WARN** cho production
- [ ] **Disable H2 console** (`spring.h2.console.enabled=false`)
- [ ] **Change default admin password** (hiện tại: admin123)
- [ ] **Configure MoMo production credentials**
- [ ] **Test MoMo payment flow** trên test environment trước
- [ ] **Setup firewall** (chỉ mở port 80, 443)
- [ ] **Regular security updates** (dependencies)

### Password Policy Recommendations

#### Database Password:
```
✅ Good: Qj8#mK2$pL9@nB7!
❌ Bad:  root, admin, 123456, password
```

#### Email App Password:
```
✅ Use Gmail App Password (16 characters)
❌ Don't use regular Gmail password
Generate at: https://myaccount.google.com/apppasswords
```

---

## 📝 TESTING

### Test với Environment Variables

```bash
# Test database connection
DB_USERNAME=shopmevabe_user \
DB_PASSWORD=YourPassword \
./mvnw spring-boot:run

# Test với tất cả variables
set -a && source .env && set +a
./mvnw spring-boot:run
```

### Verify Configuration

```bash
# Check if environment variables are loaded
echo $DB_USERNAME
echo $MAIL_USERNAME

# Test database connection
mysql -u $DB_USERNAME -p$DB_PASSWORD -e "SHOW DATABASES;"
```

---

## 🚨 XỬ LÝ KHI LỘ CREDENTIALS

Nếu đã commit credentials lên Git:

### Bước 1: Đổi tất cả credentials ngay lập tức
- Đổi database password
- Đổi email password (revoke app password)
- Đổi MoMo credentials

### Bước 2: Remove từ Git history
```bash
# Install git-filter-repo
pip install git-filter-repo

# Remove sensitive file from history
git filter-repo --path src/main/resources/application.properties --invert-paths

# Force push
git push origin --force --all
```

### Bước 3: Add to .gitignore
```bash
echo "application-local.properties" >> .gitignore
git add .gitignore
git commit -m "Add sensitive files to gitignore"
```

---

## 📚 TÀI LIỆU THAM KHẢO

1. **Spring Boot Externalized Configuration:**
   https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.external-config

2. **Gmail App Passwords:**
   https://support.google.com/accounts/answer/185833

3. **MoMo Developer Documentation:**
   https://developers.momo.vn/

4. **OWASP Security Guidelines:**
   https://owasp.org/www-project-top-ten/

---

**Cập nhật lần cuối:** 2025-11-09
**Tác giả:** Security Review - Claude AI
