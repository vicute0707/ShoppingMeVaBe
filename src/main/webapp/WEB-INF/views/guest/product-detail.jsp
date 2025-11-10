<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="pageTitle" value="${product.name} - Shopping Store" scope="request"/>
<jsp:include page="../common/header.jsp"/>

<nav aria-label="breadcrumb">
    <ol class="breadcrumb">
        <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/"><i class="fas fa-home"></i> Trang Chủ</a></li>
        <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/products"><i class="fas fa-box"></i> Sản Phẩm</a></li>
        <li class="breadcrumb-item active">${product.name}</li>
    </ol>
</nav>

<div class="row">
    <div class="col-md-6">
        <c:choose>
            <c:when test="${product.imageUrl != null && !product.imageUrl.isEmpty()}">
                <div style="height: 400px; background-color: #f8f9fa; display: flex; align-items: center; justify-content: center; padding: 20px; border-radius: 0.25rem;">
                    <img src="${product.imageUrl}" class="img-fluid" alt="${product.name}"
                         style="max-height: 100%; max-width: 100%; object-fit: contain;">
                </div>
            </c:when>
            <c:otherwise>
                <div class="bg-secondary d-flex align-items-center justify-content-center rounded"
                     style="height: 400px;">
                    <i class="fas fa-image fa-5x text-white"></i>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
    <div class="col-md-6">
        <h2>${product.name}</h2>
        <p class="text-muted"><i class="fas fa-tags"></i> Danh mục: ${product.category.name}</p>
        <hr>
        <h3 class="text-success">
            <fmt:formatNumber value="${product.price}" type="number" groupingUsed="true" maxFractionDigits="0"/>₫
        </h3>
        <p class="lead">${product.description}</p>
        <p>
            <strong><i class="fas fa-warehouse"></i> Tình Trạng Kho:</strong>
            <c:choose>
                <c:when test="${product.stockQuantity > 0}">
                    <span class="text-success">Còn ${product.stockQuantity} sản phẩm</span>
                </c:when>
                <c:otherwise>
                    <span class="text-danger">Hết hàng</span>
                </c:otherwise>
            </c:choose>
        </p>
        <hr>
        <form action="${pageContext.request.contextPath}/cart/add" method="post" id="addToCartForm">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            <input type="hidden" name="productId" value="${product.id}"/>
            <div class="row mb-3">
                <div class="col-md-4">
                    <label for="quantity" class="form-label"><i class="fas fa-sort-numeric-up"></i> Số Lượng:</label>
                    <input type="number" class="form-control" id="quantity" name="quantity"
                           value="1" min="1" max="${product.stockQuantity}" required>
                </div>
            </div>
            <button type="submit" class="btn btn-success btn-lg"
                    <c:if test="${product.stockQuantity == 0}">disabled</c:if>>
                <i class="fas fa-cart-plus"></i> Thêm Vào Giỏ Hàng
            </button>
            <a href="${pageContext.request.contextPath}/products" class="btn btn-secondary btn-lg">
                <i class="fas fa-arrow-left"></i> Quay Lại
            </a>
        </form>
    </div>
</div>

<jsp:include page="../common/footer.jsp"/>
