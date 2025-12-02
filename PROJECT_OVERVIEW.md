# ShoppingMeVaBe - Comprehensive Project Overview

## Project Information
- **Project Name**: ShoppingMeVaBe (Shop Mẹ và Bé - Mom & Baby Store)
- **Framework**: Spring Boot 3.2.5
- **Java Version**: Java 17
- **Build Tool**: Maven
- **Language**: Vietnamese
- **Database**: MariaDB/MySQL
- **Frontend**: JSP with Bootstrap + Pastel Theme CSS
- **Server Port**: 8081

---

## 1. PACKAGE STRUCTURE

### Core Java Packages
Located at: `src/main/java/iuh/student/www/`

#### Package Organization:
```
iuh.student.www/
├── ShoppingMomadnBabyApplication.java (Main class)
├── controller/
│   ├── AuthController.java
│   ├── CartController.java
│   ├── CheckoutController.java
│   ├── HomeController.java
│   ├── PaymentController.java
│   ├── admin/
│   │   ├── AdminCategoryController.java
│   │   ├── AdminDashboardController.java
│   │   ├── AdminOrderController.java
│   │   ├── AdminProductController.java
│   │   └── AdminUserController.java
│   └── rest/
│       ├── AuthRestController.java
│       ├── CartRestController.java
│       ├── CategoryRestController.java
│       ├── OrderRestController.java
│       ├── ProductRestController.java
│       └── admin/
│           ├── AdminCategoryRestController.java
│           ├── AdminOrderRestController.java
│           ├── AdminProductRestController.java
│           └── AdminUserRestController.java
├── entity/ (JPA Entities)
│   ├── User.java
│   ├── Product.java
│   ├── Category.java
│   ├── Order.java
│   └── OrderDetail.java
├── dto/ (Data Transfer Objects)
│   ├── AuthResponse.java
│   ├── CheckoutDTO.java
│   ├── LoginDTO.java
│   ├── LoginRequest.java
│   ├── RegisterDTO.java
│   ├── MoMoCallbackResponse.java
│   ├── MoMoPaymentRequest.java
│   └── MoMoPaymentResponse.java
├── model/ (Session Models)
│   ├── Cart.java
│   └── CartItem.java
├── repository/ (Data Access Layer)
│   ├── UserRepository.java
│   ├── ProductRepository.java
│   ├── CategoryRepository.java
│   ├── OrderRepository.java
│   └── OrderDetailRepository.java
├── service/ (Business Logic)
│   ├── UserService.java
│   ├── ProductService.java
│   ├── CategoryService.java
│   ├── OrderService.java
│   ├── EmailService.java
│   ├── MoMoService.java
│   ├── CloudinaryService.java
│   └── FileStorageService.java
├── security/ (Authentication & Authorization)
│   ├── JwtUtil.java
│   ├── JwtAuthenticationFilter.java
│   ├── CustomUserDetailsService.java
│   ├── CustomLogoutHandler.java
└── config/ (Configuration)
    ├── SecurityConfig.java
    ├── WebConfig.java
    ├── CloudinaryConfig.java
    ├── MoMoConfig.java
    ├── DataInitializer.java
    └── OpenAPIConfig.java
```

---

## 2. CORE FEATURES IMPLEMENTED

### A. User Management
- **Registration**: Self-service user registration with validation
  - Email validation
  - Password confirmation
  - BCrypt password hashing
  - Auto-enable upon registration
  - Welcome email sent
  
- **Authentication**:
  - JWT-based stateless authentication
  - Login with email/password
  - Role-based access control (CUSTOMER, ADMIN)
  - Secure password comparison
  - Custom logout handling
  - Cookie-based JWT storage
  
- **Authorization**:
  - @EnableMethodSecurity for method-level authorization
  - Role-based endpoint security
  - User ownership verification for orders
  - Admin-only dashboard and management pages

### B. Product Management (Admin)
- Create/Read/Update/Delete products
- Product categories
- Product images via Cloudinary
- Stock quantity management
- Active/Inactive status toggle
- Search functionality (by name/description)
- Price and description management
- Prevent deletion of products in orders (soft delete via deactivation)

### C. Category Management (Admin)
- Create/Read/Update/Delete categories
- Category listing
- Active categories for browsing
- Prevent duplicate category names

