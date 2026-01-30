package vn.edu.fpt.swp.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.edu.fpt.swp.model.Category;
import vn.edu.fpt.swp.model.User;
import vn.edu.fpt.swp.service.CategoryService;

import java.io.IOException;
import java.util.List;

/**
 * Controller for category operations
 */
@WebServlet("/category")
public class CategoryController extends HttpServlet {
    private CategoryService categoryService;
    
    @Override
    public void init() {
        categoryService = new CategoryService();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String action = request.getParameter("action");
        
        if (action == null) {
            action = "list";
        }
        
        switch (action) {
            case "list":
                showCategoryList(request, response);
                break;
            case "create":
                showCreateForm(request, response);
                break;
            case "edit":
                showEditForm(request, response);
                break;
            default:
                showCategoryList(request, response);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String action = request.getParameter("action");
        
        if (action == null) {
            action = "list";
        }
        
        switch (action) {
            case "save":
                handleSaveCategory(request, response);
                break;
            case "update":
                handleUpdateCategory(request, response);
                break;
            case "delete":
                handleDeleteCategory(request, response);
                break;
            default:
                showCategoryList(request, response);
        }
    }
    
    /**
     * Show category list
     */
    private void showCategoryList(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String search = request.getParameter("search");
        List<Category> categories;
        
        if (search != null && !search.trim().isEmpty()) {
            categories = categoryService.searchCategories(search.trim());
        } else {
            categories = categoryService.getAllCategories();
        }
        
        // Add product counts
        for (Category category : categories) {
            int productCount = categoryService.getProductCount(category.getId());
            // Store in request for JSP access
            request.setAttribute("productCount_" + category.getId(), productCount);
        }
        
        request.setAttribute("categories", categories);
        request.setAttribute("search", search);
        
        request.getRequestDispatcher("/views/category/list.jsp").forward(request, response);
    }
    
    /**
     * Show create category form
     */
    private void showCreateForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.getRequestDispatcher("/views/category/form.jsp").forward(request, response);
    }
    
    /**
     * Show edit category form
     */
    private void showEditForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String idStr = request.getParameter("id");
        
        if (idStr == null || idStr.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/category");
            return;
        }
        
        try {
            Long id = Long.parseLong(idStr);
            Category category = categoryService.getCategoryById(id);
            
            if (category == null) {
                request.setAttribute("error", "Category not found");
                showCategoryList(request, response);
                return;
            }
            
            request.setAttribute("category", category);
            request.getRequestDispatcher("/views/category/form.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/category");
        }
    }
    
    /**
     * Handle save new category
     */
    private void handleSaveCategory(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        
        // Validate input
        if (name == null || name.trim().isEmpty()) {
            request.setAttribute("error", "Category name is required");
            showCreateForm(request, response);
            return;
        }
        
        Category created = categoryService.createCategory(name, description);
        
        if (created == null) {
            request.setAttribute("error", "Category name already exists or creation failed");
            request.setAttribute("name", name);
            request.setAttribute("description", description);
            showCreateForm(request, response);
            return;
        }
        
        request.setAttribute("success", "Category created successfully");
        showCategoryList(request, response);
    }
    
    /**
     * Handle update category
     */
    private void handleUpdateCategory(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String idStr = request.getParameter("id");
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        
        // Validate input
        if (idStr == null || idStr.isEmpty() || name == null || name.trim().isEmpty()) {
            request.setAttribute("error", "Category ID and name are required");
            showCategoryList(request, response);
            return;
        }
        
        try {
            Long id = Long.parseLong(idStr);
            boolean updated = categoryService.updateCategory(id, name, description);
            
            if (!updated) {
                request.setAttribute("error", "Category name already exists or update failed");
                Category category = categoryService.getCategoryById(id);
                request.setAttribute("category", category);
                showEditForm(request, response);
                return;
            }
            
            request.setAttribute("success", "Category updated successfully");
            showCategoryList(request, response);
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid category ID");
            showCategoryList(request, response);
        }
    }
    
    /**
     * Handle delete category
     */
    private void handleDeleteCategory(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String idStr = request.getParameter("id");
        
        if (idStr == null || idStr.isEmpty()) {
            request.setAttribute("error", "Category ID is required");
            showCategoryList(request, response);
            return;
        }
        
        try {
            Long id = Long.parseLong(idStr);
            Category category = categoryService.getCategoryById(id);
            
            if (category == null) {
                request.setAttribute("error", "Category not found");
                showCategoryList(request, response);
                return;
            }
            
            // Check for associated products
            int productCount = categoryService.getProductCount(id);
            if (productCount > 0) {
                request.setAttribute("error", 
                    "Cannot delete category with associated products. Please reassign or remove products first.");
                request.setAttribute("categoryName", category.getName());
                request.setAttribute("productCount", productCount);
                showCategoryList(request, response);
                return;
            }
            
            boolean deleted = categoryService.deleteCategory(id);
            
            if (!deleted) {
                request.setAttribute("error", "Failed to delete category");
                showCategoryList(request, response);
                return;
            }
            
            request.setAttribute("success", "Category deleted successfully");
            showCategoryList(request, response);
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid category ID");
            showCategoryList(request, response);
        }
    }
}
