# Smart Warehouse Management System (Smart WMS)

A comprehensive Java-based web application for managing complete warehouse operations using **Jakarta EE 10**, JSP, Servlets, and SQL Server. This is a fully-featured warehouse management system with authentication, role-based access control, inventory management, and sales-driven fulfillment workflows.

## 🎉 Project Status: 100% Complete

**All 48 use cases across 13 modules have been successfully implemented!**

- ✅ **Authentication & Authorization** (5 use cases)
- ✅ **Category Management** (4 use cases)
- ✅ **Product Management** (5 use cases)
- ✅ **Warehouse Management** (3 use cases)
- ✅ **Location Management** (4 use cases)
- ✅ **User Management** (5 use cases)
- ✅ **Customer Management** (4 use cases)
- ✅ **Inbound Operations** (3 use cases)
- ✅ **Inventory Tracking** (3 use cases)
- ✅ **Internal Movement** (2 use cases)
- ✅ **Outbound Operations** (3 use cases)
- ✅ **Sales Orders** (4 use cases)
- ✅ **Transfer Operations** (3 use cases)

📊 **Progress Details**: See [OVERALL_PROGRESS.md](progress/OVERALL_PROGRESS.md)

## 🚀 Quick Start

### Test Credentials

Login at `http://localhost:8080/buildms/` with these accounts:

| Username | Password | Role | Access |
|----------|----------|------|--------|
| `admin` | `password123` | Admin | Full system access |
| `manager` | `password123` | Manager | Warehouse operations |
| `staff` | `password123` | Staff | Execute requests |
| `sales` | `password123` | Sales | Sales orders only |

**⚠️ Change passwords immediately in production!**

## 🌐 API Endpoints

### Authentication
| Method | URL | Description | Access |
|--------|-----|-------------|--------|
| GET | `/auth?action=login` | Show login page | Public |
| POST | `/auth` (action=login) | Process login | Public |
| GET | `/auth?action=register` | Show registration page | Public |
| POST | `/auth` (action=register) | Register new user | Public |
| GET | `/auth?action=logout` | Logout | Authenticated |
| GET | `/auth?action=changePassword` | Change password form | Authenticated |
| POST | `/auth?action=changePassword` | Update password | Authenticated |

### Product Management
| Method | URL | Description | Access |
|--------|-----|-------------|--------|
| GET | `/product` | List all products | All Roles |
| GET | `/product?action=view&id={id}` | View product details | All Roles |
| GET | `/product?action=create` | Show create form | Admin, Manager |
| POST | `/product?action=create` | Create new product | Admin, Manager |
| GET | `/product?action=edit&id={id}` | Show edit form | Admin, Manager |
| POST | `/product?action=update` | Update product | Admin, Manager |
| POST | `/product?action=toggleStatus` | Toggle active/inactive | Admin, Manager |

### Category Management
| Method | URL | Description | Access |
|--------|-----|-------------|--------|
| GET | `/category` | List all categories | All Roles |
| GET | `/category?action=create` | Show create form | Admin, Manager |
| POST | `/category?action=create` | Create category | Admin, Manager |
| GET | `/category?action=edit&id={id}` | Show edit form | Admin, Manager |
| POST | `/category?action=update` | Update category | Admin, Manager |
| POST | `/category?action=delete&id={id}` | Delete category | Admin, Manager |

### Warehouse Management
| Method | URL | Description | Access |
|--------|-----|-------------|--------|
| GET | `/warehouse` | List warehouses | All Roles |
| GET | `/warehouse?action=create` | Show create form | Admin, Manager |
| POST | `/warehouse?action=create` | Create warehouse | Admin, Manager |
| GET | `/warehouse?action=edit&id={id}` | Show edit form | Admin, Manager |
| POST | `/warehouse?action=update` | Update warehouse | Admin, Manager |

