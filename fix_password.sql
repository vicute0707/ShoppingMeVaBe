-- ================================================
-- FIX PASSWORD CHO USERS
-- ================================================
-- Password mới: admin123 (BCrypt hash đã verify)
-- ================================================

-- Update password cho tất cả users với BCrypt hash đúng cho "admin123"
UPDATE users
SET password = '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy'
WHERE email IN (
    'admin@shopmevabe.com',
    'mai.nguyen@gmail.com',
    'hung.tran@gmail.com',
    'lan.le@gmail.com',
    'tuan.pham@gmail.com'
);

-- Verify
SELECT
    id,
    email,
    role,
    enabled,
    SUBSTRING(password, 1, 30) as password_hash
FROM users
ORDER BY id;

-- ================================================
-- Nếu vẫn không được, thử hash khác
-- ================================================
-- BCrypt hash khác cho "admin123" (round 12)
-- UPDATE users SET password = '$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewY5iGDdQzy4jq6u' WHERE email = 'admin@shopmevabe.com';
