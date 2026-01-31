# Smart WMS - Copilot Instructions

## Project Overview
Smart WMS is a **Java 21 + Jakarta EE 10** web application for warehouse management. It runs on **Apache Tomcat 10+** with **SQL Server** as the database. This is an academic project demonstrating warehouse operations with role-based access control—no financial processing.

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
- **Views** (`webapp/WEB-INF/views/`): JSP pages organized by feature, protected under WEB-INF
- **Common Components** (`webapp/WEB-INF/common/`): Shared layout components (header, footer, sidebar, etc.)

## View Layer Structure
```
webapp/
├── index.jsp                    # Entry point - redirects to login or dashboard
├── assets/                      # Static assets (CSS, JS, images, fonts)
└── WEB-INF/
    ├── web.xml                  # Jakarta EE 10 configuration
    ├── common/                  # Reusable layout components
    │   ├── head.jsp             # HTML head (meta, CSS, fonts)
    │   ├── sidebar.jsp          # Left navigation menu (role-based)
    │   ├── navbar.jsp           # Top navigation bar with user dropdown
    │   ├── footer.jsp           # Page footer
    │   ├── scripts.jsp          # Common JavaScript includes
    │   ├── alerts.jsp           # Alert messages component
    │   ├── pagination.jsp       # Pagination component
    │   └── delete-modal.jsp     # Delete confirmation modal
    └── views/
        ├── auth/                # Authentication pages (login, register)
        ├── dashboard.jsp        # Main dashboard
        ├── product/             # Product management
        ├── category/            # Category management
        ├── inventory/           # Inventory views
        ├── error/               # Error pages (404, 403, 500, etc.)
        └── [feature]/           # Other feature modules
```

## Key Patterns & Conventions

### Servlet Controllers
- Use `@WebServlet("/path")` annotation (no web.xml mapping)
- Route via `action` parameter: `?action=login`, `?action=register`
- Initialize services in `init()` method
- Forward to JSP in WEB-INF: `request.getRequestDispatcher("/WEB-INF/views/feature/page.jsp").forward(request, response)`

### DAO Pattern
```java
try (Connection conn = DBConnection.getConnection();
     PreparedStatement stmt = conn.prepareStatement(sql)) {
    // Always use try-with-resources
    // Use PreparedStatement for SQL injection prevention
}
```

### Password Security
- Use `PasswordUtil.hashPassword(password)` for hashing (SHA-256 + salt)
- Use `PasswordUtil.verifyPassword(password, storedHash)` for verification
- Hash format: `salt:hash` (both Base64 encoded)

### Role-Based Access Control
Roles: `Admin`, `Manager`, `Staff`, `Sales`
- Authorization enforced by `AuthFilter` using `ROLE_ACCESS_MAP`
- Check role in controllers: `((User) session.getAttribute("user")).getRole()`
- Public URLs bypass auth: `/auth`, `/assets`, `/css`, `/js`, `/libs`

### Session Management
- 30-minute timeout configured in `web.xml`
- Session attributes: `user` (User object), `userId`, `role`
- Get session: `request.getSession(false)` (returns null if none exists)

### JSP Views (JSTL + EL Only)
**CRITICAL: NO SCRIPTLETS ALLOWED**
- Use **JSTL tags** and **EL (Expression Language)** exclusively in all JSP files
- **NEVER use scriptlets**: `<% %>`, `<%= %>`, `<%! %>`
- Always include JSTL core taglib: `<%@ taglib prefix="c" uri="jakarta.tags.core" %>`

### Using Common Layout Components
All pages with sidebar/navbar must include the common components:

```jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html lang="en" class="layout-menu-fixed layout-compact" 
      data-assets-path="${contextPath}/assets/">
<head>
    <jsp:include page="/WEB-INF/common/head.jsp">
        <jsp:param name="pageTitle" value="Page Title" />
    </jsp:include>
</head>
<body>
    <div class="layout-wrapper layout-content-navbar">
        <div class="layout-container">
            
            <!-- Sidebar with active menu -->
            <jsp:include page="/WEB-INF/common/sidebar.jsp">
                <jsp:param name="activeMenu" value="products" />
                <jsp:param name="activeSubMenu" value="product-list" />
            </jsp:include>
            
            <div class="layout-page">
                <jsp:include page="/WEB-INF/common/navbar.jsp" />
                
                <div class="content-wrapper">
                    <div class="container-xxl flex-grow-1 container-p-y">
                        <!-- Include alerts -->
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

### Alert Messages
Set these request attributes in servlets to show alerts:
- `successMessage` - Green success alert
- `errorMessage` - Red error alert
- `warningMessage` - Yellow warning alert
- `infoMessage` - Blue info alert

```java
request.setAttribute("successMessage", "Product created successfully!");
request.getRequestDispatcher("/WEB-INF/views/product/list.jsp").forward(request, response);
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
    <c:param name="id" value="${category.categoryId}"/>
