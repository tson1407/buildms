package vn.edu.fpt.swp.dao;

import vn.edu.fpt.swp.model.User;
import vn.edu.fpt.swp.util.DBConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for User entity
 */
public class UserDAO {
    
    /**
     * Find user by username
     * @param username Username to search for
     * @return User object or null if not found
     */
    public User findByUsername(String username) {
        String sql = "SELECT Id, Username, Name, Email, PasswordHash, Role, Status, WarehouseId, CreatedAt, LastLogin " +
                     "FROM Users WHERE Username = ?";
        
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
     * @return User object or null if not found
     */
    public User findByEmail(String email) {
        String sql = "SELECT Id, Username, Name, Email, PasswordHash, Role, Status, WarehouseId, CreatedAt, LastLogin " +
                     "FROM Users WHERE Email = ?";
        
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
     * Find user by ID
     * @param id User ID
     * @return User object or null if not found
     */
    public User findById(Long id) {
        String sql = "SELECT Id, Username, Name, Email, PasswordHash, Role, Status, WarehouseId, CreatedAt, LastLogin " +
                     "FROM Users WHERE Id = ?";
        
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
     * Get all users
     * @return List of all users
     */
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT Id, Username, Name, Email, PasswordHash, Role, Status, WarehouseId, CreatedAt, LastLogin " +
                     "FROM Users ORDER BY CreatedAt DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                users.add(mapResultSetToUser(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }
    
    /**
     * Get users by role
     * @param role Role to filter by
     * @return List of users with specified role
     */
    public List<User> findByRole(String role) {
        List<User> users = new ArrayList<>();
        String sql = "SELECT Id, Username, Name, Email, PasswordHash, Role, Status, WarehouseId, CreatedAt, LastLogin " +
                     "FROM Users WHERE Role = ? ORDER BY Name";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, role);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    users.add(mapResultSetToUser(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }
    
    /**
     * Create a new user
     * @param user User object to create
     * @return true if successful, false otherwise
     */
    public boolean create(User user) {
        String sql = "INSERT INTO Users (Username, Name, Email, PasswordHash, Role, Status, WarehouseId, CreatedAt) " +
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
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        user.setId(rs.getLong(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Update an existing user
     * @param user User object with updated data
     * @return true if successful, false otherwise
     */
    public boolean update(User user) {
        String sql = "UPDATE Users SET Username = ?, Name = ?, Email = ?, Role = ?, " +
                     "Status = ?, WarehouseId = ? WHERE Id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getName());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getRole());
            stmt.setString(5, user.getStatus());
            
            if (user.getWarehouseId() != null) {
                stmt.setLong(6, user.getWarehouseId());
            } else {
                stmt.setNull(6, Types.BIGINT);
            }
            
            stmt.setLong(7, user.getId());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Update user password
     * @param userId User ID
     * @param passwordHash New password hash
     * @return true if successful, false otherwise
     */
    public boolean updatePassword(Long userId, String passwordHash) {
        String sql = "UPDATE Users SET PasswordHash = ? WHERE Id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, passwordHash);
            stmt.setLong(2, userId);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Update last login timestamp
     * @param userId User ID
     * @return true if successful, false otherwise
     */
    public boolean updateLastLogin(Long userId) {
        String sql = "UPDATE Users SET LastLogin = ? WHERE Id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setLong(2, userId);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Delete a user (soft delete by setting status to Inactive)
     * @param id User ID
     * @return true if successful, false otherwise
     */
    public boolean delete(Long id) {
        String sql = "UPDATE Users SET Status = 'Inactive' WHERE Id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Check if username exists
     * @param username Username to check
     * @return true if exists, false otherwise
     */
    public boolean usernameExists(String username) {
        String sql = "SELECT COUNT(*) FROM Users WHERE Username = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            
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
     * Check if email exists
     * @param email Email to check
     * @return true if exists, false otherwise
     */
    public boolean emailExists(String email) {
        String sql = "SELECT COUNT(*) FROM Users WHERE Email = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, email);
            
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
     * Map ResultSet to User object
     * @param rs ResultSet from query
     * @return User object
     * @throws SQLException if error occurs
     */
    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getLong("Id"));
        user.setUsername(rs.getString("Username"));
        user.setName(rs.getString("Name"));
        user.setEmail(rs.getString("Email"));
        user.setPasswordHash(rs.getString("PasswordHash"));
        user.setRole(rs.getString("Role"));
        user.setStatus(rs.getString("Status"));
        
        long warehouseId = rs.getLong("WarehouseId");
        if (!rs.wasNull()) {
            user.setWarehouseId(warehouseId);
        }
        
        Timestamp createdAt = rs.getTimestamp("CreatedAt");
        if (createdAt != null) {
            user.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        Timestamp lastLogin = rs.getTimestamp("LastLogin");
        if (lastLogin != null) {
            user.setLastLogin(lastLogin.toLocalDateTime());
        }
        
        return user;
    }
}
