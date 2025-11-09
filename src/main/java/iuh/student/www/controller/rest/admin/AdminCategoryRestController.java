package iuh.student.www.controller.rest.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import iuh.student.www.entity.Category;
import iuh.student.www.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/categories")
@RequiredArgsConstructor
@Tag(name = "Admin - Categories", description = "Category management API (Admin only)")
public class AdminCategoryRestController {

    private final CategoryService categoryService;

    @Operation(summary = "Get all categories", description = "Retrieve list of all categories (active and inactive)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categories retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Category.class)))
    })
    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @Operation(summary = "Search categories", description = "Search categories by name (case-insensitive)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Search completed successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Category.class)))
    })
    @GetMapping("/search")
    public ResponseEntity<List<Category>> searchCategories(
            @Parameter(description = "Search keyword", required = true)
            @RequestParam String keyword) {
        List<Category> categories = categoryService.getAllCategories().stream()
                .filter(c -> c.getName().toLowerCase().contains(keyword.toLowerCase()) ||
                            (c.getDescription() != null && c.getDescription().toLowerCase().contains(keyword.toLowerCase())))
                .collect(Collectors.toList());
        return ResponseEntity.ok(categories);
    }

    @Operation(summary = "Get category by ID", description = "Retrieve detailed information of a specific category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Category.class))),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoryById(
            @Parameter(description = "Category ID", required = true)
            @PathVariable Long id) {
        return categoryService.getCategoryById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Create new category", description = "Create a new product category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Category.class))),
            @ApiResponse(responseCode = "400", description = "Validation failed or category name already exists")
    })
    @PostMapping
    public ResponseEntity<?> createCategory(
            @Valid @RequestBody Category category,
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
            Category savedCategory = categoryService.createCategory(category);
            return ResponseEntity.ok(Map.of(
                    "message", "Category created successfully",
                    "category", savedCategory
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @Operation(summary = "Update category", description = "Update an existing category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Category.class))),
            @ApiResponse(responseCode = "404", description = "Category not found"),
            @ApiResponse(responseCode = "400", description = "Validation failed")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategory(
            @Parameter(description = "Category ID", required = true)
            @PathVariable Long id,
            @Valid @RequestBody Category category,
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
            Category updatedCategory = categoryService.updateCategory(id, category);
            return ResponseEntity.ok(Map.of(
                    "message", "Category updated successfully",
                    "category", updatedCategory
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @Operation(summary = "Delete category",
               description = "Delete a category (fails if category has products)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Category not found"),
            @ApiResponse(responseCode = "400", description = "Cannot delete category with existing products")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(
            @Parameter(description = "Category ID", required = true)
            @PathVariable Long id) {
        try {
            categoryService.deleteCategory(id);
            return ResponseEntity.ok(Map.of("message", "Category deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
