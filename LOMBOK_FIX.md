# Hướng dẫn khắc phục lỗi Lombok trong IntelliJ IDEA

## Lỗi: "cannot find symbol: method getEmail(), getPassword(), builder(), etc."

Các lỗi này xảy ra khi Lombok chưa được enable trong IDE. Lombok sẽ tự động generate các methods như getters, setters, builder trong compile time.

## Cách khắc phục nhanh nhất - Enable Annotation Processing:

### Bước 1: Enable Annotation Processing
1. Mở **File → Settings** (hoặc **Ctrl + Alt + S**)
2. Tìm kiếm "**Annotation Processors**" hoặc đi đến:
   - **Build, Execution, Deployment → Compiler → Annotation Processors**
3. Check vào "**Enable annotation processing**"
4. Click **Apply** và **OK**

### Bước 2: Clean và Rebuild Project
1. Chọn **Build → Clean Project**
2. Sau đó chọn **Build → Rebuild Project**

### Bước 3: Invalidate Caches (nếu vẫn lỗi)
1. Chọn **File → Invalidate Caches...**
2. Check vào "**Clear file system cache and Local History**"
3. Click **Invalidate and Restart**

## ✅ Giải pháp tốt nhất: Compile và Run bằng Maven

Bạn có thể bỏ qua tất cả lỗi IDE và compile/run project trực tiếp bằng Maven:

```bash
# Clean và compile
mvn clean compile

# Run application
mvn spring-boot:run
```

**Lợi ích:**
- Không cần config gì thêm trong IDE
- Maven tự động process Lombok annotations
- Code sẽ compile và run hoàn toàn bình thường
- Truy cập application tại: http://localhost:8080
- Swagger UI tại: http://localhost:8080/swagger-ui.html

## Kiểm tra Lombok đang hoạt động:

Sau khi enable annotation processing, IntelliJ sẽ không còn highlight lỗi cho các method như:
- `getEmail()`, `setEmail()`
- `getPassword()`, `setPassword()`
- `User.builder()`
- v.v.

## Lỗi Deprecated trong SecurityConfig ✅ ĐÃ FIX

Lỗi deprecated đã được fix:
- `DaoAuthenticationProvider()` → `DaoAuthenticationProvider(passwordEncoder)`
- Code đã được update để sử dụng constructor mới thay vì deprecated methods

## Lỗi duplicate method trong CartRestController ✅ ĐÃ FIX

Lỗi này đã được fix:
- Private helper method `getCart()` đã đổi tên thành `getOrCreateCart()`
- Public REST endpoint `getCart()` giữ nguyên tên
- Không còn conflict giữa 2 methods

## Tóm tắt

**Nếu IDE báo lỗi Lombok:** Chỉ cần enable annotation processing (Bước 1-3)

**Nếu muốn chạy ngay:** Dùng lệnh `mvn spring-boot:run` - không cần làm gì thêm!
