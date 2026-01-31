# UC-INB Implementation Progress

## Module Overview
**Module:** Inbound Requests (INB)  
**Total Use Cases:** 3  
**Completed:** 3  
**Status:** ✅ Complete  
**Implementation Date:** January 31, 2026

## Use Cases Implemented

### UC-INB-001: Create Inbound Request
**Status:** ✅ Completed

**Description:** Create a new inbound request to receive products into a warehouse.

**Files Modified/Created:**
- `src/main/java/vn/edu/fpt/swp/controller/InboundController.java` - Create request handling
- `src/main/java/vn/edu/fpt/swp/service/InboundService.java` - Creation logic
- `src/main/java/vn/edu/fpt/swp/dao/RequestDAO.java` - Request persistence
- `src/main/java/vn/edu/fpt/swp/dao/RequestItemDAO.java` - Request items persistence
- `src/main/webapp/WEB-INF/views/inbound/create.jsp` - Create form view
- `src/main/webapp/WEB-INF/views/inbound/list.jsp` - Inbound requests list

**Features:**
- Select destination warehouse
- Add multiple products with quantities
- Select target location for each item
- Supplier selection (optional)
- Notes/description field
- Request created with "Created" status

**Business Rules Implemented:**
- BR-INB-001: Destination warehouse required
- BR-INB-002: At least one item required
- BR-INB-003: Quantity must be positive
- BR-INB-004: Product must be active
- BR-INB-005: Location must belong to selected warehouse

**Access Control:**
- Admin, Manager, Staff

---

### UC-INB-002: Approve Inbound Request
**Status:** ✅ Completed

**Description:** Approve or reject pending inbound requests.

**Files Modified/Created:**
- `src/main/java/vn/edu/fpt/swp/controller/InboundController.java` - Approve/reject actions
- `src/main/java/vn/edu/fpt/swp/service/InboundService.java` - Approval logic
- `src/main/webapp/WEB-INF/views/inbound/details.jsp` - Request details with approve/reject buttons

**Features:**
- View request details before approval
- Approve action → status changes to "Approved"
- Reject action → status changes to "Rejected"
- Rejection reason (optional)
- Approval timestamp recorded
- Approver user recorded

**Business Rules Implemented:**
- BR-INB-006: Only "Created" requests can be approved
- BR-INB-007: Manager or Admin can approve
- BR-INB-008: Cannot approve own request (optional rule)

**Access Control:**
- Admin, Manager only

---

### UC-INB-003: Execute Inbound Request
**Status:** ✅ Completed

**Description:** Execute approved inbound request to update inventory.

**Files Modified/Created:**
- `src/main/java/vn/edu/fpt/swp/controller/InboundController.java` - Execute action
- `src/main/java/vn/edu/fpt/swp/service/InboundService.java` - Execution logic
- `src/main/java/vn/edu/fpt/swp/dao/InventoryDAO.java` - Inventory updates
- `src/main/webapp/WEB-INF/views/inbound/execute.jsp` - Execution confirmation view

**Features:**
- Start execution → status "InProgress"
- Verify received quantities
- Update inventory for each item
- Complete execution → status "Completed"
- Execution timestamp recorded
- Executor user recorded

**Inventory Update Logic:**
```
For each RequestItem:
  - Find or create Inventory record (ProductId, WarehouseId, LocationId)
  - Add received quantity to existing quantity
  - Update lastUpdated timestamp
```

**Business Rules Implemented:**
- BR-INB-009: Only "Approved" requests can be executed
- BR-INB-010: All items must be processed
- BR-INB-011: Quantities must match or be adjusted with notes

**Access Control:**
- Admin, Manager, Staff

---

## Technical Implementation Details

### Request State Flow
```
Created → Approved → InProgress → Completed
    ↓
  Rejected
```

### Key Database Tables
| Table | Purpose |
|-------|---------|
| `Requests` | Main request header |
| `RequestItems` | Line items for each request |
| `Inventory` | Updated on execution |

### Request Model Fields
| Field | Description |
|-------|-------------|
| `requestId` | Primary key |
| `type` | "Inbound" |
| `status` | Created/Approved/Rejected/InProgress/Completed |
| `destinationWarehouseId` | Target warehouse |
| `createdBy` | User who created |
| `approvedBy` | User who approved |
| `completedBy` | User who executed |
| `createdAt` | Creation timestamp |
| `approvedAt` | Approval timestamp |
| `completedAt` | Completion timestamp |

### RequestItem Model Fields
| Field | Description |
|-------|-------------|
| `requestItemId` | Primary key |
| `requestId` | Foreign key to Request |
| `productId` | Product being received |
| `quantity` | Quantity to receive |
| `destinationLocationId` | Target location in warehouse |

### Service Methods
| Method | Purpose |
|--------|---------|
| `createInboundRequest()` | Create new request with items |
| `approveRequest()` | Change status to Approved |
| `rejectRequest()` | Change status to Rejected |
| `startExecution()` | Change status to InProgress |
| `completeExecution()` | Update inventory and mark Completed |

---

## Testing Notes
- Test creating request with multiple items
- Test approval by Manager/Admin
- Test rejection with reason
- Test execution updates inventory correctly
- Test cannot execute non-approved requests
- Test Staff cannot approve requests

## References
- [UC-INB-001-Create-Inbound-Request.md](../document/detail-design/UC-INB-001-Create-Inbound-Request.md)
- [UC-INB-002-Approve-Inbound-Request.md](../document/detail-design/UC-INB-002-Approve-Inbound-Request.md)
- [UC-INB-003-Execute-Inbound-Request.md](../document/detail-design/UC-INB-003-Execute-Inbound-Request.md)
