<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="vn.edu.fpt.swp.model.User, vn.edu.fpt.swp.model.Category" %>
<%
    HttpSession userSession = request.getSession(false);
    if (userSession == null || userSession.getAttribute("user") == null) {
        response.sendRedirect(request.getContextPath() + "/auth?action=login");
        return;
    }
    
    Category category = (Category) request.getAttribute("category");
    String error = (String) request.getAttribute("error");
    String name = (String) request.getAttribute("name");
    String description = (String) request.getAttribute("description");
    
    boolean isEdit = (category != null);
    String pageTitle = isEdit ? "Edit Category" : "Create Category";
    String formAction = isEdit ? "update" : "save";
    
    if (isEdit) {
        if (name == null) name = category.getName();
        if (description == null) description = category.getDescription();
    } else {
        if (name == null) name = "";
        if (description == null) description = "";
    }
%>

<c:set var="pageTitle" value="<%= pageTitle %>" />
<%@ include file="/views/layout/header.jsp" %>

<!-- Menu -->
<%@ include file="/views/layout/sidebar.jsp" %>

<!-- Layout page -->
<div class="layout-page">
    <!-- Navbar -->
    <%@ include file="/views/layout/navbar.jsp" %>

    <!-- Content wrapper -->
    <div class="content-wrapper">
        <!-- Content -->
        <div class="container-xxl flex-grow-1 container-p-y">
            <!-- Page header -->
            <div class="row mb-4">
                <div class="col-md-12">
                    <h4 class="fw-bold"><%= pageTitle %></h4>
                    <p class="text-muted"><%= isEdit ? "Update category information" : "Create a new product category" %></p>
                </div>
            </div>

            <!-- Form Card -->
            <div class="row">
                <div class="col-md-8">
                    <div class="card">
                        <div class="card-body">
                            <% if (error != null) { %>
                            <div class="alert alert-danger alert-dismissible" role="alert">
                                <%= error %>
                                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                            </div>
                            <% } %>

                            <form method="post" action="${pageContext.request.contextPath}/category">
                                <input type="hidden" name="action" value="<%= formAction %>" />
                                <% if (isEdit) { %>
                                <input type="hidden" name="id" value="<%= category.getId() %>" />
                                <% } %>

                                <!-- Category Name -->
                                <div class="mb-4">
                                    <label for="name" class="form-label">Category Name <span class="text-danger">*</span></label>
                                    <input type="text" class="form-control" id="name" name="name" 
                                           placeholder="Enter category name" value="<%= name %>"
                                           maxlength="255" required />
                                    <small class="form-text text-muted">Maximum 255 characters</small>
                                </div>

                                <!-- Category Description -->
                                <div class="mb-4">
                                    <label for="description" class="form-label">Description</label>
                                    <textarea class="form-control" id="description" name="description" 
                                              placeholder="Enter category description (optional)" 
                                              rows="4" maxlength="500"><%= description != null ? description : "" %></textarea>
                                    <small class="form-text text-muted">Maximum 500 characters</small>
                                </div>

                                <!-- Buttons -->
                                <div class="mb-3">
                                    <button type="submit" class="btn btn-primary">
                                        <span class="tf-icons bx bx-save"></span> <%= isEdit ? "Update" : "Create" %>
                                    </button>
                                    <a href="${pageContext.request.contextPath}/category" class="btn btn-secondary">
                                        <span class="tf-icons bx bx-arrow-back"></span> Cancel
                                    </a>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>

                <!-- Info Sidebar -->
                <div class="col-md-4">
                    <div class="card">
                        <div class="card-header">
                            <h5 class="card-title mb-0">Information</h5>
                        </div>
                        <div class="card-body">
                            <div class="mb-3">
                                <h6 class="mb-2">Category Name</h6>
                                <p class="text-muted small">The name that will be displayed in the system</p>
                            </div>
                            <div class="mb-3">
                                <h6 class="mb-2">Description</h6>
                                <p class="text-muted small">Optional details about the category to help identify it</p>
                            </div>
                            <% if (isEdit) { %>
                            <div class="mb-3">
                                <h6 class="mb-2">Category ID</h6>
                                <p class="text-muted small"><%= category.getId() %></p>
                            </div>
                            <% } %>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <!-- / Content -->

        <!-- Footer -->
        <%@ include file="/views/layout/footer.jsp" %>
    </div>
</div>
<!-- / Layout page -->

<script src="${pageContext.request.contextPath}/assets/vendor/libs/jquery/jquery.js"></script>
<script src="${pageContext.request.contextPath}/assets/vendor/libs/popper/popper.js"></script>
<script src="${pageContext.request.contextPath}/assets/vendor/js/bootstrap.js"></script>
<script src="${pageContext.request.contextPath}/assets/vendor/libs/perfect-scrollbar/perfect-scrollbar.js"></script>
<script src="${pageContext.request.contextPath}/assets/vendor/js/menu.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/main.js"></script>
</body>
</html>
