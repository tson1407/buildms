-- SMART WMS - DỮ LIỆU MẪU ĐẦY ĐỦ (Tiếng Việt)
-- Phiên bản dành cho kiểm thử các chức năng đặc thù Việt Nam

-- Người dùng
INSERT INTO Users (Username, Name, Email, PasswordHash, Role, Status, WarehouseId, CreatedAt, LastLogin)
VALUES
    (N'admin', N'Quản trị viên', N'admin@smartwms.vn', '...', N'Admin', N'Active', NULL, GETDATE(), NULL),
    (N'manager', N'Quản lý kho', N'manager@smartwms.vn', '...', N'Manager', N'Active', 1, GETDATE(), NULL),
    (N'staff', N'Nhân viên kho', N'staff@smartwms.vn', '...', N'Staff', N'Active', 1, GETDATE(), NULL),
    (N'sales', N'Nhân viên bán hàng', N'sales@smartwms.vn', '...', N'Sales', N'Active', NULL, GETDATE(), NULL);

-- Kho hàng
INSERT INTO Warehouses (Name, Location, CreatedAt)
VALUES
    (N'Kho Hà Nội', N'Đường Trần Duy Hưng, Cầu Giấy, Hà Nội', GETDATE()),
    (N'Kho Hồ Chí Minh', N'Đường Nguyễn Thị Minh Khai, Quận 1, TP.HCM', GETDATE());

-- Danh mục
INSERT INTO Categories (Name, Description)
VALUES
    (N'Điện tử', N'Sản phẩm điện tử'),
    (N'Thực phẩm', N'Sản phẩm thực phẩm'),
    (N'Đồ gia dụng', N'Sản phẩm gia dụng');

-- Sản phẩm
INSERT INTO Products (SKU, Name, Unit, CategoryId, IsActive, CreatedAt)
VALUES
    (N'PRD001', N'Điện thoại Samsung', N'Cái', 1, 1, GETDATE()),
    (N'PRD002', N'Bánh tráng', N'Gói', 2, 1, GETDATE()),
    (N'PRD003', N'Nồi cơm điện', N'Cái', 3, 1, GETDATE());

-- Vị trí kho
INSERT INTO Locations (WarehouseId, Code, Type, IsActive)
VALUES
    (1, N'KHO-HN-01', N'Storage', 1),
    (1, N'KHO-HN-02', N'Picking', 1),
    (2, N'KHO-HCM-01', N'Staging', 1);

-- Tồn kho
INSERT INTO Inventory (ProductId, WarehouseId, LocationId, Quantity)
VALUES
    (1, 1, 1, 100),
    (2, 1, 2, 200),
    (3, 2, 3, 50);

-- Khách hàng
INSERT INTO Customers (Code, Name, ContactInfo, Status)
VALUES
    (N'CUST001', N'Công ty ABC', N'abc@congtyabc.vn', N'Active'),
    (N'CUST002', N'Cửa hàng Minh', N'minh@cuahangminh.vn', N'Active');

-- Đơn hàng bán
INSERT INTO SalesOrders (OrderNo, CustomerId, Status, CreatedBy, CreatedAt)
VALUES
    (N'SO001', 1, N'Draft', 4, GETDATE()),
    (N'SO002', 2, N'Draft', 4, GETDATE());

-- Sản phẩm trong đơn hàng
INSERT INTO SalesOrderItems (SalesOrderId, ProductId, Quantity)
VALUES
    (1, 1, 2),
    (1, 2, 5),
    (2, 3, 1);

-- Yêu cầu nhập/xuất kho
INSERT INTO Requests (Type, Status, CreatedBy, SourceWarehouseId, DestinationWarehouseId, ExpectedDate, Notes, CreatedAt)
VALUES
    (N'Inbound', N'Created', 2, NULL, 1, DATEADD(DAY, 1, GETDATE()), N'Nhập hàng từ nhà cung cấp', GETDATE()),
    (N'Outbound', N'Created', 2, 1, NULL, DATEADD(DAY, 2, GETDATE()), N'Xuất hàng cho khách', GETDATE());

-- Sản phẩm trong yêu cầu
INSERT INTO RequestItems (RequestId, ProductId, Quantity, LocationId)
VALUES
    (1, 1, 10, 1),
    (1, 2, 20, 2),
    (2, 2, 5, 2),
    (2, 3, 2, 3);
