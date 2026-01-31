# UC-MOV Implementation Progress

## Overview
Implementation of Internal Movement use cases for Smart WMS.

## Use Cases

### UC-MOV-001: Create Internal Movement Request
**Status:** ✅ Completed

**Implementation Details:**
- **Service:** `MovementService.createMovementRequest()` - creates movement request with items
- **Service:** `MovementService.validateMovementItem()` - validates individual movement items
- **Service:** `MovementService.getProductsWithInventoryAtWarehouse()` - gets products for dropdown
- **Controller:** `MovementController.showCreateForm()` - displays warehouse and item form
- **Controller:** `MovementController.createRequest()` - processes form submission
- **View:** `/WEB-INF/views/movement/create.jsp` - two-step form with dynamic item addition

**Features Implemented:**
- Two-step creation flow (select warehouse, then add items)
- Staff auto-selection of assigned warehouse
- Dynamic item addition with JavaScript
- Product dropdown filtered by warehouse inventory
- Source location dropdown shows only locations with inventory
- Available quantity display when selecting source location
- Destination location validation (must be different from source)
- Quantity validation against source availability
- Optional notes field
- Client-side and server-side validation

**Business Rules Implemented:**
- ✅ BR-MOV-001: Staff can create internal movement requests
- ✅ BR-MOV-002: Movement within same warehouse only
- ✅ BR-MOV-003: Source and destination must be different
- ✅ BR-MOV-004: Quantity limited by source availability
- ✅ BR-MOV-005: Total warehouse inventory unchanged (enforced on execution)

**Access Control:**
- ✅ Admin: Can create movements
- ✅ Manager: Can create movements
- ✅ Staff: Can create movements
- ✅ Sales: Cannot create movements (redirected)

---

### UC-MOV-002: Execute Internal Movement
**Status:** ✅ Completed

**Implementation Details:**
- **Service:** `MovementService.startExecution()` - transitions status to InProgress
- **Service:** `MovementService.completeMovement()` - executes inventory changes and completes
- **Service:** `MovementService.getRequestItemsWithDetails()` - retrieves items with product/location info
- **Controller:** `MovementController.showExecutionForm()` - displays execution interface
- **Controller:** `MovementController.startExecution()` - starts movement execution
- **Controller:** `MovementController.completeExecution()` - completes and updates inventory
- **View:** `/WEB-INF/views/movement/execute.jsp` - execution interface with instructions

**Features Implemented:**
- Start Movement button (Created → InProgress)
- Complete Movement button with confirmation
- Execution instructions displayed
- Items table showing:
  - Product details
  - Source/destination locations
  - Quantity to move
  - Current available at source (with warning if insufficient)
  - Current quantity at destination
- Expected result preview (before/after quantities)
- Inventory updates on completion:
  - Decrease source location
  - Increase destination location
- Error handling for insufficient inventory

**State Transitions:**
- ✅ Created → InProgress (Staff starts)
- ✅ InProgress → Completed (Staff completes)

**Inventory Impact:**
- ✅ Source Location: Decrease quantity by moved amount
- ✅ Destination Location: Increase quantity by moved amount
- ✅ Warehouse Total: No change

**Access Control:**
- ✅ Admin: Can execute movements
- ✅ Manager: Can execute movements
- ✅ Staff: Can execute movements
- ✅ Sales: Cannot execute movements (redirected)

---

## Additional Features

### Movement List View
- **View:** `/WEB-INF/views/movement/list.jsp`
- Filter by status (Created, InProgress, Completed)
- Filter by warehouse (hidden for Staff)
- Status badges with color coding
- Action buttons (View Details, Execute)

### Movement Details View
- **View:** `/WEB-INF/views/movement/details.jsp`
- Request information card
- Movement items table
- Completed by user info (when applicable)
- Link to execution page

---

## Files Created/Modified

### Service Layer
- `src/main/java/vn/edu/fpt/swp/service/MovementService.java` (NEW)

### Controller Layer
- `src/main/java/vn/edu/fpt/swp/controller/MovementController.java` (NEW)

### View Layer
- `src/main/webapp/WEB-INF/views/movement/list.jsp` (NEW)
- `src/main/webapp/WEB-INF/views/movement/create.jsp` (NEW)
- `src/main/webapp/WEB-INF/views/movement/details.jsp` (NEW)
- `src/main/webapp/WEB-INF/views/movement/execute.jsp` (NEW)

### Existing Files Used
- `src/main/java/vn/edu/fpt/swp/dao/RequestDAO.java` (existing)
- `src/main/java/vn/edu/fpt/swp/dao/RequestItemDAO.java` (existing)
- `src/main/java/vn/edu/fpt/swp/dao/InventoryDAO.java` (existing)
- `src/main/java/vn/edu/fpt/swp/model/Request.java` (existing)
- `src/main/java/vn/edu/fpt/swp/model/RequestItem.java` (existing)
- `src/main/webapp/WEB-INF/common/sidebar.jsp` (existing, already has movement menu)

---

## Testing Notes

### Test Scenarios
1. **Create Movement:**
   - Select warehouse and verify locations/products load correctly
   - Add multiple movement items
   - Verify source != destination validation
   - Verify quantity <= available validation
   - Submit and verify request created

2. **Execute Movement:**
   - Start execution and verify status changes to InProgress
   - Verify current inventory levels displayed
   - Complete movement and verify:
     - Source location quantity decreased
     - Destination location quantity increased
     - Request status changed to Completed

3. **Access Control:**
   - Login as Staff and verify warehouse auto-selection
   - Login as Sales and verify access denied

4. **Edge Cases:**
   - Attempt to move more than available
   - Attempt to set source = destination
   - Concurrent changes (another user changes inventory)

---

## Completion Date
January 31, 2026
