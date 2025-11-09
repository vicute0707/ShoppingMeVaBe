<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<c:set var="pageTitle" value="Checkout - Shopping Store" scope="request"/>
<jsp:include page="../common/header.jsp"/>

<h2 class="mb-4">Checkout</h2>

<div class="row">
    <div class="col-md-8">
        <div class="card mb-4">
            <div class="card-header">
                <h5>Shipping Information</h5>
            </div>
            <div class="card-body">
                <form:form action="${pageContext.request.contextPath}/checkout" method="post"
                           modelAttribute="checkoutDTO" id="checkoutForm">
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

                    <div class="mb-3">
                        <label for="shippingAddress" class="form-label">Shipping Address *</label>
                        <form:textarea path="shippingAddress" class="form-control" id="shippingAddress"
                                       rows="3" required="required" maxlength="255"/>
                        <form:errors path="shippingAddress" cssClass="text-danger"/>
                        <div class="invalid-feedback">Shipping address is required.</div>
                    </div>

                    <div class="mb-3">
                        <label for="phone" class="form-label">Phone Number *</label>
                        <form:input path="phone" class="form-control" id="phone"
                                    required="required" maxlength="15"/>
                        <form:errors path="phone" cssClass="text-danger"/>
                        <div class="invalid-feedback">Phone number is required.</div>
                    </div>

                    <div class="mb-3">
                        <label for="notes" class="form-label">Order Notes (Optional)</label>
                        <form:textarea path="notes" class="form-control" id="notes"
                                       rows="3" maxlength="500"/>
                        <form:errors path="notes" cssClass="text-danger"/>
                    </div>

                    <div class="d-grid gap-2">
                        <button type="submit" class="btn btn-success btn-lg">
                            <i class="fas fa-check-circle"></i> Place Order
                        </button>
                        <a href="${pageContext.request.contextPath}/cart" class="btn btn-secondary">
                            <i class="fas fa-arrow-left"></i> Back to Cart
                        </a>
                    </div>
                </form:form>
            </div>
        </div>
    </div>

    <div class="col-md-4">
        <div class="card">
            <div class="card-header">
                <h5>Order Summary</h5>
            </div>
            <div class="card-body">
                <h6>Items (${cart.totalItems}):</h6>
                <ul class="list-group mb-3">
                    <c:forEach items="${cart.items}" var="item">
                        <li class="list-group-item d-flex justify-content-between">
                            <div>
                                <h6 class="my-0">${item.productName}</h6>
                                <small class="text-muted">Qty: ${item.quantity}</small>
                            </div>
                            <span class="text-muted">
                                <fmt:formatNumber value="${item.subtotal}" type="currency" currencySymbol="$"/>
                            </span>
                        </li>
                    </c:forEach>
                </ul>
                <hr>
                <h4>
                    <strong>Total:</strong>
                    <fmt:formatNumber value="${cart.totalAmount}" type="currency" currencySymbol="$"/>
                </h4>
            </div>
        </div>
    </div>
</div>

<jsp:include page="../common/footer.jsp"/>
