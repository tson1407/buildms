<%@ page contentType="text/html;charset=UTF-8"  %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:if test="${empty sessionScope.user}">
    <c:redirect url="${pageContext.request.contextPath}/auth?action=login"/>
</c:if>
<c:set var="userRole" value="${sessionScope.user.role}"/>

<!-- Content -->
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
                    <c:if test="${userRole eq 'Admin' || userRole eq 'Manager'}">
                    <a href="${pageContext.request.contextPath}/product?action=edit&id=${product.id}" class="btn btn-primary">
                        <span class="tf-icons bx bx-edit-alt"></span> Edit
                    </a>
                    </c:if>
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
                                        <span class="badge bg-${product.status == 'Active' ? 'success' : 'secondary'}">
                                            ${product.status}
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
            <c:if test="${userRole eq 'Admin' || userRole eq 'Manager' || userRole eq 'Staff'}">
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
            </c:if>
<!-- / Content -->

