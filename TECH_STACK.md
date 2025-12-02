# TECH STACK - DỰ ÁN SHOPPINGMEVABE

## Tổng quan dự án
**ShoppingMomadnBaby** là một hệ thống thương mại điện tử (E-commerce) bán hàng mẹ và bé, được xây dựng bằng Java Spring Boot với kiến trúc MVC và RESTful API.

---

## 1. NGÔN NGỮ LẬP TRÌNH

### Java 17
- **Phiên bản**: Java 17 (LTS - Long Term Support)
- **Vai trò**: Ngôn ngữ lập trình chính của backend
- **Đặc điểm**:
  - Hỗ trợ các tính năng hiện đại: Records, Pattern Matching, Text Blocks
  - Performance tốt và bảo mật cao
  - Hệ sinh thái thư viện phong phú

---

## 2. FRAMEWORK & LIBRARIES

### 2.1. Spring Boot 3.2.5
**Core Framework** - Framework chính để xây dựng ứng dụng

#### Spring Boot Starters được sử dụng:

**a) Spring Boot Starter Web**
- **Mục đích**: Xây dựng REST API và web application
- **Bao gồm**:
  - Spring MVC (Model-View-Controller)
  - Embedded Tomcat Server
  - Jackson (JSON serialization)
- **Cấu hình**: Server chạy trên port 8081

**b) Spring Boot Starter Data JPA**
- **Mục đích**: Quản lý database và ORM (Object-Relational Mapping)
- **Bao gồm**:
  - Hibernate ORM
  - Spring Data repositories
  - Transaction management
- **Tính năng**:
  - Auto-generate database schema
  - CRUD operations tự động
  - Query methods từ tên method

**c) Spring Boot Starter Security**
- **Mục đích**: Bảo mật ứng dụng
- **Tính năng**:
  - Authentication (JWT-based)
  - Authorization (Role-based: ADMIN, CUSTOMER)
  - Password encryption (BCrypt)
  - CSRF protection
  - HTTP-only cookies

**d) Spring Boot Starter Validation**
- **Mục đích**: Validate dữ liệu input
- **Annotation hỗ trợ**: @NotNull, @Email, @Size, @Pattern, etc.

**e) Spring Boot Starter Mail**
- **Mục đích**: Gửi email
- **Cấu hình**: Gmail SMTP
- **Use cases**:
  - Email xác nhận đơn hàng
  - Email reset password
  - Email thông báo

**f) Spring Boot Starter Actuator**
- **Mục đích**: Monitoring và health check
- **Endpoints**: /actuator/health, /actuator/info, /actuator/metrics

**g) Spring Boot DevTools**
- **Mục đích**: Tăng tốc độ phát triển
- **Tính năng**:
  - Auto-reload khi code thay đổi
  - LiveReload browser integration
  - Development-only features

---

### 2.2. View Layer - JSP & JSTL

**Apache Tomcat Jasper**
- **Vai trò**: JSP (JavaServer Pages) engine
- **Mục đích**: Render dynamic web pages

**JSTL (Jakarta Standard Tag Library)**
- **Vai trò**: Tag library cho JSP
- **Tính năng**:
  - Core tags (c:if, c:forEach, c:out)
  - Format tags (fmt:formatDate, fmt:formatNumber)
  - Functions (fn:length, fn:substring)

**Spring Security JSP Taglib**
- **Vai trò**: Security tags trong JSP
- **Ví dụ**:
  ```jsp
  <sec:authorize access="hasRole('ADMIN')">
    Admin content here
  </sec:authorize>
  ```

**Cấu hình View**:
- Prefix: `/WEB-INF/views/`
- Suffix: `.jsp`

---

### 2.3. Database

#### Database Engines (Multi-database support)

**a) MariaDB (Primary - Production)**
- **Phiên bản**: Latest (via mariadb-java-client)
- **Connection URL**: `jdbc:mariadb://localhost:3306/shop_me_va_be`
- **Đặc điểm**:
  - Open-source fork của MySQL
  - Performance cao
  - Tương thích tốt với MySQL

