<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="pageTitle" value="Quản lý người dùng - Admin" scope="request"/>
<jsp:include page="../../common/header.jsp"/>

<style>
    .page-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 30px;
        padding: 20px;
        background: linear-gradient(135deg, var(--pastel-pink) 0%, var(--pastel-purple) 100%);
        border-radius: 20px;
        box-shadow: 0 8px 20px rgba(224, 187, 228, 0.3);
    }

    .page-header h2 {
        color: white;
        font-weight: 700;
        margin: 0;
        font-size: 1.8rem;
        display: flex;
        align-items: center;
        gap: 15px;
    }

    .page-header h2 i {
        font-size: 2rem;
    }

    .stats-container {
        display: grid;
        grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
        gap: 20px;
        margin-bottom: 30px;
    }

    .stat-card {
        background: white;
        border-radius: 20px;
        padding: 25px;
        box-shadow: 0 5px 15px rgba(0,0,0,0.08);
        border: 2px solid var(--pastel-purple);
        transition: all 0.3s ease;
    }

    .stat-card:hover {
        transform: translateY(-5px);
        box-shadow: 0 8px 25px rgba(224, 187, 228, 0.3);
    }

    .stat-card .stat-icon {
        font-size: 2.5rem;
        margin-bottom: 10px;
    }

    .stat-card .stat-value {
        font-size: 2rem;
        font-weight: 700;
        color: var(--text-dark);
    }

    .stat-card .stat-label {
        color: var(--text-medium);
        font-weight: 600;
        font-size: 0.95rem;
    }

    .users-grid {
        display: grid;
        grid-template-columns: repeat(auto-fill, minmax(350px, 1fr));
        gap: 25px;
        margin-top: 20px;
    }

    .user-card {
        background: white;
        border-radius: 20px;
        padding: 25px;
        box-shadow: 0 5px 15px rgba(0,0,0,0.08);
        border: 2px solid var(--pastel-blue);
        transition: all 0.3s ease;
        position: relative;
        overflow: hidden;
    }

    .user-card::before {
        content: '';
        position: absolute;
        top: 0;
        left: 0;
        right: 0;
        height: 5px;
        background: linear-gradient(90deg, var(--pastel-pink), var(--pastel-purple), var(--pastel-blue));
    }

    .user-card:hover {
        transform: translateY(-5px);
        box-shadow: 0 12px 30px rgba(167, 199, 231, 0.3);
    }

    .user-header {
        display: flex;
        align-items: center;
        gap: 15px;
        margin-bottom: 20px;
    }

    .user-avatar {
        width: 70px;
        height: 70px;
        border-radius: 50%;
        background: linear-gradient(135deg, var(--pastel-pink), var(--pastel-purple));
        display: flex;
        align-items: center;
        justify-content: center;
        color: white;
        font-size: 2rem;
        font-weight: 700;
        box-shadow: 0 5px 15px rgba(224, 187, 228, 0.3);
    }

    .user-info h4 {
        margin: 0;
        color: var(--text-dark);
        font-weight: 700;
        font-size: 1.2rem;
    }

    .user-info .user-email {
        color: var(--text-medium);
        font-size: 0.9rem;
        margin-top: 5px;
    }

    .user-details {
        background: var(--soft-white);
        border-radius: 15px;
        padding: 15px;
        margin-bottom: 15px;
    }

    .user-detail-item {
        display: flex;
        align-items: center;
        gap: 10px;
        margin-bottom: 10px;
        color: var(--text-dark);
        font-weight: 600;
    }

    .user-detail-item:last-child {
        margin-bottom: 0;
    }

    .user-detail-item i {
        width: 20px;
        color: var(--pastel-purple);
    }

    .user-badges {
        display: flex;
        gap: 10px;
        margin-bottom: 15px;
        flex-wrap: wrap;
    }

    .badge {
        padding: 8px 15px;
        border-radius: 20px;
        font-weight: 600;
        font-size: 0.85rem;
    }

    .user-actions {
        display: flex;
        gap: 10px;
    }

    .user-actions form {
        flex: 1;
    }

    .btn {
        border-radius: 15px;
        font-weight: 600;
        padding: 10px 20px;
        transition: all 0.3s ease;
    }

    .btn:hover {
        transform: translateY(-2px);
    }

    .no-users {
        text-align: center;
        padding: 60px 20px;
        background: white;
        border-radius: 20px;
        box-shadow: 0 5px 15px rgba(0,0,0,0.08);
        border: 2px dashed var(--pastel-purple);
    }

    .no-users-icon {
        font-size: 5rem;
        color: var(--pastel-purple);
        margin-bottom: 20px;
    }
</style>

<div class="page-header">
    <h2><i class="fas fa-users"></i> Quản lý người dùng</h2>
</div>

