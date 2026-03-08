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

## Database Schema (SQL Server)

### Tables
| Table | PK | Key Columns |
|-------|-----|-------------|
| `Users` | Id (BIGINT IDENTITY) | Username (UNIQUE), Email (UNIQUE), Role, Status, WarehouseId (nullable FK) |
| `Warehouses` | Id (BIGINT IDENTITY) | Name, Location |
| `Categories` | Id (BIGINT IDENTITY) | Name, Description |
| `Products` | Id (BIGINT IDENTITY) | SKU (UNIQUE), Name, Unit, CategoryId (FK), IsActive |
| `Locations` | Id (BIGINT IDENTITY) | WarehouseId (FK), Code, Type, IsActive |
| `Inventory` | (ProductId, WarehouseId, LocationId) | Quantity — composite PK with 3 FKs |
| `Requests` | Id (BIGINT IDENTITY) | Type, Status, CreatedBy (FK), Source/DestWarehouseId, SalesOrderId |
| `RequestItems` | (RequestId, ProductId) | Quantity, LocationId, Source/DestLocationId, ReceivedQuantity, PickedQuantity |
| `Customers` | Id (BIGINT IDENTITY) | Code (UNIQUE), Name, ContactInfo, Status |
| `SalesOrders` | Id (BIGINT IDENTITY) | OrderNo (UNIQUE), CustomerId (FK), Status, CreatedBy (FK) |
| `SalesOrderItems` | (SalesOrderId, ProductId) | Quantity |

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

                <div class="content-wrapper">
                    <div class="container-xxl flex-grow-1 container-p-y">
                        <jsp:include page="/WEB-INF/common/alerts.jsp" />
                        <!-- Page content here -->
                    </div>
                    <jsp:include page="/WEB-INF/common/footer.jsp" />
                    <div class="content-backdrop fade"></div>
                </div>
            </div>
        </div>
        <div class="layout-overlay layout-menu-toggle"></div>
    </div>

    <jsp:include page="/WEB-INF/common/scripts.jsp" />
</body>
</html>
```

**Option B: Layout template (`layout.jsp`)**
`layout.jsp` wraps common structure and includes a `contentPage` parameter. It also auto-renders alert messages and breadcrumbs.

### Alert Messages
Set these request attributes in servlets to show alerts:
- `successMessage` — Green success alert
- `errorMessage` — Red error alert
- `warningMessage` — Yellow warning alert
- `infoMessage` — Blue info alert

```java
request.setAttribute("successMessage", "Product created successfully!");
request.getRequestDispatcher("/WEB-INF/views/product/list.jsp").forward(request, response);
```

For flash messages across redirects, store in session then consume:
```java
session.setAttribute("successMessage", "Logged in successfully!");
// In DashboardController, move from session to request then remove from session
```

**Common JSTL Patterns:**
```jsp
<!-- Iterate over lists -->
<c:forEach var="item" items="${itemList}">
    ${item.name}
</c:forEach>

<!-- Conditional rendering -->
<c:if test="${not empty errorMessage}">
    <div class="alert">${errorMessage}</div>
</c:if>

<c:choose>
    <c:when test="${user.role == 'Admin'}">Admin Panel</c:when>
    <c:when test="${user.role == 'Manager'}">Manager Panel</c:when>
    <c:otherwise>User Panel</c:otherwise>
</c:choose>

<!-- URL construction -->
<c:url var="deleteUrl" value="/category">
    <c:param name="action" value="delete"/>
    <c:param name="id" value="${category.id}"/>
</c:url>

