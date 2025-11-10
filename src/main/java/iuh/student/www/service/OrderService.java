package iuh.student.www.service;

import iuh.student.www.dto.CheckoutDTO;
import iuh.student.www.entity.Order;
import iuh.student.www.entity.OrderDetail;
import iuh.student.www.entity.Product;
import iuh.student.www.entity.User;
import iuh.student.www.model.Cart;
import iuh.student.www.model.CartItem;
import iuh.student.www.repository.OrderDetailRepository;
import iuh.student.www.repository.OrderRepository;
import iuh.student.www.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final ProductRepository productRepository;
    private final EmailService emailService;

    public Order createOrder(User user, Cart cart, CheckoutDTO checkoutDTO) throws Exception {
        if (cart == null || cart.isEmpty()) {
            throw new Exception("Giỏ hàng trống");
        }

        // Xác định payment status dựa trên payment method
        String paymentStatus = "COD".equals(checkoutDTO.getPaymentMethod()) ? "UNPAID" : "PENDING";

        // Create order
        Order order = Order.builder()
                .user(user)
                .orderDate(LocalDateTime.now())
                .totalAmount(cart.getTotalAmount())
                .status(Order.OrderStatus.PENDING)
                .shippingAddress(checkoutDTO.getShippingAddress())
                .phone(checkoutDTO.getPhone())
                .notes(checkoutDTO.getNotes())
                .paymentMethod(checkoutDTO.getPaymentMethod())
                .paymentStatus(paymentStatus)
                .orderDetails(new ArrayList<>())
                .build();

        // Create order details
        for (CartItem cartItem : cart.getItems()) {
            Product product = productRepository.findById(cartItem.getProductId())
                    .orElseThrow(() -> new Exception("Product not found: " + cartItem.getProductName()));

            // Check stock availability
            if (product.getStockQuantity() < cartItem.getQuantity()) {
                throw new Exception("Insufficient stock for product: " + product.getName());
            }

            OrderDetail orderDetail = OrderDetail.builder()
                    .order(order)
                    .product(product)
                    .quantity(cartItem.getQuantity())
                    .unitPrice(cartItem.getPrice())
                    .subtotal(cartItem.getSubtotal())
                    .build();

            order.getOrderDetails().add(orderDetail);

            // Update product stock
            product.setStockQuantity(product.getStockQuantity() - cartItem.getQuantity());
            productRepository.save(product);
        }

        Order savedOrder = orderRepository.save(order);

        // Send order confirmation email
        emailService.sendOrderConfirmationEmail(user.getEmail(), user.getFullName(), savedOrder);

        return savedOrder;
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAllOrderByOrderDateDesc();
    }

    public List<Order> getOrdersByUser(Long userId) {
        return orderRepository.findByUserIdOrderByOrderDateDesc(userId);
    }

    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findByIdWithDetails(id);
    }

    public Order updateOrderStatus(Long id, Order.OrderStatus status) throws Exception {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new Exception("Order not found"));

        order.setStatus(status);
        return orderRepository.save(order);
    }

    public OrderDetail updateOrderDetail(Long orderDetailId, Integer quantity) throws Exception {
        OrderDetail orderDetail = orderDetailRepository.findById(orderDetailId)
                .orElseThrow(() -> new Exception("Order detail not found"));

        if (quantity <= 0) {
            throw new Exception("Quantity must be greater than 0");
        }

        // Calculate difference in quantity
        int quantityDiff = quantity - orderDetail.getQuantity();

        // Update product stock
        Product product = orderDetail.getProduct();
        int newStock = product.getStockQuantity() - quantityDiff;

        if (newStock < 0) {
            throw new Exception("Insufficient stock for product: " + product.getName());
        }

        product.setStockQuantity(newStock);
        productRepository.save(product);

        // Update order detail
        orderDetail.setQuantity(quantity);
        orderDetail.setSubtotal(quantity * orderDetail.getUnitPrice());

        OrderDetail savedDetail = orderDetailRepository.save(orderDetail);

        // Update order total amount
        Order order = orderDetail.getOrder();
        double totalAmount = order.getOrderDetails().stream()
                .mapToDouble(OrderDetail::getSubtotal)
                .sum();
        order.setTotalAmount(totalAmount);
        orderRepository.save(order);

        return savedDetail;
    }

    public List<OrderDetail> getOrderDetailsByOrderId(Long orderId) {
        return orderDetailRepository.findByOrderId(orderId);
    }

    public Optional<Order> findById(Long id) {
        return orderRepository.findById(id);
    }

    public Order save(Order order) {
        return orderRepository.save(order);
    }
}
