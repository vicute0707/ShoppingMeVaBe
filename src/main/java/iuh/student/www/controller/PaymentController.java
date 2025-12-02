package iuh.student.www.controller;

import iuh.student.www.dto.MoMoCallbackResponse;
import iuh.student.www.dto.MoMoPaymentResponse;
import iuh.student.www.entity.Order;
import iuh.student.www.security.CustomUserDetailsService;
import iuh.student.www.security.JwtUtil;
import iuh.student.www.service.MoMoService;
import iuh.student.www.service.OrderService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Payment Controller
 * Xử lý thanh toán cho Cửa Hàng Mẹ và Bé 🍼👶
 */
@Controller
@RequestMapping("/payment")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {

    private final MoMoService moMoService;
    private final OrderService orderService;
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;

    /**
     * Tạo thanh toán MoMo
     * Chỉ cho phép khách hàng thanh toán đơn hàng của chính họ
     *
     * @param orderId ID của đơn hàng
     * @return Redirect đến MoMo payment page
     */
    @GetMapping("/momo/create/{orderId}")
    public String createMoMoPayment(
            @PathVariable Long orderId,
            RedirectAttributes redirectAttributes,
            Authentication authentication) {
        try {
            log.info("Creating MoMo payment for order: {} by user: {}", orderId, authentication.getName());

            // Lấy thông tin đơn hàng
            Order order = orderService.findById(orderId)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng #" + orderId));

            // SECURITY: Kiểm tra quyền sở hữu đơn hàng (chỉ customer của đơn hàng hoặc admin mới được tạo payment)
            String currentUserEmail = authentication.getName();
            boolean isAdmin = authentication.getAuthorities().stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

            if (!isAdmin && !order.getUser().getEmail().equals(currentUserEmail)) {
                log.warn("Unauthorized payment creation attempt for order #{} by user {}", orderId, currentUserEmail);
                redirectAttributes.addFlashAttribute("error", "Bạn không có quyền thanh toán đơn hàng này");
                return "redirect:/checkout/orders";
            }

            // Kiểm tra trạng thái đơn hàng
            if (order.getStatus() != Order.OrderStatus.PENDING) {
                log.warn("Order #{} is not in PENDING status: {}", orderId, order.getStatus());
                redirectAttributes.addFlashAttribute("error", "Đơn hàng không ở trạng thái chờ thanh toán");
                return "redirect:/checkout/orders/" + orderId;
            }

            // Kiểm tra tổng tiền > 0
            if (order.getTotalAmount() == null || order.getTotalAmount() <= 0) {
                log.error("Invalid order amount for order #{}: {}", orderId, order.getTotalAmount());
                redirectAttributes.addFlashAttribute("error", "Số tiền đơn hàng không hợp lệ");
                return "redirect:/checkout/orders/" + orderId;
            }

            // Tạo payment request
            MoMoPaymentResponse response = moMoService.createPayment(order);

            if (response.getResultCode() == 0) {
                log.info("MoMo payment URL created successfully: {}", response.getPayUrl());
                // Redirect đến trang thanh toán MoMo
                return "redirect:" + response.getPayUrl();
            } else {
                log.error("Failed to create MoMo payment: {} - {}", response.getResultCode(), response.getMessage());
                redirectAttributes.addFlashAttribute("error", "Lỗi tạo thanh toán: " + response.getMessage());
                return "redirect:/checkout/orders/" + orderId;
            }

        } catch (Exception e) {
            log.error("Error creating MoMo payment for order #{}", orderId, e);
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra khi tạo thanh toán: " + e.getMessage());
            return "redirect:/checkout/orders";
        }
    }

    /**
     * Callback từ MoMo sau khi thanh toán
     * URL này sẽ được MoMo redirect user về sau khi thanh toán
     */
    @GetMapping("/momo/callback")
    public String momoCallback(
            @RequestParam(required = false) String partnerCode,
            @RequestParam(required = false) String orderId,
            @RequestParam(required = false) String requestId,
            @RequestParam(required = false) Long amount,
            @RequestParam(required = false) String orderInfo,
            @RequestParam(required = false) String orderType,
            @RequestParam(required = false) Long transId,
            @RequestParam(required = false) Integer resultCode,
            @RequestParam(required = false) String message,
            @RequestParam(required = false) String payType,
            @RequestParam(required = false) Long responseTime,
            @RequestParam(required = false) String extraData,
            @RequestParam(required = false) String signature,
            Model model,
            RedirectAttributes redirectAttributes,
            HttpServletResponse response
    ) {
        try {
            log.info("MoMo callback received - OrderId: {}, ResultCode: {}, Message: {}", orderId, resultCode, message);

            // Tạo callback response object
            MoMoCallbackResponse callback = MoMoCallbackResponse.builder()
                    .partnerCode(partnerCode)
                    .orderId(orderId)
                    .requestId(requestId)
                    .amount(amount)
                    .orderInfo(orderInfo)
                    .orderType(orderType)
                    .transId(transId)
                    .resultCode(resultCode)
                    .message(message)
                    .payType(payType)
                    .responseTime(responseTime)
                    .extraData(extraData != null ? extraData : "")
                    .signature(signature)
                    .build();

            // Xác thực signature
            boolean isValid = moMoService.verifyCallback(callback);

            if (!isValid) {
                log.error("Invalid MoMo signature");
                model.addAttribute("success", false);
                model.addAttribute("message", "Chữ ký không hợp lệ");
                return "payment/success";
            }

            // Lấy order ID từ MoMo order ID
            Long orderIdLong = moMoService.extractOrderId(orderId);

            if (orderIdLong == null) {
                log.error("Cannot extract order ID from: {}", orderId);
                model.addAttribute("success", false);
                model.addAttribute("message", "Không thể xác định đơn hàng");
                return "payment/success";
            }

            // Lấy thông tin đơn hàng
            Order order = orderService.findById(orderIdLong)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng #" + orderIdLong));

            // Xử lý kết quả thanh toán
            if (resultCode == 0) {
                // Thanh toán thành công
                log.info("Payment successful for order #{}", orderIdLong);
                order.setStatus(Order.OrderStatus.PROCESSING);
                order.setPaymentMethod("MOMO");
                order.setPaymentStatus("PAID");
                order.setTransactionId(transId != null ? transId.toString() : null);
                orderService.save(order);

                // Tự động restore JWT token từ extraData
                String userEmail = moMoService.extractUserEmail(extraData);
                if (userEmail != null && !userEmail.isEmpty()) {
                    try {
                        // Verify order ownership
                        if (!order.getUser().getEmail().equals(userEmail)) {
                            log.warn("User email mismatch in extraData: {} vs {}", userEmail, order.getUser().getEmail());
                        } else {
                            // Load user details và tạo JWT token mới
                            UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
                            String jwtToken = jwtUtil.generateToken(userDetails);

                            // Set JWT token vào cookie
                            Cookie jwtCookie = new Cookie("JWT_TOKEN", jwtToken);
                            jwtCookie.setHttpOnly(true);
                            jwtCookie.setPath("/");
                            jwtCookie.setMaxAge(24 * 60 * 60); // 24 hours
                            response.addCookie(jwtCookie);

                            log.info("✅ Auto-restored JWT token for user: {}", userEmail);

                            // Redirect trực tiếp đến order detail page (đã có JWT token)
                            redirectAttributes.addFlashAttribute("success", "Thanh toán thành công! Đơn hàng #" + orderIdLong + " đang được xử lý.");
                            return "redirect:/checkout/orders/" + orderIdLong;
                        }
                    } catch (Exception e) {
                        log.error("Error restoring JWT token for user: {}", userEmail, e);
                    }
                }

                // Fallback: nếu không restore được token, hiển thị trang success
                log.info("⚠️ Could not auto-restore JWT token, showing success page");
                model.addAttribute("success", true);
                model.addAttribute("orderId", orderIdLong);
                model.addAttribute("amount", amount);
                model.addAttribute("transactionId", transId);
                model.addAttribute("message", "Thanh toán thành công! Đơn hàng #" + orderIdLong + " đang được xử lý.");
                return "payment/success";

            } else {
                // Thanh toán thất bại
                log.warn("Payment failed for order #{}: {} - {}", orderIdLong, resultCode, message);

                // Redirect đến trang success với thông báo lỗi
                model.addAttribute("success", false);
                model.addAttribute("orderId", orderIdLong);
                model.addAttribute("message", message);
                return "payment/success";
            }

        } catch (Exception e) {
            log.error("Error processing MoMo callback", e);
            model.addAttribute("success", false);
            model.addAttribute("message", "Có lỗi xảy ra khi xử lý thanh toán: " + e.getMessage());
            return "payment/success";
        }
    }

    /**
     * IPN (Instant Payment Notification) từ MoMo
     * Endpoint này nhận notification từ MoMo server
     */
    @PostMapping("/momo/ipn")
    @ResponseBody
    public String momoIPN(@RequestBody MoMoCallbackResponse callback) {
        try {
            log.info("MoMo IPN received - OrderId: {}, ResultCode: {}", callback.getOrderId(), callback.getResultCode());

            // Xác thực signature
            boolean isValid = moMoService.verifyCallback(callback);

            if (!isValid) {
                log.error("Invalid MoMo IPN signature");
                return "{\"status\":\"error\",\"message\":\"Invalid signature\"}";
            }

            // Lấy order ID
            Long orderId = moMoService.extractOrderId(callback.getOrderId());

            if (orderId == null) {
                log.error("Cannot extract order ID from: {}", callback.getOrderId());
                return "{\"status\":\"error\",\"message\":\"Invalid order ID\"}";
            }

            // Lấy thông tin đơn hàng
            Order order = orderService.findById(orderId)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng #" + orderId));

            // Cập nhật trạng thái đơn hàng
            if (callback.getResultCode() == 0) {
                order.setStatus(Order.OrderStatus.PROCESSING);
                order.setPaymentMethod("MOMO");
                order.setPaymentStatus("PAID");
                order.setTransactionId(callback.getTransId() != null ? callback.getTransId().toString() : null);
                orderService.save(order);
                log.info("Order #{} updated successfully via IPN", orderId);
            }

            return "{\"status\":\"success\"}";

        } catch (Exception e) {
            log.error("Error processing MoMo IPN", e);
            return "{\"status\":\"error\",\"message\":\"" + e.getMessage() + "\"}";
        }
    }
}
