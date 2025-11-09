package iuh.student.www.controller.rest.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import iuh.student.www.entity.Product;
import iuh.student.www.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/products")
@RequiredArgsConstructor
@Tag(name = "Admin - Products", description = "Product management API (Admin only)")
public class AdminProductRestController {

    private final ProductService productService;

    @Operation(summary = "Get all products", description = "Retrieve list of all products (active and inactive)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Products retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Product.class)))
    })
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @Operation(summary = "Search products", description = "Search products by name or description (all products)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Search completed successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Product.class)))
    })
    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProducts(
            @Parameter(description = "Search keyword", required = true)
            @RequestParam String keyword) {
        List<Product> products = productService.searchAllProducts(keyword);
        return ResponseEntity.ok(products);
    }

    @Operation(summary = "Get product by ID", description = "Retrieve detailed information of a specific product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Product.class))),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(
            @Parameter(description = "Product ID", required = true)
            @PathVariable Long id) {
        return productService.getProductById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Create new product", description = "Create a new product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Product.class))),
            @ApiResponse(responseCode = "400", description = "Validation failed or category not found")
    })
    @PostMapping
    public ResponseEntity<?> createProduct(
            @Valid @RequestBody Product product,
            BindingResult result) {

        if (result.hasErrors()) {
            Map<String, String> errors = result.getFieldErrors().stream()
                    .collect(Collectors.toMap(
                            error -> error.getField(),
                            error -> error.getDefaultMessage()
                    ));
            return ResponseEntity.badRequest().body(Map.of("errors", errors));
        }

        try {
            Product savedProduct = productService.createProduct(product);
            return ResponseEntity.ok(Map.of(
                    "message", "Product created successfully",
                    "product", savedProduct
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @Operation(summary = "Update product", description = "Update an existing product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Product.class))),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "400", description = "Validation failed")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(
            @Parameter(description = "Product ID", required = true)
            @PathVariable Long id,
            @Valid @RequestBody Product product,
            BindingResult result) {

        if (result.hasErrors()) {
            Map<String, String> errors = result.getFieldErrors().stream()
                    .collect(Collectors.toMap(
                            error -> error.getField(),
                            error -> error.getDefaultMessage()
                    ));
            return ResponseEntity.badRequest().body(Map.of("errors", errors));
        }

        try {
            Product updatedProduct = productService.updateProduct(id, product);
            return ResponseEntity.ok(Map.of(
                    "message", "Product updated successfully",
                    "product", updatedProduct
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @Operation(summary = "Delete product",
               description = "Delete a product (fails if product exists in any order). Use deactivate instead if product has orders.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "400", description = "Cannot delete product that has been ordered")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(
            @Parameter(description = "Product ID", required = true)
            @PathVariable Long id) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.ok(Map.of("message", "Product deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @Operation(summary = "Check if product is in orders", description = "Check if a product exists in any orders")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Check completed",
                    content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/{id}/in-orders")
    public ResponseEntity<?> isProductInOrders(
            @Parameter(description = "Product ID", required = true)
            @PathVariable Long id) {
        boolean inOrders = productService.isProductInOrders(id);
        return ResponseEntity.ok(Map.of("inOrders", inOrders));
    }
}
