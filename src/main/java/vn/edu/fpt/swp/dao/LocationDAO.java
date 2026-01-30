package vn.edu.fpt.swp.dao;

import vn.edu.fpt.swp.model.Location;
import vn.edu.fpt.swp.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Location entity
 */
public class LocationDAO {
    
    /**
     * Get all locations for a warehouse
     * @param warehouseId Warehouse ID
     * @return List of locations
     */
    public List<Location> getLocationsByWarehouse(Long warehouseId) {
        List<Location> locations = new ArrayList<>();
        String sql = "SELECT * FROM Locations WHERE WarehouseId = ? AND IsActive = 1 ORDER BY Code";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, warehouseId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    locations.add(extractLocationFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return locations;
    }
    
    /**
     * Get location by ID
     * @param id Location ID
     * @return Location object or null
     */
    public Location getLocationById(Long id) {
        String sql = "SELECT * FROM Locations WHERE Id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extractLocationFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Extract Location object from ResultSet
     */
    private Location extractLocationFromResultSet(ResultSet rs) throws SQLException {
        Location location = new Location();
        location.setId(rs.getLong("Id"));
        location.setWarehouseId(rs.getLong("WarehouseId"));
        location.setCode(rs.getString("Code"));
        location.setType(rs.getString("Type"));
        location.setActive(rs.getBoolean("IsActive"));
        return location;
    }
}
