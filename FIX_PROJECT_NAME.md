# 🔧 Hướng dẫn sửa tên project (Optional)

## ⚠️ Vấn đề phát hiện

Trong `pom.xml` line 14, tên project là: **`ShoppingMomadnBaby`**

Có vẻ như có typo: `Momadnbaby` thay vì `MomandBaby`

## 📝 Cần sửa ở đâu?

### 1. File `pom.xml` (line 14)

**Hiện tại:**
```xml
<name>ShoppingMomadnBaby</name>
```

**Nên sửa thành:**
```xml
<name>ShoppingMomAndBaby</name>
```

### 2. File Main Application

**File:** `src/main/java/iuh/student/www/ShoppingMomadnBabyApplication.java`

**Nên đổi tên thành:**
- Class name: `ShoppingMomAndBabyApplication`
- File name: `ShoppingMomAndBabyApplication.java`

## 🤔 CÓ NÊN SỬA KHÔNG?

### ✅ LÝ DO NÊN SỬA:
- Tên rõ ràng hơn: "Mom and Baby" dễ hiểu hơn "Momadnbaby"
- Chuyên nghiệp hơn
- Tránh nhầm lẫn trong tương lai

### ❌ LÝ DO KHÔNG SỬA:
- Nếu sửa phải refactor nhiều file
- Có thể ảnh hưởng đến Git history
- Nếu đã deploy production không nên đổi

## 🚀 CÁCH SỬA NHANH (NẾU MUỐN)

### Bước 1: Sửa pom.xml

```bash
# Mở file pom.xml và sửa dòng 14
vim pom.xml
# hoặc
nano pom.xml
```

Thay đổi:
```xml
<name>ShoppingMomadnBaby</name>
```

Thành:
```xml
<name>ShoppingMomAndBaby</name>
```

### Bước 2: Đổi tên class (Optional - khuyến nghị)

```bash
# Di chuyển về thư mục chứa file
cd src/main/java/iuh/student/www/

# Đổi tên file
mv ShoppingMomadnBabyApplication.java ShoppingMomAndBabyApplication.java

# Mở và sửa tên class bên trong
nano ShoppingMomAndBabyApplication.java
```

Sửa trong file:
```java
// Trước:
public class ShoppingMomadnBabyApplication {
    public static void main(String[] args) {
        SpringApplication.run(ShoppingMomadnBabyApplication.class, args);
    }
}

// Sau:
public class ShoppingMomAndBabyApplication {
    public static void main(String[] args) {
        SpringApplication.run(ShoppingMomAndBabyApplication.class, args);
    }
}
```

### Bước 3: Rebuild project

```bash
cd /home/user/ShoppingMeVaBe
./mvnw clean package -DskipTests
```

## 💡 KHUYẾN NGHỊ

**KHÔNG SỬA** nếu:
- Dự án đã chạy ổn định
- Đã có production deployment
- Đang trong rush deadline

**NÊN SỬA** nếu:
- Dự án mới bắt đầu
- Chưa deploy production
- Muốn code base sạch đẹp hơn

---

**Lưu ý:** Tên này CHỈ ẢNH HƯỞNG đến metadata của Maven project, KHÔNG ảnh hưởng đến chức năng ứng dụng. Ứng dụng vẫn chạy bình thường dù có sửa hay không.

Tên hiển thị trên website được cấu hình trong `application.properties`:
```properties
app.name=Cửa Hàng Mẹ và Bé
app.name.english=Shop Baby & Mom Cute
```
