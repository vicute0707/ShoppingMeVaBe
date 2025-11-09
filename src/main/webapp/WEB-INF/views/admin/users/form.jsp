<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<c:set var="pageTitle" value="Edit User - Admin" scope="request"/>
<jsp:include page="../../common/header.jsp"/>

<div class="row justify-content-center">
    <div class="col-md-8">
        <div class="card">
            <div class="card-header">
                <h3>Edit User</h3>
            </div>
            <div class="card-body">
                <form:form action="${pageContext.request.contextPath}/admin/users/${user.id}"
                           method="post" modelAttribute="user" id="userForm">
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

                    <div class="mb-3">
                        <label for="fullName" class="form-label">Full Name *</label>
                        <form:input path="fullName" class="form-control" id="fullName"
                                    required="required" minlength="3" maxlength="100"/>
                        <form:errors path="fullName" cssClass="text-danger"/>
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

                    <div class="mb-3">
                        <label for="role" class="form-label">Role</label>
                        <form:select path="role" class="form-select" id="role">
                            <option value="CUSTOMER">Customer</option>
                            <option value="ADMIN">Admin</option>
                        </form:select>
                    </div>

                    <div class="d-grid gap-2">
                        <button type="submit" class="btn btn-primary">
                            <i class="fas fa-save"></i> Save
                        </button>
                        <a href="${pageContext.request.contextPath}/admin/users" class="btn btn-secondary">
                            <i class="fas fa-arrow-left"></i> Back to List
                        </a>
                    </div>
                </form:form>
            </div>
        </div>
    </div>
</div>

<jsp:include page="../../common/footer.jsp"/>
