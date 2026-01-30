<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:set var="pageTitle" value="Sales Order Details" scope="request"/>
<c:set var="currentPage" value="salesorder-view" scope="request"/>
<jsp:include page="../layout/header.jsp"/>
<jsp:include page="../layout/sidebar.jsp"/>

<!-- Layout container -->
<div class="layout-page">
    <jsp:include page="../layout/navbar.jsp"/>

    <!-- Content wrapper -->
    <div class="content-wrapper">
        <!-- Content -->
        <div class="container-xxl flex-grow-1 container-p-y">
            <h4 class="fw-bold mb-4"><span class="text-muted fw-light">Sales / Sales Orders /</span> View</h4>

            <!-- Success Messages -->
            <c:if test="${param.success == 'created'}">
                <div class="alert alert-success alert-dismissible fade show" role="alert">
                    <strong>Success!</strong> Sales order created successfully!
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
            </c:if>
            <c:if test="${param.success == 'confirmed'}">
                <div class="alert alert-success alert-dismissible fade show" role="alert">
                    <strong>Success!</strong> Sales order confirmed successfully!
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
            </c:if>
            <c:if test="${param.success == 'cancelled'}">
                <div class="alert alert-success alert-dismissible fade show" role="alert">
                    <strong>Success!</strong> Sales order cancelled successfully!
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
            </c:if>

            <!-- Error Messages -->
            <c:if test="${param.error == 'confirm_failed'}">
                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                    <strong>Error!</strong> Failed to confirm sales order. Only Draft orders can be confirmed.
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
            </c:if>
            <c:if test="${param.error == 'cancel_failed'}">
                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                    <strong>Error!</strong> Failed to cancel sales order.
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
            </c:if>

            <!-- Order Details Card -->
            <div class="card mb-4">
                <div class="card-header d-flex justify-content-between align-items-center">
                    <h5 class="mb-0">Order #${order.orderNo}</h5>
                    <div>
                        <c:choose>
                            <c:when test="${order.status == 'Draft'}">
                                <span class="badge bg-secondary">Draft</span>
                            </c:when>
                            <c:when test="${order.status == 'Confirmed'}">
                                <span class="badge bg-primary">Confirmed</span>
                            </c:when>
                            <c:when test="${order.status == 'FulfillmentRequested'}">
                                <span class="badge bg-info">Fulfillment Requested</span>
                            </c:when>
                            <c:when test="${order.status == 'Completed'}">
                                <span class="badge bg-success">Completed</span>
                            </c:when>
                            <c:when test="${order.status == 'Cancelled'}">
                                <span class="badge bg-danger">Cancelled</span>
                            </c:when>
                        </c:choose>
                    </div>
                </div>
                <div class="card-body">
                    <div class="row">
                        <div class="col-md-6">
                            <table class="table table-borderless">
                                <tr>
                                    <th width="150">Order No:</th>
                                    <td><strong>${order.orderNo}</strong></td>
                                </tr>
                                <tr>
                                    <th>Customer ID:</th>
                                    <td>${order.customerId}</td>
                                </tr>
                                <tr>
                                    <th>Status:</th>
                                    <td>${order.status}</td>
                                </tr>
                                <tr>
                                    <th>Created By:</th>
                                    <td>User #${order.createdBy}</td>
                                </tr>
                                <tr>
                                    <th>Created At:</th>
                                    <td><fmt:formatDate value="${order.createdAt}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
                                </tr>
                            </table>
                        </div>
                        <div class="col-md-6">
                            <c:if test="${order.confirmedBy != null}">
                                <table class="table table-borderless">
                                    <tr>
                                        <th width="150">Confirmed By:</th>
                                        <td>User #${order.confirmedBy}</td>
                                    </tr>
                                    <tr>
                                        <th>Confirmed Date:</th>
                                        <td><fmt:formatDate value="${order.confirmedDate}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
                                    </tr>
                                </table>
                            </c:if>
                            <c:if test="${order.cancelledBy != null}">
                                <table class="table table-borderless">
                                    <tr>
                                        <th width="150">Cancelled By:</th>
                                        <td>User #${order.cancelledBy}</td>
                                    </tr>
                                    <tr>
                                        <th>Cancelled Date:</th>
                                        <td><fmt:formatDate value="${order.cancelledDate}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
                                    </tr>
                                    <tr>
                                        <th>Reason:</th>
                                        <td>${order.cancellationReason}</td>
                                    </tr>
                                </table>
                            </c:if>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Order Items Card -->
            <div class="card mb-4">
                <div class="card-header">
                    <h5 class="mb-0">Order Items</h5>
                </div>
                <div class="card-body">
                    <div class="table-responsive">
                        <table class="table table-bordered">
                            <thead>
                                <tr>
                                    <th>Product ID</th>
                                    <th>Quantity</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="item" items="${items}">
                                    <tr>
                                        <td>Product #${item.productId}</td>
                                        <td>${item.quantity}</td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>

            <!-- Actions -->
            <div class="card">
                <div class="card-body">
                    <div class="d-flex gap-2">
                        <a href="${pageContext.request.contextPath}/salesorder" class="btn btn-secondary">
                            <i class="bx bx-arrow-back me-1"></i> Back to List
                        </a>
                        
                        <c:if test="${order.status == 'Draft'}">
                            <a href="${pageContext.request.contextPath}/salesorder?action=confirm&id=${order.id}" 
                               class="btn btn-primary"
                               onclick="return confirm('Are you sure you want to confirm this sales order?')">
                                <i class="bx bx-check me-1"></i> Confirm Order
                            </a>
                        </c:if>
                        
                        <c:if test="${order.status != 'Cancelled' && order.status != 'Completed'}">
                            <a href="${pageContext.request.contextPath}/salesorder?action=cancel&id=${order.id}" 
                               class="btn btn-danger">
                                <i class="bx bx-x me-1"></i> Cancel Order
                            </a>
                        </c:if>
                    </div>
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
