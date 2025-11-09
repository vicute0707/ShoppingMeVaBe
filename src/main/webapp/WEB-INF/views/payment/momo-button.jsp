<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!-- MoMo Payment Button Fragment -->
<!-- Cửa Hàng Mẹ và Bé - Shop Baby & Mom Cute 🍼👶 -->

<style>
    .momo-payment-section {
        margin: 20px 0;
        padding: 20px;
        background: linear-gradient(135deg, #a0006d 0%, #d5007f 100%);
        border-radius: 15px;
        box-shadow: 0 4px 15px rgba(160, 0, 109, 0.3);
    }

    .momo-payment-section h4 {
        color: white;
        font-size: 1.3em;
        margin-bottom: 15px;
        display: flex;
        align-items: center;
        gap: 10px;
    }

    .momo-payment-section .baby-icon {
        font-size: 1.5em;
    }

    .momo-btn {
        display: inline-flex;
        align-items: center;
        gap: 12px;
        background: white;
        color: #a0006d;
        border: 3px solid #fff;
        padding: 15px 30px;
        border-radius: 50px;
        font-size: 1.1em;
        font-weight: bold;
        cursor: pointer;
        transition: all 0.3s ease;
        box-shadow: 0 4px 10px rgba(0, 0, 0, 0.2);
        text-decoration: none;
    }

    .momo-btn:hover {
        transform: translateY(-3px);
        box-shadow: 0 6px 20px rgba(0, 0, 0, 0.3);
        background: #fff5f8;
    }

    .momo-btn img {
        width: 40px;
        height: 40px;
    }

    .payment-info {
        background: rgba(255, 255, 255, 0.9);
        padding: 15px;
        border-radius: 10px;
        margin-top: 15px;
    }

    .payment-info .info-row {
        display: flex;
        justify-content: space-between;
        margin: 8px 0;
        color: #333;
    }

    .payment-info .info-label {
        font-weight: 600;
        color: #666;
    }

    .payment-info .info-value {
        font-weight: bold;
        color: #a0006d;
    }

    .payment-info .total-amount {
        font-size: 1.3em;
        border-top: 2px solid #a0006d;
        padding-top: 10px;
        margin-top: 10px;
    }

    .cute-note {
        text-align: center;
        color: white;
        font-size: 0.9em;
        margin-top: 10px;
        font-style: italic;
    }

    .cute-note::before {
        content: "💝 ";
    }

    .cute-note::after {
        content: " 💝";
    }
</style>

<!-- MoMo Payment Section -->
<div class="momo-payment-section">
    <h4>
        <span class="baby-icon">🍼👶</span>
        Thanh Toán Qua MoMo
        <span class="baby-icon">💕</span>
    </h4>

    <div class="payment-info">
        <div class="info-row">
            <span class="info-label">Đơn hàng:</span>
            <span class="info-value">#${order.id}</span>
        </div>
        <div class="info-row">
            <span class="info-label">Ngày đặt:</span>
            <span class="info-value">
                <fmt:formatDate value="${order.orderDate}" pattern="dd/MM/yyyy HH:mm"/>
            </span>
        </div>
        <div class="info-row">
            <span class="info-label">Trạng thái:</span>
            <span class="info-value">
                <c:choose>
                    <c:when test="${order.status == 'PENDING'}">Chờ thanh toán</c:when>
                    <c:when test="${order.status == 'PROCESSING'}">Đang xử lý</c:when>
                    <c:when test="${order.status == 'SHIPPED'}">Đang giao hàng</c:when>
                    <c:when test="${order.status == 'DELIVERED'}">Đã giao hàng</c:when>
                    <c:when test="${order.status == 'CANCELLED'}">Đã hủy</c:when>
                </c:choose>
            </span>
        </div>
        <div class="info-row total-amount">
            <span class="info-label">Tổng tiền:</span>
            <span class="info-value">
                <fmt:formatNumber value="${order.totalAmount}" pattern="#,###"/> VNĐ
            </span>
        </div>
    </div>

    <div style="text-align: center; margin-top: 20px;">
        <c:choose>
            <c:when test="${order.status == 'PENDING'}">
                <a href="${pageContext.request.contextPath}/payment/momo/create/${order.id}" class="momo-btn">
                    <img src="data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iNDAiIGhlaWdodD0iNDAiIHZpZXdCb3g9IjAgMCA0MCA0MCIgZmlsbD0ibm9uZSIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj4KPGNpcmNsZSBjeD0iMjAiIGN5PSIyMCIgcj0iMjAiIGZpbGw9IiNBRTAwNkQiLz4KPHBhdGggZD0iTTEyIDIwQzEyIDE1LjU4MTcgMTUuNTgxNyAxMiAyMCAxMkMyNC40MTgzIDEyIDI4IDE1LjU4MTcgMjggMjBDMjggMjQuNDE4MyAyNC40MTgzIDI4IDIwIDI4QzE1LjU4MTcgMjggMTIgMjQuNDE4MyAxMiAyMFoiIGZpbGw9IndoaXRlIi8+CjxwYXRoIGQ9Ik0yMCAxNkMxNy43OTA5IDE2IDE2IDE3Ljc5MDkgMTYgMjBDMTYgMjIuMjA5MSAxNy43OTA5IDI0IDIwIDI0QzIyLjIwOTEgMjQgMjQgMjIuMjA5MSAyNCAyMEMyNCAxNy43OTA5IDIyLjIwOTEgMTYgMjAgMTZaIiBmaWxsPSIjQUUwMDZEIi8+Cjwvc3ZnPg=="
                         alt="MoMo"/>
                    <span>Thanh Toán Ngay với MoMo</span>
                </a>

                <p class="cute-note">
                    An toàn - Nhanh chóng - Tiện lợi cho mẹ và bé
                </p>
            </c:when>
            <c:when test="${order.paymentStatus == 'PAID'}">
                <div style="background: #4CAF50; color: white; padding: 15px; border-radius: 10px;">
                    <h5 style="margin: 0;">✅ Đã thanh toán thành công!</h5>
                    <p style="margin: 10px 0 0 0; font-size: 0.9em;">
                        Mã giao dịch: ${order.transactionId}
                    </p>
                </div>
            </c:when>
            <c:otherwise>
                <div style="background: #ff9800; color: white; padding: 15px; border-radius: 10px;">
                    <p style="margin: 0;">
                        Đơn hàng đang được xử lý
                    </p>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</div>

<!-- How to use:
Include this in your order detail page:
<jsp:include page="/WEB-INF/views/payment/momo-button.jsp"/>

Make sure you have the 'order' object in your model
-->
