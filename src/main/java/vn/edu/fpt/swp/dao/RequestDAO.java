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
        String sql = "INSERT INTO Requests (Type, Status, CreatedBy, DestinationWarehouseId, " +
                    "ExpectedDate, Notes, CreatedAt) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, request.getType());
            stmt.setString(2, request.getStatus());
            stmt.setLong(3, request.getCreatedBy());
            
            if (request.getDestinationWarehouseId() != null) {
                stmt.setLong(4, request.getDestinationWarehouseId());
            } else {
                stmt.setNull(4, Types.BIGINT);
            }
            
            if (request.getExpectedDate() != null) {
                stmt.setTimestamp(5, Timestamp.valueOf(request.getExpectedDate()));
            } else {
                stmt.setNull(5, Types.TIMESTAMP);
            }
            
            stmt.setString(6, request.getNotes());
            stmt.setTimestamp(7, Timestamp.valueOf(request.getCreatedAt()));
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getLong(1);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Get all requests by type
     * @param type Request type (Inbound, Outbound, Transfer, Internal)
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
     * Get requests by type and status
     * @param type Request type
     * @param status Request status
     * @return List of requests
     */
    public List<Request> getRequestsByTypeAndStatus(String type, String status) {
        List<Request> requests = new ArrayList<>();
        String sql = "SELECT * FROM Requests WHERE Type = ? AND Status = ? ORDER BY CreatedAt DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, type);
            stmt.setString(2, status);
            
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
     * Update request status
     * @param id Request ID
     * @param status New status
     * @param userId User ID performing the update
     * @return true if successful
     */
    public boolean updateRequestStatus(Long id, String status, Long userId) {
        String sql;
        
        if ("Approved".equals(status)) {
            sql = "UPDATE Requests SET Status = ?, ApprovedBy = ?, ApprovedDate = ? WHERE Id = ?";
        } else if ("Rejected".equals(status)) {
            sql = "UPDATE Requests SET Status = ?, RejectedBy = ?, RejectedDate = ? WHERE Id = ?";
        } else if ("Completed".equals(status)) {
            sql = "UPDATE Requests SET Status = ?, CompletedBy = ?, CompletedDate = ? WHERE Id = ?";
        } else {
            sql = "UPDATE Requests SET Status = ? WHERE Id = ?";
        }
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, status);
            
            if ("Approved".equals(status) || "Rejected".equals(status) || "Completed".equals(status)) {
                stmt.setLong(2, userId);
                stmt.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
                stmt.setLong(4, id);
            } else {
                stmt.setLong(2, id);
            }
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Reject a request with reason
     * @param id Request ID
     * @param userId User ID performing the rejection
     * @param reason Rejection reason
     * @return true if successful
     */
    public boolean rejectRequest(Long id, Long userId, String reason) {
        String sql = "UPDATE Requests SET Status = 'Rejected', RejectedBy = ?, " +
                    "RejectedDate = ?, RejectionReason = ? WHERE Id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, userId);
            stmt.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            stmt.setString(3, reason);
            stmt.setLong(4, id);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Extract Request object from ResultSet
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
        
        Long destWarehouseId = rs.getLong("DestinationWarehouseId");
        if (!rs.wasNull()) {
            request.setDestinationWarehouseId(destWarehouseId);
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
}