### E-Commerce Features:
- **Shopping Cart**:
  - Session-based cart (serializable)
  - Add/Remove/Update items
  - Quantity management
  - Cart total calculation
  - Cart persistence in session
  
- **Checkout**:
  - Pre-fill user information
  - Validate cart before checkout
  - Shipping address and phone
  - Order notes
  - Payment method selection
  
- **Order Management**:
  - Create orders with order details
  - Stock deduction on order creation
  - Order status tracking (PENDING, PROCESSING, SHIPPED, DELIVERED, CANCELLED)
  - View order history (user-specific)
  - Order confirmation emails
  - Admin order management dashboard

### D. Payment Integration
- **MoMo Mobile Wallet**:
  - Payment gateway integration
  - HMAC SHA256 signature generation
  - IPN (Instant Payment Notification) handling
  - Callback URL handling
  - Order status update on payment success
  - Secure payment verification
  
- **Payment Methods**:
  - COD (Cash on Delivery)
  - MoMo E-wallet
  - Order status: PENDING → PROCESSING → SHIPPED

### E. Email System
- **Welcome Email**: Sent upon user registration
- **Order Confirmation**: Detailed order summary with:
  - Order details (ID, date, status)
  - Shipping information
  - Order items with pricing
  - Total amount
  - Vietnamese currency formatting

### F. Admin Dashboard
- Statistics dashboard showing:
  - Total users
  - Total products
  - Total categories
  - Total orders
  
- Management panels for:
  - Users (view, edit, toggle status)
  - Products (CRUD with image upload)
  - Categories (CRUD)
  - Orders (view details, update status)

---

## 3. DATABASE SCHEMA

### Entities and Relationships

#### **Users Table**
```
users:
- id (PK)
- fullName (VARCHAR 100)
- email (VARCHAR 100, UNIQUE)
- password (hashed, BCrypt)
- phone (VARCHAR 15)
- address (VARCHAR 255)
- role (ENUM: CUSTOMER, ADMIN)
- enabled (BOOLEAN)
- createdAt (TIMESTAMP)
- updatedAt (TIMESTAMP)
- orders (ONE-TO-MANY relationship)
```

#### **Categories Table**
```
categories:
- id (PK)
- name (VARCHAR 100, UNIQUE)
- description (VARCHAR 500)
- active (BOOLEAN)
- createdAt (TIMESTAMP)
- updatedAt (TIMESTAMP)
- products (ONE-TO-MANY relationship)
```

#### **Products Table**
```
products:
- id (PK)
- name (VARCHAR 200)
- description (VARCHAR 2000)
- price (DOUBLE)
- stockQuantity (INTEGER)
- imageUrl (VARCHAR 500) - Cloudinary URL
- active (BOOLEAN)
- category_id (FK) - References categories.id
- createdAt (TIMESTAMP)
- updatedAt (TIMESTAMP)
- orderDetails (ONE-TO-MANY relationship)
```

#### **Orders Table**
```
orders:
- id (PK)
- user_id (FK) - References users.id
- orderDate (TIMESTAMP)
- totalAmount (DOUBLE)
- status (ENUM: PENDING, PROCESSING, SHIPPED, DELIVERED, CANCELLED)
- shippingAddress (VARCHAR 255)
- phone (VARCHAR 15)
- notes (VARCHAR 500)
- paymentMethod (VARCHAR 50)
- paymentStatus (VARCHAR 20)
- transactionId (VARCHAR 100)
- createdAt (TIMESTAMP)
- updatedAt (TIMESTAMP)
- orderDetails (ONE-TO-MANY relationship)
```

#### **OrderDetails Table**
```
order_details:
- id (PK)
- order_id (FK) - References orders.id
- product_id (FK) - References products.id
- quantity (INTEGER)
- unitPrice (DOUBLE)
- subtotal (DOUBLE)
```

### Relationships
- User → Orders (1:N)
- Category → Products (1:N)
- Product → OrderDetails (1:N)
- Order → OrderDetails (1:N)

### Database Configuration
- **Type**: MariaDB
- **Host**: localhost:3306
- **Database**: shop_me_va_be
- **Connection Pool**: HikariCP (max 10 connections)
- **Dialect**: MariaDBDialect
- **DDL Auto**: update (development mode)
- **Timezone**: Asia/Ho_Chi_Minh

---

## 4. FRONTEND TECHNOLOGIES

### View Layer (JSP)
Located at: `src/main/webapp/WEB-INF/views/`

