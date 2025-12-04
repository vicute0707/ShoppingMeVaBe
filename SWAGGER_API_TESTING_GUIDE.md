# 📚 Hướng Dẫn Test & Ghi Tài Liệu API với Swagger/OpenAPI

## 🎯 Mục Lục
1. [Giới thiệu Swagger/OpenAPI](#1-giới-thiệu)
2. [Truy cập Swagger UI](#2-truy-cập-swagger-ui)
3. [Test API với Swagger UI](#3-test-api-với-swagger-ui)
4. [Ghi tài liệu API](#4-ghi-tài-liệu-api)
5. [Export Documentation](#5-export-documentation)
6. [Best Practices](#6-best-practices)
7. [Examples](#7-examples)

---

## 1. Giới Thiệu

### Swagger/OpenAPI là gì?

**Swagger** (hiện là **OpenAPI**) là một framework để:
- 📖 **Document API**: Tự động generate tài liệu API
- 🧪 **Test API**: Giao diện web để test endpoints
- 🔄 **Generate Code**: Tự động tạo client/server code
- 📊 **Visualize API**: Hiển thị API structure trực quan

### Trong dự án ShoppingMeVaBe

Dự án đã tích hợp **Springdoc OpenAPI 2.6.0**:
```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.6.0</version>
</dependency>
```

---

## 2. Truy Cập Swagger UI

### 2.1. Start Application

```bash
# Start Spring Boot application
./mvnw spring-boot:run

# Hoặc
java -jar target/www-0.0.1-SNAPSHOT.jar
```

Đợi ứng dụng khởi động (port 8081):
```
Started ShoppingMomadnBabyApplication in 5.234 seconds
```

### 2.2. Open Swagger UI

**Swagger UI URL:**
```
http://localhost:8081/swagger-ui.html
```

**Hoặc:**
```
http://localhost:8081/swagger-ui/index.html
```

**OpenAPI JSON:**
```
http://localhost:8081/v3/api-docs
```

### 2.3. Giao Diện Swagger UI

Khi mở Swagger UI, bạn sẽ thấy:

```
┌─────────────────────────────────────────────────────────┐
│  Shopping Store REST API                    v1.0        │
├─────────────────────────────────────────────────────────┤
│  Description: API cho hệ thống thương mại điện tử       │
│  Servers: http://localhost:8081                         │
├─────────────────────────────────────────────────────────┤
│  ▼ Authentication - JWT Authentication APIs             │
│     POST /api/auth/register  - Đăng ký tài khoản       │
│     POST /api/auth/login     - Đăng nhập               │
│     GET  /api/auth/verify    - Verify JWT token        │
│                                                          │
│  ▼ Guest - Products - Public product APIs              │
│     GET  /api/public/products           - All products │
│     GET  /api/public/products/{id}      - Get by ID    │
│     GET  /api/public/products/search    - Search       │
│                                                          │
│  ▼ Admin - Products - Admin product management         │
│     POST   /api/admin/products          - Create       │
│     PUT    /api/admin/products/{id}     - Update       │
│     DELETE /api/admin/products/{id}     - Delete       │
└─────────────────────────────────────────────────────────┘
```

---

## 3. Test API với Swagger UI

### 3.1. Test Public API (Không cần Auth)

#### Ví dụ: Get All Products

1. **Mở section "Guest - Products"**
   - Click vào `▼ Guest - Products`

2. **Chọn endpoint "GET /api/public/products"**
   - Click vào endpoint để expand

3. **Click nút "Try it out"**
   - Nút ở góc phải endpoint

4. **Click "Execute"**
   - Swagger sẽ gửi request đến API

5. **Xem Response:**
```json
{
  "status": 200,
  "body": [
    {
      "id": 1,
      "name": "Tã Pampers Premium Care",
      "description": "Tã cao cấp cho bé",
      "price": 299000,
      "stockQuantity": 100,
      "imageUrl": "https://...",
      "active": true,
      "category": {
        "id": 1,
        "name": "Tã bỉm"
      }
    }
  ]
}
```

6. **Copy cURL command** (để chạy trong terminal):
```bash
curl -X 'GET' \
  'http://localhost:8081/api/public/products' \
  -H 'accept: application/json'
```

### 3.2. Test API Có Authentication

#### Bước 1: Login để lấy JWT Token

1. **Mở section "Authentication"**
2. **Chọn "POST /api/auth/login"**
3. **Click "Try it out"**
4. **Nhập Request Body:**
```json
{
  "email": "admin@shopmevabe.com",
  "password": "admin123"
}
```
5. **Click "Execute"**
6. **Copy JWT token từ response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "email": "admin@shopmevabe.com",
  "fullName": "Admin",
  "role": "ADMIN",
  "userId": 1
}
```

#### Bước 2: Authorize trong Swagger

1. **Click nút "Authorize" 🔓** (ở góc trên bên phải)
2. **Nhập token vào ô "Value":**
```
Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```
   ⚠️ **Lưu ý:** Phải có chữ "Bearer " trước token!

3. **Click "Authorize"**
4. **Click "Close"**

#### Bước 3: Test API yêu cầu Auth

Ví dụ: Create Product (Admin only)

1. **Mở section "Admin - Products"**
2. **Chọn "POST /api/admin/products"**
3. **Click "Try it out"**
4. **Nhập Request Body:**
```json
{
  "name": "Test Product",
  "description": "Test Description",
  "price": 100000,
  "stockQuantity": 50,
  "imageUrl": "https://example.com/image.jpg",
  "active": true,
  "category": {
    "id": 1
  }
}
```
5. **Click "Execute"**
6. **Xem Response:**
```json
{
  "status": 200,
  "body": {
    "id": 10,
    "name": "Test Product",
    "description": "Test Description",
    "price": 100000,
    ...
  }
}
```

### 3.3. Test với Parameters

#### Ví dụ: Search Products

1. **Endpoint:** `GET /api/public/products/search`
2. **Parameter:** `keyword` (required)
3. **Nhập value:** `"Tã"`
4. **Execute**
5. **Response:** Danh sách products có từ "Tã"

#### Ví dụ: Get Product by ID

1. **Endpoint:** `GET /api/public/products/{id}`
2. **Path Parameter:** `id = 1`
3. **Execute**
4. **Response:** Product với ID = 1

### 3.4. Test Error Cases

#### Test 404 Not Found:
```
GET /api/public/products/999
→ Response: 404 Not Found
```

#### Test 401 Unauthorized:
```
POST /api/admin/products (without token)
→ Response: 401 Unauthorized
```

#### Test 400 Bad Request:
```json
POST /api/auth/register
Body: {
  "email": "invalid-email",  // Invalid format
  "password": "123"          // Too short
}
→ Response: 400 Bad Request với validation errors
```

---

## 4. Ghi Tài Liệu API

### 4.1. API Configuration

File: `src/main/java/iuh/student/www/config/OpenAPIConfig.java`

```java
@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "Shopping Store REST API",
        version = "1.0",
        description = """
            # Shopping Store API Documentation

            Comprehensive REST API for Shopping Store e-commerce platform.

            ## Features:
            - Product management
            - User authentication (JWT)
            - Order processing
            """,
        contact = @Contact(
            name = "Shopping Store Team",
            email = "support@shoppingstore.com"
        ),
        license = @License(
            name = "MIT License",
            url = "https://opensource.org/licenses/MIT"
        )
    ),
    servers = {
        @Server(
            url = "http://localhost:8081",
            description = "Local Development Server"
        ),
        @Server(
            url = "https://api.shoppingstore.com",
            description = "Production Server"
        )
    }
)
@SecurityScheme(
    name = "bearerAuth",
    type = SecuritySchemeType.HTTP,
    scheme = "bearer",
    bearerFormat = "JWT",
    description = "JWT Authentication. Login để lấy token."
)
public class OpenAPIConfig {
}
```

### 4.2. Document REST Controller

#### Level 1: Controller Level

```java
@RestController
@RequestMapping("/api/public/products")
@RequiredArgsConstructor
@Tag(name = "Guest - Products", description = "Public API for browsing products")
public class ProductRestController {
    // ...
}
```

**Annotations:**
- `@Tag`: Nhóm các endpoints vào một category
  - `name`: Tên hiển thị trong Swagger UI
  - `description`: Mô tả chi tiết

#### Level 2: Method Level

```java
@Operation(
    summary = "Get all active products",
    description = "Retrieve list of all active products available for purchase"
)
@ApiResponses(value = {
    @ApiResponse(
        responseCode = "200",
        description = "Successfully retrieved products",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = Product.class)
        )
    )
})
@GetMapping
public ResponseEntity<List<Product>> getAllProducts() {
    List<Product> products = productService.getActiveProducts();
    return ResponseEntity.ok(products);
}
```

**Annotations:**
- `@Operation`: Mô tả endpoint
  - `summary`: Tóm tắt ngắn (1 dòng)
  - `description`: Mô tả chi tiết (nhiều dòng)
- `@ApiResponses`: Danh sách responses có thể
- `@ApiResponse`: Mỗi response code
  - `responseCode`: HTTP status code (200, 404, 500...)
  - `description`: Mô tả khi nào trả về code này
  - `content`: Định dạng response body

#### Level 3: Parameter Level

```java
@Operation(summary = "Get product by ID")
@GetMapping("/{id}")
public ResponseEntity<?> getProductById(
    @Parameter(
        description = "Product ID",
        required = true,
        example = "1"
    )
    @PathVariable Long id
) {
    return productService.getProductById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
}
```

**Annotations:**
- `@Parameter`: Document parameter
  - `description`: Mô tả parameter
  - `required`: true/false
  - `example`: Giá trị ví dụ
  - `schema`: Kiểu dữ liệu

### 4.3. Document Request Body

```java
@Operation(summary = "Create new product")
@ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Product created successfully"),
    @ApiResponse(responseCode = "400", description = "Invalid input"),
    @ApiResponse(responseCode = "401", description = "Unauthorized")
})
@PostMapping
public ResponseEntity<?> createProduct(
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Product data",
        required = true,
        content = @Content(
            schema = @Schema(implementation = Product.class)
        )
    )
    @Valid @RequestBody Product product
) {
    // ...
}
```

### 4.4. Document DTO/Entity

```java
@Schema(description = "User registration data")
public class RegisterDTO {

