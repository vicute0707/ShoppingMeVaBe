<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<c:set var="pageTitle" value="Thanh Toán - Shopping Store" scope="request"/>
<jsp:include page="../common/header.jsp"/>

<h2 class="mb-4">Thanh Toán Đơn Hàng</h2>

<div class="row">
    <div class="col-md-8">
        <div class="card mb-4">
            <div class="card-header">
                <h5><i class="fas fa-shipping-fast"></i> Thông Tin Giao Hàng</h5>
            </div>
            <div class="card-body">
                <form:form action="${pageContext.request.contextPath}/checkout" method="post"
                           modelAttribute="checkoutDTO" id="checkoutForm">
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

                    <div class="mb-3">
                        <label for="shippingAddress" class="form-label"><i class="fas fa-map-marker-alt"></i> Địa Chỉ Giao Hàng *</label>
                        <form:textarea path="shippingAddress" class="form-control" id="shippingAddress"
                                       rows="3" required="required" maxlength="255"
                                       placeholder="Nhập địa chỉ giao hàng đầy đủ (số nhà, đường, phường/xã, quận/huyện, tỉnh/thành phố)"/>
                        <form:errors path="shippingAddress" cssClass="text-danger"/>
                        <div class="invalid-feedback">Vui lòng nhập địa chỉ giao hàng.</div>
                    </div>

                    <div class="mb-3">
                        <label for="phone" class="form-label"><i class="fas fa-phone"></i> Số Điện Thoại *</label>
                        <form:input path="phone" class="form-control" id="phone"
                                    required="required" maxlength="15" placeholder="Nhập số điện thoại"/>
                        <form:errors path="phone" cssClass="text-danger"/>
                        <div class="invalid-feedback">Vui lòng nhập số điện thoại.</div>
                    </div>

                    <div class="mb-3">
                        <label for="paymentMethod" class="form-label"><i class="fas fa-credit-card"></i> Phương Thức Thanh Toán *</label>
                        <select name="paymentMethod" id="paymentMethod" class="form-select" required>
                            <option value="">-- Chọn phương thức thanh toán --</option>
                            <option value="COD" selected>Thanh toán khi nhận hàng (COD)</option>
                            <option value="MOMO">Ví MoMo</option>
                            <option value="BANK_TRANSFER">Chuyển khoản ngân hàng</option>
                        </select>
                        <div class="form-text">
                            <i class="fas fa-info-circle"></i> Chọn "COD" để thanh toán bằng tiền mặt khi nhận hàng
                        </div>
                    </div>

                    <div class="mb-3">
                        <label for="notes" class="form-label"><i class="fas fa-edit"></i> Ghi Chú Đơn Hàng (Tùy chọn)</label>
                        <form:textarea path="notes" class="form-control" id="notes"
                                       rows="3" maxlength="500" placeholder="Ghi chú thêm về đơn hàng (nếu có)"/>
                        <form:errors path="notes" cssClass="text-danger"/>
                    </div>

                    <div class="d-grid gap-2">
                        <button type="submit" class="btn btn-success btn-lg">
                            <i class="fas fa-check-circle"></i> Đặt Hàng
                        </button>
                        <a href="${pageContext.request.contextPath}/cart" class="btn btn-secondary">
                            <i class="fas fa-arrow-left"></i> Quay Lại Giỏ Hàng
                        </a>
                    </div>
                </form:form>
            </div>
        </div>
    </div>

    <div class="col-md-4">
        <div class="card">
            <div class="card-header">
                <h5><i class="fas fa-receipt"></i> Tóm Tắt Đơn Hàng</h5>
            </div>
            <div class="card-body">
                <h6>Sản phẩm (${cart.totalItems} món):</h6>
                <ul class="list-group mb-3">
                    <c:forEach items="${cart.items}" var="item">
                        <li class="list-group-item d-flex justify-content-between">
                            <div>
                                <h6 class="my-0">${item.productName}</h6>
                                <small class="text-muted">Số lượng: ${item.quantity}</small>
                            </div>
                            <span class="text-muted">
                                <fmt:formatNumber value="${item.subtotal}" type="number" groupingUsed="true" maxFractionDigits="0"/>₫
                            </span>
                        </li>
                    </c:forEach>
                </ul>
                <hr>
                <h4>
                    <strong>Tổng Cộng:</strong>
                    <span class="text-success">
                        <fmt:formatNumber value="${cart.totalAmount}" type="number" groupingUsed="true" maxFractionDigits="0"/>₫
                    </span>
                </h4>
            </div>
        </div>
    </div>
</div>

<jsp:include page="../common/footer.jsp"/>
