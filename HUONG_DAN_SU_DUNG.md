# 📚 HƯỚNG DẪN SỬ DỤNG HỆ THỐNG

## 🔐 TÀI KHOẢN ĐĂNG NHẬP

### **Admin Account:**
```
Email: admin@shopmevabe.com
Password: admin123
Role: ADMIN
```

**Quyền Admin:**
- ✅ Truy cập Admin Dashboard
- ✅ Quản lý Users (xem, sửa, xóa)
- ✅ Quản lý Products (thêm, sửa, xóa)
- ✅ Quản lý Categories (thêm, sửa, xóa)
- ✅ Quản lý Orders (xem tất cả đơn hàng, cập nhật trạng thái)
- ✅ Xem thống kê hệ thống

### **Customer Account:**
Người dùng tự đăng ký tại: `http://localhost:8081/register`

**Quyền Customer:**
- ✅ Xem sản phẩm
- ✅ Thêm vào giỏ hàng
- ✅ Đặt hàng
- ✅ Thanh toán MoMo
- ✅ Xem đơn hàng của mình
- ✅ Cập nhật profile

---

## 🚀 FLOW SỬ DỤNG HỆ THỐNG

### **A. ADMIN WORKFLOW**

#### **1. Đăng nhập Admin**
```
URL: http://localhost:8081/login
Email: admin@shopmevabe.com
Password: admin123
```
➡️ Redirect: `/admin/dashboard`

#### **2. Admin Dashboard**
```
URL: http://localhost:8081/admin/dashboard
```
**Hiển thị:**
- Tổng số Users
- Tổng số Products
- Tổng số Categories
- Tổng số Orders

#### **3. Quản lý Products**
```
URL: http://localhost:8081/admin/products
```
**Chức năng:**
- Xem danh sách sản phẩm
- Thêm sản phẩm mới
- Sửa sản phẩm
- Xóa sản phẩm
- Tìm kiếm sản phẩm

#### **4. Quản lý Categories**
```
URL: http://localhost:8081/admin/categories
```
**Chức năng:**
- Xem danh mục
- Thêm danh mục
- Sửa danh mục
- Xóa danh mục

#### **5. Quản lý Users**
```
URL: http://localhost:8081/admin/users
```
**Chức năng:**
- Xem danh sách users
- Xem chi tiết user
- Enable/Disable user
- Xóa user

#### **6. Quản lý Orders**
```
URL: http://localhost:8081/admin/orders
```
**Chức năng:**
- Xem tất cả đơn hàng
- Cập nhật trạng thái đơn hàng:
  - PENDING → PROCESSING
  - PROCESSING → SHIPPED
  - SHIPPED → DELIVERED
  - Bất kỳ → CANCELLED

---

### **B. CUSTOMER WORKFLOW**

#### **1. Đăng ký tài khoản**
```
URL: http://localhost:8081/register

Form:
- Full Name
- Email
- Password
- Phone
- Address
```
➡️ Tự động login sau khi đăng ký

#### **2. Đăng nhập**
```
URL: http://localhost:8081/login

Form:
- Email
- Password
```
➡️ Redirect: `/` (Homepage)

#### **3. Browse Products**
```
URL: http://localhost:8081/products

Features:
- Xem tất cả sản phẩm
- Filter theo category
- Tìm kiếm sản phẩm
- Xem chi tiết sản phẩm
```

#### **4. Thêm vào giỏ hàng**
```
URL: http://localhost:8081/cart

Actions:
- Thêm sản phẩm vào cart
- Cập nhật số lượng
- Xóa sản phẩm khỏi cart
- View tổng tiền
```

#### **5. Checkout - Đặt hàng**
```
URL: http://localhost:8081/checkout

Form:
- Shipping Address (auto-fill từ profile)
- Phone (auto-fill)
- Notes (optional)

Submit → Tạo đơn hàng
```
➡️ Redirect: `/checkout/orders/{orderId}`

#### **6. Thanh toán MoMo**
```
URL: http://localhost:8081/payment/momo/create/{orderId}

Flow:
1. Click "Thanh toán MoMo"
2. Redirect đến MoMo payment page
3. Quét QR hoặc nhập thông tin thẻ
4. Xác nhận thanh toán
5. MoMo redirect về: /payment/momo/callback
6. Order status: PENDING → PROCESSING
7. Payment status: UNPAID → PAID
```

