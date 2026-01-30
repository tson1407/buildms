<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="pageTitle" value="Cancel Sales Order" scope="request"/>
<c:set var="currentPage" value="salesorder-cancel" scope="request"/>
<jsp:include page="../layout/header.jsp"/>
<jsp:include page="../layout/sidebar.jsp"/>

<!-- Layout container -->
<div class="layout-page">
    <jsp:include page="../layout/navbar.jsp"/>

    <!-- Content wrapper -->
    <div class="content-wrapper">
        <!-- Content -->
        <div class="container-xxl flex-grow-1 container-p-y">
            <h4 class="fw-bold mb-4"><span class="text-muted fw-light">Sales / Sales Orders /</span> Cancel</h4>

            <!-- Error Message -->
            <c:if test="${param.error == 'reason_required'}">
                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                    <strong>Error!</strong> Cancellation reason is required.
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
            </c:if>

            <!-- Cancel Form -->
            <div class="card mb-4">
                <div class="card-header">
                    <h5 class="mb-0">Cancel Sales Order #${order.orderNo}</h5>
                </div>
                <div class="card-body">
                    <div class="alert alert-warning">
                        <i class="bx bx-error me-2"></i>
                        <strong>Warning!</strong> This action cannot be undone. Are you sure you want to cancel this sales order?
                    </div>

                    <form action="${pageContext.request.contextPath}/salesorder?action=cancel" method="post">
                        <input type="hidden" name="id" value="${order.id}">
                        
                        <div class="mb-3">
                            <label for="reason" class="form-label">Cancellation Reason <span class="text-danger">*</span></label>
                            <textarea class="form-control" id="reason" name="reason" rows="4" required placeholder="Enter the reason for cancelling this order"></textarea>
                        </div>

                        <div class="d-flex gap-2">
                            <button type="submit" class="btn btn-danger">
                                <i class="bx bx-x me-1"></i> Confirm Cancellation
                            </button>
                            <a href="${pageContext.request.contextPath}/salesorder?action=view&id=${order.id}" class="btn btn-secondary">
                                <i class="bx bx-arrow-back me-1"></i> Go Back
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