<div class="stats-container">
    <div class="stat-card">
        <div class="stat-icon" style="color: var(--pastel-blue);">
            <i class="fas fa-users"></i>
        </div>
        <div class="stat-value">${users.size()}</div>
        <div class="stat-label">Tổng người dùng</div>
    </div>

    <div class="stat-card">
        <div class="stat-icon" style="color: var(--pastel-green);">
            <i class="fas fa-user-check"></i>
        </div>
        <div class="stat-value">
            <c:set var="activeCount" value="0"/>
            <c:forEach items="${users}" var="user">
                <c:if test="${user.enabled}">
                    <c:set var="activeCount" value="${activeCount + 1}"/>
                </c:if>
            </c:forEach>
            ${activeCount}
        </div>
        <div class="stat-label">Đang hoạt động</div>
    </div>

    <div class="stat-card">
        <div class="stat-icon" style="color: var(--pastel-pink);">
            <i class="fas fa-user-shield"></i>
        </div>
        <div class="stat-value">
            <c:set var="adminCount" value="0"/>
            <c:forEach items="${users}" var="user">
                <c:if test="${user.role == 'ADMIN'}">
                    <c:set var="adminCount" value="${adminCount + 1}"/>
                </c:if>
            </c:forEach>
            ${adminCount}
        </div>
        <div class="stat-label">Quản trị viên</div>
    </div>

    <div class="stat-card">
        <div class="stat-icon" style="color: var(--pastel-yellow);">
            <i class="fas fa-shopping-cart"></i>
        </div>
        <div class="stat-value">
            ${users.size() - adminCount}
        </div>
        <div class="stat-label">Khách hàng</div>
    </div>
</div>

<c:choose>
    <c:when test="${users.size() > 0}">
        <div class="users-grid">
            <c:forEach items="${users}" var="user">
                <div class="user-card">
                    <div class="user-header">
                        <div class="user-avatar">
                            ${user.fullName.substring(0, 1).toUpperCase()}
                        </div>
                        <div class="user-info">
                            <h4>${user.fullName}</h4>
                            <div class="user-email">
                                <i class="fas fa-envelope"></i> ${user.email}
                            </div>
                        </div>
                    </div>

                    <div class="user-badges">
                        <c:choose>
                            <c:when test="${user.role == 'ADMIN'}">
                                <span class="badge bg-danger">
                                    <i class="fas fa-crown"></i> Admin
                                </span>
                            </c:when>
                            <c:otherwise>
                                <span class="badge bg-primary">
                                    <i class="fas fa-user"></i> Khách hàng
                                </span>
                            </c:otherwise>
                        </c:choose>

                        <c:choose>
                            <c:when test="${user.enabled}">
                                <span class="badge bg-success">
                                    <i class="fas fa-check-circle"></i> Hoạt động
                                </span>
                            </c:when>
                            <c:otherwise>
                                <span class="badge bg-secondary">
                                    <i class="fas fa-ban"></i> Vô hiệu
                                </span>
                            </c:otherwise>
                        </c:choose>
                    </div>

                    <div class="user-details">
                        <c:if test="${user.phone != null}">
                            <div class="user-detail-item">
                                <i class="fas fa-phone"></i>
                                <span>${user.phone}</span>
                            </div>
                        </c:if>
                        <c:if test="${user.address != null}">
                            <div class="user-detail-item">
                                <i class="fas fa-map-marker-alt"></i>
                                <span>${user.address}</span>
                            </div>
                        </c:if>
                        <div class="user-detail-item">
                            <i class="fas fa-calendar"></i>
                            <span>Tham gia: ${user.createdAt.dayOfMonth}/${user.createdAt.monthValue}/${user.createdAt.year}</span>
                        </div>
                    </div>

                    <div class="user-actions">
                        <a href="${pageContext.request.contextPath}/admin/users/${user.id}/edit"
                           class="btn btn-warning" style="flex: 1;">
                            <i class="fas fa-edit"></i> Sửa
                        </a>

                        <c:if test="${user.role != 'ADMIN'}">
                            <form action="${pageContext.request.contextPath}/admin/users/${user.id}/toggle-status"
                                  method="post" style="flex: 1;">
                                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                <button type="submit" class="btn ${user.enabled ? 'btn-secondary' : 'btn-success'} w-100">
                                    <i class="fas ${user.enabled ? 'fa-ban' : 'fa-check'}"></i>
                                    ${user.enabled ? 'Vô hiệu' : 'Kích hoạt'}
                                </button>
                            </form>

                            <form action="${pageContext.request.contextPath}/admin/users/${user.id}/delete"
                                  method="post" style="flex: 1;"
                                  onsubmit="return confirm('Bạn chắc chắn muốn xóa người dùng này?')">
                                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                <button type="submit" class="btn btn-danger w-100">
                                    <i class="fas fa-trash-alt"></i> Xóa
                                </button>
                            </form>
                        </c:if>
                    </div>
                </div>
            </c:forEach>
        </div>
    </c:when>
    <c:otherwise>
        <div class="no-users">
            <div class="no-users-icon"><i class="fas fa-users-slash"></i></div>
            <h3 style="color: var(--text-dark); margin-bottom: 15px;">
                Không tìm thấy người dùng nào
            </h3>
            <p style="color: var(--text-medium);">
                Chưa có người dùng nào trong hệ thống.
            </p>
        </div>
    </c:otherwise>
</c:choose>

<jsp:include page="../../common/footer.jsp"/>
