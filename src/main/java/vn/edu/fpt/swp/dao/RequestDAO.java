package vn.edu.fpt.swp.dao;

import vn.edu.fpt.swp.model.Request;
import vn.edu.fpt.swp.model.RequestItem;
import vn.edu.fpt.swp.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Request entity
 */
public class RequestDAO {
    
    /**
     * Create a new request in the database
     * @param request Request object to create
     * @return Generated request ID or null if failed
     */
    public Long createRequest(Request request) {
        String sql = "INSERT INTO Requests (Type, Status, CreatedBy, SalesOrderId, SourceWarehouseId, DestinationWarehouseId, ExpectedDate, Notes, Reason, CreatedAt) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, request.getType());
            stmt.setString(2, request.getStatus());
            stmt.setLong(3, request.getCreatedBy());
            
            if (request.getSalesOrderId() != null) {
                stmt.setLong(4, request.getSalesOrderId());
            } else {
                stmt.setNull(4, Types.BIGINT);
            }
            
            if (request.getSourceWarehouseId() != null) {
                stmt.setLong(5, request.getSourceWarehouseId());
            } else {
                stmt.setNull(5, Types.BIGINT);
            }
            
            if (request.getDestinationWarehouseId() != null) {
                stmt.setLong(6, request.getDestinationWarehouseId());
            } else {
                stmt.setNull(6, Types.BIGINT);
            }
            
            if (request.getExpectedDate() != null) {
                stmt.setTimestamp(7, Timestamp.valueOf(request.getExpectedDate()));
            } else {
                stmt.setNull(7, Types.TIMESTAMP);
            }
            
            stmt.setString(8, request.getNotes());
            stmt.setString(9, request.getReason());
            stmt.setTimestamp(10, Timestamp.valueOf(request.getCreatedAt()));
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        Long id = rs.getLong(1);
                        request.setId(id);
                        return id;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Create a request item
     * @param item RequestItem object to create
     * @return true if successful, false otherwise
     */
    public boolean createRequestItem(RequestItem item) {
        String sql = "INSERT INTO RequestItems (RequestId, ProductId, Quantity, LocationId, SourceLocationId, DestinationLocationId, ReceivedQuantity, PickedQuantity) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
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
            
            if (item.getSourceLocationId() != null) {
                stmt.setLong(5, item.getSourceLocationId());
            } else {
                stmt.setNull(5, Types.BIGINT);
            }
            
            if (item.getDestinationLocationId() != null) {
                stmt.setLong(6, item.getDestinationLocationId());
            } else {
                stmt.setNull(6, Types.BIGINT);
            }
            
            if (item.getReceivedQuantity() != null) {
                stmt.setInt(7, item.getReceivedQuantity());
            } else {
                stmt.setNull(7, Types.INTEGER);
            }
            
            if (item.getPickedQuantity() != null) {
                stmt.setInt(8, item.getPickedQuantity());
            } else {
                stmt.setNull(8, Types.INTEGER);
            }
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Get a request by ID
     * @param id Request ID
     * @return Request object or null if not found
     */
    public Request getRequestById(Long id) {
        String sql = "SELECT * FROM Requests WHERE Id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extractRequestFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Get all request items for a specific request
     * @param requestId Request ID
     * @return List of request items
     */
    public List<RequestItem> getRequestItems(Long requestId) {
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
     * Get all requests by type
     * @param type Request type (Inbound/Outbound/Transfer/Internal)
     * @return List of requests
     */
    public List<Request> getRequestsByType(String type) {
        List<Request> requests = new ArrayList<>();
        String sql = "SELECT * FROM Requests WHERE Type = ? ORDER BY CreatedAt DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, type);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    requests.add(extractRequestFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return requests;
    }
    
    /**
     * Get all requests
     * @return List of all requests
     */
    public List<Request> getAllRequests() {
        List<Request> requests = new ArrayList<>();
        String sql = "SELECT * FROM Requests ORDER BY CreatedAt DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                requests.add(extractRequestFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return requests;
    }
    
    /**
     * Approve a request
     * @param requestId Request ID
     * @param approvedBy User ID who approved
     * @return true if successful, false otherwise
     */
    public boolean approveRequest(Long requestId, Long approvedBy) {
        String sql = "UPDATE Requests SET Status = 'Approved', ApprovedBy = ?, ApprovedDate = GETDATE() WHERE Id = ? AND Status = 'Created'";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, approvedBy);
            stmt.setLong(2, requestId);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Reject a request
     * @param requestId Request ID
     * @param rejectedBy User ID who rejected
     * @param reason Rejection reason
     * @return true if successful, false otherwise
     */
    public boolean rejectRequest(Long requestId, Long rejectedBy, String reason) {
        String sql = "UPDATE Requests SET Status = 'Rejected', RejectedBy = ?, RejectedDate = GETDATE(), RejectionReason = ? WHERE Id = ? AND Status = 'Created'";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, rejectedBy);
            stmt.setString(2, reason);
            stmt.setLong(3, requestId);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Start execution of a request
     * @param requestId Request ID
     * @return true if successful, false otherwise
     */
    public boolean startExecution(Long requestId) {
        String sql = "UPDATE Requests SET Status = 'InProgress' WHERE Id = ? AND Status = 'Approved'";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, requestId);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Complete a request
     * @param requestId Request ID
     * @param completedBy User ID who completed
     * @return true if successful, false otherwise
     */
    public boolean completeRequest(Long requestId, Long completedBy) {
        String sql = "UPDATE Requests SET Status = 'Completed', CompletedBy = ?, CompletedDate = GETDATE() WHERE Id = ? AND Status = 'InProgress'";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, completedBy);
            stmt.setLong(2, requestId);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Update request item received quantity
     * @param requestId Request ID
     * @param productId Product ID
     * @param receivedQuantity Received quantity
     * @return true if successful, false otherwise
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
     * Update request item picked quantity
     * @param requestId Request ID
     * @param productId Product ID
     * @param pickedQuantity Picked quantity
     * @return true if successful, false otherwise
     */
    public boolean updatePickedQuantity(Long requestId, Long productId, Integer pickedQuantity) {
        String sql = "UPDATE RequestItems SET PickedQuantity = ? WHERE RequestId = ? AND ProductId = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, pickedQuantity);
            stmt.setLong(2, requestId);
            stmt.setLong(3, productId);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Extract Request object from ResultSet
     * @param rs ResultSet
     * @return Request object
     * @throws SQLException if database access error occurs
     */
    private Request extractRequestFromResultSet(ResultSet rs) throws SQLException {
        Request request = new Request();
        request.setId(rs.getLong("Id"));
        request.setType(rs.getString("Type"));
        request.setStatus(rs.getString("Status"));
        request.setCreatedBy(rs.getLong("CreatedBy"));
        
        Long approvedBy = rs.getLong("ApprovedBy");
        if (!rs.wasNull()) {
            request.setApprovedBy(approvedBy);
        }
        
        Timestamp approvedDate = rs.getTimestamp("ApprovedDate");
        if (approvedDate != null) {
            request.setApprovedDate(approvedDate.toLocalDateTime());
        }
        
        Long rejectedBy = rs.getLong("RejectedBy");
        if (!rs.wasNull()) {
            request.setRejectedBy(rejectedBy);
        }
        
        Timestamp rejectedDate = rs.getTimestamp("RejectedDate");
        if (rejectedDate != null) {
            request.setRejectedDate(rejectedDate.toLocalDateTime());
        }
        
        request.setRejectionReason(rs.getString("RejectionReason"));
        
        Long completedBy = rs.getLong("CompletedBy");
        if (!rs.wasNull()) {
            request.setCompletedBy(completedBy);
        }
        
        Timestamp completedDate = rs.getTimestamp("CompletedDate");
        if (completedDate != null) {
            request.setCompletedDate(completedDate.toLocalDateTime());
        }
        
        Long salesOrderId = rs.getLong("SalesOrderId");
        if (!rs.wasNull()) {
            request.setSalesOrderId(salesOrderId);
        }
        
        Long sourceWarehouseId = rs.getLong("SourceWarehouseId");
        if (!rs.wasNull()) {
            request.setSourceWarehouseId(sourceWarehouseId);
        }
        
        Long destinationWarehouseId = rs.getLong("DestinationWarehouseId");
        if (!rs.wasNull()) {
            request.setDestinationWarehouseId(destinationWarehouseId);
        }
        
        Timestamp expectedDate = rs.getTimestamp("ExpectedDate");
        if (expectedDate != null) {
            request.setExpectedDate(expectedDate.toLocalDateTime());
        }
        
        request.setNotes(rs.getString("Notes"));
        request.setReason(rs.getString("Reason"));
        
        Timestamp createdAt = rs.getTimestamp("CreatedAt");
        if (createdAt != null) {
            request.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        return request;
    }
    
    /**
     * Extract RequestItem object from ResultSet
     * @param rs ResultSet
     * @return RequestItem object
     * @throws SQLException if database access error occurs
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
        
        Long destinationLocationId = rs.getLong("DestinationLocationId");
        if (!rs.wasNull()) {
            item.setDestinationLocationId(destinationLocationId);
        }
        
        Integer receivedQuantity = rs.getInt("ReceivedQuantity");
        if (!rs.wasNull()) {
            item.setReceivedQuantity(receivedQuantity);
        }
        
        Integer pickedQuantity = rs.getInt("PickedQuantity");
        if (!rs.wasNull()) {
            item.setPickedQuantity(pickedQuantity);
        }
        
        return item;
    }
}
