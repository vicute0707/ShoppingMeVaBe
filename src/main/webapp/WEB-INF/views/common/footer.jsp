<%@ page contentType="text/html;charset=UTF-8" language="java" %>
</div> <!-- Close #main-content -->

<footer class="mt-auto">
    <div class="footer-content">
        <div class="container">
            <div class="row">
                <div class="col-md-4 mb-4 mb-md-0">
                    <div class="footer-brand">
                        <i class="fas fa-heart"></i>
                        <span>Shopping MeVaBe</span>
                    </div>
                    <p class="footer-description">
                        Nơi mua sắm đáng yêu và tin cậy của bạn
                    </p>
                    <div class="footer-social">
                        <a href="#" class="social-link"><i class="fab fa-facebook"></i></a>
                        <a href="#" class="social-link"><i class="fab fa-instagram"></i></a>
                        <a href="#" class="social-link"><i class="fab fa-twitter"></i></a>
                        <a href="#" class="social-link"><i class="fab fa-youtube"></i></a>
                    </div>
                </div>
                <div class="col-md-4 mb-4 mb-md-0">
                    <h5 class="footer-title">Liên kết nhanh</h5>
                    <ul class="footer-links">
                        <li><a href="${pageContext.request.contextPath}/"><i class="fas fa-angle-right"></i> Trang chủ</a></li>
                        <li><a href="${pageContext.request.contextPath}/products"><i class="fas fa-angle-right"></i> Sản phẩm</a></li>
                        <li><a href="#"><i class="fas fa-angle-right"></i> Về chúng tôi</a></li>
                        <li><a href="#"><i class="fas fa-angle-right"></i> Liên hệ</a></li>
                    </ul>
                </div>
                <div class="col-md-4">
                    <h5 class="footer-title">Thông tin</h5>
                    <ul class="footer-info">
                        <li>
                            <i class="fas fa-phone"></i>
                            <span>0123 456 789</span>
                        </li>
                        <li>
                            <i class="fas fa-envelope"></i>
                            <span>contact@shoppingmevabe.vn</span>
                        </li>
                        <li>
                            <i class="fas fa-map-marker-alt"></i>
                            <span>123 Đường ABC, TP. HCM</span>
                        </li>
                    </ul>
                </div>
            </div>
            <div class="footer-bottom">
                <div class="row">
                    <div class="col-12 text-center">
                        <p class="mb-0">&copy; 2025 Shopping MeVaBe. Made with <i class="fas fa-heart" style="color: #FFD1DC;"></i> in Vietnam</p>
                    </div>
                </div>
            </div>
        </div>
    </div>
</footer>

<style>
    footer {
        background: linear-gradient(135deg, #FFD1DC 0%, #E0BBE4 50%, #A7C7E7 100%);
        color: white;
        margin-top: 60px;
        position: relative;
        overflow: hidden;
    }

    footer::before {
        content: '';
        position: absolute;
        top: 0;
        left: 0;
        right: 0;
        height: 3px;
        background: linear-gradient(90deg, var(--pastel-pink), var(--pastel-yellow), var(--pastel-green), var(--pastel-blue));
    }

    .footer-content {
        padding: 50px 0 20px;
    }

    .footer-brand {
        display: flex;
        align-items: center;
        gap: 12px;
        font-size: 1.8rem;
        font-weight: 700;
        margin-bottom: 15px;
        font-family: 'Quicksand', sans-serif;
    }

    .footer-brand i {
        font-size: 2rem;
        background: white;
        color: #FFD1DC;
        padding: 10px;
        border-radius: 50%;
        box-shadow: 0 5px 15px rgba(0,0,0,0.2);
    }

    .footer-description {
        font-size: 1rem;
        opacity: 0.95;
        margin-bottom: 20px;
        font-weight: 500;
    }

    .footer-social {
        display: flex;
        gap: 12px;
    }

    .social-link {
        width: 45px;
        height: 45px;
        display: flex;
        align-items: center;
        justify-content: center;
        background: rgba(255, 255, 255, 0.2);
        border-radius: 50%;
        color: white;
        font-size: 1.3rem;
        transition: all 0.3s ease;
        text-decoration: none;
    }

    .social-link:hover {
        background: white;
        color: #FFD1DC;
        transform: translateY(-5px);
        box-shadow: 0 8px 20px rgba(0,0,0,0.2);
    }

    .footer-title {
        font-size: 1.3rem;
        font-weight: 700;
        margin-bottom: 20px;
        position: relative;
        padding-bottom: 12px;
        font-family: 'Quicksand', sans-serif;
    }

    .footer-title::after {
        content: '';
        position: absolute;
        bottom: 0;
        left: 0;
        width: 50px;
        height: 3px;
        background: white;
        border-radius: 2px;
    }

    .footer-links {
        list-style: none;
        padding: 0;
    }

    .footer-links li {
        margin-bottom: 12px;
    }

    .footer-links a {
        color: white;
        text-decoration: none;
        display: flex;
        align-items: center;
        gap: 8px;
        font-weight: 500;
        transition: all 0.3s ease;
        opacity: 0.9;
    }

    .footer-links a:hover {
        opacity: 1;
        padding-left: 10px;
    }

    .footer-info {
        list-style: none;
        padding: 0;
    }

    .footer-info li {
        margin-bottom: 15px;
        display: flex;
        align-items: start;
        gap: 12px;
        font-weight: 500;
        opacity: 0.95;
    }

    .footer-info i {
        font-size: 1.2rem;
        margin-top: 3px;
    }

    .footer-bottom {
        margin-top: 40px;
        padding-top: 25px;
        border-top: 1px solid rgba(255, 255, 255, 0.3);
    }

    .footer-bottom p {
        font-weight: 600;
        opacity: 0.95;
    }

    @media (max-width: 768px) {
        .footer-content {
            padding: 40px 0 20px;
        }

        .footer-brand {
            font-size: 1.5rem;
        }
    }
</style>

<!-- AI Chatbot Widget -->
<jsp:include page="chatbot.jsp"/>

<script src="https://code.jquery.com/jquery-3.7.0.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script src="${pageContext.request.contextPath}/js/validation.js"></script>
</body>
</html>
