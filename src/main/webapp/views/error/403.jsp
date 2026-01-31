<%@ page contentType="text/html;charset=UTF-8"  %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>403 - Access Denied - Smart WMS</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/vendor/css/core.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/vendor/css/theme-default.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/demo.css" />
</head>
<body>
    <div class="container-xxl container-p-y">
        <div class="misc-wrapper">
            <h2 class="mb-2 mx-2">Access Denied! 🔒</h2>
            <p class="mb-4 mx-2">You don't have permission to access this resource.</p>
            <c:if test="${not empty error}">
                <p class="mb-4 mx-2 text-danger">${error}</p>
            </c:if>
            <a href="${pageContext.request.contextPath}/" class="btn btn-primary">Back to home</a>
        </div>
    </div>
</body>
</html>
