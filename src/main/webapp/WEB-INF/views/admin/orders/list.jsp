<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="pageTitle" value="Manage Orders - Admin" scope="request"/>
<jsp:include page="../../common/header.jsp"/>

<h2 class="mb-4">Manage Orders</h2>

<c:choose>
    <c:when test="${orders.size() > 0}">
        <div class="table-responsive">
            <table class="table table-striped">
                <thead>
                <tr>
                    <th>Order ID</th>
                    <th>Customer</th>
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
                        <td>${order.user.fullName}<br><small>${order.user.email}</small></td>
                        <td>
                            ${order.orderDate.dayOfMonth}/${order.orderDate.monthValue}/${order.orderDate.year} ${order.orderDate.hour}:${order.orderDate.minute < 10 ? '0' : ''}${order.orderDate.minute}
                        </td>
                        <td>
                            <fmt:formatNumber value="${order.totalAmount}" type="number" groupingUsed="true" maxFractionDigits="0"/>₫
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
                            <a href="${pageContext.request.contextPath}/admin/orders/${order.id}"
                               class="btn btn-info btn-sm">
                                <i class="fas fa-eye"></i> View
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
            No orders found.
        </div>
    </c:otherwise>
</c:choose>

<jsp:include page="../../common/footer.jsp"/>
