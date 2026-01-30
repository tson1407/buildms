package vn.edu.fpt.swp.dao;

import vn.edu.fpt.swp.model.Inventory;
import vn.edu.fpt.swp.util.DBConnection;

import java.sql.*;

/**
 * Data Access Object for Inventory entity
 */
public class InventoryDAO {
    
    /**
     * Get inventory for a specific product at a warehouse location
     * @param productId Product ID
     * @param warehouseId Warehouse ID
     * @param locationId Location ID
     * @return Inventory object or null if not found
     */
    public Inventory getInventory(Long productId, Long warehouseId, Long locationId) {
        String sql = "SELECT * FROM Inventory WHERE ProductId = ? AND WarehouseId = ? AND LocationId = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, productId);
            stmt.setLong(2, warehouseId);
            stmt.setLong(3, locationId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extractInventoryFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Update or insert inventory
     * @param inventory Inventory object
     * @return true if successful, false otherwise
     */
    public boolean upsertInventory(Inventory inventory) {
        // Check if inventory exists
        Inventory existing = getInventory(inventory.getProductId(), inventory.getWarehouseId(), inventory.getLocationId());
        
        if (existing != null) {
            // Update existing
            String sql = "UPDATE Inventory SET Quantity = ? WHERE ProductId = ? AND WarehouseId = ? AND LocationId = ?";
            
            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                
                stmt.setInt(1, inventory.getQuantity());
                stmt.setLong(2, inventory.getProductId());
                stmt.setLong(3, inventory.getWarehouseId());
                stmt.setLong(4, inventory.getLocationId());
                
                return stmt.executeUpdate() > 0;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            // Insert new
            String sql = "INSERT INTO Inventory (ProductId, WarehouseId, LocationId, Quantity) VALUES (?, ?, ?, ?)";
            
            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                
                stmt.setLong(1, inventory.getProductId());
                stmt.setLong(2, inventory.getWarehouseId());
                stmt.setLong(3, inventory.getLocationId());
                stmt.setInt(4, inventory.getQuantity());
                
                return stmt.executeUpdate() > 0;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    
    /**
     * Increase inventory quantity (for inbound operations)
     * @param productId Product ID
     * @param warehouseId Warehouse ID
     * @param locationId Location ID
     * @param quantity Quantity to add
     * @return true if successful, false otherwise
     */
    public boolean increaseInventory(Long productId, Long warehouseId, Long locationId, Integer quantity) {
        Inventory existing = getInventory(productId, warehouseId, locationId);
        
        if (existing != null) {
            existing.setQuantity(existing.getQuantity() + quantity);
        } else {
            existing = new Inventory();
            existing.setProductId(productId);
            existing.setWarehouseId(warehouseId);
            existing.setLocationId(locationId);
            existing.setQuantity(quantity);
        }
        
        return upsertInventory(existing);
    }
    
    /**
     * Decrease inventory quantity (for outbound operations)
     * @param productId Product ID
     * @param warehouseId Warehouse ID
     * @param locationId Location ID
     * @param quantity Quantity to subtract
     * @return true if successful, false otherwise
     */
    public boolean decreaseInventory(Long productId, Long warehouseId, Long locationId, Integer quantity) {
        Inventory existing = getInventory(productId, warehouseId, locationId);
        
        if (existing == null || existing.getQuantity() < quantity) {
            return false; // Insufficient inventory
        }
        
        existing.setQuantity(existing.getQuantity() - quantity);
        return upsertInventory(existing);
    }
    
    /**
     * Extract Inventory object from ResultSet
     * @param rs ResultSet
     * @return Inventory object
     * @throws SQLException if database access error occurs
     */
    private Inventory extractInventoryFromResultSet(ResultSet rs) throws SQLException {
        Inventory inventory = new Inventory();
        inventory.setProductId(rs.getLong("ProductId"));
        inventory.setWarehouseId(rs.getLong("WarehouseId"));
        inventory.setLocationId(rs.getLong("LocationId"));
        inventory.setQuantity(rs.getInt("Quantity"));
        return inventory;
    }
}
