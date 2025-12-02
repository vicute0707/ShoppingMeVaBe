# Hướng Dẫn Thanh Toán MoMo với Ngrok

## Tổng quan

Dự án của bạn đã có **đầy đủ chức năng thanh toán MoMo**! Bạn chỉ cần cấu hình ngrok để nhận callback từ MoMo.

## Các thành phần đã có

### 1. Backend
- ✅ `MoMoService` - Tạo payment request và verify signature
- ✅ `MoMoConfig` - Cấu hình MoMo
- ✅ `PaymentController` - Xử lý callback và IPN
- ✅ DTOs: MoMoPaymentRequest, MoMoPaymentResponse, MoMoCallbackResponse

### 2. Frontend
- ✅ Nút "Thanh toán MoMo" trong trang chi tiết đơn hàng
- ✅ Hiển thị trạng thái thanh toán
- ✅ Hiển thị mã giao dịch

### 3. Cấu hình
```properties
# Test Environment MoMo
momo.endpoint=https://test-payment.momo.vn/v2/gateway/api/create
momo.partner-code=MOMO
momo.access-key=F8BBA842ECF85
momo.secret-key=K951B6PE1waDMi640xX08PD3vg6EkVlz
momo.redirect-url=${app.base-url}/payment/momo/callback
momo.ipn-url=${app.base-url}/payment/momo/ipn
momo.request-type=captureWallet
```

## Bước 1: Cài đặt Ngrok (Trên máy local của bạn)

### Windows
```bash
# Download tại: https://ngrok.com/download
# Hoặc dùng Chocolatey:
choco install ngrok
```

### MacOS
```bash
brew install ngrok/ngrok/ngrok
```

### Linux
```bash
curl -s https://ngrok-agent.s3.amazonaws.com/ngrok.asc | \
  sudo tee /etc/apt/trusted.gpg.d/ngrok.asc >/dev/null && \
  echo "deb https://ngrok-agent.s3.amazonaws.com buster main" | \
  sudo tee /etc/apt/sources.list.d/ngrok.list && \
  sudo apt update && sudo apt install ngrok
```

## Bước 2: Đăng ký tài khoản Ngrok

1. Truy cập: https://dashboard.ngrok.com/signup
2. Đăng ký tài khoản miễn phí
3. Lấy authtoken từ: https://dashboard.ngrok.com/get-started/your-authtoken
4. Cấu hình authtoken:
```bash
ngrok config add-authtoken YOUR_AUTH_TOKEN
```

## Bước 3: Chạy ứng dụng Spring Boot

```bash
# Ở thư mục gốc của dự án
./mvnw spring-boot:run
```

Ứng dụng sẽ chạy ở port **8081**

## Bước 4: Chạy Ngrok

Mở terminal mới và chạy:

```bash
ngrok http 8081
```

Bạn sẽ thấy output như:

```
ngrok

Session Status                online
Account                       your@email.com (Plan: Free)
Version                       3.x.x
Region                        United States (us)
Latency                       -
Web Interface                 http://127.0.0.1:4040
Forwarding                    https://abc123.ngrok-free.app -> http://localhost:8081

Connections                   ttl     opn     rt1     rt5     p50     p90
                              0       0       0.00    0.00    0.00    0.00
```

**Lưu lại URL**: `https://abc123.ngrok-free.app`

## Bước 5: Cập nhật Ngrok URL vào Application Properties

Mở file `src/main/resources/application.properties` và cập nhật:

```properties
# Thay YOUR_NGROK_URL bằng URL từ Bước 4
app.base-url=https://abc123.ngrok-free.app
```

**LƯU Ý**: URL của bạn hiện tại là:
```
app.base-url=https://presophomore-adjunctly-margery.ngrok-free.dev
```

Nếu URL này vẫn còn hoạt động, bạn không cần thay đổi gì!

## Bước 6: Khởi động lại ứng dụng

```bash
# Dừng ứng dụng (Ctrl+C)
# Chạy lại
./mvnw spring-boot:run
```

## Bước 7: Test Flow Thanh Toán

### 7.1. Đăng nhập vào hệ thống
- Truy cập: http://localhost:8081
- Đăng nhập với tài khoản customer

### 7.2. Tạo đơn hàng
1. Thêm sản phẩm vào giỏ hàng
2. Vào giỏ hàng: http://localhost:8081/cart
3. Click "Thanh toán"
4. Nhập thông tin giao hàng
5. Chọn phương thức: **COD** (để tạo đơn hàng)
6. Click "Đặt hàng"

