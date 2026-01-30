package vn.edu.fpt.swp.dao;

import vn.edu.fpt.swp.model.Warehouse;
import vn.edu.fpt.swp.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Warehouse entity
 */
public class WarehouseDAO {
    
    /**
     * Get all warehouses
     * @return List of all warehouses
     */
    public List<Warehouse> getAllWarehouses() {
        List<Warehouse> warehouses = new ArrayList<>();
        String sql = "SELECT * FROM Warehouses ORDER BY Name";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                warehouses.add(extractWarehouseFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return warehouses;
    }
    
    /**
     * Get warehouse by ID
     * @param id Warehouse ID
     * @return Warehouse object or null
     */
    public Warehouse getWarehouseById(Long id) {
        String sql = "SELECT * FROM Warehouses WHERE Id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extractWarehouseFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Extract Warehouse object from ResultSet
     */
    private Warehouse extractWarehouseFromResultSet(ResultSet rs) throws SQLException {
        Warehouse warehouse = new Warehouse();
        warehouse.setId(rs.getLong("Id"));
        warehouse.setName(rs.getString("Name"));
        warehouse.setLocation(rs.getString("Location"));
        
        Timestamp createdAt = rs.getTimestamp("CreatedAt");
        if (createdAt != null) {
            warehouse.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        return warehouse;
    }
}
