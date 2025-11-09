<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="pageTitle" value="Order Details - Shopping Store" scope="request"/>
<jsp:include page="../common/header.jsp"/>

<nav aria-label="breadcrumb">
    <ol class="breadcrumb">
        <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/">Home</a></li>
        <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/checkout/orders">My Orders</a></li>
        <li class="breadcrumb-item active">Order #${order.id}</li>
    </ol>
</nav>

<h2 class="mb-4">Order Details - #${order.id}</h2>

<div class="row">
    <div class="col-md-8">
        <div class="card mb-4">
            <div class="card-header">
                <h5>Order Information</h5>
            </div>
            <div class="card-body">
                <div class="row">
                    <div class="col-md-6">
                        <p><strong>Order Date:</strong>
                            <fmt:formatDate value="${order.orderDate}" pattern="dd/MM/yyyy HH:mm"/></p>
                        <p><strong>Status:</strong>
                            <c:choose>
                                <c:when test="${order.status == 'PENDING'}">
                                    <span class="badge bg-warning">Pending</span>
                                </c:when>
                                <c:when test="${order.status == 'PROCESSING'}">
                                    <span class="badge bg-info">Processing</span>
                                </c:when>
                                <c:when test="${order.status == 'SHIPPED'}">
                                    <span class="badge bg-primary">Shipped</span>
                                </c:when>
                                <c:when test="${order.status == 'DELIVERED'}">
                                    <span class="badge bg-success">Delivered</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="badge bg-danger">Cancelled</span>
                                </c:otherwise>
                            </c:choose>
                        </p>
                    </div>
                    <div class="col-md-6">
                        <p><strong>Shipping Address:</strong><br>${order.shippingAddress}</p>
                        <p><strong>Phone:</strong> ${order.phone}</p>
                        <c:if test="${order.notes != null && !order.notes.isEmpty()}">
                            <p><strong>Notes:</strong> ${order.notes}</p>
                        </c:if>
                    </div>
                </div>
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
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${order.orderDetails}" var="detail">
                        <tr>
                            <td>${detail.product.name}</td>
                            <td>
                                <fmt:formatNumber value="${detail.unitPrice}" type="currency" currencySymbol="$"/>
                            </td>
                            <td>${detail.quantity}</td>
                            <td>
                                <fmt:formatNumber value="${detail.subtotal}" type="currency" currencySymbol="$"/>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                    <tfoot>
                    <tr>
                        <td colspan="3" class="text-end"><strong>Total:</strong></td>
                        <td>
                            <strong>
                                <fmt:formatNumber value="${order.totalAmount}" type="currency" currencySymbol="$"/>
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
                <h5>Order Summary</h5>
            </div>
            <div class="card-body">
                <p><strong>Order ID:</strong> #${order.id}</p>
                <p><strong>Total Items:</strong> ${order.orderDetails.size()}</p>
                <hr>
                <h4>
                    <strong>Total Amount:</strong><br>
                    <fmt:formatNumber value="${order.totalAmount}" type="currency" currencySymbol="$"/>
                </h4>
            </div>
            <div class="card-footer">
                <a href="${pageContext.request.contextPath}/checkout/orders" class="btn btn-secondary w-100">
                    <i class="fas fa-arrow-left"></i> Back to Orders
                </a>
            </div>
        </div>
    </div>
</div>

<jsp:include page="../common/footer.jsp"/>
