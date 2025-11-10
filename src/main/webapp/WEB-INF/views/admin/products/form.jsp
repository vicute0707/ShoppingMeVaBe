<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<c:set var="pageTitle" value="${product.id != null ? 'Edit Product' : 'New Product'} - Admin" scope="request"/>
<jsp:include page="../../common/header.jsp"/>

<style>
    /* Pastel cute colors */
    :root {
        --pastel-pink: #FFD1DC;
        --pastel-purple: #E0BBE4;
        --pastel-blue: #A7C7E7;
        --pastel-yellow: #FFF9A5;
        --pastel-green: #B5EAD7;
        --soft-white: #FFFBF5;
        --text-dark: #5A4A6F;
    }

    /* Cute font */
    .cute-font {
        font-family: 'Comic Sans MS', 'Segoe UI Emoji', 'Apple Color Emoji', cursive, sans-serif;
    }

    .product-form-container {
        background: linear-gradient(135deg, var(--pastel-pink) 0%, var(--pastel-purple) 50%, var(--pastel-blue) 100%);
        padding: 30px;
        border-radius: 25px;
        box-shadow: 0 10px 30px rgba(0,0,0,0.1);
        margin-bottom: 30px;
    }

    .form-card {
        background: var(--soft-white);
        border-radius: 20px;
        padding: 30px;
        box-shadow: 0 8px 20px rgba(0,0,0,0.08);
        border: 3px solid var(--pastel-purple);
    }

    .form-card h3 {
        color: var(--text-dark);
        font-weight: 700;
        margin-bottom: 25px;
        text-align: center;
        font-size: 28px;
    }

    .form-label {
        color: var(--text-dark);
        font-weight: 600;
        margin-bottom: 8px;
        font-size: 14px;
    }

    .form-control, .form-select {
        border: 2px solid var(--pastel-purple);
        border-radius: 15px;
        padding: 12px 18px;
        background: white;
        transition: all 0.3s ease;
    }

    .form-control:focus, .form-select:focus {
        border-color: var(--pastel-pink);
        box-shadow: 0 0 0 0.2rem rgba(255, 209, 220, 0.3);
        background: var(--soft-white);
    }

    /* Image upload area */
    .image-upload-area {
        border: 3px dashed var(--pastel-purple);
        border-radius: 20px;
        padding: 30px;
        text-align: center;
        background: linear-gradient(135deg, var(--soft-white) 0%, #FFF5F8 100%);
        transition: all 0.3s ease;
        cursor: pointer;
    }

    .image-upload-area:hover {
        border-color: var(--pastel-pink);
        background: #FFF0F5;
        transform: translateY(-2px);
    }

    .image-upload-area label {
        cursor: pointer;
        display: block;
        width: 100%;
    }

    .upload-icon {
        font-size: 48px;
        color: var(--pastel-purple);
        margin-bottom: 10px;
    }

    #imagePreview {
        margin-top: 20px;
        border-radius: 15px;
        overflow: hidden;
        box-shadow: 0 8px 20px rgba(0,0,0,0.1);
        display: none;
    }

    #imagePreview img {
        max-width: 100%;
        height: auto;
        border-radius: 15px;
    }

    /* Buttons */
    .btn-cute-primary {
        background: linear-gradient(135deg, var(--pastel-pink) 0%, var(--pastel-purple) 100%);
        border: none;
        color: white;
        padding: 15px 40px;
        border-radius: 25px;
        font-weight: 700;
        font-size: 16px;
        transition: all 0.3s ease;
        box-shadow: 0 5px 15px rgba(224, 187, 228, 0.4);
    }

    .btn-cute-primary:hover {
        transform: translateY(-3px);
        box-shadow: 0 8px 25px rgba(224, 187, 228, 0.6);
        background: linear-gradient(135deg, var(--pastel-purple) 0%, var(--pastel-pink) 100%);
    }

    .btn-cute-secondary {
        background: var(--pastel-blue);
        border: none;
        color: var(--text-dark);
        padding: 15px 40px;
        border-radius: 25px;
        font-weight: 600;
        font-size: 16px;
        transition: all 0.3s ease;
    }

    .btn-cute-secondary:hover {
        background: var(--pastel-purple);
        color: white;
        transform: translateY(-3px);
    }

    .form-check-input:checked {
        background-color: var(--pastel-pink);
        border-color: var(--pastel-pink);
    }

    .text-danger {
        color: #ff6b9d !important;
        font-size: 13px;
        margin-top: 5px;
    }

    .text-muted {
        color: var(--text-dark) !important;
        opacity: 0.7;
        font-size: 12px;
    }
</style>

