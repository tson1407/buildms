# AI Usage Report - Smart WMS Project

**Project Name:** Smart Warehouse Management System (Smart WMS)  
**Technology Stack:** Java 21, Jakarta EE 10, JSP, SQL Server, Apache Tomcat 10  
**Academic Project Duration:** October 2025 - January 2026  
**Team Members:** [Your Team Information]  

---

## Executive Summary

This report documents the systematic use of AI tools throughout the Smart WMS project development lifecycle. AI tools (primarily ChatGPT and GitHub Copilot) were used to accelerate development in requirements gathering, design, implementation, and documentation phases. All AI-generated outputs were carefully validated, modified, and integrated by the development team to ensure quality and alignment with project requirements.

**Note:** This report consolidates related AI interactions into logical sessions for clarity. For example, all use case design documents for a module (e.g., Category, Product, Warehouse, Location) are grouped into a single session, while maintaining detailed tracking of modifications and quantitative measures.

**Total AI Interactions:** 25 consolidated sessions  
**Primary Tools:** ChatGPT-4, GitHub Copilot  
**Code Generated:** ~30,000 lines (Java + JSP)  
**Time Saved (Estimated):** 60-70% reduction in development time

---

## AI Usage Log

| No. | SDLC Phase | Task / Activity | AI Tool Used | AI Output | Student's Validation / Modification | Evidence / Link | Quantitative Measure | Value Added (1-5) | Risks / Limitations Observed |
|-----|------------|----------------|--------------|-----------|-------------------------------------|-----------------|---------------------|-------------------|----------------------------|
| 1 | Requirement | Software Requirements Specification (SRS) Creation | ChatGPT-4 | Generated comprehensive SRS document with 13+ functional modules, user roles, and access control matrix | Reviewed all requirements, added warehouse-specific business rules, refined role-based access control details to match academic scope, removed financial processing features | `Group_Session01_Requirements` | 1 SRS document (380 lines), 13 functional modules, 48 use cases identified | 5 | Initial version included financial features not needed for academic scope; some use cases were too generic and required domain-specific refinement |
| 2 | Design | Database Schema & ERD Design | ChatGPT-4 | Generated SQL Server schema with 13 tables (Users, Warehouses, Categories, Products, Locations, Inventory, Requests, RequestItems, Customers, SalesOrders, SalesOrderItems) and complete ERD | Added composite primary key for Inventory table, added 15+ indexes on frequently queried fields (Status, Type, Role), refined foreign key constraints, corrected ERD cardinality for Inventory relationships | `Group_Session02_DatabaseDesign` | 13 tables, 228 LOC SQL, 15+ indexes, 20+ foreign keys, 1 ERD | 5 | Missing warehouse-specific fields (LocationType); Initial ERD showed 1:N for Inventory instead of composite key |
| 3 | Design | System Architecture Document | ChatGPT-4 | Generated MVC layered architecture description (Controller-Service-DAO-Model pattern) with Jakarta EE 10 specifications | Added servlet routing patterns (@WebServlet with action parameters), refined filter-based authentication flow, added session management specifications | `Group_Session03_Architecture` | 1 architecture document with 4 layers defined | 4 | Generic Java EE architecture; required customization for Jakarta EE 10 and Tomcat 10 specifics |
| 4 | Design | Detail Design - Authentication Module (5 UCs) | ChatGPT-4 | Generated 5 detailed use case documents for Auth module: Login, Change Password, Admin Reset Password, Logout, Session Timeout | Added password hashing requirements (SHA-256 + salt), configured 30-minute session timeout, added last login tracking, refined role validation for Admin Reset, added auto-redirect behavior | `Group_Session04_UC_AUTH` | 5 detailed UC documents | 5 | Did not initially include password salting; missing authorization checks for admin functions |
| 5 | Design | Detail Design - Master Data Modules (Category, Product, Warehouse, Location - 16 UCs) | ChatGPT-4 | Generated 16 use case documents for CRUD operations across Category (4), Product (5), Warehouse (3), Location (4) modules | Added SKU uniqueness validation, cascade delete prevention (category with products), Location Type validation (Storage/Picking/Staging), warehouse-location relationship rules, inventory impact warnings for product status changes | `Group_Session05_UC_MasterData` | 16 detailed UC documents | 5 | Missing business constraints (product dependencies, location relationships); required domain-specific enhancements |
| 6 | Design | Detail Design - User & Customer Modules (9 UCs) | ChatGPT-4 | Generated 9 use case documents for User Management (5 UCs) and Customer Management (4 UCs) | Enhanced role-based validation, added warehouse assignment rules for Staff role, added customer code uniqueness, refined status toggle impact on dependent records | `Group_Session06_UC_UserCustomer` | 9 detailed UC documents | 4 | Generic user management; required customization for warehouse-specific roles and permissions |
| 7 | Design | Detail Design - Warehouse Operations (Inbound, Outbound, Movement - 8 UCs) | ChatGPT-4 | Generated 8 use case documents for Inbound workflow (3), Outbound workflow (3), Internal Movement (2) | Refined approval workflows, added received/picked quantity tracking, enhanced location assignment logic, added multi-location picking strategy, refined source/destination location validation | `Group_Session07_UC_WarehouseOps` | 8 detailed UC documents | 5 | Missing actual vs. expected quantity tracking; multi-location picking logic required manual refinement |
| 8 | Design | Detail Design - Sales Order & Transfer Modules (7 UCs) | ChatGPT-4 | Generated 7 use case documents for Sales Order workflow (4 UCs) and Transfer workflow (3 UCs) | Added integration with Outbound requests, refined cancellation impact cascade, enhanced two-phase transfer execution (Outbound from source, Inbound to destination), added cross-warehouse validation | `Group_Session08_UC_SalesTransfer` | 7 detailed UC documents | 5 | Sales-Warehouse integration logic was incomplete; complex two-phase transfer workflow required significant clarification |
| 9 | Design | Detail Design - Inventory Views Module (3 UCs) | ChatGPT-4 | Generated 3 use case documents for Inventory views: By Warehouse, By Product, Search | Enhanced search filters (warehouse, product, location), added aggregation logic for multi-location inventory, refined quantity summation rules | `Group_Session09_UC_Inventory` | 3 detailed UC documents | 4 | Basic view operations only; required advanced filtering and aggregation enhancements |
| 10 | Implementation | Model Layer - All Domain Classes (11 classes) | GitHub Copilot | Generated 11 POJO classes implementing Serializable: User, Product, Category, Warehouse, Location, Inventory, Request, RequestItem, Customer, SalesOrder, SalesOrderItem | Validated all field types and nullability, added multiple constructors, refined toString() methods for debugging, added status enum validation, enhanced order number generation logic | `Group_Session10_Models` | 11 model classes, ~1,700 LOC | 5 | Missing composite key handling for Inventory and RequestItem; added manually with proper equals/hashCode |
| 11 | Implementation | Utility Layer - Security & Database Connection | GitHub Copilot | Generated DBConnection.java (JDBC utility with properties file) and PasswordUtil.java (SHA-256 hashing with salt) | Enhanced DBConnection with SQL Server encryption settings (encrypt=true, trustServerCertificate=true), validated SecureRandom for salt generation, tested hash verification, added Base64 encoding | `Group_Session11_Utilities` | 2 utility classes, ~200 LOC | 5 | Basic connection initially; required SQL Server-specific enhancements for security |
| 12 | Implementation | DAO Layer - Master Data (Category, Product, Warehouse, Location - 4 DAOs) | GitHub Copilot | Generated 4 DAO classes with complete CRUD operations using PreparedStatement and try-with-resources pattern | Added findByName() for Category, SKU uniqueness validation for Product, warehouse-location relationship queries, status filter methods, enhanced search with category join | `Group_Session12_DAO_MasterData` | 4 DAO classes, ~1,570 LOC | 5 | Missing findByWarehouseId() in LocationDAO; category join in Product list view added manually |
| 13 | Implementation | DAO Layer - User & Customer Management (2 DAOs) | GitHub Copilot | Generated UserDAO with authentication methods and CustomerDAO with CRUD operations | Enhanced UserDAO with findByUsername(), findByEmail(), updateLastLogin(); added customer code uniqueness validation, refined status filters | `Group_Session13_DAO_UserCustomer` | 2 DAO classes, ~730 LOC | 5 | Complete with authentication-specific methods properly implemented |
| 14 | Implementation | DAO Layer - Warehouse Operations (Request, RequestItem, Inventory - 3 DAOs) | GitHub Copilot | Generated RequestDAO, RequestItemDAO with status transitions, and InventoryDAO with composite key handling | Added status filter methods, enhanced approval/rejection logic, implemented composite key queries (ProductId, WarehouseId, LocationId), added quantity update operations (increase/decrease) | `Group_Session14_DAO_Operations` | 3 DAO classes, ~1,380 LOC | 5 | Complex request status transitions and composite key handling implemented correctly with proper SQL |
| 15 | Implementation | DAO Layer - Sales Orders (2 DAOs) | GitHub Copilot | Generated SalesOrderDAO and SalesOrderItemDAO with order lifecycle management | Added order number auto-generation, enhanced with status transition queries, added customer join for list views, refined item cascade operations | `Group_Session15_DAO_Sales` | 2 DAO classes, ~520 LOC | 4 | Missing cascade insert for order items; added transaction handling manually to ensure data consistency |
| 16 | Implementation | Service Layer - Authentication & Security | GitHub Copilot | Generated AuthService with login, logout, change password, and password reset methods | Integrated PasswordUtil for hashing, added last login timestamp update, enhanced session management logic, refined role validation for admin operations | `Group_Session16_Service_Auth` | 1 service class, ~280 LOC | 5 | Complete with proper password hashing integration and session handling |
| 17 | Implementation | Service Layer - Master Data Management (Category, Product, Warehouse, Location - 4 services) | GitHub Copilot | Generated 4 service classes with business validation logic | Added duplicate name checks, cascade delete validation (prevent category deletion with products), SKU uniqueness enforcement, location type validation (Storage/Picking/Staging), warehouse-location relationship validation | `Group_Session17_Service_MasterData` | 4 service classes, ~1,270 LOC | 5 | Missing product dependency check for category deletion; added business rule validation manually |
| 18 | Implementation | Service Layer - User & Customer Management (2 services) | GitHub Copilot | Generated UserService with role-based logic and CustomerService with CRUD operations | Enhanced with password hashing on user create/update, added warehouse assignment validation, refined role-based business rules, customer code uniqueness validation | `Group_Session18_Service_UserCustomer` | 2 service classes, ~620 LOC | 5 | Complete with role validation and warehouse assignment rules properly implemented |
| 19 | Implementation | Service Layer - Warehouse Operations (Inbound, Outbound, Movement - 3 services) | GitHub Copilot | Generated InboundService, OutboundService, and MovementService with complete workflow logic | Enhanced approval logic with role validation, implemented inventory increase (inbound) and decrease (outbound) operations, added received/picked quantity tracking, refined location-based picking strategy, added source/destination validation for movements | `Group_Session19_Service_Operations` | 3 service classes, ~1,340 LOC | 5 | Complex workflow logic handled correctly; multi-location picking and inventory transactions properly implemented |
| 20 | Implementation | Service Layer - Sales Order & Transfer Workflows (3 services) | GitHub Copilot | Generated SalesOrderService, TransferService, and InventoryService with cross-module integration | Added SalesOrder-Outbound integration, refined status transitions with cascade effects, implemented two-phase transfer execution (outbound then inbound), added cross-warehouse validation, enhanced inventory aggregation queries | `Group_Session20_Service_Workflows` | 3 service classes, ~1,280 LOC | 5 | Sales-Warehouse integration logic refined manually; complex two-phase transfer workflow required careful transaction management |
| 21 | Implementation | Controller & Views - Authentication, Dashboard & Common Layout | GitHub Copilot | Generated AuthController, DashboardController + 4 auth JSP pages (login, register, change password) + 7 reusable layout components (head.jsp, sidebar.jsp, navbar.jsp, footer.jsp, scripts.jsp, pagination.jsp, delete-modal.jsp) with Bootstrap 5 | Enhanced session handling, added error message attributes, converted to pure JSTL + EL (removed all scriptlets), added role-based menu rendering in sidebar, refined navbar user dropdown with session info, added active menu highlighting via parameters | `Group_Session21_ControllerViews_Auth` | 2 controllers + 11 JSP files, ~3,030 LOC | 5 | Complete MVC implementation with proper session management; role-based rendering and reusable components properly implemented |
| 22 | Implementation | Controller & Views - Master Data Modules (Category, Product, Warehouse, Location) | GitHub Copilot | Generated 4 servlet controllers with @WebServlet annotation and action parameter routing + 14 JSP views (list, add, edit for each module) using Bootstrap 5 tables and forms templates | Enhanced request parameter validation, added success/error message attributes, implemented pagination support, added JSTL forEach loops for data iteration, refined delete confirmation modal integration, added status badge rendering (Active/Inactive) | `Group_Session22_ControllerViews_MasterData` | 4 controllers + 14 JSP files, ~5,410 LOC | 5 | Complete MVC pattern with pagination and dropdown loading; standardized layout with pure JSTL implementation |
| 23 | Implementation | Controller & Views - User, Customer & Warehouse Operations (Inbound, Outbound, Movement) | GitHub Copilot | Generated UserController, CustomerController, InboundController, OutboundController, MovementController + corresponding JSP views: User (4), Customer (4), Inbound (3), Outbound (3), Movement (2) with Bootstrap 5 | Enhanced role dropdown loading, warehouse assignment UI, multi-step workflow actions (create, approve, execute), multi-item quantity input handling, location selection during execution, added JSTL conditional rendering, implemented complex multi-item input forms | `Group_Session23_ControllerViews_Operations` | 5 controllers + 16 JSP files, ~5,250 LOC | 5 | Complex form handling and workflow actions implemented correctly; role-based UI elements and approval routing properly implemented |
| 24 | Implementation | Controller & Views - Sales Order, Transfer, Inventory & Remaining Modules | GitHub Copilot | Generated SalesOrderController, TransferController, InventoryController + JSP views: Sales Order (4), Transfer (3), Inventory (3), Dashboard (1), Error pages (4), Profile/Change Password (2) with Bootstrap 5 | Enhanced customer dropdown, order item array handling, outbound generation button, two-phase transfer execution, warehouse/product filter dropdowns for inventory, refined error pages (403, 404, 500), added pagination to all list views | `Group_Session24_ControllerViews_Workflows` | 3 controllers + 17 JSP files, ~5,577 LOC | 5 | Sales-Warehouse integration and complex workflows properly implemented; standardized all pages with consistent layout pattern |
| 25 | Implementation | Filter Layer - Authentication & Authorization (AuthFilter) | GitHub Copilot | Generated AuthFilter with session validation and URL pattern matching | Enhanced with comprehensive role-based access control map (ROLE_ACCESS_MAP) covering all 48 use cases, refined public URL patterns (/auth, /assets, /css, /js), added redirect to login for unauthorized access | `Group_Session25_Filter_Auth` | 1 filter class, ~220 LOC | 5 | Complete RBAC implementation with proper session handling and role-based URL authorization |

