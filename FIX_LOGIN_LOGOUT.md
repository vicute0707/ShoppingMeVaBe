# HƯỚNG DẪN FIX LỖI ĐĂNG NHẬP VÀ ĐĂNG XUẤT

## ❌ Lỗi gặp phải:
- Lỗi 500: Cannot convert LocalDateTime to Date
- Không đăng xuất được
- Không đăng nhập được admin

## 🔍 Nguyên nhân:
Browser đang cache JWT cookie CŨ với format không đúng. Code đã được fix nhưng cookie cũ vẫn còn trong browser.

## ✅ GIẢI PHÁP - Làm theo thứ tự:

### **Bước 1: Clear Browser Cookies & Cache**

#### **Chrome/Edge:**
```
1. Nhấn Ctrl + Shift + Delete
2. Chọn "Cookies and other site data"
3. Chọn "Cached images and files"
4. Time range: "All time"
5. Click "Clear data"
```

#### **Firefox:**
```
1. Nhấn Ctrl + Shift + Delete
2. Chọn "Cookies" và "Cache"
3. Time range: "Everything"
4. Click "Clear Now"
```

#### **Hoặc dùng Incognito/Private Mode:**
```
Chrome: Ctrl + Shift + N
Firefox: Ctrl + Shift + P
Edge: Ctrl + Shift + N
```

---

### **Bước 2: Pull Code Mới Từ Git**

```powershell
# Trong thư mục D:\ShoppingMomadnBaby

# Kiểm tra branch hiện tại
git status

# Pull code mới
git pull origin claude/review-main-config-011CUxyBgNZTmd7i2rWzwxzr
```

---

### **Bước 3: Restart Ứng Dụng**

```powershell
# Stop app nếu đang chạy (Ctrl + C)

# Clean build
.\mvnw.cmd clean spring-boot:run
```

---

### **Bước 4: Đăng Nhập Lại**

**URL:** `http://localhost:8081/login`

**Tài khoản Admin:**
```
Email: admin@shopmevabe.com
Password: admin123
```

**Nếu vẫn lỗi:**
1. Dùng Incognito mode
2. Hoặc thử browser khác
3. Hoặc truy cập: http://localhost:8081/logout trước để clear cookies

---

## 🔧 Các Thay Đổi Đã Fix:

### **1. JwtUtil.java** - Fix LocalDateTime serialization
```java
// ❌ CŨ (gây lỗi):
claims.put("authorities", userDetails.getAuthorities());

// ✅ MỚI (đã fix):
claims.put("authorities", userDetails.getAuthorities().stream()
        .map(auth -> auth.getAuthority())
        .toList());
```

### **2. AuthController.java** - Improve logout
```java
@GetMapping("/logout")
public String logout(HttpServletRequest request, HttpServletResponse response) {
    // Clear JWT cookie
    Cookie jwtCookie = new Cookie("JWT_TOKEN", "");
    jwtCookie.setMaxAge(0);
    response.addCookie(jwtCookie);

    // Clear ALL cookies
    Cookie[] cookies = request.getCookies();
    if (cookies != null) {
        for (Cookie cookie : cookies) {
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        }
    }

    return "redirect:/login";
}
```

### **3. JwtAuthenticationFilter.java** - Better error handling
```java
// Skip empty or invalid JWT tokens
if (jwt != null && jwt.length() > 10) {
    try {
        username = jwtUtil.extractUsername(jwt);
    } catch (Exception e) {
        log.warn("Invalid JWT token, ignoring");
        jwt = null;  // Reset to avoid errors
    }
}

// Wrap authentication in try-catch
try {
    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
    if (jwtUtil.validateToken(jwt, userDetails)) {
        // Set authentication
    }
} catch (Exception e) {
    log.error("Authentication failed: {}", e.getMessage());
    // User will be anonymous - no crash
}
```

---

## 🎯 Test Authentication Flow:

### **1. Test Logout:**
```
1. Đăng nhập: http://localhost:8081/login
2. Click "Đăng xuất" trong menu
3. Kiểm tra cookie đã bị xóa (F12 → Application → Cookies)
4. Redirect về /login với message "Đăng xuất thành công!"
```

### **2. Test Login:**
```
1. Truy cập: http://localhost:8081/login
2. Nhập: admin@shopmevabe.com / admin123
3. Click "Login"
4. Admin → Redirect /admin/dashboard
5. Customer → Redirect /
6. Header hiển thị email và nút "Đăng xuất"
```

### **3. Test JWT Cookie:**
```
1. F12 → Application → Cookies → http://localhost:8081
2. Tìm cookie "JWT_TOKEN"
3. Value: eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
4. HttpOnly: true (checked)
5. Path: /
6. Expires: (24 hours from now)
```

---

## 📱 API Testing (Optional):

### **Login API:**
```bash
curl -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "admin@shopmevabe.com",
    "password": "admin123"
  }'
```

**Expected Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "email": "admin@shopmevabe.com",
  "fullName": "Admin Shop Mẹ và Bé",
  "role": "ADMIN",
  "userId": 1,
  "message": "Đăng nhập thành công!"
}
```

---

## ❗ Nếu Vẫn Lỗi:

### **Option 1: Force Clear Cookies via URL**
```
http://localhost:8081/logout
```

### **Option 2: Clear Browser Data Manually**
```
1. F12 (Developer Tools)
2. Application tab
3. Storage → Cookies → http://localhost:8081
4. Right click → Clear
5. Storage → Local Storage → Clear
6. Storage → Session Storage → Clear
7. Refresh page (F5)
```

### **Option 3: Database Check**
```sql
-- Check admin account exists
SELECT * FROM users WHERE email = 'admin@shopmevabe.com';

-- Check password is BCrypt hash (starts with $2a$)
SELECT email, password FROM users WHERE role = 'ADMIN';

-- Should return:
-- email: admin@shopmevabe.com
-- password: $2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy
```

---

## 🎉 Kết Quả Mong Đợi:

✅ Đăng nhập thành công
✅ JWT cookie được tạo
✅ Navigation menu hiển thị email user
✅ Nút "Đăng xuất" hiển thị
✅ Click "Đăng xuất" → cookies bị xóa
✅ Redirect về /login với success message
✅ Có thể đăng nhập lại với tài khoản khác
✅ Không còn lỗi 500 LocalDateTime

---

## 🔐 Security Checklist:

- [x] JWT stored in HTTP-Only cookie (not accessible from JavaScript)
- [x] JWT expires after 24 hours
- [x] Password hashed with BCrypt
- [x] CSRF disabled (stateless JWT)
- [x] Session stateless (no server session)
- [x] Logout clears all cookies
- [x] Invalid JWT tokens ignored (no crash)
- [x] Error handling prevents information leakage

---

**Liên hệ nếu vẫn gặp vấn đề!**
