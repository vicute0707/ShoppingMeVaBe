package iuh.student.www.controller.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import iuh.student.www.entity.Product;
import iuh.student.www.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/public/products")
@RequiredArgsConstructor
@Tag(name = "Guest - Products", description = "Public API for browsing products (Guest access)")
public class ProductRestController {

    private final ProductService productService;

    @Operation(summary = "Get all active products", description = "Retrieve list of all active products available for purchase")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved products",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Product.class)))
    })
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getActiveProducts();
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

    @Operation(summary = "Search products", description = "Search products by name or description")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Search completed successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Product.class)))
    })
    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProducts(
            @Parameter(description = "Search keyword", required = true)
            @RequestParam String keyword) {
        List<Product> products = productService.searchProducts(keyword);
        return ResponseEntity.ok(products);
    }

    @Operation(summary = "Get products by category", description = "Retrieve all active products in a specific category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Products retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Product.class)))
    })
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<Product>> getProductsByCategory(
            @Parameter(description = "Category ID", required = true)
            @PathVariable Long categoryId) {
        List<Product> products = productService.getProductsByCategory(categoryId);
        return ResponseEntity.ok(products);
    }
}
