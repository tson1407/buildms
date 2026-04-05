USE buildms;
GO

IF NOT EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID(N'[dbo].[Locations]') AND name = 'MaxQuantity')
BEGIN
    ALTER TABLE [dbo].[Locations] ADD [MaxQuantity] INT NULL;
END
GO