**b) MySQL (Alternative)**
- **Driver**: mysql-connector-j
- **Vai trò**: Có thể thay thế MariaDB

**c) H2 Database (Development/Testing)**
- **Vai trò**: In-memory database cho testing
- **Đặc điểm**:
  - Không cần cài đặt
  - Fast và lightweight
  - Tốt cho unit tests

#### Connection Pooling - HikariCP
- **Maximum pool size**: 10 connections
- **Minimum idle**: 5 connections
- **Connection timeout**: 20 seconds
- **Idle timeout**: 5 minutes
- **Max lifetime**: 20 minutes

#### Hibernate ORM
- **Dialect**: MariaDBDialect
- **DDL auto**: update (tự động cập nhật schema)
- **Show SQL**: true (hiển thị SQL trong logs)
- **Format SQL**: true (format SQL dễ đọc)
- **Batch operations**: Enabled (batch size = 20)
- **Timezone**: Asia/Ho_Chi_Minh

---

### 2.4. Security & Authentication

#### JWT (JSON Web Token)
**Libraries**:
- `io.jsonwebtoken:jjwt-api:0.12.3`
- `io.jsonwebtoken:jjwt-impl:0.12.3`
- `io.jsonwebtoken:jjwt-jackson:0.12.3`

**Cấu hình**:
- Secret key: 256-bit
- Token expiration: 24 giờ (86400000 ms)
- Token type: Bearer
- Storage: HTTP-only cookies (chống XSS attacks)

**Flow**:
1. User login → Server tạo JWT token
2. Token lưu trong HTTP-only cookie
3. Mọi request gửi kèm token
4. Server verify token và authorize

**BCrypt Password Hashing**
- Algorithm: BCrypt (via Spring Security)
- Strength: Default (10 rounds)
- Salt: Auto-generated per password

---

### 2.5. API Documentation

#### Springdoc OpenAPI
**Library**: `org.springdoc:springdoc-openapi-starter-webmvc-ui:2.6.0`

**Tính năng**:
- Auto-generate API documentation
- Swagger UI interface
- OpenAPI 3.0 specification
- Interactive API testing

**Access**: `http://localhost:8081/swagger-ui.html`

---

### 2.6. Image Upload & Storage

#### Cloudinary
**Library**: `com.cloudinary:cloudinary-http44:1.36.0`

**Tính năng**:
- Cloud-based image storage
- Image optimization
- CDN delivery
- Image transformation API

**Cấu hình**:
- Cloud name: dubthm5m6
- Upload folder: products
- Max file size: 5MB

**Alternative**: Local file storage
- Directory: `src/main/resources/static/uploads/products`

---

### 2.7. Payment Integration

#### MoMo Payment Gateway
**API Version**: v2

**Cấu hình**:
- Endpoint: `https://test-payment.momo.vn/v2/gateway/api/create`
- Partner code: MOMO (test)
- Request type: captureWallet

**Flow**:
1. Tạo order → Generate payment request
2. Redirect user to MoMo payment page
3. User thanh toán qua QR code hoặc thẻ
4. MoMo callback về hệ thống
5. Update order status và payment status

**Supported methods**:
- QR code scan
- Linked bank account
- Credit/Debit card

---

### 2.8. Utility Libraries

#### Project Lombok
**Library**: `org.projectlombok:lombok`

**Tính năng**:
- `@Data`: Auto-generate getters, setters, toString, equals, hashCode
- `@NoArgsConstructor`: Constructor không tham số
- `@AllArgsConstructor`: Constructor đầy đủ tham số
- `@Builder`: Builder pattern
- `@Slf4j`: Logger injection

**Lợi ích**: Giảm boilerplate code, code sạch hơn

---

## 3. BUILD TOOL & DEPENDENCY MANAGEMENT

### Apache Maven
**Wrapper**: Maven Wrapper (mvnw, mvnw.cmd)

**Cấu hình**:
- Java version: 17
- Compiler source: 17
- Compiler target: 17

