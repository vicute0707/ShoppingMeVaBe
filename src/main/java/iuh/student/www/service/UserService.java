package iuh.student.www.service;

import iuh.student.www.dto.RegisterDTO;
import iuh.student.www.entity.User;
import iuh.student.www.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public User registerUser(RegisterDTO registerDTO) throws Exception {
        // Check if email already exists
        if (userRepository.existsByEmail(registerDTO.getEmail())) {
            throw new Exception("Email đã tồn tại trong hệ thống");
        }

        // Check if passwords match
        if (!registerDTO.getPassword().equals(registerDTO.getConfirmPassword())) {
            throw new Exception("Mật khẩu xác nhận không khớp");
        }

        // Validate email format
        if (!registerDTO.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new Exception("Email không đúng định dạng");
        }

        // Validate full name
        if (registerDTO.getFullName() == null || registerDTO.getFullName().trim().isEmpty()) {
            throw new Exception("Họ tên không được để trống");
        }

        // Create new user
        User user = User.builder()
                .fullName(registerDTO.getFullName())
                .email(registerDTO.getEmail())
                .password(passwordEncoder.encode(registerDTO.getPassword()))
                .phone(registerDTO.getPhone())
                .address(registerDTO.getAddress())
                .role(User.Role.CUSTOMER)
                .enabled(true) // Auto-enable for simplicity, can add email verification later
                .build();

        User savedUser = userRepository.save(user);

        // Send welcome email
        emailService.sendWelcomeEmail(savedUser.getEmail(), savedUser.getFullName());

        return savedUser;
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<User> getAllCustomers() {
        return userRepository.findAllCustomers();
    }

    public User updateUser(Long id, User updatedUser) throws Exception {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new Exception("Không tìm thấy người dùng"));

        // Validate full name
        if (updatedUser.getFullName() == null || updatedUser.getFullName().trim().isEmpty()) {
            throw new Exception("Họ tên không được để trống");
        }
        user.setFullName(updatedUser.getFullName());

        // Update phone
        user.setPhone(updatedUser.getPhone());

        // Update address
        user.setAddress(updatedUser.getAddress());

        // Only admin can change role
        if (updatedUser.getRole() != null) {
            user.setRole(updatedUser.getRole());
        }

        // Update enabled status if provided
        if (updatedUser.getEnabled() != null) {
            user.setEnabled(updatedUser.getEnabled());
        }

        return userRepository.save(user);
    }

    public void deleteUser(Long id) throws Exception {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new Exception("Không tìm thấy người dùng"));

        // Check if user is admin
        if (user.getRole() == User.Role.ADMIN) {
            throw new Exception("Không thể xóa tài khoản Admin");
        }

        // Check if user has orders
        if (userRepository.hasOrders(id)) {
            throw new Exception("Không thể xóa người dùng có đơn hàng. Bạn có thể vô hiệu hóa tài khoản thay vì xóa.");
        }

        userRepository.delete(user);
    }

    public User toggleUserStatus(Long id) throws Exception {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new Exception("Không tìm thấy người dùng"));

        // Check if user is admin
        if (user.getRole() == User.Role.ADMIN) {
            throw new Exception("Không thể thay đổi trạng thái tài khoản Admin");
        }

        user.setEnabled(!user.getEnabled());
        return userRepository.save(user);
    }

    public boolean hasOrders(Long userId) {
        return userRepository.hasOrders(userId);
    }

    public User createAdminUser(String fullName, String email, String password) {
        User admin = User.builder()
                .fullName(fullName)
                .email(email)
                .password(passwordEncoder.encode(password))
                .role(User.Role.ADMIN)
                .enabled(true)
                .build();

        return userRepository.save(admin);
    }
}
