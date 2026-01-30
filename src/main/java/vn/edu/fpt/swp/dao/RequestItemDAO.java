package vn.edu.fpt.swp.dao;

import vn.edu.fpt.swp.model.RequestItem;
import vn.edu.fpt.swp.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for RequestItem entity
 */
public class RequestItemDAO {
    
    /**
     * Create a request item
     * @param item RequestItem object
     * @return true if successful
     */
    public boolean createRequestItem(RequestItem item) {
        String sql = "INSERT INTO RequestItems (RequestId, ProductId, Quantity, LocationId) " +
                    "VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, item.getRequestId());
            stmt.setLong(2, item.getProductId());
            stmt.setInt(3, item.getQuantity());
            
            if (item.getLocationId() != null) {
                stmt.setLong(4, item.getLocationId());
            } else {
                stmt.setNull(4, Types.BIGINT);
            }
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Get all items for a request
     * @param requestId Request ID
     * @return List of request items
     */
    public List<RequestItem> getItemsByRequestId(Long requestId) {
        List<RequestItem> items = new ArrayList<>();
        String sql = "SELECT * FROM RequestItems WHERE RequestId = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, requestId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    items.add(extractRequestItemFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }
    
    /**
     * Update received quantity for a request item
     * @param requestId Request ID
     * @param productId Product ID
     * @param receivedQuantity Received quantity
     * @return true if successful
     */
    public boolean updateReceivedQuantity(Long requestId, Long productId, Integer receivedQuantity) {
        String sql = "UPDATE RequestItems SET ReceivedQuantity = ? WHERE RequestId = ? AND ProductId = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, receivedQuantity);
            stmt.setLong(2, requestId);
            stmt.setLong(3, productId);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Check if product already exists in request
     * @param requestId Request ID
     * @param productId Product ID
     * @return true if exists
     */
    public boolean productExistsInRequest(Long requestId, Long productId) {
        String sql = "SELECT COUNT(*) FROM RequestItems WHERE RequestId = ? AND ProductId = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, requestId);
            stmt.setLong(2, productId);
            
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
    
    /**
     * Extract RequestItem object from ResultSet
     */
    private RequestItem extractRequestItemFromResultSet(ResultSet rs) throws SQLException {
        RequestItem item = new RequestItem();
        item.setRequestId(rs.getLong("RequestId"));
        item.setProductId(rs.getLong("ProductId"));
        item.setQuantity(rs.getInt("Quantity"));
        
        Long locationId = rs.getLong("LocationId");
        if (!rs.wasNull()) {
            item.setLocationId(locationId);
        }
        
        Long sourceLocationId = rs.getLong("SourceLocationId");
        if (!rs.wasNull()) {
            item.setSourceLocationId(sourceLocationId);
        }
        
        Long destLocationId = rs.getLong("DestinationLocationId");
        if (!rs.wasNull()) {
            item.setDestinationLocationId(destLocationId);
        }
        
        Integer receivedQty = rs.getInt("ReceivedQuantity");
        if (!rs.wasNull()) {
            item.setReceivedQuantity(receivedQty);
        }
        
        Integer pickedQty = rs.getInt("PickedQuantity");
        if (!rs.wasNull()) {
            item.setPickedQuantity(pickedQty);
        }
        
        return item;
    }
}
