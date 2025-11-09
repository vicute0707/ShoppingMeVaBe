<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Access Denied - Shopping Store" scope="request"/>
<jsp:include page="header.jsp"/>

<div class="container mt-5">
    <div class="row justify-content-center">
        <div class="col-md-6 text-center">
            <i class="fas fa-exclamation-triangle fa-5x text-danger mb-4"></i>
            <h1 class="display-4">Access Denied</h1>
            <p class="lead">You don't have permission to access this resource.</p>
            <hr>
            <a href="${pageContext.request.contextPath}/" class="btn btn-primary">
                <i class="fas fa-home"></i> Go to Home
            </a>
        </div>
    </div>
</div>

<jsp:include page="footer.jsp"/>
