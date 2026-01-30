<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="pageTitle" value="Create Product" scope="request"/>
<c:set var="currentPage" value="product-create" scope="request"/>
<jsp:include page="../layout/header.jsp"/>
<jsp:include page="../layout/sidebar.jsp"/>

<!-- Layout container -->
<div class="layout-page">
    <jsp:include page="../layout/navbar.jsp"/>

    <!-- Content wrapper -->
    <div class="content-wrapper">
        <!-- Content -->
        <div class="container-xxl flex-grow-1 container-p-y">
            <h4 class="fw-bold mb-4"><span class="text-muted fw-light">Products /</span> Create New Product</h4>

            <div class="row">
                <div class="col-md-12">
                    <div class="card mb-4">
                        <div class="card-header d-flex justify-content-between align-items-center">
                            <h5 class="mb-0">Product Information</h5>
                            <small class="text-muted float-end">Fill in the details</small>
                        </div>
                        <div class="card-body">
                            <c:if test="${error != null}">
                                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                                    <strong>Error!</strong> ${error}
                                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                                </div>
                            </c:if>

                            <form action="${pageContext.request.contextPath}/product" method="post">
                                <input type="hidden" name="action" value="create">
                                
                                <div class="mb-3">
                                    <label class="form-label" for="name">Product Name <span class="text-danger">*</span></label>
                                    <input type="text" class="form-control" id="name" name="name" 
                                           placeholder="Enter product name" value="${product.name}" required />
                                    <div class="form-text">Enter a unique and descriptive product name</div>
                                </div>

                                <div class="mb-3">
                                    <label class="form-label" for="description">Description</label>
                                    <textarea id="description" name="description" class="form-control" 
                                              placeholder="Product description" rows="3">${product.description}</textarea>
                                </div>

                                <div class="row">
                                    <div class="col-md-6 mb-3">
                                        <label class="form-label" for="price">Price <span class="text-danger">*</span></label>
                                        <div class="input-group">
                                            <span class="input-group-text">$</span>
                                            <input type="number" class="form-control" id="price" name="price" 
                                                   step="0.01" min="0" placeholder="0.00" value="${product.price}" required />
                                        </div>
                                    </div>

                                    <div class="col-md-6 mb-3">
                                        <label class="form-label" for="quantity">Quantity <span class="text-danger">*</span></label>
                                        <input type="number" class="form-control" id="quantity" name="quantity" 
                                               min="0" placeholder="0" value="${product.quantity}" required />
                                    </div>
                                </div>

                                <div class="mb-3">
                                    <label class="form-label" for="category">Category</label>
                                    <input type="text" class="form-control" id="category" name="category" 
                                           placeholder="e.g., Electronics, Clothing, Books" value="${product.category}" />
                                </div>

                                <div class="mb-3">
                                    <label class="form-label" for="imageUrl">Image URL</label>
                                    <input type="text" class="form-control" id="imageUrl" name="imageUrl" 
                                           placeholder="https://example.com/image.jpg" value="${product.imageUrl}" />
                                    <div class="form-text">Provide a direct link to the product image</div>
                                </div>

                                <div class="pt-4">
                                    <button type="submit" class="btn btn-primary me-sm-3 me-1">
                                        <i class="bx bx-save me-1"></i> Create Product
                                    </button>
                                    <a href="${pageContext.request.contextPath}/product" class="btn btn-label-secondary">
                                        <i class="bx bx-x me-1"></i> Cancel
                                    </a>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <!-- / Content -->

        <jsp:include page="../layout/footer.jsp"/>
