-- ================================================
-- Sample Data for Shop Mẹ và Bé 🍼👶
-- ================================================
-- Dữ liệu mẫu cho hệ thống Cửa hàng Mẹ và Bé
-- ================================================

-- Clear existing data (optional, use with caution)
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE order_details;
TRUNCATE TABLE orders;
TRUNCATE TABLE products;
TRUNCATE TABLE categories;
TRUNCATE TABLE users;
SET FOREIGN_KEY_CHECKS = 1;

-- ================================================
-- Insert Users (Admin và Khách hàng mẫu)
-- Password: admin123 (đã mã hóa bằng BCrypt)
-- ================================================
INSERT INTO users (id, full_name, email, password, phone, address, role, enabled, created_at, updated_at) VALUES
(1, 'Admin Shop Mẹ và Bé', 'admin@shopmevabe.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', '0909123456', 'Hồ Chí Minh', 'ADMIN', TRUE, NOW(), NOW()),
(2, 'Nguyễn Thị Mai', 'mai.nguyen@gmail.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', '0901234567', 'Quận 1, TP.HCM', 'CUSTOMER', TRUE, NOW(), NOW()),
(3, 'Trần Văn Hùng', 'hung.tran@gmail.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', '0912345678', 'Quận 3, TP.HCM', 'CUSTOMER', TRUE, NOW(), NOW()),
(4, 'Lê Thị Lan', 'lan.le@gmail.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', '0923456789', 'Quận Bình Thạnh, TP.HCM', 'CUSTOMER', TRUE, NOW(), NOW()),
(5, 'Phạm Minh Tuấn', 'tuan.pham@gmail.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', '0934567890', 'Quận 7, TP.HCM', 'CUSTOMER', TRUE, NOW(), NOW());

-- ================================================
-- Insert Categories (Danh mục sản phẩm)
-- ================================================
INSERT INTO categories (id, name, description, active, created_at, updated_at) VALUES
(1, 'Sữa bột cho bé', 'Sữa bột dinh dưỡng cho trẻ từ 0-6 tuổi các thương hiệu uy tín', TRUE, NOW(), NOW()),
(2, 'Tã bỉm', 'Tã bỉm cao cấp cho bé từ sơ sinh đến 3 tuổi', TRUE, NOW(), NOW()),
(3, 'Đồ chơi trẻ em', 'Đồ chơi an toàn, phát triển trí tuệ cho bé', TRUE, NOW(), NOW()),
(4, 'Quần áo trẻ em', 'Quần áo cotton mềm mại cho bé yêu', TRUE, NOW(), NOW()),
(5, 'Xe đẩy - Nôi - Ghế ngồi', 'Xe đẩy, nôi, ghế ngồi an toàn cho bé', TRUE, NOW(), NOW()),
(6, 'Đồ dùng cho mẹ', 'Sản phẩm hỗ trợ mẹ bầu và sau sinh', TRUE, NOW(), NOW()),
(7, 'Thực phẩm dinh dưỡng', 'Bột ăn dặm, cháo dinh dưỡng cho bé', TRUE, NOW(), NOW()),
(8, 'Đồ dùng tắm gội', 'Sản phẩm tắm gội an toàn cho làn da nhạy cảm', TRUE, NOW(), NOW());

-- ================================================
-- Insert Products (Sản phẩm cho Mẹ và Bé)
-- ================================================