### Location Management
| Method | URL | Description | Access |
|--------|-----|-------------|--------|
| GET | `/location` | List locations | All Roles |
| GET | `/location?action=create` | Show create form | Admin, Manager |
| POST | `/location?action=create` | Create location | Admin, Manager |
| GET | `/location?action=edit&id={id}` | Show edit form | Admin, Manager |
| POST | `/location?action=update` | Update location | Admin, Manager |
| POST | `/location?action=toggleStatus` | Toggle status | Admin, Manager |

### Inbound Operations
| Method | URL | Description | Access |
|--------|-----|-------------|--------|
| GET | `/inbound` | List inbound requests | All Roles |
| GET | `/inbound?action=create` | Show create form | Admin, Manager |
| POST | `/inbound?action=create` | Create inbound request | Admin, Manager |
| GET | `/inbound?action=view&id={id}` | View request details | All Roles |
| POST | `/inbound?action=approve` | Approve request | Manager |
| POST | `/inbound?action=reject` | Reject request | Manager |
| GET | `/inbound?action=execute&id={id}` | Show execution form | Staff |
| POST | `/inbound?action=execute` | Execute receiving | Staff |

### Outbound Operations
| Method | URL | Description | Access |
|--------|-----|-------------|--------|
| GET | `/outbound` | List outbound requests | All Roles |
| GET | `/outbound?action=create` | Show create form | Admin, Manager |
| POST | `/outbound?action=create` | Create outbound request | Admin, Manager |
| GET | `/outbound?action=view&id={id}` | View request details | All Roles |
| POST | `/outbound?action=approve` | Approve request | Manager |
| POST | `/outbound?action=reject` | Reject request | Manager |
| GET | `/outbound?action=execute&id={id}` | Show execution form | Staff |
| POST | `/outbound?action=execute` | Execute shipment | Staff |

### Sales Order Management
| Method | URL | Description | Access |
|--------|-----|-------------|--------|
| GET | `/sales-order` | List sales orders | Sales, Manager, Admin |
| GET | `/sales-order?action=create` | Show create form | Sales |
| POST | `/sales-order?action=create` | Create sales order | Sales |
| GET | `/sales-order?action=view&id={id}` | View order details | Sales, Manager, Admin |
| POST | `/sales-order?action=confirm` | Confirm order | Manager |
| POST | `/sales-order?action=generateOutbound` | Generate outbound | Manager |
| POST | `/sales-order?action=cancel` | Cancel order | Sales, Manager |

### Internal Movement
| Method | URL | Description | Access |
|--------|-----|-------------|--------|
| GET | `/movement` | List movements | All Roles |
| GET | `/movement?action=create` | Show create form | Admin, Manager, Staff |
| POST | `/movement?action=create` | Create movement | Admin, Manager, Staff |
| GET | `/movement?action=execute&id={id}` | Show execution form | Staff |
| POST | `/movement?action=execute` | Execute movement | Staff |

### Transfer Operations
| Method | URL | Description | Access |
|--------|-----|-------------|--------|
| GET | `/transfer` | List transfers | All Roles |
| GET | `/transfer?action=create` | Show create form | Manager |
| POST | `/transfer?action=create` | Create transfer | Manager |
| GET | `/transfer?action=view&id={id}` | View transfer details | All Roles |
| POST | `/transfer?action=executeOutbound` | Execute outbound leg | Staff |
| POST | `/transfer?action=executeInbound` | Execute inbound leg | Staff |

### Inventory Views
| Method | URL | Description | Access |
|--------|-----|-------------|--------|
| GET | `/inventory` | View inventory by warehouse | All Roles |
| GET | `/inventory?action=byProduct` | View inventory by product | All Roles |
| GET | `/inventory?action=search` | Search inventory | All Roles |

