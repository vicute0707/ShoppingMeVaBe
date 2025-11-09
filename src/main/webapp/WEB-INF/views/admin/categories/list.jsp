<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="pageTitle" value="Manage Categories - Admin" scope="request"/>
<jsp:include page="../../common/header.jsp"/>

<div class="d-flex justify-content-between align-items-center mb-4">
    <h2>Manage Categories</h2>
    <a href="${pageContext.request.contextPath}/admin/categories/new" class="btn btn-primary">
        <i class="fas fa-plus"></i> Add New Category
    </a>
</div>

<c:choose>
    <c:when test="${categories.size() > 0}">
        <div class="table-responsive">
            <table class="table table-striped">
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Description</th>
                    <th>Status</th>
                    <th>Created At</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${categories}" var="category">
                    <tr>
                        <td>${category.id}</td>
                        <td>${category.name}</td>
                        <td>${category.description}</td>
                        <td>
                            <c:choose>
                                <c:when test="${category.active}">
                                    <span class="badge bg-success">Active</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="badge bg-secondary">Inactive</span>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            ${category.createdAt.dayOfMonth}/${category.createdAt.monthValue}/${category.createdAt.year}
                        </td>
                        <td>
                            <a href="${pageContext.request.contextPath}/admin/categories/${category.id}/edit"
                               class="btn btn-warning btn-sm">
                                <i class="fas fa-edit"></i> Edit
                            </a>
                            <form action="${pageContext.request.contextPath}/admin/categories/${category.id}/delete"
                                  method="post" class="d-inline" onsubmit="return confirm('Are you sure?')">
                                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                <button type="submit" class="btn btn-danger btn-sm">
                                    <i class="fas fa-trash"></i> Delete
                                </button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </c:when>
    <c:otherwise>
        <div class="alert alert-info">
            No categories found. <a href="${pageContext.request.contextPath}/admin/categories/new">Create one</a>
        </div>
    </c:otherwise>
</c:choose>

<jsp:include page="../../common/footer.jsp"/>