    @Schema(description = "Full name", example = "Nguyen Van A", required = true)
    @NotBlank(message = "Full name is required")
    private String fullName;

    @Schema(description = "Email address", example = "user@example.com", required = true)
    @Email(message = "Email must be valid")
    private String email;

    @Schema(description = "Password (min 6 characters)", example = "password123", required = true)
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @Schema(description = "Confirm password", example = "password123", required = true)
    private String confirmPassword;

    @Schema(description = "Phone number", example = "0901234567")
    private String phone;

    @Schema(description = "Address", example = "123 Main St")
    private String address;
}
```

### 4.5. Document Authentication

#### Require Auth cho endpoint:

```java
@Operation(
    summary = "Create product (Admin only)",
    security = @SecurityRequirement(name = "bearerAuth")
)
@PostMapping
public ResponseEntity<?> createProduct(@RequestBody Product product) {
    // ...
}
```

#### Mark endpoint as public:

```java
@Operation(
    summary = "Get all products (Public)",
    security = {}  // Không cần auth
)
@GetMapping
public ResponseEntity<?> getAllProducts() {
    // ...
}
```

---

## 5. Export Documentation

### 5.1. Export OpenAPI JSON

**URL:**
```
http://localhost:8081/v3/api-docs
```

**Cách export:**
```bash
# Save to file
curl http://localhost:8081/v3/api-docs > openapi.json

