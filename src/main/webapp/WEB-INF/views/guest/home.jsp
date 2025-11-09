<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="pageTitle" value="Home - Shopping Store" scope="request"/>
<jsp:include page="../common/header.jsp"/>

<div class="jumbotron bg-light p-5 rounded">
    <h1 class="display-4">Welcome to Shopping Store!</h1>
    <p class="lead">Find the best products at the best prices</p>
    <a class="btn btn-primary btn-lg" href="${pageContext.request.contextPath}/products" role="button">
        Shop Now
    </a>
</div>

<h2 class="mt-5 mb-4">Featured Products</h2>
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
                                <button type="submit" class="btn btn-success btn-sm w-100">
                                    <i class="fas fa-cart-plus"></i> Add to Cart
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
        No products available at the moment.
    </div>
</c:if>

<jsp:include page="../common/footer.jsp"/>
