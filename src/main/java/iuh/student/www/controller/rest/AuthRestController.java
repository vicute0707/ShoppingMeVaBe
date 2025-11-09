package iuh.student.www.controller.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import iuh.student.www.dto.AuthResponse;
import iuh.student.www.dto.LoginRequest;
import iuh.student.www.dto.RegisterDTO;
import iuh.student.www.entity.User;
import iuh.student.www.security.CustomUserDetailsService;
import iuh.student.www.security.JwtUtil;
import iuh.student.www.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Authentication REST Controller
 * JWT-based authentication: Register & Login
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "JWT Authentication APIs")
@Slf4j
public class AuthRestController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    /**
     * Register new user
     * Password sẽ tự động được hash bằng BCrypt
     */
    @Operation(summary = "Đăng ký tài khoản mới", description = "User tự đăng ký, password tự động hash")
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterDTO registerDTO, BindingResult result) {

        if (result.hasErrors()) {
            Map<String, String> errors = result.getFieldErrors().stream()
                    .collect(Collectors.toMap(
                            error -> error.getField(),
                            error -> error.getDefaultMessage()
                    ));
            return ResponseEntity.badRequest().body(Map.of("errors", errors));
        }

        try {
            // Register user - password tự động hash trong UserService
            User savedUser = userService.registerUser(registerDTO);

            log.info("✅ User registered successfully: {}", savedUser.getEmail());

            // Auto login sau khi đăng ký - generate JWT token
            UserDetails userDetails = userDetailsService.loadUserByUsername(savedUser.getEmail());
            String token = jwtUtil.generateToken(userDetails);

            AuthResponse response = AuthResponse.builder()
                    .token(token)
                    .type("Bearer")
                    .email(savedUser.getEmail())
                    .fullName(savedUser.getFullName())
                    .role(savedUser.getRole().name())
                    .userId(savedUser.getId())
                    .message("Đăng ký thành công! Bạn đã được tự động đăng nhập.")
                    .build();

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Registration failed: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Login with JWT
     * Trả về JWT token nếu login thành công
     */
    @Operation(summary = "Đăng nhập", description = "Login và nhận JWT token")
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest, BindingResult result) {

        if (result.hasErrors()) {
            Map<String, String> errors = result.getFieldErrors().stream()
                    .collect(Collectors.toMap(
                            error -> error.getField(),
                            error -> error.getDefaultMessage()
                    ));
            return ResponseEntity.badRequest().body(Map.of("errors", errors));
        }

        try {
            log.info("🔐 Login attempt for user: {}", loginRequest.getEmail());

            // Authenticate user
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );

            // Load user details
            UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getEmail());

            // Get user info
            User user = userService.findByEmail(loginRequest.getEmail())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Generate JWT token
            String token = jwtUtil.generateToken(userDetails);

            log.info("✅ Login successful for user: {}", loginRequest.getEmail());

            AuthResponse response = AuthResponse.builder()
                    .token(token)
                    .type("Bearer")
                    .email(user.getEmail())
                    .fullName(user.getFullName())
                    .role(user.getRole().name())
                    .userId(user.getId())
                    .message("Đăng nhập thành công!")
                    .build();

            return ResponseEntity.ok(response);

        } catch (BadCredentialsException e) {
            log.error("❌ Login failed - Bad credentials for: {}", loginRequest.getEmail());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Email hoặc password không đúng"));

        } catch (Exception e) {
            log.error("❌ Login failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Đăng nhập thất bại: " + e.getMessage()));
        }
    }

    /**
     * Verify token
     * Kiểm tra token còn hợp lệ không
     */
    @Operation(summary = "Verify JWT token", description = "Kiểm tra token còn hợp lệ không")
    @GetMapping("/verify")
    public ResponseEntity<?> verifyToken(@RequestHeader("Authorization") String authHeader) {
        try {
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                String email = jwtUtil.extractUsername(token);
                UserDetails userDetails = userDetailsService.loadUserByUsername(email);

                if (jwtUtil.validateToken(token, userDetails)) {
                    return ResponseEntity.ok(Map.of(
                            "valid", true,
                            "email", email,
                            "message", "Token hợp lệ"
                    ));
                }
            }

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("valid", false, "message", "Token không hợp lệ"));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("valid", false, "message", e.getMessage()));
        }
    }
}