#### Directory Structure:
```
views/
├── common/
│   ├── header.jsp (Navigation & branding)
│   ├── footer.jsp
│   └── access-denied.jsp
├── guest/
│   ├── home.jsp (Homepage)
│   ├── login.jsp (Login form)
│   ├── register.jsp (Registration form)
│   ├── products.jsp (Product listing)
│   ├── product-detail.jsp (Product details)
│   ├── cart.jsp (Shopping cart)
│   └── payment/
│       └── payment-methods.jsp
├── customer/
│   ├── checkout.jsp (Checkout process)
│   ├── orders.jsp (Order history)
│   └── order-detail.jsp (Order details)
└── admin/
    ├── dashboard.jsp (Admin dashboard)
    ├── products/
    │   ├── list.jsp
    │   └── form.jsp
    ├── categories/
    │   ├── list.jsp
    │   └── form.jsp
    ├── users/
    │   ├── list.jsp
    │   ├── form.jsp
    │   └── detail.jsp
    └── orders/
        ├── list.jsp
        └── detail.jsp
```

**Total JSP Lines**: ~1,298 lines of code

#### Key JSP Features:
- JSTL Core tags (`<c:if>`, `<c:forEach>`, `<c:choose>`)
- JSTL Format tags (`<fmt:formatNumber>` for currency)
- JSP include mechanism for component reuse
- Spring Security taglib for authorization checks
- Form validation attributes
- CSRF token integration

### CSS Styling
Located at: `src/main/resources/static/css/`

#### Files:
1. **pastel-theme.css** (Custom theme - ~50+ lines)
   - Color Palette:
     - Pastel Pink: #FFD1DC
     - Pastel Purple: #E0BBE4
     - Pastel Blue: #A7C7E7
     - Pastel Yellow: #FFF9A5
     - Pastel Green: #B5EAD7
     - Pastel Peach: #FFDAB9
     - Pastel Mint: #C1E1C1
   - Gradient backgrounds
   - Rounded buttons with shadow effects
   - Font: Segoe UI, Comic Sans MS with emoji support
   - Cute design elements (sparkles, emojis)
   - Responsive card layouts
   - Button hover animations

2. **style.css** (Additional custom styles)

### JavaScript
Located at: `src/main/resources/static/js/`

#### Files:
1. **validation.js**
   - jQuery-based form validation
   - Required field validation
   - Email format validation
   - Real-time validation feedback
   - Bootstrap validation classes

### Frontend Framework
- **Bootstrap 5** (from CDN, likely)
- **jQuery** (for client-side validation)
- **Font Awesome** (for icons)
- **Custom CSS** with pastel theme

### Frontend Features:
- Responsive grid layout
- Product cards with images
- Navigation bar with links
- Form validation feedback
- Status badges for orders
- Pricing display with Vietnamese formatting
- Stock quantity indicators
- Interactive buttons with hover effects

---

## 5. SECURITY IMPLEMENTATION

### Authentication System
- **JWT (JSON Web Tokens)**:
  - Token generation using JJWT library (v0.12.3)
  - HMAC-SHA256 signing
  - 24-hour expiration
  - Token storage in HTTP-only cookies
  - Token validation on each request

- **Password Handling**:
  - BCrypt hashing (Spring Security)
  - Secure comparison
  - Minimum 6 characters
  - Auto-hashing during registration

### Authorization System
- **Role-Based Access Control (RBAC)**:
  - Two roles: CUSTOMER, ADMIN
  - Method-level security (@EnableMethodSecurity)
  - Endpoint-level security
  - Role-specific views and APIs

- **Endpoint Security**:
  ```
  Public Endpoints:
  - /login, /register, /logout
  - /products, /categories
  - /api/auth/**, /api/public/**
  - /payment/momo/callback, /payment/momo/ipn
  
  Customer Endpoints (CUSTOMER, ADMIN):
  - /checkout, /checkout/**
  - /orders, /orders/**
  - /api/customer/**, /api/orders/**
  
  Admin Endpoints (ADMIN only):
  - /admin/**, /api/admin/**
  ```

### Security Filters
- **JwtAuthenticationFilter**:
  - Intercepts all requests
  - Extracts JWT from Authorization header or JWT_TOKEN cookie
  - Validates token signature and expiration
  - Sets SecurityContext for authenticated users
  - Gracefully handles invalid tokens
  - Skips authentication for public endpoints