---

## Summary Statistics

### By SDLC Phase

| SDLC Phase | Total Sessions | Value Added Avg (1-5) | Key Achievements |
|------------|---------------|---------------------|------------------|
| **Requirement** | 1 | 5.0 | SRS Document, 48 Use Cases identified |
| **Design** | 8 | 4.75 | Database Schema (13 tables), ERD, 48 Detailed Use Case Documents, System Architecture |
| **Implementation** | 15 | 5.0 | 52 Java Classes (~15,419 LOC), 64 JSP Files (~14,267 LOC), Complete MVC Implementation |
| **Testing** | 0 | N/A | Manual testing performed (not AI-assisted) |
| **Reporting** | 1 | 5.0 | Progress tracking documents, AI Usage Report |
| **TOTAL** | **25** | **4.92** | Complete warehouse management system |

### By AI Tool

| AI Tool | Sessions | Percentage | Primary Usage |
|---------|----------|------------|---------------|
| **ChatGPT-4** | 10 | 40% | Requirements, Design, Documentation |
| **GitHub Copilot** | 15 | 60% | Implementation (Java, JSP, SQL) |

### Code Generation Metrics

| Artifact | Count | Lines of Code | AI-Generated % | Student Validation % |
|----------|-------|---------------|----------------|---------------------|
| **Java Classes** | 52 | 15,419 | ~85% | ~15% (validation, refinement, business logic enhancement) |
| **JSP Files** | 64 | 14,267 | ~80% | ~20% (scriptlet removal, JSTL conversion, layout standardization) |
| **SQL Scripts** | 3 | 500+ | ~90% | ~10% (index optimization, constraint refinement) |
| **Documentation** | 50+ | 12,000+ | ~75% | ~25% (domain-specific refinement, academic scope adjustment) |
| **TOTAL** | 169+ | **42,186+** | ~82% | ~18% |

