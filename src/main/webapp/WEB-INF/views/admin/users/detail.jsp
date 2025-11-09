<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="pageTitle" value="User Details - Admin" scope="request"/>
<jsp:include page="../../common/header.jsp"/>

<nav aria-label="breadcrumb">
    <ol class="breadcrumb">
        <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/admin/dashboard">Dashboard</a></li>
        <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/admin/users">Users</a></li>
        <li class="breadcrumb-item active">User #${user.id}</li>
    </ol>
</nav>

<div class="row">
    <div class="col-md-6">
        <div class="card">
            <div class="card-header">
                <h5>User Information</h5>
            </div>
            <div class="card-body">
                <p><strong>ID:</strong> ${user.id}</p>
                <p><strong>Full Name:</strong> ${user.fullName}</p>
                <p><strong>Email:</strong> ${user.email}</p>
                <p><strong>Phone:</strong> ${user.phone != null ? user.phone : 'N/A'}</p>
                <p><strong>Address:</strong> ${user.address != null ? user.address : 'N/A'}</p>
                <p><strong>Role:</strong>
                    <c:choose>
                        <c:when test="${user.role == 'ADMIN'}">
                            <span class="badge bg-danger">Admin</span>
                        </c:when>
                        <c:otherwise>
                            <span class="badge bg-primary">Customer</span>
                        </c:otherwise>
                    </c:choose>
                </p>
                <p><strong>Status:</strong>
                    <c:choose>
                        <c:when test="${user.enabled}">
                            <span class="badge bg-success">Active</span>
                        </c:when>
                        <c:otherwise>
                            <span class="badge bg-secondary">Inactive</span>
                        </c:otherwise>
                    </c:choose>
                </p>
                <p><strong>Created At:</strong>
                    ${user.createdAt.dayOfMonth}/${user.createdAt.monthValue}/${user.createdAt.year} ${user.createdAt.hour}:${user.createdAt.minute < 10 ? '0' : ''}${user.createdAt.minute}</p>
            </div>
            <div class="card-footer">
                <a href="${pageContext.request.contextPath}/admin/users/${user.id}/edit" class="btn btn-warning">
                    <i class="fas fa-edit"></i> Edit
                </a>
                <a href="${pageContext.request.contextPath}/admin/users" class="btn btn-secondary">
                    <i class="fas fa-arrow-left"></i> Back to List
                </a>
            </div>
        </div>
    </div>
</div>

<jsp:include page="../../common/footer.jsp"/>
