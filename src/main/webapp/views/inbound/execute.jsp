<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:set var="pageTitle" value="Execute Inbound Request" scope="request"/>
<c:set var="currentPage" value="inbound-execute" scope="request"/>
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
                <h4 class="fw-bold"><span class="text-muted fw-light">Inbound /</span> Execute Request #${request.id}</h4>
                <a href="${pageContext.request.contextPath}/inbound" class="btn btn-secondary">
                    <i class="bx bx-arrow-back"></i> Back to List
                </a>
            </div>

            <!-- Alerts -->
            <c:if test="${not empty error}">
                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                    <strong>Error!</strong> ${error}
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
            </c:if>
            <c:if test="${param.success == 'started'}">
                <div class="alert alert-success alert-dismissible fade show" role="alert">
                    <strong>Success!</strong> Request execution started!
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
            </c:if>

            <!-- Request Details Card -->
            <div class="card mb-4">
                <div class="card-header d-flex justify-content-between align-items-center">
                    <h5 class="mb-0">Request Information</h5>
                    <c:choose>
                        <c:when test="${request.status == 'Approved'}">
                            <span class="badge bg-label-info">Approved</span>
                        </c:when>
                        <c:when test="${request.status == 'InProgress'}">
                            <span class="badge bg-label-primary">In Progress</span>
                        </c:when>
                    </c:choose>
                </div>
                <div class="card-body">
                    <div class="row">
                        <div class="col-md-6 mb-3">
                            <label class="form-label text-muted small">Request ID</label>
                            <p class="mb-0"><strong>#${request.id}</strong></p>
                        </div>
                        <div class="col-md-6 mb-3">
                            <label class="form-label text-muted small">Status</label>
                            <p class="mb-0">${request.status}</p>
                        </div>
                        <div class="col-md-6 mb-3">
                            <label class="form-label text-muted small">Created Date</label>
                            <p class="mb-0"><fmt:formatDate value="${request.createdAt}" pattern="yyyy-MM-dd HH:mm:ss"/></p>
                        </div>
                        <div class="col-md-6 mb-3">
                            <label class="form-label text-muted small">Approved Date</label>
                            <p class="mb-0"><fmt:formatDate value="${request.approvedDate}" pattern="yyyy-MM-dd HH:mm:ss"/></p>
                        </div>
                        <c:if test="${not empty request.notes}">
                            <div class="col-md-12 mb-3">
                                <label class="form-label text-muted small">Notes</label>
                                <p class="mb-0">${request.notes}</p>
                            </div>
                        </c:if>
                    </div>
                </div>
            </div>

            <!-- Execution Form Card -->
            <div class="card">
                <div class="card-header">
                    <h5 class="mb-0">Receive Items</h5>
                </div>
                <div class="card-body">
                    <c:if test="${request.status == 'Approved'}">
                        <div class="alert alert-info mb-4">
                            <i class="bx bx-info-circle"></i>
                            Click "Start Execution" to begin receiving items for this request.
                        </div>
                        <form action="${pageContext.request.contextPath}/inbound?action=startExecution" method="post">
                            <input type="hidden" name="requestId" value="${request.id}">
                            <button type="submit" class="btn btn-primary">
                                <i class="bx bx-play"></i> Start Execution
                            </button>
                        </form>
                    </c:if>

                    <c:if test="${request.status == 'InProgress'}">
                        <form action="${pageContext.request.contextPath}/inbound?action=completeExecution" 
                              method="post" id="executionForm">
                            <input type="hidden" name="requestId" value="${request.id}">
                            
                            <div class="table-responsive">
                                <table class="table table-bordered">
                                    <thead>
                                        <tr>
                                            <th>Product ID</th>
                                            <th>Expected Quantity</th>
                                            <th>Received Quantity <span class="text-danger">*</span></th>
                                            <th>Target Location</th>
                                            <th>Notes</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="item" items="${items}" varStatus="status">
                                            <tr>
                                                <td>
                                                    <strong>#${item.productId}</strong>
                                                    <input type="hidden" name="productId[]" value="${item.productId}">
                                                </td>
                                                <td>
                                                    <span class="badge bg-label-info">${item.quantity}</span>
                                                </td>
                                                <td>
                                                    <input type="number" class="form-control" 
                                                           name="receivedQuantity[]" 
                                                           min="0" 
                                                           value="${item.quantity}"
                                                           required
                                                           onchange="checkVariance(this, ${item.quantity})">
                                                    <small class="text-muted variance-msg"></small>
                                                </td>
                                                <td>
                                                    <select class="form-select" name="locationId[]">
                                                        <option value="">-- Select Location --</option>
                                                        <c:forEach var="location" items="${locations}">
                                                            <option value="${location.id}" 
                                                                    ${item.locationId == location.id ? 'selected' : ''}>
                                                                ${location.code} (${location.type})
                                                            </option>
                                                        </c:forEach>
                                                    </select>
                                                </td>
                                                <td>
                                                    <input type="text" class="form-control" 
                                                           name="itemNotes[]" 
                                                           placeholder="Optional notes">
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>

                            <div class="alert alert-warning mt-3">
                                <i class="bx bx-info-circle"></i>
                                <strong>Important:</strong> Received quantity must be >= 0. If quantity differs from expected, 
                                please add notes explaining the discrepancy.
                            </div>

                            <div class="mt-4">
                                <button type="submit" class="btn btn-success me-2">
                                    <i class="bx bx-check-circle"></i> Complete Execution
                                </button>
                                <a href="${pageContext.request.contextPath}/inbound?action=view&id=${request.id}" 
                                   class="btn btn-secondary">
                                    <i class="bx bx-x"></i> Cancel
                                </a>
                            </div>
                        </form>
                    </c:if>
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
// Check for variance between expected and received quantities
function checkVariance(input, expectedQty) {
    const receivedQty = parseInt(input.value);
    const varianceMsg = input.parentElement.querySelector('.variance-msg');
    
    if (receivedQty !== expectedQty) {
        const variance = receivedQty - expectedQty;
        if (variance > 0) {
            varianceMsg.textContent = `⚠️ Over by ${variance}`;
            varianceMsg.className = 'text-warning variance-msg';
        } else {
            varianceMsg.textContent = `⚠️ Short by ${Math.abs(variance)}`;
            varianceMsg.className = 'text-danger variance-msg';
        }
    } else {
        varianceMsg.textContent = '✓ Match';
        varianceMsg.className = 'text-success variance-msg';
    }
}

// Form validation
document.getElementById('executionForm')?.addEventListener('submit', function(e) {
    const receivedQtys = document.querySelectorAll('input[name="receivedQuantity[]"]');
    let hasVariance = false;
    
    for (let input of receivedQtys) {
        const receivedQty = parseInt(input.value);
        if (receivedQty < 0) {
            e.preventDefault();
            alert('Received quantity cannot be negative');
            return false;
        }
    }
    
    // Check if all quantities are 0
    let allZero = true;
    for (let input of receivedQtys) {
        if (parseInt(input.value) > 0) {
            allZero = false;
            break;
        }
    }
    
    if (allZero) {
        e.preventDefault();
        alert('At least one item must have a received quantity greater than 0');
        return false;
    }
    
    return confirm('Are you sure you want to complete this execution? Inventory will be updated.');
});

// Initialize variance messages on load
document.addEventListener('DOMContentLoaded', function() {
    const inputs = document.querySelectorAll('input[name="receivedQuantity[]"]');
    inputs.forEach(input => {
        const expectedQty = parseInt(input.getAttribute('value'));
        checkVariance(input, expectedQty);
    });
});
</script>

<jsp:include page="../layout/header.jsp">
    <jsp:param name="includeFooter" value="true"/>
</jsp:include>
