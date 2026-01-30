<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:set var="pageTitle" value="Approve Inbound Request" scope="request"/>
<c:set var="currentPage" value="inbound-approve" scope="request"/>
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
                <h4 class="fw-bold"><span class="text-muted fw-light">Inbound /</span> Approve Request #${request.id}</h4>
                <a href="${pageContext.request.contextPath}/inbound" class="btn btn-secondary">
                    <i class="bx bx-arrow-back"></i> Back to List
                </a>
            </div>

            <c:if test="${not empty error}">
                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                    <strong>Error!</strong> ${error}
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
            </c:if>

            <!-- Request Details Card -->
            <div class="card mb-4">
                <div class="card-header d-flex justify-content-between align-items-center">
                    <h5 class="mb-0">Request Information</h5>
                    <span class="badge bg-label-warning">${request.status}</span>
                </div>
                <div class="card-body">
                    <div class="row">
                        <div class="col-md-6 mb-3">
                            <label class="form-label text-muted small">Request ID</label>
                            <p class="mb-0"><strong>#${request.id}</strong></p>
                        </div>
                        <div class="col-md-6 mb-3">
                            <label class="form-label text-muted small">Type</label>
                            <p class="mb-0">${request.type}</p>
                        </div>
                        <div class="col-md-6 mb-3">
                            <label class="form-label text-muted small">Created Date</label>
                            <p class="mb-0"><fmt:formatDate value="${request.createdAt}" pattern="yyyy-MM-dd HH:mm:ss"/></p>
                        </div>
                        <div class="col-md-6 mb-3">
                            <label class="form-label text-muted small">Expected Delivery Date</label>
                            <p class="mb-0">
                                <c:choose>
                                    <c:when test="${request.expectedDate != null}">
                                        <fmt:formatDate value="${request.expectedDate}" pattern="yyyy-MM-dd"/>
                                    </c:when>
                                    <c:otherwise>-</c:otherwise>
                                </c:choose>
                            </p>
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

            <!-- Request Items Card -->
            <div class="card mb-4">
                <div class="card-header">
                    <h5 class="mb-0">Request Items</h5>
                </div>
                <div class="card-body">
                    <div class="table-responsive">
                        <table class="table table-bordered">
                            <thead>
                                <tr>
                                    <th>Product ID</th>
                                    <th>Quantity</th>
                                    <th>Target Location</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="item" items="${items}">
                                    <tr>
                                        <td><strong>#${item.productId}</strong></td>
                                        <td>${item.quantity}</td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${item.locationId != null}">
                                                    #${item.locationId}
                                                </c:when>
                                                <c:otherwise>-</c:otherwise>
                                            </c:choose>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>

            <!-- Approval Actions Card -->
            <div class="card">
                <div class="card-header">
                    <h5 class="mb-0">Approval Decision</h5>
                </div>
                <div class="card-body">
                    <div class="row">
                        <div class="col-md-6">
                            <!-- Approve Form -->
                            <form action="${pageContext.request.contextPath}/inbound?action=approve" method="post" 
                                  onsubmit="return confirm('Are you sure you want to approve this request?');">
                                <input type="hidden" name="requestId" value="${request.id}">
                                <button type="submit" class="btn btn-success btn-lg w-100">
                                    <i class="bx bx-check-circle"></i> Approve Request
                                </button>
                            </form>
                        </div>
                        <div class="col-md-6">
                            <!-- Reject Button -->
                            <button type="button" class="btn btn-danger btn-lg w-100" data-bs-toggle="modal" data-bs-target="#rejectModal">
                                <i class="bx bx-x-circle"></i> Reject Request
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <!-- / Content -->

        <jsp:include page="../layout/footer.jsp"/>
    </div>
    <!-- Content wrapper -->
</div>
<!-- / Layout page -->

<!-- Reject Modal -->
<div class="modal fade" id="rejectModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Reject Request</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <form action="${pageContext.request.contextPath}/inbound?action=reject" method="post" id="rejectForm">
                <div class="modal-body">
                    <input type="hidden" name="requestId" value="${request.id}">
                    <div class="mb-3">
                        <label for="rejectionReason" class="form-label">Rejection Reason <span class="text-danger">*</span></label>
                        <textarea class="form-control" id="rejectionReason" name="rejectionReason" rows="4" 
                                  required placeholder="Enter reason for rejection..."></textarea>
                        <div class="form-text">Please provide a clear reason for rejecting this request.</div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                    <button type="submit" class="btn btn-danger">
                        <i class="bx bx-x-circle"></i> Confirm Rejection
                    </button>
                </div>
            </form>
        </div>
    </div>
</div>

<script>
// Form validation for rejection
document.getElementById('rejectForm').addEventListener('submit', function(e) {
    const reason = document.getElementById('rejectionReason').value.trim();
    if (reason === '') {
        e.preventDefault();
        alert('Rejection reason is required');
        return false;
    }
    return confirm('Are you sure you want to reject this request?');
});
</script>

<jsp:include page="../layout/header.jsp">
    <jsp:param name="includeFooter" value="true"/>
</jsp:include>