# Pretty print
curl http://localhost:8081/v3/api-docs | jq . > openapi-pretty.json
```

**File output:** `openapi.json`
```json
{
  "openapi": "3.0.1",
  "info": {
    "title": "Shopping Store REST API",
    "description": "...",
    "version": "1.0"
  },
  "servers": [...],
  "paths": {
    "/api/auth/login": {
      "post": {
        "tags": ["Authentication"],
        "summary": "Đăng nhập",
        ...
      }
    }
  },
  "components": {
    "schemas": {...}
  }
}
```

### 5.2. Export OpenAPI YAML

**URL:**
```
http://localhost:8081/v3/api-docs.yaml
```

**Cách export:**
```bash
curl http://localhost:8081/v3/api-docs.yaml > openapi.yaml
```

### 5.3. Generate HTML Documentation

#### Option 1: Swagger UI HTML

**Online:**
1. Vào https://editor.swagger.io/
2. File → Import File → Chọn `openapi.json`
3. Generate Client → HTML2 (official)
4. Download ZIP

**Offline:**
```bash
# Install swagger-codegen
npm install -g swagger-codegen

# Generate HTML
swagger-codegen generate -i openapi.json -l html2 -o docs/
```

#### Option 2: ReDoc

**Add dependency:**
```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-ui</artifactId>
    <version>1.7.0</version>
