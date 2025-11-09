package iuh.student.www.controller;

import iuh.student.www.entity.Product;
import iuh.student.www.model.Cart;
import iuh.student.www.model.CartItem;
import iuh.student.www.service.ProductService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final ProductService productService;

    private Cart getCart(HttpSession session) {
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null) {
            cart = new Cart();
            session.setAttribute("cart", cart);
        }
        return cart;
    }

    @GetMapping
    public String viewCart(HttpSession session, Model model) {
        Cart cart = getCart(session);
        model.addAttribute("cart", cart);
        return "guest/cart";
    }

    @PostMapping("/add")
    public String addToCart(
            @RequestParam Long productId,
            @RequestParam(defaultValue = "1") Integer quantity,
            HttpSession session,
            RedirectAttributes redirectAttributes
    ) {
        try {
            Product product = productService.getProductById(productId)
                    .orElseThrow(() -> new Exception("Product not found"));

            if (!product.getActive()) {
                throw new Exception("Product is not available");
            }

            if (product.getStockQuantity() < quantity) {
                throw new Exception("Insufficient stock");
            }

            Cart cart = getCart(session);
            CartItem cartItem = new CartItem(
                    product.getId(),
                    product.getName(),
                    product.getPrice(),
                    quantity,
                    product.getImageUrl()
            );

            cart.addItem(cartItem);
            session.setAttribute("cart", cart);

            redirectAttributes.addFlashAttribute("successMessage",
                "Product added to cart successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/cart";
    }

    @PostMapping("/update")
    public String updateCart(
            @RequestParam Long productId,
            @RequestParam Integer quantity,
            HttpSession session,
            RedirectAttributes redirectAttributes
    ) {
        try {
            Cart cart = getCart(session);
            cart.updateQuantity(productId, quantity);
            session.setAttribute("cart", cart);

            redirectAttributes.addFlashAttribute("successMessage",
                "Cart updated successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/cart";
    }

    @PostMapping("/remove")
    public String removeFromCart(
            @RequestParam Long productId,
            HttpSession session,
            RedirectAttributes redirectAttributes
    ) {
        Cart cart = getCart(session);
        cart.removeItem(productId);
        session.setAttribute("cart", cart);

        redirectAttributes.addFlashAttribute("successMessage",
            "Product removed from cart");

        return "redirect:/cart";
    }

    @PostMapping("/clear")
    public String clearCart(HttpSession session, RedirectAttributes redirectAttributes) {
        Cart cart = getCart(session);
        cart.clear();
        session.setAttribute("cart", cart);

        redirectAttributes.addFlashAttribute("successMessage", "Cart cleared");

        return "redirect:/cart";
    }
}
