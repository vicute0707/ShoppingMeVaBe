<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="pageTitle" value="Trang Chủ - Shopping Store" scope="request"/>
<jsp:include page="../common/header.jsp"/>

<div class="jumbotron bg-light p-5 rounded">
    <h1 class="display-4"><i class="fas fa-store"></i> Chào Mừng Đến Shopping Store!</h1>
    <p class="lead">Tìm kiếm sản phẩm chất lượng với giá tốt nhất</p>
    <a class="btn btn-primary btn-lg" href="${pageContext.request.contextPath}/products" role="button">
        <i class="fas fa-shopping-bag"></i> Mua Sắm Ngay
    </a>
</div>

<h2 class="mt-5 mb-4"><i class="fas fa-star"></i> Sản Phẩm Nổi Bật</h2>
<div class="row">
    <c:forEach items="${products}" var="product" varStatus="status">
        <c:if test="${status.index < 8}">
            <div class="col-md-3 mb-4">
                <div class="card h-100">
                    <c:choose>
                        <c:when test="${product.imageUrl != null && !product.imageUrl.isEmpty()}">
                            <img src="${product.imageUrl}" class="card-img-top" alt="${product.name}"
                                 style="height: 200px; object-fit: cover;">
                        </c:when>
                        <c:otherwise>
                            <div class="card-img-top bg-secondary d-flex align-items-center justify-content-center"
                                 style="height: 200px;">
                                <i class="fas fa-image fa-3x text-white"></i>
                            </div>
                        </c:otherwise>
                    </c:choose>
                    <div class="card-body">
                        <h5 class="card-title">${product.name}</h5>
                        <p class="card-text text-truncate">${product.description}</p>
                        <p class="card-text">
                            <strong class="text-success">
                                <fmt:formatNumber value="${product.price}" type="number" groupingUsed="true" maxFractionDigits="0"/>₫
                            </strong>
                        </p>
                        <p class="card-text">
                            <small class="text-muted"><i class="fas fa-warehouse"></i> Kho: ${product.stockQuantity}</small>
                        </p>
                    </div>
                    <div class="card-footer">
                        <div class="d-grid gap-2">
                            <a href="${pageContext.request.contextPath}/products/detail?id=${product.id}"
                               class="btn btn-primary btn-sm"><i class="fas fa-info-circle"></i> Xem Chi Tiết</a>
                            <form action="${pageContext.request.contextPath}/cart/add" method="post">
                                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                <input type="hidden" name="productId" value="${product.id}"/>
                                <input type="hidden" name="quantity" value="1"/>
                                <button type="submit" class="btn btn-success btn-sm w-100">
                                    <i class="fas fa-cart-plus"></i> Thêm Vào Giỏ
                                </button>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </c:if>
    </c:forEach>
</div>

<c:if test="${products.size() == 0}">
    <div class="alert alert-info">
        <i class="fas fa-info-circle"></i> Hiện tại chưa có sản phẩm nào.
    </div>
</c:if>

<jsp:include page="../common/footer.jsp"/>
