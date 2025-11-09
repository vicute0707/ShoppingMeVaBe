# 🍼👶 Hướng Dẫn Cấu Hình Production - Cửa Hàng Mẹ và Bé

## 📝 Tổng Quan

Đây là hướng dẫn chi tiết để triển khai ứng dụng **Cửa Hàng Mẹ và Bé** lên production environment với:
- ✅ Tích hợp thanh toán **MoMo Payment Gateway**
- ✅ Kết nối **MySQL Database**
- ✅ Hỗ trợ test với **Ngrok**
- ✅ Thông tin cửa hàng đáng yêu cho mẹ và bé

---

## 🚀 Các Bước Cài Đặt

### 1. Cài Đặt MySQL Database

```bash
# Tạo database
mysql -u root -p
CREATE DATABASE shop_me_va_be_cute CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
exit;
```

### 2. Cấu Hình Application Properties

File cấu hình production đã được tạo tại: `src/main/resources/application-prod.properties`

**Lưu ý:** Cần cập nhật các giá trị sau:

```properties
# Database password (nếu có)
spring.datasource.password=YOUR_MYSQL_PASSWORD

# Ngrok URL (sau khi chạy ngrok)
app.base-url=https://your-ngrok-url.ngrok.io
```

### 3. Chạy với Ngrok (để test MoMo Payment)

#### Bước 1: Cài đặt Ngrok
```bash
# Download ngrok từ https://ngrok.com/download
# Hoặc sử dụng snap (trên Ubuntu)
sudo snap install ngrok
```

#### Bước 2: Chạy ngrok
```bash
# Expose port 8080
ngrok http 8080
```

#### Bước 3: Copy Forwarding URL
Ngrok sẽ hiển thị URL kiểu: `https://abc123.ngrok.io`

#### Bước 4: Cập nhật vào application-prod.properties
```properties
app.base-url=https://abc123.ngrok.io
```

### 4. Build và Run Application

```bash
# Clean và build project
mvn clean package -DskipTests

# Chạy với profile production
java -jar target/www-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
```

Hoặc chạy trực tiếp từ Maven:
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

---

## 💰 Cấu Hình MoMo Payment

### Thông Tin MoMo Test Environment

```properties
MOMO_ENDPOINT=https://test-payment.momo.vn/v2/gateway/api/create
MOMO_PARTNER_CODE=MOMO
MOMO_ACCESS_KEY=F8BBA842ECF85
MOMO_SECRET_KEY=K951B6PE1waDMi640xX08PD3vg6EkVlz
```

### Flow Thanh Toán

1. **Tạo đơn hàng** → Order status: `PENDING`
2. **Nhấn thanh toán MoMo** → Redirect đến: `/payment/momo/create/{orderId}`
3. **MoMo xử lý** → Hiển thị QR Code hoặc MoMo App
4. **Thanh toán thành công** → MoMo redirect về: `/payment/momo/callback`
5. **Cập nhật đơn hàng** → Order status: `PROCESSING`, Payment status: `PAID`

### Test MoMo Payment

#### Tài khoản test MoMo:
- **Phone:** 9x.xxxx.xxxx (bất kỳ số nào)
- **OTP:** Bất kỳ 6 số nào (trong test environment)

#### API Endpoints:

| Endpoint | Method | Mô tả |
|----------|--------|-------|
| `/payment/momo/create/{orderId}` | GET | Tạo thanh toán MoMo |
| `/payment/momo/callback` | GET | Nhận kết quả từ MoMo |
| `/payment/momo/ipn` | POST | Nhận IPN notification |

---

## 🏪 Thông Tin Cửa Hàng

### Tên cửa hàng
- **Tiếng Việt:** Cửa Hàng Mẹ và Bé
- **Tiếng Anh:** Shop Baby & Mom Cute

### Slogan
*"Chuyên cung cấp sản phẩm chất lượng cho mẹ và bé yêu"* 🍼👶💕

