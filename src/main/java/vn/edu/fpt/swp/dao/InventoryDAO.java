package vn.edu.fpt.swp.dao;

import vn.edu.fpt.swp.model.Inventory;
import vn.edu.fpt.swp.util.DBConnection;

import java.sql.*;

/**
 * Data Access Object for Inventory entity
 */
public class InventoryDAO {
    
    /**
     * Get inventory quantity for a product at a warehouse location
     * @param productId Product ID
     * @param warehouseId Warehouse ID
     * @param locationId Location ID
     * @return Current quantity
     */
    public int getInventoryQuantity(Long productId, Long warehouseId, Long locationId) {
        String sql = "SELECT Quantity FROM Inventory WHERE ProductId = ? AND WarehouseId = ? AND LocationId = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, productId);
            stmt.setLong(2, warehouseId);
            stmt.setLong(3, locationId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("Quantity");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    /**
     * Update or insert inventory quantity
     * @param productId Product ID
     * @param warehouseId Warehouse ID
     * @param locationId Location ID
     * @param quantityChange Quantity to add (positive) or subtract (negative)
     * @return true if successful
     */
    public boolean updateInventory(Long productId, Long warehouseId, Long locationId, int quantityChange) {
        // Check if inventory record exists
        int currentQty = getInventoryQuantity(productId, warehouseId, locationId);
        
        if (currentQty > 0 || inventoryRecordExists(productId, warehouseId, locationId)) {
            // Update existing record
            String sql = "UPDATE Inventory SET Quantity = Quantity + ? " +
                        "WHERE ProductId = ? AND WarehouseId = ? AND LocationId = ?";
            
            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                
                stmt.setInt(1, quantityChange);
                stmt.setLong(2, productId);
                stmt.setLong(3, warehouseId);
                stmt.setLong(4, locationId);
                
                return stmt.executeUpdate() > 0;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            // Insert new record
            String sql = "INSERT INTO Inventory (ProductId, WarehouseId, LocationId, Quantity) " +
                        "VALUES (?, ?, ?, ?)";
            
            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                
                stmt.setLong(1, productId);
                stmt.setLong(2, warehouseId);
                stmt.setLong(3, locationId);
                stmt.setInt(4, quantityChange);
                
                return stmt.executeUpdate() > 0;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    
    /**
     * Check if inventory record exists
     */
    private boolean inventoryRecordExists(Long productId, Long warehouseId, Long locationId) {
        String sql = "SELECT COUNT(*) FROM Inventory WHERE ProductId = ? AND WarehouseId = ? AND LocationId = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, productId);
            stmt.setLong(2, warehouseId);
            stmt.setLong(3, locationId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
