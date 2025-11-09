package iuh.student.www.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Shopping Store REST API",
                version = "1.0",
                description = """
                        # Shopping Store API Documentation

                        Comprehensive REST API for Shopping Store e-commerce platform.

                        ## User Types:
                        - **Guest**: Browse products, manage cart, register account
                        - **Customer**: All Guest features + checkout, view orders
                        - **Admin**: All Customer features + CRUD operations for products, categories, users, orders

                        ## Features:
                        - Product management (CRUD)
                        - Category management (CRUD)
                        - Shopping cart (Session-based)
                        - Order processing
                        - User management
                        - Email notifications
                        - Role-based access control

                        ## Authentication:
                        Use the login endpoint to authenticate. The API uses session-based authentication.
                        """,
                contact = @Contact(
                        name = "Shopping Store Team",
                        email = "support@shoppingstore.com",
                        url = "https://shoppingstore.com"
                ),
                license = @License(
                        name = "MIT License",
                        url = "https://opensource.org/licenses/MIT"
                )
        ),
        servers = {
                @Server(
                        url = "http://localhost:8080",
                        description = "Local Development Server"
                ),
                @Server(
                        url = "https://api.shoppingstore.com",
                        description = "Production Server"
                )
        },
        security = @SecurityRequirement(name = "session")
)
@SecurityScheme(
        name = "session",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        description = "Session-based authentication. Login first to get authenticated session."
)
public class OpenAPIConfig {
}