### Thông tin liên hệ (Cập nhật trong config)
```properties
app.name=Cửa Hàng Mẹ và Bé
app.email=contact@shopmevabeute.com
app.phone=0123456789
app.address=123 Đường ABC, Quận XYZ, TP.HCM
```

---

## 🔧 Kiểm Tra Cấu Hình

### 1. Kiểm tra Database Connection
```bash
# Truy cập MySQL
mysql -u root -p shop_me_va_be_cute

# Xem các bảng
SHOW TABLES;
```

### 2. Kiểm tra Application đã chạy
```bash
curl http://localhost:8080/actuator/health
```

### 3. Kiểm tra Ngrok
```bash
curl https://your-ngrok-url.ngrok.io
```

### 4. Test MoMo Payment Flow
```
1. Tạo tài khoản test
2. Thêm sản phẩm vào giỏ hàng
3. Checkout → tạo đơn hàng
4. Nhấn "Thanh toán MoMo"
5. Quét QR Code hoặc mở MoMo App
6. Xác nhận thanh toán
7. Kiểm tra đơn hàng đã cập nhật status
```

---

## 📊 Database Schema

### Bảng Orders (đã cập nhật)

| Column | Type | Description |
|--------|------|-------------|
| id | BIGINT | Primary key |
| user_id | BIGINT | Foreign key to users |
| order_date | DATETIME | Ngày đặt hàng |
| total_amount | DOUBLE | Tổng tiền |
| status | VARCHAR(20) | PENDING, PROCESSING, SHIPPED, DELIVERED, CANCELLED |
| **payment_method** | VARCHAR(50) | MOMO, COD, BANK_TRANSFER |
| **payment_status** | VARCHAR(20) | PENDING, PAID, FAILED |
| **transaction_id** | VARCHAR(100) | MoMo transaction ID |
| shipping_address | VARCHAR(255) | Địa chỉ giao hàng |
| phone | VARCHAR(15) | Số điện thoại |
| notes | VARCHAR(500) | Ghi chú |
| created_at | DATETIME | Thời gian tạo |
| updated_at | DATETIME | Thời gian cập nhật |

---

## 🐛 Troubleshooting

### Lỗi kết nối Database
```
Error: Communications link failure
```
**Giải pháp:**
- Kiểm tra MySQL đã chạy: `systemctl status mysql`
- Kiểm tra username/password
- Kiểm tra database đã tạo

### Lỗi MoMo Payment
```
resultCode: -1
```
**Giải pháp:**
- Kiểm tra MoMo credentials
- Kiểm tra signature generation
- Kiểm tra ngrok URL đã cập nhật

### Lỗi 404 khi callback từ MoMo
**Giải pháp:**
- Kiểm tra ngrok đang chạy
- Cập nhật `app.base-url` trong config
- Restart application sau khi đổi ngrok URL

---

## 📱 Screenshots & Demo

### Payment Flow
```
User → Checkout → Create Order (PENDING)
     → Click "Thanh toán MoMo"
     → Redirect to MoMo
     → Scan QR / Open App
     → Confirm Payment
     → Callback to App
     → Order Updated (PROCESSING, PAID)
```

---

## 🔒 Security Notes

### Production Checklist
- [ ] Thay đổi default admin password
- [ ] Sử dụng HTTPS (SSL certificate)
- [ ] Bảo mật MoMo secret key
- [ ] Validate MoMo signature
- [ ] Rate limiting cho payment APIs
- [ ] Logging và monitoring
- [ ] Backup database định kỳ

---

## 📞 Hỗ Trợ

Nếu gặp vấn đề, vui lòng liên hệ:
- **Email:** contact@shopmevabeute.com
- **Phone:** 0123456789

---

## 🎉 Chúc Mừng!

Bạn đã cấu hình thành công **Cửa Hàng Mẹ và Bé** với MoMo Payment! 🍼👶💕

**Happy Coding!** ✨
