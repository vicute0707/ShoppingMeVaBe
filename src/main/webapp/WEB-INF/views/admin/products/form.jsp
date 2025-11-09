<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<c:set var="pageTitle" value="${product.id != null ? 'Edit Product' : 'New Product'} - Admin" scope="request"/>
<jsp:include page="../../common/header.jsp"/>

<div class="row justify-content-center">
    <div class="col-md-8">
        <div class="card">
            <div class="card-header">
                <h3>${product.id != null ? 'Edit Product' : 'New Product'}</h3>
            </div>
            <div class="card-body">
                <form:form action="${product.id != null ? pageContext.request.contextPath.concat('/admin/products/').concat(product.id) : pageContext.request.contextPath.concat('/admin/products')}"
                           method="post" modelAttribute="product" id="productForm">
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

                    <div class="mb-3">
                        <label for="name" class="form-label">Product Name *</label>
                        <form:input path="name" class="form-control" id="name"
                                    required="required" minlength="2" maxlength="200"/>
                        <form:errors path="name" cssClass="text-danger"/>
                        <div class="invalid-feedback">Product name must be between 2 and 200 characters.</div>
                    </div>

                    <div class="mb-3">
                        <label for="category" class="form-label">Category *</label>
                        <form:select path="category.id" class="form-select" id="category" required="required">
                            <option value="">Select Category</option>
                            <c:forEach items="${categories}" var="cat">
                                <option value="${cat.id}"
                                        <c:if test="${product.category != null && product.category.id == cat.id}">selected</c:if>>
                                        ${cat.name}
                                </option>
                            </c:forEach>
                        </form:select>
                        <form:errors path="category" cssClass="text-danger"/>
                    </div>

                    <div class="mb-3">
                        <label for="description" class="form-label">Description</label>
                        <form:textarea path="description" class="form-control" id="description"
                                       rows="4" maxlength="2000"/>
                        <form:errors path="description" cssClass="text-danger"/>
                    </div>

                    <div class="row">
                        <div class="col-md-6 mb-3">
                            <label for="price" class="form-label">Price *</label>
                            <form:input path="price" type="number" step="0.01" class="form-control" id="price"
                                        required="required" min="0.01"/>
                            <form:errors path="price" cssClass="text-danger"/>
                            <div class="invalid-feedback">Price must be greater than 0.</div>
                        </div>

                        <div class="col-md-6 mb-3">
                            <label for="stockQuantity" class="form-label">Stock Quantity *</label>
                            <form:input path="stockQuantity" type="number" class="form-control" id="stockQuantity"
                                        required="required" min="0"/>
                            <form:errors path="stockQuantity" cssClass="text-danger"/>
                        </div>
                    </div>

                    <div class="mb-3">
                        <label for="imageUrl" class="form-label">Image URL</label>
                        <form:input path="imageUrl" class="form-control" id="imageUrl" maxlength="500"/>
                        <form:errors path="imageUrl" cssClass="text-danger"/>
                        <small class="text-muted">Enter a valid image URL</small>
                    </div>

                    <div class="mb-3 form-check">
                        <form:checkbox path="active" class="form-check-input" id="active"/>
                        <label class="form-check-label" for="active">Active</label>
                    </div>

                    <div class="d-grid gap-2">
                        <button type="submit" class="btn btn-primary">
                            <i class="fas fa-save"></i> Save
                        </button>
                        <a href="${pageContext.request.contextPath}/admin/products" class="btn btn-secondary">
                            <i class="fas fa-arrow-left"></i> Back to List
                        </a>
                    </div>
                </form:form>
            </div>
        </div>
    </div>
</div>

<jsp:include page="../../common/footer.jsp"/>
