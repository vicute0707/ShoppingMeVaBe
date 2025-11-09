<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Login - Shopping Store" scope="request"/>
<jsp:include page="../common/header.jsp"/>

<div class="row justify-content-center">
    <div class="col-md-6">
        <div class="card">
            <div class="card-header">
                <h3>Đăng nhập</h3>
            </div>
            <div class="card-body">
                <%-- Display error message --%>
                <c:if test="${not empty errorMessage}">
                    <div class="alert alert-danger alert-dismissible fade show" role="alert">
                        ${errorMessage}
                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                    </div>
                </c:if>

                <%-- Display success message --%>
                <c:if test="${not empty successMessage}">
                    <div class="alert alert-success alert-dismissible fade show" role="alert">
                        ${successMessage}
                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                    </div>
                </c:if>

                <form action="${pageContext.request.contextPath}/perform-login" method="post" id="loginForm">
                    <div class="mb-3">
                        <label for="email" class="form-label">Email *</label>
                        <input type="email" class="form-control" id="email" name="username"
                               required autofocus placeholder="Nhập email của bạn">
                        <div class="invalid-feedback">Vui lòng nhập email hợp lệ.</div>
                    </div>

                    <div class="mb-3">
                        <label for="password" class="form-label">Mật khẩu *</label>
                        <input type="password" class="form-control" id="password" name="password"
                               required minlength="6" placeholder="Nhập mật khẩu (tối thiểu 6 ký tự)">
                        <div class="invalid-feedback">Mật khẩu phải có ít nhất 6 ký tự.</div>
                    </div>

                    <div class="d-grid gap-2">
                        <button type="submit" class="btn btn-primary btn-lg">
                            <i class="fas fa-sign-in-alt"></i> Đăng nhập
                        </button>
                    </div>
                </form>

                <hr>
                <p class="text-center">
                    Chưa có tài khoản?
                    <a href="${pageContext.request.contextPath}/register" class="fw-bold">Đăng ký ngay</a>
                </p>
                <p class="text-center text-muted small">
                    <strong>Tài khoản Admin mẫu:</strong><br>
                    Email: admin@shopmevabe.com | Password: admin123
                </p>
            </div>
        </div>
    </div>
</div>

<jsp:include page="../common/footer.jsp"/>
