package vn.edu.fpt.swp.dao;

import vn.edu.fpt.swp.model.Category;
import vn.edu.fpt.swp.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Category entity
 */
public class CategoryDAO {
    
    /**
     * Create a new category in the database
     * @param category Category object to create
     * @return true if successful, false otherwise
     */
    public boolean createCategory(Category category) {
        String sql = "INSERT INTO Categories (Name, Description) VALUES (?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, category.getName());
            stmt.setString(2, category.getDescription());
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        category.setId(rs.getLong(1));
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
     * Get all categories from the database
     * @return List of all categories
     */
    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT * FROM Categories ORDER BY Name";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                categories.add(extractCategoryFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories;
    }
    
    /**
     * Get a category by ID
     * @param id Category ID
     * @return Category object or null if not found
     */
    public Category getCategoryById(Long id) {
        String sql = "SELECT * FROM Categories WHERE Id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extractCategoryFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Get a category by name
     * @param name Category name
     * @return Category object or null if not found
     */
    public Category getCategoryByName(String name) {
        String sql = "SELECT * FROM Categories WHERE Name = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, name);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extractCategoryFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Update a category in the database
     * @param category Category object with updated information
     * @return true if successful, false otherwise
     */
    public boolean updateCategory(Category category) {
        String sql = "UPDATE Categories SET Name = ?, Description = ? WHERE Id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, category.getName());
            stmt.setString(2, category.getDescription());
            stmt.setLong(3, category.getId());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Delete a category from the database
     * @param id Category ID
     * @return true if successful, false otherwise
     */
    public boolean deleteCategory(Long id) {
        String sql = "DELETE FROM Categories WHERE Id = ?";
        
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
     * Check if category has products
     * @param id Category ID
     * @return true if category has products, false otherwise
     */
    public boolean hasProducts(Long id) {
        String sql = "SELECT COUNT(*) as count FROM Products WHERE CategoryId = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("count") > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Search categories by name
     * @param keyword Search keyword
     * @return List of matching categories
     */
    public List<Category> searchCategories(String keyword) {
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT * FROM Categories WHERE Name LIKE ? OR Description LIKE ? ORDER BY Name";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            String searchPattern = "%" + keyword + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    categories.add(extractCategoryFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories;
    }
    
    /**
     * Extract Category object from ResultSet
     * @param rs ResultSet from query
     * @return Category object
     * @throws SQLException if error occurs
     */
    private Category extractCategoryFromResultSet(ResultSet rs) throws SQLException {
        Category category = new Category();
        category.setId(rs.getLong("Id"));
        category.setName(rs.getString("Name"));
        category.setDescription(rs.getString("Description"));
        return category;
    }
}
