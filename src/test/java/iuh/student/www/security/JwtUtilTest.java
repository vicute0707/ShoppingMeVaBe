package iuh.student.www.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

/**
 * White Box Testing cho JwtUtil
 * Test coverage: Tất cả các method và branches trong JwtUtil
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("JwtUtil White Box Tests")
class JwtUtilTest {

    private JwtUtil jwtUtil;
    private UserDetails userDetails;
    private String validToken;
    private static final String TEST_SECRET = "ShopMeVaBe2025SecretKeyForJWTAuthenticationVeryLongAndSecure123456789";
    private static final Long TEST_EXPIRATION = 86400000L; // 24 hours

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(jwtUtil, "secret", TEST_SECRET);
        ReflectionTestUtils.setField(jwtUtil, "expiration", TEST_EXPIRATION);

        // Create test user with authorities
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_CUSTOMER"));

        userDetails = User.builder()
                .username("test@example.com")
                .password("password123")
                .authorities(authorities)
                .build();

        // Generate a valid token for testing
        validToken = jwtUtil.generateToken(userDetails);
    }

    // ==================== GENERATE TOKEN TESTS ====================

    @Test
    @DisplayName("Test 1: Generate token thành công - Happy path")
    void testGenerateToken_Success() {
        // When
        String token = jwtUtil.generateToken(userDetails);

        // Then
        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertTrue(token.split("\\.").length == 3); // JWT has 3 parts
    }

    @Test
    @DisplayName("Test 2: Generate token - Chứa username đúng")
    void testGenerateToken_ContainsCorrectUsername() {
        // When
        String token = jwtUtil.generateToken(userDetails);
        String extractedUsername = jwtUtil.extractUsername(token);

        // Then
        assertEquals("test@example.com", extractedUsername);
    }

    @Test
    @DisplayName("Test 3: Generate token - Chứa authorities")
    void testGenerateToken_ContainsAuthorities() {
        // When
        String token = jwtUtil.generateToken(userDetails);
        Claims claims = Jwts.parser()
                .verifyWith(getTestSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        // Then
        assertNotNull(claims.get("authorities"));
    }

    @Test
    @DisplayName("Test 4: Generate token - Có expiration date")
    void testGenerateToken_HasExpirationDate() {
        // When
        String token = jwtUtil.generateToken(userDetails);
        Date expiration = jwtUtil.extractExpiration(token);

        // Then
        assertNotNull(expiration);
        assertTrue(expiration.after(new Date())); // Expiration in future
    }

    @Test
    @DisplayName("Test 5: Generate token cho multiple users - Different tokens")
    void testGenerateToken_DifferentUsersGetDifferentTokens() {
        // Given
        Collection<GrantedAuthority> authorities2 = new ArrayList<>();
        authorities2.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        UserDetails userDetails2 = User.builder()
                .username("admin@example.com")
                .password("admin123")
                .authorities(authorities2)
                .build();

        // When
        String token1 = jwtUtil.generateToken(userDetails);
        String token2 = jwtUtil.generateToken(userDetails2);

        // Then
        assertNotEquals(token1, token2);
    }

    // ==================== EXTRACT USERNAME TESTS ====================

    @Test
    @DisplayName("Test 6: Extract username thành công")
    void testExtractUsername_Success() {
        // When
        String username = jwtUtil.extractUsername(validToken);

        // Then
        assertEquals("test@example.com", username);
    }

    @Test
    @DisplayName("Test 7: Extract username - Invalid token format")
    void testExtractUsername_InvalidToken() {
        // Given
        String invalidToken = "invalid.token.format";

        // When & Then
        assertThrows(Exception.class, () -> {
            jwtUtil.extractUsername(invalidToken);
        });
    }

    // ==================== EXTRACT EXPIRATION TESTS ====================

    @Test
    @DisplayName("Test 8: Extract expiration thành công")
    void testExtractExpiration_Success() {
        // When
        Date expiration = jwtUtil.extractExpiration(validToken);

        // Then
        assertNotNull(expiration);
        assertTrue(expiration.after(new Date()));
    }

    @Test
    @DisplayName("Test 9: Extract expiration - Token hết hạn sau 24h")
    void testExtractExpiration_CorrectDuration() {
        // When
        Date expiration = jwtUtil.extractExpiration(validToken);
        long diff = expiration.getTime() - new Date().getTime();

        // Then - Should be approximately 24 hours (with some tolerance)
        assertTrue(diff > 86000000L); // > 23.88 hours
        assertTrue(diff < 86500000L); // < 24.02 hours
    }

    // ==================== VALIDATE TOKEN TESTS ====================

    @Test
    @DisplayName("Test 10: Validate token - Valid token (Happy path)")
    void testValidateToken_ValidToken() {
        // When
        Boolean isValid = jwtUtil.validateToken(validToken, userDetails);

        // Then
        assertTrue(isValid);
    }

    @Test
    @DisplayName("Test 11: Validate token - Username không khớp (Branch 1)")
    void testValidateToken_UsernameMismatch() {
        // Given
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_CUSTOMER"));
        UserDetails differentUser = User.builder()
                .username("different@example.com")
                .password("password123")
                .authorities(authorities)
                .build();

        // When
        Boolean isValid = jwtUtil.validateToken(validToken, differentUser);

        // Then
        assertFalse(isValid);
    }

    @Test
    @DisplayName("Test 12: Validate token - Token expired (Branch 2)")
    void testValidateToken_ExpiredToken() {
        // Given - Create JwtUtil with very short expiration
        JwtUtil shortExpirationJwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(shortExpirationJwtUtil, "secret", TEST_SECRET);
        ReflectionTestUtils.setField(shortExpirationJwtUtil, "expiration", 1L); // 1ms

        String expiredToken = shortExpirationJwtUtil.generateToken(userDetails);

        // Wait for token to expire
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // When & Then
        assertThrows(ExpiredJwtException.class, () -> {
            shortExpirationJwtUtil.extractUsername(expiredToken);
        });
    }

    @Test
    @DisplayName("Test 13: Validate token - Token chưa hết hạn")
    void testValidateToken_NotExpired() {
        // When
        Boolean isValid = jwtUtil.validateToken(validToken, userDetails);

        // Then
        assertTrue(isValid);

        Date expiration = jwtUtil.extractExpiration(validToken);
        assertTrue(expiration.after(new Date()));
    }

    // ==================== EXTRACT CLAIM TESTS ====================

    @Test
    @DisplayName("Test 14: Extract claim - Subject")
    void testExtractClaim_Subject() {
        // When
        String subject = jwtUtil.extractClaim(validToken, Claims::getSubject);

        // Then
        assertEquals("test@example.com", subject);
    }

    @Test
    @DisplayName("Test 15: Extract claim - Expiration")
    void testExtractClaim_Expiration() {
        // When
        Date expiration = jwtUtil.extractClaim(validToken, Claims::getExpiration);

        // Then
        assertNotNull(expiration);
        assertTrue(expiration.after(new Date()));
    }

    @Test
    @DisplayName("Test 16: Extract claim - IssuedAt")
    void testExtractClaim_IssuedAt() {
        // When
        Date issuedAt = jwtUtil.extractClaim(validToken, Claims::getIssuedAt);

        // Then
        assertNotNull(issuedAt);
        assertTrue(issuedAt.before(new Date()) || issuedAt.equals(new Date()));
    }

    // ==================== SIGNING KEY TESTS ====================

    @Test
    @DisplayName("Test 17: Signing key - Token có thể verify bằng cùng secret")
    void testSigningKey_CanVerifyTokenWithSameSecret() {
        // When
        String token = jwtUtil.generateToken(userDetails);

        // Then - Should not throw exception
        assertDoesNotThrow(() -> {
            Jwts.parser()
                    .verifyWith(getTestSigningKey())
                    .build()
                    .parseSignedClaims(token);
        });
    }

    @Test
    @DisplayName("Test 18: Signing key - Token không verify được với secret khác")
    void testSigningKey_CannotVerifyTokenWithDifferentSecret() {
        // Given
        String token = jwtUtil.generateToken(userDetails);
        SecretKey differentKey = Keys.hmacShaKeyFor(
                "DifferentSecretKeyForJWTAuthenticationVeryLongAndSecure987654321".getBytes(StandardCharsets.UTF_8)
        );

        // When & Then
        assertThrows(Exception.class, () -> {
            Jwts.parser()
                    .verifyWith(differentKey)
                    .build()
                    .parseSignedClaims(token);
        });
    }

    // ==================== EDGE CASE TESTS ====================

    @Test
    @DisplayName("Test 19: Generate token - User với multiple roles")
    void testGenerateToken_UserWithMultipleRoles() {
        // Given
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_CUSTOMER"));
        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));

        UserDetails multiRoleUser = User.builder()
                .username("admin@example.com")
                .password("admin123")
                .authorities(authorities)
                .build();

        // When
        String token = jwtUtil.generateToken(multiRoleUser);

        // Then
        assertNotNull(token);
        String username = jwtUtil.extractUsername(token);
        assertEquals("admin@example.com", username);
    }

    @Test
    @DisplayName("Test 20: Generate token - User với email phức tạp")
    void testGenerateToken_ComplexEmail() {
        // Given
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_CUSTOMER"));

        UserDetails userWithComplexEmail = User.builder()
                .username("user.name+tag@example.co.uk")
                .password("password123")
                .authorities(authorities)
                .build();

        // When
        String token = jwtUtil.generateToken(userWithComplexEmail);
        String extractedUsername = jwtUtil.extractUsername(token);

        // Then
        assertEquals("user.name+tag@example.co.uk", extractedUsername);
    }

    @Test
    @DisplayName("Test 21: Validate token - Same username, same token = valid")
    void testValidateToken_SameUsernameSameToken() {
        // When
        Boolean isValid1 = jwtUtil.validateToken(validToken, userDetails);
        Boolean isValid2 = jwtUtil.validateToken(validToken, userDetails);

        // Then
        assertTrue(isValid1);
        assertTrue(isValid2);
    }

    // Helper method
    private SecretKey getTestSigningKey() {
        byte[] keyBytes = TEST_SECRET.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
