# 🔐 JWT Authentication API - Hướng Dẫn

## Tính Năng Mới - JWT Authentication

Hệ thống đã được nâng cấp lên JWT authentication - đơn giản, hiện đại và không rườm rà!

### 🎯 Ưu Điểm
- ✅ **Không cần session** - Stateless authentication
- ✅ **Password tự động hash** - BCrypt khi đăng ký
- ✅ **User tự đăng ký** - Không cần setup database thủ công
- ✅ **Token-based** - Modern RESTful API
- ✅ **24h expiration** - Token tự động hết hạn

---

## 🚀 Cách Sử Dụng

### 1. Đăng Ký Tài Khoản Mới

**Endpoint:** `POST /api/auth/register`

**Request Body:**
```json
{
  "fullName": "Nguyễn Văn A",
  "email": "test@example.com",
  "password": "123456",
  "phone": "0901234567",
  "address": "Hà Nội"
}
```

**Response (Success):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "email": "test@example.com",
  "fullName": "Nguyễn Văn A",
  "role": "CUSTOMER",
  "userId": 1,
  "message": "Đăng ký thành công! Bạn đã được tự động đăng nhập."
}
```

**Lưu ý:**
- Password sẽ TỰ ĐỘNG hash bằng BCrypt
- User sẽ TỰ ĐỘNG được login và nhận JWT token
- Mặc định role = CUSTOMER
- enabled = true (không cần activate)

---

### 2. Đăng Nhập

**Endpoint:** `POST /api/auth/login`

**Request Body:**
```json
{
  "email": "test@example.com",
  "password": "123456"
}
```

**Response (Success):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "email": "test@example.com",
  "fullName": "Nguyễn Văn A",
  "role": "CUSTOMER",
  "userId": 1,
  "message": "Đăng nhập thành công!"
}
```

**Response (Error):**
```json
{
  "error": "Email hoặc password không đúng"
}
```

---

### 3. Sử Dụng API với JWT Token

Sau khi login, bạn nhận được JWT token. Sử dụng token này cho các API cần authentication:

**Header:**
```
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

**Ví dụ với cURL:**
```bash
curl -X GET http://localhost:8081/api/customer/profile \
  -H "Authorization: Bearer YOUR_JWT_TOKEN_HERE"
```

**Ví dụ với Postman:**
1. Chọn tab **Authorization**
2. Type: **Bearer Token**
3. Token: Paste JWT token vào

**Ví dụ với JavaScript (Fetch API):**
```javascript
fetch('http://localhost:8081/api/customer/profile', {
  method: 'GET',
  headers: {
    'Authorization': 'Bearer ' + token,
    'Content-Type': 'application/json'
  }
})
.then(response => response.json())
.then(data => console.log(data));
```

---

### 4. Verify Token

Kiểm tra token còn hợp lệ không:

**Endpoint:** `GET /api/auth/verify`

**Header:**
```
Authorization: Bearer YOUR_TOKEN
```

**Response:**
```json
{
  "valid": true,
  "email": "test@example.com",
  "message": "Token hợp lệ"
}
```

---

## 🧪 Test với Postman/Swagger

### Swagger UI
Truy cập: http://localhost:8081/swagger-ui.html

1. **Register:** Tìm `/api/auth/register` → Try it out
2. **Copy token** từ response
3. **Authorize:** Click nút 🔒 **Authorize** ở góc trên
4. **Paste token:** Nhập `Bearer YOUR_TOKEN`
5. **Test APIs:** Giờ có thể test các API cần auth

### Postman Collection

**1. Register:**
```
POST http://localhost:8081/api/auth/register
Body (JSON):
{
  "fullName": "Test User",
  "email": "test@test.com",
  "password": "123456",
  "phone": "0901234567",
  "address": "Ha Noi"
}
```

**2. Login:**
```
POST http://localhost:8081/api/auth/login
Body (JSON):
{
  "email": "test@test.com",
  "password": "123456"
}
```

**3. Get Profile (Authenticated):**
```
GET http://localhost:8081/api/customer/profile
Headers:
Authorization: Bearer {{token}}
```

---

## 🔐 JWT Token Structure

Token được chia làm 3 phần (cách nhau bởi dấu `.`):

```
eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0QHRlc3QuY29tIiwiYXV0aG9yaXRpZXMiOlt7ImF1dGhvcml0eSI6IlJPTEVfQ1VTVE9NRVIifV0sImlhdCI6MTczMTI1MjAwMCwiZXhwIjoxNzMxMzM4NDAwfQ.signature_here
```

**Decode token tại:** https://jwt.io/

**Payload Example:**
```json
{
  "sub": "test@test.com",
  "authorities": [{"authority": "ROLE_CUSTOMER"}],
  "iat": 1731252000,
  "exp": 1731338400
}
```

---

## 📝 API Endpoints Summary

### Public (No Auth)
```
POST   /api/auth/register          - Đăng ký
POST   /api/auth/login             - Đăng nhập
GET    /api/products               - Xem sản phẩm
GET    /api/categories             - Xem danh mục
```

### Customer/Admin (Auth Required)
```
GET    /api/customer/profile       - Xem profile
PUT    /api/customer/profile       - Cập nhật profile
POST   /api/orders                 - Tạo đơn hàng
GET    /api/orders                 - Xem đơn hàng
POST   /payment/momo/create/{id}   - Thanh toán MoMo
```

### Admin Only
```
GET    /api/admin/users            - Quản lý users
POST   /api/admin/products         - Thêm sản phẩm
PUT    /api/admin/products/{id}    - Sửa sản phẩm
DELETE /api/admin/products/{id}    - Xóa sản phẩm
```

---

## 🛠️ Configuration

### JWT Settings (application.properties)

```properties
# JWT Secret Key (256-bit minimum)
jwt.secret=ShopMeVaBe2025SecretKeyForJWTAuthenticationVeryLongAndSecure123456789

