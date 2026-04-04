# UC-PRD-002: Update Product (Name Only)

## 1. Use Case Overview

| Field | Description |
|-------|-------------|
| **Use Case ID** | UC-PRD-002 |
| **Use Case Name** | Update Product (Name Only) |
| **Actor(s)** | Admin, Manager |
| **Description** | User updates an existing product's name. SKU, Category, and Unit are locked to preserve inventory integrity |
| **Trigger** | User clicks "Edit" on a product from the list |
| **Pre-conditions** | - User is logged in with Admin/Manager role<br>- Product exists in the system |
| **Post-conditions** | - Product name is updated |

---

## 2. Main Flow

| Step | Actor | System |
|------|-------|--------|
| 1 | User clicks "Edit" on a product | System retrieves product data |
| 2 | | System displays edit form with current values. SKU, Unit, and Category fields are read-only |
| 3 | User modifies product name | System validates input |
| 4 | User clicks "Save" | System validates all fields |
| 5 | | System updates product record (Name only) |
| 6 | | System displays success message |
| 7 | | System redirects to product list |

---

## 3. Alternative Flows

### AF-1: Product Not Found
| Step | Description |
|------|-------------|
| 1a | System cannot find the product |
| 1b | System displays error: "Product not found" |
| 1c | System redirects to product list |

### AF-2: Cancel Operation
| Step | Description |
|------|-------------|
| 3a | User clicks "Cancel" |
| 3b | System redirects to product list without saving |

---

## 4. Business Rules

| Rule ID | Description |
|---------|-------------|
| BR-PRD-001 | SKU is immutable once created |
| BR-PRD-002 | Name is a required field |
| BR-PRD-005 | Unit and Category are immutable to prevent cascading mismatches in inventory tracking and sales reports |

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
| SKU | String (100) | No | **Read-only** |
| Name | String (255) | Yes | Not empty |
| Unit | String (50) | No | **Read-only** |
| CategoryId | Long | No | **Read-only** |

### Database Changes
- UPDATE `Products` table (name field only)

---

## 7. UI Requirements

- Use form layout template from `template/html/form-layouts-vertical.html`
- Pre-populate form with existing product data
- SKU, Unit, and Category inputs must be `readonly` or visually disabled with a padlock icon/tooltip explaining they cannot be changed
- Display validation errors inline
- Include "Save" and "Cancel" buttons
