<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<c:set var="pageTitle" value="${category.id != null ? 'Edit Category' : 'New Category'} - Admin" scope="request"/>
<jsp:include page="../../common/header.jsp"/>

<div class="row justify-content-center">
    <div class="col-md-8">
        <div class="card">
            <div class="card-header">
                <h3>${category.id != null ? 'Edit Category' : 'New Category'}</h3>
            </div>
            <div class="card-body">
                <form:form action="${category.id != null ? pageContext.request.contextPath.concat('/admin/categories/').concat(category.id) : pageContext.request.contextPath.concat('/admin/categories')}"
                           method="post" modelAttribute="category" id="categoryForm">
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

                    <div class="mb-3">
                        <label for="name" class="form-label">Category Name *</label>
                        <form:input path="name" class="form-control" id="name"
                                    required="required" minlength="2" maxlength="100"/>
                        <form:errors path="name" cssClass="text-danger"/>
                        <div class="invalid-feedback">Category name must be between 2 and 100 characters.</div>
                    </div>

                    <div class="mb-3">
                        <label for="description" class="form-label">Description</label>
                        <form:textarea path="description" class="form-control" id="description"
                                       rows="3" maxlength="500"/>
                        <form:errors path="description" cssClass="text-danger"/>
                    </div>

                    <div class="mb-3 form-check">
                        <form:checkbox path="active" class="form-check-input" id="active"/>
                        <label class="form-check-label" for="active">Active</label>
                    </div>

                    <div class="d-grid gap-2">
                        <button type="submit" class="btn btn-primary">
                            <i class="fas fa-save"></i> Save
                        </button>
                        <a href="${pageContext.request.contextPath}/admin/categories" class="btn btn-secondary">
                            <i class="fas fa-arrow-left"></i> Back to List
                        </a>
                    </div>
                </form:form>
            </div>
        </div>
    </div>
</div>

<jsp:include page="../../common/footer.jsp"/>
