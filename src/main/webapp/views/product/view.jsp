<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:set var="pageTitle" value="View Product" scope="request"/>
<c:set var="currentPage" value="products" scope="request"/>
<jsp:include page="../layout/header.jsp"/>
<jsp:include page="../layout/sidebar.jsp"/>

<!-- Layout container -->
<div class="layout-page">
    <jsp:include page="../layout/navbar.jsp"/>

    <!-- Content wrapper -->
    <div class="content-wrapper">
        <!-- Content -->
        <div class="container-xxl flex-grow-1 container-p-y">
            <h4 class="fw-bold mb-4"><span class="text-muted fw-light">Products /</span> Product Details</h4>

            <div class="row">
                <div class="col-md-4 col-12">
                    <div class="card mb-4">
                        <div class="card-body">
                            <c:choose>
                                <c:when test="${not empty product.imageUrl}">
                                    <img src="${product.imageUrl}" alt="${product.name}" class="img-fluid rounded" />
                                </c:when>
                                <c:otherwise>
                                    <div class="text-center py-5 bg-label-secondary rounded">
                                        <i class="bx bx-image-alt bx-lg text-muted"></i>
                                        <p class="mt-2 text-muted">No Image</p>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>

                <div class="col-md-8 col-12">
                    <div class="card mb-4">
                        <div class="card-header">
                            <h5 class="mb-0">Product Information</h5>
                        </div>
                        <div class="card-body">
                            <dl class="row mb-0">
                                <dt class="col-sm-3 fw-bold">Product ID</dt>
                                <dd class="col-sm-9">#${product.id}</dd>

                                <dt class="col-sm-3 fw-bold">Name</dt>
                                <dd class="col-sm-9">${product.name}</dd>

                                <dt class="col-sm-3 fw-bold">Description</dt>
                                <dd class="col-sm-9">
                                    <c:choose>
                                        <c:when test="${not empty product.description}">
                                            ${product.description}
                                        </c:when>
                                        <c:otherwise>
                                            <em class="text-muted">No description</em>
                                        </c:otherwise>
                                    </c:choose>
                                </dd>

                                <dt class="col-sm-3 fw-bold">Price</dt>
                                <dd class="col-sm-9">
                                    <span class="h5 text-primary">
                                        <fmt:formatNumber value="${product.price}" type="currency" currencySymbol="$"/>
                                    </span>
                                </dd>

                                <dt class="col-sm-3 fw-bold">Quantity</dt>
                                <dd class="col-sm-9">
                                    <span class="badge ${product.quantity > 10 ? 'bg-success' : product.quantity > 0 ? 'bg-warning' : 'bg-danger'}">
                                        ${product.quantity} units
                                    </span>
                                </dd>

                                <dt class="col-sm-3 fw-bold">Category</dt>
                                <dd class="col-sm-9">
                                    <c:choose>
                                        <c:when test="${not empty product.category}">
                                            <span class="badge bg-label-info">${product.category}</span>
                                        </c:when>
                                        <c:otherwise>
                                            <em class="text-muted">No category</em>
                                        </c:otherwise>
                                    </c:choose>
                                </dd>

                                <dt class="col-sm-3 fw-bold">Status</dt>
                                <dd class="col-sm-9">
                                    <c:choose>
                                        <c:when test="${product.active}">
                                            <span class="badge bg-label-success">Active</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge bg-label-secondary">Inactive</span>
                                        </c:otherwise>
                                    </c:choose>
                                </dd>

                                <dt class="col-sm-3 fw-bold">Created Date</dt>
                                <dd class="col-sm-9">
                                    <c:choose>
                                        <c:when test="${not empty product.createdAt}">
                                            <fmt:formatDate value="${product.createdAt}" pattern="MMM dd, yyyy HH:mm"/>
                                        </c:when>
                                        <c:otherwise>
                                            <em class="text-muted">N/A</em>
                                        </c:otherwise>
                                    </c:choose>
                                </dd>

                                <dt class="col-sm-3 fw-bold">Last Updated</dt>
                                <dd class="col-sm-9">
                                    <c:choose>
                                        <c:when test="${not empty product.updatedAt}">
                                            <fmt:formatDate value="${product.updatedAt}" pattern="MMM dd, yyyy HH:mm"/>
                                        </c:when>
                                        <c:otherwise>
                                            <em class="text-muted">N/A</em>
                                        </c:otherwise>
                                    </c:choose>
                                </dd>
                            </dl>
                        </div>
                        <div class="card-footer">
                            <a href="${pageContext.request.contextPath}/product?action=edit&id=${product.id}" class="btn btn-primary me-2">
                                <i class="bx bx-edit-alt me-1"></i> Edit
                            </a>
                            <a href="${pageContext.request.contextPath}/product" class="btn btn-label-secondary">
                                <i class="bx bx-arrow-back me-1"></i> Back to List
                            </a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <!-- / Content -->

        <jsp:include page="../layout/footer.jsp"/>
