package vn.edu.fpt.swp.dao;

import vn.edu.fpt.swp.model.User;
import vn.edu.fpt.swp.util.DBConnection;

import java.sql.*;
import java.time.LocalDateTime;

/**
 * Data Access Object for User entity
 */
public class UserDAO {
    
    /**
     * Find user by ID
     * @param id User ID
     * @return User object if found, null otherwise
     */
    public User findById(Long id) {
        String sql = "SELECT id, username, name, email, passwordHash, role, status, warehouseId, createdAt, lastLogin " +
                     "FROM Users WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUser(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Find user by username
     * @param username Username to search for
     * @return User object if found, null otherwise
     */
    public User findByUsername(String username) {
        String sql = "SELECT id, username, name, email, passwordHash, role, status, warehouseId, createdAt, lastLogin " +
                     "FROM Users WHERE username = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUser(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Find user by email
     * @param email Email to search for
     * @return User object if found, null otherwise
     */
    public User findByEmail(String email) {
        String sql = "SELECT id, username, name, email, passwordHash, role, status, warehouseId, createdAt, lastLogin " +
                     "FROM Users WHERE email = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, email);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUser(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Update user's last login timestamp
     * @param userId User ID
     * @return true if update successful
     */
    public boolean updateLastLogin(Long userId) {
        String sql = "UPDATE Users SET lastLogin = ? WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setLong(2, userId);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Update user's password
     * @param userId User ID
     * @param newPasswordHash New password hash (includes salt)
     * @return true if update successful
     */
    public boolean updatePassword(Long userId, String newPasswordHash) {
        String sql = "UPDATE Users SET passwordHash = ? WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, newPasswordHash);
            stmt.setLong(2, userId);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Create new user
     * @param user User object to create
     * @return Created user with ID, or null if failed
     */
    public User create(User user) {
        String sql = "INSERT INTO Users (username, name, email, passwordHash, role, status, warehouseId, createdAt) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getName());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getPasswordHash());
            stmt.setString(5, user.getRole());
            stmt.setString(6, user.getStatus());
            
            if (user.getWarehouseId() != null) {
                stmt.setLong(7, user.getWarehouseId());
            } else {
                stmt.setNull(7, Types.BIGINT);
            }
            
            stmt.setTimestamp(8, Timestamp.valueOf(user.getCreatedAt()));
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        user.setId(generatedKeys.getLong(1));
                        return user;
                    }
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Map ResultSet to User object
     * @param rs ResultSet
     * @return User object
     */
    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getLong("id"));
        user.setUsername(rs.getString("username"));
        user.setName(rs.getString("name"));
        user.setEmail(rs.getString("email"));
        user.setPasswordHash(rs.getString("passwordHash"));
        user.setRole(rs.getString("role"));
        user.setStatus(rs.getString("status"));
        
        Long warehouseId = rs.getLong("warehouseId");
        if (!rs.wasNull()) {
            user.setWarehouseId(warehouseId);
        }
        
        Timestamp createdAt = rs.getTimestamp("createdAt");
        if (createdAt != null) {
            user.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        Timestamp lastLogin = rs.getTimestamp("lastLogin");
        if (lastLogin != null) {
            user.setLastLogin(lastLogin.toLocalDateTime());
        }
        
        return user;
    }
}
