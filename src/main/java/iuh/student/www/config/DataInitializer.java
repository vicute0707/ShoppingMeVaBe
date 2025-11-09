package iuh.student.www.config;

import iuh.student.www.entity.User;
import iuh.student.www.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * DataInitializer - Tạo tài khoản Admin mặc định
 *
 * Tài khoản Admin sẽ tự động được tạo khi khởi động ứng dụng (nếu chưa có):
 * - Email: admin@shopmevabe.com
 * - Password: admin123
 *
 * Người dùng có thể tự đăng ký tài khoản CUSTOMER qua API /api/auth/register
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Tạo hoặc cập nhật admin account
        var existingAdmin = userRepository.findByEmail("admin@shopmevabe.com");

        if (existingAdmin.isPresent()) {
            // Cập nhật mật khẩu admin (đảm bảo luôn dùng mật khẩu mới)
            User admin = existingAdmin.get();
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole(User.Role.ADMIN);
            admin.setEnabled(true);
            admin.setFullName("Admin Shop Mẹ và Bé");
            userRepository.save(admin);

            log.info("========================================");
            log.info("🔄 Admin Account Updated!");
            log.info("📧 Email: admin@shopmevabe.com");
            log.info("🔑 Password: admin123 (RESET)");
            log.info("========================================");
        } else {
            // Tạo mới admin account
            User admin = User.builder()
                    .fullName("Admin Shop Mẹ và Bé")
                    .email("admin@shopmevabe.com")
                    .password(passwordEncoder.encode("admin123"))
                    .phone("0123456789")
                    .address("Cửa hàng Shop Mẹ và Bé")
                    .role(User.Role.ADMIN)
                    .enabled(true)
                    .build();

            userRepository.save(admin);

            log.info("========================================");
            log.info("✅ Default Admin Account Created!");
            log.info("📧 Email: admin@shopmevabe.com");
            log.info("🔑 Password: admin123");
            log.info("========================================");
        }
    }
}
