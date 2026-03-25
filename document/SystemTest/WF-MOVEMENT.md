# WF-MOVEMENT – Internal Movement Operations

| | | | | | | | | | | | | | | | |
|:---|:---|:---|:---|:---|:---|:---|:---|:---|:---|:---|:---|:---|:---|:---|:---|
| **Workflow** | WF-MOVEMENT – Internal Movement Operations | | | | | | | | | | | | | | **Passed** |
| **Test requirement** | Tests covering the full Internal Movement workflow: Create (UC-MOV-001) and Execute (UC-MOV-002). Verifies that inventory can be moved between locations within the same warehouse without requiring approval, that location data is updated correctly, and that only eligible roles can perform these operations. | | | | | | | | | | | | | | **Failed** |
| **Number of TCs** | 8 | | | | | | | | | | | | | | **Pending** |
| **Testing Round** | Passed | Failed | Pending | | | | | | | | | | | | |
| Round 1 | 0 | 0 | 8 | 0 | | | | | | | | | | | |
| Round 2 | 0 | 0 | 8 | 0 | | | | | | | | | | | |
| Round 3 | 0 | 0 | 8 | 0 | | | | | | | | | | | |

| Test Case ID | Test Case Description | Test Case Procedure | Expected Results | Pre-conditions | Round 1 | Test date | Tester | Round 2 | Test date | Tester | Round 3 | Test date | Tester | Note | |
|:---|:---|:---|:---|:---|:---|:---|:---|:---|:---|:---|:---|:---|:---|:---|:---|
| **Scenario A: Create Internal Movement (UC-MOV-001)** | | | | | | | | | | | | | | | |
| TC-MOV-001 | Staff creates an internal movement request successfully | 1. Login as `staff`<br>2. Navigate to Internal Movement → Create<br>3. Select source location (e.g., `A-01-01`) containing Product X (qty ≥ 10)<br>4. Select destination location (e.g., `B-02-01`) in the same warehouse<br>5. Add Product X with quantity 10<br>6. Click "Submit" | - Movement request created successfully<br>- Status displayed in movement list<br>- No approval step required<br>- Success message displayed | Staff logged in; inventory with product at source location exists | Pending | | | Pending | | | Pending | | | | |
| TC-MOV-002 | Creating movement with same source and destination location is rejected | 1. Login as `staff`<br>2. Navigate to Create Movement<br>3. Select same location for both source and destination<br>4. Add a product and click "Submit" | - Creation rejected<br>- Error: "Source and destination locations must be different" | Staff logged in; locations in same warehouse exist | Pending | | | Pending | | | Pending | | | | |
| TC-MOV-003 | Creating movement with zero quantity is rejected | 1. Login as `staff`<br>2. Navigate to Create Movement<br>3. Add product with quantity = 0<br>4. Click "Submit" | - Validation error: "Quantity must be greater than 0" | Staff logged in | Pending | | | Pending | | | Pending | | | | |
| TC-MOV-004 | Manager creates an internal movement request | 1. Login as `manager`<br>2. Navigate to Internal Movement → Create<br>3. Fill in valid source, destination, product, quantity<br>4. Click "Submit" | - Movement created successfully<br>- Manager listed as creator | Manager logged in; inventory at source location exists | Pending | | | Pending | | | Pending | | | | |
| **Scenario B: Execute Internal Movement (UC-MOV-002)** | | | | | | | | | | | | | | | |
| TC-MOV-005 | Staff executes an internal movement request successfully | 1. Login as `staff`<br>2. Navigate to Internal Movement list<br>3. Find movement request (e.g., from TC-MOV-001), click "Execute"<br>4. Confirm quantities<br>5. Click "Complete Movement" | - Movement completed successfully<br>- Inventory at source location decreased by moved quantity<br>- Inventory at destination location increased by moved quantity<br>- Success message displayed | Internal movement request created (TC-MOV-001) | Pending | | | Pending | | | Pending | | | | |
| TC-MOV-006 | Inventory reflects correct changes after movement execution | 1. Note inventories at source (`A-01-01`) and destination (`B-02-01`) before test<br>2. Execute a movement of Product X qty 10 from `A-01-01` to `B-02-01`<br>3. Check inventory at both locations | - `A-01-01` inventory reduced by 10<br>- `B-02-01` inventory increased by 10<br>- Total inventory for product remains unchanged | Movement executed (TC-MOV-005) | Pending | | | Pending | | | Pending | | | | |
| TC-MOV-007 | Movement to a cross-warehouse location is not allowed | 1. Login as `staff`<br>2. Navigate to Create Movement<br>3. Select source location from Warehouse A<br>4. Attempt to select destination location from Warehouse B | - Destination location filtered to same warehouse only<br>- Selecting a different warehouse location is not possible or shows error | Staff logged in; two warehouses with locations exist | Pending | | | Pending | | | Pending | | | | |
| **Scenario C: Access Control for Movement** | | | | | | | | | | | | | | | |
| TC-MOV-008 | Sales user cannot access Internal Movement | 1. Login as `sales`<br>2. Attempt to navigate to `/movement?action=create` | - Access denied<br>- Redirect to error page | Sales logged in | Pending | | | Pending | | | Pending | | | | |
