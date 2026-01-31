<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!doctype html>
<html lang="en" class="${layoutClass != null ? layoutClass : 'layout-menu-fixed layout-compact'}" data-assets-path="${pageContext.request.contextPath}/" data-template="vertical-menu-template-free">
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0" />
    <title>${pageTitle != null ? pageTitle : 'Dashboard'} - Smart WMS</title>
    <meta name="description" content="Smart Warehouse Management System" />

    <!-- Favicon -->
    <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/assets/img/favicon/favicon.ico" />

    <!-- Fonts -->
    <link rel="preconnect" href="https://fonts.googleapis.com" />
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
    <link href="https://fonts.googleapis.com/css2?family=Public+Sans:ital,wght@0,300;0,400;0,500;0,600;0,700;1,300;1,400;1,500;1,600;1,700&display=swap" rel="stylesheet" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/vendor/fonts/iconify-icons.css" />

    <!-- Core CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/vendor/css/core.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/demo.css" />

    <!-- Vendors CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/vendor/libs/perfect-scrollbar/perfect-scrollbar.css" />

    <!-- Page-specific CSS -->
    <c:if test="${not empty pageCss}">
        <link rel="stylesheet" href="${pageContext.request.contextPath}${pageCss}" />
    </c:if>

    <!-- Helpers -->
    <script src="${pageContext.request.contextPath}/assets/vendor/js/helpers.js"></script>
    <script src="${pageContext.request.contextPath}/assets/js/config.js"></script>
</head>

<body>
    <c:choose>
        <c:when test="${layoutType eq 'auth'}">
            <!-- Auth layout: no sidebar/navbar/footer -->
            <jsp:include page="${contentPage}" />
        </c:when>
        <c:otherwise>
            <!-- Main layout: with sidebar, navbar, and footer -->
            <!-- Layout wrapper -->
            <div class="layout-wrapper layout-content-navbar">
                <div class="layout-container">
                    <!-- Sidebar and Navbar -->
                    <jsp:include page="/WEB-INF/views/common/header.jsp" />
                    
                    <!-- Layout page -->
                    <div class="layout-page">
                        <!-- Content wrapper -->
                        <div class="content-wrapper">
                            <!-- Page Content -->
                            <jsp:include page="${contentPage}" />
                            
                            <!-- Footer -->
                            <jsp:include page="/WEB-INF/views/common/footer.jsp" />
                            
                            <div class="content-backdrop fade"></div>
                        </div>
                        <!-- / Content wrapper -->
                    </div>
                    <!-- / Layout page -->
                </div>
                <!-- Overlay -->
                <div class="layout-overlay layout-menu-toggle"></div>
            </div>
            <!-- / Layout wrapper -->
        </c:otherwise>
    </c:choose>

    <!-- Core JS -->
    <script src="${pageContext.request.contextPath}/assets/vendor/libs/jquery/jquery.js"></script>
    <script src="${pageContext.request.contextPath}/assets/vendor/libs/popper/popper.js"></script>
    <script src="${pageContext.request.contextPath}/assets/vendor/js/bootstrap.js"></script>
    <script src="${pageContext.request.contextPath}/assets/vendor/libs/perfect-scrollbar/perfect-scrollbar.js"></script>
    <script src="${pageContext.request.contextPath}/assets/vendor/js/menu.js"></script>

    <!-- Main JS -->
    <script src="${pageContext.request.contextPath}/assets/js/main.js"></script>

    <!-- Page-specific JS -->
    <c:if test="${not empty pageJs}">
        <script src="${pageContext.request.contextPath}${pageJs}"></script>
    </c:if>

    <!-- GitHub Buttons -->
    <script async defer src="https://buttons.github.io/buttons.js"></script>
</body>
</html>