### User Management (Admin Only)
| Method | URL | Description | Access |
|--------|-----|-------------|--------|
| GET | `/user` | List all users | Admin |
| GET | `/user?action=create` | Show create form | Admin |
| POST | `/user?action=create` | Create user | Admin |
| GET | `/user?action=edit&id={id}` | Show edit form | Admin |
| POST | `/user?action=update` | Update user | Admin |
| POST | `/user?action=toggleStatus` | Toggle user status | Admin |
| POST | `/user?action=resetPassword` | Reset user password | Admin |
| POST | `/user?action=assignWarehouse` | Assign warehouse | Admin |

### Customer Management
| Method | URL | Description | Access |
|--------|-----|-------------|--------|
| GET | `/customer` | List customers | Sales, Manager, Admin |
| GET | `/customer?action=create` | Show create form | Sales, Manager, Admin |
| POST | `/customer?action=create` | Create customer | Sales, Manager, Admin |
| GET | `/customer?action=edit&id={id}` | Show edit form | Sales, Manager, Admin |
| POST | `/customer?action=update` | Update customer | Sales, Manager, Admin |
| POST | `/customer?action=toggleStatus` | Toggle status | Manager, Admin |

## 📋 Features

### 🔐 Authentication & Security
- **Secure Login/Registration**: SHA-256 password hashing with salt
- **Role-Based Access Control**: Admin, Manager, Staff, and Sales roles with granular permissions
- **Session Management**: 30-minute inactivity timeout
- **Protected Routes**: Authorization filter for all resources (`AuthFilter`)
- **Password Management**: Change password and admin password reset

### 📦 Product & Category Management
- **Product CRUD**: Create, view, update, and toggle product status
- **Category Management**: Organize products into hierarchical categories
- **Product Details**: Comprehensive product information with inventory levels
- **Search & Filter**: Find products by name, category, or SKU
- **Status Management**: Active/Inactive product control

### 🏢 Warehouse & Location Management
- **Multi-Warehouse Support**: Manage multiple warehouse facilities
- **Location Hierarchy**: Organize warehouse locations (Aisles, Racks, Bins)
- **Location Status**: Enable/disable specific storage locations
- **Capacity Tracking**: Monitor available storage capacity

### 📊 Inventory Management
- **Real-Time Tracking**: View inventory by warehouse or by product
- **Multi-Level Search**: Search inventory by product, warehouse, or location
- **Stock Levels**: Monitor current quantities and locations
- **Inventory History**: Track all inventory movements

### 📥 Inbound Operations
- **Create Inbound Requests**: Manager/Admin initiate receiving operations
- **Approval Workflow**: Manager approval required before execution
- **Execute Receiving**: Staff receive goods into specific locations
- **Automatic Inventory Update**: Stock levels updated upon execution

### 📤 Outbound Operations
- **Internal Outbound**: Create outbound requests for internal use
- **Sales Order Integration**: Automatic outbound generation from sales orders
- **Approval Workflow**: Manager approval before execution
- **Execute Shipment**: Staff pick and ship from specific locations
- **Inventory Deduction**: Automatic stock level updates

### 🔄 Internal Movement
- **Location Transfer**: Move inventory between locations within warehouse
- **Two-Step Process**: Create request, then execute movement
- **No Approval Required**: Streamlined internal operations
- **Audit Trail**: Track all internal movements

### 🔀 Transfer Operations
- **Inter-Warehouse Transfer**: Move inventory between warehouses
- **Three-Step Process**: Outbound execution → Transit → Inbound execution
- **Dual Inventory Update**: Deduct from source, add to destination
- **Transfer Tracking**: Monitor transfer status and history

### 🛒 Sales Order Management
- **Create Sales Orders**: Sales role creates customer orders
- **Order Confirmation**: Manager confirms sales orders
- **Automatic Outbound**: Generate outbound requests from confirmed orders
- **Order Cancellation**: Cancel unconfirmed orders
- **Customer Integration**: Link orders to customer database

