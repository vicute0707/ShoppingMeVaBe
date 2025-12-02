# ShoppingMeVaBe - Quick Reference Guide

## Key File Locations

### Core Application Files
- **Main Application**: `/home/user/ShoppingMeVaBe/src/main/java/iuh/student/www/ShoppingMomadnBabyApplication.java`
- **POM Configuration**: `/home/user/ShoppingMeVaBe/pom.xml`
- **Application Properties**: `/home/user/ShoppingMeVaBe/src/main/resources/application.properties`

### Authentication & Security
- **JWT Utility**: `/home/user/ShoppingMeVaBe/src/main/java/iuh/student/www/security/JwtUtil.java`
- **JWT Filter**: `/home/user/ShoppingMeVaBe/src/main/java/iuh/student/www/security/JwtAuthenticationFilter.java`
- **User Details Service**: `/home/user/ShoppingMeVaBe/src/main/java/iuh/student/www/security/CustomUserDetailsService.java`
- **Security Config**: `/home/user/ShoppingMeVaBe/src/main/java/iuh/student/www/config/SecurityConfig.java`

### User Management
- **Auth Controller**: `/home/user/ShoppingMeVaBe/src/main/java/iuh/student/www/controller/AuthController.java`
- **Auth REST Controller**: `/home/user/ShoppingMeVaBe/src/main/java/iuh/student/www/controller/rest/AuthRestController.java`
- **User Service**: `/home/user/ShoppingMeVaBe/src/main/java/iuh/student/www/service/UserService.java`
- **User Entity**: `/home/user/ShoppingMeVaBe/src/main/java/iuh/student/www/entity/User.java`
- **User Repository**: `/home/user/ShoppingMeVaBe/src/main/java/iuh/student/www/repository/UserRepository.java`

### E-Commerce Core
- **Product Controller**: `/home/user/ShoppingMeVaBe/src/main/java/iuh/student/www/controller/rest/ProductRestController.java`
- **Product Service**: `/home/user/ShoppingMeVaBe/src/main/java/iuh/student/www/service/ProductService.java`
- **Product Entity**: `/home/user/ShoppingMeVaBe/src/main/java/iuh/student/www/entity/Product.java`
- **Product Repository**: `/home/user/ShoppingMeVaBe/src/main/java/iuh/student/www/repository/ProductRepository.java`

- **Category Service**: `/home/user/ShoppingMeVaBe/src/main/java/iuh/student/www/service/CategoryService.java`
- **Category Entity**: `/home/user/ShoppingMeVaBe/src/main/java/iuh/student/www/entity/Category.java`

### Cart & Checkout
- **Cart Controller**: `/home/user/ShoppingMeVaBe/src/main/java/iuh/student/www/controller/CartController.java`
- **Cart Model**: `/home/user/ShoppingMeVaBe/src/main/java/iuh/student/www/model/Cart.java`
- **Cart Item Model**: `/home/user/ShoppingMeVaBe/src/main/java/iuh/student/www/model/CartItem.java`
- **Checkout Controller**: `/home/user/ShoppingMeVaBe/src/main/java/iuh/student/www/controller/CheckoutController.java`
- **Checkout DTO**: `/home/user/ShoppingMeVaBe/src/main/java/iuh/student/www/dto/CheckoutDTO.java`

### Orders
- **Order Controller**: `/home/user/ShoppingMeVaBe/src/main/java/iuh/student/www/controller/rest/OrderRestController.java`
- **Order Service**: `/home/user/ShoppingMeVaBe/src/main/java/iuh/student/www/service/OrderService.java`
- **Order Entity**: `/home/user/ShoppingMeVaBe/src/main/java/iuh/student/www/entity/Order.java`
- **Order Detail Entity**: `/home/user/ShoppingMeVaBe/src/main/java/iuh/student/www/entity/OrderDetail.java`
- **Order Repository**: `/home/user/ShoppingMeVaBe/src/main/java/iuh/student/www/repository/OrderRepository.java`

### Payment Integration
- **Payment Controller**: `/home/user/ShoppingMeVaBe/src/main/java/iuh/student/www/controller/PaymentController.java`
- **MoMo Service**: `/home/user/ShoppingMeVaBe/src/main/java/iuh/student/www/service/MoMoService.java`
- **MoMo Config**: `/home/user/ShoppingMeVaBe/src/main/java/iuh/student/www/config/MoMoConfig.java`
- **MoMo DTOs**: `/home/user/ShoppingMeVaBe/src/main/java/iuh/student/www/dto/Momo*.java`

### Email System
- **Email Service**: `/home/user/ShoppingMeVaBe/src/main/java/iuh/student/www/service/EmailService.java`

### Image Upload
- **Cloudinary Config**: `/home/user/ShoppingMeVaBe/src/main/java/iuh/student/www/config/CloudinaryConfig.java`
- **Cloudinary Service**: `/home/user/ShoppingMeVaBe/src/main/java/iuh/student/www/service/CloudinaryService.java`

