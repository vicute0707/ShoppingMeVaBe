# 🚀 Swagger API Testing - Quick Start

## 📍 Quick Access

### URLs
```
Swagger UI:     http://localhost:8081/swagger-ui.html
OpenAPI JSON:   http://localhost:8081/v3/api-docs
OpenAPI YAML:   http://localhost:8081/v3/api-docs.yaml
```

---

## ⚡ 3 Bước Test API

### Bước 1: Start Application
```bash
./mvnw spring-boot:run
```

### Bước 2: Open Swagger UI
```
http://localhost:8081/swagger-ui.html
```

### Bước 3: Test Endpoint

**Public API (không cần auth):**
1. Chọn endpoint: `GET /api/public/products`
2. Click "Try it out"
3. Click "Execute"
4. Xem response ✅

**Protected API (cần auth):**
1. Login: `POST /api/auth/login`
   ```json
   {
     "email": "admin@shopmevabe.com",
     "password": "admin123"
   }
   ```
2. Copy token từ response
3. Click "Authorize" 🔓 button
4. Paste: `Bearer <your-token>`
5. Click "Authorize"
6. Test protected endpoints ✅

---

## 📚 Ghi Tài Liệu API

### Controller Level
```java
@RestController
@RequestMapping("/api/products")
@Tag(name = "Products", description = "Product APIs")
public class ProductController {
```

### Method Level
```java
@Operation(
    summary = "Get all products",
    description = "Retrieve list of all active products"
)
@ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Success"),
    @ApiResponse(responseCode = "401", description = "Unauthorized")
})
@GetMapping
public ResponseEntity<List<Product>> getAllProducts() {
```

### Parameter Level
```java
@GetMapping("/{id}")
public ResponseEntity<?> getProduct(
    @Parameter(description = "Product ID", required = true)
    @PathVariable Long id
) {
```

### DTO Level
```java
@Schema(description = "User registration data")
public class RegisterDTO {

    @Schema(description = "Email address", example = "user@example.com")
    @Email
    private String email;
}
```

---

## 📥 Export Documentation

### JSON
```bash
curl http://localhost:8081/v3/api-docs > openapi.json
```

### YAML
```bash
curl http://localhost:8081/v3/api-docs.yaml > openapi.yaml
```

### Postman Collection
1. Open Postman
2. Import → Link → `http://localhost:8081/v3/api-docs`
3. Done ✅

---

## 🎯 Common Annotations

| Annotation | Sử Dụng |
|------------|---------|
| `@Tag` | Nhóm endpoints |
| `@Operation` | Mô tả endpoint |
| `@ApiResponses` | List response codes |
| `@Parameter` | Mô tả parameter |
| `@Schema` | Mô tả DTO field |
| `@SecurityRequirement` | Yêu cầu auth |

---

## 🧪 Test Scenarios

### ✅ Test Success Case
```
GET /api/public/products
→ 200 OK với list products
```

### ⚠️ Test Error Cases
```
GET /api/public/products/999
→ 404 Not Found

POST /api/admin/products (no auth)
→ 401 Unauthorized

POST /api/admin/products (invalid data)
→ 400 Bad Request
```

---

## 📊 Swagger UI Sections

Khi mở Swagger UI, bạn sẽ thấy:

```
┌─────────────────────────────────────┐
│ 🔐 Authentication                   │
│   POST /api/auth/register          │
│   POST /api/auth/login             │
│   GET  /api/auth/verify            │
├─────────────────────────────────────┤
│ 🛍️ Guest - Products                │
│   GET  /api/public/products        │
│   GET  /api/public/products/{id}   │
│   GET  /api/public/products/search │
├─────────────────────────────────────┤
│ 👤 Customer - Orders                │
│   GET  /api/orders                 │
│   POST /api/orders                 │
├─────────────────────────────────────┤
│ ⚙️ Admin - Products                 │
│   POST   /api/admin/products       │
│   PUT    /api/admin/products/{id}  │
│   DELETE /api/admin/products/{id}  │
└─────────────────────────────────────┘
```

---

## 🔐 Authentication Flow

```
1. Login
   POST /api/auth/login
   Body: { "email": "admin@shopmevabe.com", "password": "admin123" }

2. Get Token
   Response: { "token": "eyJhbG..." }

3. Authorize
   Click "Authorize" button
   Enter: "Bearer eyJhbG..."

4. Test Protected APIs
   All requests now include JWT token ✅
```

---

## 💡 Tips

### ✅ Do's
- Luôn test public endpoints trước
- Document tất cả parameters
- Provide example values
- List all possible response codes
- Group related endpoints với @Tag

### ❌ Don'ts
- Không bỏ qua error responses
- Không quên "Bearer " prefix khi authorize
- Không hardcode tokens trong code
- Không skip documentation cho admin APIs

---

## 🐛 Troubleshooting

### Swagger UI không mở được
```bash
# Check application running
curl http://localhost:8081/actuator/health

# Check Swagger URL
curl http://localhost:8081/swagger-ui.html
```

### Token không hoạt động
```
❌ Wrong: eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
✅ Right: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
          ^^^^^^ Cần có "Bearer " prefix
```

### Endpoints không hiển thị
```java
// Check có annotations này chưa:
@RestController  // ✅ Required
@RequestMapping  // ✅ Required
@GetMapping      // ✅ Required
```

---

## 📖 Full Documentation

Xem hướng dẫn chi tiết: **[SWAGGER_API_TESTING_GUIDE.md](./SWAGGER_API_TESTING_GUIDE.md)**

---

## 🎓 Cho Báo Cáo

### Cần nộp:
1. ✅ Screenshot Swagger UI dashboard
2. ✅ Screenshot test login endpoint
3. ✅ Screenshot test create/update endpoint
4. ✅ File `openapi.json` hoặc `openapi.yaml`
5. ✅ Code examples với annotations

### Export command:
```bash
# Create docs folder
mkdir -p docs/swagger

# Export JSON
curl http://localhost:8081/v3/api-docs > docs/swagger/openapi.json

# Export YAML
curl http://localhost:8081/v3/api-docs.yaml > docs/swagger/openapi.yaml

# Take screenshots
# 1. Open http://localhost:8081/swagger-ui.html
# 2. Screenshot dashboard
# 3. Test login → Screenshot
# 4. Test CRUD → Screenshot
```

---

**🎉 That's it! Happy Testing!**

**Next:** [Full Swagger Guide](./SWAGGER_API_TESTING_GUIDE.md) | [JUnit Testing](./JUNIT_TESTING_GUIDE.md)
