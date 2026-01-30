<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="vn.edu.fpt.swp.model.User, vn.edu.fpt.swp.model.Product" %>
<%
    HttpSession userSession = request.getSession(false);
    if (userSession == null || userSession.getAttribute("user") == null) {
        response.sendRedirect(request.getContextPath() + "/auth?action=login");
        return;
    }
    
    Product product = (Product) request.getAttribute("product");
    String error = (String) request.getAttribute("error");
    String sku = (String) request.getAttribute("sku");
    String name = (String) request.getAttribute("name");
    String unit = (String) request.getAttribute("unit");
    String categoryId = (String) request.getAttribute("categoryId");
    
    boolean isEdit = (product != null);
    String pageTitle = isEdit ? "Edit Product" : "Create Product";
    String formAction = isEdit ? "update" : "save";
    
    if (isEdit) {
        if (sku == null) sku = product.getSku();
        if (name == null) name = product.getName();
        if (unit == null) unit = product.getUnit();
        if (categoryId == null) categoryId = String.valueOf(product.getCategoryId());
    } else {
        if (sku == null) sku = "";
        if (name == null) name = "";
        if (unit == null) unit = "";
        if (categoryId == null) categoryId = "";
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
                    <p class="text-muted"><%= isEdit ? "Update product information" : "Create a new product" %></p>
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

                            <form method="post" action="${pageContext.request.contextPath}/product">
                                <input type="hidden" name="action" value="<%= formAction %>" />
                                <% if (isEdit) { %>
                                <input type="hidden" name="id" value="<%= product.getId() %>" />
                                <% } %>

                                <!-- SKU -->
                                <div class="mb-4">
                                    <label for="sku" class="form-label">SKU <span class="text-danger">*</span></label>
                                    <input type="text" class="form-control" id="sku" name="sku" 
                                           placeholder="e.g., PROD-001" value="<%= sku %>"
                                           maxlength="100" required />
                                    <small class="form-text text-muted">Alphanumeric with hyphens. Maximum 100 characters. Must be unique.</small>
                                </div>

                                <!-- Product Name -->
                                <div class="mb-4">
                                    <label for="name" class="form-label">Product Name <span class="text-danger">*</span></label>
                                    <input type="text" class="form-control" id="name" name="name" 
                                           placeholder="Enter product name" value="<%= name %>"
                                           maxlength="255" required />
                                    <small class="form-text text-muted">Maximum 255 characters</small>
                                </div>

                                <!-- Unit of Measure -->
                                <div class="mb-4">
                                    <label for="unit" class="form-label">Unit of Measure</label>
                                    <select id="unit" name="unit" class="form-select">
                                        <option value="">Select a unit</option>
                                        <option value="pcs" <%= unit != null && unit.equals("pcs") ? "selected" : "" %>>Pieces (pcs)</option>
                                        <option value="kg" <%= unit != null && unit.equals("kg") ? "selected" : "" %>>Kilogram (kg)</option>
                                        <option value="box" <%= unit != null && unit.equals("box") ? "selected" : "" %>>Box</option>
                                        <option value="carton" <%= unit != null && unit.equals("carton") ? "selected" : "" %>>Carton</option>
                                        <option value="liter" <%= unit != null && unit.equals("liter") ? "selected" : "" %>>Liter</option>
                                        <option value="meter" <%= unit != null && unit.equals("meter") ? "selected" : "" %>>Meter</option>
                                        <option value="unit" <%= unit != null && unit.equals("unit") ? "selected" : "" %>>Unit</option>
                                    </select>
                                    <small class="form-text text-muted">Common units are provided, or enter a custom value</small>
                                </div>

                                <!-- Category -->
                                <div class="mb-4">
                                    <label for="categoryId" class="form-label">Category <span class="text-danger">*</span></label>
                                    <select id="categoryId" name="categoryId" class="form-select" required>
                                        <option value="">Select a category</option>
                                        <c:forEach var="cat" items="${categories}">
                                            <option value="${cat.id}" <%= categoryId.equals(String.valueOf(cat.getId())) ? "selected" : "" %>>
                                                ${cat.name}
                                            </option>
                                        </c:forEach>
                                    </select>
                                    <small class="form-text text-muted">Product must belong to one category</small>
                                </div>

                                <!-- Buttons -->
                                <div class="mb-3">
                                    <button type="submit" class="btn btn-primary">
                                        <span class="tf-icons bx bx-save"></span> <%= isEdit ? "Update" : "Create" %>
                                    </button>
                                    <a href="${pageContext.request.contextPath}/product" class="btn btn-secondary">
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
                                <h6 class="mb-2">SKU</h6>
                                <p class="text-muted small">Product identifier - must be unique in the system</p>
                            </div>
                            <div class="mb-3">
                                <h6 class="mb-2">Product Name</h6>
                                <p class="text-muted small">The display name of the product</p>
                            </div>
                            <div class="mb-3">
                                <h6 class="mb-2">Unit of Measure</h6>
                                <p class="text-muted small">How the product is measured/sold (e.g., pcs, kg, box)</p>
                            </div>
                            <div class="mb-3">
                                <h6 class="mb-2">Category</h6>
                                <p class="text-muted small">Classification for organization and filtering</p>
                            </div>
                            <% if (isEdit) { %>
                            <div class="mb-3">
                                <h6 class="mb-2">Product ID</h6>
                                <p class="text-muted small"><%= product.getId() %></p>
                            </div>
                            <div class="mb-3">
                                <h6 class="mb-2">Status</h6>
                                <p class="text-muted small">
                                    <span class="badge bg-<%= product.isActive() ? "success" : "secondary" %>">
                                        <%= product.isActive() ? "Active" : "Inactive" %>
                                    </span>
                                </p>
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
