package iuh.student.www.controller;

import iuh.student.www.entity.Category;
import iuh.student.www.entity.Product;
import iuh.student.www.service.CategoryService;
import iuh.student.www.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final ProductService productService;
    private final CategoryService categoryService;

    @GetMapping({"/", "/home"})
    public String home(Model model) {
        List<Product> products = productService.getActiveProducts();
        List<Category> categories = categoryService.getActiveCategories();

        model.addAttribute("products", products);
        model.addAttribute("categories", categories);

        return "guest/home";
    }

    @GetMapping("/products")
    public String products(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String search,
            Model model
    ) {
        List<Product> products;
        List<Category> categories = categoryService.getActiveCategories();

        if (search != null && !search.trim().isEmpty()) {
            products = productService.searchProducts(search);
            model.addAttribute("search", search);
        } else if (categoryId != null) {
            products = productService.getProductsByCategory(categoryId);
            model.addAttribute("selectedCategoryId", categoryId);
        } else {
            products = productService.getActiveProducts();
        }

        model.addAttribute("products", products);
        model.addAttribute("categories", categories);

        return "guest/products";
    }

    @GetMapping("/products/detail")
    public String productDetail(@RequestParam Long id, Model model) {
        Product product = productService.getProductById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        model.addAttribute("product", product);

        return "guest/product-detail";
    }
}
