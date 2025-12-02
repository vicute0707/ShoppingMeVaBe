<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="pageTitle" value="Thanh toán thành công - Shopping Store" scope="request"/>
<jsp:include page="../common/header.jsp"/>

<div class="container mt-5">
    <div class="row justify-content-center">
        <div class="col-md-8">
            <c:choose>
                <c:when test="${success}">
                    <!-- Success Card -->
                    <div class="card shadow-lg border-0">
                        <div class="card-body text-center p-5">
                            <!-- Success Icon -->
                            <div class="mb-4">
                                <i class="fas fa-check-circle text-success" style="font-size: 80px;"></i>
                            </div>

                            <!-- Success Message -->
                            <h2 class="text-success mb-3">Thanh toán thành công!</h2>
                            <p class="lead mb-4">Đơn hàng của bạn đã được thanh toán và đang được xử lý.</p>

                            <!-- Order Information -->
                            <div class="alert alert-success mb-4" role="alert">
                                <div class="row">
                                    <div class="col-md-6 text-start">
                                        <p class="mb-2"><strong>Mã đơn hàng:</strong> #${orderId}</p>
                                        <p class="mb-2"><strong>Số tiền:</strong>
                                            <fmt:formatNumber value="${amount}" type="number" groupingUsed="true" maxFractionDigits="0"/>₫
                                        </p>
                                    </div>
                                    <div class="col-md-6 text-start">
                                        <p class="mb-2"><strong>Phương thức:</strong>
                                            <span class="badge bg-success">
                                                <i class="fas fa-wallet"></i> MoMo
                                            </span>
                                        </p>
                                        <c:if test="${transactionId != null}">
                                            <p class="mb-2"><strong>Mã GD:</strong> <code>${transactionId}</code></p>
                                        </c:if>
                                    </div>
                                </div>
                            </div>

                            <!-- Additional Info -->
                            <div class="alert alert-info mb-4" role="alert">
                                <i class="fas fa-info-circle"></i>
                                Chúng tôi sẽ gửi email xác nhận và cập nhật tình trạng đơn hàng cho bạn.
                            </div>

                            <!-- Action Buttons -->
                            <div class="d-grid gap-2 d-md-flex justify-content-md-center">
                                <a href="${pageContext.request.contextPath}/auth/login?returnUrl=/checkout/orders/${orderId}"
                                   class="btn btn-primary btn-lg px-4">
                                    <i class="fas fa-sign-in-alt"></i> Đăng nhập để xem chi tiết
                                </a>
                                <a href="${pageContext.request.contextPath}/"
                                   class="btn btn-outline-secondary btn-lg px-4">
                                    <i class="fas fa-home"></i> Về trang chủ
                                </a>
                            </div>

                            <!-- Next Steps -->
                            <div class="mt-5 text-start">
                                <h5 class="mb-3"><i class="fas fa-list-check"></i> Các bước tiếp theo:</h5>
                                <ol class="list-group list-group-numbered">
                                    <li class="list-group-item d-flex align-items-start">
                                        <div class="ms-2 me-auto">
                                            <div class="fw-bold">Xác nhận thanh toán</div>
                                            Chúng tôi đã nhận được thanh toán của bạn
                                        </div>
                                        <span class="badge bg-success rounded-pill">✓</span>
                                    </li>
                                    <li class="list-group-item d-flex align-items-start">
                                        <div class="ms-2 me-auto">
                                            <div class="fw-bold">Chuẩn bị hàng</div>
                                            Đơn hàng đang được chuẩn bị
                                        </div>
                                        <span class="badge bg-warning rounded-pill">⏳</span>
                                    </li>
                                    <li class="list-group-item d-flex align-items-start">
                                        <div class="ms-2 me-auto">
                                            <div class="fw-bold">Giao hàng</div>
                                            Đơn hàng sẽ được giao trong 2-3 ngày
                                        </div>
                                        <span class="badge bg-secondary rounded-pill">⏰</span>
                                    </li>
                                </ol>
                            </div>
                        </div>
                    </div>
                </c:when>

                <c:otherwise>
                    <!-- Failed Card -->
                    <div class="card shadow-lg border-0">
                        <div class="card-body text-center p-5">
                            <!-- Error Icon -->
                            <div class="mb-4">
                                <i class="fas fa-times-circle text-danger" style="font-size: 80px;"></i>
                            </div>

                            <!-- Error Message -->
                            <h2 class="text-danger mb-3">Thanh toán thất bại!</h2>
                            <p class="lead mb-4">${message != null ? message : 'Có lỗi xảy ra trong quá trình thanh toán.'}</p>

                            <!-- Order Information -->
                            <c:if test="${orderId != null}">
                                <div class="alert alert-warning mb-4" role="alert">
                                    <p class="mb-0"><strong>Mã đơn hàng:</strong> #${orderId}</p>
                                    <p class="mb-0 mt-2">Đơn hàng vẫn được giữ và chờ thanh toán.</p>
                                </div>
                            </c:if>

                            <!-- Reasons -->
                            <div class="alert alert-info text-start mb-4" role="alert">
                                <h6 class="alert-heading"><i class="fas fa-question-circle"></i> Nguyên nhân có thể:</h6>
                                <ul class="mb-0">
                                    <li>Số dư tài khoản không đủ</li>
                                    <li>Thông tin thanh toán không chính xác</li>
                                    <li>Hủy giao dịch trong quá trình thanh toán</li>
                                    <li>Hết thời gian thanh toán</li>
                                </ul>
                            </div>

                            <!-- Action Buttons -->
                            <div class="d-grid gap-2 d-md-flex justify-content-md-center">
                                <c:if test="${orderId != null}">
                                    <a href="${pageContext.request.contextPath}/auth/login?returnUrl=/checkout/orders/${orderId}"
                                       class="btn btn-warning btn-lg px-4">
                                        <i class="fas fa-redo"></i> Thử lại thanh toán
                                    </a>
                                </c:if>
                                <a href="${pageContext.request.contextPath}/"
                                   class="btn btn-outline-secondary btn-lg px-4">
                                    <i class="fas fa-home"></i> Về trang chủ
                                </a>
                            </div>

                            <!-- Support Info -->
                            <div class="mt-5">
                                <p class="text-muted">
                                    <i class="fas fa-headset"></i>
                                    Cần hỗ trợ? Liên hệ: <a href="mailto:support@shopmevabe.com">support@shopmevabe.com</a>
                                </p>
                            </div>
                        </div>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>

<style>
    .card {
        border-radius: 15px;
    }

    .list-group-item {
        border: none;
        border-bottom: 1px solid rgba(0,0,0,.125);
    }

    .list-group-item:last-child {
        border-bottom: none;
    }

    @keyframes bounce {
        0%, 20%, 50%, 80%, 100% {
            transform: translateY(0);
        }
        40% {
            transform: translateY(-20px);
        }
        60% {
            transform: translateY(-10px);
        }
    }

    .fa-check-circle, .fa-times-circle {
        animation: bounce 2s;
    }
</style>

<jsp:include page="../common/footer.jsp"/>