### 👥 User Management (Admin Only)
- **User CRUD**: Create, update, and manage user accounts
- **Role Assignment**: Assign roles (Admin, Manager, Staff, Sales)
- **Warehouse Assignment**: Link users to specific warehouses
- **Status Control**: Activate/deactivate user accounts
- **Password Reset**: Admin can reset user passwords

### 👤 Customer Management
- **Customer Database**: Maintain customer information
- **Contact Management**: Store customer contact details
- **Status Management**: Active/Inactive customer control
- **Sales Order Integration**: Link customers to sales orders

## 🏗️ Project Structure

```
buildms/
├── src/
│   └── main/
│       ├── java/vn/edu/fpt/swp/
│       │   ├── controller/           # Servlets (HTTP request handlers)
│       │   │   ├── AuthController.java
│       │   │   ├── CategoryController.java
│       │   │   ├── ProductController.java
│       │   │   ├── WarehouseController.java
│       │   │   ├── LocationController.java
│       │   │   ├── UserController.java
│       │   │   ├── CustomerController.java
│       │   │   ├── InboundController.java
│       │   │   ├── OutboundController.java
│       │   │   ├── MovementController.java
│       │   │   ├── TransferController.java
│       │   │   ├── SalesOrderController.java
│       │   │   ├── InventoryController.java
│       │   │   └── DashboardController.java
│       │   │
│       │   ├── service/              # Business logic layer
│       │   │   ├── AuthService.java
│       │   │   ├── CategoryService.java
│       │   │   ├── ProductService.java
│       │   │   ├── WarehouseService.java
│       │   │   ├── LocationService.java
│       │   │   ├── UserService.java
│       │   │   ├── CustomerService.java
│       │   │   ├── RequestService.java
│       │   │   ├── SalesOrderService.java
│       │   │   └── InventoryService.java
│       │   │
│       │   ├── dao/                  # Data access layer
│       │   │   ├── UserDAO.java
│       │   │   ├── CategoryDAO.java
│       │   │   ├── ProductDAO.java
│       │   │   ├── WarehouseDAO.java
│       │   │   ├── LocationDAO.java
│       │   │   ├── CustomerDAO.java
│       │   │   ├── RequestDAO.java
│       │   │   ├── RequestItemDAO.java
│       │   │   ├── SalesOrderDAO.java
│       │   │   ├── SalesOrderItemDAO.java
│       │   │   └── InventoryDAO.java
│       │   │
│       │   ├── model/                # Domain entities (POJOs)
│       │   │   ├── User.java
│       │   │   ├── Category.java
│       │   │   ├── Product.java
│       │   │   ├── Warehouse.java
│       │   │   ├── Location.java
│       │   │   ├── Customer.java
│       │   │   ├── Request.java
│       │   │   ├── RequestItem.java
│       │   │   ├── SalesOrder.java
│       │   │   ├── SalesOrderItem.java
│       │   │   └── Inventory.java
│       │   │
│       │   ├── filter/               # Servlet filters
│       │   │   └── AuthFilter.java   # Authentication & authorization
│       │   │
│       │   └── util/                 # Utility classes
│       │       ├── DBConnection.java # Database connection pool
│       │       └── PasswordUtil.java # Password hashing/verification
│       │
│       ├── resources/
│       │   └── db.properties         # Database configuration
│       │
│       └── webapp/
│           ├── WEB-INF/
│           │   ├── web.xml           # Jakarta EE 10 configuration
│           │   ├── common/           # Reusable components
│           │   │   ├── head.jsp      # HTML head (CSS, meta)
│           │   │   ├── sidebar.jsp   # Navigation menu (role-based)
│           │   │   ├── navbar.jsp    # Top bar with user dropdown
│           │   │   ├── footer.jsp    # Footer
│           │   │   ├── scripts.jsp   # Common JavaScript
│           │   │   ├── alerts.jsp    # Alert messages
│           │   │   ├── pagination.jsp # Pagination control
│           │   │   └── delete-modal.jsp # Delete confirmation
│           │   │
│           │   └── views/            # JSP pages (protected)
│           │       ├── dashboard.jsp # Main dashboard
│           │       ├── auth/         # Login, register
│           │       ├── category/     # Category CRUD
│           │       ├── product/      # Product CRUD
│           │       ├── warehouse/    # Warehouse CRUD
│           │       ├── location/     # Location CRUD
│           │       ├── user/         # User management
│           │       ├── customer/     # Customer CRUD
│           │       ├── inbound/      # Inbound operations
│           │       ├── outbound/     # Outbound operations
│           │       ├── movement/     # Internal movement
│           │       ├── transfer/     # Warehouse transfers
│           │       ├── sales-order/  # Sales order management
│           │       ├── inventory/    # Inventory views
│           │       └── error/        # Error pages (403, 404, 500)
│           │
│           ├── assets/               # Static resources
│           │   ├── css/             # Stylesheets
│           │   ├── js/              # JavaScript
│           │   ├── img/             # Images
│           │   ├── fonts/           # Web fonts
│           │   └── vendor/          # Third-party libraries
│           │
│           └── index.jsp             # Entry point
│
├── database/
│   ├── schema.sql                    # Complete DB schema + sample data
│   ├── user_seed.sql                 # Test user accounts
│   └── full_seed_data.sql           # Comprehensive test data
│
├── document/
│   ├── SRS.md                        # Software Requirements Specification
│   ├── AUTHENTICATION.md             # Auth system documentation
│   ├── AUTH_QUICK_REF.md            # Quick reference guide
│   └── detail-design/               # Use case specifications (48 files)
│       ├── UC-AUTH-001-User-Login.md
│       ├── UC-CAT-001-Create-Category.md
│       ├── UC-PRD-001-Create-Product.md
│       └── ...                       # All 48 use cases
│
├── progress/                         # Implementation tracking
│   ├── OVERALL_PROGRESS.md          # Complete progress summary
│   ├── UC-AUTH-IMPLEMENTATION.md
│   ├── UC-CAT-IMPLEMENTATION.md
│   └── ...                           # Module implementation details
│
├── template/                         # Bootstrap 5 UI templates
│   ├── html/                        # HTML template files
│   └── assets/                      # Template assets
│
├── pom.xml                           # Maven configuration
└── README.md                         # This file
```

