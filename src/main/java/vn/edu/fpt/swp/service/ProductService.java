package vn.edu.fpt.swp.service;

import vn.edu.fpt.swp.dao.ProductDAO;
import vn.edu.fpt.swp.model.Product;

import java.util.List;

/**
 * Service layer for Product business logic
 */
public class ProductService {
    private ProductDAO productDAO;
    
    public ProductService() {
        this.productDAO = new ProductDAO();
    }
    
    /**
     * Create a new product
     * @param product Product to create
     * @return true if successful, false otherwise
     */
    public boolean createProduct(Product product) {
        // Add business logic validation here
        if (product == null || product.getName() == null || product.getName().trim().isEmpty()) {
            return false;
        }
        
        if (product.getSku() == null || product.getSku().trim().isEmpty()) {
            return false;
        }

        // Check if SKU already exists
        if (productDAO.getProductBySku(product.getSku()) != null) {
            return false; // SKU must be unique
        }
        
        if (product.getCategoryId() == null || product.getCategoryId() <= 0) {
            return false;
        }
        
        return productDAO.createProduct(product);
    }
    
    /**
     * Get all products
     * @return List of all products
     */
    public List<Product> getAllProducts() {
        return productDAO.getAllProducts();
    }
    
    /**
     * Get a product by ID
     * @param id Product ID
     * @return Product object or null if not found
     */
    public Product getProductById(Long id) {
        if (id == null || id <= 0) {
            return null;
        }
        return productDAO.getProductById(id);
    }

    /**
     * Get a product by SKU
     * @param sku Product SKU
     * @return Product object or null if not found
     */
    public Product getProductBySku(String sku) {
        if (sku == null || sku.trim().isEmpty()) {
            return null;
        }
        return productDAO.getProductBySku(sku);
    }

    /**
     * Get products by category
     * @param categoryId Category ID
     * @return List of products in the category
     */
    public List<Product> getProductsByCategory(Long categoryId) {
        if (categoryId == null || categoryId <= 0) {
            return null;
        }
        return productDAO.getProductsByCategory(categoryId);
    }
    
    /**
     * Update a product
     * @param product Product with updated information
     * @return true if successful, false otherwise
     */
    public boolean updateProduct(Product product) {
        if (product == null || product.getId() == null) {
            return false;
        }
        
        // Validate product data
        if (product.getName() == null || product.getName().trim().isEmpty()) {
            return false;
        }
        
        if (product.getSku() == null || product.getSku().trim().isEmpty()) {
            return false;
        }

        // Check if SKU is being changed and if new SKU already exists
        Product existingProduct = productDAO.getProductById(product.getId());
        if (existingProduct != null && !existingProduct.getSku().equals(product.getSku())) {
            Product skuCheck = productDAO.getProductBySku(product.getSku());
            if (skuCheck != null) {
                return false; // New SKU already exists
            }
        }
        
        if (product.getCategoryId() == null || product.getCategoryId() <= 0) {
            return false;
        }
        
        return productDAO.updateProduct(product);
    }
    
    /**
     * Delete a product (soft delete)
     * @param id Product ID
     * @return true if successful, false otherwise
     */
    public boolean deleteProduct(Long id) {
        if (id == null || id <= 0) {
            return false;
        }
        return productDAO.deleteProduct(id);
    }
    
    /**
     * Search products by keyword
     * @param keyword Search keyword
     * @return List of matching products
     */
    public List<Product> searchProducts(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllProducts();
        }
        return productDAO.searchProducts(keyword.trim());
    }
}
