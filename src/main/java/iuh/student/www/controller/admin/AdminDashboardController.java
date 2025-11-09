package iuh.student.www.controller.admin;

import iuh.student.www.service.CategoryService;
import iuh.student.www.service.OrderService;
import iuh.student.www.service.ProductService;
import iuh.student.www.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminDashboardController {

    private final UserService userService;
    private final ProductService productService;
    private final CategoryService categoryService;
    private final OrderService orderService;

    @GetMapping({"/", "/dashboard"})
    public String dashboard(Model model) {
        long totalUsers = userService.getAllCustomers().size();
        long totalProducts = productService.getAllProducts().size();
        long totalCategories = categoryService.getAllCategories().size();
        long totalOrders = orderService.getAllOrders().size();

        model.addAttribute("totalUsers", totalUsers);
        model.addAttribute("totalProducts", totalProducts);
        model.addAttribute("totalCategories", totalCategories);
        model.addAttribute("totalOrders", totalOrders);

        return "admin/dashboard";
    }
}