## 🏛️ Architecture

This project follows the **MVC (Model-View-Controller)** pattern with clear separation of concerns:

```
┌─────────────┐
│   Browser   │
└──────┬──────┘
       │ HTTP Request
       ▼
┌─────────────────────────────┐
│   Controller Layer          │  ← Servlets (@WebServlet)
│   - Handle HTTP requests    │
│   - Route to services       │
│   - Forward to JSP views    │
└──────────┬──────────────────┘
           │
           ▼
┌─────────────────────────────┐
│   Service Layer             │  ← Business Logic
│   - Validation              │
│   - Orchestration           │
│   - Transaction management  │
└──────────┬──────────────────┘
           │
           ▼
┌─────────────────────────────┐
│   DAO Layer                 │  ← Data Access
│   - JDBC operations         │
│   - PreparedStatements      │
│   - CRUD operations         │
└──────────┬──────────────────┘
           │
           ▼
┌─────────────────────────────┐
│   SQL Server Database       │
└─────────────────────────────┘
```

### Key Design Patterns

- **MVC Pattern**: Clean separation of presentation, business logic, and data
- **DAO Pattern**: Abstraction of data persistence logic
- **Service Layer Pattern**: Encapsulation of business rules
- **Front Controller**: `AuthFilter` intercepts all requests for security
- **Dependency Injection**: Services initialized in servlet `init()` methods
- **Try-with-resources**: Automatic resource management for JDBC connections

### Request Flow Example

