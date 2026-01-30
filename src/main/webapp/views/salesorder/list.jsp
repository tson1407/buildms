<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:set var="pageTitle" value="Sales Orders" scope="request"/>
<c:set var="currentPage" value="salesorder-list" scope="request"/>
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
                <h4 class="fw-bold"><span class="text-muted fw-light">Sales /</span> Sales Orders</h4>
                <a href="${pageContext.request.contextPath}/salesorder?action=create" class="btn btn-primary">
                    <i class="bx bx-plus me-1"></i> Create Sales Order
                </a>
            </div>

            <!-- Alerts -->
            <c:if test="${param.success == 'created'}">
                <div class="alert alert-success alert-dismissible fade show" role="alert">
                    <strong>Success!</strong> Sales order created successfully!
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
            </c:if>

            <!-- Sales Order List Card -->
            <div class="card">
                <div class="card-header">
                    <h5 class="mb-0">All Sales Orders</h5>
                </div>
                <div class="card-body">
                    <c:choose>
                        <c:when test="${empty orders}">
                            <div class="text-center py-5">
                                <i class="bx bx-cart bx-lg text-muted"></i>
                                <p class="mt-2 text-muted">No sales orders found.</p>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="table-responsive text-nowrap">
                                <table class="table table-hover">
                                    <thead>
                                        <tr>
                                            <th>Order No</th>
                                            <th>Customer</th>
                                            <th>Status</th>
                                            <th>Created At</th>
                                            <th>Actions</th>
                                        </tr>
                                    </thead>
                                    <tbody class="table-border-bottom-0">
                                        <c:forEach var="order" items="${orders}">
                                            <tr>
                                                <td><strong>${order.orderNo}</strong></td>
                                                <td>Customer #${order.customerId}</td>
                                                <td>
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
                                                        <c:otherwise>
                                                            <span class="badge bg-secondary">${order.status}</span>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td>
                                                    <fmt:formatDate value="${order.createdAt}" pattern="yyyy-MM-dd HH:mm" />
                                                </td>
                                                <td>
                                                    <a href="${pageContext.request.contextPath}/salesorder?action=view&id=${order.id}" 
                                                       class="btn btn-sm btn-info">
                                                        <i class="bx bx-show me-1"></i> View
                                                    </a>
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
        <div class="content-backdrop fade"></div>
    </div>
    <!-- Content wrapper -->
</div>
<!-- / Layout page -->
