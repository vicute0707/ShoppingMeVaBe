package iuh.student.www.controller.admin;

import iuh.student.www.entity.User;
import iuh.student.www.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final UserService userService;

    @GetMapping
    public String listUsers(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "admin/users/list";
    }

    @GetMapping("/{id}")
    public String viewUser(@PathVariable Long id, Model model) {
        User user = userService.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        model.addAttribute("user", user);
        return "admin/users/detail";
    }

    @GetMapping("/{id}/edit")
    public String editUserForm(@PathVariable Long id, Model model) {
        User user = userService.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        model.addAttribute("user", user);
        return "admin/users/form";
    }

    @PostMapping("/{id}")
    public String updateUser(
            @PathVariable Long id,
            @Valid @ModelAttribute("user") User user,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            user.setId(id);
            return "admin/users/form";
        }

        try {
            userService.updateUser(id, user);
            redirectAttributes.addFlashAttribute("successMessage",
                "Người dùng đã được cập nhật thành công!");
            return "redirect:/admin/users";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            user.setId(id);
            return "admin/users/form";
        }
    }

    @PostMapping("/{id}/delete")
    public String deleteUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            userService.deleteUser(id);
            redirectAttributes.addFlashAttribute("successMessage",
                "Người dùng đã được xóa thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                "Lỗi: " + e.getMessage());
        }
        return "redirect:/admin/users";
    }

    @PostMapping("/{id}/toggle-status")
    public String toggleUserStatus(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            User user = userService.toggleUserStatus(id);
            String status = user.getEnabled() ? "kích hoạt" : "vô hiệu hóa";
            redirectAttributes.addFlashAttribute("successMessage",
                "Tài khoản đã được " + status + " thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                "Lỗi: " + e.getMessage());
        }
        return "redirect:/admin/users";
    }
}