1. **User Action** → Browser sends HTTP request
2. **AuthFilter** → Validates session & authorization
3. **Controller Servlet** → Parses request, calls service
4. **Service Layer** → Validates input, applies business logic
5. **DAO Layer** → Executes SQL via PreparedStatement
6. **Database** → Returns data
7. **Controller** → Sets attributes, forwards to JSP
8. **JSP View** → Renders HTML using JSTL/EL
9. **Response** → Browser displays page

## 🛠️ Technologies

| Technology | Version | Purpose |
|------------|---------|---------|
| **Java** | 17 | Backend programming language |
| **Jakarta EE** | 10 | Enterprise application framework |
| **Servlet API** | 6.0 | HTTP request handling |
| **JSP** | 3.1 | View layer (server-side rendering) |
| **JSTL** | 3.0 | JSP tag library (no scriptlets!) |
| **SQL Server** | 2019+ | Relational database |
| **JDBC Driver** | 12.8.1 | Database connectivity |
| **Maven** | 3.8+ | Build & dependency management |
| **Apache Tomcat** | 10.1+ | Servlet container (Jakarta EE 10) |
| **Bootstrap** | 5.3 | Frontend UI framework |
| **jQuery** | 3.6 | JavaScript library |
| **SHA-256** | Built-in | Password hashing with salt |

### Key Dependencies (pom.xml)

```xml
<dependencies>
    <!-- Jakarta Servlet API 6.0 -->
    <dependency>
        <groupId>jakarta.servlet</groupId>
        <artifactId>jakarta.servlet-api</artifactId>
        <version>6.0.0</version>
        <scope>provided</scope>
    </dependency>
    
    <!-- Jakarta JSP API 3.1 -->
    <dependency>
        <groupId>jakarta.servlet.jsp</groupId>
        <artifactId>jakarta.servlet.jsp-api</artifactId>
        <version>3.1.1</version>
        <scope>provided</scope>
    </dependency>
    
    <!-- JSTL 3.0 -->
    <dependency>
        <groupId>jakarta.servlet.jsp.jstl</groupId>
        <artifactId>jakarta.servlet.jsp.jstl-api</artifactId>
        <version>3.0.0</version>
    </dependency>
    
    <!-- SQL Server JDBC Driver -->
    <dependency>
        <groupId>com.microsoft.sqlserver</groupId>
        <artifactId>mssql-jdbc</artifactId>
        <version>12.8.1.jre11</version>
    </dependency>
</dependencies>
```

##  Security & Access Control

### Role-Based Permissions

| Role | Access Level | Allowed Operations |
|------|-------------|-------------------|
| **Admin** | Full System Access | All operations including user management, password resets |
| **Manager** | Warehouse Operations | Approve requests, manage inventory, create warehouses/locations |
| **Staff** | Execution Only | Execute approved requests (inbound, outbound, movement, transfer) |
| **Sales** | Sales Only | Create and view sales orders, view customers |

### Security Features

- **Password Security**: SHA-256 hashing with unique salt per user
- **Session Management**: 30-minute timeout, automatic logout
- **Authorization Filter**: `AuthFilter` validates every request
- **SQL Injection Protection**: All queries use `PreparedStatement`
- **XSS Prevention**: JSTL/EL escapes output by default
- **Protected Resources**: All JSP files under `WEB-INF/` (not directly accessible)
- **Public Endpoints**: Only `/auth`, `/assets`, `/css`, `/js` bypass authentication

### Authentication Flow

```
Login → Validate Credentials → Create Session → Store User Object → Set Timeout
   ↓
Every Request → AuthFilter → Check Session → Check Role → Allow/Deny → Continue/Redirect
   ↓
Logout → Invalidate Session → Redirect to Login
```
### Workflow States

**Request Workflow**: `Created → Approved → InProgress → Completed` (or `Rejected`)  
**Sales Order Workflow**: `Created → Confirmed → (Outbound Generated) → Shipped` (or `Cancelled`)  
**Transfer Workflow**: `Created → Outbound Executed → Inbound Executed → Completed`

