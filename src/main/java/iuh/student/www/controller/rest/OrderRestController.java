package iuh.student.www.controller.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import iuh.student.www.dto.CheckoutDTO;
import iuh.student.www.entity.Order;
import iuh.student.www.entity.User;
import iuh.student.www.model.Cart;
import iuh.student.www.service.OrderService;
import iuh.student.www.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/customer/orders")
@RequiredArgsConstructor
@Tag(name = "Customer - Orders", description = "Order management API (Customer/Admin access)")
public class OrderRestController {

    private final OrderService orderService;
    private final UserService userService;

    @Operation(summary = "Checkout and create order",
               description = "Create a new order from shopping cart, update stock, send email, and clear cart")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Order.class))),
            @ApiResponse(responseCode = "400", description = "Validation failed or cart is empty"),
            @ApiResponse(responseCode = "401", description = "User not authenticated")
    })
    @PostMapping("/checkout")
    public ResponseEntity<?> checkout(
            @Valid @RequestBody CheckoutDTO checkoutDTO,
            BindingResult result,
            Authentication authentication,
            HttpSession session) {

        if (result.hasErrors()) {
            Map<String, String> errors = result.getFieldErrors().stream()
                    .collect(Collectors.toMap(
                            error -> error.getField(),
                            error -> error.getDefaultMessage()
                    ));
            return ResponseEntity.badRequest().body(Map.of("errors", errors));
        }

        // Get current user
        String email = authentication.getName();
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Get cart from session
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null || cart.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Cart is empty"));
        }

        try {
            // Create order
            Order order = orderService.createOrder(user, cart, checkoutDTO);

            // Clear cart from session
            session.removeAttribute("cart");

            return ResponseEntity.ok(Map.of(
                    "message", "Order created successfully! Confirmation email has been sent.",
                    "order", order
            ));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @Operation(summary = "Get user's orders", description = "Retrieve all orders for the authenticated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orders retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Order.class))),
            @ApiResponse(responseCode = "401", description = "User not authenticated")
    })
    @GetMapping
    public ResponseEntity<List<Order>> getUserOrders(Authentication authentication) {
        String email = authentication.getName();
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Order> orders = orderService.getOrdersByUser(user.getId());
        return ResponseEntity.ok(orders);
    }

    @Operation(summary = "Get order by ID", description = "Retrieve detailed information of a specific order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Order.class))),
            @ApiResponse(responseCode = "404", description = "Order not found"),
            @ApiResponse(responseCode = "403", description = "Access denied - not your order")
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderById(
            @Parameter(description = "Order ID", required = true)
            @PathVariable Long id,
            Authentication authentication) {

        String email = authentication.getName();
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Order order = orderService.getOrderById(id)
                .orElse(null);

        if (order == null) {
            return ResponseEntity.notFound().build();
        }

        // Check if order belongs to user (unless admin)
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin && !order.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(403).body(Map.of("error", "Access denied"));
        }

        return ResponseEntity.ok(order);
    }
}