<!-- Set variables -->
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
```

**EL Expression Examples:**
```jsp
${user.username}                    <!-- Property access -->
${sessionScope.user.role}          <!-- Scope specification -->
${not empty errorMessage}          <!-- Logical operators -->
${param.action}                    <!-- Request parameters -->
${product.active ? 'Active' : 'Inactive'}  <!-- Ternary -->
```

## Developer Workflow

### Build & Run
```bash
mvn clean package                    # Build WAR file
# Copy target/buildms.war to Tomcat 10+ webapps/
# Access: http://localhost:8080/buildms/
```

### Database Setup
```bash
sqlcmd -S localhost -i database/schema.sql                         # Create schema
sqlcmd -S localhost -d smartwms_db -i database/user_seed.sql       # Add test users
sqlcmd -S localhost -d smartwms_db -i database/full_seed_data.sql  # Full seed data (all tables)
```

### Database Connection
Edit `src/main/resources/db.properties`:
```properties
db.url=jdbc:sqlserver://localhost;databaseName=smartwms_db;encrypt=true;trustServerCertificate=true
db.username=your_username
db.password=your_password
db.driver=com.microsoft.sqlserver.jdbc.SQLServerDriver
```
Configuration is automatically loaded by `DBConnection.java` static initializer at startup.

### Test Credentials
| Username | Password | Role |
|----------|----------|------|
| admin | password123 | Admin |
| manager | password123 | Manager |
| staff | password123 | Staff |
| sales | password123 | Sales |

## Important Files
- [schema.sql](database/schema.sql) — Database schema (all tables)
- [full_seed_data.sql](database/full_seed_data.sql) — Complete seed data
- [user_seed.sql](database/user_seed.sql) — Test user accounts
- [AuthFilter.java](src/main/java/vn/edu/fpt/swp/filter/AuthFilter.java) — Role permissions map & session timeout
- [DBConnection.java](src/main/java/vn/edu/fpt/swp/util/DBConnection.java) — JDBC connection factory
- [PasswordUtil.java](src/main/java/vn/edu/fpt/swp/util/PasswordUtil.java) — SHA-256 password hashing
- [document/detail-design/](document/detail-design/) — Use case specifications (**MUST follow strictly**)
- [document/SRS.md](document/SRS.md) — Feature requirements
- [progress/OVERALL_PROGRESS.md](progress/OVERALL_PROGRESS.md) — Implementation status (48/48 complete)
- [template/](template/) — UI templates (Sneat Bootstrap 5) — **MUST use for all new views**

## ⚠️ CRITICAL: Follow Detail Design & Templates

### Detail Design Documents (MANDATORY)
Before implementing any feature, **read and follow the corresponding use case document** in `document/detail-design/`:
- Each UC-*.md file defines exact flows, validations, error messages, and business rules
- Follow the Main Flow steps precisely
- Implement all Alternative Flows for error handling
- Respect the Access Control section for role permissions
- Example: For login feature → follow `document/detail-design/UC-AUTH-001-User-Login.md`

### UI Templates (MANDATORY)
All JSP views **MUST use the Sneat Bootstrap 5 templates** from `template/`:
- Copy HTML structure from `template/html/` files
- Use assets from `template/assets/` (already copied to `webapp/assets/`)
- Reference `template/html/auth-login-basic.html` for auth pages
- Reference `template/html/tables-basic.html` for list views
- Reference `template/html/form-layouts-vertical.html` for forms
- Maintain consistent look-and-feel across all pages

## Request/Inventory Workflows

### Request Lifecycle
```
Created → Approved → InProgress → Completed
              ↓
           Rejected
```
- Types: `Inbound`, `Outbound`, `Transfer`, `Internal`
- Sales Orders trigger Outbound requests (no direct inventory modification)
- Inventory keyed by composite PK: `(ProductId, WarehouseId, LocationId)`

### Sales Order Lifecycle
```
Draft → Confirmed → FulfillmentRequested → Completed
  ↓         ↓
