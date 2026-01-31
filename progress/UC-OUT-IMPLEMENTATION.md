# UC-OUT Implementation Progress

## Module Overview
**Module:** Outbound Requests (OUT)  
**Total Use Cases:** 3  
**Completed:** 3  
**Status:** ✅ Complete  
**Implementation Date:** January 31, 2026

## Use Cases Implemented

### UC-OUT-001: Approve Outbound Request
**Status:** ✅ Completed

**Description:** Approve or reject pending outbound requests.

**Files Modified/Created:**
- `src/main/java/vn/edu/fpt/swp/controller/OutboundController.java` - Approve/reject actions
- `src/main/java/vn/edu/fpt/swp/service/OutboundService.java` - Approval logic
- `src/main/webapp/WEB-INF/views/outbound/list.jsp` - Outbound requests list
- `src/main/webapp/WEB-INF/views/outbound/details.jsp` - Request details with actions

**Features:**
- View request details before approval
- Check inventory availability before approval
- Approve action → status changes to "Approved"
- Reject action → status changes to "Rejected"
- Rejection reason field
- Approval timestamp and approver recorded

**Business Rules Implemented:**
- BR-OUT-001: Only "Created" requests can be approved
- BR-OUT-002: Inventory must be sufficient for all items
- BR-OUT-003: Manager or Admin can approve
- BR-OUT-004: Approval reserves inventory (soft lock)

**Access Control:**
- Admin, Manager only

---

### UC-OUT-002: Execute Outbound Request
**Status:** ✅ Completed

**Description:** Execute approved outbound request to deduct inventory.

**Files Modified/Created:**
- `src/main/java/vn/edu/fpt/swp/controller/OutboundController.java` - Execute action
- `src/main/java/vn/edu/fpt/swp/service/OutboundService.java` - Execution logic
- `src/main/java/vn/edu/fpt/swp/dao/InventoryDAO.java` - Inventory deduction
- `src/main/webapp/WEB-INF/views/outbound/execute.jsp` - Execution view

**Features:**
- Start execution → status "InProgress"
- Pick items from specified locations
- Deduct inventory for each item
- Complete execution → status "Completed"
- Execution timestamp recorded
- Executor user recorded

**Inventory Update Logic:**
```
For each RequestItem:
  - Find Inventory record (ProductId, WarehouseId, LocationId)
  - Verify quantity >= requested
  - Subtract requested quantity
  - Update lastUpdated timestamp
  - If quantity becomes 0, optionally remove record
```

**Business Rules Implemented:**
- BR-OUT-005: Only "Approved" requests can be executed
- BR-OUT-006: Inventory must still be sufficient at execution time
- BR-OUT-007: All items must be processed
- BR-OUT-008: Partial execution not allowed (all or nothing)

**Access Control:**
- Admin, Manager, Staff

---

### UC-OUT-003: Create Internal Outbound Request
**Status:** ✅ Completed

**Description:** Create an outbound request for internal purposes (not from sales order).

**Files Modified/Created:**
- `src/main/java/vn/edu/fpt/swp/controller/OutboundController.java` - Create action
- `src/main/java/vn/edu/fpt/swp/service/OutboundService.java` - Creation logic
- `src/main/webapp/WEB-INF/views/outbound/create.jsp` - Create form

**Features:**
- Select source warehouse
- Add multiple products with quantities
- Select source location for each item
- Reason/notes field for internal use
- Validate inventory availability
- Request created with "Created" status

**Business Rules Implemented:**
- BR-OUT-009: Source warehouse required
- BR-OUT-010: At least one item required
- BR-OUT-011: Quantity must be positive
- BR-OUT-012: Product must exist in specified location
- BR-OUT-013: Quantity cannot exceed available inventory

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

### Outbound Types
| Type | Source | Description |
|------|--------|-------------|
| Sales Order | Sales module | Generated from confirmed sales orders |
| Internal | Manual | Created by staff for internal needs |

### Key Database Tables
| Table | Purpose |
|-------|---------|
| `Requests` | Main request header |
| `RequestItems` | Line items with source locations |
| `Inventory` | Deducted on execution |

### Request Model Fields (Outbound-specific)
| Field | Description |
|-------|-------------|
| `type` | "Outbound" |
| `sourceWarehouseId` | Warehouse to pick from |
| `salesOrderId` | Link to sales order (if applicable) |
| `customerId` | Customer (for sales-related) |

### RequestItem Model Fields (Outbound-specific)
| Field | Description |
|-------|-------------|
| `sourceLocationId` | Location to pick from |
| `quantity` | Quantity to ship |

### Service Methods
| Method | Purpose |
|--------|---------|
| `createOutboundRequest()` | Create internal outbound request |
| `validateInventoryAvailability()` | Check stock before approval/creation |
| `approveRequest()` | Approve with inventory check |
| `rejectRequest()` | Reject with reason |
| `startExecution()` | Begin picking process |
| `completeExecution()` | Deduct inventory and complete |

### Inventory Validation
```java
public boolean validateInventoryAvailability(List<RequestItem> items) {
    for (RequestItem item : items) {
        Inventory inv = inventoryDAO.findByProductWarehouseLocation(
            item.getProductId(),
            item.getWarehouseId(),
            item.getSourceLocationId()
        );
        if (inv == null || inv.getQuantity() < item.getQuantity()) {
            return false;
        }
    }
    return true;
}
```

---

## Comparison: Inbound vs Outbound

| Aspect | Inbound | Outbound |
|--------|---------|----------|
| Direction | Into warehouse | Out of warehouse |
| Warehouse Field | destinationWarehouseId | sourceWarehouseId |
| Location Field | destinationLocationId | sourceLocationId |
| Inventory Effect | Add quantity | Subtract quantity |
| Pre-validation | None | Check availability |
| Linked Entity | Supplier (optional) | Customer/Sales Order |

---

## Testing Notes
- Test creating internal outbound with multiple items
- Test approval checks inventory availability
- Test rejection with reason
- Test execution deducts inventory correctly
- Test cannot execute if inventory insufficient
- Test Staff cannot approve requests
- Test cannot request more than available

## References
- [UC-OUT-001-Approve-Outbound-Request.md](../document/detail-design/UC-OUT-001-Approve-Outbound-Request.md)
- [UC-OUT-002-Execute-Outbound-Request.md](../document/detail-design/UC-OUT-002-Execute-Outbound-Request.md)
- [UC-OUT-003-Create-Internal-Outbound-Request.md](../document/detail-design/UC-OUT-003-Create-Internal-Outbound-Request.md)
