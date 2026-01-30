package vn.edu.fpt.swp.dao;

import vn.edu.fpt.swp.model.SalesOrder;
import vn.edu.fpt.swp.model.SalesOrderItem;
import vn.edu.fpt.swp.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for SalesOrder entity
 */
public class SalesOrderDAO {
    
    /**
     * Create a new sales order in the database
     * @param order SalesOrder object to create
     * @return Generated order ID or null if failed
     */
    public Long createSalesOrder(SalesOrder order) {
        String sql = "INSERT INTO SalesOrders (OrderNo, CustomerId, Status, CreatedBy, CreatedAt) " +
                    "VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, order.getOrderNo());
            stmt.setLong(2, order.getCustomerId());
            stmt.setString(3, order.getStatus());
            stmt.setLong(4, order.getCreatedBy());
            stmt.setTimestamp(5, Timestamp.valueOf(order.getCreatedAt()));
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        Long id = rs.getLong(1);
                        order.setId(id);
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
     * Create a sales order item
     * @param item SalesOrderItem object to create
     * @return true if successful, false otherwise
     */
    public boolean createSalesOrderItem(SalesOrderItem item) {
        String sql = "INSERT INTO SalesOrderItems (SalesOrderId, ProductId, Quantity) " +
                    "VALUES (?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, item.getSalesOrderId());
            stmt.setLong(2, item.getProductId());
            stmt.setInt(3, item.getQuantity());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Get a sales order by ID
     * @param id Sales order ID
     * @return SalesOrder object or null if not found
     */
    public SalesOrder getSalesOrderById(Long id) {
        String sql = "SELECT * FROM SalesOrders WHERE Id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extractSalesOrderFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Get all sales order items for a specific order
     * @param orderId Sales order ID
     * @return List of sales order items
     */
    public List<SalesOrderItem> getSalesOrderItems(Long orderId) {
        List<SalesOrderItem> items = new ArrayList<>();
        String sql = "SELECT * FROM SalesOrderItems WHERE SalesOrderId = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, orderId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    items.add(extractSalesOrderItemFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }
    
    /**
     * Get all sales orders
     * @return List of all sales orders
     */
    public List<SalesOrder> getAllSalesOrders() {
        List<SalesOrder> orders = new ArrayList<>();
        String sql = "SELECT * FROM SalesOrders ORDER BY CreatedAt DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                orders.add(extractSalesOrderFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }
    
    /**
     * Update sales order status
     * @param orderId Order ID
     * @param status New status
     * @return true if successful, false otherwise
     */
    public boolean updateSalesOrderStatus(Long orderId, String status) {
        String sql = "UPDATE SalesOrders SET Status = ? WHERE Id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, status);
            stmt.setLong(2, orderId);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Confirm a sales order
     * @param orderId Order ID
     * @param confirmedBy User ID who confirmed
     * @return true if successful, false otherwise
     */
    public boolean confirmSalesOrder(Long orderId, Long confirmedBy) {
        String sql = "UPDATE SalesOrders SET Status = 'Confirmed', ConfirmedBy = ?, ConfirmedDate = GETDATE() WHERE Id = ? AND Status = 'Draft'";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, confirmedBy);
            stmt.setLong(2, orderId);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Cancel a sales order
     * @param orderId Order ID
     * @param cancelledBy User ID who cancelled
     * @param reason Cancellation reason
     * @return true if successful, false otherwise
     */
    public boolean cancelSalesOrder(Long orderId, Long cancelledBy, String reason) {
        String sql = "UPDATE SalesOrders SET Status = 'Cancelled', CancelledBy = ?, CancelledDate = GETDATE(), CancellationReason = ? WHERE Id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, cancelledBy);
            stmt.setString(2, reason);
            stmt.setLong(3, orderId);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Generate unique order number
     * @return Generated order number
     */
    public String generateOrderNumber() {
        String sql = "SELECT TOP 1 OrderNo FROM SalesOrders ORDER BY Id DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                String lastOrderNo = rs.getString("OrderNo");
                // Extract number from order number (e.g., SO-0001 -> 1)
                String numPart = lastOrderNo.substring(3);
                int nextNum = Integer.parseInt(numPart) + 1;
                return String.format("SO-%04d", nextNum);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // Default starting number
        return "SO-0001";
    }
    
    /**
     * Extract SalesOrder object from ResultSet
     * @param rs ResultSet
     * @return SalesOrder object
     * @throws SQLException if database access error occurs
     */
    private SalesOrder extractSalesOrderFromResultSet(ResultSet rs) throws SQLException {
        SalesOrder order = new SalesOrder();
        order.setId(rs.getLong("Id"));
        order.setOrderNo(rs.getString("OrderNo"));
        order.setCustomerId(rs.getLong("CustomerId"));
        order.setStatus(rs.getString("Status"));
        order.setCreatedBy(rs.getLong("CreatedBy"));
        
        Timestamp createdAt = rs.getTimestamp("CreatedAt");
        if (createdAt != null) {
            order.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        Long confirmedBy = rs.getLong("ConfirmedBy");
        if (!rs.wasNull()) {
            order.setConfirmedBy(confirmedBy);
        }
        
        Timestamp confirmedDate = rs.getTimestamp("ConfirmedDate");
        if (confirmedDate != null) {
            order.setConfirmedDate(confirmedDate.toLocalDateTime());
        }
        
        Long cancelledBy = rs.getLong("CancelledBy");
        if (!rs.wasNull()) {
            order.setCancelledBy(cancelledBy);
        }
        
        Timestamp cancelledDate = rs.getTimestamp("CancelledDate");
        if (cancelledDate != null) {
            order.setCancelledDate(cancelledDate.toLocalDateTime());
        }
        
        order.setCancellationReason(rs.getString("CancellationReason"));
        
        return order;
    }
    
    /**
     * Extract SalesOrderItem object from ResultSet
     * @param rs ResultSet
     * @return SalesOrderItem object
     * @throws SQLException if database access error occurs
     */
    private SalesOrderItem extractSalesOrderItemFromResultSet(ResultSet rs) throws SQLException {
        SalesOrderItem item = new SalesOrderItem();
        item.setSalesOrderId(rs.getLong("SalesOrderId"));
        item.setProductId(rs.getLong("ProductId"));
        item.setQuantity(rs.getInt("Quantity"));
        return item;
    }
}
