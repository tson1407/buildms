<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="pageTitle" value="Dashboard" scope="request"/>
<c:set var="currentPage" value="dashboard" scope="request"/>
<jsp:include page="views/layout/header.jsp"/>
<jsp:include page="views/layout/sidebar.jsp"/>

<!-- Layout container -->
<div class="layout-page">
    <jsp:include page="views/layout/navbar.jsp"/>

    <!-- Content wrapper -->
    <div class="content-wrapper">
        <!-- Content -->
        <div class="container-xxl flex-grow-1 container-p-y">
            <div class="row">
                <div class="col-lg-12 mb-4 order-0">
                    <div class="card">
                        <div class="d-flex align-items-end row">
                            <div class="col-sm-7">
                                <div class="card-body">
                                    <h5 class="card-title text-primary">Welcome to Build MS! 🎉</h5>
                                    <p class="mb-4">
                                        Your comprehensive <span class="fw-bold">Building Material Management System</span>. 
                                        Start managing your products, inventory, and more.
                                    </p>

                                    <a href="${pageContext.request.contextPath}/product" class="btn btn-sm btn-primary">View Products</a>
                                </div>
                            </div>
                            <div class="col-sm-5 text-center text-sm-left">
                                <div class="card-body pb-0 px-0 px-md-4">
                                    <img src="${pageContext.request.contextPath}/assets/img/illustrations/man-with-laptop.png" 
                                         height="180" alt="View Badge User" 
                                         data-app-dark-img="illustrations/man-with-laptop-dark.png" 
                                         data-app-light-img="illustrations/man-with-laptop.png"/>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="row">
                <!-- Statistics Cards -->
                <div class="col-lg-3 col-md-6 col-sm-6 mb-4">
                    <div class="card">
                        <div class="card-body">
                            <div class="card-title d-flex align-items-start justify-content-between">
                                <div class="avatar flex-shrink-0">
                                    <i class="menu-icon tf-icons bx bx-package bx-md text-success"></i>
                                </div>
                            </div>
                            <span class="fw-semibold d-block mb-1">Total Products</span>
                            <h3 class="card-title mb-2">0</h3>
                            <small class="text-success fw-semibold"><i class="bx bx-up-arrow-alt"></i> View All</small>
                        </div>
                    </div>
                </div>

                <div class="col-lg-3 col-md-6 col-sm-6 mb-4">
                    <div class="card">
                        <div class="card-body">
                            <div class="card-title d-flex align-items-start justify-content-between">
                                <div class="avatar flex-shrink-0">
                                    <i class="menu-icon tf-icons bx bx-check-circle bx-md text-info"></i>
                                </div>
                            </div>
                            <span class="fw-semibold d-block mb-1">Active Products</span>
                            <h3 class="card-title mb-2">0</h3>
                            <small class="text-info fw-semibold"><i class="bx bx-stats"></i> Active</small>
                        </div>
                    </div>
                </div>

                <div class="col-lg-3 col-md-6 col-sm-6 mb-4">
                    <div class="card">
                        <div class="card-body">
                            <div class="card-title d-flex align-items-start justify-content-between">
                                <div class="avatar flex-shrink-0">
                                    <i class="menu-icon tf-icons bx bx-dollar bx-md text-warning"></i>
                                </div>
                            </div>
                            <span class="fw-semibold d-block mb-1">Total Value</span>
                            <h3 class="card-title mb-2">$0</h3>
                            <small class="text-warning fw-semibold"><i class="bx bx-trending-up"></i> Inventory</small>
                        </div>
                    </div>
                </div>

                <div class="col-lg-3 col-md-6 col-sm-6 mb-4">
                    <div class="card">
                        <div class="card-body">
                            <div class="card-title d-flex align-items-start justify-content-between">
                                <div class="avatar flex-shrink-0">
                                    <i class="menu-icon tf-icons bx bx-category bx-md text-primary"></i>
                                </div>
                            </div>
                            <span class="fw-semibold d-block mb-1">Categories</span>
                            <h3 class="card-title mb-2">0</h3>
                            <small class="text-primary fw-semibold"><i class="bx bx-list-ul"></i> Total</small>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Quick Actions -->
            <div class="row">
                <div class="col-md-12 mb-4">
                    <div class="card">
                        <h5 class="card-header">Quick Actions</h5>
                        <div class="card-body">
                            <div class="row">
                                <div class="col-md-3 mb-3">
                                    <a href="${pageContext.request.contextPath}/product?action=create" class="btn btn-primary w-100">
                                        <i class="bx bx-plus me-1"></i> Add New Product
                                    </a>
                                </div>
                                <div class="col-md-3 mb-3">
                                    <a href="${pageContext.request.contextPath}/product" class="btn btn-info w-100">
                                        <i class="bx bx-list-ul me-1"></i> View All Products
                                    </a>
                                </div>
                                <div class="col-md-3 mb-3">
                                    <a href="#" class="btn btn-success w-100">
                                        <i class="bx bx-file me-1"></i> Generate Report
                                    </a>
                                </div>
                                <div class="col-md-3 mb-3">
                                    <a href="#" class="btn btn-warning w-100">
                                        <i class="bx bx-cog me-1"></i> Settings
                                    </a>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <!-- / Content -->

<jsp:include page="views/layout/footer.jsp"/>
