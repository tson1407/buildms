package vn.edu.fpt.swp.controller;

import vn.edu.fpt.swp.model.Category;
import vn.edu.fpt.swp.service.CategoryService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Controller servlet for handling Category requests
 */
@WebServlet("/category")
public class CategoryController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private CategoryService categoryService;
    
    @Override
    public void init() throws ServletException {
        super.init();
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
                listCategories(request, response);
                break;
            case "view":
                viewCategory(request, response);
                break;
            case "create":
                showCreateForm(request, response);
                break;
            case "edit":
                showEditForm(request, response);
                break;
            case "delete":
                deleteCategory(request, response);
                break;
            case "search":
                searchCategories(request, response);
                break;
            default:
                listCategories(request, response);
                break;
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        
        if ("create".equals(action)) {
            createCategory(request, response);
        } else if ("update".equals(action)) {
            updateCategory(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/category");
        }
    }
    
    /**
     * List all categories
     */
    private void listCategories(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        List<Category> categories = categoryService.getAllCategories();
        request.setAttribute("categories", categories);
        request.getRequestDispatcher("/views/category/list.jsp").forward(request, response);
    }
    
    /**
     * View a single category
     */
    private void viewCategory(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            Long id = Long.parseLong(request.getParameter("id"));
            Category category = categoryService.getCategoryById(id);
            
            if (category != null) {
                request.setAttribute("category", category);
                request.getRequestDispatcher("/views/category/view.jsp").forward(request, response);
            } else {
                response.sendRedirect(request.getContextPath() + "/category?error=notfound");
            }
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/category?error=invalid");
        }
    }
    
    /**
     * Show create form
     */
    private void showCreateForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.getRequestDispatcher("/views/category/create.jsp").forward(request, response);
    }
    
    /**
     * Show edit form
     */
    private void showEditForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            Long id = Long.parseLong(request.getParameter("id"));
            Category category = categoryService.getCategoryById(id);
            
            if (category != null) {
                request.setAttribute("category", category);
                request.getRequestDispatcher("/views/category/edit.jsp").forward(request, response);
            } else {
                response.sendRedirect(request.getContextPath() + "/category?error=notfound");
            }
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/category?error=invalid");
        }
    }
    
    /**
     * Create a new category
     */
    private void createCategory(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            Category category = new Category();
            category.setName(request.getParameter("name"));
            category.setDescription(request.getParameter("description"));
            
            boolean success = categoryService.createCategory(category);
            
            if (success) {
                response.sendRedirect(request.getContextPath() + "/category?success=created");
            } else {
                request.setAttribute("error", "Failed to create category. Name might already exist.");
                request.setAttribute("category", category);
                request.getRequestDispatcher("/views/category/create.jsp").forward(request, response);
            }
        } catch (Exception e) {
            request.setAttribute("error", "Invalid input: " + e.getMessage());
            request.getRequestDispatcher("/views/category/create.jsp").forward(request, response);
        }
    }
    
    /**
     * Update an existing category
     */
    private void updateCategory(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            Category category = new Category();
            category.setId(Long.parseLong(request.getParameter("id")));
            category.setName(request.getParameter("name"));
            category.setDescription(request.getParameter("description"));
            
            boolean success = categoryService.updateCategory(category);
            
            if (success) {
                response.sendRedirect(request.getContextPath() + "/category?success=updated");
            } else {
                request.setAttribute("error", "Failed to update category. Name might already exist.");
                request.setAttribute("category", category);
                request.getRequestDispatcher("/views/category/edit.jsp").forward(request, response);
            }
        } catch (Exception e) {
            request.setAttribute("error", "Invalid input: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/category?error=update");
        }
    }
    
    /**
     * Delete a category
     */
    private void deleteCategory(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            Long id = Long.parseLong(request.getParameter("id"));
            
            // Check if category has products
            if (categoryService.hasProducts(id)) {
                response.sendRedirect(request.getContextPath() + "/category?error=hasproducts");
                return;
            }
            
            boolean success = categoryService.deleteCategory(id);
            
            if (success) {
                response.sendRedirect(request.getContextPath() + "/category?success=deleted");
            } else {
                response.sendRedirect(request.getContextPath() + "/category?error=delete");
            }
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/category?error=invalid");
        }
    }
    
    /**
     * Search categories
     */
    private void searchCategories(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String keyword = request.getParameter("keyword");
        List<Category> categories = categoryService.searchCategories(keyword);
        request.setAttribute("categories", categories);
        request.setAttribute("keyword", keyword);
        request.getRequestDispatcher("/views/category/list.jsp").forward(request, response);
    }
}