</dependency>
```

**Access:**
```
http://localhost:8081/redoc/index.html
```

### 5.4. Generate Postman Collection

**Online:**
1. Vào https://www.postman.com/
2. Import → Link → Nhập: `http://localhost:8081/v3/api-docs`
3. Postman tự động tạo collection

**Offline:**
```bash
# Install openapi-to-postmanv2
npm install -g openapi-to-postmanv2

# Convert
openapi2postmanv2 -s openapi.json -o postman-collection.json
```

### 5.5. Generate Client Code

```bash
# Java Client
swagger-codegen generate -i openapi.json -l java -o client/java

# JavaScript/TypeScript Client
swagger-codegen generate -i openapi.json -l typescript-axios -o client/typescript

# Python Client
swagger-codegen generate -i openapi.json -l python -o client/python
```

---

## 6. Best Practices

### 6.1. Naming Conventions

**Endpoints:**
```
✅ Good:
GET    /api/products           - List all
GET    /api/products/{id}      - Get one
POST   /api/products           - Create
PUT    /api/products/{id}      - Update
DELETE /api/products/{id}      - Delete

❌ Bad:
GET    /api/getProducts
POST   /api/createProduct
GET    /api/product/{id}/get
```

**Tags:**
```
✅ Good:
- "Authentication"
- "Guest - Products"
- "Admin - Products"
- "Customer - Orders"

❌ Bad:
- "auth"
- "ProductAPI"
- "admin_products"
```

### 6.2. Documentation Quality

**Summary:**
- ✅ Ngắn gọn, 1 dòng
- ✅ Bắt đầu bằng động từ: "Get", "Create", "Update"
- ❌ Không quá dài, không giải thích chi tiết

**Description:**
- ✅ Chi tiết, nhiều dòng
- ✅ Giải thích business logic
- ✅ Liệt kê requirements
- ✅ Đưa ra examples

**Examples:**
```java
// ✅ Good
@Operation(
    summary = "Create new product",
    description = """
        Create a new product in the system.

        Requirements:
        - Admin role required
        - Category must exist
        - Name must be unique
        - Price must be > 0

        Returns:
        - 200: Product created successfully
        - 400: Validation errors
        - 401: Unauthorized
        - 404: Category not found
        """
)

// ❌ Bad
@Operation(
    summary = "This endpoint creates a new product in the database if the user is admin and all validations pass",
    description = "Create product"
)
```

### 6.3. Response Documentation

