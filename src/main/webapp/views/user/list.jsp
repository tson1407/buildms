<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:set var="pageTitle" value="User Management" scope="request"/>
<c:set var="currentPage" value="user-list" scope="request"/>
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
                <h4 class="fw-bold"><span class="text-muted fw-light">Admin /</span> User Management</h4>
                <a href="${pageContext.request.contextPath}/user?action=create" class="btn btn-primary">
                    <i class="bx bx-plus me-1"></i> Create New User
                </a>
            </div>

            <!-- Alerts -->
            <c:if test="${param.success == 'created'}">
                <div class="alert alert-success alert-dismissible fade show" role="alert">
                    <strong>Success!</strong> User created successfully!
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
            </c:if>
            <c:if test="${param.success == 'updated'}">
                <div class="alert alert-success alert-dismissible fade show" role="alert">
                    <strong>Success!</strong> User updated successfully!
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
            </c:if>
            <c:if test="${param.success == 'deleted'}">
                <div class="alert alert-success alert-dismissible fade show" role="alert">
                    <strong>Success!</strong> User deactivated successfully!
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
            </c:if>
            <c:if test="${param.success == 'passwordreset'}">
                <div class="alert alert-success alert-dismissible fade show" role="alert">
                    <strong>Success!</strong> User password has been reset!
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
            </c:if>
            <c:if test="${param.error != null}">
                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                    <strong>Error!</strong> ${param.error}
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
            </c:if>

            <!-- User List Card -->
            <div class="card">
                <div class="card-header">
                    <h5 class="mb-0">Users</h5>
                </div>
                <div class="card-body">
                    <c:choose>
                        <c:when test="${empty users}">
                            <div class="text-center py-5">
                                <i class="bx bx-user bx-lg text-muted"></i>
                                <p class="mt-2 text-muted">No users found.</p>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="table-responsive text-nowrap">
                                <table class="table table-hover">
                                    <thead>
                                        <tr>
                                            <th>ID</th>
                                            <th>Username</th>
                                            <th>Name</th>
                                            <th>Email</th>
                                            <th>Role</th>
                                            <th>Status</th>
                                            <th>Last Login</th>
                                            <th>Actions</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="user" items="${users}">
                                            <tr>
                                                <td>${user.id}</td>
                                                <td><strong>${user.username}</strong></td>
                                                <td>${user.name}</td>
                                                <td>${user.email}</td>
                                                <td>
                                                    <span class="badge bg-label-primary">${user.role}</span>
                                                </td>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${user.status == 'Active'}">
                                                            <span class="badge bg-label-success">Active</span>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <span class="badge bg-label-secondary">Inactive</span>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${not empty user.lastLogin}">
                                                            <fmt:formatDate value="${user.lastLogin}" pattern="yyyy-MM-dd HH:mm" />
                                                        </c:when>
                                                        <c:otherwise>
                                                            <span class="text-muted">Never</span>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td>
                                                    <div class="dropdown">
                                                        <button type="button" class="btn p-0 dropdown-toggle hide-arrow" data-bs-toggle="dropdown">
                                                            <i class="bx bx-dots-vertical-rounded"></i>
                                                        </button>
                                                        <div class="dropdown-menu">
                                                            <a class="dropdown-item" href="${pageContext.request.contextPath}/user?action=view&id=${user.id}">
                                                                <i class="bx bx-show me-1"></i> View
                                                            </a>
                                                            <a class="dropdown-item" href="${pageContext.request.contextPath}/user?action=edit&id=${user.id}">
                                                                <i class="bx bx-edit me-1"></i> Edit
                                                            </a>
                                                            <a class="dropdown-item" href="javascript:void(0);" onclick="showResetPasswordModal(${user.id}, '${user.username}')">
                                                                <i class="bx bx-key me-1"></i> Reset Password
                                                            </a>
                                                            <div class="dropdown-divider"></div>
                                                            <a class="dropdown-item text-danger" href="javascript:void(0);" 
                                                               onclick="confirmDelete(${user.id}, '${user.username}')">
                                                                <i class="bx bx-trash me-1"></i> Deactivate
                                                            </a>
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
    </div>
    <!-- Content wrapper -->
</div>
<!-- / Layout page -->

<!-- Reset Password Modal -->
<div class="modal fade" id="resetPasswordModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Reset Password</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <form action="${pageContext.request.contextPath}/user" method="POST">
                <input type="hidden" name="action" value="resetPassword">
                <input type="hidden" name="id" id="resetUserId">
                <div class="modal-body">
                    <p>Reset password for user: <strong id="resetUsername"></strong></p>
                    <div class="mb-3">
                        <label for="newPassword" class="form-label">New Password</label>
                        <input type="password" class="form-control" id="newPassword" name="newPassword" 
                               placeholder="Enter new password" required minlength="6">
                        <small class="text-muted">Password must be at least 6 characters</small>
                    </div>
                    <div class="mb-3">
                        <label for="confirmPassword" class="form-label">Confirm Password</label>
                        <input type="password" class="form-control" id="confirmPassword" name="confirmPassword" 
                               placeholder="Confirm new password" required minlength="6">
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                    <button type="submit" class="btn btn-primary">Reset Password</button>
                </div>
            </form>
        </div>
    </div>
</div>

<jsp:include page="../layout/footer.jsp"/>

<script>
function showResetPasswordModal(userId, username) {
    document.getElementById('resetUserId').value = userId;
    document.getElementById('resetUsername').textContent = username;
    new bootstrap.Modal(document.getElementById('resetPasswordModal')).show();
}

function confirmDelete(userId, username) {
    if (confirm('Are you sure you want to deactivate user "' + username + '"?')) {
        window.location.href = '${pageContext.request.contextPath}/user?action=delete&id=' + userId;
    }
}
</script>
