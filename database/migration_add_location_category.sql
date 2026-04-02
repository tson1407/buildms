-- Migration: Add CategoryId to Locations table
-- Date: April 2026
-- Description: Optional category restriction for storage locations

IF NOT EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID('Locations') AND name = 'CategoryId')
BEGIN
    ALTER TABLE Locations ADD CategoryId BIGINT NULL;
    ALTER TABLE Locations ADD CONSTRAINT FK_Location_Category 
        FOREIGN KEY (CategoryId) REFERENCES Categories(Id);
    PRINT 'Added CategoryId column to Locations table';
END
GO