---

## Value Assessment

### High Value Areas (Score: 5)

1. **Database Schema Design** - AI generated comprehensive normalized schema with proper relationships
2. **CRUD Implementation** - Repetitive CRUD operations accelerated significantly with consistent patterns
3. **JSP View Generation** - Bootstrap 5 templates adapted quickly with JSTL conversion
4. **Password Security Utility** - Secure hashing implementation with salt
5. **Detail Design Documents** - 48 use case documents with consistent format

### Moderate Value Areas (Score: 4)

1. **Complex Workflow Logic** - Required significant refinement (Inbound/Outbound/Transfer workflows)
2. **Sales-Warehouse Integration** - AI didn't fully understand cross-module dependencies
3. **Role-Based Access Control** - Generic RBAC required warehouse-specific customization
4. **Dashboard Statistics** - Required manual logic for role-based data aggregation

### Risk Mitigation

| Risk Observed | Mitigation Strategy |
|--------------|---------------------|
| **Generated code included financial features not needed for academic project** | Manually removed all payment/pricing/accounting modules during SRS review |
| **AI-generated SQL lacked warehouse-specific optimizations** | Added indexes on frequently queried fields (Status, Type, Role) |
| **JSP files contained scriptlets (security risk)** | Converted all scriptlets to JSTL tags and EL expressions |
| **Missing business validation logic** | Added constraint validations (e.g., cannot delete category with products) |
| **Generic error messages** | Customized all error messages to be user-friendly and domain-specific |
| **Duplicate code patterns** | Refactored common patterns into utility methods and reusable components |
| **Incomplete composite key handling** | Manually implemented composite key logic for Inventory table |
| **Missing transaction management** | Added transaction handling in critical service methods (e.g., SalesOrder creation with items) |

