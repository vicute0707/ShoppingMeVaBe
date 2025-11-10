package iuh.student.www.controller;

import iuh.student.www.dto.RegisterDTO;
import iuh.student.www.entity.User;
import iuh.student.www.security.CustomUserDetailsService;
import iuh.student.www.security.JwtUtil;
import iuh.student.www.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;

    @GetMapping("/login")
    public String loginPage(
            @ModelAttribute("error") String error,
            @ModelAttribute("logout") String logout,
            Authentication authentication,
            Model model
    ) {
        // Redirect authenticated users away from login page
        if (authentication != null && authentication.isAuthenticated()
                && !authentication.getPrincipal().equals("anonymousUser")) {
            // Check if admin or customer
            boolean isAdmin = authentication.getAuthorities().stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
            return isAdmin ? "redirect:/admin/dashboard" : "redirect:/";
        }

        if (error != null) {
            model.addAttribute("errorMessage", "Email hoặc mật khẩu không đúng!");
        }
        if (logout != null) {
            model.addAttribute("successMessage", "Đăng xuất thành công!");
        }
        return "guest/login";
    }

    @PostMapping("/perform-login")
    public String performLogin(
            @RequestParam("username") String email,
            @RequestParam("password") String password,
            HttpServletResponse response,
            RedirectAttributes redirectAttributes
    ) {
        try {
            // Authenticate user
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );

            // Generate JWT token
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);
            String jwtToken = jwtUtil.generateToken(userDetails);

            // Store JWT in cookie
            Cookie jwtCookie = new Cookie("JWT_TOKEN", jwtToken);
            jwtCookie.setHttpOnly(true);
            jwtCookie.setPath("/");
            jwtCookie.setMaxAge(24 * 60 * 60); // 24 hours
            response.addCookie(jwtCookie);

            log.info("User {} logged in successfully via web form", email);

            // Redirect based on role
            boolean isAdmin = userDetails.getAuthorities().stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

            if (isAdmin) {
                return "redirect:/admin/dashboard";
            } else {
                return "redirect:/";
            }

        } catch (AuthenticationException e) {
            log.error("Login failed for user {}: {}", email, e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Email hoặc mật khẩu không đúng!");
            return "redirect:/login";
        }
    }

    @GetMapping("/login-success")
    public String loginSuccess(Authentication authentication) {
        // Redirect based on role
        for (GrantedAuthority authority : authentication.getAuthorities()) {
            if (authority.getAuthority().equals("ROLE_ADMIN")) {
                return "redirect:/admin/dashboard";
            }
        }
        return "redirect:/";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("registerDTO", new RegisterDTO());
        return "guest/register";
    }

    @PostMapping("/register")
    public String register(
            @Valid @ModelAttribute("registerDTO") RegisterDTO registerDTO,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            return "guest/register";
        }

        try {
            userService.registerUser(registerDTO);
            redirectAttributes.addFlashAttribute("successMessage",
                "Registration successful! Please login.");
            return "redirect:/login";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "guest/register";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response,
                        Authentication authentication, RedirectAttributes redirectAttributes) {

        String username = authentication != null ? authentication.getName() : "anonymous";
        log.info("Starting logout process for user: {}", username);

        // Step 1: Clear Spring Security Context FIRST
        if (authentication != null) {
            new org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler()
                    .logout(request, response, authentication);
            log.info("SecurityContext cleared for user: {}", username);
        }

        // Step 2: Invalidate HTTP Session (to remove JSESSIONID)
        try {
            if (request.getSession(false) != null) {
                request.getSession().invalidate();
                log.info("HTTP Session invalidated for user: {}", username);
            }
        } catch (Exception e) {
            log.warn("Failed to invalidate session: {}", e.getMessage());
        }

        // Step 3: Aggressively delete JWT_TOKEN cookie with multiple variations
        // This ensures the cookie is deleted regardless of how it was set
        deleteCookieAllVariants(response, "JWT_TOKEN");
        log.info("JWT_TOKEN cookie deleted with all variants");

        // Step 4: Aggressively delete JSESSIONID cookie with multiple variations
        deleteCookieAllVariants(response, "JSESSIONID");
        log.info("JSESSIONID cookie deleted with all variants");

        // Step 5: Delete all other cookies
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                String cookieName = cookie.getName();
                if (!cookieName.equals("JWT_TOKEN") && !cookieName.equals("JSESSIONID")) {
                    deleteCookieAllVariants(response, cookieName);
                    log.info("Deleted cookie: {}", cookieName);
                }
            }
        }

        // Step 6: Add cache control headers to prevent caching
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate, private");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "0");
        response.setHeader("Clear-Site-Data", "\"cache\", \"cookies\", \"storage\"");

        log.info("Logout completed successfully for user: {}", username);

        // Redirect directly to login page with logout parameter
        redirectAttributes.addFlashAttribute("successMessage", "Đăng xuất thành công!");
        return "redirect:/login?logout=true";
    }

    /**
     * Helper method to delete a cookie with all possible variations
     * This ensures the cookie is deleted regardless of how it was originally set
     */
    private void deleteCookieAllVariants(HttpServletResponse response, String cookieName) {
        // Variant 1: Basic deletion (path=/, no HttpOnly)
        Cookie cookie1 = new Cookie(cookieName, null);
        cookie1.setPath("/");
        cookie1.setMaxAge(0);
        response.addCookie(cookie1);

        // Variant 2: With HttpOnly flag
        Cookie cookie2 = new Cookie(cookieName, "");
        cookie2.setPath("/");
        cookie2.setMaxAge(0);
        cookie2.setHttpOnly(true);
        response.addCookie(cookie2);

        // Variant 3: With Secure flag (for HTTPS)
        Cookie cookie3 = new Cookie(cookieName, "");
        cookie3.setPath("/");
        cookie3.setMaxAge(0);
        cookie3.setSecure(true);
        response.addCookie(cookie3);

        // Variant 4: With both HttpOnly and Secure
        Cookie cookie4 = new Cookie(cookieName, "");
        cookie4.setPath("/");
        cookie4.setMaxAge(0);
        cookie4.setHttpOnly(true);
        cookie4.setSecure(true);
        response.addCookie(cookie4);
    }

    /**
     * Force clear all cookies - Use this if you have login issues
     * This endpoint provides aggressive cookie cleanup for troubleshooting
     */
    @GetMapping("/clear-cookies")
    public String clearCookies(HttpServletRequest request, HttpServletResponse response,
                                Authentication authentication, RedirectAttributes redirectAttributes) {

        log.info("Starting aggressive cookie cleanup");

        // Step 1: Clear Spring Security Context if authenticated
        if (authentication != null) {
            new org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler()
                    .logout(request, response, authentication);
            log.info("SecurityContext cleared during cookie cleanup");
        }

        // Step 2: Invalidate HTTP Session if exists
        try {
            if (request.getSession(false) != null) {
                request.getSession().invalidate();
                log.info("HTTP Session invalidated during cookie cleanup");
            }
        } catch (Exception e) {
            log.warn("Failed to invalidate session during cleanup: {}", e.getMessage());
        }

        // Step 3: Delete JWT_TOKEN with all variants
        deleteCookieAllVariants(response, "JWT_TOKEN");
        log.info("JWT_TOKEN deleted with all variants during cleanup");

        // Step 4: Delete JSESSIONID with all variants
        deleteCookieAllVariants(response, "JSESSIONID");
        log.info("JSESSIONID deleted with all variants during cleanup");

        // Step 5: Clear ALL other cookies
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                String cookieName = cookie.getName();
                if (!cookieName.equals("JWT_TOKEN") && !cookieName.equals("JSESSIONID")) {
                    deleteCookieAllVariants(response, cookieName);
                    log.info("Forcefully deleted cookie: {}", cookieName);
                }
            }
        }

        // Step 6: Add cache control headers and Clear-Site-Data
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate, private");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "0");
        response.setHeader("Clear-Site-Data", "\"cache\", \"cookies\", \"storage\"");

        log.info("Aggressive cookie cleanup completed successfully!");
        redirectAttributes.addFlashAttribute("successMessage",
                "Đã xóa tất cả cookies và phiên đăng nhập! Bạn có thể đăng nhập lại.");
        return "redirect:/login";
    }

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "common/access-denied";
    }
}
