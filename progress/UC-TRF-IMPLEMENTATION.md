# UC-TRF Implementation Progress

## Overview
Inter-Warehouse Transfer Management - Create, approve, execute outbound, and execute inbound for transfers between warehouses.

**Updated Flow (March 2026):** Transfer now follows a cross-warehouse collaborative workflow where the **destination warehouse controls approval** and the **source warehouse executes outbound**.

### Transfer Workflow
```
Source WH creates → Dest WH approves → Source WH executes outbound → Dest WH executes inbound & completes
```

| Phase | Actor | Status Transition |
|-------|-------|-------------------|
| 1. Create | Source WH Manager/Admin | → Created |
| 2. Approve/Reject | **Destination WH Manager** (not source!) | Created → Approved / Rejected |
| 3. Execute Outbound | Source WH Staff/Manager | Approved → InProgress → InTransit |
| 4. Execute Inbound & Complete | Dest WH Staff/Manager | InTransit → Receiving → Completed |

## Use Cases

### UC-TRF-001: Create Inter-Warehouse Transfer Request
- **Status:** ✅ Completed
- **Detail Design:** `document/detail-design/UC-TRF-001-Create-Transfer-Request.md`
- **Files:**
  - `TransferService.java` - `createTransferRequest()` validates warehouses, products, duplicates; creates Transfer request with items
  - `TransferController.java` - `showCreateForm()` / `createTransfer()` handle GET/POST for creation
  - `views/transfer/create.jsp` - Step-by-step form: select source warehouse → load products → select destination → add items → submit
  - `views/transfer/list.jsp` - List all transfers with status filter, action dropdown
- **Access Control:** Admin, Manager only
- **Required Changes:**
  - Manager can only set their assigned warehouse as **source** (already partially implemented)
  - Manager can only view transfers related to their assigned warehouse
  - Remove linked outbound/inbound request generation (single Request record)
- **Validation:**
  - Source and destination must be different (BR-TRF-002)
  - At least one item required
  - Products must be active, no duplicates
  - Quantity must be positive

### UC-TRF-002: Approve/Reject Transfer Request
- **Status:** ✅ Completed (March 8, 2026)
- **Detail Design:** `document/detail-design/UC-TRF-002-Approve-Transfer-Request.md`
- **Files:**
  - `TransferService.java` - `approveTransfer()`, `rejectTransfer()`
  - `TransferController.java` - `approveTransfer()`, `rejectTransfer()` actions
  - `views/transfer/view.jsp` - Detail page with approve/reject buttons
- **Access Control:** Admin, Manager (destination warehouse only)
- **Required Changes:**
  - **CRITICAL:** Manager can only approve/reject transfers where their assigned warehouse is the **destination** (currently allows source OR destination — must restrict to destination only)
  - Admin can approve/reject any transfer
  - Rejection reason is mandatory
- **Business Rules:**
  - Only "Created" status can be approved/rejected
  - Approval enables source warehouse to execute outbound
  - Rejection finalizes the transfer

### UC-TRF-003: Execute Transfer Outbound (was UC-TRF-002)
- **Status:** ✅ Completed (March 8, 2026)
- **Detail Design:** `document/detail-design/UC-TRF-003-Execute-Transfer-Outbound.md`
- **Files:**
  - `TransferService.java` - `startOutboundExecution()` (Approved→InProgress), `completeOutboundExecution()` (InProgress→InTransit, deducts inventory across locations)
  - `TransferController.java` - `showOutboundExecutionForm()`, `startOutbound()`, `completeOutbound()`
  - `views/transfer/execute-outbound.jsp` - Shows availability check (Approved state), picking form (InProgress state)
- **Access Control:** Admin, Manager, Staff (**source warehouse only**)
- **Required Changes:**
  - **CRITICAL:** Only source warehouse Staff/Manager can execute outbound (need to verify warehouse check)
  - Admin can execute for any transfer
- **Inventory Impact:** Decreases inventory at source warehouse using multi-location pattern
- **Key Fix (previous):** `completeOutboundExecution()` was using `item.getLocationId()` (null for transfers). Fixed to iterate through inventory locations at source warehouse.

