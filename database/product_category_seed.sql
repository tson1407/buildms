-- Seed data for Categories and Products (Building Materials)
USE smartwms_db;
GO

BEGIN TRANSACTION;

-- Categories (building materials)
INSERT INTO Categories (Name, Description) VALUES
('Cement & Concrete', 'Portland cement, ready-mix, concrete additives'),
('Bricks & Blocks', 'Clay bricks, concrete blocks, hollow blocks'),
('Steel & Rebar', 'Reinforcement bars, steel sections, wire mesh'),
('Tiles & Flooring', 'Ceramic tiles, porcelain, vinyl, floor tiles'),
('Timber & Plywood', 'Lumber, plywood sheets, timber products'),
('Paints & Coatings', 'Interior/exterior paints, primers, coatings'),
('Roofing & Gutters', 'Roof sheets, tiles, guttering, flashings'),
('Plumbing & Pipes', 'PVC/HDPE/Steel pipes, fittings, valves'),
('Electrical', 'Cables, conduits, switches, fittings'),
('Fasteners & Fixings', 'Nails, screws, bolts, anchors')
;

-- Products (building materials)
-- Use SELECT Id from Categories to associate products to categories by name
INSERT INTO Products (SKU, Name, Unit, CategoryId, IsActive) VALUES
-- Cement & Concrete
('CEM-50-OP', 'Portland Cement OPC 50kg Bag', 'bag', (SELECT Id FROM Categories WHERE Name = 'Cement & Concrete'), 1),
('CEM-RM-25', 'Ready-Mix Concrete 25MPa (per m3)', 'm3', (SELECT Id FROM Categories WHERE Name = 'Cement & Concrete'), 1),
('ADM-PLAST', 'Plasticizer Admixture 20L', 'litre', (SELECT Id FROM Categories WHERE Name = 'Cement & Concrete'), 1),

-- Bricks & Blocks
('BRK-STD', 'Clay Brick (Standard 230x110x76mm)', 'pcs', (SELECT Id FROM Categories WHERE Name = 'Bricks & Blocks'), 1),
('BLK-200', 'Concrete Block 200x200x400mm', 'pcs', (SELECT Id FROM Categories WHERE Name = 'Bricks & Blocks'), 1),

-- Steel & Rebar
('RBR-10', 'Rebar B500 10mm (per meter)', 'm', (SELECT Id FROM Categories WHERE Name = 'Steel & Rebar'), 1),
('RBR-16', 'Rebar B500 16mm (per meter)', 'm', (SELECT Id FROM Categories WHERE Name = 'Steel & Rebar'), 1),
('STR-UB', 'Steel U-Channel 100x50 (per meter)', 'm', (SELECT Id FROM Categories WHERE Name = 'Steel & Rebar'), 1),

-- Tiles & Flooring
('TLE-300', 'Porcelain Floor Tile 300x300mm', 'box', (SELECT Id FROM Categories WHERE Name = 'Tiles & Flooring'), 1),
('TLE-WALL-250', 'Ceramic Wall Tile 250x150mm', 'box', (SELECT Id FROM Categories WHERE Name = 'Tiles & Flooring'), 1),

-- Timber & Plywood
('PLY-18-1224', 'Plywood 18mm 1220x2440mm', 'sheet', (SELECT Id FROM Categories WHERE Name = 'Timber & Plywood'), 1),
('TMB-2X4', 'Sawn Timber 2x4 (per meter)', 'm', (SELECT Id FROM Categories WHERE Name = 'Timber & Plywood'), 1),

-- Paints & Coatings
('PNT-WHT-5L', 'Interior Emulsion Paint White 5L', 'can', (SELECT Id FROM Categories WHERE Name = 'Paints & Coatings'), 1),
('PRM-1L', 'Multi-surface Primer 1L', 'can', (SELECT Id FROM Categories WHERE Name = 'Paints & Coatings'), 1),

-- Roofing & Gutters
('RF-GL-ROOF', 'Galvanized Roof Sheet 0.5mm (per sheet)', 'sheet', (SELECT Id FROM Categories WHERE Name = 'Roofing & Gutters'), 1),
('RF-CLAY-TL', 'Clay Roof Tile (interlocking)', 'pcs', (SELECT Id FROM Categories WHERE Name = 'Roofing & Gutters'), 1),

-- Plumbing & Pipes
('PVC-PIPE-110', 'PVC Pipe 110mm (per meter)', 'm', (SELECT Id FROM Categories WHERE Name = 'Plumbing & Pipes'), 1),
('PVC-FIT-110', 'PVC Pipe Elbow 110mm', 'pcs', (SELECT Id FROM Categories WHERE Name = 'Plumbing & Pipes'), 1),

-- Electrical
('CABL-3C-6', '3-Core Power Cable 6mm2 (per meter)', 'm', (SELECT Id FROM Categories WHERE Name = 'Electrical'), 1),
('SW-2G', 'Toggle Switch 2-Gang', 'pcs', (SELECT Id FROM Categories WHERE Name = 'Electrical'), 1),

-- Fasteners & Fixings
('SCR-50', 'Wood Screw 5x50mm Box (100 pcs)', 'box', (SELECT Id FROM Categories WHERE Name = 'Fasteners & Fixings'), 1),
('ANR-10', 'Concrete Anchor M10', 'pcs', (SELECT Id FROM Categories WHERE Name = 'Fasteners & Fixings'), 1)
;

COMMIT TRANSACTION;
GO

-- End of seed file
