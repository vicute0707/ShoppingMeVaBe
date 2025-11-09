# 💰 Hướng Dẫn Tích Hợp MoMo Payment - Cửa Hàng Mẹ và Bé

## 🎯 Tổng Quan

Hệ thống thanh toán MoMo đã được tích hợp đầy đủ với các tính năng:

- ✅ Tạo thanh toán qua MoMo API
- ✅ Xử lý callback từ MoMo
- ✅ Nhận IPN (Instant Payment Notification)
- ✅ Xác thực chữ ký HMAC SHA256
- ✅ Cập nhật trạng thái đơn hàng tự động
- ✅ Giao diện đáng yêu cho mẹ và bé 🍼👶

---

## 📁 Cấu Trúc Files

```
src/main/java/iuh/student/www/
├── config/
│   └── MoMoConfig.java                 # Cấu hình MoMo
├── dto/
│   ├── MoMoPaymentRequest.java         # DTO request
│   ├── MoMoPaymentResponse.java        # DTO response
│   └── MoMoCallbackResponse.java       # DTO callback
├── service/
│   └── MoMoService.java                # Service xử lý logic MoMo
└── controller/
    └── PaymentController.java          # Controller xử lý endpoints

src/main/resources/
├── application.properties              # Config development
└── application-prod.properties         # Config production

src/main/webapp/WEB-INF/views/
└── payment/
    └── momo-button.jsp                 # Template nút thanh toán
```

---

## 🔧 Cấu Hình

### 1. Application Properties

**Development (application.properties):**
```properties
spring.application.name=ShopMeVaBeCute
app.name=Cửa Hàng Mẹ và Bé
```

**Production (application-prod.properties):**
```properties
# MoMo Configuration
momo.endpoint=https://test-payment.momo.vn/v2/gateway/api/create
momo.partner-code=MOMO
momo.access-key=F8BBA842ECF85
momo.secret-key=K951B6PE1waDMi640xX08PD3vg6EkVlz
momo.redirect-url=${app.base-url}/payment/momo/callback
momo.ipn-url=${app.base-url}/payment/momo/ipn
momo.request-type=captureWallet

# App Base URL (cập nhật với ngrok URL)
app.base-url=https://your-ngrok-url.ngrok.io
```

### 2. Database Schema Updates

Bảng `orders` đã được cập nhật với các trường:

```sql
ALTER TABLE orders
ADD COLUMN payment_method VARCHAR(50),
ADD COLUMN payment_status VARCHAR(20),
ADD COLUMN transaction_id VARCHAR(100);
```

---

## 🚀 API Endpoints

### 1. Tạo Thanh Toán MoMo

**Endpoint:** `GET /payment/momo/create/{orderId}`

**Description:** Tạo request thanh toán và redirect user đến MoMo

**Example:**
```
GET http://localhost:8080/payment/momo/create/123
→ Redirect to MoMo payment page
```

### 2. MoMo Callback

**Endpoint:** `GET /payment/momo/callback`

**Description:** Nhận kết quả thanh toán từ MoMo (user redirect)

**Parameters:**
- partnerCode
- orderId
- requestId
- amount
- resultCode
- message
- signature
- ...

**Example:**
```
GET /payment/momo/callback?partnerCode=MOMO&orderId=MOMOBE123_1234567890&resultCode=0&...
→ Cập nhật đơn hàng → Redirect to order detail
```

### 3. MoMo IPN

**Endpoint:** `POST /payment/momo/ipn`

**Description:** Nhận notification từ MoMo server

**Request Body:**
```json
{
  "partnerCode": "MOMO",
  "orderId": "MOMOBE123_1234567890",
  "requestId": "uuid",
  "amount": 100000,
  "resultCode": 0,
  "message": "Success",
  "signature": "...",
  ...
}
```

**Response:**
```json
{
  "status": "success"
}
```

---

## 💻 Sử Dụng Trong Code

### 1. Trong Controller

```java
@Autowired
private MoMoService moMoService;

@GetMapping("/checkout/{orderId}")
public String checkout(@PathVariable Long orderId, Model model) {
    Order order = orderService.findById(orderId).orElseThrow();
    model.addAttribute("order", order);
    return "order/detail";
}
```

### 2. Trong JSP View

```jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="order-detail">
    <!-- Your order details here -->

    <!-- Include MoMo payment button -->
    <jsp:include page="/WEB-INF/views/payment/momo-button.jsp"/>
</div>
```

### 3. Programmatic Payment Creation

```java
@Service
public class CheckoutService {

    @Autowired
    private MoMoService moMoService;

    public String createPayment(Order order) {
        MoMoPaymentResponse response = moMoService.createPayment(order);

        if (response.getResultCode() == 0) {
            // Success - redirect to payment URL
            return response.getPayUrl();
        } else {
            // Failed
            throw new PaymentException(response.getMessage());
        }
    }
}
```

