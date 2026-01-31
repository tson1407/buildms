<%@ page contentType="text/html;charset=UTF-8"  %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:if test="${empty sessionScope.user}">
    <c:redirect url="${pageContext.request.contextPath}/auth?action=login"/>
</c:if>
<c:set var="userRole" value="${sessionScope.user.role}"/>
<c:set var="error" value="${requestScope.error}"/>
<c:set var="success" value="${requestScope.success}"/>
<c:set var="search" value="${not empty requestScope.search ? requestScope.search : ''}"/>
<c:set var="selectedCategoryId" value="${not empty requestScope.selectedCategoryId ? requestScope.selectedCategoryId : ''}"/>
<c:set var="selectedStatus" value="${not empty requestScope.selectedStatus ? requestScope.selectedStatus : ''}"/>

<!-- Content -->
<!-- Page header -->
            <div class="row mb-4">
                <div class="col-md-6">
                    <h4 class="fw-bold">Products</h4>
                    <p class="text-muted">Manage product inventory</p>
                </div>
                <div class="col-md-6 text-end">
                    <c:if test="${userRole eq 'Admin' || userRole eq 'Manager'}">
                    <a href="${pageContext.request.contextPath}/product?action=create" class="btn btn-primary">
                        <span class="tf-icons bx bx-plus"></span> Add Product
                    </a>
                    </c:if>
                </div>
            </div>

            <!-- Alerts -->
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

            <!-- Filters -->
            <div class="card mb-4">
                <div class="card-body">
                    <form method="get" action="${pageContext.request.contextPath}/product?action=list" class="row g-3">
                        <div class="col-md-4">
                            <label for="search" class="form-label">Search</label>
                            <input type="text" id="search" name="search" class="form-control" 
                                   placeholder="Search by SKU or name" value="${search}" />
                        </div>
                        <div class="col-md-3">
                            <label for="categoryId" class="form-label">Category</label>
                            <select id="categoryId" name="categoryId" class="form-select">
                                <option value="">All Categories</option>
                                <c:forEach var="category" items="${categories}">
                                    <option value="${category.id}" ${selectedCategoryId eq category.id ? 'selected' : ''}>
                                        ${category.name}
                                    </option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="col-md-3">
                            <label for="status" class="form-label">Status</label>
                            <select id="status" name="status" class="form-select">
                                <option value="">All Products</option>
                                <option value="active" ${selectedStatus eq 'active' ? 'selected' : ''}>Active</option>
                                <option value="inactive" ${selectedStatus eq 'inactive' ? 'selected' : ''}>Inactive</option>
                            </select>
                        </div>
                        <div class="col-md-2">
                            <label>&nbsp;</label>
                            <button type="submit" class="btn btn-primary w-100">
                                <span class="tf-icons bx bx-search"></span> Filter
                            </button>
                        </div>
                    </form>
                </div>
            </div>

            <!-- Products Table -->
            <div class="card">
                <div class="table-responsive text-nowrap">
                    <table class="table table-hover">
                        <thead class="table-light">
                            <tr>
                                <th>SKU</th>
                                <th>Name</th>
                                <th>Category</th>
                                <th>Unit</th>
                                <th>Stock</th>
                                <th>Status</th>
                                <c:if test="${userRole eq 'Admin' || userRole eq 'Manager'}">
                                <th>Actions</th>
                                </c:if>
                            </tr>
                        </thead>
                        <tbody class="table-border-bottom-0">
                            <c:choose>
                                <c:when test="${empty products}">
                                    <tr>
                                        <c:set var="colspan" value="${userRole eq 'Admin' || userRole eq 'Manager' ? '7' : '6'}"/>
                                        <td colspan="${colspan}" class="text-center text-muted py-4">
                                            No products found
                                        </td>
                                    </tr>
                                </c:when>
                                <c:otherwise>
                                    <c:forEach var="product" items="${products}">
                                        <tr style="${product.status == 'Inactive' ? 'opacity: 0.6;' : ''}">
                                            <td>
                                                <strong>${product.sku}</strong>
                                            </td>
                                            <td>
                                                <a href="${pageContext.request.contextPath}/product?action=details&id=${product.id}">
                                                    ${product.name}
                                                </a>
                                            </td>
                                            <td>
                                                <c:forEach var="category" items="${categories}">
                                                    <c:if test="${category.id == product.categoryId}">
                                                        ${category.name}
                                                    </c:if>
                                                </c:forEach>
                                            </td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${empty product.unit}">
                                                        <span class="text-muted">-</span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        ${product.unit}
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>
                                                <span class="badge bg-info">
                                                    ${requestScope["totalStock_".concat(product.id)]}
                                                </span>
                                            </td>
                                            <td>
                                                <span class="badge bg-${product.status == 'Active' ? 'success' : 'secondary'}">
                                                    ${product.status}
                                                </span>
                                            </td>
                                            <c:if test="${userRole eq 'Admin' || userRole eq 'Manager'}">
                                            <td>
                                                <div class="dropdown">
                                                    <button type="button" class="btn p-0 dropdown-toggle hide-arrow" data-bs-toggle="dropdown">
                                                        <i class="bx bx-dots-vertical-rounded"></i>
                                                    </button>
                                                    <div class="dropdown-menu">
                                                        <a class="dropdown-item" href="${pageContext.request.contextPath}/product?action=edit&id=${product.id}">
                                                            <i class="bx bx-edit-alt me-1"></i> Edit
                                                        </a>
                                                        <form method="post" style="display:inline;" 
                                                              onsubmit="return confirm('Toggle product status?');">
                                                            <input type="hidden" name="action" value="toggleStatus" />
                                                            <input type="hidden" name="id" value="${product.id}" />
                                                            <button type="submit" class="dropdown-item">
                                                                <i class="bx bx-toggle-left me-1"></i> Toggle Status
                                                            </button>
                                                        </form>
                                                        <form method="post" style="display:inline;" 
                                                              onsubmit="return confirm('Delete this product?');">
                                                            <input type="hidden" name="action" value="delete" />
                                                            <input type="hidden" name="id" value="${product.id}" />
                                                            <button type="submit" class="dropdown-item text-danger">
                                                                <i class="bx bx-trash me-1"></i> Delete
                                                            </button>
                                                        </form>
                                                    </div>
                                                </div>
                                            </td>
                                            </c:if>
                                        </tr>
                                    </c:forEach>
                                </c:otherwise>
                            </c:choose>
                        </tbody>
                    </table>
                </div>
            </div>
<!-- / Content -->
