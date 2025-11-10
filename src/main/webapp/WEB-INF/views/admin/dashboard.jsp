<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Trang Quản Trị - Shopping Store" scope="request"/>
<jsp:include page="../common/header.jsp"/>

<h2 class="mb-4">Bảng Điều Khiển Quản Trị</h2>

<div class="row">
    <div class="col-md-3 mb-4">
        <div class="card bg-primary text-white">
            <div class="card-body">
                <h5 class="card-title"><i class="fas fa-users"></i> Người Dùng</h5>
                <h2>${totalUsers}</h2>
                <a href="${pageContext.request.contextPath}/admin/users" class="btn btn-light btn-sm">Xem Chi Tiết</a>
            </div>
        </div>
    </div>
    <div class="col-md-3 mb-4">
        <div class="card bg-success text-white">
            <div class="card-body">
                <h5 class="card-title"><i class="fas fa-box"></i> Sản Phẩm</h5>
                <h2>${totalProducts}</h2>
                <a href="${pageContext.request.contextPath}/admin/products" class="btn btn-light btn-sm">Xem Chi Tiết</a>
            </div>
        </div>
    </div>
    <div class="col-md-3 mb-4">
        <div class="card bg-warning text-white">
            <div class="card-body">
                <h5 class="card-title"><i class="fas fa-tags"></i> Danh Mục</h5>
                <h2>${totalCategories}</h2>
                <a href="${pageContext.request.contextPath}/admin/categories" class="btn btn-light btn-sm">Xem Chi Tiết</a>
            </div>
        </div>
    </div>
    <div class="col-md-3 mb-4">
        <div class="card bg-info text-white">
            <div class="card-body">
                <h5 class="card-title"><i class="fas fa-shopping-cart"></i> Đơn Hàng</h5>
                <h2>${totalOrders}</h2>
                <a href="${pageContext.request.contextPath}/admin/orders" class="btn btn-light btn-sm">Xem Chi Tiết</a>
            </div>
        </div>
    </div>
</div>

<div class="row">
    <div class="col-md-12">
        <div class="card">
            <div class="card-header">
                <h5>Truy Cập Nhanh</h5>
            </div>
            <div class="card-body">
                <div class="list-group">
                    <a href="${pageContext.request.contextPath}/admin/categories" class="list-group-item list-group-item-action">
                        <i class="fas fa-tags"></i> Quản Lý Danh Mục
                    </a>
                    <a href="${pageContext.request.contextPath}/admin/products" class="list-group-item list-group-item-action">
                        <i class="fas fa-box"></i> Quản Lý Sản Phẩm
                    </a>
                    <a href="${pageContext.request.contextPath}/admin/users" class="list-group-item list-group-item-action">
                        <i class="fas fa-users"></i> Quản Lý Người Dùng
                    </a>
                    <a href="${pageContext.request.contextPath}/admin/orders" class="list-group-item list-group-item-action">
                        <i class="fas fa-shopping-cart"></i> Quản Lý Đơn Hàng
                    </a>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="../common/footer.jsp"/>
