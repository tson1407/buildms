package vn.edu.fpt.swp.service;

import vn.edu.fpt.swp.dao.CategoryDAO;
import vn.edu.fpt.swp.model.Category;

import java.util.List;

/**
 * Service layer for Category business logic
 */
public class CategoryService {
    private CategoryDAO categoryDAO;
    
    public CategoryService() {
        this.categoryDAO = new CategoryDAO();
    }
    
    /**
     * Create a new category
     * @param category Category to create
     * @return true if successful, false otherwise
     */
    public boolean createCategory(Category category) {
        // Add business logic validation here
        if (category == null || category.getName() == null || category.getName().trim().isEmpty()) {
            return false;
        }

        // Check if category name already exists
        if (categoryDAO.getCategoryByName(category.getName()) != null) {
            return false; // Category name must be unique
        }
        
        return categoryDAO.createCategory(category);
    }
    
    /**
     * Get all categories
     * @return List of all categories
     */
    public List<Category> getAllCategories() {
        return categoryDAO.getAllCategories();
    }
    
    /**
     * Get a category by ID
     * @param id Category ID
     * @return Category object or null if not found
     */
    public Category getCategoryById(Long id) {
        if (id == null || id <= 0) {
            return null;
        }
        return categoryDAO.getCategoryById(id);
    }

    /**
     * Get a category by name
     * @param name Category name
     * @return Category object or null if not found
     */
    public Category getCategoryByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return null;
        }
        return categoryDAO.getCategoryByName(name);
    }
    
    /**
     * Update a category
     * @param category Category with updated information
     * @return true if successful, false otherwise
     */
    public boolean updateCategory(Category category) {
        if (category == null || category.getId() == null) {
            return false;
        }
        
        // Validate category data
        if (category.getName() == null || category.getName().trim().isEmpty()) {
            return false;
        }

        // Check if category name is being changed and if new name already exists
        Category existingCategory = categoryDAO.getCategoryById(category.getId());
        if (existingCategory != null && !existingCategory.getName().equals(category.getName())) {
            Category nameCheck = categoryDAO.getCategoryByName(category.getName());
            if (nameCheck != null) {
                return false; // New name already exists
            }
        }
        
        return categoryDAO.updateCategory(category);
    }
    
    /**
     * Delete a category
     * @param id Category ID
     * @return true if successful, false otherwise
     */
    public boolean deleteCategory(Long id) {
        if (id == null || id <= 0) {
            return false;
        }

        // Check if category has products
        if (categoryDAO.hasProducts(id)) {
            return false; // Cannot delete category with products
        }
        
        return categoryDAO.deleteCategory(id);
    }

    /**
     * Check if category has products
     * @param id Category ID
     * @return true if category has products, false otherwise
     */
    public boolean hasProducts(Long id) {
        if (id == null || id <= 0) {
            return false;
        }
        return categoryDAO.hasProducts(id);
    }
    
    /**
     * Search categories by keyword
     * @param keyword Search keyword
     * @return List of matching categories
     */
    public List<Category> searchCategories(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllCategories();
        }
        return categoryDAO.searchCategories(keyword.trim());
    }
}