-- Sữa bột cho bé (Category 1)
INSERT INTO products (id, name, description, price, stock_quantity, image_url, active, category_id, created_at, updated_at) VALUES
(1, 'Sữa Enfamil A+ 1 - 900g', 'Sữa bột Enfamil A+ công thức Neuro Pro giúp phát triển trí não và tăng cường miễn dịch cho bé 0-6 tháng', 520000, 100, 'https://via.placeholder.com/400x400?text=Enfamil+A%2B+1', TRUE, 1, NOW(), NOW()),
(2, 'Sữa Aptamil Úc số 1 - 900g', 'Sữa Aptamil Úc công thức ProNutra giúp hệ tiêu hóa khỏe mạnh cho bé 0-6 tháng', 680000, 80, 'https://via.placeholder.com/400x400?text=Aptamil+1', TRUE, 1, NOW(), NOW()),
(3, 'Sữa Similac IQ 2 - 900g', 'Sữa Similac IQ Plus HMO giúp phát triển não bộ và tăng cường miễn dịch cho bé 6-12 tháng', 490000, 120, 'https://via.placeholder.com/400x400?text=Similac+IQ+2', TRUE, 1, NOW(), NOW()),
(4, 'Sữa Vinamilk Optimum Gold 3 - 850g', 'Sữa Vinamilk Optimum Gold với HMO+ giúp bé 1-2 tuổi phát triển toàn diện', 425000, 150, 'https://via.placeholder.com/400x400?text=Optimum+Gold+3', TRUE, 1, NOW(), NOW()),
(5, 'Sữa NAN Optipro 4 - 900g', 'Sữa NAN Optipro 4 công thức BL Probiotics cho bé trên 2 tuổi', 455000, 90, 'https://via.placeholder.com/400x400?text=NAN+4', TRUE, 1, NOW(), NOW());

-- Tã bỉm (Category 2)
INSERT INTO products (id, name, description, price, stock_quantity, image_url, active, category_id, created_at, updated_at) VALUES
(6, 'Tã Bobby Extra Soft Dry NB - 84 miếng', 'Tã Bobby Extra Soft Dry siêu thấm hút cho bé sơ sinh dưới 5kg', 189000, 200, 'https://via.placeholder.com/400x400?text=Bobby+NB', TRUE, 2, NOW(), NOW()),
(7, 'Tã Pampers Premium S - 84 miếng', 'Tã Pampers Premium Care siêu mềm mại cho bé 3-8kg', 265000, 180, 'https://via.placeholder.com/400x400?text=Pampers+S', TRUE, 2, NOW(), NOW()),
(8, 'Tã Merries M - 64 miếng', 'Tã dán Merries cao cấp Nhật Bản cho bé 6-11kg', 315000, 150, 'https://via.placeholder.com/400x400?text=Merries+M', TRUE, 2, NOW(), NOW()),
(9, 'Tã quần Moony L - 44 miếng', 'Tã quần Moony siêu thoáng khí cho bé 9-14kg', 289000, 120, 'https://via.placeholder.com/400x400?text=Moony+L', TRUE, 2, NOW(), NOW()),
(10, 'Tã Huggies Dry Pants XL - 54 miếng', 'Tã quần Huggies khô thoáng cho bé 12-17kg', 245000, 160, 'https://via.placeholder.com/400x400?text=Huggies+XL', TRUE, 2, NOW(), NOW());

-- Đồ chơi trẻ em (Category 3)
INSERT INTO products (id, name, description, price, stock_quantity, image_url, active, category_id, created_at, updated_at) VALUES
(11, 'Xúc xắc cho bé Winfun', 'Bộ 5 xúc xắc nhiều màu sắc giúp phát triển giác quan cho bé từ 3 tháng', 125000, 80, 'https://via.placeholder.com/400x400?text=Xuc+Xac+Winfun', TRUE, 3, NOW(), NOW()),
(12, 'Bộ đồ chơi âm nhạc 5 món', 'Bộ đồ chơi nhạc cụ phát triển tính sáng tạo cho bé từ 6 tháng', 235000, 60, 'https://via.placeholder.com/400x400?text=Am+Nhac+5mon', TRUE, 3, NOW(), NOW()),
(13, 'Bộ xếp hình gỗ Montessori', 'Bộ xếp hình gỗ cao cấp phát triển tư duy logic cho bé 1-3 tuổi', 350000, 45, 'https://via.placeholder.com/400x400?text=Xep+Hinh+Go', TRUE, 3, NOW(), NOW()),
(14, 'Xe máy điện trẻ em 3 bánh', 'Xe máy điện an toàn có nhạc và đèn cho bé từ 1-4 tuổi', 1250000, 25, 'https://via.placeholder.com/400x400?text=Xe+May+Dien', TRUE, 3, NOW(), NOW()),
(15, 'Bộ lego duplo 100 chi tiết', 'Bộ lego duplo lớn an toàn cho bé từ 18 tháng', 580000, 50, 'https://via.placeholder.com/400x400?text=Lego+Duplo', TRUE, 3, NOW(), NOW());

