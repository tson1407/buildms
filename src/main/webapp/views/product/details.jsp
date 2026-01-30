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
%>

<c:set var="pageTitle" value="Product Details" />
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
                    <h4 class="fw-bold">Product Details</h4>
                    <p class="text-muted">View product information and inventory</p>
                </div>
                <div class="col-md-6 text-end">
                    <a href="${pageContext.request.contextPath}/product" class="btn btn-secondary">
                        <span class="tf-icons bx bx-arrow-back"></span> Back to List
                    </a>
                    <% if (userRole.equals("Admin") || userRole.equals("Manager")) { %>
                    <a href="${pageContext.request.contextPath}/product?action=edit&id=${product.id}" class="btn btn-primary">
                        <span class="tf-icons bx bx-edit-alt"></span> Edit
                    </a>
                    <% } %>
                </div>
            </div>

            <!-- Product Info Card -->
            <div class="row mb-4">
                <div class="col-md-8">
                    <div class="card">
                        <div class="card-header">
                            <h5 class="card-title mb-0">Product Information</h5>
                        </div>
                        <div class="card-body">
                            <div class="row mb-4">
                                <div class="col-md-6">
                                    <h6 class="mb-2">SKU</h6>
                                    <p class="text-dark">
                                        <strong>${product.sku}</strong>
                                    </p>
                                </div>
                                <div class="col-md-6">
                                    <h6 class="mb-2">Status</h6>
                                    <p>
                                        <span class="badge bg-<%= ((c.core_rt.PageContext)pageContext).getAttribute("product").getClass().getMethod("isActive").invoke(((c.core_rt.PageContext)pageContext).getAttribute("product")) != null && (boolean)((c.core_rt.PageContext)pageContext).getAttribute("product").getClass().getMethod("isActive").invoke(((c.core_rt.PageContext)pageContext).getAttribute("product")) ? "success" : "secondary" %>">
                                            <%= ((c.core_rt.PageContext)pageContext).getAttribute("product").getClass().getMethod("isActive").invoke(((c.core_rt.PageContext)pageContext).getAttribute("product")) != null && (boolean)((c.core_rt.PageContext)pageContext).getAttribute("product").getClass().getMethod("isActive").invoke(((c.core_rt.PageContext)pageContext).getAttribute("product")) ? "Active" : "Inactive" %>
                                        </span>
                                    </p>
                                </div>
                            </div>

                            <div class="row mb-4">
                                <div class="col-md-6">
                                    <h6 class="mb-2">Product Name</h6>
                                    <p class="text-dark">
                                        <strong>${product.name}</strong>
                                    </p>
                                </div>
                                <div class="col-md-6">
                                    <h6 class="mb-2">Unit of Measure</h6>
                                    <p class="text-dark">
                                        <c:choose>
                                            <c:when test="${empty product.unit}">
                                                <span class="text-muted">-</span>
                                            </c:when>
                                            <c:otherwise>
                                                ${product.unit}
                                            </c:otherwise>
                                        </c:choose>
                                    </p>
                                </div>
                            </div>

                            <div class="row mb-4">
                                <div class="col-md-6">
                                    <h6 class="mb-2">Category</h6>
                                    <p class="text-dark">
                                        <strong>${category.name}</strong>
                                    </p>
                                </div>
                                <div class="col-md-6">
                                    <h6 class="mb-2">Created Date</h6>
                                    <p class="text-dark">
                                        ${product.createdAt}
                                    </p>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Inventory Summary -->
                <div class="col-md-4">
                    <div class="card">
                        <div class="card-header">
                            <h5 class="card-title mb-0">Inventory Summary</h5>
                        </div>
                        <div class="card-body">
                            <div class="text-center">
                                <h3 class="text-primary mb-2">
                                    <strong>${totalInventory}</strong>
                                </h3>
                                <p class="text-muted">Total Stock Quantity</p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Inventory By Location (Admin/Manager/Staff Only) -->
            <% if (userRole.equals("Admin") || userRole.equals("Manager") || userRole.equals("Staff")) { %>
            <div class="row">
                <div class="col-md-12">
                    <div class="card">
                        <div class="card-header">
                            <h5 class="card-title mb-0">Inventory by Location</h5>
                        </div>
                        <div class="table-responsive text-nowrap">
                            <table class="table table-hover">
                                <thead class="table-light">
                                    <tr>
                                        <th>Warehouse</th>
                                        <th>Location</th>
                                        <th>Quantity</th>
                                    </tr>
                                </thead>
                                <tbody class="table-border-bottom-0">
                                    <tr>
                                        <td colspan="3" class="text-center text-muted py-4">
                                            Inventory details view not yet implemented
                                        </td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
            <% } %>
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