**Lifecycle**:
```bash
./mvnw clean           # Xóa target folder
./mvnw compile         # Compile source code
./mvnw test            # Chạy unit tests
./mvnw package         # Tạo JAR file
./mvnw spring-boot:run # Run application
```

**File cấu hình**: `pom.xml`

---

## 4. LOGGING

### SLF4J + Logback
**Framework**: SLF4J (Simple Logging Facade for Java)
**Implementation**: Logback (default của Spring Boot)

**Log levels được cấu hình**:
- Root: INFO
- Application (iuh.student.www): DEBUG
- Spring Security: DEBUG
- Spring Web: DEBUG
- Hibernate SQL: DEBUG
- SQL parameters: TRACE

**Output**: Console (development), File (production)

---

## 5. TESTING FRAMEWORK

### Spring Boot Test Starter
**Bao gồm**:
- **JUnit 5 (Jupiter)**: Testing framework chính
- **Mockito**: Mocking framework
- **AssertJ**: Fluent assertions
- **Spring Test**: Spring context testing
- **Hamcrest**: Matchers

### Spring Security Test
**Tính năng**:
- `@WithMockUser`: Test với user giả
- `@WithUserDetails`: Test với user thật từ DB
- Security annotations testing

---

## 6. CONFIGURATION MANAGEMENT

### Application Properties
**File**: `src/main/resources/application.properties`

**Sections**:
1. Server configuration
2. Database configuration
3. JPA/Hibernate configuration
4. JSP configuration
5. Email configuration
6. MoMo payment configuration
7. JWT configuration
8. Logging configuration
9. Security configuration
10. Actuator configuration
11. Performance optimization
12. Cloudinary configuration
13. Error handling

**Environment**: Development (có thể override bằng profiles)

---

## 7. KIẾN TRÚC ỨNG DỤNG

### 7.1. Architectural Pattern

**MVC (Model-View-Controller)**
- **Model**: Entity classes (JPA entities)
- **View**: JSP pages
- **Controller**: Spring MVC Controllers

**Layered Architecture**:
```
┌─────────────────────────┐
│   Presentation Layer    │  ← Controllers, JSP
├─────────────────────────┤
│   Service Layer         │  ← Business logic
├─────────────────────────┤
│   Repository Layer      │  ← Data access (JPA)
├─────────────────────────┤
│   Database Layer        │  ← MariaDB
└─────────────────────────┘
```

### 7.2. RESTful API Design

**API Endpoints**:
- `/api/auth/*` - Authentication APIs (public)
- `/api/products/*` - Product APIs (public)
- `/api/orders/*` - Order APIs (authenticated)
- `/api/admin/*` - Admin APIs (admin only)

**HTTP Methods**:
- GET: Lấy dữ liệu
- POST: Tạo mới
- PUT: Cập nhật
- DELETE: Xóa

**Response format**: JSON

---

## 8. PERFORMANCE OPTIMIZATION

### 8.1. Caching
**Static resources caching**: 1 hour (3600s)

### 8.2. Compression
**Enabled for**:
- text/html
- text/xml
- text/css
- text/javascript
- application/javascript
- application/json

### 8.3. Database Optimization
- Connection pooling (HikariCP)
- Batch operations (batch size = 20)
- Query optimization
- Index usage (via JPA annotations)

### 8.4. Session Management
**Session timeout**: 30 minutes
**Stateless authentication**: JWT (không dùng server-side session)

---

## 9. DEPLOYMENT

### 9.1. Embedded Server
**Tomcat**: Embedded trong Spring Boot
**Port**: 8081

### 9.2. Packaging
**Format**: JAR (executable)
**Command**: `java -jar target/www-0.0.1-SNAPSHOT.jar`

### 9.3. Ngrok (Development)
**Purpose**: Public URL cho development
**URL**: `https://presophomore-adjunctly-margery.ngrok-free.dev`
**Use case**: MoMo callback URL testing

---

## 10. DEVELOPMENT TOOLS

