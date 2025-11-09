# Hướng dẫn khắc phục lỗi Lombok trong IntelliJ IDEA

## Lỗi: "cannot find symbol: method getEmail(), getPassword(), builder(), etc."

Các lỗi này xảy ra khi Lombok chưa được enable trong IDE. Lombok sẽ tự động generate các methods như getters, setters, builder trong compile time.

## Cách khắc phục trong IntelliJ IDEA:

### Bước 1: Cài đặt Lombok Plugin
1. Mở **File → Settings** (hoặc **Ctrl + Alt + S**)
2. Chọn **Plugins**
3. Tìm kiếm "**Lombok**"
4. Click **Install** (nếu chưa cài)
5. Restart IntelliJ IDEA

### Bước 2: Enable Annotation Processing
1. Mở **File → Settings** (hoặc **Ctrl + Alt + S**)
2. Tìm kiếm "**Annotation Processors**" hoặc đi đến:
   - **Build, Execution, Deployment → Compiler → Annotation Processors**
3. Check vào "**Enable annotation processing**"
4. Click **Apply** và **OK**

### Bước 3: Clean và Rebuild Project
1. Chọn **Build → Clean Project**
2. Sau đó chọn **Build → Rebuild Project**

### Bước 4: Invalidate Caches (nếu vẫn lỗi)
1. Chọn **File → Invalidate Caches...**
2. Check vào "**Clear file system cache and Local History**"
3. Click **Invalidate and Restart**

## Kiểm tra Lombok đang hoạt động:

Sau khi làm các bước trên, mở file `User.java` hoặc `RegisterDTO.java`. IntelliJ sẽ không còn highlight lỗi cho các method như:
- `getEmail()`, `setEmail()`
- `getPassword()`, `setPassword()`
- `User.builder()`
- v.v.

## Compile bằng Maven (nếu vẫn gặp lỗi trong IDE):

Bạn có thể compile và chạy project trực tiếp bằng Maven mà không cần IDE:

```bash
# Clean và compile
mvn clean compile

# Hoặc run project
mvn spring-boot:run
```

Maven sẽ tự động process Lombok annotations và generate code cần thiết.

## Lỗi Deprecated trong SecurityConfig

Lỗi deprecated đã được fix trong commit mới nhất:
- `DaoAuthenticationProvider()` → `DaoAuthenticationProvider(passwordEncoder)`
- Code đã được update để sử dụng constructor mới thay vì deprecated methods

## Lỗi duplicate method trong CartRestController

Lỗi này đã được fix:
- Private helper method `getCart()` đã đổi tên thành `getOrCreateCart()`
- Public REST endpoint `getCart()` giữ nguyên tên
- Không còn conflict giữa 2 methods
