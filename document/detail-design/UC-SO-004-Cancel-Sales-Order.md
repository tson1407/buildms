# UC-SO-004: Cancel Sales Order

## Use Case Overview
| Attribute | Description |
|-----------|-------------|
| **Use Case ID** | UC-SO-004 |
| **Use Case Name** | Cancel Sales Order |
| **Primary Actor** | Sales |
| **Secondary Actor** | Admin |
| **Description** | Cancel a sales order that is no longer needed |
| **Preconditions** | User is logged in; Sales Order exists and is not Completed |
| **Postconditions** | Sales Order status changed to "Cancelled" |

---

## Main Flow

### Step 1: Navigate to Sales Orders
- User navigates to Sales Order Management section
- System displays list of sales orders

### Step 2: Select Order to Cancel
- User clicks on the order to view details
- System displays order detail page

### Step 3: Display Order Details
- System displays:
  - Order ID and current status
  - Customer information
  - Order items and quantities
  - Fulfillment status (if any)
  - Related outbound requests (if any)

### Step 4: Verify Cancellation is Allowed
- System checks order status:
  - Draft: Can cancel
  - Confirmed: Can cancel (only if no outbound requests are generated)
  - Fulfillment Requested: Cannot cancel → **Alternative Flow A2**
  - Completed: Cannot cancel → **Alternative Flow A1**

### Step 5: Initiate Cancellation
- User clicks "Cancel Order" button
- System displays cancellation form

### Step 6: Enter Cancellation Details
- System displays:
  - Cancellation Reason (text area, required)
  - Warning about related requests (if any)
- User enters reason for cancellation

### Step 7: Confirm Cancellation
- System displays confirmation dialog:
  - "Are you sure you want to cancel this order?"
  - Shows impact summary (e.g., related requests)
- User confirms cancellation

### Step 8: Handle Related Outbound Requests
- If the order has any related outbound requests (in any status):
  - System blocks the cancellation process.
  - Sales Order cannot be cancelled after an outbound request has been generated.

### Step 9: Update Sales Order Status
- System updates SalesOrder record:
  - Status: "Cancelled"
  - Cancelled By: Current user's ID
  - Cancelled Date: Current timestamp
  - Cancellation Reason: Entered text

### Step 10: Display Confirmation
- System displays success message: "Sales Order [ID] has been cancelled"
- Display summary of any related actions taken

---

## Alternative Flows

### A1: Order Already Completed
- **Trigger:** Order status is "Completed"
- **Steps:**
  1. System displays error: "Completed orders cannot be cancelled"
  2. User may need to process a return instead (out of scope)
  3. Return to order detail page

### A2: Has Outbound Requests
- **Trigger:** Order has ANY outbound requests generated from it.
- **Steps:**
  1. System blocks cancellation.
  2. System displays error: "Sales orders with generated outbound requests cannot be cancelled."
  3. Return to order detail page.

---

## Business Rules
| Rule ID | Description |
|---------|-------------|
| BR-CAN-001 | Sales and Admin can cancel orders |
| BR-CAN-002 | Completed orders cannot be cancelled |
| BR-CAN-003 | Cancellation reason is required |
| BR-CAN-004 | Sales Orders with generated outbound requests cannot be cancelled |
| BR-CAN-005 | Active requests block cancellation entirely |
| BR-CAN-006 | Cancellation does not affect already shipped inventory (N/A since shipping blocks cancellation) |

---

## Access Control
| Role | Permission |
|------|------------|
| Admin | ✓ Can cancel orders |
| Manager | ✗ Cannot cancel orders (View Only) |
| Staff | ✗ Cannot cancel orders |
| Sales | ✓ Can cancel orders |

---

## State Transition
Draft → Cancelled
Confirmed (no outbound) → Cancelled
Confirmed (with outbound) → (Cannot cancel)
Fulfillment Requested → (Cannot cancel)
Completed → (Cannot cancel)
```

---

## UI Requirements
- Cancel button visible on order detail page
- Cancellation reason is mandatory
- Display warnings for related requests
- Confirmation dialog before cancellation
- Clear indication of cancelled status
