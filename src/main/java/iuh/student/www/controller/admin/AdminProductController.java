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
    public String listProducts(@RequestParam(required = false) String search, Model model) {
        List<Product> products;

        if (search != null && !search.trim().isEmpty()) {
            products = productService.searchAllProducts(search);
            model.addAttribute("search", search);
        } else {
            products = productService.getAllProducts();
        }

        model.addAttribute("products", products);
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
                log.info("✅ Uploaded image for product: {}", product.getName());
            }

            productService.createProduct(product);
            redirectAttributes.addFlashAttribute("successMessage",
                "🎉 Product created successfully with image!");
            return "redirect:/admin/products";
        } catch (Exception e) {
            log.error("❌ Failed to create product: {}", e.getMessage());
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
                log.info("✅ Updated image for product: {}", product.getName());
            } else {
                // Keep old image if no new image uploaded
                product.setImageUrl(existingProduct.getImageUrl());
            }

            productService.updateProduct(id, product);
            redirectAttributes.addFlashAttribute("successMessage",
                "✨ Product updated successfully!");
            return "redirect:/admin/products";
        } catch (Exception e) {
            log.error("❌ Failed to update product: {}", e.getMessage());
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
            productService.deleteProduct(id);
            redirectAttributes.addFlashAttribute("successMessage",
                "Product deleted successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin/products";
    }
}
