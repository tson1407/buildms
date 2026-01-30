package vn.edu.fpt.swp.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.edu.fpt.swp.model.Category;
import vn.edu.fpt.swp.model.Product;
import vn.edu.fpt.swp.service.CategoryService;
import vn.edu.fpt.swp.service.ProductService;

import java.io.IOException;
import java.util.List;

/**
 * Controller for product operations
 */
@WebServlet("/product")
public class ProductController extends HttpServlet {
    private ProductService productService;
    private CategoryService categoryService;
    
    @Override
    public void init() {
        productService = new ProductService();
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
                showProductList(request, response);
                break;
            case "create":
                showCreateForm(request, response);
                break;
            case "edit":
                showEditForm(request, response);
                break;
            case "details":
                showProductDetails(request, response);
                break;
            default:
                showProductList(request, response);
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
                handleSaveProduct(request, response);
                break;
            case "update":
                handleUpdateProduct(request, response);
                break;
            case "toggleStatus":
                handleToggleStatus(request, response);
                break;
            case "delete":
                handleDeleteProduct(request, response);
                break;
            default:
                showProductList(request, response);
        }
    }
    
    /**
     * Show product list
     */
    private void showProductList(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String search = request.getParameter("search");
        String categoryId = request.getParameter("categoryId");
        String status = request.getParameter("status");
        
        List<Product> products;
        
        // Apply filters
        if (search != null && !search.trim().isEmpty()) {
            products = productService.searchProducts(search.trim());
        } else if (categoryId != null && !categoryId.isEmpty()) {
            try {
                products = productService.getProductsByCategory(Long.parseLong(categoryId));
            } catch (NumberFormatException e) {
                products = productService.getAllProducts();
            }
        } else if (status != null && !status.isEmpty()) {
            boolean isActive = "active".equalsIgnoreCase(status);
            products = productService.getProductsByStatus(isActive);
        } else {
            products = productService.getAllProducts();
        }
        
        // Add inventory quantities
        for (Product product : products) {
            int totalQty = productService.getTotalInventoryQuantity(product.getId());
            request.setAttribute("totalStock_" + product.getId(), totalQty);
        }
        
        // Get categories for filter
        List<Category> categories = categoryService.getAllCategories();
        
        request.setAttribute("products", products);
        request.setAttribute("categories", categories);
        request.setAttribute("search", search);
        request.setAttribute("selectedCategoryId", categoryId);
        request.setAttribute("selectedStatus", status);
        
        request.getRequestDispatcher("/views/product/list.jsp").forward(request, response);
    }
    
    /**
     * Show create product form
     */
    private void showCreateForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Get categories for dropdown
        List<Category> categories = categoryService.getAllCategories();
        
        if (categories.isEmpty()) {
            request.setAttribute("error", "Please create a category first");
            showProductList(request, response);
            return;
        }
        
        request.setAttribute("categories", categories);
        request.getRequestDispatcher("/views/product/form.jsp").forward(request, response);
    }
    
    /**
     * Show edit product form
     */
    private void showEditForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String idStr = request.getParameter("id");
        
        if (idStr == null || idStr.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/product");
            return;
        }
        
        try {
            Long id = Long.parseLong(idStr);
            Product product = productService.getProductById(id);
            
            if (product == null) {
                request.setAttribute("error", "Product not found");
                showProductList(request, response);
                return;
            }
            
            List<Category> categories = categoryService.getAllCategories();
            
            request.setAttribute("product", product);
            request.setAttribute("categories", categories);
            request.getRequestDispatcher("/views/product/form.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/product");
        }
    }
    
    /**
     * Show product details
     */
    private void showProductDetails(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String idStr = request.getParameter("id");
        
        if (idStr == null || idStr.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/product");
            return;
        }
        
        try {
            Long id = Long.parseLong(idStr);
            Product product = productService.getProductById(id);
            
            if (product == null) {
                request.setAttribute("error", "Product not found");
                showProductList(request, response);
                return;
            }
            
            Category category = categoryService.getCategoryById(product.getCategoryId());
            int totalInventory = productService.getTotalInventoryQuantity(id);
            
            request.setAttribute("product", product);
            request.setAttribute("category", category);
            request.setAttribute("totalInventory", totalInventory);
            request.getRequestDispatcher("/views/product/details.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/product");
        }
    }
    
    /**
     * Handle save new product
     */
    private void handleSaveProduct(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String sku = request.getParameter("sku");
        String name = request.getParameter("name");
        String unit = request.getParameter("unit");
        String categoryIdStr = request.getParameter("categoryId");
        
        // Validate input
        if (sku == null || sku.trim().isEmpty() || name == null || name.trim().isEmpty() ||
            categoryIdStr == null || categoryIdStr.isEmpty()) {
            request.setAttribute("error", "SKU, Name, and Category are required");
            showCreateForm(request, response);
            return;
        }
        
        try {
            Long categoryId = Long.parseLong(categoryIdStr);
            
            Product created = productService.createProduct(sku, name, unit, categoryId);
            
            if (created == null) {
                request.setAttribute("error", "SKU already exists or creation failed");
                request.setAttribute("sku", sku);
                request.setAttribute("name", name);
                request.setAttribute("unit", unit);
                request.setAttribute("categoryId", categoryId);
                showCreateForm(request, response);
                return;
            }
            
            request.setAttribute("success", "Product created successfully");
            showProductList(request, response);
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid category ID");
            showCreateForm(request, response);
        }
    }
    
    /**
     * Handle update product
     */
    private void handleUpdateProduct(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String idStr = request.getParameter("id");
        String sku = request.getParameter("sku");
        String name = request.getParameter("name");
        String unit = request.getParameter("unit");
        String categoryIdStr = request.getParameter("categoryId");
        
        // Validate input
        if (idStr == null || idStr.isEmpty() || sku == null || sku.trim().isEmpty() ||
            name == null || name.trim().isEmpty() || categoryIdStr == null || categoryIdStr.isEmpty()) {
            request.setAttribute("error", "All required fields must be filled");
            showProductList(request, response);
            return;
        }
        
        try {
            Long id = Long.parseLong(idStr);
            Long categoryId = Long.parseLong(categoryIdStr);
            
            boolean updated = productService.updateProduct(id, sku, name, unit, categoryId);
            
            if (!updated) {
                request.setAttribute("error", "SKU already exists or update failed");
                Product product = productService.getProductById(id);
                request.setAttribute("product", product);
                List<Category> categories = categoryService.getAllCategories();
                request.setAttribute("categories", categories);
                showEditForm(request, response);
                return;
            }
            
            request.setAttribute("success", "Product updated successfully");
            showProductList(request, response);
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid ID or category ID");
            showProductList(request, response);
        }
    }
    
    /**
     * Handle toggle product status
     */
    private void handleToggleStatus(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String idStr = request.getParameter("id");
        
        if (idStr == null || idStr.isEmpty()) {
            request.setAttribute("error", "Product ID is required");
            showProductList(request, response);
            return;
        }
        
        try {
            Long id = Long.parseLong(idStr);
            Product product = productService.getProductById(id);
            
            if (product == null) {
                request.setAttribute("error", "Product not found");
                showProductList(request, response);
                return;
            }
            
            // Check for pending orders if deactivating
            if (product.isActive()) {
                int pendingOrders = productService.getPendingOrderCount(id);
                if (pendingOrders > 0) {
                    request.setAttribute("warning", 
                        "This product has " + pendingOrders + " pending orders. Deactivating will prevent new orders only.");
                }
            }
            
            boolean updated = productService.toggleProductStatus(id);
            
            if (!updated) {
                request.setAttribute("error", "Failed to toggle product status");
                showProductList(request, response);
                return;
            }
            
            String newStatus = product.isActive() ? "deactivated" : "activated";
            request.setAttribute("success", "Product " + newStatus + " successfully");
            showProductList(request, response);
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid product ID");
            showProductList(request, response);
        }
    }
    
    /**
     * Handle delete product
     */
    private void handleDeleteProduct(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String idStr = request.getParameter("id");
        
        if (idStr == null || idStr.isEmpty()) {
            request.setAttribute("error", "Product ID is required");
            showProductList(request, response);
            return;
        }
        
        try {
            Long id = Long.parseLong(idStr);
            Product product = productService.getProductById(id);
            
            if (product == null) {
                request.setAttribute("error", "Product not found");
                showProductList(request, response);
                return;
            }
            
            boolean deleted = productService.deleteProduct(id);
            
            if (!deleted) {
                request.setAttribute("error", "Failed to delete product");
                showProductList(request, response);
                return;
            }
            
            request.setAttribute("success", "Product deleted successfully");
            showProductList(request, response);
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid product ID");
            showProductList(request, response);
        }
    }
}
