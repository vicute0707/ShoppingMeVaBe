package iuh.student.www.service;

import iuh.student.www.entity.Order;
import iuh.student.www.entity.OrderDetail;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendWelcomeEmail(String to, String fullName) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject("Welcome to Shopping Store!");

            String htmlContent = buildWelcomeEmailContent(fullName);
            helper.setText(htmlContent, true);

            mailSender.send(message);
            log.info("Welcome email sent to: {}", to);
        } catch (MessagingException e) {
            log.error("Failed to send welcome email to: {}", to, e);
        }
    }

    public void sendOrderConfirmationEmail(String to, String fullName, Order order) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject("Order Confirmation - Order #" + order.getId());

            String htmlContent = buildOrderConfirmationEmailContent(fullName, order);
            helper.setText(htmlContent, true);

            mailSender.send(message);
            log.info("Order confirmation email sent to: {}", to);
        } catch (MessagingException e) {
            log.error("Failed to send order confirmation email to: {}", to, e);
        }
    }

    private String buildWelcomeEmailContent(String fullName) {
        return "<!DOCTYPE html>" +
                "<html>" +
                "<head><meta charset='UTF-8'></head>" +
                "<body style='font-family: Arial, sans-serif; line-height: 1.6;'>" +
                "<div style='max-width: 600px; margin: 0 auto; padding: 20px;'>" +
                "<h2 style='color: #333;'>Welcome to Shopping Store!</h2>" +
                "<p>Dear " + fullName + ",</p>" +
                "<p>Thank you for registering with our store. We're excited to have you as a customer!</p>" +
                "<p>You can now:</p>" +
                "<ul>" +
                "<li>Browse our products</li>" +
                "<li>Add items to your cart</li>" +
                "<li>Place orders and track them</li>" +
                "</ul>" +
                "<p>Happy shopping!</p>" +
                "<p style='margin-top: 30px;'>Best regards,<br>Shopping Store Team</p>" +
                "</div>" +
                "</body>" +
                "</html>";
    }

    private String buildOrderConfirmationEmailContent(String fullName, Order order) {
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        StringBuilder orderItems = new StringBuilder();
        for (OrderDetail detail : order.getOrderDetails()) {
            orderItems.append("<tr>")
                    .append("<td style='padding: 10px; border-bottom: 1px solid #ddd;'>")
                    .append(detail.getProduct().getName())
                    .append("</td>")
                    .append("<td style='padding: 10px; border-bottom: 1px solid #ddd; text-align: center;'>")
                    .append(detail.getQuantity())
                    .append("</td>")
                    .append("<td style='padding: 10px; border-bottom: 1px solid #ddd; text-align: right;'>")
                    .append(currencyFormat.format(detail.getUnitPrice()))
                    .append("</td>")
                    .append("<td style='padding: 10px; border-bottom: 1px solid #ddd; text-align: right;'>")
                    .append(currencyFormat.format(detail.getSubtotal()))
                    .append("</td>")
                    .append("</tr>");
        }

        return "<!DOCTYPE html>" +
                "<html>" +
                "<head><meta charset='UTF-8'></head>" +
                "<body style='font-family: Arial, sans-serif; line-height: 1.6;'>" +
                "<div style='max-width: 600px; margin: 0 auto; padding: 20px;'>" +
                "<h2 style='color: #333;'>Order Confirmation</h2>" +
                "<p>Dear " + fullName + ",</p>" +
                "<p>Thank you for your order! Here are the details:</p>" +
                "<div style='background: #f5f5f5; padding: 15px; margin: 20px 0;'>" +
                "<p><strong>Order ID:</strong> #" + order.getId() + "</p>" +
                "<p><strong>Order Date:</strong> " + order.getOrderDate().format(dateFormat) + "</p>" +
                "<p><strong>Status:</strong> " + order.getStatus() + "</p>" +
                "<p><strong>Shipping Address:</strong> " + order.getShippingAddress() + "</p>" +
                "<p><strong>Phone:</strong> " + order.getPhone() + "</p>" +
                "</div>" +
                "<h3>Order Items:</h3>" +
                "<table style='width: 100%; border-collapse: collapse;'>" +
                "<thead>" +
                "<tr style='background: #333; color: white;'>" +
                "<th style='padding: 10px; text-align: left;'>Product</th>" +
                "<th style='padding: 10px; text-align: center;'>Quantity</th>" +
                "<th style='padding: 10px; text-align: right;'>Unit Price</th>" +
                "<th style='padding: 10px; text-align: right;'>Subtotal</th>" +
                "</tr>" +
                "</thead>" +
                "<tbody>" +
                orderItems +
                "</tbody>" +
                "</table>" +
                "<div style='text-align: right; margin-top: 20px;'>" +
                "<h3>Total Amount: " + currencyFormat.format(order.getTotalAmount()) + "</h3>" +
                "</div>" +
                "<p style='margin-top: 30px;'>We'll send you another email when your order ships.</p>" +
                "<p>Best regards,<br>Shopping Store Team</p>" +
                "</div>" +
                "</body>" +
                "</html>";
    }
}
