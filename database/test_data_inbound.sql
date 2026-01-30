-- =====================================================
-- Smart WMS - Test Data for Inbound Management
-- Run this after schema.sql to populate test data
-- =====================================================

USE smartwms_db;
GO

-- Insert test warehouses
IF NOT EXISTS (SELECT * FROM Warehouses WHERE Name = 'Main Warehouse')
BEGIN
    INSERT INTO Warehouses (Name, Location) VALUES 
    ('Main Warehouse', 'Building A, District 1'),
    ('Secondary Warehouse', 'Building B, District 2'),
    ('Distribution Center', 'Building C, District 3');
END
GO

-- Insert test categories
IF NOT EXISTS (SELECT * FROM Categories WHERE Name = 'Electronics')
BEGIN
    INSERT INTO Categories (Name, Description) VALUES 
    ('Electronics', 'Electronic devices and components'),
    ('Furniture', 'Office and home furniture'),
    ('Office Supplies', 'Stationery and office materials'),
    ('Food & Beverage', 'Non-perishable food items');
END
GO

-- Insert test products
IF NOT EXISTS (SELECT * FROM Products WHERE SKU = 'LAPTOP-001')
BEGIN
    INSERT INTO Products (SKU, Name, Unit, CategoryId, IsActive) VALUES 
    ('LAPTOP-001', 'Dell Laptop XPS 15', 'Unit', 1, 1),
    ('PHONE-001', 'iPhone 13 Pro', 'Unit', 1, 1),
    ('DESK-001', 'Office Desk Standard', 'Unit', 2, 1),
    ('CHAIR-001', 'Ergonomic Office Chair', 'Unit', 2, 1),
    ('PEN-001', 'Ballpoint Pen Blue', 'Box', 3, 1),
    ('PAPER-001', 'A4 Paper Ream', 'Ream', 3, 1),
    ('COFFEE-001', 'Instant Coffee Pack', 'Box', 4, 1),
    ('WATER-001', 'Bottled Water 500ml', 'Case', 4, 1);
END
GO

-- Insert test locations for each warehouse
DECLARE @MainWarehouseId BIGINT, @SecondaryWarehouseId BIGINT, @DistributionCenterId BIGINT;

SELECT @MainWarehouseId = Id FROM Warehouses WHERE Name = 'Main Warehouse';
SELECT @SecondaryWarehouseId = Id FROM Warehouses WHERE Name = 'Secondary Warehouse';
SELECT @DistributionCenterId = Id FROM Warehouses WHERE Name = 'Distribution Center';

IF NOT EXISTS (SELECT * FROM Locations WHERE Code = 'A-01-01')
BEGIN
    -- Main Warehouse Locations
    INSERT INTO Locations (WarehouseId, Code, Type, IsActive) VALUES
    (@MainWarehouseId, 'A-01-01', 'Storage', 1),
    (@MainWarehouseId, 'A-01-02', 'Storage', 1),
    (@MainWarehouseId, 'A-02-01', 'Storage', 1),
    (@MainWarehouseId, 'P-01-01', 'Picking', 1),
    (@MainWarehouseId, 'S-01-01', 'Staging', 1),
    
    -- Secondary Warehouse Locations
    (@SecondaryWarehouseId, 'B-01-01', 'Storage', 1),
    (@SecondaryWarehouseId, 'B-01-02', 'Storage', 1),
    (@SecondaryWarehouseId, 'B-02-01', 'Storage', 1),
    
    -- Distribution Center Locations
    (@DistributionCenterId, 'C-01-01', 'Storage', 1),
    (@DistributionCenterId, 'C-01-02', 'Storage', 1);
END
GO

-- Insert test customers
IF NOT EXISTS (SELECT * FROM Customers WHERE Code = 'CUST-001')
BEGIN
    INSERT INTO Customers (Code, Name, ContactInfo, Status) VALUES
    ('CUST-001', 'ABC Corporation', 'contact@abc.com, 0123456789', 'Active'),
    ('CUST-002', 'XYZ Limited', 'info@xyz.com, 0987654321', 'Active'),
    ('CUST-003', 'Tech Solutions Inc', 'sales@techsol.com, 0369258147', 'Active');
END
GO

PRINT 'Test data inserted successfully!';
PRINT '';
PRINT 'Available Warehouses:';
SELECT Id, Name, Location FROM Warehouses;
PRINT '';
PRINT 'Available Products:';
SELECT Id, SKU, Name, Unit FROM Products WHERE IsActive = 1;
PRINT '';
PRINT 'Available Locations (Main Warehouse):';
SELECT L.Id, L.Code, L.Type, W.Name AS WarehouseName 
FROM Locations L 
JOIN Warehouses W ON L.WarehouseId = W.Id 
WHERE W.Name = 'Main Warehouse';
GO