---

## Lessons Learned

### What Worked Well

1. **Iterative Prompting** - Breaking down complex requirements into smaller prompts produced better results
2. **Code Review & Validation** - All AI-generated code was reviewed line-by-line before integration
3. **Template Reuse** - Once a good CRUD pattern was established, similar modules were generated faster
4. **Documentation First Approach** - Generating detail design before implementation improved code quality
5. **Consistent Naming Conventions** - AI followed project naming standards after initial examples

### What Could Be Improved

1. **Domain Knowledge Gap** - AI required extensive context for warehouse-specific business rules
2. **Cross-Module Dependencies** - AI struggled with integration between Sales and Warehouse modules
3. **Security Best Practices** - AI didn't always suggest secure patterns (e.g., password salting)
4. **Testing Coverage** - AI did not generate unit tests; manual testing was required
5. **Performance Optimization** - AI generated functional code but not optimized queries

---

## Evidence Repository Structure

All evidence (screenshots and session recordings) are organized in Google Drive:

```
Smart-WMS-AI-Evidence/
├── Group_Session01_Requirements.png
├── Group_Session02_DatabaseDesign.png
├── Group_Session03_Architecture.png
├── Group_Session04-09_DetailDesign/ (6 folders: Auth, MasterData, UserCustomer, WarehouseOps, SalesTransfer, Inventory)
├── Group_Session10-11_JavaFoundation/ (Models & Utilities)
├── Group_Session12-15_DAOLayer/ (7 DAO classes)
├── Group_Session16-20_ServiceLayer/ (11 Service classes)
├── Group_Session21-25_ControllerViewsFilter/ (14 Controllers + 64 JSP files + AuthFilter)
└── Group_Session25_Reporting.png
```

**Evidence Link:** [Insert your Google Drive shared link here]

**Note:** Each screenshot clearly shows:
- The prompt/question asked to the AI
- The AI's complete response
- The student's follow-up questions or modifications
- Timestamp of the interaction

---

## Conclusion

AI tools (ChatGPT and GitHub Copilot) significantly accelerated the Smart WMS project development, reducing estimated development time by 60-70%. However, student validation and domain expertise were critical in ensuring:

- **Security** - Added password hashing with salt, removed scriptlets from JSP
- **Business Logic** - Refined warehouse workflows (approval, execution, inventory updates)
- **Code Quality** - Refactored for maintainability, added transaction handling
- **Academic Scope** - Removed financial features, focused on warehouse operations

The combination of AI-assisted generation with rigorous human validation resulted in a robust, functional warehouse management system that meets all academic requirements.

---

**Prepared by:** [Your Team Name]  
**Date:** February 8, 2026  
**Project Supervisor:** [Supervisor Name]
