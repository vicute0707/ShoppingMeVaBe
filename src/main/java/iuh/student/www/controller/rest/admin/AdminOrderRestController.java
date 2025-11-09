package iuh.student.www.controller.rest.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import iuh.student.www.entity.Order;
import iuh.student.www.entity.OrderDetail;
import iuh.student.www.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/orders")
@RequiredArgsConstructor
@Tag(name = "Admin - Orders", description = "Order management API (Admin only)")
public class AdminOrderRestController {

    private final OrderService orderService;

    @Operation(summary = "Get all orders", description = "Retrieve list of all orders from all users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orders retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Order.class)))
    })
    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    @Operation(summary = "Search orders", description = "Search orders by user name, email, or order status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Search completed successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Order.class)))
    })
    @GetMapping("/search")
    public ResponseEntity<List<Order>> searchOrders(
            @Parameter(description = "Search keyword", required = true)
            @RequestParam String keyword) {
        List<Order> orders = orderService.getAllOrders().stream()
                .filter(o -> o.getUser().getFullName().toLowerCase().contains(keyword.toLowerCase()) ||
                            o.getUser().getEmail().toLowerCase().contains(keyword.toLowerCase()) ||
                            o.getStatus().toString().toLowerCase().contains(keyword.toLowerCase()) ||
                            o.getId().toString().contains(keyword))
                .collect(Collectors.toList());
        return ResponseEntity.ok(orders);
    }

    @Operation(summary = "Get orders by user", description = "Retrieve all orders for a specific user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orders retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Order.class)))
    })
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Order>> getOrdersByUser(
            @Parameter(description = "User ID", required = true)
            @PathVariable Long userId) {
        List<Order> orders = orderService.getOrdersByUser(userId);
        return ResponseEntity.ok(orders);
    }

    @Operation(summary = "Get order by ID", description = "Retrieve detailed information of a specific order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Order.class))),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderById(
            @Parameter(description = "Order ID", required = true)
            @PathVariable Long id) {
        return orderService.getOrderById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Update order status", description = "Update the status of an order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order status updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Order.class))),
            @ApiResponse(responseCode = "404", description = "Order not found"),
            @ApiResponse(responseCode = "400", description = "Invalid status")
    })
    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateOrderStatus(
            @Parameter(description = "Order ID", required = true)
            @PathVariable Long id,
            @Parameter(description = "New order status", required = true)
            @RequestParam Order.OrderStatus status) {
        try {
            Order updatedOrder = orderService.updateOrderStatus(id, status);
            return ResponseEntity.ok(Map.of(
                    "message", "Order status updated successfully",
                    "order", updatedOrder
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @Operation(summary = "Get order details", description = "Retrieve all order details for a specific order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order details retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderDetail.class)))
    })
    @GetMapping("/{orderId}/details")
    public ResponseEntity<List<OrderDetail>> getOrderDetails(
            @Parameter(description = "Order ID", required = true)
            @PathVariable Long orderId) {
        List<OrderDetail> orderDetails = orderService.getOrderDetailsByOrderId(orderId);
        return ResponseEntity.ok(orderDetails);
    }

    @Operation(summary = "Update order detail quantity",
               description = "Update quantity of a specific item in an order (adjusts stock accordingly)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order detail updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderDetail.class))),
            @ApiResponse(responseCode = "404", description = "Order detail not found"),
            @ApiResponse(responseCode = "400", description = "Invalid quantity or insufficient stock")
    })
    @PutMapping("/details/{orderDetailId}")
    public ResponseEntity<?> updateOrderDetail(
            @Parameter(description = "Order Detail ID", required = true)
            @PathVariable Long orderDetailId,
            @Parameter(description = "New quantity", required = true)
            @RequestParam Integer quantity) {
        try {
            OrderDetail updatedDetail = orderService.updateOrderDetail(orderDetailId, quantity);
            return ResponseEntity.ok(Map.of(
                    "message", "Order detail updated successfully",
                    "orderDetail", updatedDetail
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