-- Quần áo trẻ em (Category 4)
INSERT INTO products (id, name, description, price, stock_quantity, image_url, active, category_id, created_at, updated_at) VALUES
(16, 'Bộ body suit cotton cho bé 0-6 tháng', 'Set 5 bộ body suit 100% cotton mềm mại cho bé sơ sinh', 285000, 100, 'https://via.placeholder.com/400x400?text=Body+Suit', TRUE, 4, NOW(), NOW()),
(17, 'Áo liền quần họa tiết dễ thương', 'Áo liền quần cotton cao cấp nhiều màu sắc cho bé 3-12 tháng', 165000, 120, 'https://via.placeholder.com/400x400?text=Ao+Lien+Quan', TRUE, 4, NOW(), NOW()),
(18, 'Bộ quần áo thu đông cho bé', 'Bộ quần áo cotton lót nỉ ấm áp cho bé 1-3 tuổi', 215000, 90, 'https://via.placeholder.com/400x400?text=Thu+Dong', TRUE, 4, NOW(), NOW()),
(19, 'Váy công chúa cho bé gái', 'Váy xinh xắn phối ren cho bé gái 1-5 tuổi', 195000, 70, 'https://via.placeholder.com/400x400?text=Vay+Cong+Chua', TRUE, 4, NOW(), NOW()),
(20, 'Bộ đồ thể thao cho bé trai', 'Bộ đồ thể thao năng động cho bé trai 2-6 tuổi', 185000, 85, 'https://via.placeholder.com/400x400?text=The+Thao', TRUE, 4, NOW(), NOW());

-- Xe đẩy - Nôi - Ghế ngồi (Category 5)
INSERT INTO products (id, name, description, price, stock_quantity, image_url, active, category_id, created_at, updated_at) VALUES
(21, 'Xe đẩy 2 chiều Seebaby Q5', 'Xe đẩy 2 chiều cao cấp có mái che UV cho bé 0-3 tuổi', 2450000, 30, 'https://via.placeholder.com/400x400?text=Xe+Day+Q5', TRUE, 5, NOW(), NOW()),
(22, 'Nôi điện tự động Mamakids', 'Nôi điện đa năng có nhạc ru và điều khiển từ xa', 3250000, 20, 'https://via.placeholder.com/400x400?text=Noi+Dien', TRUE, 5, NOW(), NOW()),
(23, 'Ghế ăn dặm Mastela 3 in 1', 'Ghế ăn dặm điều chỉnh độ cao, gấp gọn tiện lợi', 1850000, 40, 'https://via.placeholder.com/400x400?text=Ghe+An+Dam', TRUE, 5, NOW(), NOW()),
(24, 'Ghế ngồi ô tô Aprica 360 độ', 'Ghế ngồi ô tô xoay 360 độ cho bé 0-7 tuổi', 4850000, 15, 'https://via.placeholder.com/400x400?text=Ghe+Oto', TRUE, 5, NOW(), NOW()),
(25, 'Nôi xách tay Graco', 'Nôi xách tay nhẹ gọn, tiện lợi cho bé sơ sinh', 1250000, 35, 'https://via.placeholder.com/400x400?text=Noi+Xach', TRUE, 5, NOW(), NOW());

-- Đồ dùng cho mẹ (Category 6)
INSERT INTO products (id, name, description, price, stock_quantity, image_url, active, category_id, created_at, updated_at) VALUES
(26, 'Máy hút sữa điện đôi Real Bubble', 'Máy hút sữa điện đôi massage mô phỏng bú của bé', 1680000, 50, 'https://via.placeholder.com/400x400?text=May+Hut+Sua', TRUE, 6, NOW(), NOW()),
(27, 'Túi trữ sữa Unimom 210ml', 'Hộp 50 túi trữ sữa an toàn không BPA', 145000, 100, 'https://via.placeholder.com/400x400?text=Tui+Tru+Sua', TRUE, 6, NOW(), NOW()),
(28, 'Áo lót cho mẹ bầu và sau sinh', 'Bộ 3 áo lót cotton thoáng mát cho mẹ', 285000, 80, 'https://via.placeholder.com/400x400?text=Ao+Lot+Me', TRUE, 6, NOW(), NOW()),
(29, 'Gối bầu đa năng Mamaway', 'Gối bầu đa năng giúp mẹ ngủ ngon và cho con bú', 685000, 45, 'https://via.placeholder.com/400x400?text=Goi+Bau', TRUE, 6, NOW(), NOW()),
(30, 'Vitamin tổng hợp cho mẹ bầu Elevit', 'Vitamin và khoáng chất thiết yếu cho mẹ và bé', 520000, 60, 'https://via.placeholder.com/400x400?text=Elevit', TRUE, 6, NOW(), NOW());

