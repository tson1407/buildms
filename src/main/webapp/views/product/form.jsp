<%@ page contentType="text/html;charset=UTF-8"  %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:if test="${empty sessionScope.user}">
    <c:redirect url="${pageContext.request.contextPath}/auth?action=login"/>
</c:if>

<c:set var="product" value="${requestScope.product}"/>
<c:set var="error" value="${requestScope.error}"/>
<c:set var="isEdit" value="${not empty product}"/>
<c:set var="pageTitle" value="${isEdit ? 'Edit Product' : 'Create Product'}"/>
<c:set var="formAction" value="${isEdit ? 'update' : 'save'}"/>

<c:set var="sku" value="${not empty requestScope.sku ? requestScope.sku : (isEdit ? product.sku : '')}"/>
<c:set var="name" value="${not empty requestScope.name ? requestScope.name : (isEdit ? product.name : '')}"/>
<c:set var="unit" value="${not empty requestScope.unit ? requestScope.unit : (isEdit ? product.unit : '')}"/>
<c:set var="categoryId" value="${not empty requestScope.categoryId ? requestScope.categoryId : (isEdit ? product.categoryId : '')}"/>

<!-- Content -->
<!-- Page header -->
            <div class="row mb-4">
                <div class="col-md-12">
                    <h4 class="fw-bold">${pageTitle}</h4>
                    <p class="text-muted">${isEdit ? 'Update product information' : 'Create a new product'}</p>
                </div>
            </div>

            <!-- Form Card -->
            <div class="row">
                <div class="col-md-8">
                    <div class="card">
                        <div class="card-body">
                            <c:if test="${not empty error}">
                            <div class="alert alert-danger alert-dismissible" role="alert">
                                ${error}
                                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                            </div>
                            </c:if>

                            <form method="post" action="${pageContext.request.contextPath}/product">
                                <input type="hidden" name="action" value="${formAction}" />
                                <c:if test="${isEdit}">
                                <input type="hidden" name="id" value="${product.id}" />
                                </c:if>

                                <!-- SKU -->
                                <div class="mb-4">
                                    <label for="sku" class="form-label">SKU <span class="text-danger">*</span></label>
                                    <input type="text" class="form-control" id="sku" name="sku" 
                                           placeholder="e.g., PROD-001" value="${sku}"
                                           maxlength="100" required />
                                    <small class="form-text text-muted">Alphanumeric with hyphens. Maximum 100 characters. Must be unique.</small>
                                </div>

                                <!-- Product Name -->
                                <div class="mb-4">
                                    <label for="name" class="form-label">Product Name <span class="text-danger">*</span></label>
                                    <input type="text" class="form-control" id="name" name="name" 
                                           placeholder="Enter product name" value="${name}"
                                           maxlength="255" required />
                                    <small class="form-text text-muted">Maximum 255 characters</small>
                                </div>

                                <!-- Unit of Measure -->
                                <div class="mb-4">
                                    <label for="unit" class="form-label">Unit of Measure</label>
                                    <select id="unit" name="unit" class="form-select">
                                        <option value="">Select a unit</option>
                                        <option value="pcs" ${unit eq 'pcs' ? 'selected' : ''}>Pieces (pcs)</option>
                                        <option value="kg" ${unit eq 'kg' ? 'selected' : ''}>Kilogram (kg)</option>
                                        <option value="box" ${unit eq 'box' ? 'selected' : ''}>Box</option>
                                        <option value="carton" ${unit eq 'carton' ? 'selected' : ''}>Carton</option>
                                        <option value="liter" ${unit eq 'liter' ? 'selected' : ''}>Liter</option>
                                        <option value="meter" ${unit eq 'meter' ? 'selected' : ''}>Meter</option>
                                        <option value="unit" ${unit eq 'unit' ? 'selected' : ''}>Unit</option>
                                    </select>
                                    <small class="form-text text-muted">Common units are provided, or enter a custom value</small>
                                </div>

                                <!-- Category -->
                                <div class="mb-4">
                                    <label for="categoryId" class="form-label">Category <span class="text-danger">*</span></label>
                                    <select id="categoryId" name="categoryId" class="form-select" required>
                                        <option value="">Select a category</option>
                                        <c:forEach var="cat" items="${categories}">
                                            <option value="${cat.id}" ${categoryId eq cat.id ? 'selected' : ''}>
                                                ${cat.name}
                                            </option>
                                        </c:forEach>
                                    </select>
                                    <small class="form-text text-muted">Product must belong to one category</small>
                                </div>

                                <!-- Buttons -->
                                <div class="mb-3">
                                    <button type="submit" class="btn btn-primary">
                                        <span class="tf-icons bx bx-save"></span> ${isEdit ? 'Update' : 'Create'}
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
                            <c:if test="${isEdit}">
                            <div class="mb-3">
                                <h6 class="mb-2">Product ID</h6>
                                <p class="text-muted small">${product.id}</p>
                            </div>
                            <div class="mb-3">
                                <h6 class="mb-2">Status</h6>
                                <p class="text-muted small">
                                    <span class="badge bg-${product.status == 'Active' ? 'success' : 'secondary'}">
                                        ${product.status}
                                    </span>
                                </p>
                            </div>
                            </c:if>
                        </div>
                    </div>
                </div>
            </div>
<!-- / Content -->
