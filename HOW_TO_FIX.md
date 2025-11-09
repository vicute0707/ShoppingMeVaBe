# 🚑 FIX LỖI 500 - HƯỚNG DẪN 4 BƯỚC

## ❌ LỖI BẠN ĐANG GẶP:
```
Cannot convert [2025-11-10T...] of type [class java.time.LocalDateTime] to [class java.util.Date]
```

---

## ✅ GIẢI PHÁP - LÀM ĐÚNG 4 BƯỚC SAU:

### **BƯỚC 1: XÓA COOKIES CŨ**

Mở browser và truy cập:
```
http://localhost:8081/clear-cookies
```

➡️ Thấy message "Đã xóa tất cả cookies!"

---

### **BƯỚC 2: PULL CODE MỚI**

Mở PowerShell tại `D:\ShoppingMomadnBaby`:

```powershell
git pull origin claude/review-main-config-011CUxyBgNZTmd7i2rWzwxzr
```

---

### **BƯỚC 3: RESTART ỨNG DỤNG**

```powershell
# Stop app: Ctrl + C

# Restart:
.\mvnw.cmd clean spring-boot:run
```

Chờ đến khi thấy:
```
✅ Default Admin Account Created!
```

---

### **BƯỚC 4: ĐĂNG NHẬP**

Truy cập: **http://localhost:8081/login**

Đăng nhập:
```
Email: admin@shopmevabe.com
Password: admin123
```

---

## ✅ KẾT QUẢ:

- ✅ Đăng nhập thành công
- ✅ Không còn lỗi 500
- ✅ Header hiển thị email admin
- ✅ Có nút "Đăng xuất"

---

## 🔥 NẾU VẪN LỖI:

### Dùng Incognito Mode:
```
Chrome/Edge: Ctrl + Shift + N
Firefox: Ctrl + Shift + P
```

Trong Incognito, mở: **http://localhost:8081/login**

---

## 📝 GIẢI THÍCH:

**Tại sao lỗi?**
- Browser đang gửi JWT cookie CŨ
- JWT cũ có format SAI
- Server parse JWT cũ → Crash → 500

**Tại sao fix được?**
1. `/clear-cookies` xóa JWT cũ
2. Pull code mới → JwtFilter KHÔNG BAO GIỜ crash
3. Restart → Load code mới
4. Login → Tạo JWT mới (format đúng)

---

**LÀM 4 BƯỚC TRÊN LÀ CHẮC CHẮN ĐƯỢC!** 🚀
