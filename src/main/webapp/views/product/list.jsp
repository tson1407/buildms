<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:set var="pageTitle" value="Product List" scope="request"/>
<c:set var="currentPage" value="product-list" scope="request"/>
<jsp:include page="../layout/header.jsp"/>
<jsp:include page="../layout/sidebar.jsp"/>

<!-- Layout container -->
<div class="layout-page">
    <jsp:include page="../layout/navbar.jsp"/>

    <!-- Content wrapper -->
    <div class="content-wrapper">
        <!-- Content -->
        <div class="container-xxl flex-grow-1 container-p-y">
            <div class="d-flex justify-content-between align-items-center mb-4">
                <h4 class="fw-bold"><span class="text-muted fw-light">Management /</span> Product List</h4>
                <a href="${pageContext.request.contextPath}/product?action=create" class="btn btn-primary">
                    <i class="bx bx-plus me-1"></i> Add New Product
                </a>
            </div>

            <!-- Alerts -->
            <c:if test="${param.success == 'created'}">
                <div class="alert alert-success alert-dismissible fade show" role="alert">
                    <strong>Success!</strong> Product created successfully!
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
            </c:if>
            <c:if test="${param.success == 'updated'}">
                <div class="alert alert-success alert-dismissible fade show" role="alert">
                    <strong>Success!</strong> Product updated successfully!
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
            </c:if>
            <c:if test="${param.success == 'deleted'}">
                <div class="alert alert-success alert-dismissible fade show" role="alert">
                    <strong>Success!</strong> Product deleted successfully!
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
            </c:if>
            <c:if test="${param.error != null}">
                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                    <strong>Error!</strong> ${param.error}
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
            </c:if>

            <!-- Product List Card -->
            <div class="card">
                <div class="card-header d-flex justify-content-between align-items-center">
                    <h5 class="mb-0">Products</h5>
                    <form action="${pageContext.request.contextPath}/product" method="get" class="d-flex gap-2">
                        <input type="hidden" name="action" value="search">
                        <input type="text" name="keyword" class="form-control" placeholder="Search products..." value="${keyword}" style="width: 300px;">
                        <button type="submit" class="btn btn-primary">
                            <i class="bx bx-search"></i> Search
                        </button>
                        <a href="${pageContext.request.contextPath}/product" class="btn btn-secondary">
                            <i class="bx bx-reset"></i> Reset
                        </a>
                    </form>
                </div>
                <div class="card-body">
                    <c:choose>
                        <c:when test="${empty products}">
                            <div class="text-center py-5">
                                <i class="bx bx-package bx-lg text-muted"></i>
                                <p class="mt-2 text-muted">No products found.</p>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="table-responsive text-nowrap">
                                <table class="table table-hover">
                                    <thead>
                                        <tr>
                                            <th>ID</th>
                                            <th>Name</th>
                                            <th>Category</th>
                                            <th>Price</th>
                                            <th>Quantity</th>
                                            <th>Status</th>
                                            <th>Actions</th>
                                        </tr>
                                    </thead>
                                    <tbody class="table-border-bottom-0">
                                        <c:forEach var="product" items="${products}">
                                            <tr>
                                                <td><strong>#${product.id}</strong></td>
                                                <td>${product.name}</td>
                                                <td><span class="badge bg-label-info">${product.category}</span></td>
                                                <td><fmt:formatNumber value="${product.price}" type="currency" currencySymbol="$"/></td>
                                                <td>
                                                    <span class="badge ${product.quantity > 10 ? 'bg-label-success' : product.quantity > 0 ? 'bg-label-warning' : 'bg-label-danger'}">
                                                        ${product.quantity}
                                                    </span>
                                                </td>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${product.active}">
                                                            <span class="badge bg-label-success">Active</span>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <span class="badge bg-label-secondary">Inactive</span>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td>
                                                    <div class="dropdown">
                                                        <button type="button" class="btn p-0 dropdown-toggle hide-arrow" data-bs-toggle="dropdown">
                                                            <i class="bx bx-dots-vertical-rounded"></i>
                                                        </button>
                                                        <div class="dropdown-menu">
                                                            <a class="dropdown-item" href="${pageContext.request.contextPath}/product?action=view&id=${product.id}">
                                                                <i class="bx bx-show me-1"></i> View
                                                            </a>
                                                            <a class="dropdown-item" href="${pageContext.request.contextPath}/product?action=edit&id=${product.id}">
                                                                <i class="bx bx-edit-alt me-1"></i> Edit
                                                            </a>
                                                            <a class="dropdown-item text-danger" href="${pageContext.request.contextPath}/product?action=delete&id=${product.id}" 
                                                               onclick="return confirm('Are you sure you want to delete this product?')">
                                                                <i class="bx bx-trash me-1"></i> Delete
                                                            </a>
                                                        </div>
                                                    </div>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
        <!-- / Content -->

        <jsp:include page="../layout/footer.jsp"/>
