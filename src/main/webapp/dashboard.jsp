<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="vn.edu.fpt.swp.model.User" %>
<%
    // Check if user is logged in
    HttpSession userSession = request.getSession(false);
    if (userSession == null || userSession.getAttribute("user") == null) {
        response.sendRedirect(request.getContextPath() + "/auth?action=login");
        return;
    }
    
    User currentUser = (User) userSession.getAttribute("user");
%>
<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Dashboard - Smart WMS</title>
    
    <!-- Favicon -->
    <link rel="icon" type="image/x-icon" href="<%= request.getContextPath() %>/assets/img/favicon/favicon.ico" />
    
    <!-- Fonts -->
    <link rel="preconnect" href="https://fonts.googleapis.com" />
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
    <link href="https://fonts.googleapis.com/css2?family=Public+Sans:ital,wght@0,300;0,400;0,500;0,600;0,700;1,300;1,400;1,500;1,600;1,700&display=swap" rel="stylesheet" />
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/vendor/fonts/iconify-icons.css" />
    
    <!-- Core CSS -->
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/vendor/css/core.css" />
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/demo.css" />
    
    <!-- Vendors CSS -->
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/vendor/libs/perfect-scrollbar/perfect-scrollbar.css" />
</head>

<body>
    <div class="container-xxl flex-grow-1 container-p-y">
        <div class="row">
            <div class="col-12">
                <div class="card">
                    <div class="card-body">
                        <h2 class="card-title">Welcome to Smart WMS!</h2>
                        <p class="card-text">
                            Hello, <strong><%= currentUser.getName() != null ? currentUser.getName() : currentUser.getUsername() %></strong>
                        </p>
                        <p class="card-text">
                            Role: <span class="badge bg-primary"><%= currentUser.getRole() %></span>
                        </p>
                        
                        <hr />
                        
                        <h5>Quick Actions</h5>
                        <div class="mt-3">
                            <a href="<%= request.getContextPath() %>/auth?action=change-password" class="btn btn-primary me-2">
                                Change Password
                            </a>
                            
                            <% if ("Admin".equals(currentUser.getRole())) { %>
                            <a href="<%= request.getContextPath() %>/auth?action=register" class="btn btn-success me-2">
                                Create New User
                            </a>
                            <% } %>
                            
                            <a href="<%= request.getContextPath() %>/auth?action=logout" class="btn btn-danger">
                                Logout
                            </a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <!-- Core JS -->
    <script src="<%= request.getContextPath() %>/assets/vendor/libs/jquery/jquery.js"></script>
    <script src="<%= request.getContextPath() %>/assets/vendor/libs/popper/popper.js"></script>
    <script src="<%= request.getContextPath() %>/assets/vendor/js/bootstrap.js"></script>
    <script src="<%= request.getContextPath() %>/assets/vendor/libs/perfect-scrollbar/perfect-scrollbar.js"></script>
    <script src="<%= request.getContextPath() %>/assets/vendor/js/menu.js"></script>
    <script src="<%= request.getContextPath() %>/assets/js/main.js"></script>
</body>
</html>