### Admin Management
- **Admin Dashboard Controller**: `/home/user/ShoppingMeVaBe/src/main/java/iuh/student/www/controller/admin/AdminDashboardController.java`
- **Admin Product Controller**: `/home/user/ShoppingMeVaBe/src/main/java/iuh/student/www/controller/admin/AdminProductController.java`
- **Admin Category Controller**: `/home/user/ShoppingMeVaBe/src/main/java/iuh/student/www/controller/admin/AdminCategoryController.java`
- **Admin User Controller**: `/home/user/ShoppingMeVaBe/src/main/java/iuh/student/www/controller/admin/AdminUserController.java`
- **Admin Order Controller**: `/home/user/ShoppingMeVaBe/src/main/java/iuh/student/www/controller/admin/AdminOrderController.java`

### Frontend - JSP Views
- **Header/Footer**: `/home/user/ShoppingMeVaBe/src/main/webapp/WEB-INF/views/common/`
- **Guest Views**: `/home/user/ShoppingMeVaBe/src/main/webapp/WEB-INF/views/guest/`
  - `home.jsp`, `login.jsp`, `register.jsp`, `products.jsp`, `product-detail.jsp`, `cart.jsp`
- **Customer Views**: `/home/user/ShoppingMeVaBe/src/main/webapp/WEB-INF/views/customer/`
  - `checkout.jsp`, `orders.jsp`, `order-detail.jsp`
- **Admin Views**: `/home/user/ShoppingMeVaBe/src/main/webapp/WEB-INF/views/admin/`
  - `dashboard.jsp`, `products/`, `categories/`, `users/`, `orders/`

### Frontend - CSS & JS
- **Pastel Theme CSS**: `/home/user/ShoppingMeVaBe/src/main/resources/static/css/pastel-theme.css`
- **Style CSS**: `/home/user/ShoppingMeVaBe/src/main/resources/static/css/style.css`
- **Validation JS**: `/home/user/ShoppingMeVaBe/src/main/resources/static/js/validation.js`

### Configuration & Initialization
- **Security Config**: `/home/user/ShoppingMeVaBe/src/main/java/iuh/student/www/config/SecurityConfig.java`
- **Data Initializer**: `/home/user/ShoppingMeVaBe/src/main/java/iuh/student/www/config/DataInitializer.java`
- **OpenAPI Config**: `/home/user/ShoppingMeVaBe/src/main/java/iuh/student/www/config/OpenAPIConfig.java`
- **Web Config**: `/home/user/ShoppingMeVaBe/src/main/java/iuh/student/www/config/WebConfig.java`

---

## Default Admin Credentials
- **Email**: admin@shopmevabe.com
- **Password**: admin123
- **Note**: Auto-created and reset on application startup

---

## Important URLs
- **Application**: http://localhost:8081
- **Swagger/API Docs**: http://localhost:8081/swagger-ui.html
- **API Base**: http://localhost:8081/api
- **Admin Dashboard**: http://localhost:8081/admin/dashboard
- **Login**: http://localhost:8081/login
- **Register**: http://localhost:8081/register

---

## Key Dependencies
- Spring Boot 3.2.5
- Spring Security with JWT
- Spring Data JPA
- Spring Mail
- Cloudinary for image upload
- JJWT for JWT tokens
- MariaDB/MySQL driver
- Swagger/OpenAPI for documentation
- Lombok for annotations
- Tomcat Jasper for JSP

---

## Database Connection
- **URL**: jdbc:mariadb://localhost:3306/shop_me_va_be
- **Username**: root
- **Password**: sapassword
- **Pool Size**: 5-10 connections

---

## Email Configuration
- **Provider**: Gmail SMTP
- **Host**: smtp.gmail.com
- **Port**: 587
- **Username**: nguyenthituongvi2023@gmail.com

---

## Payment Gateway
- **Provider**: MoMo (Vietnam Mobile Wallet)
- **Environment**: Test
- **Endpoint**: https://test-payment.momo.vn/v2/gateway/api/create

---

## Project Structure Summary
```
ShoppingMeVaBe/
├── src/main/
│   ├── java/iuh/student/www/
│   │   ├── controller/ (Web & REST controllers)
│   │   ├── entity/ (JPA entities)
│   │   ├── dto/ (Data transfer objects)
│   │   ├── model/ (Session models)
│   │   ├── service/ (Business logic)
│   │   ├── repository/ (Data access)
│   │   ├── security/ (Auth & security)
│   │   └── config/ (Configuration)
│   ├── resources/
│   │   ├── application.properties (Main config)
│   │   └── static/ (CSS, JS, images)
│   └── webapp/WEB-INF/views/ (JSP files)
├── pom.xml (Maven dependencies)
└── README files
```

---

For detailed information, refer to the PROJECT_OVERVIEW.md file.
