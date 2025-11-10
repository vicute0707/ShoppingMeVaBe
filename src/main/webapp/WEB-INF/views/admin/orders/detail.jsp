<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="pageTitle" value="Order Details - Admin" scope="request"/>
<jsp:include page="../../common/header.jsp"/>

<nav aria-label="breadcrumb">
    <ol class="breadcrumb">
        <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/admin/dashboard">Dashboard</a></li>
        <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/admin/orders">Orders</a></li>
        <li class="breadcrumb-item active">Order #${order.id}</li>
    </ol>
</nav>

<div class="row">
    <div class="col-md-8">
        <div class="card mb-4">
            <div class="card-header">
                <h5>Order Information</h5>
            </div>
            <div class="card-body">
                <div class="row">
                    <div class="col-md-6">
                        <p><strong>Order ID:</strong> #${order.id}</p>
                        <p><strong>Order Date:</strong>
                            ${order.orderDate.dayOfMonth}/${order.orderDate.monthValue}/${order.orderDate.year} ${order.orderDate.hour}:${order.orderDate.minute < 10 ? '0' : ''}${order.orderDate.minute}</p>
                        <p><strong>Customer:</strong> ${order.user.fullName}</p>
                        <p><strong>Email:</strong> ${order.user.email}</p>
                    </div>
                    <div class="col-md-6">
                        <p><strong>Shipping Address:</strong><br>${order.shippingAddress}</p>
                        <p><strong>Phone:</strong> ${order.phone}</p>
                        <c:if test="${order.notes != null && !order.notes.isEmpty()}">
                            <p><strong>Notes:</strong> ${order.notes}</p>
                        </c:if>
                    </div>
                </div>
                <hr>
                <form action="${pageContext.request.contextPath}/admin/orders/${order.id}/status" method="post">
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                    <div class="row">
                        <div class="col-md-6">
                            <label for="status" class="form-label">Update Status:</label>
                            <select name="status" id="status" class="form-select">
                                <option value="PENDING" ${order.status == 'PENDING' ? 'selected' : ''}>Pending</option>
                                <option value="PROCESSING"
                                        ${order.status == 'PROCESSING' ? 'selected' : ''}>Processing
                                </option>
                                <option value="SHIPPED" ${order.status == 'SHIPPED' ? 'selected' : ''}>Shipped</option>
                                <option value="DELIVERED"
                                        ${order.status == 'DELIVERED' ? 'selected' : ''}>Delivered
                                </option>
                                <option value="CANCELLED"
                                        ${order.status == 'CANCELLED' ? 'selected' : ''}>Cancelled
                                </option>
                            </select>
                        </div>
                        <div class="col-md-6">
                            <label>&nbsp;</label>
                            <button type="submit" class="btn btn-primary d-block">
                                <i class="fas fa-save"></i> Update Status
                            </button>
                        </div>
                    </div>
                </form>
            </div>
        </div>

        <div class="card">
            <div class="card-header">
                <h5>Order Items</h5>
            </div>
            <div class="card-body">
                <table class="table">
                    <thead>
                    <tr>
                        <th>Product</th>
                        <th>Unit Price</th>
                        <th>Quantity</th>
                        <th>Subtotal</th>
                        <th>Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${order.orderDetails}" var="detail">
                        <tr>
                            <td>${detail.product.name}</td>
                            <td>
                                <fmt:formatNumber value="${detail.unitPrice}" type="number" groupingUsed="true" maxFractionDigits="0"/>₫
                            </td>
                            <td>
                                <form action="${pageContext.request.contextPath}/admin/orders/details/${detail.id}/update"
                                      method="post" class="d-inline">
                                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                    <input type="hidden" name="orderId" value="${order.id}"/>
                                    <input type="number" name="quantity" value="${detail.quantity}"
                                           min="1" max="100" style="width: 70px;" class="form-control d-inline">
                                    <button type="submit" class="btn btn-sm btn-primary">
                                        <i class="fas fa-check"></i>
                                    </button>
                                </form>
                            </td>
                            <td>
                                <fmt:formatNumber value="${detail.subtotal}" type="number" groupingUsed="true" maxFractionDigits="0"/>₫
                            </td>
                            <td>
                                <small class="text-muted">Có thể cập nhật số lượng</small>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                    <tfoot>
                    <tr>
                        <td colspan="3" class="text-end"><strong>Tổng Cộng:</strong></td>
                        <td colspan="2">
                            <strong class="text-success">
                                <fmt:formatNumber value="${order.totalAmount}" type="number" groupingUsed="true" maxFractionDigits="0"/>₫
                            </strong>
                        </td>
                    </tr>
                    </tfoot>
                </table>
            </div>
        </div>
    </div>

    <div class="col-md-4">
        <div class="card">
            <div class="card-header">
                <h5>Quick Actions</h5>
            </div>
            <div class="card-body">
                <div class="d-grid gap-2">
                    <a href="${pageContext.request.contextPath}/admin/orders" class="btn btn-secondary">
                        <i class="fas fa-arrow-left"></i> Back to Orders
                    </a>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="../../common/footer.jsp"/>
