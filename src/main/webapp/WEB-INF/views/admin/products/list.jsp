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
        border: 2px solid rgba(224, 187, 228, 0.3);
        border-radius: 20px;
        padding: 20px;
        transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
        position: relative;
        overflow: hidden;
        box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
    }

    .admin-product-card::before {
        content: '';
        position: absolute;
        top: 0;
        left: 0;
        right: 0;
        height: 4px;
        background: linear-gradient(90deg, var(--pastel-pink), var(--pastel-purple), var(--pastel-blue));
        opacity: 0;
        transition: opacity 0.3s ease;
    }

    .admin-product-card:hover::before {
        opacity: 1;
    }

    .admin-product-card:hover {
        transform: translateY(-8px);
        box-shadow: 0 20px 40px rgba(224, 187, 228, 0.3);
        border-color: rgba(224, 187, 228, 0.6);
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

    .no-image-fallback {
        width: 100%;
        height: 100%;
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 4rem;
        color: var(--pastel-purple);
        background: linear-gradient(135deg, #FFF5F7 0%, #F0F8FF 100%);
    }

    .no-image-fallback i {
        opacity: 0.3;
    }

    .product-badge {
        position: absolute;
        top: 10px;
        right: 10px;
        background: linear-gradient(135deg, var(--pastel-pink), var(--pastel-purple));
        color: white;
        padding: 6px 14px;
        border-radius: 20px;
        font-weight: 700;
        font-size: 11px;
        z-index: 1;
        box-shadow: 0 3px 10px rgba(224, 187, 228, 0.5);
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
        gap: 8px;
        margin-top: 10px;
    }

    .product-actions .btn {
        flex: 1;
        padding: 10px 12px;
        border-radius: 12px;
        font-size: 13px;
        font-weight: 600;
        border: none;
        transition: all 0.2s ease;
        display: flex;
        align-items: center;
        justify-content: center;
        gap: 6px;
    }

    .product-actions .btn i {
        font-size: 14px;
    }

    .product-actions .btn-warning {
        background: linear-gradient(135deg, #ffc107 0%, #ffb300 100%);
        color: white;
    }

    .product-actions .btn-warning:hover {
        background: linear-gradient(135deg, #ffb300 0%, #ffa000 100%);
        transform: translateY(-2px);
        box-shadow: 0 4px 12px rgba(255, 193, 7, 0.3);
    }

    .product-actions .btn-success {
        background: linear-gradient(135deg, #28a745 0%, #20c997 100%);
        color: white;
    }

    .product-actions .btn-success:hover {
        background: linear-gradient(135deg, #20c997 0%, #17a2b8 100%);
        transform: translateY(-2px);
        box-shadow: 0 4px 12px rgba(32, 201, 151, 0.3);
    }

    .product-actions .btn-secondary {
        background: linear-gradient(135deg, #6c757d 0%, #5a6268 100%);
        color: white;
    }

    .product-actions .btn-secondary:hover {
        background: linear-gradient(135deg, #5a6268 0%, #545b62 100%);
        transform: translateY(-2px);
        box-shadow: 0 4px 12px rgba(108, 117, 125, 0.3);
    }

    .product-actions .btn-danger {
        background: linear-gradient(135deg, #dc3545 0%, #c82333 100%);
        color: white;
    }

    .product-actions .btn-danger:hover {
        background: linear-gradient(135deg, #c82333 0%, #bd2130 100%);
        transform: translateY(-2px);
        box-shadow: 0 4px 12px rgba(220, 53, 69, 0.3);
    }

    .product-actions form {
        flex: 1;
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
        border: 2px solid rgba(224, 187, 228, 0.3);
        border-radius: 20px;
        padding: 25px;
        margin-bottom: 30px;
        box-shadow: 0 4px 12px rgba(0, 0, 0, 0.06);
        transition: all 0.3s ease;
    }

    .search-box-container:hover {
        border-color: rgba(224, 187, 228, 0.6);
        box-shadow: 0 6px 20px rgba(167, 199, 231, 0.15);
    }

    .search-box-container .input-group-text {
        border: none;
        font-weight: 600;
    }

    .search-box-container .form-control,
    .search-box-container .form-select {
        border: 2px solid rgba(224, 187, 228, 0.3);
        font-weight: 500;
        transition: all 0.2s ease;
    }

    .search-box-container .form-control:focus,
    .search-box-container .form-select:focus {
        border-color: var(--pastel-purple);
        box-shadow: 0 0 0 0.2rem rgba(224, 187, 228, 0.2);
    }

    .search-box-container .btn-primary {
        background: linear-gradient(135deg, var(--pastel-pink) 0%, var(--pastel-purple) 100%);
        border: none;
        font-weight: 700;
        transition: all 0.2s ease;
    }

    .search-box-container .btn-primary:hover {
        background: linear-gradient(135deg, var(--pastel-purple) 0%, var(--pastel-blue) 100%);
        transform: translateY(-2px);
        box-shadow: 0 4px 12px rgba(224, 187, 228, 0.4);
    }

    .search-box-container .btn-secondary {
        background: linear-gradient(135deg, #6c757d 0%, #5a6268 100%);
        border: none;
        font-weight: 700;
        transition: all 0.2s ease;
    }

    .search-box-container .btn-secondary:hover {
        background: linear-gradient(135deg, #5a6268 0%, #545b62 100%);
        transform: translateY(-2px);
        box-shadow: 0 4px 12px rgba(108, 117, 125, 0.3);
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
    <h2><i class="fas fa-boxes"></i> Quản lý sản phẩm</h2>
    <a href="${pageContext.request.contextPath}/admin/products/new" class="btn btn-primary float-animation">
        <i class="fas fa-plus-circle"></i> Thêm sản phẩm mới
    </a>
</div>

<div class="search-box-container">
    <form action="${pageContext.request.contextPath}/admin/products" method="get" class="search-form" id="filterForm">
        <div class="row g-3">
            <div class="col-md-4">
                <div class="input-group">
                    <span class="input-group-text" style="background: var(--pastel-purple); color: white; border: 2px solid var(--pastel-purple); border-radius: 20px 0 0 20px;">
                        <i class="fas fa-search"></i>
                    </span>
                    <input type="search" name="search" class="form-control" style="border-radius: 0 20px 20px 0;"
                           placeholder="Tìm kiếm sản phẩm..." value="${search}">
                </div>
            </div>

            <div class="col-md-2">
                <div class="input-group">
                    <span class="input-group-text" style="background: var(--pastel-blue); color: white; border: 2px solid var(--pastel-blue); border-radius: 20px 0 0 20px;">
                        <i class="fas fa-folder"></i>
                    </span>
                    <select name="categoryId" class="form-select" style="border-radius: 0 20px 20px 0;">
                        <option value="">Tất cả danh mục</option>
                        <c:forEach items="${categories}" var="cat">
                            <option value="${cat.id}" <c:if test="${categoryId == cat.id}">selected</c:if>>
                                ${cat.name}
                            </option>
                        </c:forEach>
                    </select>
                </div>
            </div>

            <div class="col-md-2">
                <div class="input-group">
                    <span class="input-group-text" style="background: var(--pastel-green); color: white; border: 2px solid var(--pastel-green); border-radius: 20px 0 0 20px;">
                        <i class="fas fa-toggle-on"></i>
                    </span>
                    <select name="active" class="form-select" style="border-radius: 0 20px 20px 0;">
                        <option value="">Tất cả trạng thái</option>
                        <option value="true" <c:if test="${active == true}">selected</c:if>>Đang bán</option>
                        <option value="false" <c:if test="${active == false}">selected</c:if>>Đã ẩn</option>
                    </select>
                </div>
            </div>

            <div class="col-md-2">
                <div class="input-group">
                    <span class="input-group-text" style="background: var(--pastel-yellow); color: var(--text-dark); border: 2px solid var(--pastel-yellow); border-radius: 20px 0 0 20px;">
                        <i class="fas fa-sort"></i>
                    </span>
                    <select name="sortBy" class="form-select" style="border-radius: 0 20px 20px 0;">
                        <option value="">Sắp xếp</option>
                        <option value="priceAsc" <c:if test="${sortBy == 'priceAsc'}">selected</c:if>>Giá tăng dần</option>
                        <option value="priceDesc" <c:if test="${sortBy == 'priceDesc'}">selected</c:if>>Giá giảm dần</option>
                        <option value="nameAsc" <c:if test="${sortBy == 'nameAsc'}">selected</c:if>>Tên A-Z</option>
                        <option value="nameDesc" <c:if test="${sortBy == 'nameDesc'}">selected</c:if>>Tên Z-A</option>
                        <option value="stockAsc" <c:if test="${sortBy == 'stockAsc'}">selected</c:if>>Kho tăng dần</option>
                        <option value="stockDesc" <c:if test="${sortBy == 'stockDesc'}">selected</c:if>>Kho giảm dần</option>
                    </select>
                </div>
            </div>

            <div class="col-md-2">
                <button type="submit" class="btn btn-primary w-100">
                    <i class="fas fa-filter"></i> Lọc
                </button>
            </div>
        </div>

        <c:if test="${search != null || categoryId != null || active != null || sortBy != null}">
            <div class="row mt-3">
                <div class="col-12 text-center">
                    <a href="${pageContext.request.contextPath}/admin/products" class="btn btn-secondary">
                        <i class="fas fa-times"></i> Xóa bộ lọc
                    </a>
                </div>
            </div>
        </c:if>
    </form>
</div>

<c:choose>
    <c:when test="${products.size() > 0}">
        <div class="product-list-container">
            <div style="margin-bottom: 20px; color: var(--text-medium); font-weight: 600;">
                <i class="fas fa-star"></i> Tìm thấy ${products.size()} sản phẩm
            </div>

            <div class="product-grid">
                <c:forEach items="${products}" var="product">
                    <div class="admin-product-card">
                        <div class="product-image-container">
                            <c:choose>
                                <c:when test="${product.imageUrl != null && !product.imageUrl.isEmpty()}">
                                    <c:choose>
                                        <c:when test="${product.imageUrl.startsWith('http://') || product.imageUrl.startsWith('https://')}">
                                            <img src="${product.imageUrl}"
                                                 alt="${product.name}"
                                                 onerror="this.style.display='none'; this.nextElementSibling.style.display='flex';">
                                        </c:when>
                                        <c:otherwise>
                                            <img src="${pageContext.request.contextPath}${product.imageUrl}"
                                                 alt="${product.name}"
                                                 onerror="this.style.display='none'; this.nextElementSibling.style.display='flex';">
                                        </c:otherwise>
                                    </c:choose>
                                    <div class="no-image-fallback" style="display: none;">
                                        <i class="fas fa-image"></i>
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <div class="no-image-fallback" style="display: flex;">
                                        <i class="fas fa-image"></i>
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
                                <i class="fas fa-folder"></i> ${product.category.name}
                            </div>

                            <div class="product-price">
                                <i class="fas fa-tag"></i> <fmt:formatNumber value="${product.price}" pattern="#,##0.00"/>đ
                            </div>

                            <div class="product-stock">
                                <i class="fas fa-box"></i> Kho: ${product.stockQuantity}
                                <c:choose>
                                    <c:when test="${product.active}">
                                        <span class="badge bg-success" style="margin-left: 10px;"><i class="fas fa-check-circle"></i> Đang bán</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge bg-secondary" style="margin-left: 10px;"><i class="fas fa-eye-slash"></i> Đã ẩn</span>
                                    </c:otherwise>
                                </c:choose>
                            </div>

                            <div class="product-actions">
                                <a href="${pageContext.request.contextPath}/admin/products/${product.id}/edit"
                                   class="btn btn-warning" style="flex: 1;">
                                    <i class="fas fa-edit"></i> Sửa
                                </a>

                                <form action="${pageContext.request.contextPath}/admin/products/${product.id}/toggle-status"
                                      method="post" style="flex: 1;">
                                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                    <button type="submit" class="btn ${product.active ? 'btn-secondary' : 'btn-success'} w-100">
                                        <i class="fas ${product.active ? 'fa-eye-slash' : 'fa-eye'}"></i>
                                        ${product.active ? 'Ẩn' : 'Hiện'}
                                    </button>
                                </form>

                                <form action="${pageContext.request.contextPath}/admin/products/${product.id}/delete"
                                      method="post" style="flex: 1;"
                                      onsubmit="return confirm('Bạn chắc chắn muốn xóa sản phẩm này?')">
                                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                    <button type="submit" class="btn btn-danger w-100">
                                        <i class="fas fa-trash-alt"></i> Xóa
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
            <div class="no-products-icon"><i class="fas fa-search"></i></div>
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
                <i class="fas fa-plus-circle"></i> Thêm sản phẩm ngay
            </a>
        </div>
    </c:otherwise>
</c:choose>

<jsp:include page="../../common/footer.jsp"/>
