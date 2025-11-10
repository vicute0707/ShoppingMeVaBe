<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="pageTitle" value="My Orders - Shopping Store" scope="request"/>
<jsp:include page="../common/header.jsp"/>

<h2 class="mb-4">Đơn hàng của tôi</h2>

<c:choose>
    <c:when test="${orders.size() > 0}">
        <div class="table-responsive">
            <table class="table table-striped table-hover">
                <thead class="table-light">
                <tr>
                    <th>Mã đơn</th>
                    <th>Ngày đặt</th>
                    <th>Tổng tiền</th>
                    <th>Trạng thái</th>
                    <th>Thanh toán</th>
                    <th>Thao tác</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${orders}" var="order">
                    <tr>
                        <td><strong>#${order.id}</strong></td>
                        <td>
                            ${order.orderDate.dayOfMonth}/${order.orderDate.monthValue}/${order.orderDate.year}
                            <br>
                            <small class="text-muted">${order.orderDate.hour}:${order.orderDate.minute < 10 ? '0' : ''}${order.orderDate.minute}</small>
                        </td>
                        <td>
                            <strong><fmt:formatNumber value="${order.totalAmount}" type="number" groupingUsed="true" maxFractionDigits="0"/>₫</strong>
                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${order.status == 'PENDING'}">
                                    <span class="badge bg-warning text-dark">Chờ thanh toán</span>
                                </c:when>
                                <c:when test="${order.status == 'PROCESSING'}">
                                    <span class="badge bg-info">Đang xử lý</span>
                                </c:when>
                                <c:when test="${order.status == 'SHIPPED'}">
                                    <span class="badge bg-primary">Đang giao</span>
                                </c:when>
                                <c:when test="${order.status == 'DELIVERED'}">
                                    <span class="badge bg-success">Đã giao</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="badge bg-danger">Đã hủy</span>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${order.paymentStatus == 'PAID'}">
                                    <span class="badge bg-success">
                                        <i class="fas fa-check-circle"></i> Đã thanh toán
                                    </span>
                                    <c:if test="${order.paymentMethod == 'MOMO'}">
                                        <br><small class="text-muted">MoMo</small>
                                    </c:if>
                                </c:when>
                                <c:otherwise>
                                    <span class="badge bg-warning text-dark">
                                        <i class="fas fa-clock"></i> Chưa thanh toán
                                    </span>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <a href="${pageContext.request.contextPath}/checkout/orders/${order.id}"
                               class="btn btn-primary btn-sm">
                                <i class="fas fa-eye"></i> Xem chi tiết
                            </a>
                            <c:if test="${order.status == 'PENDING' && order.paymentStatus != 'PAID'}">
                                <a href="${pageContext.request.contextPath}/payment/momo/create/${order.id}"
                                   class="btn btn-success btn-sm mt-1">
                                    <i class="fas fa-credit-card"></i> Thanh toán
                                </a>
                            </c:if>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </c:when>
    <c:otherwise>
        <div class="alert alert-info">
            <h4>Chưa có đơn hàng nào</h4>
            <p>Bạn chưa đặt đơn hàng nào. Hãy bắt đầu mua sắm ngay!</p>
            <a href="${pageContext.request.contextPath}/products" class="btn btn-primary">
                <i class="fas fa-shopping-bag"></i> Mua sắm ngay
            </a>
        </div>
    </c:otherwise>
</c:choose>

<jsp:include page="../common/footer.jsp"/>
