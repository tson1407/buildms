# UC-OUT-002: Execute Outbound Request (Auto-Execute)

## Use Case Overview
| Attribute | Description |
|-----------|-------------|
| **Use Case ID** | UC-OUT-002 |
| **Use Case Name** | Execute Outbound Request (Auto-Execute) |
| **Primary Actor** | Staff |
| **Secondary Actor** | Manager |
| **Description** | Validate inventory and automatically complete an outbound shipment in one step |
| **Preconditions** | User is logged in; Request exists with status "Approved"; Sufficient inventory exists |
| **Postconditions** | Request completed; Inventory reduced; Sales Order updated (if applicable) |

---

## Main Flow

### Step 1: Navigate to Approved Requests
- Staff navigates to Outbound Management section
- System displays list of requests
- Staff filters by status "Approved"

### Step 2: Select Request to Execute
- Staff clicks the execute icon for an approved outbound request
- System loads the execute confirmation page

### Step 3: Display Inventory Availability
- System displays the request details (ID, Source Warehouse, Reason)
- System actively calculates and displays inventory availability for each requested item
- If all items have sufficient inventory, system displays a success banner and enables execution
- If any item lacks inventory → **Alternative Flow A1**

### Step 4: Confirm Execution
- Staff clicks the "Confirm & Execute" button
- System validates the action

### Step 5: Auto-Execute and Deduct
- System automatically applies the full requested quantity as the "picked" quantity
- System decreases inventory for each product at the source warehouse
- System creates inventory transaction records (Type: "Outbound")

### Step 6: Update Status
- System updates request record status to "Completed"
- System records completion timestamp and user

### Step 7: Update Sales Order (if applicable)
- System updates SalesOrderItem fulfilled quantities
- System checks if order is fulfilled:
  - All items fulfilled → SalesOrder status: "Completed"

### Step 8: Display Confirmation
- System redirects to the request details page
- System displays success message: "Outbound Request executed successfully. Inventory has been updated."

---

## Alternative Flows

### A1: Insufficient Inventory
- **Trigger:** Available inventory at the warehouse is less than the requested quantity for one or more items.
- **Steps:**
  1. System detects the shortage when loading the execute page.
  2. System displays an inline error status for the affected items, showing the exact shortage amount.
  3. The "Confirm & Execute" button is disabled. 
  4. The request cannot be executed until inventory is replenished or the request is modified/rejected.

---

## Business Rules
| Rule ID | Description |
|---------|-------------|
| BR-EXO-001 | Only Staff or Manager can execute outbound requests |
| BR-EXO-002 | Only "Approved" requests can be executed |
| BR-EXO-003 | Inventory decreases immediately upon execution |
| BR-EXO-004 | Execution is completely blocked if any item does not have sufficient inventory at the source warehouse |
| BR-EXO-005 | Sales Order is automatically updated upon outbound completion |
| BR-EXO-006 | Manual picking and partial picking are no longer supported; an outbound is fulfilled entirely or not at all |

---

## Access Control
| Role | Permission |
|------|------------|
| Admin | ✓ Can execute requests |
| Manager | ✓ Can execute requests |
| Staff | ✓ Can execute requests |
| Sales | ✗ Cannot execute requests |

---

## State Transition

### Request:
```
Approved → Completed (Execution is instant)
```

### Sales Order (if applicable):
```
Fulfillment Requested → Completed (upon successful outbound execution)
```

---

## Inventory Impact
| Action | Effect |
|--------|--------|
| Execute outbound | Auto-deduct full requested quantity for each product from the source warehouse |

---

## UI Requirements
- Read-only summary of items with requested quantities
- Real-time inventory availability table with visual indicators (sufficient/insufficient)
- Complete button — disabled when any item has insufficient inventory
- Cannot edit picked quantities
- No "In Progress" execution state