📖 **Complete Schema**: See [schema.sql](database/schema.sql)

## 🚀 Setup Instructions

### Prerequisites

- **Java Development Kit (JDK)**: Version 17 or higher
- **Apache Tomcat**: Version 10.1 or higher (Jakarta EE 10 compatible)
- **SQL Server**: 2019 or higher (or SQL Server Express)
- **Maven**: Version 3.8 or higher
- **IDE** (optional): IntelliJ IDEA, Eclipse, or VS Code

### Step 1: Database Setup

```powershell
# Start SQL Server (if not running)
# Then run the schema script

# Option 1: Using sqlcmd (Windows)
sqlcmd -S localhost -i database\schema.sql

# Option 2: Using SQL Server Management Studio (SSMS)
# Open schema.sql and execute it

# The schema.sql includes:
# - Database creation (smartwms_db)
# - All table definitions
# - Sample data for all modules
# - Test user accounts
```

The database script creates:
- Database: `smartwms_db`
- 11 tables with relationships
- Test users (admin, manager, staff, sales)
- Sample categories, products, warehouses, locations
- Sample customers and test data

### Step 2: Configure Database Connection

Edit `src/main/resources/db.properties`:

```properties
db.url=jdbc:sqlserver://localhost;databaseName=smartwms_db;encrypt=true;trustServerCertificate=true
db.username=your_sql_server_username
db.password=your_sql_server_password
db.driver=com.microsoft.sqlserver.jdbc.SQLServerDriver
```

**Common configurations:**

```properties
# Local SQL Server with Windows Authentication
db.url=jdbc:sqlserver://localhost;databaseName=smartwms_db;integratedSecurity=true;trustServerCertificate=true

# SQL Server Express (default instance)
db.url=jdbc:sqlserver://localhost\\SQLEXPRESS;databaseName=smartwms_db;encrypt=true;trustServerCertificate=true

# Remote SQL Server
db.url=jdbc:sqlserver://your-server:1433;databaseName=smartwms_db;encrypt=true;trustServerCertificate=true
```

**Benefits of properties file:**
- No recompilation needed when changing credentials
- Easy environment-specific configuration (dev/staging/prod)
- Keeps sensitive data separate from source code

### Step 3: Build the Project

```powershell
# Navigate to project directory
cd d:\Project\buildms

# Clean and build
mvn clean package

# Output: target/buildms.war
```

**Maven will:**
- Download all dependencies
- Compile Java source code
- Run tests (if any)
- Package into WAR file

### Step 4: Deploy to Tomcat

**Option A: Manual Deployment**
```powershell
# Copy WAR file to Tomcat webapps directory
copy target\buildms.war C:\path\to\tomcat\webapps\

# Tomcat will auto-deploy the WAR file
# Wait for deployment to complete (~10-30 seconds)
```

**Option B: Tomcat Manager**
1. Access Tomcat Manager: `http://localhost:8080/manager`
2. Scroll to "WAR file to deploy"
3. Choose `target/buildms.war`
4. Click "Deploy"

**Option C: IDE Deployment**
- **IntelliJ IDEA**: Configure Run → Tomcat Server → Local
- **Eclipse**: Add to Server → Tomcat 10.x → Add and Remove

### Step 5: Verify Deployment

```powershell
# Check Tomcat logs for successful deployment
# Windows: check catalina.log in tomcat/logs/

# Look for:
# - "Deployment of web application archive [buildms.war] has finished"
# - "DBConnection initialized successfully"
# - No error messages
```

### Step 6: Access the Application

1. **Open browser**: Navigate to `http://localhost:8080/buildms/`
2. **Login page** should appear
3. **Test login** with credentials:
   - Username: `admin`
   - Password: `password123`
4. **Dashboard** should display after successful login

### Troubleshooting