- **CustomLogoutHandler**:
  - Properly deletes JWT_TOKEN cookie
  - Invalidates HTTP session
  - Clears SecurityContext
  - Sets cache control headers

### Security Configuration
- **CSRF Protection**:
  - Disabled for JWT (stateless)
  - Enabled for form-based endpoints
  
- **Session Management**:
  - STATELESS mode (JWT-based)
  - No server-side session storage
  - Session timeout: 30 minutes
  
- **CORS** (if configured):
  - OpenAPI/Swagger endpoints permitted
  - Public API endpoints permitted

### Additional Security
- **Input Validation**:
  - Bean validation (@Valid)
  - Custom validation in services
  - DTO-level constraints
  
- **Data Protection**:
  - User ownership verification for orders
  - Admin-only operations protected
  - Sensitive data not exposed in responses

- **MoMo Payment Security**:
  - HMAC SHA256 signature verification
  - Request/callback validation
  - Order ID extraction validation

---

## 6. PAYMENT INTEGRATION

### MoMo Payment Gateway
- **Integration Type**: Server-to-Server API
- **Environment**: Test (development)
- **Configuration** (`MoMoConfig`):
  ```
  Endpoint: https://test-payment.momo.vn/v2/gateway/api/create
  Partner Code: MOMO
  Access Key: F8BBA842ECF85
  Secret Key: K951B6PE1waDMi640xX08PD3vg6EkVlz
  Request Type: captureWallet
  ```

### Payment Flow
1. **Create Payment**:
   - GET `/payment/momo/create/{orderId}`
   - Verify order ownership
   - Generate HMAC SHA256 signature
   - Call MoMo API
   - Redirect to MoMo payment page

2. **Callback Handling**:
   - GET `/payment/momo/callback`
   - Verify signature
   - Update order status to PROCESSING if successful
   - Set payment status to PAID
   - Store transaction ID

3. **IPN (Server Notification)**:
   - POST `/payment/momo/ipn`
   - Receive payment notification from MoMo
   - Verify signature and update order
   - Return status response

### Payment Methods
- **COD (Cash on Delivery)**:
  - Order status: PENDING
  - Payment Status: UNPAID
  - Manual verification by admin
  
- **MoMo E-wallet**:
  - Online payment
  - Order status: PROCESSING upon success
  - Payment Status: PAID

---

## 7. EMAIL FUNCTIONALITY

### Email Service (`EmailService`)
- **Provider**: Gmail SMTP
- **Configuration**:
  ```
  Host: smtp.gmail.com
  Port: 587
  Protocol: TLS
  Username: nguyenthituongvi2023@gmail.com
  ```

### Email Templates

#### 1. **Welcome Email**
- Sent upon user registration
- Contains:
  - Personalized greeting
  - Welcome message
  - List of store features
  - Professional HTML formatting

#### 2. **Order Confirmation Email**
- Sent upon order creation
- Contains:
  - Order ID and date
  - Order status
  - Shipping address and phone
  - Detailed order items table:
    - Product name
    - Quantity
    - Unit price
    - Subtotal
  - Total amount (Vietnamese currency format)
  - Shipping notification message

### Email Features
- HTML email templates
- UTF-8 encoding support
- Vietnamese currency formatting (₫)
- Error logging and handling
- MIME message helper for attachments

---

## 8. IMAGE UPLOAD & STORAGE

### Cloudinary Integration
- **Service**: CloudinaryService
- **Configuration** (`CloudinaryConfig`):
  ```
  Cloud Name: dubthm5m6
  API Key: 214531891487818
  API Secret: NKYTxvXdkPNSgNetWJduDEB0N84
  ```

### Image Upload Features
- Upload via Cloudinary CDN
- Automatic URL generation
- Product image storage
- Support for multiple formats
- File size limit: 5MB
- Request size limit: 5MB

### Usage
- Admin product management
- Product image display
- Responsive image sizing
- Fallback icon for missing images

---

## 9. API DOCUMENTATION

### Swagger/OpenAPI
- **Configuration**: OpenAPIConfig
- **Version**: springdoc-openapi 2.6.0
- **Access**: `/swagger-ui.html`, `/v3/api-docs`

### REST API Endpoints

#### **Authentication API** (`/api/auth`)
- POST `/api/auth/register` - User registration
- POST `/api/auth/login` - User login with JWT
- GET `/api/auth/verify` - Verify JWT token