</c:url>

<!-- Set variables -->
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
```

**EL Expression Examples:**
```jsp
${user.username}                    <!-- Property access -->
${product.price * quantity}         <!-- Arithmetic -->
${status == 'Active'}              <!-- Comparison -->
${not empty errorMessage}          <!-- Logical operators -->
${sessionScope.user.role}          <!-- Scope specification -->
${param.action}                    <!-- Request parameters -->
```

## Developer Workflow

### Build & Run
```bash
mvn clean package                    # Build WAR file
# Copy target/buildms.war to Tomcat webapps/
# Access: http://localhost:8080/buildms/
```

### Database Setup
```bash
sqlcmd -S localhost -i database/schema.sql           # Create schema
sqlcmd -S localhost -d smartwms_db -i database/auth_migration.sql  # Add test users
```

### Database Connection
Edit `src/main/resources/db.properties`:
```properties
db.url=jdbc:sqlserver://localhost;databaseName=smartwms_db;encrypt=true;trustServerCertificate=true
db.username=your_username
db.password=your_password
db.driver=com.microsoft.sqlserver.jdbc.SQLServerDriver
```
Configuration is automatically loaded by `DBConnection.java` at startup.

### Test Credentials
| Username | Password | Role |
|----------|----------|------|
| admin | password123 | Admin |
| manager | password123 | Manager |
| staff | password123 | Staff |
| sales | password123 | Sales |

## Important Files
- [schema.sql](database/schema.sql) - Database schema (all tables)
- [AuthFilter.java](src/main/java/vn/edu/fpt/swp/filter/AuthFilter.java) - Role permissions map
- [document/detail-design/](document/detail-design/) - Use case specifications (**MUST follow strictly**)
- [document/SRS.md](document/SRS.md) - Feature requirements
- [template/](template/) - UI templates (Bootstrap 5) - **MUST use for all new views**

## ⚠️ CRITICAL: Follow Detail Design & Templates

### Detail Design Documents (MANDATORY)
Before implementing any feature, **read and follow the corresponding use case document** in `document/detail-design/`:
- Each UC-*.md file defines exact flows, validations, error messages, and business rules
- Follow the Main Flow steps precisely
- Implement all Alternative Flows for error handling
- Respect the Access Control section for role permissions
- Example: For login feature → follow [UC-AUTH-001-User-Login.md](document/detail-design/UC-AUTH-001-User-Login.md)

### UI Templates (MANDATORY)
All JSP views **MUST use the Bootstrap 5 templates** from `template/`:
- Copy HTML structure from `template/html/` files
- Use assets from `template/assets/` (already copied to `webapp/assets/`)
- Reference `template/html/auth-login-basic.html` for auth pages
- Reference `template/html/tables-basic.html` for list views
- Reference `template/html/form-layouts-vertical.html` for forms
- Maintain consistent look-and-feel across all pages

## Request/Inventory Workflow
Requests follow: `Created → Approved → InProgress → Completed` (or `Rejected`)
- Types: `Inbound`, `Outbound`, `Transfer`, `Internal`
- Sales Orders trigger Outbound requests (no direct inventory modification)
- Inventory keyed by: `(ProductId, WarehouseId, LocationId)`

## When Adding New Features
1. **Read the detail design** in `document/detail-design/UC-*.md` for exact requirements
2. Create Model in `model/` with getters/setters
3. Create DAO in `dao/` using `DBConnection` pattern
4. Create Service in `service/` for business logic
5. Create Controller in `controller/` with `@WebServlet`
6. Create JSP views in `webapp/WEB-INF/views/feature/` **using layout components from `WEB-INF/common/`**
7. Update `AuthFilter.ROLE_ACCESS_MAP` if new protected routes
8. Verify implementation matches all flows in the detail design document

## Sidebar Menu Configuration
The sidebar (`WEB-INF/common/sidebar.jsp`) uses parameters to highlight active menu items:

| Parameter | Values |
|-----------|--------|
| `activeMenu` | `dashboard`, `products`, `categories`, `inventory`, `warehouses`, `locations`, `inbound`, `outbound`, `movement`, `customers`, `sales-orders`, `users`, `suppliers`, `profile`, `change-password` |
| `activeSubMenu` | `product-list`, `product-add`, `category-list`, `category-add`, `inbound-list`, `inbound-create`, etc. |

Menu items are role-based and automatically show/hide based on `sessionScope.user.role`.
