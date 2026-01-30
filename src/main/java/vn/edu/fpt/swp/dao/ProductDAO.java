package vn.edu.fpt.swp.dao;

import vn.edu.fpt.swp.model.Product;
import vn.edu.fpt.swp.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Product entity
 */
public class ProductDAO {
    
    /**
     * Create a new product in the database
     * @param product Product object to create
     * @return true if successful, false otherwise
     */
    public boolean createProduct(Product product) {
        String sql = "INSERT INTO Products (SKU, Name, Unit, CategoryId, IsActive, CreatedAt) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, product.getSku());
            stmt.setString(2, product.getName());
            stmt.setString(3, product.getUnit());
            stmt.setLong(4, product.getCategoryId());
            stmt.setBoolean(5, product.isActive());
            stmt.setTimestamp(6, Timestamp.valueOf(product.getCreatedAt()));
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        product.setId(rs.getLong(1));
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
     * Get all products from the database
     * @return List of all products
     */
    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM Products WHERE IsActive = 1 ORDER BY CreatedAt DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                products.add(extractProductFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }
    
    /**
     * Get a product by ID
     * @param id Product ID
     * @return Product object or null if not found
     */
    public Product getProductById(Long id) {
        String sql = "SELECT * FROM Products WHERE Id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extractProductFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Get a product by SKU
     * @param sku Product SKU
     * @return Product object or null if not found
     */
    public Product getProductBySku(String sku) {
        String sql = "SELECT * FROM Products WHERE SKU = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, sku);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extractProductFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Get products by category
     * @param categoryId Category ID
     * @return List of products in the category
     */
    public List<Product> getProductsByCategory(Long categoryId) {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM Products WHERE CategoryId = ? AND IsActive = 1 ORDER BY Name";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, categoryId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    products.add(extractProductFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }
    
    /**
     * Update a product in the database
     * @param product Product object with updated information
     * @return true if successful, false otherwise
     */
    public boolean updateProduct(Product product) {
        String sql = "UPDATE Products SET SKU = ?, Name = ?, Unit = ?, CategoryId = ?, IsActive = ? WHERE Id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, product.getSku());
            stmt.setString(2, product.getName());
            stmt.setString(3, product.getUnit());
            stmt.setLong(4, product.getCategoryId());
            stmt.setBoolean(5, product.isActive());
            stmt.setLong(6, product.getId());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Delete a product (soft delete - sets IsActive to false)
     * @param id Product ID
     * @return true if successful, false otherwise
     */
    public boolean deleteProduct(Long id) {
        String sql = "UPDATE Products SET IsActive = 0 WHERE Id = ?";
        
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
     * Search products by name or SKU
     * @param keyword Search keyword
     * @return List of matching products
     */
    public List<Product> searchProducts(String keyword) {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM Products WHERE IsActive = 1 AND " +
                    "(Name LIKE ? OR SKU LIKE ?) " +
                    "ORDER BY Name";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            String searchPattern = "%" + keyword + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    products.add(extractProductFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }
    
    /**
     * Extract Product object from ResultSet
     * @param rs ResultSet from query
     * @return Product object
     * @throws SQLException if error occurs
     */
    private Product extractProductFromResultSet(ResultSet rs) throws SQLException {
        Product product = new Product();
        product.setId(rs.getLong("Id"));
        product.setSku(rs.getString("SKU"));
        product.setName(rs.getString("Name"));
        product.setUnit(rs.getString("Unit"));
        product.setCategoryId(rs.getLong("CategoryId"));
        product.setActive(rs.getBoolean("IsActive"));
        
        Timestamp createdAt = rs.getTimestamp("CreatedAt");
        if (createdAt != null) {
            product.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        return product;
    }
}
