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
        // Clear Spring Security Context first
        if (authentication != null) {
            new org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler()
                    .logout(request, response, authentication);
            log.info("User {} logged out - SecurityContext cleared", authentication.getName());
        }

        // Clear JWT cookie
        Cookie jwtCookie = new Cookie("JWT_TOKEN", null);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(0); // Delete immediately
        response.addCookie(jwtCookie);

        // Clear all other cookies
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                Cookie deleteCookie = new Cookie(cookie.getName(), null);
                deleteCookie.setPath("/");
                deleteCookie.setMaxAge(0);
                response.addCookie(deleteCookie);
                log.debug("Deleted cookie: {}", cookie.getName());
            }
        }

        // Add cache control headers to prevent caching
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "0");

        log.info("Logout completed - All cookies cleared");

        // Return logout page that will clear cookies with JavaScript and redirect
        return "guest/logout";
    }

    /**
     * Force clear all cookies - Use this if you have login issues
     */
    @GetMapping("/clear-cookies")
    public String clearCookies(HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
        // Clear ALL cookies aggressively
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                // Create new cookie with same name but empty value
                Cookie deleteCookie = new Cookie(cookie.getName(), "");
                deleteCookie.setPath("/");
                deleteCookie.setMaxAge(0);
                deleteCookie.setHttpOnly(true);
                response.addCookie(deleteCookie);

                log.info("Deleted cookie: {}", cookie.getName());
            }
        }

        // Also add cache control headers to prevent caching
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "0");

        log.info("All cookies forcefully cleared!");
        redirectAttributes.addFlashAttribute("successMessage", "Đã xóa tất cả cookies! Bạn có thể đăng nhập lại.");
        return "redirect:/login";
    }

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "common/access-denied";
    }
}
