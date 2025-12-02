package iuh.student.www.config;

import iuh.student.www.security.CustomLogoutHandler;
import iuh.student.www.security.CustomUserDetailsService;
import iuh.student.www.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Security Configuration với JWT Authentication
 * Modern stateless authentication using JWT tokens
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomLogoutHandler customLogoutHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(passwordEncoder());
        authProvider.setUserDetailsService(userDetailsService);
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, DaoAuthenticationProvider authenticationProvider) throws Exception {
        http
                .authenticationProvider(authenticationProvider)
                .csrf(csrf -> csrf.disable()) // Disable CSRF cho JWT (stateless)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Không dùng session
                )
                .authorizeHttpRequests(authorize -> authorize
                        // Public endpoints - Static resources
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/favicon.ico", "/webjars/**").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()

                        // Public web pages - Thymeleaf views (cho phép truy cập tất cả trang web)
                        .requestMatchers("/", "/home", "/error", "/login", "/register", "/logout", "/clear-cookies").permitAll()
                        .requestMatchers("/perform-login").permitAll()
                        .requestMatchers("/products", "/products/**", "/categories", "/categories/**").permitAll()
                        .requestMatchers("/cart", "/cart/**", "/about", "/contact").permitAll()

                        // Authentication APIs - Public
                        .requestMatchers("/api/auth/**").permitAll()

                        // Public REST APIs
                        .requestMatchers("/api/public/**").permitAll()

                        // Product APIs - Public read, authenticated write
                        .requestMatchers("/api/products", "/api/products/**").permitAll()
                        .requestMatchers("/api/categories", "/api/categories/**").permitAll()

                        // MoMo Payment - Public callbacks and success page
                        .requestMatchers("/payment/momo/callback", "/payment/momo/ipn", "/payment/success").permitAll()

                        // AI Chatbot - Public API
                        .requestMatchers("/api/chatbot/**").permitAll()

                        // Customer web pages - Authenticated
                        .requestMatchers("/checkout", "/checkout/**").hasAnyRole("CUSTOMER", "ADMIN")
                        .requestMatchers("/orders", "/orders/**", "/profile", "/profile/**").hasAnyRole("CUSTOMER", "ADMIN")

                        // Customer APIs - Authenticated
                        .requestMatchers("/api/customer/**").hasAnyRole("CUSTOMER", "ADMIN")
                        .requestMatchers("/api/orders/**").hasAnyRole("CUSTOMER", "ADMIN")
                        .requestMatchers("/payment/**").hasAnyRole("CUSTOMER", "ADMIN")

                        // Admin web pages & APIs - Admin only
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")

                        // All other requests are permitted (for web views)
                        .anyRequest().permitAll()
                )
                // Configure logout - Use custom handler to properly delete cookies
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout=true")
                        .addLogoutHandler(customLogoutHandler) // Custom handler to delete cookies
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .permitAll()
                )
                // Add JWT filter before UsernamePasswordAuthenticationFilter
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
