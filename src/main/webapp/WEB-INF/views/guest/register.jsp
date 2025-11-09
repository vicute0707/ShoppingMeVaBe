<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<c:set var="pageTitle" value="Register - Shopping Store" scope="request"/>
<jsp:include page="../common/header.jsp"/>

<div class="row justify-content-center">
    <div class="col-md-6">
        <div class="card">
            <div class="card-header">
                <h3>Register</h3>
            </div>
            <div class="card-body">
                <form:form action="${pageContext.request.contextPath}/register" method="post"
                           modelAttribute="registerDTO" id="registerForm">
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

                    <div class="mb-3">
                        <label for="fullName" class="form-label">Full Name *</label>
                        <form:input path="fullName" class="form-control" id="fullName" required="required"
                                    minlength="3" maxlength="100"/>
                        <form:errors path="fullName" cssClass="text-danger"/>
                        <div class="invalid-feedback">Full name must be between 3 and 100 characters.</div>
                    </div>

                    <div class="mb-3">
                        <label for="email" class="form-label">Email *</label>
                        <form:input path="email" type="email" class="form-control" id="email" required="required"/>
                        <form:errors path="email" cssClass="text-danger"/>
                        <div class="invalid-feedback">Please enter a valid email.</div>
                    </div>

                    <div class="mb-3">
                        <label for="password" class="form-label">Password *</label>
                        <form:password path="password" class="form-control" id="password" required="required"
                                       minlength="6"/>
                        <form:errors path="password" cssClass="text-danger"/>
                        <div class="invalid-feedback">Password must be at least 6 characters.</div>
                    </div>

                    <div class="mb-3">
                        <label for="confirmPassword" class="form-label">Confirm Password *</label>
                        <form:password path="confirmPassword" class="form-control" id="confirmPassword"
                                       required="required" minlength="6"/>
                        <form:errors path="confirmPassword" cssClass="text-danger"/>
                        <div class="invalid-feedback">Passwords must match.</div>
                    </div>

                    <div class="mb-3">
                        <label for="phone" class="form-label">Phone</label>
                        <form:input path="phone" class="form-control" id="phone" maxlength="15"/>
                        <form:errors path="phone" cssClass="text-danger"/>
                    </div>

                    <div class="mb-3">
                        <label for="address" class="form-label">Address</label>
                        <form:textarea path="address" class="form-control" id="address" rows="3" maxlength="255"/>
                        <form:errors path="address" cssClass="text-danger"/>
                    </div>

                    <div class="d-grid gap-2">
                        <button type="submit" class="btn btn-primary">
                            <i class="fas fa-user-plus"></i> Register
                        </button>
                    </div>
                </form:form>

                <hr>
                <p class="text-center">
                    Already have an account?
                    <a href="${pageContext.request.contextPath}/login">Login here</a>
                </p>
            </div>
        </div>
    </div>
</div>

<jsp:include page="../common/footer.jsp"/>
