---
trigger: always_on
---

# Smart WMS - Copilot Instructions

## Project Overview
Smart WMS is a **Java 17 + Jakarta Servlet 6.0** web application for warehouse management. It runs on **Apache Tomcat 10+** with **SQL Server** as the database. This is an academic FPT University SWP project demonstrating warehouse operations with role-based access control—no financial processing.

**Base package:** `vn.edu.fpt.swp`  
**WAR artifact:** `buildms.war`  
**All 48 use cases across 13 modules are implemented. Transfer module (TRF) is being updated to enforce destination-warehouse approval (49 UCs total).**

## Architecture (MVC Layered)
```
Controller (Servlets) → Service → DAO → SQL Server
     ↓
   JSP Views (WEB-INF/views/)
```

- **Controllers** (`controller/`): Handle HTTP via `@WebServlet`, route to services, forward to JSP
- **Services** (`service/`): Business logic, validation, orchestration
- **DAOs** (`dao/`): Direct JDBC operations using `DBConnection.getConnection()`
- **Models** (`model/`): Plain Java POJOs implementing `Serializable`
- **Filters** (`filter/`): `AuthFilter` intercepts all requests for auth/authorization
- **Utilities** (`util/`): `DBConnection` (JDBC helper), `PasswordUtil` (SHA-256 hashing)
- **Views** (`webapp/WEB-INF/views/`): JSP pages organized by feature, protected under WEB-INF
- **Common Components** (`webapp/WEB-INF/common/`): Shared layout components (header, footer, sidebar, etc.)

## Source Code Inventory

### Models (`model/`)
| Model | Key Fields | Notes |
|-------|-----------|-------|
| `User` | id, username, name, email, passwordHash, role, status, warehouseId, createdAt, lastLogin | Has `getFullName()`/`setFullName()` aliases for `name` |
| `Product` | id, sku, name, unit, categoryId, isActive, createdAt | |
| `Category` | id, name, description | |
| `Warehouse` | id, name, location, createdAt | |
| `Location` | id, warehouseId, code, type (Storage/Picking/Staging), isActive | |
| `Inventory` | productId, warehouseId, locationId, quantity | Composite PK: (productId, warehouseId, locationId) |
| `Customer` | id, code, name, contactInfo, status | |
| `Request` | id, type, status, createdBy, approvedBy, rejectedBy, completedBy, salesOrderId, sourceWarehouseId, destinationWarehouseId, expectedDate, notes, reason, createdAt | Shared by Inbound/Outbound/Transfer/Internal |
| `RequestItem` | requestId, productId, quantity, locationId, sourceLocationId, destinationLocationId, receivedQuantity, pickedQuantity | Composite PK: (requestId, productId) |
| `SalesOrder` | id, orderNo, customerId, status, createdBy, confirmedBy, cancelledBy, cancellationReason, createdAt | |
| `SalesOrderItem` | salesOrderId, productId, quantity | Composite PK: (salesOrderId, productId) |

### DAOs (`dao/`)
`CategoryDAO`, `CustomerDAO`, `InventoryDAO`, `LocationDAO`, `ProductDAO`, `RequestDAO`, `RequestItemDAO`, `SalesOrderDAO`, `SalesOrderItemDAO`, `UserDAO`, `WarehouseDAO`

### Services (`service/`)
`AuthService`, `CategoryService`, `CustomerService`, `InboundService`, `InventoryService`, `LocationService`, `MovementService`, `OutboundService`, `ProductService`, `SalesOrderService`, `TransferService`, `UserService`, `WarehouseService`

### Controllers (`controller/`) — Servlet URL Mappings
| Controller | URL | Modules |
|-----------|-----|---------|
| `AuthController` | `/auth` | Login, Logout, Change Password |
| `DashboardController` | `/dashboard` | Main dashboard |
| `ProductController` | `/product` | CRUD + toggle status + details |
| `CategoryController` | `/category` | CRUD + delete |
| `WarehouseController` | `/warehouse` | CRUD |
| `LocationController` | `/location` | CRUD + toggle status |
| `UserController` | `/user` | CRUD + toggle status + assign warehouse |
| `CustomerController` | `/customer` | CRUD + toggle status |
| `InboundController` | `/inbound` | Create, Approve/Reject, Execute |
| `OutboundController` | `/outbound` | Create internal, Approve/Reject, Execute |
| `MovementController` | `/movement` | Create, Execute |
| `TransferController` | `/transfer` | Create, Approve/Reject, Execute outbound, Execute inbound |
| `SalesOrderController` | `/sales-order` | Create, Confirm, Generate outbound, Cancel |
| `InventoryController` | `/inventory` | View by warehouse, by product, search |