-- Thực phẩm dinh dưỡng (Category 7)
INSERT INTO products (id, name, description, price, stock_quantity, image_url, active, category_id, created_at, updated_at) VALUES
(31, 'Bột ăn dặm Ridielac Alpha Gold - 200g', 'Bột ăn dặm dinh dưỡng cho bé từ 6 tháng', 98000, 150, 'https://via.placeholder.com/400x400?text=Bot+An+Dam', TRUE, 7, NOW(), NOW()),
(32, 'Cháo ăn liền Wakodo vị cá hồi - 80g', 'Cháo ăn liền Nhật Bản cho bé từ 7 tháng', 45000, 200, 'https://via.placeholder.com/400x400?text=Chao+Wakodo', TRUE, 7, NOW(), NOW()),
(33, 'Bánh ăn dặm Pigeon gạo lứt - 50g', 'Bánh ăn dặm tan trong miệng cho bé từ 6 tháng', 55000, 180, 'https://via.placeholder.com/400x400?text=Banh+Pigeon', TRUE, 7, NOW(), NOW()),
(34, 'Sữa chua cho bé Dutch Lady - 100g x 4', 'Sữa chua dinh dưỡng cho bé từ 1 tuổi', 36000, 220, 'https://via.placeholder.com/400x400?text=Sua+Chua', TRUE, 7, NOW(), NOW()),
(35, 'Váng sữa Bledina vị cam - 100g x 4', 'Váng sữa Pháp bổ sung canxi cho bé', 125000, 130, 'https://via.placeholder.com/400x400?text=Vang+Sua', TRUE, 7, NOW(), NOW());

-- Đồ dùng tắm gội (Category 8)
INSERT INTO products (id, name, description, price, stock_quantity, image_url, active, category_id, created_at, updated_at) VALUES
(36, 'Tắm gội Kodomo 200ml', 'Sữa tắm gội 2 trong 1 cho bé từ 0 tháng', 89000, 120, 'https://via.placeholder.com/400x400?text=Kodomo', TRUE, 8, NOW(), NOW()),
(37, 'Dầu gội Johnson Baby 500ml', 'Dầu gội không cay mắt cho bé', 125000, 150, 'https://via.placeholder.com/400x400?text=Johnson+Goi', TRUE, 8, NOW(), NOW()),
(38, 'Sữa tắm Lactacyd Baby 250ml', 'Sữa tắm pH cân bằng cho da nhạy cảm', 105000, 100, 'https://via.placeholder.com/400x400?text=Lactacyd', TRUE, 8, NOW(), NOW()),
(39, 'Khăn tắm xô cao cấp 6 lớp', 'Khăn tắm 100% cotton mềm mịn cho bé', 135000, 90, 'https://via.placeholder.com/400x400?text=Khan+Tam', TRUE, 8, NOW(), NOW()),
(40, 'Chậu tắm kèm giá đỡ Babycute', 'Chậu tắm có giá đỡ an toàn cho bé sơ sinh', 385000, 60, 'https://via.placeholder.com/400x400?text=Chau+Tam', TRUE, 8, NOW(), NOW());

-- ================================================
-- Insert Sample Orders
-- ================================================