#### **7. Xem đơn hàng**
```
URL: http://localhost:8081/checkout/orders

Features:
- Xem tất cả đơn hàng của mình
- Xem chi tiết từng đơn hàng
- Xem trạng thái đơn hàng
- Xem lịch sử thanh toán
```

#### **8. Đăng xuất**
```
Click: Header → "👤 Email ▼" → "Đăng xuất"
```
➡️ Cookies bị xóa, redirect `/login`

---

## 📱 API ENDPOINTS

### **Authentication APIs (Public)**

#### **1. Register**
```bash
POST http://localhost:8081/api/auth/register
Content-Type: application/json

{
  "fullName": "Nguyen Van A",
  "email": "nguyenvana@example.com",
  "password": "password123",
  "phone": "0901234567",
  "address": "123 ABC Street"
}

Response:
{
  "token": "eyJhbGci...",
  "type": "Bearer",
  "email": "nguyenvana@example.com",
  "fullName": "Nguyen Van A",
  "role": "CUSTOMER",
  "userId": 10,
  "message": "Đăng ký thành công!"
}
```

#### **2. Login**
```bash
POST http://localhost:8081/api/auth/login
Content-Type: application/json

{
  "email": "admin@shopmevabe.com",
  "password": "admin123"
}

Response:
{
  "token": "eyJhbGci...",
  "type": "Bearer",
  "email": "admin@shopmevabe.com",
  "fullName": "Admin Shop Mẹ và Bé",
  "role": "ADMIN",
  "userId": 1,
  "message": "Đăng nhập thành công!"
}
```

#### **3. Verify Token**
```bash
GET http://localhost:8081/api/auth/verify
Authorization: Bearer <your-token>

Response:
{
  "valid": true,
  "email": "admin@shopmevabe.com",
  "role": "ADMIN"
}
```

### **Product APIs (Public)**

```bash
# Get all products
GET http://localhost:8081/api/products

# Get product by ID
GET http://localhost:8081/api/products/{id}

# Search products
GET http://localhost:8081/api/products/search?keyword=sua

# Get products by category
GET http://localhost:8081/api/products/category/{categoryId}
```

### **Order APIs (Authenticated)**

```bash
# Get my orders
GET http://localhost:8081/api/orders
Authorization: Bearer <your-token>

# Get order by ID
GET http://localhost:8081/api/orders/{id}
Authorization: Bearer <your-token>

# Create new order
POST http://localhost:8081/api/orders
Authorization: Bearer <your-token>
Content-Type: application/json

{
  "shippingAddress": "123 ABC Street",
  "phone": "0901234567",
  "notes": "Giao giờ hành chính",
  "items": [
    {
      "productId": 1,
      "quantity": 2
    }
  ]
}
```

### **Admin APIs (Admin only)**

```bash
# Get all users
GET http://localhost:8081/api/admin/users
Authorization: Bearer <admin-token>

# Get all products (admin view)
GET http://localhost:8081/api/admin/products
Authorization: Bearer <admin-token>

# Create product
POST http://localhost:8081/api/admin/products
Authorization: Bearer <admin-token>
Content-Type: application/json

# Update order status
PUT http://localhost:8081/api/admin/orders/{id}/status
Authorization: Bearer <admin-token>
Content-Type: application/json

{
  "status": "PROCESSING"
}
```

---

## 🔧 TRẠNG THÁI ĐƠN HÀNG

### **Order Status Flow:**
```
PENDING (Chờ thanh toán)
    ↓
PROCESSING (Đang xử lý) - sau khi thanh toán thành công
    ↓
SHIPPED (Đã gửi hàng)
    ↓
DELIVERED (Đã giao hàng)

Hoặc:
CANCELLED (Đã hủy) - có thể hủy từ bất kỳ trạng thái nào
```

### **Payment Status:**
```
UNPAID - Chưa thanh toán
PAID - Đã thanh toán
REFUNDED - Đã hoàn tiền
```

