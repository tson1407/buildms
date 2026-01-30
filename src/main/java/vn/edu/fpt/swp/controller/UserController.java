package vn.edu.fpt.swp.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.edu.fpt.swp.dao.UserDAO;
import vn.edu.fpt.swp.model.User;
import vn.edu.fpt.swp.service.AuthService;

import java.io.IOException;
import java.util.List;

/**
 * Controller for user management operations (Admin only)
 */
@WebServlet("/user")
public class UserController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserDAO userDAO;
    private AuthService authService;
    
    @Override
    public void init() {
        userDAO = new UserDAO();
        authService = new AuthService();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }
        
        // Check if user is admin
        if (!isAdmin(request)) {
            response.sendRedirect(request.getContextPath() + "/auth?action=login");
            return;
        }
        
        switch (action) {
            case "list":
                listUsers(request, response);
                break;
            case "view":
                viewUser(request, response);
                break;
            case "create":
                showCreateForm(request, response);
                break;
            case "edit":
                showEditForm(request, response);
                break;
            case "delete":
                deleteUser(request, response);
                break;
            default:
                listUsers(request, response);
                break;
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String action = request.getParameter("action");
        
        // Check if user is admin
        if (!isAdmin(request)) {
            response.sendRedirect(request.getContextPath() + "/auth?action=login");
            return;
        }
        
        switch (action) {
            case "create":
                createUser(request, response);
                break;
            case "update":
                updateUser(request, response);
                break;
            case "resetPassword":
                resetPassword(request, response);
                break;
            default:
                listUsers(request, response);
                break;
        }
    }
    
    /**
     * List all users
     */
    private void listUsers(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        List<User> users = userDAO.findAll();
        request.setAttribute("users", users);
        request.setAttribute("pageTitle", "User Management");
        request.getRequestDispatcher("/views/user/list.jsp").forward(request, response);
    }
    
    /**
     * View user details
     */
    private void viewUser(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        Long id = Long.parseLong(request.getParameter("id"));
        User user = userDAO.findById(id);
        
        if (user != null) {
            request.setAttribute("user", user);
            request.setAttribute("pageTitle", "User Details");
            request.getRequestDispatcher("/views/user/view.jsp").forward(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/user?error=notfound");
        }
    }
    
    /**
     * Show create user form
     */
    private void showCreateForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.setAttribute("pageTitle", "Create User");
        request.getRequestDispatcher("/views/user/create.jsp").forward(request, response);
    }
    
    /**
     * Show edit user form
     */
    private void showEditForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        Long id = Long.parseLong(request.getParameter("id"));
        User user = userDAO.findById(id);
        
        if (user != null) {
            request.setAttribute("user", user);
            request.setAttribute("pageTitle", "Edit User");
            request.getRequestDispatcher("/views/user/edit.jsp").forward(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/user?error=notfound");
        }
    }
    
    /**
     * Create new user
     */
    private void createUser(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String role = request.getParameter("role");
        
        User user = authService.register(username, password, name, email, role);
        
        if (user != null) {
            response.sendRedirect(request.getContextPath() + "/user?success=created");
        } else {
            request.setAttribute("error", "Failed to create user. Username or email may already exist.");
            request.setAttribute("username", username);
            request.setAttribute("name", name);
            request.setAttribute("email", email);
            request.setAttribute("role", role);
            showCreateForm(request, response);
        }
    }
    
    /**
     * Update user
     */
    private void updateUser(HttpServletRequest request, HttpServletResponse response) 
            throws IOException, ServletException {
        Long id = Long.parseLong(request.getParameter("id"));
        String username = request.getParameter("username");
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String role = request.getParameter("role");
        String status = request.getParameter("status");
        
        User user = userDAO.findById(id);
        if (user != null) {
            user.setUsername(username);
            user.setName(name);
            user.setEmail(email);
            user.setRole(role);
            user.setStatus(status);
            
            if (userDAO.update(user)) {
                response.sendRedirect(request.getContextPath() + "/user?success=updated");
            } else {
                request.setAttribute("error", "Failed to update user");
                request.setAttribute("user", user);
                showEditForm(request, response);
            }
        } else {
            response.sendRedirect(request.getContextPath() + "/user?error=notfound");
        }
    }
    
    /**
     * Reset user password
     */
    private void resetPassword(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        Long id = Long.parseLong(request.getParameter("id"));
        String newPassword = request.getParameter("newPassword");
        
        if (authService.resetPassword(id, newPassword)) {
            response.sendRedirect(request.getContextPath() + "/user?success=passwordreset");
        } else {
            response.sendRedirect(request.getContextPath() + "/user?error=passwordreset");
        }
    }
    
    /**
     * Delete user (soft delete)
     */
    private void deleteUser(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        Long id = Long.parseLong(request.getParameter("id"));
        
        if (userDAO.delete(id)) {
            response.sendRedirect(request.getContextPath() + "/user?success=deleted");
        } else {
            response.sendRedirect(request.getContextPath() + "/user?error=delete");
        }
    }
    
    /**
     * Check if current user is admin
     */
    private boolean isAdmin(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            User user = (User) session.getAttribute("user");
            return user != null && "Admin".equals(user.getRole());
        }
        return false;
    }
}