-- Order 1: Nguyễn Thị Mai
INSERT INTO orders (id, user_id, order_date, total_amount, status, shipping_address, phone, notes, payment_method, payment_status, transaction_id, created_at, updated_at) VALUES
(1, 2, '2025-10-15 10:30:00', 1455000, 'DELIVERED', 'Quận 1, TP.HCM', '0901234567', 'Giao giờ hành chính', 'MOMO', 'PAID', 'MOMO202510151030', '2025-10-15 10:30:00', '2025-10-20 14:30:00');

INSERT INTO order_details (order_id, product_id, quantity, unit_price, subtotal) VALUES
(1, 1, 2, 520000, 1040000),  -- Sữa Enfamil A+ 1 x 2
(1, 7, 1, 265000, 265000),   -- Tã Pampers Premium S x 1
(1, 36, 1, 89000, 89000),    -- Tắm gội Kodomo x 1
(1, 31, 1, 98000, 98000);    -- Bột ăn dặm x 1

-- Order 2: Trần Văn Hùng
INSERT INTO orders (id, user_id, order_date, total_amount, status, shipping_address, phone, notes, payment_method, payment_status, transaction_id, created_at, updated_at) VALUES
(2, 3, '2025-10-20 14:20:00', 5100000, 'SHIPPED', 'Quận 3, TP.HCM', '0912345678', 'Gọi trước khi giao', 'COD', 'UNPAID', NULL, '2025-10-20 14:20:00', '2025-10-22 09:00:00');

INSERT INTO order_details (order_id, product_id, quantity, unit_price, subtotal) VALUES
(2, 21, 1, 2450000, 2450000),  -- Xe đẩy Seebaby Q5 x 1
(2, 23, 1, 1850000, 1850000),  -- Ghế ăn dặm Mastela x 1
(2, 8, 2, 315000, 630000),     -- Tã Merries M x 2
(2, 11, 1, 125000, 125000);    -- Xúc xắc Winfun x 1

-- Order 3: Lê Thị Lan
INSERT INTO orders (id, user_id, order_date, total_amount, status, shipping_address, phone, notes, payment_method, payment_status, transaction_id, created_at, updated_at) VALUES
(3, 4, '2025-10-25 09:15:00', 2565000, 'PROCESSING', 'Quận Bình Thạnh, TP.HCM', '0923456789', 'Giao buổi chiều', 'MOMO', 'PAID', 'MOMO202510250915', '2025-10-25 09:15:00', '2025-10-25 09:15:00');

INSERT INTO order_details (order_id, product_id, quantity, unit_price, subtotal) VALUES
(3, 26, 1, 1680000, 1680000),  -- Máy hút sữa điện đôi x 1
(3, 29, 1, 685000, 685000),    -- Gối bầu đa năng x 1
(3, 27, 1, 145000, 145000);    -- Túi trữ sữa x 1

-- Order 4: Phạm Minh Tuấn
INSERT INTO orders (id, user_id, order_date, total_amount, status, shipping_address, phone, notes, payment_method, payment_status, created_at, updated_at) VALUES
(4, 5, '2025-10-28 16:45:00', 890000, 'PENDING', 'Quận 7, TP.HCM', '0934567890', '', 'COD', 'UNPAID', '2025-10-28 16:45:00', '2025-10-28 16:45:00');

INSERT INTO order_details (order_id, product_id, quantity, unit_price, subtotal) VALUES
(4, 3, 1, 490000, 490000),    -- Sữa Similac IQ 2 x 1
(4, 10, 1, 245000, 245000),   -- Tã Huggies XL x 1
(4, 37, 1, 125000, 125000);   -- Dầu gội Johnson x 1

-- ================================================
-- Reset Auto Increment (Optional)
-- ================================================
ALTER TABLE users AUTO_INCREMENT = 6;
ALTER TABLE categories AUTO_INCREMENT = 9;
ALTER TABLE products AUTO_INCREMENT = 41;
ALTER TABLE orders AUTO_INCREMENT = 5;
ALTER TABLE order_details AUTO_INCREMENT = 100;

-- ================================================
-- End of Sample Data
-- ================================================
-- 🎉 Dữ liệu mẫu đã được import thành công!
-- Tài khoản admin: admin@shopmevabe.com / admin123
-- Tài khoản khách hàng: mai.nguyen@gmail.com / admin123
-- ================================================
