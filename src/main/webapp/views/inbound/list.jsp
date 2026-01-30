<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:set var="pageTitle" value="Inbound Requests" scope="request"/>
<c:set var="currentPage" value="inbound-list" scope="request"/>
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
                <h4 class="fw-bold"><span class="text-muted fw-light">Warehouse /</span> Inbound Requests</h4>
                <c:if test="${user.role == 'Admin' || user.role == 'Manager'}">
                    <a href="${pageContext.request.contextPath}/inbound?action=create" class="btn btn-primary">
                        <i class="bx bx-plus me-1"></i> Create Inbound Request
                    </a>
                </c:if>
            </div>

            <!-- Alerts -->
            <c:if test="${param.success == 'created'}">
                <div class="alert alert-success alert-dismissible fade show" role="alert">
                    <strong>Success!</strong> Inbound request created successfully!
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
            </c:if>
            <c:if test="${param.success == 'approved'}">
                <div class="alert alert-success alert-dismissible fade show" role="alert">
                    <strong>Success!</strong> Inbound request has been approved!
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
            </c:if>
            <c:if test="${param.success == 'rejected'}">
                <div class="alert alert-success alert-dismissible fade show" role="alert">
                    <strong>Success!</strong> Inbound request has been rejected!
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
            </c:if>
            <c:if test="${param.success == 'completed'}">
                <div class="alert alert-success alert-dismissible fade show" role="alert">
                    <strong>Success!</strong> Inbound request completed successfully!
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
            </c:if>
            <c:if test="${param.error != null}">
                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                    <strong>Error!</strong> ${param.error}
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
            </c:if>

            <!-- Filter Card -->
            <div class="card mb-4">
                <div class="card-body">
                    <form action="${pageContext.request.contextPath}/inbound" method="get" class="row g-3">
                        <div class="col-md-4">
                            <label class="form-label">Filter by Status</label>
                            <select name="status" class="form-select">
                                <option value="">All Status</option>
                                <option value="Created" ${statusFilter == 'Created' ? 'selected' : ''}>Created</option>
                                <option value="Approved" ${statusFilter == 'Approved' ? 'selected' : ''}>Approved</option>
                                <option value="InProgress" ${statusFilter == 'InProgress' ? 'selected' : ''}>In Progress</option>
                                <option value="Completed" ${statusFilter == 'Completed' ? 'selected' : ''}>Completed</option>
                                <option value="Rejected" ${statusFilter == 'Rejected' ? 'selected' : ''}>Rejected</option>
                            </select>
                        </div>
                        <div class="col-md-8 d-flex align-items-end gap-2">
                            <button type="submit" class="btn btn-primary">
                                <i class="bx bx-search"></i> Filter
                            </button>
                            <a href="${pageContext.request.contextPath}/inbound" class="btn btn-secondary">
                                <i class="bx bx-reset"></i> Reset
                            </a>
                        </div>
                    </form>
                </div>
            </div>

            <!-- Requests List Card -->
            <div class="card">
                <div class="card-header">
                    <h5 class="mb-0">Inbound Requests</h5>
                </div>
                <div class="card-body">
                    <c:choose>
                        <c:when test="${empty requests}">
                            <div class="text-center py-5">
                                <i class="bx bx-package bx-lg text-muted"></i>
                                <p class="mt-2 text-muted">No inbound requests found.</p>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="table-responsive">
                                <table class="table table-hover">
                                    <thead>
                                        <tr>
                                            <th>Request ID</th>
                                            <th>Status</th>
                                            <th>Created Date</th>
                                            <th>Expected Date</th>
                                            <th>Notes</th>
                                            <th>Actions</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="req" items="${requests}">
                                            <tr>
                                                <td><strong>#${req.id}</strong></td>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${req.status == 'Created'}">
                                                            <span class="badge bg-label-warning">Created</span>
                                                        </c:when>
                                                        <c:when test="${req.status == 'Approved'}">
                                                            <span class="badge bg-label-info">Approved</span>
                                                        </c:when>
                                                        <c:when test="${req.status == 'InProgress'}">
                                                            <span class="badge bg-label-primary">In Progress</span>
                                                        </c:when>
                                                        <c:when test="${req.status == 'Completed'}">
                                                            <span class="badge bg-label-success">Completed</span>
                                                        </c:when>
                                                        <c:when test="${req.status == 'Rejected'}">
                                                            <span class="badge bg-label-danger">Rejected</span>
                                                        </c:when>
                                                    </c:choose>
                                                </td>
                                                <td><fmt:formatDate value="${req.createdAt}" pattern="yyyy-MM-dd HH:mm"/></td>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${req.expectedDate != null}">
                                                            <fmt:formatDate value="${req.expectedDate}" pattern="yyyy-MM-dd"/>
                                                        </c:when>
                                                        <c:otherwise>-</c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${not empty req.notes}">
                                                            ${req.notes.length() > 30 ? req.notes.substring(0, 30).concat('...') : req.notes}
                                                        </c:when>
                                                        <c:otherwise>-</c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td>
                                                    <div class="dropdown">
                                                        <button type="button" class="btn btn-sm btn-icon dropdown-toggle hide-arrow" 
                                                                data-bs-toggle="dropdown">
                                                            <i class="bx bx-dots-vertical-rounded"></i>
                                                        </button>
                                                        <div class="dropdown-menu">
                                                            <a class="dropdown-item" href="${pageContext.request.contextPath}/inbound?action=view&id=${req.id}">
                                                                <i class="bx bx-show me-1"></i> View Details
                                                            </a>
                                                            <c:if test="${req.status == 'Created' && (user.role == 'Admin' || user.role == 'Manager')}">
                                                                <a class="dropdown-item" href="${pageContext.request.contextPath}/inbound?action=approve&id=${req.id}">
                                                                    <i class="bx bx-check me-1"></i> Approve/Reject
                                                                </a>
                                                            </c:if>
                                                            <c:if test="${req.status == 'Approved' || req.status == 'InProgress'}">
                                                                <a class="dropdown-item" href="${pageContext.request.contextPath}/inbound?action=execute&id=${req.id}">
                                                                    <i class="bx bx-box me-1"></i> Execute
                                                                </a>
                                                            </c:if>
                                                        </div>
                                                    </div>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
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