### 7.3. Thanh toán MoMo
1. Vào "Đơn hàng của tôi": http://localhost:8081/checkout/orders
2. Click vào đơn hàng vừa tạo
3. Click nút **"Thanh toán MoMo"**
4. Bạn sẽ được redirect đến trang thanh toán MoMo

### 7.4. Test với MoMo Test Environment

**Thông tin test MoMo**:
- Card Number: `9704 0000 0000 0018`
- Card Holder: `NGUYEN VAN A`
- Issue Date: `03/07`
- OTP: `OTP` (nhập chữ "OTP")

Hoặc sử dụng app MoMo test để quét QR code.

## Bước 8: Kiểm tra Callback

### 8.1. Xem log của ứng dụng

Khi thanh toán thành công, bạn sẽ thấy log:

```
INFO  - MoMo callback received - OrderId: MOMOBE123_1234567890, ResultCode: 0, Message: Successful
INFO  - Payment successful for order #123
INFO  - Order #123 updated successfully via IPN
```

### 8.2. Kiểm tra trạng thái đơn hàng

- Trạng thái đơn hàng: **PROCESSING**
- Trạng thái thanh toán: **PAID**
- Phương thức: **MOMO**
- Có mã giao dịch

## Bước 9: Xem Web Interface của Ngrok

Truy cập: http://127.0.0.1:4040

Tại đây bạn có thể:
- Xem tất cả requests đến ứng dụng
- Xem callback từ MoMo
- Debug requests/responses
- Replay requests

## Các Endpoint MoMo

### 1. Tạo thanh toán
```
GET /payment/momo/create/{orderId}
```

### 2. Callback (Redirect URL)
```
GET /payment/momo/callback
```
MoMo sẽ redirect user về đây sau khi thanh toán.

### 3. IPN (Instant Payment Notification)
```
POST /payment/momo/ipn
```
MoMo server sẽ gửi notification đến đây để cập nhật trạng thái.

## Flow Hoàn Chỉnh

```
1. User clicks "Thanh toán MoMo"
   ↓
2. App gọi MoMoService.createPayment()
   ↓
3. App gửi request đến MoMo API
   ↓
4. MoMo trả về payUrl
   ↓
5. User được redirect đến MoMo payment page
   ↓
6. User thanh toán trên MoMo
   ↓
7. MoMo redirect về: {ngrok-url}/payment/momo/callback
   ↓
8. App verify signature và cập nhật order status
   ↓
9. MoMo gửi IPN đến: {ngrok-url}/payment/momo/ipn
   ↓
10. App verify và confirm đã nhận được
```

## Security Configuration

File `SecurityConfig.java` đã cho phép truy cập public đến:
```java
.requestMatchers("/payment/**").permitAll()
```

Điều này cần thiết để MoMo có thể gọi callback và IPN.

## Troubleshooting

### Lỗi: Signature không hợp lệ

**Nguyên nhân**: Raw signature string sai thứ tự
**Giải pháp**: Đã được xử lý trong `MoMoService.verifyCallback()`

### Lỗi: Cannot extract order ID

**Nguyên nhân**: Format order ID không đúng
**Giải pháp**: Order ID format: `MOMOBE{orderId}_{timestamp}`

### Lỗi: MoMo không gọi IPN

**Nguyên nhân**:
- Ngrok URL không hoạt động
- IPN URL không public
- MoMo test environment delay

**Giải pháp**:
- Kiểm tra ngrok đang chạy
- Kiểm tra app.base-url trong application.properties
- Đợi vài phút

### Lỗi: 403 Forbidden từ MoMo callback

**Nguyên nhân**: Spring Security block request

**Giải pháp**: Đã cấu hình permitAll() cho /payment/**

## Script Tự Động

Tôi đã tạo các script để giúp bạn:

### `start-with-ngrok.sh` (Linux/Mac)
```bash
#!/bin/bash
./start-with-ngrok.sh
```

### `start-with-ngrok.bat` (Windows)
```bash
start-with-ngrok.bat
```

## Tài liệu MoMo

- API Documentation: https://developers.momo.vn/
- Test Environment: https://test-payment.momo.vn/
- Support: developer@momo.vn

## Kết luận

Hệ thống thanh toán MoMo của bạn đã sẵn sàng! Chỉ cần:

1. ✅ Chạy ngrok
2. ✅ Cập nhật URL vào application.properties
3. ✅ Test thanh toán

**Lưu ý quan trọng**:
- URL ngrok miễn phí sẽ thay đổi mỗi lần restart
- Nếu dùng cho production, cần domain cố định
- Test environment của MoMo có giới hạn rate limit

Chúc bạn thành công! 🎉