**Always document:**
- ✅ 200 Success responses
- ✅ 400 Bad Request (validation errors)
- ✅ 401 Unauthorized (missing/invalid auth)
- ✅ 403 Forbidden (insufficient permissions)
- ✅ 404 Not Found (resource doesn't exist)
- ✅ 500 Internal Server Error

**Example:**
```java
@ApiResponses(value = {
    @ApiResponse(
        responseCode = "200",
        description = "Product created successfully",
        content = @Content(schema = @Schema(implementation = Product.class))
    ),
    @ApiResponse(
        responseCode = "400",
        description = "Validation errors",
        content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    ),
    @ApiResponse(
        responseCode = "401",
        description = "JWT token missing or invalid"
    ),
    @ApiResponse(
        responseCode = "403",
        description = "User does not have ADMIN role"
    )
})
```

### 6.4. Security Documentation

```java
// Public endpoint
@Operation(
    summary = "Get all products (Public)",
    security = {}  // No auth required
)

// Protected endpoint
@Operation(
    summary = "Create product (Admin only)",
    security = @SecurityRequirement(name = "bearerAuth")
)

// Multiple auth options
@Operation(
    summary = "Get user profile",
    security = {
        @SecurityRequirement(name = "bearerAuth"),
        @SecurityRequirement(name = "basicAuth")
    }
)
```

### 6.5. Versioning

**URL Versioning:**
```java
@RequestMapping("/api/v1/products")  // Version 1
@RequestMapping("/api/v2/products")  // Version 2
```

**Header Versioning:**
```java
@Operation(
    summary = "Get products",
    parameters = @Parameter(
        name = "API-Version",
        in = ParameterIn.HEADER,
        schema = @Schema(type = "string", allowableValues = {"1", "2"})
    )
)
```

---

## 7. Examples

### 7.1. Complete Controller Example

```java
@RestController
@RequestMapping("/api/admin/products")
@RequiredArgsConstructor
@Tag(name = "Admin - Products", description = "Product management for administrators")
@SecurityRequirement(name = "bearerAuth")  // All endpoints require auth
public class AdminProductRestController {

    private final ProductService productService;

    @Operation(
        summary = "Get all products",
        description = """
            Retrieve all products including inactive ones.
            Only accessible by administrators.
            """
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Products retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = Product.class))
            )
        ),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden - Admin role required")
    })
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @Operation(
        summary = "Create new product",
        description = """
            Create a new product in the system.

            Validations:
            - Name: required, 2-200 characters
            - Price: required, must be > 0
            - Stock: required, must be >= 0
            - Category: required, must exist
            """
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Product created successfully",
            content = @Content(schema = @Schema(implementation = Product.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Validation errors",
            content = @Content(schema = @Schema(implementation = Map.class))
        ),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @PostMapping
    public ResponseEntity<?> createProduct(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Product data to create",
            required = true,
            content = @Content(
                schema = @Schema(implementation = Product.class),
                examples = @ExampleObject(
                    name = "Product Example",
                    value = """
                        {
                          "name": "Tã Pampers Premium",
                          "description": "Tã cao cấp cho bé",
                          "price": 299000,
                          "stockQuantity": 100,
                          "imageUrl": "https://example.com/image.jpg",
                          "active": true,
                          "category": {
                            "id": 1
                          }
                        }
                        """
                )
            )
        )
        @Valid @RequestBody Product product
    ) {
        try {
            Product created = productService.createProduct(product);
            return ResponseEntity.ok(created);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }

    @Operation(
        summary = "Update product",
        description = "Update an existing product by ID"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Product updated successfully"),
        @ApiResponse(responseCode = "400", description = "Validation errors"),
        @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(
        @Parameter(description = "Product ID", required = true, example = "1")
        @PathVariable Long id,
        @Valid @RequestBody Product product
    ) {
        try {
            Product updated = productService.updateProduct(id, product);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }

    @Operation(
        summary = "Delete product",
        description = """
            Delete a product by ID.
            Note: Products in orders cannot be deleted.
            """
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Product deleted successfully"),
        @ApiResponse(responseCode = "400", description = "Cannot delete - product in orders"),
        @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(
        @Parameter(description = "Product ID", required = true)
        @PathVariable Long id
    ) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.ok(Map.of("message", "Product deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }
}
```

### 7.2. Complete DTO Example

```java
@Data
@Schema(description = "User registration request")
public class RegisterDTO {

    @Schema(
        description = "User's full name",
        example = "Nguyen Van A",
        required = true,
        minLength = 3,
        maxLength = 100
    )
    @NotBlank(message = "Full name is required")
    @Size(min = 3, max = 100)
    private String fullName;

    @Schema(
        description = "User's email address",
        example = "user@example.com",
        required = true,
        format = "email"
    )
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;

    @Schema(
        description = "Password (minimum 6 characters)",
        example = "password123",
        required = true,
        minLength = 6,
        format = "password"
    )
    @NotBlank(message = "Password is required")
    @Size(min = 6)
    private String password;

    @Schema(
        description = "Password confirmation (must match password)",
        example = "password123",
        required = true,
        format = "password"
    )
    @NotBlank(message = "Confirm password is required")
    private String confirmPassword;

    @Schema(
        description = "Phone number (optional)",
        example = "0901234567",
        pattern = "^0\\d{9}$"
    )
    private String phone;

    @Schema(
        description = "User's address (optional)",
        example = "123 Main Street, District 1, HCMC"
    )
    private String address;
}
```

---

## 8. Cheat Sheet

### Quick Reference

| Annotation | Level | Purpose |
|------------|-------|---------|
| `@OpenAPIDefinition` | Application | Configure API info |
| `@SecurityScheme` | Application | Define auth scheme |
| `@Tag` | Controller | Group endpoints |
| `@Operation` | Method | Document endpoint |
| `@ApiResponses` | Method | List possible responses |
| `@Parameter` | Parameter | Document parameter |
| `@RequestBody` | Parameter | Document request body |
| `@Schema` | DTO/Entity | Document model |
| `@SecurityRequirement` | Method | Require authentication |

### Common Patterns

**Public Endpoint:**
```java
@Operation(summary = "...", security = {})
@GetMapping
public ResponseEntity<?> publicMethod() { }
```

**Auth Required:**
```java
@Operation(
    summary = "...",
    security = @SecurityRequirement(name = "bearerAuth")
)
@PostMapping
public ResponseEntity<?> protectedMethod() { }
```

**With Examples:**
```java
@Operation(
    summary = "...",
    requestBody = @RequestBody(
        content = @Content(
            examples = @ExampleObject(value = "{...}")
        )
    )
)
```

---

## 9. Troubleshooting

### Swagger UI không hiển thị

**Check:**
1. Application đã start chưa?
2. Port có đúng 8081 không?
3. URL có đúng `/swagger-ui.html` không?

**Fix:**
```bash
# Check logs
./mvnw spring-boot:run | grep -i swagger

# Expected output:
# Swagger UI: http://localhost:8081/swagger-ui.html
```

### Endpoints không xuất hiện trong Swagger

**Nguyên nhân:**
- Controller không có `@RestController`
- Method không có `@GetMapping/@PostMapping/...`
- Controller trong package không được scan

**Fix:**
```java
// Ensure controller is in correct package
package iuh.student.www.controller.rest;

@RestController  // Must have this
@RequestMapping("/api/...")
public class MyController {

    @GetMapping  // Must have this
    public ResponseEntity<?> method() { }
}
```

### Authentication không hoạt động

**Check:**
1. Đã login và lấy token chưa?
2. Token có "Bearer " prefix chưa?
3. Token còn hạn không? (24h)

**Fix:**
```
Authorize: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
           ^^^^^^ Cần có chữ "Bearer " và space
```

---

## 10. Báo Cáo cho Giáo Viên

### Nội dung cần nộp:

1. **Swagger UI Screenshots:**
   - Dashboard overview
   - Authentication endpoints
   - Product endpoints
   - Test một vài endpoints

2. **OpenAPI JSON:**
```bash
curl http://localhost:8081/v3/api-docs > docs/openapi.json
```

3. **Test Results:**
   - Screenshot test thành công
   - Screenshot test error cases
   - cURL commands

4. **Documentation Examples:**
   - Show controller với annotations
   - Show DTO với @Schema
   - Show API responses

### Template báo cáo:

```markdown
# Báo Cáo API Documentation với Swagger

## 1. Tổng Quan
- Tổng số endpoints: 25+
- Authentication: JWT Bearer Token
- API Version: 1.0

## 2. API Groups
- Authentication (3 endpoints)
- Guest - Products (4 endpoints)
- Admin - Products (5 endpoints)
- Customer - Orders (6 endpoints)
...

## 3. Screenshots
[Screenshot Swagger UI]
[Screenshot Test Login]
[Screenshot Test Create Product]

## 4. Code Documentation
[Code examples với annotations]

## 5. Export
- openapi.json: ✅
- Postman collection: ✅
```

---

## ✅ Summary

**Bạn đã học:**
- ✅ Cách truy cập Swagger UI
- ✅ Cách test API với Swagger
- ✅ Cách ghi tài liệu với annotations
- ✅ Cách export documentation
- ✅ Best practices cho API documentation

**Next Steps:**
1. Mở Swagger UI: `http://localhost:8081/swagger-ui.html`
2. Test các endpoints
3. Export OpenAPI JSON
4. Screenshot để nộp báo cáo

**Tài liệu tham khảo:**
- [Springdoc OpenAPI](https://springdoc.org/)
- [OpenAPI Specification](https://swagger.io/specification/)
- [Swagger Editor](https://editor.swagger.io/)

Chúc bạn thành công! 🚀
