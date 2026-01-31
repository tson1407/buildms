<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:if test="${empty sessionScope.user}">
    <c:redirect url="${pageContext.request.contextPath}/auth?action=login"/>
</c:if>
<c:if test="${sessionScope.user.role ne 'Admin'}">
    <c:redirect url="${pageContext.request.contextPath}/error/403.jsp"/>
</c:if>
<c:set var="error" value="${requestScope.error}"/>
<c:set var="success" value="${requestScope.success}"/>
<c:set var="username" value="${not empty requestScope.username ? requestScope.username : ''}"/>
<c:set var="name" value="${not empty requestScope.name ? requestScope.name : ''}"/>
<c:set var="email" value="${not empty requestScope.email ? requestScope.email : ''}"/>
<c:set var="role" value="${not empty requestScope.role ? requestScope.role : ''}"/>
<!doctype html>
<html lang="en" class="layout-wide customizer-hide" data-assets-path="${pageContext.request.contextPath}/assets/" data-template="vertical-menu-template-free">
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0" />
    <title>Register User - Smart WMS</title>
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

    <!-- Page CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/vendor/css/pages/page-auth.css" />

    <!-- Helpers -->
    <script src="${pageContext.request.contextPath}/assets/vendor/js/helpers.js"></script>
    <script src="${pageContext.request.contextPath}/assets/js/config.js"></script>
</head>

<body>
    <!-- Content -->
    <div class="container-xxl">
        <div class="authentication-wrapper authentication-basic container-p-y">
            <div class="authentication-inner">
                <!-- Register Card -->
                <div class="card px-sm-6 px-0">
                    <div class="card-body">
                        <!-- Logo -->
                        <div class="app-brand justify-content-center mb-6">
                            <a href="${pageContext.request.contextPath}/" class="app-brand-link gap-2">
                                <span class="app-brand-text demo text-heading fw-bold">Smart WMS</span>
                            </a>
                        </div>
                        <!-- /Logo -->
                        
                        <h4 class="mb-1">Create New User</h4>
                        <p class="mb-6">Register a new user account</p>

                        <c:if test="${not empty error}">
                        <div class="alert alert-danger alert-dismissible" role="alert">
                            ${error}
                            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                        </div>
                        </c:if>
                        
                        <c:if test="${not empty success}">
                        <div class="alert alert-success alert-dismissible" role="alert">
                            ${success}
                            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                        </div>
                        </c:if>

                        <form id="formAuthentication" class="mb-6" action="${pageContext.request.contextPath}/auth" method="post">
                            <input type="hidden" name="action" value="register" />
                            
                            <div class="mb-6">
                                <label for="username" class="form-label">Username *</label>
                                <input type="text" class="form-control" id="username" name="username" 
                                       placeholder="Enter username (3-50 characters)" value="${username}" 
                                       autofocus required minlength="3" maxlength="50" />
                            </div>
                            
                            <div class="mb-6">
                                <label for="name" class="form-label">Full Name</label>
                                <input type="text" class="form-control" id="name" name="name" 
                                       placeholder="Enter full name" value="${name}" />
                            </div>
                            
                            <div class="mb-6">
                                <label for="email" class="form-label">Email *</label>
                                <input type="email" class="form-control" id="email" name="email" 
                                       placeholder="Enter email address" value="${email}" required />
                            </div>
                            
                            <div class="mb-6">
                                <label for="role" class="form-label">Role *</label>
                                <select class="form-select" id="role" name="role" required>
                                    <option value="">Select Role</option>
                                    <option value="Admin" ${role eq 'Admin' ? 'selected' : ''}>Admin</option>
                                    <option value="Manager" ${role eq 'Manager' ? 'selected' : ''}>Manager</option>
                                    <option value="Staff" ${role eq 'Staff' ? 'selected' : ''}>Staff</option>
                                    <option value="Sales" ${role eq 'Sales' ? 'selected' : ''}>Sales</option>
                                </select>
                            </div>
                            
                            <div class="mb-6 form-password-toggle">
                                <label class="form-label" for="password">Password *</label>
                                <div class="input-group input-group-merge">
                                    <input type="password" id="password" class="form-control" name="password"
                                           placeholder="&#xb7;&#xb7;&#xb7;&#xb7;&#xb7;&#xb7;&#xb7;&#xb7;&#xb7;&#xb7;&#xb7;&#xb7;"
                                           aria-describedby="password" required minlength="6" />
                                    <span class="input-group-text cursor-pointer">
                                        <i class="icon-base bx bx-hide"></i>
                                    </span>
                                </div>
                                <small class="text-muted">Minimum 6 characters</small>
                            </div>
                            
                            <div class="mb-6 form-password-toggle">
                                <label class="form-label" for="confirmPassword">Confirm Password *</label>
                                <div class="input-group input-group-merge">
                                    <input type="password" id="confirmPassword" class="form-control" name="confirmPassword"
                                           placeholder="&#xb7;&#xb7;&#xb7;&#xb7;&#xb7;&#xb7;&#xb7;&#xb7;&#xb7;&#xb7;&#xb7;&#xb7;"
                                           aria-describedby="confirmPassword" required minlength="6" />
                                    <span class="input-group-text cursor-pointer">
                                        <i class="icon-base bx bx-hide"></i>
                                    </span>
                                </div>
                            </div>
                            
                            <div class="mb-6">
                                <button class="btn btn-primary d-grid w-100" type="submit">Create User</button>
                            </div>
                            
                            <div class="text-center">
                                <a href="${pageContext.request.contextPath}/index.jsp" class="btn btn-outline-secondary">
                                    Cancel
                                </a>
                            </div>
                        </form>
                    </div>
                </div>
                <!-- /Register Card -->
            </div>
        </div>
    </div>
    <!-- / Content -->

    <!-- Core JS -->
    <script src="${pageContext.request.contextPath}/assets/vendor/libs/jquery/jquery.js"></script>
    <script src="${pageContext.request.contextPath}/assets/vendor/libs/popper/popper.js"></script>
    <script src="${pageContext.request.contextPath}/assets/vendor/js/bootstrap.js"></script>
    <script src="${pageContext.request.contextPath}/assets/vendor/libs/perfect-scrollbar/perfect-scrollbar.js"></script>
    <script src="${pageContext.request.contextPath}/assets/vendor/js/menu.js"></script>
    <script src="${pageContext.request.contextPath}/assets/js/main.js"></script>
</body>
</html>
