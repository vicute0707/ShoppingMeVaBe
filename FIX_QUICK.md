# ⚠️ FIX LỖI 500 - HƯỚNG DẪN NHANH

## ❌ Lỗi hiện tại:
```
Cannot convert [2025-11-10T05:34:22.169149] of type
[class java.time.LocalDateTime] to [class java.util.Date]
```

## 🔥 GIẢI PHÁP - Làm theo THỨ TỰ:

### **Bước 1: XÓA TẤT CẢ COOKIES (QUAN TRỌNG NHẤT!)**

Truy cập URL sau để xóa cookies:
```
http://localhost:8081/clear-cookies
```

Hoặc xóa thủ công:
```
1. Nhấn F12 (Developer Tools)
2. Tab "Application"
3. Storage → Cookies → http://localhost:8081
4. Right-click → "Clear"
5. Đóng browser và mở lại
```

---

### **Bước 2: PULL CODE MỚI**

```powershell
# Trong PowerShell tại D:\ShoppingMomadnBaby

git pull origin claude/review-main-config-011CUxyBgNZTmd7i2rWzwxzr
```

---

### **Bước 3: RESTART ỨNG DỤNG**

```powershell
# Stop app hiện tại: Ctrl + C

# Clean và restart
.\mvnw.cmd clean spring-boot:run
```

Chờ đến khi thấy:
```
✅ Default Admin Account Created!
📧 Email: admin@shopmevabe.com
🔑 Password: admin123
```

---

### **Bước 4: ĐĂNG NHẬP**

**Truy cập:** http://localhost:8081/login

**Tài khoản Admin:**
```
Email: admin@shopmevabe.com
Password: admin123
```

---

## ✅ KẾT QUẢ MONG ĐỢI:

✅ Đăng nhập thành công
✅ Redirect → /admin/dashboard
✅ Header hiển thị: "👤 admin@shopmevabe.com ▼"
✅ Có nút "Đăng xuất"
✅ Không còn lỗi 500

---

## 🆘 NẾU VẪN LỖI:

### **Option 1: Dùng Incognito Mode**
```
Chrome/Edge: Ctrl + Shift + N
Firefox: Ctrl + Shift + P
```

### **Option 2: Dùng browser khác**
```
Đang dùng Chrome → Thử Edge
Đang dùng Edge → Thử Firefox
```

### **Option 3: Check code đã pull chưa**
```powershell
git log --oneline -1
```
Phải thấy: `681b93a 🔒 Fix authentication - Better logout & JWT error handling`

---

## 📝 GIẢI THÍCH:

**Tại sao lỗi?**
- Browser đang cache JWT cookie CŨ
- JWT cũ có format SAI (authorities là objects, không phải strings)
- Khi parse JWT cũ → gặp LocalDateTime → Lỗi 500

**Tại sao phải xóa cookies?**
- Code đã fix NHƯNG JWT cookie cũ vẫn còn
- Browser tự động gửi JWT cũ với mọi request
- Server parse JWT cũ → Crash

**Tại sao /clear-cookies hoạt động?**
- Endpoint mới để FORCE xóa TẤT CẢ cookies
- Set maxAge = 0 cho tất cả cookies
- Browser phải xóa cookies ngay lập tức

---

## 🔐 ĐÃ FIX:

1. **JwtUtil.java** - authorities → List<String>
2. **AuthController.java** - Thêm /clear-cookies endpoint
3. **JwtAuthenticationFilter.java** - Better error handling
4. **SecurityConfig.java** - Allow /clear-cookies

---

**Làm theo 4 bước trên là CHẮC CHẮN được!** 🚀
