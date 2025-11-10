<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate">
    <meta http-equiv="Pragma" content="no-cache">
    <meta http-equiv="Expires" content="0">
    <title>Đăng xuất thành công</title>
    <script>
        // Clear any remaining non-HttpOnly cookies (if any)
        // Note: HttpOnly cookies (JSESSIONID, JWT_TOKEN) are already cleared by server
        function clearNonHttpOnlyCookies() {
            var cookies = document.cookie.split(";");
            for (var i = 0; i < cookies.length; i++) {
                var cookie = cookies[i];
                var eqPos = cookie.indexOf("=");
                var name = eqPos > -1 ? cookie.substr(0, eqPos) : cookie;
                // Try to delete cookie
                document.cookie = name.trim() + "=;expires=Thu, 01 Jan 1970 00:00:00 GMT;path=/";
                console.log("Attempted to clear cookie: " + name.trim());
            }
        }

        // Execute on page load
        window.onload = function() {
            // Clear any non-HttpOnly cookies
            clearNonHttpOnlyCookies();

            // Redirect to login page after 1 second
            setTimeout(function() {
                window.location.replace("${pageContext.request.contextPath}/login?logout=true");
            }, 1000);
        };
    </script>
    <style>
        body {
            font-family: Arial, sans-serif;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
        }
        .logout-container {
            background: white;
            padding: 40px;
            border-radius: 10px;
            box-shadow: 0 10px 25px rgba(0,0,0,0.2);
            text-align: center;
            max-width: 400px;
        }
        .logout-container h2 {
            color: #667eea;
            margin-bottom: 20px;
        }
        .spinner {
            border: 4px solid #f3f3f3;
            border-top: 4px solid #667eea;
            border-radius: 50%;
            width: 40px;
            height: 40px;
            animation: spin 1s linear infinite;
            margin: 20px auto;
        }
        @keyframes spin {
            0% { transform: rotate(0deg); }
            100% { transform: rotate(360deg); }
        }
    </style>
</head>
<body>
    <div class="logout-container">
        <h2>✓ Đăng xuất thành công!</h2>
        <div class="spinner"></div>
        <p>Đang chuyển hướng đến trang đăng nhập...</p>
        <p style="font-size: 12px; color: #999; margin-top: 20px;">
            Tất cả phiên đăng nhập đã được xóa an toàn.
        </p>
    </div>
</body>
</html>
