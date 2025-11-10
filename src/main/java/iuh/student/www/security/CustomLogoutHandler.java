package iuh.student.www.security;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

/**
 * Custom Logout Handler to properly delete JWT and session cookies
 * This is necessary because Spring Security's deleteCookies() doesn't always work with HttpOnly cookies
 */
@Component
@Slf4j
public class CustomLogoutHandler implements LogoutHandler {

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String username = authentication != null ? authentication.getName() : "anonymous";
        log.info("CustomLogoutHandler: Starting logout for user: {}", username);

        // Delete JWT_TOKEN cookie - MUST match exactly how it was created
        Cookie jwtCookie = new Cookie("JWT_TOKEN", "");
        jwtCookie.setHttpOnly(true);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(0);
        response.addCookie(jwtCookie);
        log.info("CustomLogoutHandler: JWT_TOKEN cookie deleted");

        // Delete JSESSIONID cookie
        Cookie jsessionCookie = new Cookie("JSESSIONID", "");
        jsessionCookie.setHttpOnly(true);
        jsessionCookie.setPath("/");
        jsessionCookie.setMaxAge(0);
        response.addCookie(jsessionCookie);
        log.info("CustomLogoutHandler: JSESSIONID cookie deleted");

        // Delete all other cookies
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                String cookieName = cookie.getName();
                if (!cookieName.equals("JWT_TOKEN") && !cookieName.equals("JSESSIONID")) {
                    Cookie deleteCookie = new Cookie(cookieName, "");
                    deleteCookie.setPath("/");
                    deleteCookie.setMaxAge(0);
                    response.addCookie(deleteCookie);
                    log.info("CustomLogoutHandler: Deleted cookie: {}", cookieName);
                }
            }
        }

        // Add headers to prevent caching
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate, private");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "0");

        log.info("CustomLogoutHandler: Logout completed for user: {}", username);
    }
}
