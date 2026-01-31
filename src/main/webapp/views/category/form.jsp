<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:if test="${empty sessionScope.user}">
    <c:redirect url="${pageContext.request.contextPath}/auth?action=login"/>
</c:if>

<c:set var="category" value="${requestScope.category}"/>
<c:set var="error" value="${requestScope.error}"/>
<c:set var="isEdit" value="${not empty category}"/>
<c:set var="pageTitle" value="${isEdit ? 'Edit Category' : 'Create Category'}"/>
<c:set var="formAction" value="${isEdit ? 'update' : 'save'}"/>

<c:set var="name" value="${not empty requestScope.name ? requestScope.name : (isEdit ? category.name : '')}"/>
<c:set var="description" value="${not empty requestScope.description ? requestScope.description : (isEdit ? category.description : '')}"/>

<!-- Content -->
<!-- Page header -->
            <div class="row mb-4">
                <div class="col-md-12">
                    <h4 class="fw-bold">${pageTitle}</h4>
                    <p class="text-muted">${isEdit ? 'Update category information' : 'Create a new product category'}</p>
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

                            <form method="post" action="${pageContext.request.contextPath}/category">
                                <input type="hidden" name="action" value="${formAction}" />
                                <c:if test="${isEdit}">
                                <input type="hidden" name="id" value="${category.id}" />
                                </c:if>

                                <!-- Category Name -->
                                <div class="mb-4">
                                    <label for="name" class="form-label">Category Name <span class="text-danger">*</span></label>
                                    <input type="text" class="form-control" id="name" name="name" 
                                           placeholder="Enter category name" value="${name}"
                                           maxlength="255" required />
                                    <small class="form-text text-muted">Maximum 255 characters</small>
                                </div>

                                <!-- Category Description -->
                                <div class="mb-4">
                                    <label for="description" class="form-label">Description</label>
                                    <textarea class="form-control" id="description" name="description" 
                                              placeholder="Enter category description (optional)" 
                                              rows="4" maxlength="500">${description}</textarea>
                                    <small class="form-text text-muted">Maximum 500 characters</small>
                                </div>

                                <!-- Buttons -->
                                <div class="mb-3">
                                    <button type="submit" class="btn btn-primary">
                                        <span class="tf-icons bx bx-save"></span> ${isEdit ? 'Update' : 'Create'}
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
                            <c:if test="${isEdit}">
                            <div class="mb-3">
                                <h6 class="mb-2">Category ID</h6>
                                <p class="text-muted small">${category.id}</p>
                            </div>
                            </c:if>
                        </div>
                    </div>
                </div>
            </div>
<!-- / Content -->
