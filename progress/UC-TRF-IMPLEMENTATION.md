# UC-TRF Implementation Progress

## Overview
Inter-Warehouse Transfer Management - Create, execute outbound, and execute inbound for transfers between warehouses.

## Use Cases

### UC-TRF-001: Create Inter-Warehouse Transfer Request
- **Status:** Completed
- **Files:**
  - `TransferService.java` - `createTransferRequest()` validates warehouses, products, duplicates; creates Transfer request with items
  - `TransferController.java` - `showCreateForm()` / `createTransfer()` handle GET/POST for creation
  - `views/transfer/create.jsp` - Step-by-step form: select source warehouse → load products → select destination → add items → submit
  - `views/transfer/list.jsp` - List all transfers with status filter, action dropdown
- **Access Control:** Admin, Manager only (per BR-TRF-001)
- **Validation:**
  - Source and destination must be different (BR-TRF-002)
  - At least one item required
  - Products must be active, no duplicates
  - Quantity must be positive
- **Notes:** Transfer uses single Request record with custom status flow (Created → Approved → InProgress → InTransit → Receiving → Completed)

### UC-TRF-002: Execute Transfer Outbound
- **Status:** Completed
- **Files:**
  - `TransferService.java` - `startOutboundExecution()` (Approved→InProgress), `completeOutboundExecution()` (InProgress→InTransit, deducts inventory across locations)
  - `TransferController.java` - `showOutboundExecutionForm()`, `startOutbound()`, `completeOutbound()`
  - `views/transfer/execute-outbound.jsp` - Shows availability check (Approved state), picking form (InProgress state)
- **Access Control:** Admin, Manager, Staff
- **Inventory Impact:** Decreases inventory at source warehouse using multi-location pattern (iterates across locations to fulfill quantity)
- **Key Fix:** `completeOutboundExecution()` was using `item.getLocationId()` (null for transfers). Fixed to iterate through inventory locations at source warehouse, matching the OutboundService pattern.

### UC-TRF-003: Execute Transfer Inbound
- **Status:** Completed
- **Files:**
  - `TransferService.java` - `startInboundExecution()` (InTransit→Receiving), `completeInboundExecution()` (Receiving→Completed, adds inventory per-item location)
  - `TransferController.java` - `showInboundExecutionForm()`, `startInbound()`, `completeInbound()`
  - `views/transfer/execute-inbound.jsp` - Shows items in transit (InTransit state), receiving form with per-item location selection (Receiving state)
- **Access Control:** Admin, Manager, Staff
- **Inventory Impact:** Increases inventory at destination warehouse at user-selected locations
- **Key Fix:** `completeInboundExecution()` was accepting single `destinationLocationId` for all items. Changed to accept `Map<Long, Long>` (productId → locationId) for per-item location assignment. Controller parses `productId[]` and `locationId[]` arrays from form.

## Supporting Changes

### Sidebar Navigation
- **File:** `WEB-INF/common/sidebar.jsp`
- **Change:** Added "Transfers" menu section with Transfer List and Create Transfer sub-items
- **Visibility:** Admin, Manager, Staff roles

### View Details Page
- **File:** `views/transfer/view.jsp`
- **Fixes:**
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

## Status Flow
```
Created → Approved → InProgress → InTransit → Receiving → Completed
   ↓
Rejected
```

## Date: 2026-02-08
