<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="pageTitle" value="Sản Phẩm - Shopping Store" scope="request"/>
<jsp:include page="../common/header.jsp"/>

<h2 class="mb-4"><i class="fas fa-box"></i> Danh Sách Sản Phẩm</h2>

<div class="row mb-4">
    <div class="col-md-6">
        <form action="${pageContext.request.contextPath}/products" method="get" class="d-flex">
            <input type="search" name="search" class="form-control me-2"
                   placeholder="Tìm kiếm sản phẩm..." value="${search}">
            <button type="submit" class="btn btn-primary">
                <i class="fas fa-search"></i> Tìm
            </button>
        </form>
    </div>
    <div class="col-md-6">
        <select class="form-select" id="categoryFilter" onchange="filterByCategory()">
            <option value="">Tất Cả Danh Mục</option>
            <c:forEach items="${categories}" var="category">
                <option value="${category.id}"
                        <c:if test="${selectedCategoryId == category.id}">selected</c:if>>
                        ${category.name}
                </option>
            </c:forEach>
        </select>
    </div>
</div>

<div class="row">
    <c:forEach items="${products}" var="product">
        <div class="col-md-3 mb-4">
            <div class="card h-100">
                <c:choose>
                    <c:when test="${product.imageUrl != null && !product.imageUrl.isEmpty()}">
                        <div style="height: 200px; background-color: #f8f9fa; display: flex; align-items: center; justify-content: center; padding: 10px;">
                            <img src="${product.imageUrl}" class="card-img-top" alt="${product.name}"
                                 style="max-height: 100%; max-width: 100%; object-fit: contain;">
                        </div>
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
                    <p class="card-text"><small class="text-muted">${product.category.name}</small></p>
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
                            <button type="submit" class="btn btn-success btn-sm w-100"
                                    <c:if test="${product.stockQuantity == 0}">disabled</c:if>>
                                <i class="fas fa-cart-plus"></i> Thêm Vào Giỏ
                            </button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </c:forEach>
</div>

<c:if test="${products.size() == 0}">
    <div class="alert alert-info">
        <i class="fas fa-info-circle"></i> Không tìm thấy sản phẩm nào.
    </div>
</c:if>

<script>
    function filterByCategory() {
        const categoryId = document.getElementById('categoryFilter').value;
        if (categoryId) {
            window.location.href = '${pageContext.request.contextPath}/products?categoryId=' + categoryId;
        } else {
            window.location.href = '${pageContext.request.contextPath}/products';
        }
    }
</script>

<jsp:include page="../common/footer.jsp"/>
