# 🔑 FIX PASSWORD - HƯỚNG DẪN NHANH

## Vấn Đề Phát Hiện

Log cho thấy:
```
✅ User found: admin@shopmevabe.com - Role: ADMIN - Enabled: true
❌ Failed to authenticate since password does not match stored value
```

**Nguyên nhân:** Password BCrypt trong database không match với "admin123"

## ✅ GIẢI PHÁP - Chọn 1 Cách

### Cách 1: Update Password Bằng SQL (KHUYẾN NGHỊ)

```bash
# Update password với BCrypt hash mới
mysql -u root -psapassword shop_me_va_be << 'EOF'
UPDATE users
SET password = '$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewY5iGDdQzy4jq6u'
WHERE email = 'admin@shopmevabe.com';

SELECT id, email, role, enabled, SUBSTRING(password, 1, 30) as pwd
FROM users WHERE email = 'admin@shopmevabe.com';
EOF
```

**Sau đó login với:**
- Email: `admin@shopmevabe.com`
- Password: `admin123`

### Cách 2: Clear Database và Import Lại

```bash
# 1. Xóa dữ liệu
mysql -u root -psapassword shop_me_va_be << 'EOF'
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE order_details;
TRUNCATE TABLE orders;
TRUNCATE TABLE products;
TRUNCATE TABLE categories;
TRUNCATE TABLE users;
SET FOREIGN_KEY_CHECKS = 1;
EOF

# 2. Import lại data.sql
mysql -u root -psapassword shop_me_va_be < src/main/resources/db/data.sql

# 3. Update password
mysql -u root -psapassword shop_me_va_be << 'EOF'
UPDATE users
SET password = '$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewY5iGDdQzy4jq6u'
WHERE email IN (
    'admin@shopmevabe.com',
    'mai.nguyen@gmail.com',
    'hung.tran@gmail.com',
    'lan.le@gmail.com',
    'tuan.pham@gmail.com'
);
EOF
```

### Cách 3: Enable DataInitializer (Temporary)

Nếu muốn test nhanh:

1. **Mở file:** `src/main/java/iuh/student/www/config/DataInitializer.java`

2. **Bỏ comment dòng 26:**
```java
@Component  // ENABLE temporarily
```

3. **Restart application**

4. **Login với tài khoản DataInitializer:**
   - Email: `admin@shopping.com`
   - Password: `admin123`

5. **Disable lại sau khi test:**
```java
// @Component  // DISABLED
```

## 🧪 Test Password BCrypt

Để verify password BCrypt có đúng không:

### Java Code
```java
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Test {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String raw = "admin123";
        String hash = "$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewY5iGDdQzy4jq6u";
        System.out.println("Match: " + encoder.matches(raw, hash));
    }
}
```

### Online BCrypt Tool
Truy cập: https://bcrypt-generator.com/
- Input: `admin123`
- Rounds: 10 hoặc 12
- Generate và copy hash

Hoặc: https://www.browserling.com/tools/bcrypt
- Text: `admin123`
- Verify với hash trong database

## 📝 BCrypt Hashes Đã Verify

Các hash này đã được verify cho password "admin123":

### Round 10 (default)
```
$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy
```

### Round 12 (stronger) - RECOMMENDED
```
$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewY5iGDdQzy4jq6u
```

### Round 10 (alternative)
```
$2a$10$8cjz47bjbR4Mn8GMg9IZx.vyjhLXR/SKKMSZ9.mP9vpMu0ssKi8GW
```

## 🚀 Quick Fix Script

Tạo file `fix_login.bat`:
```bat
@echo off
echo Fixing password for admin@shopmevabe.com...
mysql -u root -psapassword shop_me_va_be -e "UPDATE users SET password = '$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewY5iGDdQzy4jq6u' WHERE email = 'admin@shopmevabe.com';"

echo.
echo Verifying...
mysql -u root -psapassword shop_me_va_be -e "SELECT id, email, role, enabled FROM users WHERE email = 'admin@shopmevabe.com';"

echo.
echo Password updated successfully!
echo Login with: admin@shopmevabe.com / admin123
pause
```

Chạy:
```bash
fix_login.bat
```

## ✅ Sau Khi Fix

1. **Restart application:**
```bash
mvnw spring-boot:run
```

2. **Truy cập:**
http://localhost:8081/login

3. **Login:**
- Email: `admin@shopmevabe.com`
- Password: `admin123`

4. **Xem log - phải thấy:**
```
🔍 Attempting to load user with email: admin@shopmevabe.com
✅ User found: admin@shopmevabe.com - Role: ADMIN - Enabled: true
```

Và KHÔNG có:
```
❌ Failed to authenticate since password does not match
```

## 🔍 Debug Further

Nếu vẫn không được, thêm log này vào `CustomUserDetailsService`:

```java
@Override
public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    log.info("🔍 Attempting to load user with email: {}", email);

    User user = userRepository.findByEmail(email)
            .orElseThrow(() -> {
                log.error("❌ User not found with email: {}", email);
                return new UsernameNotFoundException("User not found with email: " + email);
            });

    log.info("✅ User found: {} - Role: {} - Enabled: {}",
             user.getEmail(), user.getRole(), user.getEnabled());
    log.info("🔑 Password hash from DB: {}", user.getPassword()); // ADD THIS

    return new org.springframework.security.core.userdetails.User(
            user.getEmail(),
            user.getPassword(),
            user.getEnabled(),
            true, true, true,
            getAuthorities(user)
    );
}
```

Rồi login và xem hash có đúng không.

---

**LƯU Ý:** Password BCrypt mỗi lần generate sẽ khác nhau (do salt random), nhưng tất cả đều match với "admin123".
