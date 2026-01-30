<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="vn.edu.fpt.swp.model.User" %>
<%
    User targetUser = (User) request.getAttribute("targetUser");
%>
<!doctype html>
<html lang="en" class="layout-wide customizer-hide">
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0" />
    <title>${pageTitle != null ? pageTitle : 'Reset Password - Smart WMS'}</title>
    
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
                <!-- Reset Password Card -->
                <div class="card px-sm-6 px-0">
                    <div class="card-body">
                        <!-- Logo -->
                        <div class="app-brand justify-content-center mb-6">
                            <a href="${pageContext.request.contextPath}/" class="app-brand-link gap-2">
                                <span class="app-brand-text demo text-heading fw-bold">Smart WMS</span>
                            </a>
                        </div>
                        <!-- /Logo -->
                        
                        <h4 class="mb-1">Reset User Password 🔑</h4>
                        <% if (targetUser != null) { %>
                            <p class="mb-6">Reset password for <strong><%= targetUser.getUsername() %></strong> (<%= targetUser.getEmail() %>)</p>
                        <% } else { %>
                            <p class="mb-6">Reset user password</p>
                        <% } %>

                        <!-- Error Message -->
                        <% if (request.getAttribute("error") != null) { %>
                            <div class="alert alert-danger alert-dismissible" role="alert">
                                <%= request.getAttribute("error") %>
                                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                            </div>
                        <% } %>
                        
                        <!-- Success Message -->
                        <% if (request.getAttribute("success") != null) { %>
                            <div class="alert alert-success alert-dismissible" role="alert">
                                <%= request.getAttribute("success") %>
                                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                            </div>
                        <% } %>

                        <% if (targetUser != null) { %>
                        <!-- Reset Password Form -->
                        <form id="formResetPassword" class="mb-6" action="${pageContext.request.contextPath}/auth" method="POST">
                            <input type="hidden" name="action" value="resetPassword" />
                            <input type="hidden" name="userId" value="<%= targetUser.getId() %>" />
                            
                            <div class="mb-6">
                                <label class="form-label" for="username">Username (Read-only)</label>
                                <input
                                    type="text"
                                    class="form-control"
                                    id="username"
                                    value="<%= targetUser.getUsername() %>"
                                    readonly
                                    disabled />
                            </div>
                            
                            <div class="mb-6">
                                <label class="form-label" for="email">Email (Read-only)</label>
                                <input
                                    type="text"
                                    class="form-control"
                                    id="email"
                                    value="<%= targetUser.getEmail() %>"
                                    readonly
                                    disabled />
                            </div>
                            
                            <div class="mb-6 form-password-toggle">
                                <label class="form-label" for="newPassword">New Password</label>
                                <div class="input-group input-group-merge">
                                    <input
                                        type="password"
                                        id="newPassword"
                                        class="form-control"
                                        name="newPassword"
                                        placeholder="&#xb7;&#xb7;&#xb7;&#xb7;&#xb7;&#xb7;&#xb7;&#xb7;&#xb7;&#xb7;&#xb7;&#xb7;"
                                        aria-describedby="newPassword"
                                        autofocus
                                        required />
                                    <span class="input-group-text cursor-pointer"><i class="icon-base bx bx-hide"></i></span>
                                </div>
                                <small class="form-text text-muted">Minimum 6 characters</small>
                            </div>
                            
                            <div class="mb-6 form-password-toggle">
                                <label class="form-label" for="confirmNewPassword">Confirm New Password</label>
                                <div class="input-group input-group-merge">
                                    <input
                                        type="password"
                                        id="confirmNewPassword"
                                        class="form-control"
                                        name="confirmNewPassword"
                                        placeholder="&#xb7;&#xb7;&#xb7;&#xb7;&#xb7;&#xb7;&#xb7;&#xb7;&#xb7;&#xb7;&#xb7;&#xb7;"
                                        aria-describedby="confirmNewPassword"
                                        required />
                                    <span class="input-group-text cursor-pointer"><i class="icon-base bx bx-hide"></i></span>
                                </div>
                            </div>
                            
                            <div class="mb-6">
                                <button class="btn btn-primary d-grid w-100" type="submit">Reset Password</button>
                            </div>
                        </form>
                        <% } %>

                        <p class="text-center">
                            <a href="${pageContext.request.contextPath}/auth?action=users">
                                <span>Back to User Management</span>
                            </a>
                        </p>
                    </div>
                </div>
                <!-- /Reset Password Card -->
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
</body>
</html>
