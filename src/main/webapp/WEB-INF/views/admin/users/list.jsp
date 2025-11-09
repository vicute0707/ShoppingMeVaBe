<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="pageTitle" value="Manage Users - Admin" scope="request"/>
<jsp:include page="../../common/header.jsp"/>

<h2 class="mb-4">Manage Users</h2>

<c:choose>
    <c:when test="${users.size() > 0}">
        <div class="table-responsive">
            <table class="table table-striped">
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Full Name</th>
                    <th>Email</th>
                    <th>Phone</th>
                    <th>Role</th>
                    <th>Status</th>
                    <th>Created At</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${users}" var="user">
                    <tr>
                        <td>${user.id}</td>
                        <td>${user.fullName}</td>
                        <td>${user.email}</td>
                        <td>${user.phone}</td>
                        <td>
                            <c:choose>
                                <c:when test="${user.role == 'ADMIN'}">
                                    <span class="badge bg-danger">Admin</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="badge bg-primary">Customer</span>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${user.enabled}">
                                    <span class="badge bg-success">Active</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="badge bg-secondary">Inactive</span>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            ${user.createdAt.dayOfMonth}/${user.createdAt.monthValue}/${user.createdAt.year}
                        </td>
                        <td>
                            <a href="${pageContext.request.contextPath}/admin/users/${user.id}"
                               class="btn btn-info btn-sm">
                                <i class="fas fa-eye"></i>
                            </a>
                            <a href="${pageContext.request.contextPath}/admin/users/${user.id}/edit"
                               class="btn btn-warning btn-sm">
                                <i class="fas fa-edit"></i>
                            </a>
                            <c:if test="${user.role != 'ADMIN'}">
                                <form action="${pageContext.request.contextPath}/admin/users/${user.id}/delete"
                                      method="post" class="d-inline" onsubmit="return confirm('Are you sure?')">
                                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                    <button type="submit" class="btn btn-danger btn-sm">
                                        <i class="fas fa-trash"></i>
                                    </button>
                                </form>
                            </c:if>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </c:when>
    <c:otherwise>
        <div class="alert alert-info">
            No users found.
        </div>
    </c:otherwise>
</c:choose>

<jsp:include page="../../common/footer.jsp"/>
