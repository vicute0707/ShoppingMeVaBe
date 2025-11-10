<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="pageTitle" value="Manage Products - Admin" scope="request"/>
<jsp:include page="../../common/header.jsp"/>

<link rel="stylesheet" href="${pageContext.request.contextPath}/css/pastel-theme.css">

<style>
    .product-list-container {
        background: white;
        border-radius: 25px;
        padding: 30px;
        box-shadow: 0 10px 30px rgba(0,0,0,0.1);
        border: 3px solid var(--pastel-purple);
    }

    .product-grid {
        display: grid;
        grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
        gap: 25px;
        margin-top: 25px;
    }

    .admin-product-card {
        background: white;
        border: 3px solid var(--pastel-purple);
        border-radius: 20px;
        padding: 20px;
        transition: all 0.3s ease;
        position: relative;
        overflow: hidden;
    }

    .admin-product-card::before {
        content: '';
        position: absolute;
        top: 0;
        left: 0;
        right: 0;
        height: 5px;
        background: linear-gradient(90deg, var(--pastel-pink), var(--pastel-purple), var(--pastel-blue));
    }

    .admin-product-card:hover {
        transform: translateY(-8px) scale(1.02);
        box-shadow: 0 15px 35px rgba(224, 187, 228, 0.4);
        border-color: var(--pastel-pink);
    }

    .product-image-container {
        position: relative;
        border-radius: 15px;
        overflow: hidden;
        height: 200px;
        background: linear-gradient(135deg, var(--soft-white) 0%, #FFF5F8 100%);
        margin-bottom: 15px;
    }

    .product-image-container img {
        width: 100%;
        height: 100%;
        object-fit: cover;
        transition: transform 0.3s ease;
    }

    .admin-product-card:hover .product-image-container img {
        transform: scale(1.1);
    }

    .product-badge {
        position: absolute;
        top: 10px;
        right: 10px;
        background: rgba(255, 255, 255, 0.95);
        padding: 5px 12px;
        border-radius: 15px;
        font-weight: 600;
        font-size: 12px;
        z-index: 1;
    }

    .product-info {
        padding: 10px 0;
    }

    .product-name {
        font-size: 18px;
        font-weight: 700;
        color: var(--text-dark);
        margin-bottom: 8px;
        display: -webkit-box;
        -webkit-line-clamp: 2;
        -webkit-box-orient: vertical;
        overflow: hidden;
    }

    .product-category {
        color: var(--text-medium);
        font-size: 14px;
        margin-bottom: 10px;
    }

    .product-price {
        font-size: 24px;
        font-weight: 700;
        color: var(--pastel-purple);
        margin-bottom: 10px;
    }

    .product-stock {
        font-size: 14px;
        color: var(--text-medium);
        margin-bottom: 15px;
    }

    .product-actions {
        display: flex;
        gap: 10px;
    }

    .product-actions .btn {
        flex: 1;
        padding: 10px;
        border-radius: 15px;
        font-size: 14px;
    }

    .page-header {
        background: linear-gradient(135deg, var(--pastel-pink) 0%, var(--pastel-purple) 50%, var(--pastel-blue) 100%);
        border-radius: 25px;
        padding: 30px;
        margin-bottom: 30px;
        color: white;
        display: flex;
        justify-content: space-between;
        align-items: center;
        box-shadow: 0 10px 30px rgba(224, 187, 228, 0.4);
    }

    .page-header h2 {
        margin: 0;
        font-weight: 700;
        font-size: 32px;
    }

    .page-header h2::before,
    .page-header h2::after {
        color: white;
    }

    .search-box-container {
        background: white;
        border: 3px solid var(--pastel-purple);
        border-radius: 20px;
        padding: 25px;
        margin-bottom: 30px;
        box-shadow: 0 5px 15px rgba(0,0,0,0.08);
    }

    .search-form {
        display: flex;
        gap: 15px;
    }

    .search-form input {
        flex: 1;
        border: 2px solid var(--pastel-purple);
        border-radius: 20px;
        padding: 12px 20px;
    }

    .no-products {
        text-align: center;
        padding: 60px 20px;
        background: linear-gradient(135deg, var(--soft-white) 0%, #FFF5F8 100%);
        border-radius: 25px;
        border: 3px dashed var(--pastel-purple);
    }

    .no-products-icon {
        font-size: 80px;
        margin-bottom: 20px;
    }
</style>

<div class="page-header">
    <h2>📦 Quản lý sản phẩm</h2>
    <a href="${pageContext.request.contextPath}/admin/products/new" class="btn btn-primary float-animation">
        <i class="fas fa-plus"></i> 🎀 Thêm sản phẩm mới
    </a>
</div>

<div class="search-box-container">
    <form action="${pageContext.request.contextPath}/admin/products" method="get" class="search-form">
        <input type="search" name="search" class="form-control"
               placeholder="🔍 Tìm kiếm sản phẩm..." value="${search}">
        <button type="submit" class="btn btn-primary">
            <i class="fas fa-search"></i> Tìm kiếm
        </button>
    </form>
</div>

<c:choose>
    <c:when test="${products.size() > 0}">
        <div class="product-list-container">
            <div style="margin-bottom: 20px; color: var(--text-medium); font-weight: 600;">
                💫 Tìm thấy ${products.size()} sản phẩm
            </div>

            <div class="product-grid">
                <c:forEach items="${products}" var="product">
                    <div class="admin-product-card">
                        <div class="product-image-container">
                            <c:choose>
                                <c:when test="${product.imageUrl != null && !product.imageUrl.isEmpty()}">
                                    <img src="${pageContext.request.contextPath}${product.imageUrl}"
                                         alt="${product.name}"
                                         onerror="this.src='${pageContext.request.contextPath}/images/no-image.png'">
                                </c:when>
                                <c:otherwise>
                                    <div style="display: flex; align-items: center; justify-content: center; height: 100%; font-size: 60px; color: var(--pastel-purple);">
                                        🖼️
                                    </div>
                                </c:otherwise>
                            </c:choose>

                            <div class="product-badge">
                                #${product.id}
                            </div>
                        </div>

                        <div class="product-info">
                            <div class="product-name" title="${product.name}">
                                ${product.name}
                            </div>

                            <div class="product-category">
                                📁 ${product.category.name}
                            </div>

                            <div class="product-price">
                                💰 <fmt:formatNumber value="${product.price}" pattern="#,##0.00"/>đ
                            </div>

                            <div class="product-stock">
                                📦 Kho: ${product.stockQuantity}
                                <c:choose>
                                    <c:when test="${product.active}">
                                        <span class="badge bg-success" style="margin-left: 10px;">✅ Đang bán</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge bg-secondary" style="margin-left: 10px;">❌ Đã ẩn</span>
                                    </c:otherwise>
                                </c:choose>
                            </div>

                            <div class="product-actions">
                                <a href="${pageContext.request.contextPath}/admin/products/${product.id}/edit"
                                   class="btn btn-warning">
                                    <i class="fas fa-edit"></i> Sửa
                                </a>
                                <form action="${pageContext.request.contextPath}/admin/products/${product.id}/delete"
                                      method="post" style="flex: 1;"
                                      onsubmit="return confirm('🗑️ Bạn chắc chắn muốn xóa sản phẩm này?')">
                                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                    <button type="submit" class="btn btn-danger w-100">
                                        <i class="fas fa-trash"></i> Xóa
                                    </button>
                                </form>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </div>
    </c:when>
    <c:otherwise>
        <div class="no-products">
            <div class="no-products-icon">🔍</div>
            <h3 style="color: var(--text-dark); margin-bottom: 15px;">
                Không tìm thấy sản phẩm nào
            </h3>
            <p style="color: var(--text-medium); margin-bottom: 25px;">
                <c:choose>
                    <c:when test="${search != null && !search.isEmpty()}">
                        Không có sản phẩm nào khớp với từ khóa "<strong>${search}</strong>"
                    </c:when>
                    <c:otherwise>
                        Chưa có sản phẩm nào. Hãy thêm sản phẩm đầu tiên!
                    </c:otherwise>
                </c:choose>
            </p>
            <a href="${pageContext.request.contextPath}/admin/products/new" class="btn btn-primary">
                <i class="fas fa-plus"></i> 🎀 Thêm sản phẩm ngay
            </a>
        </div>
    </c:otherwise>
</c:choose>

<jsp:include page="../../common/footer.jsp"/>