**Database Connection Issues:**
```powershell
# Verify SQL Server is running
Get-Service -Name 'MSSQL$*', 'SQLSERVERAGENT'

# Test connection with sqlcmd
sqlcmd -S localhost -U your_username -P your_password -d smartwms_db

# Check if database exists
sqlcmd -S localhost -Q "SELECT name FROM sys.databases WHERE name='smartwms_db'"
```

**Tomcat Issues:**
```powershell
# Check Tomcat is running
curl http://localhost:8080

# View Tomcat logs
Get-Content C:\path\to\tomcat\logs\catalina.out -Tail 50

# Restart Tomcat
# Windows: Use Tomcat service or batch files
# Linux: sudo systemctl restart tomcat
```

**Build Issues:**
```powershell
# Verify Maven installation
mvn -version

# Clean Maven cache and rebuild
mvn clean install -U

# Skip tests if needed
mvn clean package -DskipTests
```

**Port Conflicts:**
- Default Tomcat port: 8080
- If port 8080 is in use, change in `tomcat/conf/server.xml`
- Or kill process using port: `netstat -ano | findstr :8080`

### Default Port Configuration

| Service | Port | URL |
|---------|------|-----|
| Tomcat | 8080 | http://localhost:8080 |
| Application | 8080 | http://localhost:8080/buildms |
| SQL Server | 1433 | localhost:1433 |

### First Time Setup Checklist

- [ ] Java 17+ installed and JAVA_HOME set
- [ ] SQL Server running and accessible
- [ ] Database created (smartwms_db)
- [ ] Schema and seed data loaded
- [ ] db.properties configured correctly
- [ ] Maven build successful (buildms.war created)
- [ ] Tomcat 10.1+ installed
- [ ] WAR file deployed to Tomcat
- [ ] Application accessible at http://localhost:8080/buildms
- [ ] Login successful with test credentials

## 📚 Documentation

### Project Documentation

| Document | Description |
|----------|-------------|
| [SRS.md](document/SRS.md) | Software Requirements Specification - Complete feature requirements |
| [AUTHENTICATION.md](document/AUTHENTICATION.md) | Authentication system architecture and implementation |
| [AUTH_QUICK_REF.md](document/AUTH_QUICK_REF.md) | Quick reference for authentication features |
| [OVERALL_PROGRESS.md](progress/OVERALL_PROGRESS.md) | Complete implementation progress tracking |

### Use Case Specifications

All 48 use cases are documented in [document/detail-design/](document/detail-design/):

**Authentication (UC-AUTH-001 to 005)**
- User Login, Change Password, Admin Reset, Logout, Session Timeout

**Category Management (UC-CAT-001 to 004)**
- Create, Update, Delete, View Category List

**Product Management (UC-PRD-001 to 005)**
- Create, Update, Toggle Status, View List, View Details

**Warehouse Management (UC-WH-001 to 003)**
- Create, Update, View Warehouse List

**Location Management (UC-LOC-001 to 004)**
- Create, Update, Toggle Status, View Location List

**User Management (UC-USER-001 to 005)**
- Create, Update, Toggle Status, View List, Assign Warehouse

**Customer Management (UC-CUS-001 to 004)**
- Create, Update, Toggle Status, View Customer List

**Inbound Operations (UC-INB-001 to 003)**
- Create, Approve, Execute Inbound Request

**Inventory Views (UC-INV-001 to 003)**
- View by Warehouse, View by Product, Search Inventory

**Internal Movement (UC-MOV-001 to 002)**
- Create, Execute Internal Movement

**Outbound Operations (UC-OUT-001 to 003)**
- Approve, Execute, Create Internal Outbound Request

**Sales Orders (UC-SO-001 to 004)**
- Create, Confirm, Generate Outbound, Cancel Sales Order

**Transfer Operations (UC-TRF-001 to 003)**
- Create, Execute Outbound, Execute Inbound Transfer


**Built with ❤️ using Java and Jakarta EE 10**
