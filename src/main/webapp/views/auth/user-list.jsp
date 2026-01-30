<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="vn.edu.fpt.swp.model.User" %>
<%@ page import="java.util.List" %>
<%
    List<User> users = (List<User>) request.getAttribute("users");
%>
<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0" />
    <title>${pageTitle != null ? pageTitle : 'User Management - Smart WMS'}</title>
    
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
    
    <!-- Helpers -->
    <script src="${pageContext.request.contextPath}/assets/vendor/js/helpers.js"></script>
    <script src="${pageContext.request.contextPath}/assets/js/config.js"></script>
</head>

<body>
    <!-- Content -->
    <div class="container-xxl flex-grow-1 container-p-y">
        <h4 class="py-3 mb-4">
            <span class="text-muted fw-light">Admin /</span> User Management
        </h4>

        <!-- Add New User Button -->
        <div class="card mb-4">
            <div class="card-body">
                <a href="${pageContext.request.contextPath}/auth?action=register" class="btn btn-primary">
                    <i class="bx bx-plus me-1"></i> Create New User
                </a>
            </div>
        </div>

        <!-- User List Table -->
        <div class="card">
            <h5 class="card-header">Users</h5>
            <div class="table-responsive text-nowrap">
                <table class="table">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Username</th>
                            <th>Name</th>
                            <th>Email</th>
                            <th>Role</th>
                            <th>Status</th>
                            <th>Last Login</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody class="table-border-bottom-0">
                        <% if (users != null && !users.isEmpty()) { 
                            for (User user : users) { %>
                        <tr>
                            <td><%= user.getId() %></td>
                            <td><strong><%= user.getUsername() %></strong></td>
                            <td><%= user.getName() != null ? user.getName() : "-" %></td>
                            <td><%= user.getEmail() %></td>
                            <td>
                                <% String roleClass = "";
                                   if ("Admin".equals(user.getRole())) roleClass = "bg-label-danger";
                                   else if ("Manager".equals(user.getRole())) roleClass = "bg-label-warning";
                                   else if ("Staff".equals(user.getRole())) roleClass = "bg-label-info";
                                   else if ("Sales".equals(user.getRole())) roleClass = "bg-label-success";
                                %>
                                <span class="badge <%= roleClass %>"><%= user.getRole() %></span>
                            </td>
                            <td>
                                <% if ("Active".equals(user.getStatus())) { %>
                                    <span class="badge bg-label-success">Active</span>
                                <% } else { %>
                                    <span class="badge bg-label-secondary">Inactive</span>
                                <% } %>
                            </td>
                            <td><%= user.getLastLogin() != null ? user.getLastLogin().toString() : "Never" %></td>
                            <td>
                                <a href="${pageContext.request.contextPath}/auth?action=resetPassword&userId=<%= user.getId() %>" 
                                   class="btn btn-sm btn-warning" 
                                   title="Reset Password">
                                    <i class="bx bx-key"></i>
                                </a>
                            </td>
                        </tr>
                        <% } 
                        } else { %>
                        <tr>
                            <td colspan="8" class="text-center">No users found</td>
                        </tr>
                        <% } %>
                    </tbody>
                </table>
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
