<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="pageTitle" value="Products - Shopping Store" scope="request"/>
<jsp:include page="../common/header.jsp"/>

<h2 class="mb-4">Products</h2>

<div class="row mb-4">
    <div class="col-md-6">
        <form action="${pageContext.request.contextPath}/products" method="get" class="d-flex">
            <input type="search" name="search" class="form-control me-2"
                   placeholder="Search products..." value="${search}">
            <button type="submit" class="btn btn-primary">
                <i class="fas fa-search"></i> Search
            </button>
        </form>
    </div>
    <div class="col-md-6">
        <select class="form-select" id="categoryFilter" onchange="filterByCategory()">
            <option value="">All Categories</option>
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
                    <p class="card-text"><small class="text-muted">${product.category.name}</small></p>
                    <p class="card-text text-truncate">${product.description}</p>
                    <p class="card-text">
                        <strong>Price:</strong>
                        <fmt:formatNumber value="${product.price}" type="currency" currencySymbol="$"/>
                    </p>
                    <p class="card-text">
                        <small class="text-muted">Stock: ${product.stockQuantity}</small>
                    </p>
                </div>
                <div class="card-footer">
                    <div class="d-grid gap-2">
                        <a href="${pageContext.request.contextPath}/products/detail?id=${product.id}"
                           class="btn btn-primary btn-sm">View Details</a>
                        <form action="${pageContext.request.contextPath}/cart/add" method="post">
                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                            <input type="hidden" name="productId" value="${product.id}"/>
                            <input type="hidden" name="quantity" value="1"/>
                            <button type="submit" class="btn btn-success btn-sm w-100"
                                    <c:if test="${product.stockQuantity == 0}">disabled</c:if>>
                                <i class="fas fa-cart-plus"></i> Add to Cart
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
        No products found.
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
