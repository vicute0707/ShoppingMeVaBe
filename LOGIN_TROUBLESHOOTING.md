# 🔐 TROUBLESHOOTING ĐĂNG NHẬP

## Vấn Đề: Không Đăng Nhập Được

### ✅ Checklist Kiểm Tra

#### 1. Đã Import Database Chưa?
```bash
# Kiểm tra dữ liệu trong database
mysql -u root -psapassword shop_me_va_be

# Xem danh sách users
SELECT id, full_name, email, role, enabled FROM users;

# Kiểm tra password đã mã hóa
SELECT email, password FROM users WHERE email = 'admin@shopmevabe.com';
```

**Kết quả mong đợi:**
- Phải có 5 users
- Password phải là BCrypt hash: `$2a$10$N9qo8uLOickgx2ZMRZoMyeI...`
- `enabled` phải là `1` (TRUE)

#### 2. Database Có Đúng Không?

**Nếu chưa có dữ liệu:**
```bash
# Import lại data.sql
mysql -u root -psapassword shop_me_va_be < src/main/resources/db/data.sql

# Hoặc từ console
mysql -u root -psapassword shop_me_va_be
source D:/ShoppingMomadnBaby/src/main/resources/db/data.sql;
exit;
```

#### 3. Kiểm Tra Application Running

```bash
# Xem log khi start
mvnw spring-boot:run

# Tìm dòng này trong log:
# "Initializing sample data..." (từ DataInitializer)
```

**Nếu thấy dòng "Initializing sample data..."** → DataInitializer đang chạy (tạo dữ liệu khác)
- DataInitializer tạo user: `admin@shopping.com` (KHÁC với data.sql!)
- Phải disable DataInitializer (đã disable rồi)

#### 4. Test Login

**Tài khoản từ data.sql:**
```
Email: admin@shopmevabe.com
Password: admin123
```

**Tài khoản từ DataInitializer (nếu chạy):**
```
Email: admin@shopping.com
Password: admin123
```

## 🔧 Giải Pháp

### Giải Pháp 1: Import Dữ Liệu Lại

```bash
# Xóa dữ liệu cũ và import lại
mysql -u root -psapassword shop_me_va_be << EOF
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE order_details;
TRUNCATE TABLE orders;
TRUNCATE TABLE products;
TRUNCATE TABLE categories;
TRUNCATE TABLE users;
SET FOREIGN_KEY_CHECKS = 1;
EOF

# Import data.sql
mysql -u root -psapassword shop_me_va_be < src/main/resources/db/data.sql

# Verify
mysql -u root -psapassword shop_me_va_be -e "SELECT email, role, enabled FROM users;"
```

### Giải Pháp 2: Kiểm Tra DataInitializer

Xem file: `src/main/java/iuh/student/www/config/DataInitializer.java`

**Dòng 26 phải có comment:**
```java
// @Component  // DISABLED - Using data.sql instead
```

Nếu không có `//` ở đầu `@Component` → DataInitializer đang chạy!

### Giải Pháp 3: Tạo User Mới Bằng Code

Nếu vẫn không được, tạo user test:

```java
// Trong DataInitializer.java, uncomment @Component
// Và sửa email thành admin@shopmevabe.com
```

Hoặc tạo trực tiếp trong database:

```sql
-- Password cho "admin123" đã BCrypt
INSERT INTO users (full_name, email, password, phone, address, role, enabled, created_at, updated_at)
VALUES (
  'Admin Test',
  'test@test.com',
  '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
  '0123456789',
  'Test Address',
  'ADMIN',
  TRUE,
  NOW(),
  NOW()
);
```

### Giải Pháp 4: Debug Login Flow

Thêm log vào CustomUserDetailsService:

