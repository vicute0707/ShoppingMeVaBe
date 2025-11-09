# Shopping Store - Ecommerce Multi-User System

Hệ thống thương mại điện tử đầy đủ với 3 loại người dùng: Guest, Customer, và Admin.

## Công nghệ sử dụng

- **Backend Framework**: Spring Boot 3.5.7
  - Spring MVC
  - Spring Data JPA
  - Spring Security
  - Spring Mail
- **Frontend**: JSP + JSTL + Bootstrap 5
- **Database**: H2 (in-memory)
- **ORM**: Hibernate
- **Validation**: Hibernate Validator (server-side) + jQuery (client-side)
- **Build Tool**: Maven

## Tính năng chính

### 1. Guest User (Chưa đăng nhập)
- ✅ Xem danh sách sản phẩm (từ CSDL)
- ✅ Xem chi tiết sản phẩm
- ✅ Thêm vào giỏ hàng (từ list hoặc detail)
- ✅ Xem giỏ hàng (lưu Session)
- ✅ Sửa số lượng (nếu = 0 → tự xóa)
- ✅ Xóa sản phẩm khỏi giỏ
- ✅ Đăng ký tài khoản (email validation + gửi email xác nhận)

### 2. Customer User (Đã đăng nhập)
Tất cả chức năng Guest +
- ✅ Thanh toán (checkout)
- ✅ Lưu orders + order_detail vào CSDL
- ✅ Gửi email hóa đơn
- ✅ Xóa Session giỏ hàng sau checkout
- ✅ Xem lịch sử đơn hàng
- ✅ Xem chi tiết đơn hàng

### 3. Admin User
Tất cả chức năng Customer + PHẦN BACK-END:
- ✅ Đăng nhập riêng (có phân quyền)
- ✅ **Quản lý danh mục (CRUD)**
  - Không xóa được nếu còn sản phẩm
- ✅ **Quản lý sản phẩm (CRUD + tìm kiếm)**
  - Không xóa được nếu đã có trong đơn hàng
- ✅ **Quản lý người dùng (xem, sửa, xóa)**
  - Chỉ xóa được nếu chưa đặt hàng
- ✅ **Quản lý đơn hàng**
  - Xem danh sách, xem chi tiết
  - Cập nhật trạng thái đơn hàng
  - Cập nhật số lượng sản phẩm trong đơn

## Yêu cầu hệ thống

- Java 17 hoặc cao hơn
- Maven 3.6+
- Port 8080 available

## Cài đặt và chạy

### 1. Clone repository

```bash
git clone https://github.com/vicute0707/ShoppingMeVaBe.git
cd ShoppingMeVaBe
```

### 2. Chuyển sang branch làm việc

```bash
git checkout claude/ecommerce-multi-user-system-011CUxRDLczXqbDFKKtfQEJ8
```

### 3. Build project

```bash
./mvnw clean install
```

hoặc trên Windows:

```bash
mvnw.cmd clean install
```

### 4. Chạy ứng dụng

```bash
./mvnw spring-boot:run
```

hoặc trên Windows:

```bash
mvnw.cmd spring-boot:run
```

### 5. Truy cập ứng dụng

- **Homepage**: http://localhost:8080
- **H2 Console**: http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:mem:shoppingdb`
  - Username: `sa`
  - Password: (để trống)

## Tài khoản đăng nhập mặc định

### Admin Account
- **Email**: admin@shopping.com
- **Password**: admin123

### Customer Accounts
1. **Email**: john@example.com
   - **Password**: password123

2. **Email**: jane@example.com
   - **Password**: password123

## Cấu trúc dự án

```
src/main/
├── java/iuh/student/www/
│   ├── config/              # Cấu hình (Security, Web, Data Initializer)
│   ├── controller/          # Controllers (Guest/Customer)
│   │   └── admin/          # Admin Controllers
│   ├── dto/                # Data Transfer Objects
│   ├── entity/             # JPA Entities
│   ├── model/              # Models (Cart, CartItem)
│   ├── repository/         # JPA Repositories
│   ├── security/           # Security configuration
│   └── service/            # Business Logic Services
├── resources/
│   ├── static/
│   │   ├── css/           # CSS files
│   │   └── js/            # JavaScript files (validation)
│   └── application.properties
└── webapp/WEB-INF/views/
    ├── admin/              # Admin JSP pages
    ├── customer/           # Customer JSP pages
    ├── guest/              # Guest JSP pages
    └── common/             # Common JSP components
```

## Database Schema

### Tables
1. **users** - Người dùng (ADMIN, CUSTOMER)
2. **categories** - Danh mục sản phẩm
3. **products** - Sản phẩm
4. **orders** - Đơn hàng
5. **order_details** - Chi tiết đơn hàng

### Relationships
- User 1-N Orders
- Category 1-N Products
- Product N-N Orders (through order_details)
- Order 1-N OrderDetails

## Ràng buộc và Validation

### Server-side (Hibernate Validator)
- Validation cho tất cả DTO và Entity
- Email format validation
- Length constraints
- Required field validation

### Client-side (jQuery)
- Real-time form validation
- Password confirmation matching
- Email format check
- Number range validation
- Dynamic error messages

### Business Rules
1. **Xóa sản phẩm**: Chỉ khi chưa có trong đơn hàng
2. **Xóa danh mục**: Chỉ khi không còn sản phẩm
3. **Xóa user**: Chỉ khi chưa đặt hàng
4. **Email unique**: Không trùng lặp email khi đăng ký

## Email Configuration

Để sử dụng tính năng gửi email, cập nhật file `application.properties`:

```properties
spring.mail.username=your-email@gmail.com
spring.mail.password=your-app-password
```

**Lưu ý**: Sử dụng App Password của Gmail, không phải password thông thường.

## Testing

### Test các chức năng

#### Guest User
1. Truy cập http://localhost:8080
2. Xem sản phẩm, thêm vào giỏ
3. Đăng ký tài khoản mới

#### Customer
1. Đăng nhập với customer account
2. Thêm sản phẩm vào giỏ
3. Checkout và xem đơn hàng

#### Admin
1. Đăng nhập với admin account
2. Truy cập http://localhost:8080/admin/dashboard
3. Test CRUD operations cho:
   - Categories
   - Products (có tìm kiếm)
   - Users
   - Orders (cập nhật status và quantity)

## Các tính năng kỹ thuật đặc biệt

1. **Session-based Cart**: Giỏ hàng được lưu trong Session, không lưu DB
2. **Email Notifications**:
   - Email chào mừng khi đăng ký
   - Email hóa đơn khi đặt hàng thành công
3. **Security**:
   - Role-based access control (ADMIN, CUSTOMER)
   - Password encryption (BCrypt)
   - CSRF protection
4. **Responsive UI**: Bootstrap 5 responsive design
5. **Auto data initialization**: Tự động tạo dữ liệu mẫu khi khởi động

## Troubleshooting

### Port 8080 đã được sử dụng
Thay đổi port trong `application.properties`:
```properties
server.port=8081
```

### Email không gửi được
- Kiểm tra email configuration trong `application.properties`
- Sử dụng Gmail App Password
- Cho phép "Less secure app access" (nếu cần)

### Database lỗi
- H2 database là in-memory, sẽ reset khi restart app
- Kiểm tra H2 Console: http://localhost:8080/h2-console

## License

This project is created for educational purposes.

## Author

Developed by Student - IUH University

---

**Note**: Đây là project demo cho mục đích học tập. Không sử dụng cho production environment mà không có thêm security hardening và optimization.