### UC-TRF-004: Execute Transfer Inbound & Complete (was UC-TRF-003)
- **Status:** ✅ Completed (March 8, 2026)
- **Detail Design:** `document/detail-design/UC-TRF-004-Execute-Transfer-Inbound.md`
- **Files:**
  - `TransferService.java` - `startInboundExecution()` (InTransit→Receiving), `completeInboundExecution()` (Receiving→Completed, adds inventory per-item location)
  - `TransferController.java` - `showInboundExecutionForm()`, `startInbound()`, `completeInbound()`
  - `views/transfer/execute-inbound.jsp` - Shows items in transit (InTransit state), receiving form with per-item location selection (Receiving state)
- **Access Control:** Admin, Manager, Staff (**destination warehouse only**)
- **Required Changes:**
  - **CRITICAL:** Only destination warehouse Staff/Manager can execute inbound (need to verify warehouse check)
  - Admin can execute for any transfer
  - **Destination warehouse marks the transfer as "Completed"**
- **Inventory Impact:** Increases inventory at destination warehouse at user-selected locations
- **Category Enforcement:** BR-TRI-009 — **Destination location must be compatible with product's category. Location dropdown filtered to show only compatible locations**
- **Key Fix (previous):** `completeInboundExecution()` changed to accept `Map<Long, Long>` (productId → locationId) for per-item location assignment.

## Supporting Changes

### Sidebar Navigation
- **File:** `WEB-INF/common/sidebar.jsp`
- **Change:** Added "Transfers" menu section with Transfer List and Create Transfer sub-items
- **Visibility:** Admin, Manager, Staff roles

### View Details Page
- **File:** `views/transfer/view.jsp`
- **Required Changes:**
  - Approve/Reject buttons should only be visible to destination warehouse Manager or Admin
  - Execute Outbound button should only be visible to source warehouse Staff/Manager or Admin
  - Execute Inbound button should only be visible to destination warehouse Staff/Manager or Admin
- **Previous Fixes:**
  - Removed `<c:set var="transfer" value="${request}" />` that was overwriting the transfer attribute with null
  - Fixed `requestNumber` references → `transfer.id` (Request model has no requestNumber)
  - Fixed `createdByUser` → `creator` (matching controller attribute name)
  - Fixed `requestItems` → `items` (matching controller attribute name)
  - Fixed item data access for Map structure (`data.item.quantity`, `data.product.name`)
  - Fixed warehouse `code` → `location` (Warehouse model has no code field)
  - Fixed date formatting with parseDate for LocalDateTime
  - Replaced non-existent statusHistory with rejection details section

### Auth Filter
- **File:** `AuthFilter.java`
- **Status:** Already configured - `/transfer` mapped to Admin, Manager, Staff
- **Note:** Fine-grained warehouse-based access is enforced at controller/service level, not in AuthFilter

## Code Changes Required (Summary)

### TransferController.java
1. **`approveTransfer()`** — Change warehouse check: Manager must be at **destination** warehouse only (currently allows source OR destination)
2. **`rejectTransfer()`** — Same change: Manager must be at **destination** warehouse only
3. **`showOutboundExecutionForm()` / `startOutbound()` / `completeOutbound()`** — Verify: user must be at **source** warehouse
4. **`showInboundExecutionForm()` / `startInbound()` / `completeInbound()`** — Verify: user must be at **destination** warehouse
5. **`listTransfers()`** — Filter transfers by user's warehouse (source or destination)

### TransferService.java
1. Add warehouse validation methods if not present
2. Ensure status transitions are strictly enforced

### views/transfer/view.jsp
1. Show approve/reject buttons only for destination WH Manager/Admin when status is "Created"
2. Show execute outbound button only for source WH Staff/Manager/Admin when status is "Approved"/"InProgress"
3. Show execute inbound button only for dest WH Staff/Manager/Admin when status is "InTransit"/"Receiving"

## Status Flow
```
Created ──[Dest WH Manager approves]──→ Approved ──[Source WH starts outbound]──→ InProgress
   │                                                                                    │
   │                                                                          [Source WH completes]
   │                                                                                    │
   └──[Dest WH Manager rejects]──→ Rejected                                      InTransit
                                                                                        │
                                                                          [Dest WH starts inbound]
                                                                                        │
                                                                                   Receiving
                                                                                        │
                                                                          [Dest WH completes inbound]
                                                                                        │
                                                                                   Completed
```

## Date: 2026-03-08 (Updated)
- **Change:** Updated transfer workflow to require destination warehouse approval
- **Previous date:** 2026-02-08 (initial implementation)