```java
@Override
public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    log.info("🔍 Attempting login with email: {}", email);

    User user = userRepository.findByEmail(email)
            .orElseThrow(() -> {
                log.error("❌ User not found: {}", email);
                return new UsernameNotFoundException("User not found with email: " + email);
            });

    log.info("✅ User found: {} - Role: {} - Enabled: {}",
             user.getEmail(), user.getRole(), user.getEnabled());

    return new org.springframework.security.core.userdetails.User(
            user.getEmail(),
            user.getPassword(),
            user.getEnabled(),
            true, true, true,
            getAuthorities(user)
    );
}
```

## 🧪 Test Cases

### Test 1: Kiểm Tra Database
```bash
mysql -u root -psapassword shop_me_va_be << EOF
SELECT
  id,
  email,
  role,
  enabled,
  SUBSTRING(password, 1, 20) as password_preview
FROM users;
EOF
```

**Kết quả mong đợi:**
```
+----+--------------------------+----------+---------+----------------------+
| id | email                    | role     | enabled | password_preview     |
+----+--------------------------+----------+---------+----------------------+
|  1 | admin@shopmevabe.com     | ADMIN    |       1 | $2a$10$N9qo8uLOickgx |
|  2 | mai.nguyen@gmail.com     | CUSTOMER |       1 | $2a$10$N9qo8uLOickgx |
+----+--------------------------+----------+---------+----------------------+
```

### Test 2: Verify BCrypt Password
```bash
# Trong Java console hoặc test
String rawPassword = "admin123";
String encodedPassword = "$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy";
BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
boolean matches = encoder.matches(rawPassword, encodedPassword);
System.out.println("Password matches: " + matches); // Should be TRUE
```

### Test 3: Test Login API
```bash
# Test với curl
curl -X POST http://localhost:8081/perform-login \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "username=admin@shopmevabe.com&password=admin123" \
  -v
```

## 📝 Common Issues

### Issue 1: "Bad credentials"
**Nguyên nhân:**
- Password không match
- User không tồn tại
- Password trong DB không phải BCrypt

**Fix:**
```sql
-- Update password mới (BCrypt của "admin123")
UPDATE users
SET password = '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy'
WHERE email = 'admin@shopmevabe.com';
```

### Issue 2: "User is disabled"
**Nguyên nhân:**
- `enabled` = FALSE trong database

**Fix:**
```sql
UPDATE users SET enabled = TRUE WHERE email = 'admin@shopmevabe.com';
```

### Issue 3: "User not found"
**Nguyên nhân:**
- Email sai
- Chưa import dữ liệu
- DataInitializer tạo email khác

**Fix:**
```sql
-- Xem tất cả users
SELECT email FROM users;

-- Import lại nếu cần
```

## 🎯 Quick Fix Command

Chạy tất cả commands này để fix nhanh:

```bash
# 1. Clear và import lại database
mysql -u root -psapassword shop_me_va_be << 'EOF'
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE order_details;
TRUNCATE TABLE orders;
TRUNCATE TABLE products;
TRUNCATE TABLE categories;
TRUNCATE TABLE users;
SET FOREIGN_KEY_CHECKS = 1;
EOF

# 2. Import data.sql
mysql -u root -psapassword shop_me_va_be < src/main/resources/db/data.sql

# 3. Verify
mysql -u root -psapassword shop_me_va_be -e "SELECT email, role, enabled FROM users;"

# 4. Restart application
mvnw spring-boot:run
```

## 📞 Tài Khoản Đăng Nhập

Sau khi import data.sql:

| Email | Password | Role | Status |
|-------|----------|------|--------|
| admin@shopmevabe.com | admin123 | ADMIN | ✅ Enabled |
| mai.nguyen@gmail.com | admin123 | CUSTOMER | ✅ Enabled |
| hung.tran@gmail.com | admin123 | CUSTOMER | ✅ Enabled |
| lan.le@gmail.com | admin123 | CUSTOMER | ✅ Enabled |
| tuan.pham@gmail.com | admin123 | CUSTOMER | ✅ Enabled |

---

**Lưu ý:** Nếu vẫn không đăng nhập được, check log trong console khi chạy `mvnw spring-boot:run` để xem lỗi chi tiết.