<div class="row justify-content-center">
    <div class="col-md-10">
        <div class="product-form-container cute-font">
            <div class="form-card">
                <h3>
                    ${product.id != null ? '<i class="fas fa-edit"></i> Chỉnh sửa sản phẩm' : '<i class="fas fa-plus-circle"></i> Thêm sản phẩm mới'}
                </h3>

                <form:form action="${product.id != null ? pageContext.request.contextPath.concat('/admin/products/').concat(product.id) : pageContext.request.contextPath.concat('/admin/products')}"
                           method="post" modelAttribute="product" enctype="multipart/form-data" id="productForm">
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

                    <div class="row">
                        <div class="col-md-6 mb-3">
                            <label for="name" class="form-label"><i class="fas fa-tag"></i> Tên sản phẩm *</label>
                            <form:input path="name" class="form-control" id="name"
                                        required="required" minlength="2" maxlength="200"
                                        placeholder="Nhập tên sản phẩm đáng yêu..."/>
                            <form:errors path="name" cssClass="text-danger"/>
                        </div>

                        <div class="col-md-6 mb-3">
                            <label for="category" class="form-label"><i class="fas fa-folder"></i> Danh mục *</label>
                            <form:select path="category.id" class="form-select" id="category" required="required">
                                <option value="">Chọn danh mục...</option>
                                <c:forEach items="${categories}" var="cat">
                                    <option value="${cat.id}"
                                            <c:if test="${product.category != null && product.category.id == cat.id}">selected</c:if>>
                                            ${cat.name}
                                    </option>
                                </c:forEach>
                            </form:select>
                            <form:errors path="category" cssClass="text-danger"/>
                        </div>
                    </div>

                    <div class="mb-3">
                        <label for="description" class="form-label"><i class="fas fa-align-left"></i> Mô tả</label>
                        <form:textarea path="description" class="form-control" id="description"
                                       rows="4" maxlength="2000"
                                       placeholder="Mô tả chi tiết về sản phẩm..."/>
                        <form:errors path="description" cssClass="text-danger"/>
                    </div>

                    <div class="row">
                        <div class="col-md-6 mb-3">
                            <label for="price" class="form-label"><i class="fas fa-dollar-sign"></i> Giá *</label>
                            <form:input path="price" type="number" step="0.01" class="form-control" id="price"
                                        required="required" min="0.01" placeholder="0.00"/>
                            <form:errors path="price" cssClass="text-danger"/>
                        </div>

                        <div class="col-md-6 mb-3">
                            <label for="stockQuantity" class="form-label"><i class="fas fa-box"></i> Số lượng *</label>
                            <form:input path="stockQuantity" type="number" class="form-control" id="stockQuantity"
                                        required="required" min="0" placeholder="0"/>
                            <form:errors path="stockQuantity" cssClass="text-danger"/>
                        </div>
                    </div>

                    <div class="mb-4">
                        <label class="form-label"><i class="fas fa-image"></i> Hình ảnh sản phẩm</label>
                        <div class="image-upload-area" onclick="event.stopPropagation(); document.getElementById('imageFile').click();">
                            <div class="upload-content">
                                <div class="upload-icon"><i class="fas fa-camera"></i></div>
                                <p style="margin: 0; color: var(--text-dark); font-weight: 600;">
                                    Nhấn để chọn ảnh từ máy tính
                                </p>
                                <small class="text-muted">Chấp nhận: JPG, PNG, GIF (Tối đa 5MB)</small>
                            </div>
                        </div>
                        <input type="file" id="imageFile" name="imageFile"
                               accept="image/*" style="display: none;" onchange="previewImage(event)"/>

                        <div id="imagePreview">
                            <img id="previewImg" src="" alt="Preview"/>
                        </div>

                        <c:if test="${product.imageUrl != null}">
                            <div style="margin-top: 15px;">
                                <p style="margin-bottom: 10px; color: var(--text-dark);">
                                    <strong>Ảnh hiện tại:</strong>
                                </p>
                                <c:choose>
                                    <c:when test="${product.imageUrl.startsWith('http://') || product.imageUrl.startsWith('https://')}">
                                        <img src="${product.imageUrl}"
                                             alt="${product.name}"
                                             style="max-width: 300px; border-radius: 15px; box-shadow: 0 5px 15px rgba(0,0,0,0.1);"/>
                                    </c:when>
                                    <c:otherwise>
                                        <img src="${pageContext.request.contextPath}${product.imageUrl}"
                                             alt="${product.name}"
                                             style="max-width: 300px; border-radius: 15px; box-shadow: 0 5px 15px rgba(0,0,0,0.1);"/>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </c:if>

                        <div style="margin-top: 10px;">
                            <form:input path="imageUrl" type="text" class="form-control"
                                        placeholder="Hoặc nhập URL ảnh..." maxlength="500"/>
                            <form:errors path="imageUrl" cssClass="text-danger"/>
                        </div>
                    </div>

                    <div class="mb-4 form-check">
                        <form:checkbox path="active" class="form-check-input" id="active"/>
                        <label class="form-check-label" for="active" style="color: var(--text-dark); font-weight: 600;">
                            <i class="fas fa-check-circle"></i> Kích hoạt sản phẩm
                        </label>
                    </div>

                    <div class="row g-3">
                        <div class="col-md-6">
                            <button type="submit" class="btn btn-cute-primary w-100">
                                <i class="fas fa-save"></i> Lưu sản phẩm
                            </button>
                        </div>
                        <div class="col-md-6">
                            <a href="${pageContext.request.contextPath}/admin/products"
                               class="btn btn-cute-secondary w-100">
                                <i class="fas fa-arrow-left"></i> Quay lại
                            </a>
                        </div>
                    </div>
                </form:form>
            </div>
        </div>
    </div>
</div>

<script>
function previewImage(event) {
    const file = event.target.files[0];
    if (file) {
        const reader = new FileReader();
        reader.onload = function(e) {
            document.getElementById('previewImg').src = e.target.result;
            document.getElementById('imagePreview').style.display = 'block';
        };
        reader.readAsDataURL(file);
    }
}
</script>

<jsp:include page="../../common/footer.jsp"/>
