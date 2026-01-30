<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="pageTitle" value="Create Inbound Request" scope="request"/>
<c:set var="currentPage" value="inbound-create" scope="request"/>
<jsp:include page="../layout/header.jsp"/>
<jsp:include page="../layout/sidebar.jsp"/>

<!-- Layout container -->
<div class="layout-page">
    <jsp:include page="../layout/navbar.jsp"/>

    <!-- Content wrapper -->
    <div class="content-wrapper">
        <!-- Content -->
        <div class="container-xxl flex-grow-1 container-p-y">
            <h4 class="fw-bold mb-4"><span class="text-muted fw-light">Inbound /</span> Create Request</h4>

            <c:if test="${not empty error}">
                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                    <strong>Error!</strong> ${error}
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
            </c:if>

            <div class="card">
                <div class="card-header">
                    <h5 class="mb-0">Request Information</h5>
                </div>
                <div class="card-body">
                    <form action="${pageContext.request.contextPath}/inbound?action=create" method="post" id="inboundForm">
                        <div class="row mb-3">
                            <div class="col-md-6">
                                <label for="warehouseId" class="form-label">Destination Warehouse <span class="text-danger">*</span></label>
                                <select class="form-select" id="warehouseId" name="warehouseId" required>
                                    <option value="">-- Select Warehouse --</option>
                                    <c:forEach var="warehouse" items="${warehouses}">
                                        <option value="${warehouse.id}">${warehouse.name} - ${warehouse.location}</option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="col-md-6">
                                <label for="expectedDate" class="form-label">Expected Delivery Date</label>
                                <input type="date" class="form-control" id="expectedDate" name="expectedDate">
                            </div>
                        </div>

                        <div class="mb-3">
                            <label for="notes" class="form-label">Notes/Description</label>
                            <textarea class="form-control" id="notes" name="notes" rows="3" 
                                      placeholder="Enter any additional notes..."></textarea>
                        </div>

                        <hr class="my-4">

                        <div class="d-flex justify-content-between align-items-center mb-3">
                            <h5 class="mb-0">Request Items <span class="text-danger">*</span></h5>
                            <button type="button" class="btn btn-sm btn-primary" onclick="addItem()">
                                <i class="bx bx-plus"></i> Add Item
                            </button>
                        </div>

                        <div id="itemsContainer">
                            <!-- Items will be added here dynamically -->
                        </div>

                        <div class="mt-4">
                            <button type="submit" class="btn btn-primary me-2">
                                <i class="bx bx-check"></i> Submit Request
                            </button>
                            <a href="${pageContext.request.contextPath}/inbound" class="btn btn-secondary">
                                <i class="bx bx-x"></i> Cancel
                            </a>
                        </div>
                    </form>
                </div>
            </div>
        </div>
        <!-- / Content -->

        <jsp:include page="../layout/footer.jsp"/>
    </div>
    <!-- Content wrapper -->
</div>
<!-- / Layout page -->

<script>
let itemCount = 0;
const products = [
    <c:forEach var="product" items="${products}" varStatus="status">
        {id: ${product.id}, name: "${product.name}", sku: "${product.sku}"}<c:if test="${!status.last}">,</c:if>
    </c:forEach>
];

function addItem() {
    const container = document.getElementById('itemsContainer');
    const itemDiv = document.createElement('div');
    itemDiv.className = 'card mb-3';
    itemDiv.id = 'item-' + itemCount;
    
    itemDiv.innerHTML = `
        <div class="card-body">
            <div class="row">
                <div class="col-md-5">
                    <label class="form-label">Product <span class="text-danger">*</span></label>
                    <select class="form-select" name="productId[]" required>
                        <option value="">-- Select Product --</option>
                        ${products.map(p => '<option value="' + p.id + '">' + p.name + ' (' + p.sku + ')</option>').join('')}
                    </select>
                </div>
                <div class="col-md-3">
                    <label class="form-label">Quantity <span class="text-danger">*</span></label>
                    <input type="number" class="form-control" name="quantity[]" min="1" required placeholder="0">
                </div>
                <div class="col-md-3">
                    <label class="form-label">Target Location</label>
                    <input type="text" class="form-control" name="locationId[]" placeholder="Optional">
                </div>
                <div class="col-md-1 d-flex align-items-end">
                    <button type="button" class="btn btn-danger" onclick="removeItem(${itemCount})">
                        <i class="bx bx-trash"></i>
                    </button>
                </div>
            </div>
        </div>
    `;
    
    container.appendChild(itemDiv);
    itemCount++;
}

function removeItem(id) {
    const item = document.getElementById('item-' + id);
    if (item) {
        item.remove();
    }
}

// Add first item on load
document.addEventListener('DOMContentLoaded', function() {
    addItem();
});

// Form validation
document.getElementById('inboundForm').addEventListener('submit', function(e) {
    const items = document.querySelectorAll('#itemsContainer .card');
    if (items.length === 0) {
        e.preventDefault();
        alert('At least one item is required');
        return false;
    }
    
    // Check for duplicate products
    const selectedProducts = [];
    const productSelects = document.querySelectorAll('select[name="productId[]"]');
    
    for (let select of productSelects) {
        if (select.value) {
            if (selectedProducts.includes(select.value)) {
                e.preventDefault();
                alert('Product already exists in the request. Please select different products.');
                return false;
            }
            selectedProducts.push(select.value);
        }
    }
});
</script>

<jsp:include page="../layout/header.jsp">
    <jsp:param name="includeFooter" value="true"/>
</jsp:include>
