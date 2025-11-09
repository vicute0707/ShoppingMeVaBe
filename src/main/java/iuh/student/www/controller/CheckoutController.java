package iuh.student.www.controller;

import iuh.student.www.dto.CheckoutDTO;
import iuh.student.www.entity.Order;
import iuh.student.www.entity.User;
import iuh.student.www.model.Cart;
import iuh.student.www.service.OrderService;
import iuh.student.www.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/checkout")
@RequiredArgsConstructor
public class CheckoutController {

    private final OrderService orderService;
    private final UserService userService;

    @GetMapping
    public String checkoutPage(
            HttpSession session,
            Authentication authentication,
            Model model
    ) {
        Cart cart = (Cart) session.getAttribute("cart");

        if (cart == null || cart.isEmpty()) {
            return "redirect:/cart";
        }

        User user = userService.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        CheckoutDTO checkoutDTO = new CheckoutDTO();
        checkoutDTO.setShippingAddress(user.getAddress());
        checkoutDTO.setPhone(user.getPhone());

        model.addAttribute("cart", cart);
        model.addAttribute("checkoutDTO", checkoutDTO);
        model.addAttribute("user", user);

        return "customer/checkout";
    }

    @PostMapping
    public String processCheckout(
            @Valid @ModelAttribute("checkoutDTO") CheckoutDTO checkoutDTO,
            BindingResult bindingResult,
            HttpSession session,
            Authentication authentication,
            RedirectAttributes redirectAttributes,
            Model model
    ) {
        Cart cart = (Cart) session.getAttribute("cart");

        if (cart == null || cart.isEmpty()) {
            return "redirect:/cart";
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("cart", cart);
            return "customer/checkout";
        }

        try {
            User user = userService.findByEmail(authentication.getName())
                    .orElseThrow(() -> new Exception("User not found"));

            Order order = orderService.createOrder(user, cart, checkoutDTO);

            // Clear cart after successful order
            cart.clear();
            session.setAttribute("cart", cart);

            redirectAttributes.addFlashAttribute("successMessage",
                "Order placed successfully! Order ID: #" + order.getId());
            return "redirect:/checkout/orders/" + order.getId();
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("cart", cart);
            return "customer/checkout";
        }
    }

    @GetMapping("/orders")
    public String viewOrders(Authentication authentication, Model model) {
        User user = userService.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Order> orders = orderService.getOrdersByUser(user.getId());
        model.addAttribute("orders", orders);

        return "customer/orders";
    }

    @GetMapping("/orders/{id}")
    public String viewOrderDetail(@PathVariable Long id, Authentication authentication, Model model) {
        User user = userService.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Order order = orderService.getOrderById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // Check if order belongs to user
        if (!order.getUser().getId().equals(user.getId())) {
            return "redirect:/access-denied";
        }

        model.addAttribute("order", order);

        return "customer/order-detail";
    }
}
