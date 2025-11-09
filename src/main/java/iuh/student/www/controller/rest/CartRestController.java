package iuh.student.www.controller.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import iuh.student.www.entity.Product;
import iuh.student.www.model.Cart;
import iuh.student.www.model.CartItem;
import iuh.student.www.service.ProductService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/public/cart")
@RequiredArgsConstructor
@Tag(name = "Guest - Shopping Cart", description = "Session-based shopping cart API (Guest access)")
public class CartRestController {

    private final ProductService productService;

    private Cart getOrCreateCart(HttpSession session) {
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null) {
            cart = new Cart();
            session.setAttribute("cart", cart);
        }
        return cart;
    }

    @Operation(summary = "Get cart contents", description = "Retrieve current shopping cart from session")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cart retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Cart.class)))
    })
    @GetMapping
    public ResponseEntity<Cart> getCart(HttpSession session) {
        Cart cart = getOrCreateCart(session);
        return ResponseEntity.ok(cart);
    }

    @Operation(summary = "Add product to cart", description = "Add a product to shopping cart or increase quantity if already exists")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product added to cart successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "400", description = "Invalid quantity")
    })
    @PostMapping("/add")
    public ResponseEntity<?> addToCart(
            @Parameter(description = "Product ID", required = true)
            @RequestParam Long productId,
            @Parameter(description = "Quantity to add", required = true)
            @RequestParam Integer quantity,
            HttpSession session) {

        if (quantity == null || quantity <= 0) {
            return ResponseEntity.badRequest().body(Map.of("error", "Quantity must be greater than 0"));
        }

        Product product = productService.getProductById(productId)
                .orElse(null);

        if (product == null) {
            return ResponseEntity.notFound().build();
        }

        if (!product.getActive()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Product is not available"));
        }

        if (product.getStockQuantity() < quantity) {
            return ResponseEntity.badRequest().body(Map.of("error", "Insufficient stock"));
        }

        Cart cart = getOrCreateCart(session);
        CartItem item = new CartItem(
                product.getId(),
                product.getName(),
                product.getPrice(),
                quantity,
                product.getImageUrl()
        );
        cart.addItem(item);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Product added to cart successfully");
        response.put("cart", cart);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Update cart item quantity", description = "Update quantity of a product in cart (set to 0 to remove)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cart updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid quantity")
    })
    @PutMapping("/update")
    public ResponseEntity<?> updateCart(
            @Parameter(description = "Product ID", required = true)
            @RequestParam Long productId,
            @Parameter(description = "New quantity (0 to remove)", required = true)
            @RequestParam Integer quantity,
            HttpSession session) {

        if (quantity == null || quantity < 0) {
            return ResponseEntity.badRequest().body(Map.of("error", "Quantity must be 0 or greater"));
        }

        Cart cart = getOrCreateCart(session);
        cart.updateQuantity(productId, quantity);

        Map<String, Object> response = new HashMap<>();
        response.put("message", quantity == 0 ? "Item removed from cart" : "Cart updated successfully");
        response.put("cart", cart);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Remove product from cart", description = "Remove a specific product from shopping cart")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product removed successfully")
    })
    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<?> removeFromCart(
            @Parameter(description = "Product ID", required = true)
            @PathVariable Long productId,
            HttpSession session) {

        Cart cart = getOrCreateCart(session);
        cart.removeItem(productId);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Product removed from cart");
        response.put("cart", cart);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Clear cart", description = "Remove all items from shopping cart")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cart cleared successfully")
    })
    @DeleteMapping("/clear")
    public ResponseEntity<?> clearCart(HttpSession session) {
        Cart cart = getOrCreateCart(session);
        cart.clear();

        return ResponseEntity.ok(Map.of("message", "Cart cleared successfully"));
    }
}
