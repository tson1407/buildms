# Detail Design Documents

## Overview

This folder contains detailed design documents for all use cases in the Smart Warehouse Management System (Smart WMS). Each use case is documented in a separate Markdown file following a consistent structure.

## Document Structure

Each use case document includes:

1. **Use Case Overview** - ID, name, actors, description, pre/post conditions
2. **Main Flow** - Step-by-step sequence of actions
3. **Alternative Flows** - Exception handling and alternate paths
4. **Business Rules** - Applicable rules and constraints
5. **Access Control** - Role-based permissions
6. **State Transitions** - State changes triggered by the use case
7. **UI Requirements** - Interface considerations
8. **Audit Trail** - What is logged for traceability

## Use Case Index

### Authentication & Authorization (UC-AUTH)
| ID | Name | File |
|----|------|------|
| UC-AUTH-001 | User Login | [UC-AUTH-001-User-Login.md](UC-AUTH-001-User-Login.md) |
| UC-AUTH-002 | User Registration | [UC-AUTH-002-User-Registration.md](UC-AUTH-002-User-Registration.md) |
| UC-AUTH-003 | Change Password | [UC-AUTH-003-Change-Password.md](UC-AUTH-003-Change-Password.md) |
| UC-AUTH-004 | Admin Reset Password | [UC-AUTH-004-Admin-Reset-Password.md](UC-AUTH-004-Admin-Reset-Password.md) |
| UC-AUTH-005 | User Logout | [UC-AUTH-005-User-Logout.md](UC-AUTH-005-User-Logout.md) |
| UC-AUTH-006 | Session Timeout | [UC-AUTH-006-Session-Timeout.md](UC-AUTH-006-Session-Timeout.md) |

### Inbound Management (UC-INB)
| ID | Name | File |
|----|------|------|
| UC-INB-001 | Create Inbound Request | [UC-INB-001-Create-Inbound-Request.md](UC-INB-001-Create-Inbound-Request.md) |
| UC-INB-002 | Approve Inbound Request | [UC-INB-002-Approve-Inbound-Request.md](UC-INB-002-Approve-Inbound-Request.md) |
| UC-INB-003 | Execute Inbound Request | [UC-INB-003-Execute-Inbound-Request.md](UC-INB-003-Execute-Inbound-Request.md) |

### Sales Order Management (UC-SO)
| ID | Name | File |
|----|------|------|
| UC-SO-001 | Create Sales Order | [UC-SO-001-Create-Sales-Order.md](UC-SO-001-Create-Sales-Order.md) |
| UC-SO-002 | Confirm Sales Order | [UC-SO-002-Confirm-Sales-Order.md](UC-SO-002-Confirm-Sales-Order.md) |
| UC-SO-003 | Generate Outbound from Sales Order | [UC-SO-003-Generate-Outbound-From-Sales-Order.md](UC-SO-003-Generate-Outbound-From-Sales-Order.md) |
| UC-SO-004 | Cancel Sales Order | [UC-SO-004-Cancel-Sales-Order.md](UC-SO-004-Cancel-Sales-Order.md) |

### Outbound Management (UC-OUT)
| ID | Name | File |
|----|------|------|
| UC-OUT-001 | Approve Outbound Request | [UC-OUT-001-Approve-Outbound-Request.md](UC-OUT-001-Approve-Outbound-Request.md) |
| UC-OUT-002 | Execute Outbound Request | [UC-OUT-002-Execute-Outbound-Request.md](UC-OUT-002-Execute-Outbound-Request.md) |
| UC-OUT-003 | Create Internal Outbound Request | [UC-OUT-003-Create-Internal-Outbound-Request.md](UC-OUT-003-Create-Internal-Outbound-Request.md) |

### Inter-Warehouse Transfer (UC-TRF)
| ID | Name | File |
|----|------|------|
| UC-TRF-001 | Create Transfer Request | [UC-TRF-001-Create-Transfer-Request.md](UC-TRF-001-Create-Transfer-Request.md) |
| UC-TRF-002 | Execute Transfer Outbound | [UC-TRF-002-Execute-Transfer-Outbound.md](UC-TRF-002-Execute-Transfer-Outbound.md) |
| UC-TRF-003 | Execute Transfer Inbound | [UC-TRF-003-Execute-Transfer-Inbound.md](UC-TRF-003-Execute-Transfer-Inbound.md) |

### Internal Movement (UC-MOV)
| ID | Name | File |
|----|------|------|
| UC-MOV-001 | Create Internal Movement | [UC-MOV-001-Create-Internal-Movement.md](UC-MOV-001-Create-Internal-Movement.md) |
| UC-MOV-002 | Execute Internal Movement | [UC-MOV-002-Execute-Internal-Movement.md](UC-MOV-002-Execute-Internal-Movement.md) |

## State Models Reference

### Request Lifecycle
```
Created → Approved → In Progress → Completed
              ↓
           Rejected
```

### Sales Order Lifecycle
```
Draft → Confirmed → Fulfillment Requested → Partially Shipped → Completed
  ↓         ↓               ↓                      ↓
Cancelled  Cancelled     Cancelled              Cancelled
```

## Role Summary

| Role | Primary Responsibilities |
|------|--------------------------|
| Admin | System configuration, user management, full access |
| Manager | Request creation, approval, inventory oversight |
| Staff | Request execution, physical operations |
| Sales | Sales order management (no warehouse access) |

## Notes

- These documents describe **behavior and flow only**
- Entity/database models are defined in [database/schema.sql](../../database/schema.sql)
- Business rules follow the SRS in [document/SRS.md](../SRS.md)
- No source code is included in these documents
