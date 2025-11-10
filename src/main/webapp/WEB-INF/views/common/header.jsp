<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${pageTitle != null ? pageTitle : 'Shopping MeVaBe - Cute Shopping Store'}</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Quicksand:wght@300;400;500;600;700&family=Nunito:wght@300;400;600;700;800&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/pastel-theme.css">
    <style>
        /* Global Typography - Cute & Easy to Read */
        * {
            font-family: 'Nunito', 'Quicksand', -apple-system, BlinkMacSystemFont, 'Segoe UI', sans-serif;
        }

        html, body {
            height: 100%;
            margin: 0;
            padding: 0;
        }

        body {
            min-height: 100vh;
            display: flex;
            flex-direction: column;
            font-family: 'Nunito', 'Quicksand', sans-serif;
            font-weight: 400;
            line-height: 1.6;
            color: var(--text-dark);
            background: linear-gradient(135deg, #FFF5F7 0%, #FFF8FA 50%, #F0F8FF 100%);
            background-attachment: fixed;
        }

        #main-content {
            flex: 1 0 auto;
            width: 100%;
        }

        footer {
            flex-shrink: 0;
            width: 100%;
            margin-top: auto;
        }

        h1, h2, h3, h4, h5, h6 {
            font-family: 'Quicksand', sans-serif;
            font-weight: 700;
            letter-spacing: -0.5px;
        }

        p, span, div, label {
            font-family: 'Nunito', sans-serif;
            font-weight: 500;
        }

        .btn {
            font-family: 'Nunito', sans-serif;
            font-weight: 700;
            letter-spacing: 0.3px;
        }

        /* Content Focus - Clean & Minimal */
        .container {
            max-width: 1400px;
        }

        /* Smooth Transitions */
        * {
            transition: all 0.2s ease;
        }

        /* Pastel Navbar */
        .navbar-pastel {
            background: linear-gradient(135deg, #FFD1DC 0%, #E0BBE4 50%, #A7C7E7 100%) !important;
            box-shadow: 0 4px 15px rgba(224, 187, 228, 0.3);
            padding: 15px 0;
        }

        .navbar-brand {
            font-weight: 700 !important;
            font-size: 1.8rem !important;
            color: white !important;
            display: flex;
            align-items: center;
            gap: 10px;
        }

        .navbar-brand i {
            font-size: 2rem;
            color: #FFD1DC;
            background: white;
            padding: 8px;
            border-radius: 50%;
        }

        .nav-link {
            color: white !important;
            font-weight: 600 !important;
            padding: 10px 20px !important;
            border-radius: 20px !important;
            transition: all 0.3s ease !important;
            display: flex;
            align-items: center;
            gap: 8px;
        }

        .nav-link:hover {
            background: rgba(255, 255, 255, 0.3) !important;
            transform: translateY(-2px);
        }

        .nav-link i {
            font-size: 1.2rem;
        }

        .navbar-nav .dropdown-menu {
            border-radius: 15px;
            border: 2px solid #E0BBE4;
            box-shadow: 0 8px 20px rgba(0,0,0,0.1);
        }

        .dropdown-item {
            border-radius: 10px;
            margin: 5px;
            padding: 10px 15px;
            font-weight: 600;
        }

        .dropdown-item:hover {
            background: linear-gradient(135deg, #FFD1DC 0%, #E0BBE4 100%);
            color: white;
        }

        .badge-cart {
            background: #ff6b9d !important;
            border-radius: 50%;
            padding: 4px 8px;
            font-size: 12px;
            font-weight: 700;
            position: absolute;
            top: 5px;
            right: 5px;
        }

        .cart-icon-wrapper {
            position: relative;
            display: inline-block;
        }

        /* Alert Messages */
        .alert-container {
            position: fixed;
            top: 80px;
            right: 20px;
            z-index: 9999;
            max-width: 400px;
        }

        .alert {
            border-radius: 15px !important;
            border: 2px solid;
            padding: 15px 20px;
            display: flex;
            align-items: center;
            gap: 12px;
            animation: slideIn 0.3s ease;
        }

        @keyframes slideIn {
            from {
                transform: translateX(100%);
                opacity: 0;
            }
            to {
                transform: translateX(0);
                opacity: 1;
            }
        }

        .alert i {
            font-size: 1.5rem;
        }

        .alert-success {
            background: var(--pastel-green) !important;
            border-color: var(--pastel-mint) !important;
            color: var(--text-dark) !important;
        }

        .alert-success i {
            color: #28a745;
        }

        .alert-danger {
            background: #ffb3ba !important;
            border-color: #ffc9de !important;
            color: var(--text-dark) !important;
        }

        .alert-danger i {
            color: #dc3545;
        }

        /* User Dropdown */
        .user-dropdown {
            background: rgba(255, 255, 255, 0.3);
            border-radius: 25px;
            padding: 5px 15px;
        }

        .user-dropdown:hover {
            background: rgba(255, 255, 255, 0.5);
        }

        /* Mobile Responsive */
        @media (max-width: 768px) {
            .navbar-brand {
                font-size: 1.4rem !important;
            }

            .navbar-brand i {
                font-size: 1.5rem;
            }

            .nav-link {
                padding: 8px 15px !important;
                margin: 5px 0;
            }
        }
    </style>
</head>
<body>
<nav class="navbar navbar-expand-lg navbar-pastel">
    <div class="container">
        <a class="navbar-brand" href="${pageContext.request.contextPath}/">
            <i class="fas fa-heart"></i>
            <span>Shopping MeVaBe</span>
        </a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav"
                style="background: white; border-radius: 10px;">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav me-auto">
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/">
                        <i class="fas fa-home"></i>
                        <span>Trang chủ</span>
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/products">
                        <i class="fas fa-shopping-bag"></i>
                        <span>Sản phẩm</span>
                    </a>
                </li>
                <c:if test="${pageContext.request.userPrincipal != null && pageContext.request.isUserInRole('CUSTOMER')}">
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/checkout/orders">
                            <i class="fas fa-box"></i>
                            <span>Đơn hàng</span>
                        </a>
                    </li>
                </c:if>
                <c:if test="${pageContext.request.userPrincipal != null && pageContext.request.isUserInRole('ADMIN')}">
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/admin/dashboard">
                            <i class="fas fa-tachometer-alt"></i>
                            <span>Quản trị</span>
                        </a>
                    </li>
                </c:if>
            </ul>
            <ul class="navbar-nav">
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/cart">
                        <div class="cart-icon-wrapper">
                            <i class="fas fa-shopping-cart"></i>
                            <c:if test="${sessionScope.cart != null && !sessionScope.cart.isEmpty()}">
                                <span class="badge-cart">${sessionScope.cart.totalItems}</span>
                            </c:if>
                        </div>
                        <span>Giỏ hàng</span>
                    </a>
                </li>
                <c:choose>
                    <c:when test="${pageContext.request.userPrincipal != null}">
                        <li class="nav-item dropdown">
                            <a class="nav-link dropdown-toggle user-dropdown" href="#" id="userDropdown"
                               role="button" data-bs-toggle="dropdown">
                                <i class="fas fa-user-circle"></i>
                                <span>${pageContext.request.userPrincipal.name}</span>
                            </a>
                            <ul class="dropdown-menu dropdown-menu-end">
                                <li>
                                    <a href="${pageContext.request.contextPath}/logout" class="dropdown-item">
                                        <i class="fas fa-sign-out-alt"></i>
                                        Đăng xuất
                                    </a>
                                </li>
                            </ul>
                        </li>
                    </c:when>
                    <c:otherwise>
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/login">
                                <i class="fas fa-sign-in-alt"></i>
                                <span>Đăng nhập</span>
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/register">
                                <i class="fas fa-user-plus"></i>
                                <span>Đăng ký</span>
                            </a>
                        </li>
                    </c:otherwise>
                </c:choose>
            </ul>
        </div>
    </div>
</nav>

<div class="container mt-4">
    <!-- Alert Messages with Animation -->
    <c:if test="${successMessage != null || errorMessage != null}">
        <div class="alert-container">
            <c:if test="${successMessage != null}">
                <div class="alert alert-success alert-dismissible fade show">
                    <i class="fas fa-check-circle"></i>
                    <div>
                        <strong>Thành công!</strong><br>
                        ${successMessage}
                    </div>
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
            </c:if>
            <c:if test="${errorMessage != null}">
                <div class="alert alert-danger alert-dismissible fade show">
                    <i class="fas fa-exclamation-circle"></i>
                    <div>
                        <strong>Lỗi!</strong><br>
                        ${errorMessage}
                    </div>
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
            </c:if>
        </div>
    </c:if>

    <!-- Auto hide alerts after 5 seconds -->
    <script>
        document.addEventListener('DOMContentLoaded', function() {
            const alerts = document.querySelectorAll('.alert');
            alerts.forEach(alert => {
                setTimeout(() => {
                    const bsAlert = new bootstrap.Alert(alert);
                    bsAlert.close();
                }, 5000);
            });
        });
    </script>

<!-- Main Content Wrapper for Sticky Footer -->
<div id="main-content" class="container my-4">
