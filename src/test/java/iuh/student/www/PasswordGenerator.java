package iuh.student.www;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Password Generator cho testing
 * Tạo BCrypt hash cho password "admin123"
 */
public class PasswordGenerator {

    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        String rawPassword = "admin123";
        String encodedPassword = encoder.encode(rawPassword);

        System.out.println("========================================");
        System.out.println("Password Generator");
        System.out.println("========================================");
        System.out.println("Raw Password: " + rawPassword);
        System.out.println("BCrypt Hash:  " + encodedPassword);
        System.out.println("========================================");

        // Verify
        boolean matches = encoder.matches(rawPassword, encodedPassword);
        System.out.println("Verification: " + (matches ? "✅ MATCH" : "❌ NO MATCH"));
        System.out.println("========================================");

        // Test với hash từ database
        String dbHash = "$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy";
        boolean dbMatches = encoder.matches(rawPassword, dbHash);
        System.out.println("Database Hash Test:");
        System.out.println("Hash: " + dbHash);
        System.out.println("Match: " + (dbMatches ? "✅ MATCH" : "❌ NO MATCH"));
        System.out.println("========================================");

        // SQL Update Statement
        System.out.println("\nSQL UPDATE Statement:");
        System.out.println("UPDATE users SET password = '" + encodedPassword + "' WHERE email = 'admin@shopmevabe.com';");
        System.out.println("========================================");
    }
}
