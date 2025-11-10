package iuh.student.www.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT Authentication Filter
 * Intercept requests and validate JWT tokens
 * CRITICAL: All exceptions are caught to prevent 500 errors from invalid JWTs
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // Skip JWT authentication for logout and login endpoints
        // /logout: Allows Spring Security's logout handler to properly clear cookies
        // /login: Prevents auto-login from old JWT cookie after logout
        // /register, /perform-login: Public endpoints that don't need JWT
        String requestURI = request.getRequestURI();
        if (requestURI.equals("/logout") ||
            requestURI.equals("/clear-cookies") ||
            requestURI.equals("/login") ||
            requestURI.equals("/register") ||
            requestURI.equals("/perform-login")) {
            log.debug("Skipping JWT authentication for public endpoint: {}", requestURI);
            filterChain.doFilter(request, response);
            return;
        }

        // CRITICAL: Wrap everything in try-catch to prevent crashes from bad JWT tokens
        try {
            final String authorizationHeader = request.getHeader("Authorization");

            String username = null;
            String jwt = null;

            // Extract JWT from Authorization header (for API calls)
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                jwt = authorizationHeader.substring(7);
                try {
                    username = jwtUtil.extractUsername(jwt);
                    log.debug("JWT token found in header for user: {}", username);
                } catch (Exception e) {
                    log.warn("Invalid JWT in Authorization header, ignoring: {}", e.getMessage());
                    jwt = null;
                    username = null;
                }
            }

            // If no JWT in header, try to get from cookie (for web login)
            if (jwt == null && request.getCookies() != null) {
                for (jakarta.servlet.http.Cookie cookie : request.getCookies()) {
                    if ("JWT_TOKEN".equals(cookie.getName())) {
                        String cookieValue = cookie.getValue();
                        // Skip empty or very short tokens
                        if (cookieValue != null && cookieValue.length() > 10) {
                            try {
                                username = jwtUtil.extractUsername(cookieValue);
                                jwt = cookieValue;
                                log.debug("JWT token found in cookie for user: {}", username);
                            } catch (Exception e) {
                                log.warn("Invalid JWT token in cookie, ignoring: {}", e.getMessage());
                                jwt = null;
                                username = null;
                            }
                        }
                        break;
                    }
                }
            }

            // Validate token and set authentication
            if (username != null && jwt != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                try {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                    if (jwtUtil.validateToken(jwt, userDetails)) {
                        UsernamePasswordAuthenticationToken authToken =
                                new UsernamePasswordAuthenticationToken(
                                        userDetails,
                                        null,
                                        userDetails.getAuthorities()
                                );
                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                        log.debug("User {} authenticated via JWT", username);
                    } else {
                        log.warn("JWT token validation failed for user: {}", username);
                    }
                } catch (Exception e) {
                    log.error("Authentication failed for user {}: {}", username, e.getMessage());
                    // Don't set authentication - user will be anonymous
                }
            }

        } catch (Exception e) {
            // CRITICAL: Catch ALL exceptions to prevent 500 errors
            log.error("JWT Filter encountered unexpected error, continuing as anonymous user: {}", e.getMessage());
            // Don't throw - let the request continue without authentication
        }

        // Always continue the filter chain, even if JWT processing failed
        filterChain.doFilter(request, response);
    }
}
