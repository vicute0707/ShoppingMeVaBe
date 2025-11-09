<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<c:set var="pageTitle" value="Register - Shopping Store" scope="request"/>
<jsp:include page="../common/header.jsp"/>

<div class="row justify-content-center">
    <div class="col-md-6">
        <div class="card">
            <div class="card-header">
                <h3>Đăng ký tài khoản</h3>
            </div>
            <div class="card-body">
                <form:form action="${pageContext.request.contextPath}/register" method="post"
                           modelAttribute="registerDTO" id="registerForm">
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

                    <div class="mb-3">
                        <label for="fullName" class="form-label">Họ và tên *</label>
                        <form:input path="fullName" class="form-control" id="fullName" required="required"
                                    minlength="3" maxlength="100" autofocus="autofocus"
                                    placeholder="Nhập họ và tên đầy đủ"/>
                        <form:errors path="fullName" cssClass="text-danger"/>
                        <div class="invalid-feedback">Họ tên phải từ 3-100 ký tự.</div>
                    </div>

                    <div class="mb-3">
                        <label for="email" class="form-label">Email *</label>
                        <form:input path="email" type="email" class="form-control" id="email" required="required"
                                    placeholder="Nhập email của bạn"/>
                        <form:errors path="email" cssClass="text-danger"/>
                        <div class="invalid-feedback">Vui lòng nhập email hợp lệ.</div>
                    </div>

                    <div class="mb-3">
                        <label for="password" class="form-label">Mật khẩu *</label>
                        <form:password path="password" class="form-control" id="password" required="required"
                                       minlength="6" placeholder="Tối thiểu 6 ký tự"/>
                        <form:errors path="password" cssClass="text-danger"/>
                        <div class="invalid-feedback">Mật khẩu phải có ít nhất 6 ký tự.</div>
                    </div>

                    <div class="mb-3">
                        <label for="confirmPassword" class="form-label">Xác nhận mật khẩu *</label>
                        <form:password path="confirmPassword" class="form-control" id="confirmPassword"
                                       required="required" minlength="6" placeholder="Nhập lại mật khẩu"/>
                        <form:errors path="confirmPassword" cssClass="text-danger"/>
                        <div class="invalid-feedback">Mật khẩu không khớp.</div>
                    </div>

                    <div class="mb-3">
                        <label for="phone" class="form-label">Số điện thoại</label>
                        <form:input path="phone" class="form-control" id="phone" maxlength="15"
                                    placeholder="VD: 0901234567 (không bắt buộc)"/>
                        <form:errors path="phone" cssClass="text-danger"/>
                    </div>

                    <div class="mb-3">
                        <label for="address" class="form-label">Địa chỉ</label>
                        <form:textarea path="address" class="form-control" id="address" rows="3" maxlength="255"
                                       placeholder="Địa chỉ giao hàng (không bắt buộc)"/>
                        <form:errors path="address" cssClass="text-danger"/>
                    </div>

                    <div class="d-grid gap-2">
                        <button type="submit" class="btn btn-primary btn-lg">
                            <i class="fas fa-user-plus"></i> Đăng ký
                        </button>
                    </div>
                </form:form>

                <hr>
                <p class="text-center">
                    Đã có tài khoản?
                    <a href="${pageContext.request.contextPath}/login" class="fw-bold">Đăng nhập ngay</a>
                </p>
            </div>
        </div>
    </div>
</div>

<jsp:include page="../common/footer.jsp"/>