### **Payment Methods:**
```
COD - Cash on Delivery (Thanh toán khi nhận hàng)
MOMO - Thanh toán qua ví MoMo
BANK_TRANSFER - Chuyển khoản ngân hàng
```

---

## 🛡️ SECURITY

### **Authentication:**
- ✅ JWT Token (24 hours expiration)
- ✅ HTTP-Only Cookie (chống XSS)
- ✅ BCrypt Password Hashing
- ✅ Stateless (no server session)

### **Authorization:**
- ✅ Role-based: ADMIN, CUSTOMER
- ✅ Resource ownership validation
- ✅ Protected endpoints

### **Best Practices:**
1. **Đổi password mặc định** của admin sau khi deploy
2. **Không share JWT token** với người khác
3. **Logout** khi xong việc
4. **Kiểm tra HTTPS** khi deploy production

---

## 🔍 TROUBLESHOOTING

### **Vấn đề 1: Không đăng nhập được**
```
Giải pháp:
1. Xóa cookies: http://localhost:8081/clear-cookies
2. Thử Incognito mode: Ctrl + Shift + N
3. Check tài khoản tồn tại trong database
4. Check password đúng chưa
```

### **Vấn đề 2: Lỗi 403 Forbidden**
```
Giải pháp:
1. Check đã đăng nhập chưa
2. Check role có đúng không (ADMIN vs CUSTOMER)
3. Clear cookies và login lại
```

### **Vấn đề 3: Thanh toán MoMo không hoạt động**
```
Kiểm tra:
1. MoMo credentials trong application.properties
2. Ngrok URL đúng chưa (app.base-url)
3. Order status là PENDING
4. Order amount > 0
5. Network connection
```

### **Vấn đề 4: Không tạo được đơn hàng**
```
Kiểm tra:
1. Giỏ hàng có sản phẩm chưa
2. Đã đăng nhập chưa
3. Shipping address, phone đã điền chưa
4. Product còn hàng không (stock > 0)
```

---

## 📊 DATABASE

### **Tables:**
- `users` - Thông tin người dùng
- `products` - Sản phẩm
- `categories` - Danh mục
- `orders` - Đơn hàng
- `order_details` - Chi tiết đơn hàng

### **Import Sample Data:**
```bash
# Sử dụng HeidiSQL hoặc MySQL Client
mysql -u root -p shop_me_va_be < src/main/resources/db/data.sql
```

**Data.sql includes:**
- 1 Admin account
- 4 Customer accounts
- 8 Categories
- 40 Products
- 4 Sample orders

---

## 🎯 TESTING CHECKLIST

### **Authentication:**
- [ ] Đăng ký tài khoản mới
- [ ] Đăng nhập admin
- [ ] Đăng nhập customer
- [ ] Đăng xuất
- [ ] Clear cookies

### **Shopping Flow:**
- [ ] Browse products
- [ ] View product detail
- [ ] Add to cart
- [ ] Update cart quantity
- [ ] Remove from cart
- [ ] Checkout
- [ ] Create order

### **Payment:**
- [ ] Tạo MoMo payment
- [ ] Thanh toán thành công
- [ ] Callback về hệ thống
- [ ] Order status update
- [ ] Payment status update

### **Admin:**
- [ ] Access admin dashboard
- [ ] Manage products
- [ ] Manage categories
- [ ] Manage users
- [ ] Manage orders
- [ ] Update order status

---

## ✅ KÊTLUẬN

**Hệ thống đã sẵn sàng sử dụng!**

✅ Authentication hoạt động (JWT)
✅ Authorization theo role (ADMIN, CUSTOMER)
✅ Shopping cart & checkout
✅ MoMo payment integration
✅ Order management
✅ Admin dashboard
✅ Security đầy đủ

**Contact:** Nếu có vấn đề, hãy check logs và các file hướng dẫn khác:
- `HOW_TO_FIX.md` - Fix lỗi nhanh
- `FIX_LOGIN_LOGOUT.md` - Chi tiết troubleshooting
- `FIX_QUICK.md` - Hướng dẫn 4 bước

🎉 **Chúc bạn sử dụng hệ thống hiệu quả!**
