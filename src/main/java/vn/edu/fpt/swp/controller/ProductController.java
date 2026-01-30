package vn.edu.fpt.swp.controller;

import vn.edu.fpt.swp.model.Product;
import vn.edu.fpt.swp.model.Category;
import vn.edu.fpt.swp.service.ProductService;
import vn.edu.fpt.swp.service.CategoryService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Controller servlet for handling Product requests
 */
@WebServlet("/product")
public class ProductController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ProductService productService;
    private CategoryService categoryService;
    
    @Override
    public void init() throws ServletException {
        super.init();
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
                listProducts(request, response);
                break;
            case "view":
                viewProduct(request, response);
                break;
            case "create":
                showCreateForm(request, response);
                break;
            case "edit":
                showEditForm(request, response);
                break;
            case "delete":
                deleteProduct(request, response);
                break;
            case "search":
                searchProducts(request, response);
                break;
            default:
                listProducts(request, response);
                break;
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        
        if ("create".equals(action)) {
            createProduct(request, response);
        } else if ("update".equals(action)) {
            updateProduct(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/product");
        }
    }
    
    /**
     * List all products
     */
    private void listProducts(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        List<Product> products = productService.getAllProducts();
        request.setAttribute("products", products);
        request.getRequestDispatcher("/views/product/list.jsp").forward(request, response);
    }
    
    /**
     * View a single product
     */
    private void viewProduct(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            Long id = Long.parseLong(request.getParameter("id"));
            Product product = productService.getProductById(id);
            
            if (product != null) {
                request.setAttribute("product", product);
                request.getRequestDispatcher("/views/product/view.jsp").forward(request, response);
            } else {
                response.sendRedirect(request.getContextPath() + "/product?error=notfound");
            }
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/product?error=invalid");
        }
    }
    
    /**
     * Show create form
     */
    private void showCreateForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Load categories for the dropdown
        List<Category> categories = categoryService.getAllCategories();
        request.setAttribute("categories", categories);
        request.getRequestDispatcher("/views/product/create.jsp").forward(request, response);
    }
    
    /**
     * Show edit form
     */
    private void showEditForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            Long id = Long.parseLong(request.getParameter("id"));
            Product product = productService.getProductById(id);
            
            if (product != null) {
                // Load categories for the dropdown
                List<Category> categories = categoryService.getAllCategories();
                request.setAttribute("categories", categories);
                request.setAttribute("product", product);
                request.getRequestDispatcher("/views/product/edit.jsp").forward(request, response);
            } else {
                response.sendRedirect(request.getContextPath() + "/product?error=notfound");
            }
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/product?error=invalid");
        }
    }
    
    /**
     * Create a new product
     */
    private void createProduct(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            Product product = new Product();
            product.setSku(request.getParameter("sku"));
            product.setName(request.getParameter("name"));
            product.setUnit(request.getParameter("unit"));
            product.setCategoryId(Long.parseLong(request.getParameter("categoryId")));
            
            boolean success = productService.createProduct(product);
            
            if (success) {
                response.sendRedirect(request.getContextPath() + "/product?success=created");
            } else {
                request.setAttribute("error", "Failed to create product. SKU might already exist.");
                request.setAttribute("product", product);
                List<Category> categories = categoryService.getAllCategories();
                request.setAttribute("categories", categories);
                request.getRequestDispatcher("/views/product/create.jsp").forward(request, response);
            }
        } catch (Exception e) {
            request.setAttribute("error", "Invalid input: " + e.getMessage());
            List<Category> categories = categoryService.getAllCategories();
            request.setAttribute("categories", categories);
            request.getRequestDispatcher("/views/product/create.jsp").forward(request, response);
        }
    }
    
    /**
     * Update an existing product
     */
    private void updateProduct(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            Product product = new Product();
            product.setId(Long.parseLong(request.getParameter("id")));
            product.setSku(request.getParameter("sku"));
            product.setName(request.getParameter("name"));
            product.setUnit(request.getParameter("unit"));
            product.setCategoryId(Long.parseLong(request.getParameter("categoryId")));
            product.setActive("true".equals(request.getParameter("active")));
            
            boolean success = productService.updateProduct(product);
            
            if (success) {
                response.sendRedirect(request.getContextPath() + "/product?success=updated");
            } else {
                request.setAttribute("error", "Failed to update product. SKU might already exist.");
                request.setAttribute("product", product);
                List<Category> categories = categoryService.getAllCategories();
                request.setAttribute("categories", categories);
                request.getRequestDispatcher("/views/product/edit.jsp").forward(request, response);
            }
        } catch (Exception e) {
            request.setAttribute("error", "Invalid input: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/product?error=update");
        }
    }
    
    /**
     * Delete a product
     */
    private void deleteProduct(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            Long id = Long.parseLong(request.getParameter("id"));
            boolean success = productService.deleteProduct(id);
            
            if (success) {
                response.sendRedirect(request.getContextPath() + "/product?success=deleted");
            } else {
                response.sendRedirect(request.getContextPath() + "/product?error=delete");
            }
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/product?error=invalid");
        }
    }
    
    /**
     * Search products
     */
    private void searchProducts(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String keyword = request.getParameter("keyword");
        List<Product> products = productService.searchProducts(keyword);
        request.setAttribute("products", products);
        request.setAttribute("keyword", keyword);
        request.getRequestDispatcher("/views/product/list.jsp").forward(request, response);
    }
}
