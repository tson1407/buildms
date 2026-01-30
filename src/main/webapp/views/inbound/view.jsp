<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:set var="pageTitle" value="Inbound Request Details" scope="request"/>
<c:set var="currentPage" value="inbound-view" scope="request"/>
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
                <h4 class="fw-bold"><span class="text-muted fw-light">Inbound /</span> Request #${request.id}</h4>
                <a href="${pageContext.request.contextPath}/inbound" class="btn btn-secondary">
                    <i class="bx bx-arrow-back"></i> Back to List
                </a>
            </div>

            <!-- Alerts -->
            <c:if test="${param.success == 'created'}">
                <div class="alert alert-success alert-dismissible fade show" role="alert">
                    <strong>Success!</strong> Inbound Request #${request.id} created successfully!
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
            </c:if>
            <c:if test="${param.success == 'approved'}">
                <div class="alert alert-success alert-dismissible fade show" role="alert">
                    <strong>Success!</strong> Inbound Request #${request.id} has been approved!
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
            </c:if>
            <c:if test="${param.success == 'rejected'}">
                <div class="alert alert-success alert-dismissible fade show" role="alert">
                    <strong>Success!</strong> Inbound Request #${request.id} has been rejected!
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
            </c:if>
            <c:if test="${param.success == 'completed'}">
                <div class="alert alert-success alert-dismissible fade show" role="alert">
                    <strong>Success!</strong> Inbound Request #${request.id} completed successfully!
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
            </c:if>

            <!-- Request Details Card -->
            <div class="card mb-4">
                <div class="card-header d-flex justify-content-between align-items-center">
                    <h5 class="mb-0">Request Information</h5>
                    <c:choose>
                        <c:when test="${request.status == 'Created'}">
                            <span class="badge bg-label-warning">Created</span>
                        </c:when>
                        <c:when test="${request.status == 'Approved'}">
                            <span class="badge bg-label-info">Approved</span>
                        </c:when>
                        <c:when test="${request.status == 'InProgress'}">
                            <span class="badge bg-label-primary">In Progress</span>
                        </c:when>
                        <c:when test="${request.status == 'Completed'}">
                            <span class="badge bg-label-success">Completed</span>
                        </c:when>
                        <c:when test="${request.status == 'Rejected'}">
                            <span class="badge bg-label-danger">Rejected</span>
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
                        <c:if test="${request.approvedDate != null}">
                            <div class="col-md-6 mb-3">
                                <label class="form-label text-muted small">Approved Date</label>
                                <p class="mb-0"><fmt:formatDate value="${request.approvedDate}" pattern="yyyy-MM-dd HH:mm:ss"/></p>
                            </div>
                        </c:if>
                        <c:if test="${request.rejectedDate != null}">
                            <div class="col-md-6 mb-3">
                                <label class="form-label text-muted small">Rejected Date</label>
                                <p class="mb-0"><fmt:formatDate value="${request.rejectedDate}" pattern="yyyy-MM-dd HH:mm:ss"/></p>
                            </div>
                            <div class="col-md-12 mb-3">
                                <label class="form-label text-muted small">Rejection Reason</label>
                                <p class="mb-0">${request.rejectionReason}</p>
                            </div>
                        </c:if>
                        <c:if test="${request.completedDate != null}">
                            <div class="col-md-6 mb-3">
                                <label class="form-label text-muted small">Completed Date</label>
                                <p class="mb-0"><fmt:formatDate value="${request.completedDate}" pattern="yyyy-MM-dd HH:mm:ss"/></p>
                            </div>
                        </c:if>
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
            <div class="card">
                <div class="card-header">
                    <h5 class="mb-0">Request Items</h5>
                </div>
                <div class="card-body">
                    <div class="table-responsive">
                        <table class="table table-bordered">
                            <thead>
                                <tr>
                                    <th>Product ID</th>
                                    <th>Expected Quantity</th>
                                    <c:if test="${request.status == 'Completed'}">
                                        <th>Received Quantity</th>
                                        <th>Variance</th>
                                    </c:if>
                                    <th>Target Location</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="item" items="${items}">
                                    <tr>
                                        <td><strong>#${item.productId}</strong></td>
                                        <td>${item.quantity}</td>
                                        <c:if test="${request.status == 'Completed'}">
                                            <td>
                                                <c:choose>
                                                    <c:when test="${item.receivedQuantity != null}">
                                                        ${item.receivedQuantity}
                                                    </c:when>
                                                    <c:otherwise>0</c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>
                                                <c:set var="variance" value="${item.receivedQuantity != null ? item.receivedQuantity - item.quantity : -item.quantity}"/>
                                                <c:choose>
                                                    <c:when test="${variance == 0}">
                                                        <span class="badge bg-label-success">Match</span>
                                                    </c:when>
                                                    <c:when test="${variance > 0}">
                                                        <span class="badge bg-label-warning">+${variance}</span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="badge bg-label-danger">${variance}</span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                        </c:if>
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

            <!-- Action Buttons -->
            <div class="mt-4">
                <c:if test="${request.status == 'Created' && (user.role == 'Admin' || user.role == 'Manager')}">
                    <a href="${pageContext.request.contextPath}/inbound?action=approve&id=${request.id}" 
                       class="btn btn-primary">
                        <i class="bx bx-check"></i> Approve/Reject Request
                    </a>
                </c:if>
                <c:if test="${request.status == 'Approved' || request.status == 'InProgress'}">
                    <a href="${pageContext.request.contextPath}/inbound?action=execute&id=${request.id}" 
                       class="btn btn-success">
                        <i class="bx bx-box"></i> Execute Request
                    </a>
                </c:if>
            </div>
        </div>
        <!-- / Content -->

        <jsp:include page="../layout/footer.jsp"/>
    </div>
    <!-- Content wrapper -->
</div>
<!-- / Layout page -->

<jsp:include page="../layout/header.jsp">
    <jsp:param name="includeFooter" value="true"/>
</jsp:include>