---

## 🔐 Security - Signature Verification

### Signature Generation

```java
String rawSignature = "accessKey=" + accessKey +
    "&amount=" + amount +
    "&extraData=" +
    "&ipnUrl=" + ipnUrl +
    "&orderId=" + orderId +
    "&orderInfo=" + orderInfo +
    "&partnerCode=" + partnerCode +
    "&redirectUrl=" + redirectUrl +
    "&requestId=" + requestId +
    "&requestType=" + requestType;

String signature = generateHmacSHA256(rawSignature, secretKey);
```

### Signature Verification

```java
boolean isValid = moMoService.verifyCallback(callbackResponse);
if (!isValid) {
    throw new SecurityException("Invalid signature");
}
```

---

## 🧪 Testing

### 1. Local Testing với Ngrok

```bash
# Terminal 1: Start ngrok
ngrok http 8080

# Terminal 2: Update config và start app
# Update app.base-url in application-prod.properties
./start-prod.sh

# Terminal 3: Test
curl http://localhost:8080/payment/momo/create/1
```

### 2. Test MoMo Payment Flow

1. **Tạo đơn hàng:**
   - Login với user account
   - Thêm sản phẩm vào giỏ
   - Checkout → Order created (status: PENDING)

2. **Thanh toán:**
   - Nhấn nút "Thanh toán MoMo"
   - Scan QR code hoặc mở MoMo app
   - Xác nhận thanh toán

3. **Verify:**
   - Check callback logs
   - Verify order status updated to PROCESSING
   - Verify paymentStatus = PAID
   - Verify transactionId saved

### 3. Test Accounts (MoMo Test Environment)

- **Phone:** Bất kỳ số điện thoại nào
- **OTP:** Bất kỳ 6 số nào

---

## 📊 Payment Flow Diagram

```
User                    App                     MoMo
 |                       |                        |
 |-- Click "Pay" ------->|                        |
 |                       |-- Create Payment ----->|
 |                       |<----- PayUrl ----------|
 |<----- Redirect -------|                        |
 |                       |                        |
 |---------- Scan QR Code / Pay --------------->|
 |                       |                        |
 |<----------- Redirect (Callback) -------------|
 |-- Callback URL ------>|                        |
 |                       |-- Verify Signature     |
 |                       |-- Update Order         |
 |<-- Order Detail ------|                        |
 |                       |                        |
 |                       |<----- IPN (Async) -----|
 |                       |-- Verify & Update      |
 |                       |------- Success ------->|
```

---

## 🐛 Troubleshooting

### Lỗi: Invalid Signature

**Nguyên nhân:**
- Sai secret key
- Raw signature không đúng format
- Encoding không đúng

**Giải pháp:**
```java
// Enable debug logs
logging.level.iuh.student.www.service.MoMoService=DEBUG

// Check logs for:
// - Raw signature string
// - Generated signature
// - Received signature
```

### Lỗi: Ngrok URL không hoạt động

**Nguyên nhân:** Ngrok URL thay đổi mỗi khi restart

**Giải pháp:**
1. Copy ngrok URL mới
2. Update `app.base-url` trong `application-prod.properties`
3. Restart application

### Lỗi: Order not found trong callback

**Nguyên nhân:** Format orderId không đúng

**Giải pháp:**
```java
// MoMo OrderID format: MOMOBE{orderId}_{timestamp}
// Example: MOMOBE123_1699876543210
// Extract: 123

Long orderId = moMoService.extractOrderId(momoOrderId);
```

---

## 📈 Production Deployment

### Checklist

- [ ] Update MoMo credentials với production keys
- [ ] Cấu hình domain name (thay vì ngrok)
- [ ] Enable HTTPS/SSL
- [ ] Setup monitoring và logging
- [ ] Configure rate limiting
- [ ] Test IPN endpoint
- [ ] Backup database trước khi deploy
- [ ] Test rollback procedure

### Production URLs

```properties
# Production MoMo endpoint
momo.endpoint=https://payment.momo.vn/v2/gateway/api/create

# Your production domain
app.base-url=https://shopmevabeute.com
```

---

## 📞 Support

### MoMo Documentation
- **API Docs:** https://developers.momo.vn
- **Support:** https://business.momo.vn/support

### Project Contact
- **Email:** contact@shopmevabeute.com
- **Phone:** 0123456789

---

## 🎉 Kết Luận

Hệ thống thanh toán MoMo đã được tích hợp hoàn chỉnh với:
- ✅ Full payment flow
- ✅ Security với signature verification
- ✅ Error handling
- ✅ Beautiful UI
- ✅ Production ready

**Chúc bạn kinh doanh thành công với Cửa Hàng Mẹ và Bé!** 🍼👶💕

---

*Last updated: $(date)*
