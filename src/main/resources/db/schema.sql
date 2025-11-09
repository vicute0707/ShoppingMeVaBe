-- ================================================
-- Database Schema for Shop Mẹ và Bé
-- ================================================
-- Hệ thống Cửa hàng chuyên cung cấp sản phẩm cho Mẹ và Bé
-- ================================================

-- Drop tables if exists (for clean installation)
DROP TABLE IF EXISTS order_details;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS products;
DROP TABLE IF EXISTS categories;
DROP TABLE IF EXISTS users;

-- ================================================
-- Table: users
-- Quản lý thông tin người dùng (khách hàng và admin)
-- ================================================
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL COMMENT 'Họ và tên đầy đủ',
    email VARCHAR(100) NOT NULL UNIQUE COMMENT 'Email đăng nhập',
    password VARCHAR(255) NOT NULL COMMENT 'Mật khẩu đã mã hóa (BCrypt)',
    phone VARCHAR(15) COMMENT 'Số điện thoại liên hệ',
    address VARCHAR(255) COMMENT 'Địa chỉ giao hàng',
    role VARCHAR(20) NOT NULL DEFAULT 'CUSTOMER' COMMENT 'Vai trò: CUSTOMER, ADMIN',
    enabled BOOLEAN NOT NULL DEFAULT FALSE COMMENT 'Trạng thái kích hoạt tài khoản',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Ngày tạo tài khoản',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Ngày cập nhật cuối',

    INDEX idx_email (email),
    INDEX idx_role (role),
    INDEX idx_enabled (enabled)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Bảng quản lý người dùng';

-- ================================================
-- Table: categories
-- Danh mục sản phẩm (Sữa bột, Tã bỉm, Đồ chơi, v.v.)
-- ================================================
CREATE TABLE categories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE COMMENT 'Tên danh mục',
    description VARCHAR(500) COMMENT 'Mô tả danh mục',
    active BOOLEAN NOT NULL DEFAULT TRUE COMMENT 'Trạng thái hiển thị',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Ngày tạo',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Ngày cập nhật',

    INDEX idx_name (name),
    INDEX idx_active (active)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Bảng danh mục sản phẩm';

-- ================================================
-- Table: products
-- Sản phẩm cho Mẹ và Bé
-- ================================================
CREATE TABLE products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(200) NOT NULL COMMENT 'Tên sản phẩm',
    description TEXT COMMENT 'Mô tả chi tiết sản phẩm',
    price DECIMAL(15,2) NOT NULL COMMENT 'Giá bán (VNĐ)',
    stock_quantity INT NOT NULL DEFAULT 0 COMMENT 'Số lượng tồn kho',
    image_url VARCHAR(500) COMMENT 'URL hình ảnh sản phẩm',
    active BOOLEAN NOT NULL DEFAULT TRUE COMMENT 'Trạng thái hiển thị',
    category_id BIGINT NOT NULL COMMENT 'ID danh mục',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Ngày thêm sản phẩm',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Ngày cập nhật',

    FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE RESTRICT,
    INDEX idx_name (name),
    INDEX idx_price (price),
    INDEX idx_category (category_id),
    INDEX idx_active (active),
    INDEX idx_stock (stock_quantity)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Bảng sản phẩm';

-- ================================================
-- Table: orders
-- Đơn hàng của khách hàng
-- ================================================
CREATE TABLE orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL COMMENT 'ID khách hàng',
    order_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Ngày đặt hàng',
    total_amount DECIMAL(15,2) NOT NULL COMMENT 'Tổng tiền đơn hàng (VNĐ)',
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING' COMMENT 'Trạng thái: PENDING, PROCESSING, SHIPPED, DELIVERED, CANCELLED',
    shipping_address VARCHAR(255) COMMENT 'Địa chỉ giao hàng',
    phone VARCHAR(15) COMMENT 'Số điện thoại nhận hàng',
    notes VARCHAR(500) COMMENT 'Ghi chú đơn hàng',
    payment_method VARCHAR(50) COMMENT 'Phương thức thanh toán: COD, MOMO, BANK_TRANSFER',
    payment_status VARCHAR(20) COMMENT 'Trạng thái thanh toán: UNPAID, PAID, REFUNDED',
    transaction_id VARCHAR(100) COMMENT 'Mã giao dịch thanh toán',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Ngày tạo',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Ngày cập nhật',

    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE RESTRICT,
    INDEX idx_user (user_id),
    INDEX idx_order_date (order_date),
    INDEX idx_status (status),
    INDEX idx_payment_status (payment_status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Bảng đơn hàng';

-- ================================================
-- Table: order_details
-- Chi tiết đơn hàng (sản phẩm trong đơn)
-- ================================================
CREATE TABLE order_details (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT NOT NULL COMMENT 'ID đơn hàng',
    product_id BIGINT NOT NULL COMMENT 'ID sản phẩm',
    quantity INT NOT NULL COMMENT 'Số lượng đặt mua',
    unit_price DECIMAL(15,2) NOT NULL COMMENT 'Đơn giá tại thời điểm mua',
    subtotal DECIMAL(15,2) NOT NULL COMMENT 'Thành tiền = quantity * unit_price',

    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE RESTRICT,
    INDEX idx_order (order_id),
    INDEX idx_product (product_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Bảng chi tiết đơn hàng';

-- ================================================
-- End of Schema
-- ================================================
