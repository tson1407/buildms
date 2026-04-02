# UC-LOC-002: Update Location

## 1. Use Case Overview

| Field | Description |
|-------|-------------|
| **Use Case ID** | UC-LOC-002 |
| **Use Case Name** | Update Location |
| **Actor(s)** | Admin, Manager |
| **Description** | User updates an existing location's information |
| **Trigger** | User clicks "Edit" on a location from the list |
| **Pre-conditions** | - User is logged in with Admin/Manager role<br>- Location exists in the system |
| **Post-conditions** | - Location information is updated |

---

## 2. Main Flow

| Step | Actor | System |
|------|-------|--------|
| 1 | User clicks "Edit" on a location | System retrieves location data |
| 2 | | System displays edit form with current values (including current category restriction if any) |
| 3 | User modifies location code | System validates code format |
| 4 | User modifies location type | System validates type selection |
| 5 | User optionally changes category restriction | System validates category selection |
| 6 | User clicks "Save" | System validates all fields |
| 7 | | System checks for inventory conflicts if category is changed |
| 8 | | System updates location record |
| 9 | | System displays success message |
| 10 | | System redirects to location list |

---

## 3. Alternative Flows

### AF-1: Location Not Found
| Step | Description |
|------|-------------|
| 1a | System cannot find the location |
| 1b | System displays error: "Location not found" |
| 1c | System redirects to location list |

### AF-2: Duplicate Location Code
| Step | Description |
|------|-------------|
| 5a | System detects another location has the same code in the warehouse |
| 5b | System displays error: "Location code already exists in this warehouse" |
| 5c | User corrects the code and resubmits |

### AF-3: Category Conflict with Existing Inventory
| Step | Description |
|------|-------------|
| 7a | System detects location has inventory from a category different than the newly selected one |
| 7b | System displays error: "Cannot set category restriction. Location has inventory from a different category. Please move inventory first." |
| 7c | User corrects selection or moves inventory before retrying |

### AF-4: Cancel Operation
| Step | Description |
|------|-------------|
| 3a | User clicks "Cancel" |
| 3b | System redirects to location list without saving |

---

## 4. Business Rules

| Rule ID | Description |
|---------|-------------|
| BR-LOC-001 | Location code must be unique within a warehouse |
| BR-LOC-002 | Location code is required |
| BR-LOC-003 | Location type must be one of: Storage, Picking, Staging |
| BR-LOC-005 | Warehouse assignment cannot be changed after creation |
| BR-LOC-011 | Category restriction is optional. If set, only products from that category can be stored at this location |
| BR-LOC-012 | Cannot change category restriction if location has inventory from a different category |

---

## 5. Access Control

| Role | Access Level |
|------|--------------|
| Admin | Full access |
| Manager | Full access |
| Staff | No access |
| Sales | No access |

---

## 6. Data Requirements

### Input Fields
| Field | Type | Required | Validation |
|-------|------|----------|------------|
| Code | String (100) | Yes | Not empty, unique per warehouse |
| Type | String (50) | Yes | Must be: Storage, Picking, or Staging |
| CategoryId | Long | No | If provided, must exist in Categories table. System validates against existing inventory |

### Read-Only Fields
| Field | Description |
|-------|-------------|
| WarehouseId | Cannot be changed after creation |
| Warehouse Name | Displayed for reference |

### Database Changes
- UPDATE `Locations` table

---

## 7. UI Requirements

- Use form layout template from `template/html/form-layouts-vertical.html`
- Display warehouse name as read-only field
- Pre-populate form with existing location data (including current category restriction)
- Category restriction dropdown with "No Restriction (Any Product)" option
- Warning indicator if changing category would conflict with existing inventory
- Display validation errors inline
- Include "Save" and "Cancel" buttons
