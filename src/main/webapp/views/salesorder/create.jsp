<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="pageTitle" value="Create Sales Order" scope="request"/>
<c:set var="currentPage" value="salesorder-create" scope="request"/>
<jsp:include page="../layout/header.jsp"/>
<jsp:include page="../layout/sidebar.jsp"/>

<!-- Layout container -->
<div class="layout-page">
    <jsp:include page="../layout/navbar.jsp"/>

    <!-- Content wrapper -->
    <div class="content-wrapper">
        <!-- Content -->
        <div class="container-xxl flex-grow-1 container-p-y">
            <h4 class="fw-bold mb-4"><span class="text-muted fw-light">Sales / Sales Orders /</span> Create</h4>

            <!-- Error Message -->
            <c:if test="${error != null}">
                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                    <strong>Error!</strong> ${error}
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
            </c:if>

            <!-- Create Form -->
            <div class="card mb-4">
                <div class="card-header d-flex justify-content-between align-items-center">
                    <h5 class="mb-0">Sales Order Information</h5>
                </div>
                <div class="card-body">
                    <form action="${pageContext.request.contextPath}/salesorder?action=create" method="post" id="salesOrderForm">
                        <!-- Customer Selection -->
                        <div class="mb-3">
                            <label for="customerId" class="form-label">Customer <span class="text-danger">*</span></label>
                            <select class="form-select" id="customerId" name="customerId" required>
                                <option value="">Select Customer</option>
                                <c:forEach var="customer" items="${customers}">
                                    <option value="${customer.id}">${customer.name} (${customer.code})</option>
                                </c:forEach>
                            </select>
                        </div>

                        <!-- Order Items -->
                        <div class="mb-3">
                            <label class="form-label">Order Items <span class="text-danger">*</span></label>
                            <div id="orderItemsContainer">
                                <!-- Items will be added here dynamically -->
                            </div>
                            <button type="button" class="btn btn-sm btn-secondary mt-2" onclick="addOrderItem()">
                                <i class="bx bx-plus me-1"></i> Add Item
                            </button>
                        </div>

                        <!-- Form Actions -->
                        <div class="d-flex gap-2">
                            <button type="submit" class="btn btn-primary">
                                <i class="bx bx-save me-1"></i> Create Order
                            </button>
                            <a href="${pageContext.request.contextPath}/salesorder" class="btn btn-secondary">
                                <i class="bx bx-x me-1"></i> Cancel
                            </a>
                        </div>
                    </form>
                </div>
            </div>
        </div>
        <!-- / Content -->

        <jsp:include page="../layout/footer.jsp"/>
        <div class="content-backdrop fade"></div>
    </div>
    <!-- Content wrapper -->
</div>
<!-- / Layout page -->

<!-- Hidden product data for JavaScript -->
<script>
    const products = [
        <c:forEach var="product" items="${products}" varStatus="status">
        {
            id: ${product.id},
            name: "${product.name}",
            sku: "${product.sku}",
            unit: "${product.unit}"
        }<c:if test="${!status.last}">,</c:if>
        </c:forEach>
    ];

    let itemCount = 0;

    function addOrderItem() {
        itemCount++;
        const container = document.getElementById('orderItemsContainer');
        const itemDiv = document.createElement('div');
        itemDiv.className = 'row mb-2 align-items-end';
        itemDiv.id = 'item-' + itemCount;
        
        itemDiv.innerHTML = `
            <div class="col-md-6">
                <label class="form-label">Product</label>
                <select class="form-select" name="productId[]" required>
                    <option value="">Select Product</option>
                    ${products.map(p => `<option value="${p.id}">${p.name} (${p.sku})</option>`).join('')}
                </select>
            </div>
            <div class="col-md-4">
                <label class="form-label">Quantity</label>
                <input type="number" class="form-control" name="quantity[]" min="1" required placeholder="Enter quantity">
            </div>
            <div class="col-md-2">
                <button type="button" class="btn btn-danger" onclick="removeOrderItem(${itemCount})">
                    <i class="bx bx-trash"></i>
                </button>
            </div>
        `;
        
        container.appendChild(itemDiv);
    }

    function removeOrderItem(id) {
        const item = document.getElementById('item-' + id);
        if (item) {
            item.remove();
        }
    }

    // Add first item on page load
    document.addEventListener('DOMContentLoaded', function() {
        addOrderItem();
    });

    // Form validation
    document.getElementById('salesOrderForm').addEventListener('submit', function(e) {
        const items = document.querySelectorAll('#orderItemsContainer .row');
        if (items.length === 0) {
            e.preventDefault();
            alert('Please add at least one item to the order');
            return false;
        }

        // Check for duplicate products
        const productIds = [];
        const productSelects = document.querySelectorAll('select[name="productId[]"]');
        
        for (let select of productSelects) {
            const productId = select.value;
            if (productId && productIds.includes(productId)) {
                e.preventDefault();
                alert('Duplicate products are not allowed in the same order');
                return false;
            }
            if (productId) {
                productIds.push(productId);
            }
        }

        return true;
    });
</script>