#### **Product API** (`/api/products`, `/api/public/products`)
- GET `/api/products` - List all active products
- GET `/api/products/{id}` - Get product details
- GET `/api/products/search` - Search products
- GET `/api/products/category/{categoryId}` - Get category products

#### **Category API** (`/api/categories`)
- GET `/api/categories` - List categories
- GET `/api/categories/{id}` - Get category details

#### **Admin APIs** (`/api/admin/*`)
- Product management (CRUD)
- Category management (CRUD)
- User management (CRUD)
- Order management (CRUD, status update)

#### **Customer APIs** (`/api/customer/*`)
- Cart operations
- Order history
- Profile management

---

## 10. ADDITIONAL FEATURES

### Data Initialization
- **DataInitializer** (CommandLineRunner):
  - Auto-creates default admin account on startup
  - Email: admin@shopmevabe.com
  - Password: admin123
  - Auto-resets password on each startup

### Performance Optimization
- HikariCP connection pooling (max 10, min 5)
- Lazy loading for relationships
- Eager loading for order details
- SQL batching (batch size: 20)
- Static resource caching (1 hour)
- Compression enabled for web requests

### Monitoring & Logging
- Spring Boot Actuator enabled
- Health endpoint: `/actuator/health`
- Detailed logging for security and database
- Request/Response logging
- SQL query logging

### Error Handling
- Global exception handling
- Validation error messages
- User-friendly error pages
- Stack trace on_param for debugging

---

## 11. DEVELOPMENT & BUILD

### Technologies
- **Spring Boot**: 3.2.5
- **Spring Security**: JWT + Form-based
- **Spring Data JPA**: Hibernate ORM
- **Testing**: JUnit 5, Spring Test
- **Build**: Maven 3.9+
- **Java**: JDK 17+

### Maven Dependencies Highlights
- Spring Boot Web, Security, JPA, Mail
- JWT (JJWT) for token management
- Cloudinary for image upload
- Swagger/OpenAPI for documentation
- Lombok for code generation
- MariaDB & H2 drivers
- JSP support (Tomcat Jasper)

### Application Properties
- Server Port: 8081
- Base URL: https://presophomore-adjunctly-margery.ngrok-free.dev (development)
- JSP prefix: /WEB-INF/views/
- JSP suffix: .jsp

---

## 12. KEY DESIGN PATTERNS

### Architectural Patterns
- **MVC Pattern**: Controller → Service → Repository → Entity
- **DTO Pattern**: Data transfer objects for API communication
- **Repository Pattern**: Data access abstraction
- **Singleton Pattern**: Services, Utils, Config beans
- **Builder Pattern**: Entities with Lombok @Builder

### Security Patterns
- **JWT Pattern**: Stateless token-based authentication
- **Filter Pattern**: JwtAuthenticationFilter for request interception
- **Role-Based Access Control**: @PreAuthorize annotations
- **Principle of Least Privilege**: Specific role requirements

### Database Patterns
- **Lazy Loading**: Relationships defined with FetchType.LAZY
- **Eager Loading**: Explicit FETCH JOINs where needed
- **Optimistic Locking**: createdAt, updatedAt timestamps
- **Cascade Operations**: Automatic child deletion on parent removal

---

## 13. DEPLOYMENT & CONFIGURATION

### Environment-Specific
- Development: application.properties with debug logging
- Database: MariaDB (production-ready)
- Email: Gmail SMTP
- Payment: MoMo test environment

### Build & Run
```bash
# Maven build
mvn clean package

# Run application
java -jar target/www-0.0.1-SNAPSHOT.jar

# Or via Spring Boot Maven plugin
mvn spring-boot:run
```

---

## Summary

The **ShoppingMeVaBe** project is a comprehensive, production-ready e-commerce application built with Spring Boot that provides:

✓ Complete user authentication and authorization with JWT
✓ Full product and category management system
✓ Shopping cart and checkout functionality
✓ MoMo payment gateway integration
✓ Order management with email notifications
✓ Admin dashboard with statistics
✓ Responsive UI with pastel theme design
✓ RESTful API with Swagger documentation
✓ Secure data protection and validation
✓ Cloud-based image storage (Cloudinary)
✓ Professional email system

The application follows Spring Framework best practices, implements proper security measures, and provides a delightful user experience with its cute pastel design theme for a mom and baby product store.

