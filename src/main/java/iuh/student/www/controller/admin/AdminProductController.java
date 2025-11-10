package iuh.student.www.controller.admin;

import iuh.student.www.entity.Category;
import iuh.student.www.entity.Product;
import iuh.student.www.service.CategoryService;
import iuh.student.www.service.FileStorageService;
import iuh.student.www.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/products")
@RequiredArgsConstructor
@Slf4j
public class AdminProductController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final FileStorageService fileStorageService;

    @GetMapping
    public String listProducts(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Boolean active,
            @RequestParam(required = false) String sortBy,
            Model model
    ) {
        List<Product> products;

        // Apply filters
        if (search != null && !search.trim().isEmpty()) {
            products = productService.searchAllProducts(search);
            model.addAttribute("search", search);
        } else {
            products = productService.getAllProducts();
        }

        // Filter by category
        if (categoryId != null) {
            products = products.stream()
                    .filter(p -> p.getCategory().getId().equals(categoryId))
                    .collect(java.util.stream.Collectors.toList());
            model.addAttribute("categoryId", categoryId);
        }

        // Filter by status
        if (active != null) {
            products = products.stream()
                    .filter(p -> p.getActive().equals(active))
                    .collect(java.util.stream.Collectors.toList());
            model.addAttribute("active", active);
        }

        // Sort products
        if (sortBy != null && !sortBy.isEmpty()) {
            switch (sortBy) {
                case "priceAsc":
                    products.sort((a, b) -> a.getPrice().compareTo(b.getPrice()));
                    break;
                case "priceDesc":
                    products.sort((a, b) -> b.getPrice().compareTo(a.getPrice()));
                    break;
                case "nameAsc":
                    products.sort((a, b) -> a.getName().compareTo(b.getName()));
                    break;
                case "nameDesc":
                    products.sort((a, b) -> b.getName().compareTo(a.getName()));
                    break;
                case "stockAsc":
                    products.sort((a, b) -> a.getStockQuantity().compareTo(b.getStockQuantity()));
                    break;
                case "stockDesc":
                    products.sort((a, b) -> b.getStockQuantity().compareTo(a.getStockQuantity()));
                    break;
            }
            model.addAttribute("sortBy", sortBy);
        }

        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("products", products);
        model.addAttribute("categories", categories);
        return "admin/products/list";
    }

    @GetMapping("/new")
    public String newProductForm(Model model) {
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categories);
        return "admin/products/form";
    }

    @PostMapping
    public String createProduct(
            @Valid @ModelAttribute("product") Product product,
            BindingResult bindingResult,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
            RedirectAttributes redirectAttributes,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            List<Category> categories = categoryService.getAllCategories();
            model.addAttribute("categories", categories);
            return "admin/products/form";
        }

        try {
            // Upload image nếu có
            if (imageFile != null && !imageFile.isEmpty()) {
                String imageUrl = fileStorageService.storeProductImage(imageFile);
                product.setImageUrl(imageUrl);
                log.info("Uploaded image for product: {}", product.getName());
            }

            productService.createProduct(product);
            redirectAttributes.addFlashAttribute("successMessage",
                "Sản phẩm đã được tạo thành công!");
            return "redirect:/admin/products";
        } catch (Exception e) {
            log.error("Failed to create product: {}", e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
            List<Category> categories = categoryService.getAllCategories();
            model.addAttribute("categories", categories);
            return "admin/products/form";
        }
    }

    @GetMapping("/{id}/edit")
    public String editProductForm(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("product", product);
        model.addAttribute("categories", categories);
        return "admin/products/form";
    }

    @PostMapping("/{id}")
    public String updateProduct(
            @PathVariable Long id,
            @Valid @ModelAttribute("product") Product product,
            BindingResult bindingResult,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
            RedirectAttributes redirectAttributes,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            product.setId(id);
            List<Category> categories = categoryService.getAllCategories();
            model.addAttribute("categories", categories);
            return "admin/products/form";
        }

        try {
            // Get existing product to check old image
            Product existingProduct = productService.getProductById(id)
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            // Upload new image nếu có
            if (imageFile != null && !imageFile.isEmpty()) {
                // Delete old image if exists
                if (existingProduct.getImageUrl() != null) {
                    fileStorageService.deleteFile(existingProduct.getImageUrl());
                }

                // Upload new image
                String imageUrl = fileStorageService.storeProductImage(imageFile);
                product.setImageUrl(imageUrl);
                log.info("Updated image for product: {}", product.getName());
            } else {
                // Keep old image if no new image uploaded
                product.setImageUrl(existingProduct.getImageUrl());
            }

            productService.updateProduct(id, product);
            redirectAttributes.addFlashAttribute("successMessage",
                "Sản phẩm đã được cập nhật thành công!");
            return "redirect:/admin/products";
        } catch (Exception e) {
            log.error("Failed to update product: {}", e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
            product.setId(id);
            List<Category> categories = categoryService.getAllCategories();
            model.addAttribute("categories", categories);
            return "admin/products/form";
        }
    }

    @PostMapping("/{id}/delete")
    public String deleteProduct(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            // Get product to delete its image
            Product product = productService.getProductById(id)
                    .orElseThrow(() -> new Exception("Không tìm thấy sản phẩm"));

            // Delete image file if exists
            if (product.getImageUrl() != null && !product.getImageUrl().isEmpty()) {
                fileStorageService.deleteFile(product.getImageUrl());
            }

            productService.deleteProduct(id);
            redirectAttributes.addFlashAttribute("successMessage",
                "Sản phẩm đã được xóa thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                "Lỗi: " + e.getMessage());
        }
        return "redirect:/admin/products";
    }

    @PostMapping("/{id}/toggle-status")
    public String toggleProductStatus(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Product product = productService.getProductById(id)
                    .orElseThrow(() -> new Exception("Không tìm thấy sản phẩm"));

            // Toggle active status
            product.setActive(!product.getActive());
            productService.updateProduct(id, product);

            String status = product.getActive() ? "hiển thị" : "ẩn";
            redirectAttributes.addFlashAttribute("successMessage",
                "Sản phẩm đã được " + status + " thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                "Lỗi: " + e.getMessage());
        }
        return "redirect:/admin/products";
    }
}
