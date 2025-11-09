<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Logging out...</title>
    <script>
        // Clear all cookies using JavaScript
        function deleteAllCookies() {
            var cookies = document.cookie.split(";");
            for (var i = 0; i < cookies.length; i++) {
                var cookie = cookies[i];
                var eqPos = cookie.indexOf("=");
                var name = eqPos > -1 ? cookie.substr(0, eqPos) : cookie;
                document.cookie = name.trim() + "=;expires=Thu, 01 Jan 1970 00:00:00 GMT;path=/";
            }
        }

        // Execute on page load
        window.onload = function() {
            deleteAllCookies();
            // Redirect to login after 500ms
            setTimeout(function() {
                window.location.href = "${pageContext.request.contextPath}/login?logout=true";
            }, 500);
        };
    </script>
</head>
<body>
    <div style="text-align: center; padding: 50px; font-family: Arial, sans-serif;">
        <h2>Đang đăng xuất...</h2>
        <p>Vui lòng chờ...</p>
    </div>
</body>
</html>
