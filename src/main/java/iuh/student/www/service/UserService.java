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
            throw new Exception("Email already exists");
        }

        // Check if passwords match
        if (!registerDTO.getPassword().equals(registerDTO.getConfirmPassword())) {
            throw new Exception("Passwords do not match");
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
                .orElseThrow(() -> new Exception("User not found"));

        user.setFullName(updatedUser.getFullName());
        user.setPhone(updatedUser.getPhone());
        user.setAddress(updatedUser.getAddress());

        // Only admin can change role
        if (updatedUser.getRole() != null) {
            user.setRole(updatedUser.getRole());
        }

        return userRepository.save(user);
    }

    public void deleteUser(Long id) throws Exception {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new Exception("User not found"));

        // Check if user has orders
        if (userRepository.hasOrders(id)) {
            throw new Exception("Cannot delete user with existing orders");
        }

        userRepository.delete(user);
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
