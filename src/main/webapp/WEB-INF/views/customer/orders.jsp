<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="pageTitle" value="My Orders - Shopping Store" scope="request"/>
<jsp:include page="../common/header.jsp"/>

<h2 class="mb-4">My Orders</h2>

<c:choose>
    <c:when test="${orders.size() > 0}">
        <div class="table-responsive">
            <table class="table table-striped">
                <thead>
                <tr>
                    <th>Order ID</th>
                    <th>Order Date</th>
                    <th>Total Amount</th>
                    <th>Status</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${orders}" var="order">
                    <tr>
                        <td>#${order.id}</td>
                        <td>
                            <fmt:formatDate value="${order.orderDate}" pattern="dd/MM/yyyy HH:mm"/>
                        </td>
                        <td>
                            <fmt:formatNumber value="${order.totalAmount}" type="currency" currencySymbol="$"/>
                        </td>
                        <td>
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
                        </td>
                        <td>
                            <a href="${pageContext.request.contextPath}/checkout/orders/${order.id}"
                               class="btn btn-primary btn-sm">
                                <i class="fas fa-eye"></i> View Details
                            </a>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </c:when>
    <c:otherwise>
        <div class="alert alert-info">
            <h4>No orders yet</h4>
            <p>You haven't placed any orders yet.</p>
            <a href="${pageContext.request.contextPath}/products" class="btn btn-primary">
                <i class="fas fa-shopping-bag"></i> Start Shopping
            </a>
        </div>
    </c:otherwise>
</c:choose>

<jsp:include page="../common/footer.jsp"/>
