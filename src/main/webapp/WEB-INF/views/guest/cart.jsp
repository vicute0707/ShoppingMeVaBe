<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<c:set var="pageTitle" value="Giỏ Hàng - Shopping Store" scope="request"/>
<jsp:include page="../common/header.jsp"/>

<h2 class="mb-4"><i class="fas fa-shopping-cart"></i> Giỏ Hàng Của Bạn</h2>

<c:choose>
    <c:when test="${cart != null && !cart.isEmpty()}">
        <div class="row">
            <div class="col-md-8">
                <div class="card">
                    <div class="card-body">
                        <table class="table">
                            <thead>
                            <tr>
                                <th>Sản Phẩm</th>
                                <th>Giá</th>
                                <th>Số Lượng</th>
                                <th>Tạm Tính</th>
                                <th>Thao Tác</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach items="${cart.items}" var="item">
                                <tr>
                                    <td>
                                        <div class="d-flex align-items-center">
                                            <c:choose>
                                                <c:when test="${item.imageUrl != null && !item.imageUrl.isEmpty()}">
                                                    <img src="${item.imageUrl}" alt="${item.productName}"
                                                         style="width: 50px; height: 50px; object-fit: cover;"
                                                         class="me-3">
                                                </c:when>
                                                <c:otherwise>
                                                    <div class="bg-secondary me-3"
                                                         style="width: 50px; height: 50px;"></div>
                                                </c:otherwise>
                                            </c:choose>
                                            <span>${item.productName}</span>
                                        </div>
                                    </td>
                                    <td>
                                        <fmt:formatNumber value="${item.price}" type="number" groupingUsed="true" maxFractionDigits="0"/>₫
                                    </td>
                                    <td>
                                        <form action="${pageContext.request.contextPath}/cart/update" method="post"
                                              class="d-inline">
                                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                            <input type="hidden" name="productId" value="${item.productId}"/>
                                            <input type="number" name="quantity" value="${item.quantity}"
                                                   min="0" max="100" style="width: 70px;" class="form-control d-inline"
                                                   onchange="this.form.submit()">
                                        </form>
                                    </td>
                                    <td>
                                        <fmt:formatNumber value="${item.subtotal}" type="number" groupingUsed="true" maxFractionDigits="0"/>₫
                                    </td>
                                    <td>
                                        <form action="${pageContext.request.contextPath}/cart/remove" method="post"
                                              class="d-inline">
                                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                            <input type="hidden" name="productId" value="${item.productId}"/>
                                            <button type="submit" class="btn btn-danger btn-sm">
                                                <i class="fas fa-trash"></i> Xóa
                                            </button>
                                        </form>
                                    </td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
            <div class="col-md-4">
                <div class="card">
                    <div class="card-header">
                        <h5><i class="fas fa-receipt"></i> Tổng Giỏ Hàng</h5>
                    </div>
                    <div class="card-body">
                        <p><strong>Tổng Số Lượng:</strong> ${cart.totalItems} sản phẩm</p>
                        <hr>
                        <h4>
                            <strong>Tổng Tiền:</strong>
                            <span class="text-success">
                                <fmt:formatNumber value="${cart.totalAmount}" type="number" groupingUsed="true" maxFractionDigits="0"/>₫
                            </span>
                        </h4>
                        <hr>
                        <div class="d-grid gap-2">
                            <sec:authorize access="isAuthenticated()">
                                <a href="${pageContext.request.contextPath}/checkout" class="btn btn-success btn-lg">
                                    <i class="fas fa-check"></i> Tiến Hành Thanh Toán
                                </a>
                            </sec:authorize>
                            <sec:authorize access="!isAuthenticated()">
                                <a href="${pageContext.request.contextPath}/login" class="btn btn-success btn-lg">
                                    <i class="fas fa-sign-in-alt"></i> Đăng Nhập Để Thanh Toán
                                </a>
                            </sec:authorize>
                            <a href="${pageContext.request.contextPath}/products" class="btn btn-primary">
                                <i class="fas fa-arrow-left"></i> Tiếp Tục Mua Sắm
                            </a>
                            <form action="${pageContext.request.contextPath}/cart/clear" method="post">
                                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                <button type="submit" class="btn btn-danger w-100"
                                        onclick="return confirm('Bạn có chắc muốn xóa toàn bộ giỏ hàng?')">
                                    <i class="fas fa-trash"></i> Xóa Giỏ Hàng
                                </button>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </c:when>
    <c:otherwise>
        <div class="alert alert-info">
            <h4><i class="fas fa-shopping-cart"></i> Giỏ hàng của bạn đang trống</h4>
            <p>Hãy bắt đầu mua sắm và thêm sản phẩm vào giỏ hàng!</p>
            <a href="${pageContext.request.contextPath}/products" class="btn btn-primary">
                <i class="fas fa-shopping-bag"></i> Xem Sản Phẩm
            </a>
        </div>
    </c:otherwise>
</c:choose>

<jsp:include page="../common/footer.jsp"/>
