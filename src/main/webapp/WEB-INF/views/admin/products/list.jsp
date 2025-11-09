<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="pageTitle" value="Manage Products - Admin" scope="request"/>
<jsp:include page="../../common/header.jsp"/>

<div class="d-flex justify-content-between align-items-center mb-4">
    <h2>Manage Products</h2>
    <a href="${pageContext.request.contextPath}/admin/products/new" class="btn btn-primary">
        <i class="fas fa-plus"></i> Add New Product
    </a>
</div>

<div class="mb-4">
    <form action="${pageContext.request.contextPath}/admin/products" method="get" class="d-flex">
        <input type="search" name="search" class="form-control me-2"
               placeholder="Search products..." value="${search}">
        <button type="submit" class="btn btn-primary">
            <i class="fas fa-search"></i> Search
        </button>
    </form>
</div>

<c:choose>
    <c:when test="${products.size() > 0}">
        <div class="table-responsive">
            <table class="table table-striped">
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Image</th>
                    <th>Name</th>
                    <th>Category</th>
                    <th>Price</th>
                    <th>Stock</th>
                    <th>Status</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${products}" var="product">
                    <tr>
                        <td>${product.id}</td>
                        <td>
                            <c:choose>
                                <c:when test="${product.imageUrl != null && !product.imageUrl.isEmpty()}">
                                    <img src="${product.imageUrl}" alt="${product.name}"
                                         style="width: 50px; height: 50px; object-fit: cover;">
                                </c:when>
                                <c:otherwise>
                                    <div class="bg-secondary" style="width: 50px; height: 50px;"></div>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td>${product.name}</td>
                        <td>${product.category.name}</td>
                        <td>
                            <fmt:formatNumber value="${product.price}" type="currency" currencySymbol="$"/>
                        </td>
                        <td>${product.stockQuantity}</td>
                        <td>
                            <c:choose>
                                <c:when test="${product.active}">
                                    <span class="badge bg-success">Active</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="badge bg-secondary">Inactive</span>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <a href="${pageContext.request.contextPath}/admin/products/${product.id}/edit"
                               class="btn btn-warning btn-sm">
                                <i class="fas fa-edit"></i>
                            </a>
                            <form action="${pageContext.request.contextPath}/admin/products/${product.id}/delete"
                                  method="post" class="d-inline" onsubmit="return confirm('Are you sure?')">
                                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                <button type="submit" class="btn btn-danger btn-sm">
                                    <i class="fas fa-trash"></i>
                                </button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </c:when>
    <c:otherwise>
        <div class="alert alert-info">
            No products found.
        </div>
    </c:otherwise>
</c:choose>

<jsp:include page="../../common/footer.jsp"/>