### 10.1. Recommended IDEs
- **IntelliJ IDEA** (recommended)
- **Eclipse**
- **VS Code** (with Spring Boot extensions)

### 10.2. Database Tools
- **HeidiSQL** (Windows)
- **MySQL Workbench**
- **DBeaver**
- **phpMyAdmin**

### 10.3. API Testing
- **Postman**
- **Insomnia**
- **Swagger UI** (built-in)
- **cURL**

---

## 11. VERSION CONTROL

### Git
**Repository structure**:
```
.git/
.gitignore
.gitattributes
```

**Branch strategy**: Feature branches

---

## 12. TÓM TẮT TECH STACK

| Category | Technology | Version |
|----------|-----------|---------|
| **Language** | Java | 17 |
| **Framework** | Spring Boot | 3.2.5 |
| **Security** | Spring Security + JWT | 0.12.3 |
| **ORM** | Hibernate (via JPA) | 6.x |
| **Database** | MariaDB | Latest |
| **View** | JSP + JSTL | 3.x |
| **Build Tool** | Maven | 3.x |
| **Server** | Embedded Tomcat | 10.x |
| **API Docs** | Springdoc OpenAPI | 2.6.0 |
| **Image Storage** | Cloudinary | 1.36.0 |
| **Payment** | MoMo Gateway | v2 |
| **Email** | Gmail SMTP | - |
| **Logging** | SLF4J + Logback | - |
| **Testing** | JUnit 5 + Mockito | - |
| **Utilities** | Lombok | Latest |

---

## 13. LỢI ÍCH CỦA TECH STACK NÀY

### Ưu điểm:

1. **Spring Boot Ecosystem**
   - Convention over configuration
   - Auto-configuration
   - Production-ready features
   - Large community support

2. **Security**
   - JWT stateless authentication
   - Role-based authorization
   - BCrypt password hashing
   - CSRF protection

3. **Scalability**
   - Stateless design (JWT)
   - Connection pooling
   - Batch operations
   - Caching support

4. **Developer Experience**
   - Hot reload (DevTools)
   - Lombok (less boilerplate)
   - Swagger UI (API testing)
   - Clear separation of concerns

5. **Modern Architecture**
   - RESTful API
   - MVC pattern
   - Layered architecture
   - Dependency injection

### Nhược điểm cần lưu ý:

1. **JSP**: Công nghệ khá cũ, nên cân nhắc thay bằng Thymeleaf hoặc frontend framework (React, Vue)
2. **Monolithic**: Chưa phải microservices, khó scale horizontal
3. **Email credentials**: Hardcoded trong properties, nên dùng environment variables

---

## 14. HỌC TẬP VÀ TÀI LIỆU

### Official Documentation:
- [Spring Boot Docs](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [Spring Security](https://docs.spring.io/spring-security/reference/)
- [Spring Data JPA](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/)
- [JWT.io](https://jwt.io/)
- [Cloudinary Docs](https://cloudinary.com/documentation)
- [MoMo API](https://developers.momo.vn/)

### Tutorials:
- Spring Boot official guides
- Baeldung Spring tutorials
- JavaBrains Spring Boot series

---

## KẾT LUẬN

Dự án **ShoppingMeVaBe** được xây dựng trên một tech stack hiện đại và robust, phù hợp cho một hệ thống thương mại điện tử quy mô vừa và nhỏ. Với Spring Boot ecosystem, ứng dụng có khả năng mở rộng tốt và dễ dàng maintain.

**Điểm mạnh chính**:
- Security tốt (JWT + Spring Security)
- RESTful API design chuẩn
- Database optimization với JPA/Hibernate
- Payment integration (MoMo)
- Cloud image storage (Cloudinary)
- API documentation tự động (Swagger)

**Gợi ý cải tiến**:
- Migrate từ JSP sang modern frontend framework
- Thêm Redis cache layer
- Implement microservices architecture nếu scale lớn
- Thêm Elasticsearch cho tìm kiếm sản phẩm
- Implement message queue (RabbitMQ/Kafka) cho async processing