# JWT Expiration (24 hours in milliseconds)
jwt.expiration=86400000
```

### Security Features
- ✅ BCrypt password hashing (auto)
- ✅ JWT signature verification (HMAC SHA256)
- ✅ Token expiration (24h)
- ✅ Stateless sessions
- ✅ CSRF disabled (stateless)
- ✅ Role-based authorization

---

## 🎨 Frontend Integration Example

### React / Vue / Angular

```javascript
// 1. Register
async function register(userData) {
  const response = await fetch('http://localhost:8081/api/auth/register', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(userData)
  });
  const data = await response.json();

  // Lưu token vào localStorage
  localStorage.setItem('token', data.token);
  localStorage.setItem('user', JSON.stringify(data));

  return data;
}

// 2. Login
async function login(email, password) {
  const response = await fetch('http://localhost:8081/api/auth/login', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ email, password })
  });
  const data = await response.json();

  localStorage.setItem('token', data.token);
  return data;
}

// 3. Sử dụng API với token
async function getProfile() {
  const token = localStorage.getItem('token');

  const response = await fetch('http://localhost:8081/api/customer/profile', {
    headers: {
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    }
  });

  return response.json();
}

// 4. Logout
function logout() {
  localStorage.removeItem('token');
  localStorage.removeItem('user');
  window.location.href = '/login';
}
```

---

## 🔍 Troubleshooting

### Token Invalid
```json
{
  "valid": false,
  "message": "JWT expired at..."
}
```
**Fix:** Login lại để lấy token mới

### 401 Unauthorized
**Nguyên nhân:**
- Token sai
- Token hết hạn
- Thiếu header Authorization
- Format header sai

**Fix:**
```
Authorization: Bearer YOUR_TOKEN_HERE
           ↑ Có khoảng trắng
```

### 403 Forbidden
**Nguyên nhân:** Không đủ quyền

**Fix:** Đăng nhập với account có role phù hợp

---

## 📚 So Sánh: Session vs JWT

| Feature | Session (Old) | JWT (New) |
|---------|--------------|-----------|
| State | Stateful | Stateless |
| Storage | Server memory | Client-side |
| Scalability | Limited | Excellent |
| Mobile-friendly | No | Yes |
| CSRF Protection | Required | Not needed |
| Token Expiry | Server control | Auto expire |
| Password Hash | Manual | Auto BCrypt |

---

## ✅ Checklist Migration

Nếu đang dùng session-based auth:

- [x] Update pom.xml (thêm JWT dependencies)
- [x] Tạo JwtUtil class
- [x] Tạo JwtAuthenticationFilter
- [x] Update SecurityConfig (STATELESS mode)
- [x] Update AuthRestController (login/register)
- [x] Add JWT config vào application.properties
- [ ] Test register API
- [ ] Test login API
- [ ] Test authenticated APIs
- [ ] Update frontend (nếu có)

---

**Happy Coding!** 🚀

Bây giờ không cần setup database thủ công, không cần fix password BCrypt nữa. User tự đăng ký và mọi thứ tự động! 🎉