Cancelled  Cancelled
```
- Sales creates order → Confirms → Generates outbound request → Warehouse executes

### Transfer Workflow
Transfer requests move goods between warehouses via a **cross-warehouse collaborative workflow**:
1. **Create** (Source WH Manager/Admin) — specify source/destination warehouses and items → Status: Created
2. **Approve/Reject** (**Destination WH Manager**/Admin) — destination warehouse decides whether to accept → Status: Approved/Rejected
3. **Execute outbound** (Source WH Staff/Manager) — pick items from source warehouse → Status: InProgress → InTransit
4. **Execute inbound & complete** (Dest WH Staff/Manager) — receive items at destination, complete the transfer → Status: Receiving → Completed

**Key rule:** Only the **destination warehouse Manager** can approve/reject a transfer (source WH Manager cannot approve their own transfer). This ensures the receiving warehouse agrees to accept the goods.

### Inbound Execution
- Staff enters `receivedQuantity` per item (may differ from requested)
- On completion, inventory is created/updated at specified locations

### Outbound Execution
- Staff enters `pickedQuantity` per item from specified locations
- On completion, inventory is deducted from specified locations

### Internal Movement
- Moves items between locations **within the same warehouse**
- Uses `sourceLocationId` → `destinationLocationId` on `RequestItem`
- No approval step required — directly created and executed

## Implemented Modules (49 Use Cases — 48 Complete, TRF updating)

| Module | UCs | Controllers | Description |
|--------|-----|-------------|-------------|
| AUTH | 5 | AuthController | Login, Logout, Change Password, Admin Reset, Session Timeout |
| PRD | 5 | ProductController | CRUD, Toggle Status, Details |
| CAT | 4 | CategoryController | CRUD, Delete |
| WH | 3 | WarehouseController | CRUD |
| LOC | 4 | LocationController | CRUD, Toggle Status |
| USER | 5 | UserController | CRUD, Toggle Status, Assign Warehouse |
| CUS | 4 | CustomerController | CRUD, Toggle Status |
| INB | 3 | InboundController | Create, Approve, Execute |
| OUT | 3 | OutboundController | Approve, Execute, Create Internal |
| MOV | 2 | MovementController | Create, Execute |
| INV | 3 | InventoryController | By Warehouse, By Product, Search |
| SO | 4 | SalesOrderController | Create, Confirm, Generate Outbound, Cancel |
| TRF | 4 | TransferController | Create, Approve/Reject (dest WH), Execute Outbound (source WH), Execute Inbound (dest WH) |

## When Adding New Features
1. **Read the detail design** in `document/detail-design/UC-*.md` for exact requirements
2. Create Model in `model/` with getters/setters implementing `Serializable`
3. Create DAO in `dao/` using `DBConnection` try-with-resources pattern
4. Create Service in `service/` for business logic and validation
5. Create Controller in `controller/` with `@WebServlet` annotation
6. Create JSP views in `webapp/WEB-INF/views/feature/` **using layout components from `WEB-INF/common/`**
7. Update `AuthFilter.ROLE_ACCESS_MAP` if new protected routes need role restrictions
8. Verify implementation matches all flows in the detail design document

## Sidebar Menu Configuration
The sidebar (`WEB-INF/common/sidebar.jsp`) uses parameters to highlight active menu items.

> **Simplified navigation (March 2026):** Sections were consolidated and redundant
> "Add X" shortcut sub-items were removed from catalog items. Suppliers (never
> implemented) was removed entirely. Use the `+` button on each list page to add.

| Parameter | Values |
|-----------|--------|
| `activeMenu` | `dashboard`, `products`, `categories`, `inventory`, `warehouses`, `locations`, `inbound`, `outbound`, `movement`, `transfers`, `customers`, `sales-orders`, `users`, `profile`, `change-password` |
| `activeSubMenu` | `inventory-warehouse`, `inventory-product`, `inbound-list`, `inbound-create`, `outbound-list`, `outbound-create`, `movement-list`, `movement-create`, `transfer-list`, `transfer-create`, `order-list`, `order-create` |

**Notes on `activeSubMenu`:** Only Operation and Sales Order menu items have sub-menus now.
Catalog items (Products, Categories, Warehouses, Locations, Users, Customers) are direct links —
pass only `activeMenu` for those pages.

### Sidebar Menu Sections (role-based visibility)
| Section | Menu Items | Visible To |
|---------|-----------|------------|
| Catalog & Setup | Products, Categories, Warehouses\*, Locations\*\* | All authenticated (manage: Admin/Manager) |
| Operations | Inbound, Outbound, Movement, Transfers, Inventory | Manager, Staff |
| Sales | Customers, Sales Orders | Manager, Sales |
| Administration | Users | Admin only |
| Account | My Profile, Change Password | All |

\* Warehouses: Admin and Manager only  
\*\* Locations: Admin and Manager only (simplified from previous Admin/Manager/Staff)

**Admin sees:** Dashboard · Products · Categories · Warehouses · Locations · Users · Profile · Change Password  
**Admin does NOT see:** Inventory, Inbound, Outbound, Movement, Transfers, Customers, Sales Orders
