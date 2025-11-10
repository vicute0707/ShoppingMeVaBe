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

<h2 class="mb-4">Chi tiết đơn hàng #${order.id}</h2>

<!-- Success/Error Messages -->
<c:if test="${success != null}">
    <div class="alert alert-success alert-dismissible fade show">
        <i class="fas fa-check-circle"></i> ${success}
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    </div>
</c:if>
<c:if test="${error != null}">
    <div class="alert alert-danger alert-dismissible fade show">
        <i class="fas fa-exclamation-circle"></i> ${error}
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    </div>
</c:if>

<div class="row">
    <div class="col-md-8">
        <div class="card mb-4">
            <div class="card-header">
                <h5>Thông tin đơn hàng</h5>
            </div>
            <div class="card-body">
                <div class="row">
                    <div class="col-md-6">
                        <p><strong>Ngày đặt:</strong>
                            ${order.orderDate.dayOfMonth}/${order.orderDate.monthValue}/${order.orderDate.year} ${order.orderDate.hour}:${order.orderDate.minute < 10 ? '0' : ''}${order.orderDate.minute}</p>
                        <p><strong>Trạng thái đơn hàng:</strong>
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
                        </p>

                        <!-- Payment Status -->
                        <p><strong>Trạng thái thanh toán:</strong>
                            <c:choose>
                                <c:when test="${order.paymentStatus == 'PAID'}">
                                    <span class="badge bg-success">
                                        <i class="fas fa-check-circle"></i> Đã thanh toán
                                    </span>
                                </c:when>
                                <c:otherwise>
                                    <span class="badge bg-warning text-dark">
                                        <i class="fas fa-clock"></i> Chưa thanh toán
                                    </span>
                                </c:otherwise>
                            </c:choose>
                        </p>

                        <!-- Payment Method -->
                        <c:if test="${order.paymentMethod != null && !order.paymentMethod.isEmpty()}">
                            <p><strong>Phương thức thanh toán:</strong>
                                <c:choose>
                                    <c:when test="${order.paymentMethod == 'MOMO'}">
                                        <span class="badge bg-pink" style="background-color: #d82d8b;">
                                            <i class="fas fa-wallet"></i> MoMo
                                        </span>
                                    </c:when>
                                    <c:when test="${order.paymentMethod == 'COD'}">
                                        <span class="badge bg-secondary">
                                            <i class="fas fa-money-bill"></i> Tiền mặt
                                        </span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge bg-info">${order.paymentMethod}</span>
                                    </c:otherwise>
                                </c:choose>
                            </p>
                        </c:if>

                        <!-- Transaction ID -->
                        <c:if test="${order.transactionId != null && !order.transactionId.isEmpty()}">
                            <p><strong>Mã giao dịch:</strong>
                                <code>${order.transactionId}</code>
                            </p>
                        </c:if>
                    </div>
                    <div class="col-md-6">
                        <p><strong>Địa chỉ giao hàng:</strong><br>${order.shippingAddress}</p>
                        <p><strong>Số điện thoại:</strong> ${order.phone}</p>
                        <c:if test="${order.notes != null && !order.notes.isEmpty()}">
                            <p><strong>Ghi chú:</strong> ${order.notes}</p>
                        </c:if>
                    </div>
                </div>
            </div>
        </div>

        <div class="card">
            <div class="card-header">
                <h5>Sản phẩm</h5>
            </div>
            <div class="card-body">
                <table class="table">
                    <thead>
                    <tr>
                        <th>Tên sản phẩm</th>
                        <th>Đơn giá</th>
                        <th>Số lượng</th>
                        <th>Thành tiền</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${order.orderDetails}" var="detail">
                        <tr>
                            <td>${detail.product.name}</td>
                            <td>
                                <fmt:formatNumber value="${detail.unitPrice}" type="number" groupingUsed="true" maxFractionDigits="0"/>₫
                            </td>
                            <td>${detail.quantity}</td>
                            <td>
                                <fmt:formatNumber value="${detail.subtotal}" type="number" groupingUsed="true" maxFractionDigits="0"/>₫
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                    <tfoot>
                    <tr>
                        <td colspan="3" class="text-end"><strong>Tổng cộng:</strong></td>
                        <td>
                            <strong>
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
                <h5>Tóm tắt đơn hàng</h5>
            </div>
            <div class="card-body">
                <p><strong>Mã đơn hàng:</strong> #${order.id}</p>
                <p><strong>Số sản phẩm:</strong> ${order.orderDetails.size()}</p>

                <!-- Payment Status Summary -->
                <c:if test="${order.paymentStatus == 'PAID'}">
                    <div class="alert alert-success mb-0 mt-3">
                        <i class="fas fa-check-circle"></i> Đã thanh toán thành công
                        <c:if test="${order.paymentMethod == 'MOMO'}">
                            qua <strong>MoMo</strong>
                        </c:if>
                    </div>
                </c:if>

                <hr>
                <h4>
                    <strong>Tổng tiền:</strong><br>
                    <fmt:formatNumber value="${order.totalAmount}" type="number" groupingUsed="true" maxFractionDigits="0"/>₫
                </h4>
            </div>
            <div class="card-footer">
                <!-- Show payment button only if PENDING and NOT PAID -->
                <c:if test="${order.status == 'PENDING' && order.paymentStatus != 'PAID'}">
                    <a href="${pageContext.request.contextPath}/payment/momo/create/${order.id}"
                       class="btn btn-success w-100 mb-2">
                        <i class="fas fa-credit-card"></i> Thanh toán MoMo
                    </a>
                </c:if>
                <a href="${pageContext.request.contextPath}/checkout/orders" class="btn btn-secondary w-100">
                    <i class="fas fa-arrow-left"></i> Về danh sách đơn hàng
                </a>
            </div>
        </div>
    </div>
</div>

<jsp:include page="../common/footer.jsp"/>
