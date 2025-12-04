package iuh.student.www.service;

import iuh.student.www.dto.RegisterDTO;
import iuh.student.www.entity.User;
import iuh.student.www.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * White Box Testing cho UserService
 * Test coverage: Tất cả các đường dẫn logic trong UserService
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("UserService White Box Tests")
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private UserService userService;

    private RegisterDTO validRegisterDTO;
    private User testUser;

    @BeforeEach
    void setUp() {
        // Setup test data
        validRegisterDTO = new RegisterDTO();
        validRegisterDTO.setFullName("Nguyen Van A");
        validRegisterDTO.setEmail("test@example.com");
        validRegisterDTO.setPassword("password123");
        validRegisterDTO.setConfirmPassword("password123");
        validRegisterDTO.setPhone("0901234567");
        validRegisterDTO.setAddress("123 Test Street");

        testUser = User.builder()
                .id(1L)
                .fullName("Nguyen Van A")
                .email("test@example.com")
                .password("encodedPassword")
                .phone("0901234567")
                .address("123 Test Street")
                .role(User.Role.CUSTOMER)
                .enabled(true)
                .build();
    }

    // ==================== REGISTER USER TESTS ====================

    @Test
    @DisplayName("Test 1: Register user thành công - Happy path")
    void testRegisterUser_Success() throws Exception {
        // Given
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        doNothing().when(emailService).sendWelcomeEmail(anyString(), anyString());

        // When
        User result = userService.registerUser(validRegisterDTO);

        // Then
        assertNotNull(result);
        assertEquals("Nguyen Van A", result.getFullName());
        assertEquals("test@example.com", result.getEmail());
        assertEquals(User.Role.CUSTOMER, result.getRole());
        assertTrue(result.getEnabled());

        // Verify interactions
        verify(userRepository, times(1)).existsByEmail("test@example.com");
        verify(passwordEncoder, times(1)).encode("password123");
        verify(userRepository, times(1)).save(any(User.class));
        verify(emailService, times(1)).sendWelcomeEmail("test@example.com", "Nguyen Van A");
    }

    @Test
    @DisplayName("Test 2: Register user - Email đã tồn tại (Branch 1)")
    void testRegisterUser_EmailExists() {
        // Given
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        // When & Then
        Exception exception = assertThrows(Exception.class, () -> {
            userService.registerUser(validRegisterDTO);
        });

        assertEquals("Email đã tồn tại trong hệ thống", exception.getMessage());
        verify(userRepository, times(1)).existsByEmail("test@example.com");
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Test 3: Register user - Password không khớp (Branch 2)")
    void testRegisterUser_PasswordMismatch() {
        // Given
        validRegisterDTO.setConfirmPassword("differentPassword");
        when(userRepository.existsByEmail(anyString())).thenReturn(false);

        // When & Then
        Exception exception = assertThrows(Exception.class, () -> {
            userService.registerUser(validRegisterDTO);
        });

        assertEquals("Mật khẩu xác nhận không khớp", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Test 4: Register user - Email không đúng định dạng (Branch 3)")
    void testRegisterUser_InvalidEmailFormat() {
        // Given
        validRegisterDTO.setEmail("invalid-email");
        when(userRepository.existsByEmail(anyString())).thenReturn(false);

        // When & Then
        Exception exception = assertThrows(Exception.class, () -> {
            userService.registerUser(validRegisterDTO);
        });

        assertEquals("Email không đúng định dạng", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Test 5: Register user - FullName null (Branch 4)")
    void testRegisterUser_NullFullName() {
        // Given
        validRegisterDTO.setFullName(null);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);

        // When & Then
        Exception exception = assertThrows(Exception.class, () -> {
            userService.registerUser(validRegisterDTO);
        });

        assertEquals("Họ tên không được để trống", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Test 6: Register user - FullName empty (Branch 4)")
    void testRegisterUser_EmptyFullName() {
        // Given
        validRegisterDTO.setFullName("   ");
        when(userRepository.existsByEmail(anyString())).thenReturn(false);

        // When & Then
        Exception exception = assertThrows(Exception.class, () -> {
            userService.registerUser(validRegisterDTO);
        });

        assertEquals("Họ tên không được để trống", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    // ==================== UPDATE USER TESTS ====================

    @Test
    @DisplayName("Test 7: Update user thành công - Happy path")
    void testUpdateUser_Success() throws Exception {
        // Given
        User updatedUser = User.builder()
                .fullName("Nguyen Van B")
                .phone("0987654321")
                .address("456 New Street")
                .role(User.Role.ADMIN)
                .enabled(false)
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // When
        User result = userService.updateUser(1L, updatedUser);

        // Then
        assertNotNull(result);
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Test 8: Update user - User không tồn tại")
    void testUpdateUser_UserNotFound() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        Exception exception = assertThrows(Exception.class, () -> {
            userService.updateUser(1L, testUser);
        });

        assertEquals("Không tìm thấy người dùng", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Test 9: Update user - FullName null")
    void testUpdateUser_NullFullName() {
        // Given
        User updatedUser = User.builder()
                .fullName(null)
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        // When & Then
        Exception exception = assertThrows(Exception.class, () -> {
            userService.updateUser(1L, updatedUser);
        });

        assertEquals("Họ tên không được để trống", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Test 10: Update user - FullName empty")
    void testUpdateUser_EmptyFullName() {
        // Given
        User updatedUser = User.builder()
                .fullName("   ")
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        // When & Then
        Exception exception = assertThrows(Exception.class, () -> {
            userService.updateUser(1L, updatedUser);
        });

        assertEquals("Họ tên không được để trống", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    // ==================== DELETE USER TESTS ====================

    @Test
    @DisplayName("Test 11: Delete user - User không tồn tại")
    void testDeleteUser_UserNotFound() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        Exception exception = assertThrows(Exception.class, () -> {
            userService.deleteUser(1L);
        });

        assertEquals("Không tìm thấy người dùng", exception.getMessage());
        verify(userRepository, never()).delete(any(User.class));
    }

    @Test
    @DisplayName("Test 12: Delete user - Không thể xóa Admin (Branch 1)")
    void testDeleteUser_CannotDeleteAdmin() {
        // Given
        testUser.setRole(User.Role.ADMIN);
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        // When & Then
        Exception exception = assertThrows(Exception.class, () -> {
            userService.deleteUser(1L);
        });

        assertEquals("Không thể xóa tài khoản Admin", exception.getMessage());
        verify(userRepository, never()).delete(any(User.class));
    }

    @Test
    @DisplayName("Test 13: Delete user - User có orders (Branch 2)")
    void testDeleteUser_UserHasOrders() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.hasOrders(1L)).thenReturn(true);

        // When & Then
        Exception exception = assertThrows(Exception.class, () -> {
            userService.deleteUser(1L);
        });

        assertTrue(exception.getMessage().contains("Không thể xóa người dùng có đơn hàng"));
        verify(userRepository, never()).delete(any(User.class));
    }

    @Test
    @DisplayName("Test 14: Delete user thành công - Happy path")
    void testDeleteUser_Success() throws Exception {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.hasOrders(1L)).thenReturn(false);
        doNothing().when(userRepository).delete(any(User.class));

        // When
        userService.deleteUser(1L);

        // Then
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).hasOrders(1L);
        verify(userRepository, times(1)).delete(testUser);
    }

    // ==================== TOGGLE USER STATUS TESTS ====================

    @Test
    @DisplayName("Test 15: Toggle user status - User không tồn tại")
    void testToggleUserStatus_UserNotFound() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        Exception exception = assertThrows(Exception.class, () -> {
            userService.toggleUserStatus(1L);
        });

        assertEquals("Không tìm thấy người dùng", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Test 16: Toggle user status - Không thể thay đổi Admin")
    void testToggleUserStatus_CannotToggleAdmin() {
        // Given
        testUser.setRole(User.Role.ADMIN);
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        // When & Then
        Exception exception = assertThrows(Exception.class, () -> {
            userService.toggleUserStatus(1L);
        });

        assertEquals("Không thể thay đổi trạng thái tài khoản Admin", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Test 17: Toggle user status thành công - Enable to Disable")
    void testToggleUserStatus_EnableToDisable() throws Exception {
        // Given
        testUser.setEnabled(true);
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // When
        User result = userService.toggleUserStatus(1L);

        // Then
        assertNotNull(result);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Test 18: Toggle user status thành công - Disable to Enable")
    void testToggleUserStatus_DisableToEnable() throws Exception {
        // Given
        testUser.setEnabled(false);
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // When
        User result = userService.toggleUserStatus(1L);

        // Then
        assertNotNull(result);
        verify(userRepository, times(1)).save(any(User.class));
    }

    // ==================== FIND METHODS TESTS ====================

    @Test
    @DisplayName("Test 19: Find by email - Tìm thấy")
    void testFindByEmail_Found() {
        // Given
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));

        // When
        Optional<User> result = userService.findByEmail("test@example.com");

        // Then
        assertTrue(result.isPresent());
        assertEquals("test@example.com", result.get().getEmail());
        verify(userRepository, times(1)).findByEmail("test@example.com");
    }

    @Test
    @DisplayName("Test 20: Find by email - Không tìm thấy")
    void testFindByEmail_NotFound() {
        // Given
        when(userRepository.findByEmail("notfound@example.com")).thenReturn(Optional.empty());

        // When
        Optional<User> result = userService.findByEmail("notfound@example.com");

        // Then
        assertFalse(result.isPresent());
        verify(userRepository, times(1)).findByEmail("notfound@example.com");
    }

    @Test
    @DisplayName("Test 21: Find by ID - Tìm thấy")
    void testFindById_Found() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        // When
        Optional<User> result = userService.findById(1L);

        // Then
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Test 22: Find by ID - Không tìm thấy")
    void testFindById_NotFound() {
        // Given
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // When
        Optional<User> result = userService.findById(999L);

        // Then
        assertFalse(result.isPresent());
        verify(userRepository, times(1)).findById(999L);
    }

    @Test
    @DisplayName("Test 23: Get all users")
    void testGetAllUsers() {
        // Given
        List<User> users = Arrays.asList(testUser);
        when(userRepository.findAll()).thenReturn(users);

        // When
        List<User> result = userService.getAllUsers();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Test 24: Get all customers")
    void testGetAllCustomers() {
        // Given
        List<User> customers = Arrays.asList(testUser);
        when(userRepository.findAllCustomers()).thenReturn(customers);

        // When
        List<User> result = userService.getAllCustomers();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(userRepository, times(1)).findAllCustomers();
    }

    @Test
    @DisplayName("Test 25: Has orders - True")
    void testHasOrders_True() {
        // Given
        when(userRepository.hasOrders(1L)).thenReturn(true);

        // When
        boolean result = userService.hasOrders(1L);

        // Then
        assertTrue(result);
        verify(userRepository, times(1)).hasOrders(1L);
    }

    @Test
    @DisplayName("Test 26: Has orders - False")
    void testHasOrders_False() {
        // Given
        when(userRepository.hasOrders(1L)).thenReturn(false);

        // When
        boolean result = userService.hasOrders(1L);

        // Then
        assertFalse(result);
        verify(userRepository, times(1)).hasOrders(1L);
    }

    @Test
    @DisplayName("Test 27: Create admin user")
    void testCreateAdminUser() {
        // Given
        User adminUser = User.builder()
                .id(2L)
                .fullName("Admin User")
                .email("admin@example.com")
                .password("encodedPassword")
                .role(User.Role.ADMIN)
                .enabled(true)
                .build();

        when(passwordEncoder.encode("admin123")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(adminUser);

        // When
        User result = userService.createAdminUser("Admin User", "admin@example.com", "admin123");

        // Then
        assertNotNull(result);
        assertEquals(User.Role.ADMIN, result.getRole());
        assertTrue(result.getEnabled());
        verify(passwordEncoder, times(1)).encode("admin123");
        verify(userRepository, times(1)).save(any(User.class));
    }
}
