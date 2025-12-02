# MoMo Payment - Quick Start Guide

## Tóm tắt

Dự án đã có **đầy đủ chức năng thanh toán MoMo**. Bạn chỉ cần chạy ngrok!

## Quick Start (3 bước)

### 1. Cài đặt Ngrok

**Windows**: Tải tại https://ngrok.com/download

**Mac**:
```bash
brew install ngrok/ngrok/ngrok
```

**Linux**:
```bash
curl -s https://ngrok-agent.s3.amazonaws.com/ngrok.asc | \
  sudo tee /etc/apt/trusted.gpg.d/ngrok.asc >/dev/null && \
  echo "deb https://ngrok-agent.s3.amazonaws.com buster main" | \
  sudo tee /etc/apt/sources.list.d/ngrok.list && \
  sudo apt update && sudo apt install ngrok
```

### 2. Cấu hình Ngrok

```bash
# Đăng ký tại: https://dashboard.ngrok.com/signup
# Lấy authtoken từ: https://dashboard.ngrok.com/get-started/your-authtoken

ngrok config add-authtoken YOUR_AUTH_TOKEN
```

### 3. Chạy ứng dụng

**Tự động** (Khuyến nghị):
```bash
# Linux/Mac
./start-with-ngrok.sh

# Windows
start-with-ngrok.bat
```

**Thủ công**:

Terminal 1 - Chạy ứng dụng:
```bash
./mvnw spring-boot:run
```

Terminal 2 - Chạy ngrok:
```bash
ngrok http 8081
```

Terminal 3 - Cập nhật config:
```bash
# Lấy URL từ ngrok (vd: https://abc123.ngrok-free.app)
# Cập nhật trong src/main/resources/application.properties:
app.base-url=https://abc123.ngrok-free.app
```

## Test Payment

### Cách 1: Dùng script test
```bash
./test-momo-payment.sh
```

### Cách 2: Test thủ công

1. **Đăng nhập**: http://localhost:8081
   - Admin: admin@shopmevabe.com / admin123

2. **Tạo đơn hàng**:
   - Thêm sản phẩm vào giỏ
   - Checkout với COD

3. **Thanh toán MoMo**:
   - Vào "Đơn hàng của tôi"
   - Click "Thanh toán MoMo"

4. **Test thông tin MoMo**:
   - Card: `9704 0000 0000 0018`
   - Name: `NGUYEN VAN A`
   - Date: `03/07`
   - OTP: `OTP` (gõ chữ "OTP")

## URLs quan trọng

- 🌐 Application: http://localhost:8081
- 📊 Ngrok Dashboard: http://localhost:4040
- 📝 API Docs: http://localhost:8081/swagger-ui.html
- 📧 MailHog (if used): http://localhost:8025

## MoMo Endpoints

```
GET  /payment/momo/create/{orderId}  - Tạo payment
GET  /payment/momo/callback           - Callback redirect
POST /payment/momo/ipn                - IPN notification
```

## Troubleshooting

### Ngrok URL thay đổi mỗi lần restart?

**Giải pháp 1**: Dùng script tự động `start-with-ngrok.sh`

**Giải pháp 2**: Nâng cấp ngrok lên plan có static domain

### MoMo không gọi callback?

**Kiểm tra**:
1. Ngrok đang chạy: http://localhost:4040
2. URL đúng trong application.properties
3. Ứng dụng đang chạy

### Lỗi signature không hợp lệ?

**Kiểm tra**: Log của ứng dụng để xem raw signature

## Xem log chi tiết

```bash
# Xem log realtime
tail -f logs/spring-boot-logger.log

# Hoặc xem trong Ngrok dashboard
# http://localhost:4040
```

## Production Checklist

Khi deploy lên production:

- [ ] Đổi sang MoMo production endpoint
- [ ] Lấy production credentials từ MoMo
- [ ] Dùng domain cố định (không dùng ngrok)
- [ ] Bật HTTPS
- [ ] Cấu hình firewall cho IPN
- [ ] Enable audit logging
- [ ] Setup monitoring

## Tài liệu đầy đủ

Xem: `HUONG_DAN_THANH_TOAN_MOMO.md`

## Hỗ trợ

- MoMo Developer: https://developers.momo.vn/
- MoMo Support: developer@momo.vn

---

**Chúc bạn thành công!** 🎉
