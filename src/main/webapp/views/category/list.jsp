<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="vn.edu.fpt.swp.model.User" %>
<%
    HttpSession userSession = request.getSession(false);
    if (userSession == null || userSession.getAttribute("user") == null) {
        response.sendRedirect(request.getContextPath() + "/auth?action=login");
        return;
    }
    User user = (User) userSession.getAttribute("user");
    String userRole = user.getRole();
    
    String error = (String) request.getAttribute("error");
    String success = (String) request.getAttribute("success");
    String search = (String) request.getAttribute("search");
    if (search == null) search = "";
%>

<c:set var="pageTitle" value="Categories" />
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
                <div class="col-md-6">
                    <h4 class="fw-bold">Categories</h4>
                    <p class="text-muted">Manage product categories</p>
                </div>
                <div class="col-md-6 text-end">
                    <% if (userRole.equals("Admin") || userRole.equals("Manager")) { %>
                    <a href="${pageContext.request.contextPath}/category?action=create" class="btn btn-primary">
                        <span class="tf-icons bx bx-plus"></span> Add Category
                    </a>
                    <% } %>
                </div>
            </div>

            <!-- Alerts -->
            <% if (error != null) { %>
            <div class="alert alert-danger alert-dismissible" role="alert">
                <%= error %>
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
            <% } %>
            
            <% if (success != null) { %>
            <div class="alert alert-success alert-dismissible" role="alert">
                <%= success %>
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
            <% } %>

            <!-- Search -->
            <div class="card mb-4">
                <div class="card-body">
                    <form method="get" action="${pageContext.request.contextPath}/category?action=list" class="row g-3">
                        <div class="col-md-9">
                            <label for="search" class="form-label">Search</label>
                            <input type="text" id="search" name="search" class="form-control" 
                                   placeholder="Search by category name or description" value="<%= search %>" />
                        </div>
                        <div class="col-md-3">
                            <label>&nbsp;</label>
                            <button type="submit" class="btn btn-primary w-100">
                                <span class="tf-icons bx bx-search"></span> Search
                            </button>
                        </div>
                    </form>
                </div>
            </div>

            <!-- Categories Table -->
            <div class="card">
                <div class="table-responsive text-nowrap">
                    <table class="table table-hover">
                        <thead class="table-light">
                            <tr>
                                <th>Name</th>
                                <th>Description</th>
                                <th>Product Count</th>
                                <% if (userRole.equals("Admin") || userRole.equals("Manager")) { %>
                                <th>Actions</th>
                                <% } %>
                            </tr>
                        </thead>
                        <tbody class="table-border-bottom-0">
                            <c:choose>
                                <c:when test="${empty categories}">
                                    <tr>
                                        <td colspan="<%= userRole.equals("Admin") || userRole.equals("Manager") ? "4" : "3" %>" class="text-center text-muted py-4">
                                            No categories found
                                        </td>
                                    </tr>
                                </c:when>
                                <c:otherwise>
                                    <c:forEach var="category" items="${categories}">
                                        <tr>
                                            <td>
                                                <strong>${category.name}</strong>
                                            </td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${empty category.description}">
                                                        <span class="text-muted">-</span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        ${category.description}
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>
                                                <span class="badge bg-primary">
                                                    ${requestScope["productCount_".concat(category.id)]}
                                                </span>
                                            </td>
                                            <% if (userRole.equals("Admin") || userRole.equals("Manager")) { %>
                                            <td>
                                                <div class="dropdown">
                                                    <button type="button" class="btn p-0 dropdown-toggle hide-arrow" data-bs-toggle="dropdown">
                                                        <i class="bx bx-dots-vertical-rounded"></i>
                                                    </button>
                                                    <div class="dropdown-menu">
                                                        <a class="dropdown-item" href="${pageContext.request.contextPath}/category?action=edit&id=${category.id}">
                                                            <i class="bx bx-edit-alt me-1"></i> Edit
                                                        </a>
                                                        <form method="post" style="display:inline;" 
                                                              onsubmit="return confirm('Are you sure you want to delete this category?');">
                                                            <input type="hidden" name="action" value="delete" />
                                                            <input type="hidden" name="id" value="${category.id}" />
                                                            <button type="submit" class="dropdown-item text-danger">
                                                                <i class="bx bx-trash me-1"></i> Delete
                                                            </button>
                                                        </form>
                                                    </div>
                                                </div>
                                            </td>
                                            <% } %>
                                        </tr>
                                    </c:forEach>
                                </c:otherwise>
                            </c:choose>
                        </tbody>
                    </table>
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
