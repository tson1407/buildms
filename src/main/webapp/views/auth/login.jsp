<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en" class="light-style customizer-hide" dir="ltr" data-theme="theme-default" 
      data-assets-path="${pageContext.request.contextPath}/assets/" data-template="vertical-menu-template-free">
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0" />
    <title>Login - Smart WMS</title>
    <meta name="description" content="" />
    
    <!-- Favicon -->
    <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/assets/img/favicon/favicon.ico" />
    
    <!-- Fonts -->
    <link rel="preconnect" href="https://fonts.googleapis.com" />
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
    <link href="https://fonts.googleapis.com/css2?family=Public+Sans:ital,wght@0,300;0,400;0,500;0,600;0,700;1,300;1,400;1,500;1,600;1,700&display=swap" rel="stylesheet" />
    
    <!-- Icons -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/vendor/fonts/boxicons.css" />
    
    <!-- Core CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/vendor/css/core.css" class="template-customizer-core-css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/vendor/css/theme-default.css" class="template-customizer-theme-css" />
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
                <!-- Login Card -->
                <div class="card">
                    <div class="card-body">
                        <!-- Logo -->
                        <div class="app-brand justify-content-center mb-4">
                            <a href="${pageContext.request.contextPath}/" class="app-brand-link gap-2">
                                <span class="app-brand-text demo text-body fw-bolder">Smart WMS</span>
                            </a>
                        </div>
                        <!-- /Logo -->
                        
                        <h4 class="mb-2">Welcome to Smart WMS! 👋</h4>
                        <p class="mb-4">Please sign-in to your account and start the adventure</p>
                        
                        <!-- Error Message -->
                        <c:if test="${not empty error}">
                            <div class="alert alert-danger alert-dismissible" role="alert">
                                ${error}
                                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                            </div>
                        </c:if>
                        
                        <!-- Success Message -->
                        <c:if test="${param.success == 'registered'}">
                            <div class="alert alert-success alert-dismissible" role="alert">
                                Registration successful! Please login with your credentials.
                                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                            </div>
                        </c:if>
                        
                        <c:if test="${param.success == 'logout'}">
                            <div class="alert alert-info alert-dismissible" role="alert">
                                You have been successfully logged out.
                                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                            </div>
                        </c:if>
                        
                        <!-- Login Form -->
                        <form id="formAuthentication" class="mb-3" action="${pageContext.request.contextPath}/auth" method="POST">
                            <input type="hidden" name="action" value="login">
                            
                            <div class="mb-3">
                                <label for="username" class="form-label">Username</label>
                                <input type="text" class="form-control" id="username" name="username" 
                                       value="${username}" placeholder="Enter your username" autofocus required />
                            </div>
                            
                            <div class="mb-3 form-password-toggle">
                                <div class="d-flex justify-content-between">
                                    <label class="form-label" for="password">Password</label>
                                </div>
                                <div class="input-group input-group-merge">
                                    <input type="password" id="password" class="form-control" name="password"
                                           placeholder="&#xb7;&#xb7;&#xb7;&#xb7;&#xb7;&#xb7;&#xb7;&#xb7;&#xb7;&#xb7;&#xb7;&#xb7;"
                                           aria-describedby="password" required />
                                    <span class="input-group-text cursor-pointer"><i class="bx bx-hide"></i></span>
                                </div>
                            </div>
                            
                            <div class="mb-3">
                                <div class="form-check">
                                    <input class="form-check-input" type="checkbox" id="remember-me" name="rememberMe" />
                                    <label class="form-check-label" for="remember-me"> Remember Me </label>
                                </div>
                            </div>
                            
                            <div class="mb-3">
                                <button class="btn btn-primary d-grid w-100" type="submit">Sign in</button>
                            </div>
                        </form>
                        
                        <p class="text-center">
                            <span>New on our platform?</span>
                            <a href="${pageContext.request.contextPath}/auth?action=register">
                                <span>Create an account</span>
                            </a>
                        </p>
                    </div>
                </div>
                <!-- /Login Card -->
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
    
    <!-- Main JS -->
    <script src="${pageContext.request.contextPath}/assets/js/main.js"></script>
    
    <!-- Page JS -->
    <script>
        // Toggle password visibility
        document.querySelectorAll('.form-password-toggle').forEach(function(el) {
            const passwordInput = el.querySelector('input');
            const toggleIcon = el.querySelector('.input-group-text i');
            
            el.querySelector('.input-group-text').addEventListener('click', function() {
                if (passwordInput.type === 'password') {
                    passwordInput.type = 'text';
                    toggleIcon.classList.remove('bx-hide');
                    toggleIcon.classList.add('bx-show');
                } else {
                    passwordInput.type = 'password';
                    toggleIcon.classList.remove('bx-show');
                    toggleIcon.classList.add('bx-hide');
                }
            });
        });
    </script>
</body>
</html>
