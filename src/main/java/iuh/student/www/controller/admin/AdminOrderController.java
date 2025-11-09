package iuh.student.www.controller.admin;

import iuh.student.www.entity.Order;
import iuh.student.www.entity.OrderDetail;
import iuh.student.www.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/orders")
@RequiredArgsConstructor
public class AdminOrderController {

    private final OrderService orderService;

    @GetMapping
    public String listOrders(Model model) {
        List<Order> orders = orderService.getAllOrders();
        model.addAttribute("orders", orders);
        return "admin/orders/list";
    }

    @GetMapping("/{id}")
    public String viewOrder(@PathVariable Long id, Model model) {
        Order order = orderService.getOrderById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        model.addAttribute("order", order);
        return "admin/orders/detail";
    }

    @PostMapping("/{id}/status")
    public String updateOrderStatus(
            @PathVariable Long id,
            @RequestParam("status") Order.OrderStatus status,
            RedirectAttributes redirectAttributes
    ) {
        try {
            orderService.updateOrderStatus(id, status);
            redirectAttributes.addFlashAttribute("successMessage",
                "Order status updated successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin/orders/" + id;
    }

    @PostMapping("/details/{detailId}/update")
    public String updateOrderDetail(
            @PathVariable Long detailId,
            @RequestParam("quantity") Integer quantity,
            @RequestParam("orderId") Long orderId,
            RedirectAttributes redirectAttributes
    ) {
        try {
            orderService.updateOrderDetail(detailId, quantity);
            redirectAttributes.addFlashAttribute("successMessage",
                "Order item updated successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin/orders/" + orderId;
    }
}
