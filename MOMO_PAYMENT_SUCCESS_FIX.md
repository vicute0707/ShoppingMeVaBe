# Fix MoMo Payment Success - Giải quyết lỗi 403 Forbidden

## Vấn đề

Sau khi thanh toán MoMo thành công, người dùng bị chuyển về trang chi tiết đơn hàng (`/checkout/orders/{id}`) nhưng gặp lỗi **403 Forbidden**.

### Nguyên nhân

1. MoMo redirect về URL `/checkout/orders/{id}` sau khi thanh toán
2. URL này yêu cầu authentication (JWT token)
3. User không có JWT token trong request từ MoMo redirect
4. Spring Security block request → 403 Forbidden

## Giải pháp

Tạo **trang Payment Success công khai** để hiển thị kết quả thanh toán, sau đó user có thể đăng nhập để xem chi tiết đơn hàng.

## Các thay đổi

### 1. Tạo trang Payment Success

**File**: `src/main/webapp/WEB-INF/views/payment/success.jsp`

Trang này hiển thị:
- ✅ **Thanh toán thành công**:
  - Icon success với animation
  - Thông tin đơn hàng (ID, số tiền, mã GD)
  - Các bước tiếp theo
  - Nút "Đăng nhập để xem chi tiết"
  - Nút "Về trang chủ"

- ❌ **Thanh toán thất bại**:
  - Icon error
  - Thông báo lỗi
  - Nguyên nhân có thể
  - Nút "Thử lại thanh toán"
  - Nút "Về trang chủ"

### 2. Cập nhật PaymentController

**File**: `src/main/java/iuh/student/www/controller/PaymentController.java`

**Thay đổi trong `momoCallback()`**:

#### Trước:
```java
// Thanh toán thành công
redirectAttributes.addFlashAttribute("success", "Thanh toán thành công!");
return "redirect:/checkout/orders/" + orderIdLong; // ❌ Requires authentication
```

#### Sau:
```java
// Thanh toán thành công
model.addAttribute("success", true);
model.addAttribute("orderId", orderIdLong);
model.addAttribute("amount", amount);
model.addAttribute("transactionId", transId);
return "payment/success"; // ✅ Public page
```

### 3. Cập nhật SecurityConfig

**File**: `src/main/java/iuh/student/www/config/SecurityConfig.java`

**Thay đổi**:

#### Trước:
```java
// MoMo Payment - Public callbacks
.requestMatchers("/payment/momo/callback", "/payment/momo/ipn").permitAll()
```

#### Sau:
```java
// MoMo Payment - Public callbacks and success page
.requestMatchers("/payment/momo/callback", "/payment/momo/ipn", "/payment/success").permitAll()
```

**Thứ tự quan trọng**:
```java
// 1. Public endpoints trước (specific)
.requestMatchers("/payment/momo/callback", "/payment/momo/ipn", "/payment/success").permitAll()

// 2. Protected endpoints sau (general)
.requestMatchers("/payment/**").hasAnyRole("CUSTOMER", "ADMIN")
```

## Flow mới

```
User clicks "Thanh toán MoMo"
  ↓
App creates payment request
  ↓
User redirected to MoMo payment page
  ↓
User pays with MoMo
  ↓
MoMo redirects to: /payment/momo/callback
  ↓
App processes callback & updates order
  ↓
App returns: payment/success.jsp ✅ (Public page)
  ↓
User sees success message
  ↓
User clicks "Đăng nhập để xem chi tiết"
  ↓
App redirects to: /auth/login?returnUrl=/checkout/orders/{id}
  ↓
User logs in with JWT token ✅
  ↓
User redirected to order detail page ✅
```

## Features của trang Success

### Thanh toán thành công
- ✅ Icon check circle với animation bounce
- ✅ Thông tin đơn hàng: ID, số tiền, mã giao dịch
- ✅ Badge MoMo màu hồng
- ✅ Timeline các bước tiếp theo
- ✅ Nút "Đăng nhập để xem chi tiết" với returnUrl
- ✅ Nút "Về trang chủ"

### Thanh toán thất bại
- ❌ Icon times circle
- ❌ Thông báo lỗi từ MoMo
- ❌ Danh sách nguyên nhân có thể
- ❌ Nút "Thử lại thanh toán"
- ❌ Thông tin support

## Testing

### Test thanh toán thành công

1. Đăng nhập vào hệ thống
2. Tạo đơn hàng (checkout với COD)
3. Vào "Đơn hàng của tôi"
4. Click "Thanh toán MoMo"
5. Thanh toán với test card:
   - Card: `9704 0000 0000 0018`
   - Name: `NGUYEN VAN A`
   - Date: `03/07`
   - OTP: `OTP`
6. ✅ **Được chuyển về trang Success**
7. ✅ **Thấy thông tin đơn hàng**
8. Click "Đăng nhập để xem chi tiết"
9. ✅ **Đăng nhập thành công và xem được order detail**

### Test thanh toán thất bại

1. Làm tương tự bước 1-4 ở trên
2. Ở trang MoMo, click "Hủy" hoặc để timeout
3. ✅ **Được chuyển về trang Success với thông báo lỗi**
4. ✅ **Thấy nút "Thử lại thanh toán"**

## Security Notes

### ✅ Secure
- `/payment/momo/callback` - Public (cần thiết cho MoMo redirect)
- `/payment/momo/ipn` - Public (cần thiết cho MoMo server notification)
- `/payment/success` - Public (hiển thị kết quả)

### 🔒 Protected
- `/payment/momo/create/**` - Authenticated (chỉ customer/admin được tạo payment)
- `/checkout/orders/**` - Authenticated (xem chi tiết đơn hàng)

### 🛡️ Order Ownership Verification

Trong `PaymentController.createMoMoPayment()`:
```java
// Kiểm tra quyền sở hữu đơn hàng
if (!isAdmin && !order.getUser().getEmail().equals(currentUserEmail)) {
    log.warn("Unauthorized payment creation attempt");
    return "redirect:/checkout/orders";
}
```

## Responsive Design

Trang success responsive với Bootstrap 5:
- Mobile: 1 cột
- Tablet: 1 cột centered
- Desktop: centered card với max-width

## Animation

Success/Error icon có animation bounce để thu hút sự chú ý.

## CSS Customization

```css
.card {
    border-radius: 15px;
    box-shadow: large;
}

.fa-check-circle, .fa-times-circle {
    animation: bounce 2s;
}
```

## Improvements

### Future enhancements:
1. ✨ Gửi email thông báo thanh toán thành công
2. ✨ Push notification
3. ✨ Tự động redirect sau 10 giây nếu user đã đăng nhập
4. ✨ QR code để tracking đơn hàng
5. ✨ Social sharing button

## Troubleshooting

### Vẫn bị 403 sau khi fix?

**Kiểm tra**:
1. Đã restart ứng dụng chưa?
2. SecurityConfig có đúng thứ tự requestMatchers chưa? (public trước, protected sau)
3. Trang success.jsp có tồn tại không?
4. URL có đúng `/payment/success` không?

### Không hiển thị thông tin đơn hàng?

**Kiểm tra**:
1. Model attributes có được set trong PaymentController?
2. JSP có sử dụng đúng attribute names?
3. JSTL tags có import đúng không?

## Kết luận

Giải pháp này:
- ✅ Fix lỗi 403 Forbidden
- ✅ Cải thiện UX với trang success đẹp
- ✅ Giữ nguyên security (vẫn yêu cầu login để xem order detail)
- ✅ Tương thích với stateless JWT authentication
- ✅ Responsive và có animation

---

**Updated**: 2025-12-02
**Status**: ✅ Fixed and Tested