## View Layer Structure
```
webapp/
├── index.jsp                    # Entry point — redirects to login or dashboard
├── assets/                      # Static assets (CSS, JS, images, vendor libs)
├── dist/                        # Distribution files
├── fonts/                       # Font files
├── js/                          # Application JavaScript
├── libs/                        # Third-party libraries
└── WEB-INF/
    ├── web.xml                  # Jakarta Servlet 6.0 configuration
    ├── common/                  # Reusable layout components
    │   ├── head.jsp             # HTML head (meta, CSS, fonts) — param: pageTitle, pageCss
    │   ├── sidebar.jsp          # Left navigation menu (role-based) — params: activeMenu, activeSubMenu
    │   ├── navbar.jsp           # Top navigation bar with user dropdown
    │   ├── footer.jsp           # Page footer
    │   ├── scripts.jsp          # Common JS includes — param: pageScript
    │   ├── alerts.jsp           # Alert messages component (success/error/warning/info)
    │   ├── layout.jsp           # Full-page layout template — params: pageTitle, activeMenu, activeSubMenu, contentPage, pageCss, pageScript
    │   ├── pagination.jsp       # Pagination component
    │   └── delete-modal.jsp     # Delete confirmation modal
    └── views/
        ├── auth/                # login.jsp, register.jsp, change-password.jsp
        ├── dashboard.jsp        # Main dashboard
        ├── product/             # list.jsp, add.jsp, edit.jsp, details.jsp
        ├── category/            # list.jsp, add.jsp, edit.jsp
        ├── warehouse/           # list.jsp, add.jsp, edit.jsp
        ├── location/            # list.jsp, add.jsp, edit.jsp
        ├── user/                # list.jsp, add.jsp, edit.jsp
        ├── customer/            # list.jsp, add.jsp, edit.jsp
        ├── inventory/           # by-warehouse.jsp, by-product.jsp, search.jsp
        ├── inbound/             # list.jsp, create.jsp, details.jsp, execute.jsp
        ├── outbound/            # list.jsp, create.jsp, details.jsp, execute.jsp
        ├── movement/            # list.jsp, create.jsp, details.jsp, execute.jsp
        ├── transfer/            # list.jsp, create.jsp, view.jsp, execute-outbound.jsp, execute-inbound.jsp
        ├── sales-order/         # list.jsp, create.jsp, view.jsp, generate-outbound.jsp, cancel.jsp
        └── error/               # 403.jsp, 404.jsp, 500.jsp, maintenance.jsp, session-expired.jsp
```

## Key Patterns & Conventions

### Servlet Controllers
- Use `@WebServlet("/path")` annotation (no web.xml mapping needed)
- Route via `action` parameter: `?action=list`, `?action=add`, `?action=edit`
- Initialize services in `init()` method: `service = new XxxService();`
- Forward to JSP: `request.getRequestDispatcher("/WEB-INF/views/feature/page.jsp").forward(request, response)`
- Redirect after POST: `response.sendRedirect(request.getContextPath() + "/path?action=list")`
- Check role in controllers via helper methods like `hasManageAccess()`, `isAdmin()`

### DAO Pattern
```java
try (Connection conn = DBConnection.getConnection();
     PreparedStatement stmt = conn.prepareStatement(sql)) {
    // Always use try-with-resources
    // Use PreparedStatement for SQL injection prevention
    // Use Statement.RETURN_GENERATED_KEYS for inserts
    // Use Types.BIGINT / Types.TIMESTAMP for nullable parameters
}
```

### Service Pattern
- Services instantiate DAOs in constructor: `this.dao = new XxxDAO();`
- Validate inputs before calling DAO (null checks, empty strings, ID ranges)
- Return `null` or `false` on failure, caller checks result
- No exceptions thrown to controllers — fail gracefully

### Password Security
- Use `PasswordUtil.hashPassword(password)` for hashing (SHA-256 + salt)
- Use `PasswordUtil.verifyPassword(password, storedHash)` for verification
- Hash format: `salt:hash` (both Base64 encoded, 16-byte salt)

### Role-Based Access Control
Roles: `Admin`, `Manager`, `Staff`, `Sales`

| URL Pattern | Allowed Roles |
|-------------|--------------|
| `/user`, `/users` | Admin |
| `/warehouse`, `/location` | Admin, Manager |
| `/category/add`, `/category/edit`, `/category/delete` | Admin, Manager |
| `/product/add`, `/product/edit`, `/product/toggle` | Admin, Manager |
| `/inbound`, `/outbound`, `/transfer`, `/movement`, `/inventory` | Manager, Staff |
| `/sales-order`, `/customer` | Manager, Sales |
| `/dashboard`, `/profile`, `/product`, `/category` | All authenticated |

- Authorization enforced by `AuthFilter` (`@WebFilter("/*")`) using `ROLE_ACCESS_MAP`
- Public URLs bypass auth: `/auth`, `/assets`, `/css`, `/js`, `/images`, `/libs`, `/fonts`, static file extensions
- If no specific rule found, all authenticated users are allowed

### Session Management
- 30-minute inactivity timeout — enforced both in `web.xml` and `AuthFilter`
- `AuthFilter` tracks `lastActivityTime` attribute and invalidates expired sessions
- Session cookie: `HttpOnly=true`, `Secure=false`, cookie-based tracking
- Key session attributes:
  - `user` — full `User` object
  - `lastActivityTime` — `Long` timestamp updated on each request
  - `successMessage` — flash message (consumed and removed after display)
- Get session: `request.getSession(false)` (returns null if none exists)
- Expired sessions redirect to login with `?expired=true`

### JSP Views (JSTL + EL Only)
**CRITICAL: NO SCRIPTLETS ALLOWED**
- Use **JSTL tags** and **EL (Expression Language)** exclusively in all JSP files
- **NEVER use scriptlets**: `<% %>`, `<%= %>`, `<%! %>`
- Always include JSTL core taglib: `<%@ taglib prefix="c" uri="jakarta.tags.core" %>`

### Using Common Layout Components

**Option A: Manual Include (used by most pages)**
```jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html lang="en" class="layout-menu-fixed layout-compact"
      data-assets-path="${contextPath}/assets/"
      data-template="vertical-menu-template-free">
<head>
    <jsp:include page="/WEB-INF/common/head.jsp">
        <jsp:param name="pageTitle" value="Page Title" />
    </jsp:include>
</head>
<body>
    <div class="layout-wrapper layout-content-navbar">
        <div class="layout-container">

            <jsp:include page="/WEB-INF/common/sidebar.jsp">
                <jsp:param name="activeMenu" value="products" />
                <jsp:param name="activeSubMenu" value="product-list" />
            </jsp:include>

            <div class="layout-page">
                <jsp:include page="/WEB-INF/common/navbar.jsp" />

     