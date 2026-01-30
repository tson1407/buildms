package vn.edu.fpt.swp.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.edu.fpt.swp.model.User;
import vn.edu.fpt.swp.service.AuthService;

import java.io.IOException;

/**
 * Controller for authentication operations
 */
@WebServlet("/auth")
public class AuthController extends HttpServlet {
    private AuthService authService;
    
    @Override
    public void init() {
        authService = new AuthService();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String action = request.getParameter("action");
        
        if (action == null) {
            action = "login";
        }
        
        switch (action) {
            case "login":
                showLoginForm(request, response);
                break;
            case "register":
                showRegisterForm(request, response);
                break;
            case "change-password":
                showChangePasswordForm(request, response);
                break;
            case "logout":
                logout(request, response);
                break;
            default:
                showLoginForm(request, response);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String action = request.getParameter("action");
        
        if (action == null) {
            action = "login";
        }
        
        switch (action) {
            case "login":
                handleLogin(request, response);
                break;
            case "register":
                handleRegister(request, response);
                break;
            case "change-password":
                handleChangePassword(request, response);
                break;
            default:
                showLoginForm(request, response);
        }
    }
    
    /**
     * Show login form
     */
    private void showLoginForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.getRequestDispatcher("/views/auth/login.jsp").forward(request, response);
    }
    
    /**
     * Handle login request
     */
    private void handleLogin(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        // Validate input fields
        if (username == null || username.trim().isEmpty() || 
            password == null || password.trim().isEmpty()) {
            request.setAttribute("error", "Username and password are required");
            request.setAttribute("username", username);
            request.getRequestDispatcher("/views/auth/login.jsp").forward(request, response);
            return;
        }
        
        // Authenticate user
        User user = authService.authenticate(username, password);
        
        if (user == null) {
            // Check if it's because user is inactive
            // For security, we use same error message for all auth failures
            request.setAttribute("error", "Invalid username or password");
            request.setAttribute("username", username);
            request.getRequestDispatcher("/views/auth/login.jsp").forward(request, response);
            return;
        }
        
        // Create session
        HttpSession session = request.getSession(true);
        session.setAttribute("user", user);
        session.setAttribute("userId", user.getId());
        session.setAttribute("username", user.getUsername());
        session.setAttribute("role", user.getRole());
        session.setMaxInactiveInterval(30 * 60); // 30 minutes
        
        // Redirect to dashboard
        request.setAttribute("success", "Login successful");
        response.sendRedirect(request.getContextPath() + "/dashboard");
    }
    
    /**
     * Show register form
     */
    private void showRegisterForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Check if user is admin
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/auth?action=login");
            return;
        }
        
        User currentUser = (User) session.getAttribute("user");
        if (!"Admin".equals(currentUser.getRole())) {
            response.sendRedirect(request.getContextPath() + "/error/403.jsp");
            return;
        }
        
        request.getRequestDispatcher("/views/auth/register.jsp").forward(request, response);
    }
    
    /**
     * Handle register request
     */
    private void handleRegister(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Check if user is admin
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/auth?action=login");
            return;
        }
        
        User currentUser = (User) session.getAttribute("user");
        if (!"Admin".equals(currentUser.getRole())) {
            response.sendRedirect(request.getContextPath() + "/error/403.jsp");
            return;
        }
        
        // Get form data
        String username = request.getParameter("username");
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String role = request.getParameter("role");
        
        // Validate input
        if (username == null || username.trim().isEmpty() || username.length() < 3 || username.length() > 50) {
            request.setAttribute("error", "Username is required and must be 3-50 alphanumeric characters");
            retainRegisterFormData(request, username, name, email, role);
            request.getRequestDispatcher("/views/auth/register.jsp").forward(request, response);
            return;
        }
        
        if (email == null || email.trim().isEmpty() || !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            request.setAttribute("error", "Please enter a valid email address");
            retainRegisterFormData(request, username, name, email, role);
            request.getRequestDispatcher("/views/auth/register.jsp").forward(request, response);
            return;
        }
        
        if (password == null || password.length() < 6) {
            request.setAttribute("error", "Password must be at least 6 characters");
            retainRegisterFormData(request, username, name, email, role);
            request.getRequestDispatcher("/views/auth/register.jsp").forward(request, response);
            return;
        }
        
        if (!password.equals(confirmPassword)) {
            request.setAttribute("error", "Passwords do not match");
            retainRegisterFormData(request, username, name, email, role);
            request.getRequestDispatcher("/views/auth/register.jsp").forward(request, response);
            return;
        }
        
        if (role == null || role.trim().isEmpty()) {
            request.setAttribute("error", "Please select a valid role");
            retainRegisterFormData(request, username, name, email, role);
            request.getRequestDispatcher("/views/auth/register.jsp").forward(request, response);
            return;
        }
        
        // Create user object
        User newUser = new User();
        newUser.setUsername(username.trim());
        newUser.setName(name != null ? name.trim() : username.trim());
        newUser.setEmail(email.trim());
        newUser.setRole(role);
        newUser.setStatus("Active");
        
        // Register user
        User createdUser = authService.registerUser(newUser, password);
        
        if (createdUser == null) {
            // Check specific error (username or email exists)
            request.setAttribute("error", "Username or email is already registered");
            retainRegisterFormData(request, username, name, email, role);
            request.getRequestDispatcher("/views/auth/register.jsp").forward(request, response);
            return;
        }
        
        // Success
        request.setAttribute("success", "User " + username + " created successfully with role " + role);
        request.getRequestDispatcher("/views/auth/register.jsp").forward(request, response);
    }
    
    /**
     * Show change password form
     */
    private void showChangePasswordForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Check if user is logged in
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/auth?action=login");
            return;
        }
        
        request.getRequestDispatcher("/views/auth/change-password.jsp").forward(request, response);
    }
    
    /**
     * Handle change password request
     */
    private void handleChangePassword(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Check if user is logged in
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/auth?action=login");
            return;
        }
        
        User currentUser = (User) session.getAttribute("user");
        
        // Get form data
        String currentPassword = request.getParameter("currentPassword");
        String newPassword = request.getParameter("newPassword");
        String confirmNewPassword = request.getParameter("confirmNewPassword");
        
        // Validate input
        if (currentPassword == null || currentPassword.trim().isEmpty()) {
            request.setAttribute("error", "Current password is required");
            request.getRequestDispatcher("/views/auth/change-password.jsp").forward(request, response);
            return;
        }
        
        if (newPassword == null || newPassword.length() < 6) {
            request.setAttribute("error", "New password must be at least 6 characters");
            request.getRequestDispatcher("/views/auth/change-password.jsp").forward(request, response);
            return;
        }
        
        if (!newPassword.equals(confirmNewPassword)) {
            request.setAttribute("error", "New passwords do not match");
            request.getRequestDispatcher("/views/auth/change-password.jsp").forward(request, response);
            return;
        }
        
        if (currentPassword.equals(newPassword)) {
            request.setAttribute("error", "New password must be different from current password");
            request.getRequestDispatcher("/views/auth/change-password.jsp").forward(request, response);
            return;
        }
        
        // Change password
        boolean success = authService.changePassword(currentUser.getId(), currentPassword, newPassword);
        
        if (!success) {
            request.setAttribute("error", "Current password is incorrect");
            request.getRequestDispatcher("/views/auth/change-password.jsp").forward(request, response);
            return;
        }
        
        // Success
        request.setAttribute("success", "Password changed successfully");
        request.getRequestDispatcher("/views/auth/change-password.jsp").forward(request, response);
    }
    
    /**
     * Handle logout
     */
    private void logout(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        
        request.setAttribute("success", "You have been logged out successfully");
        response.sendRedirect(request.getContextPath() + "/auth?action=login");
    }
    
    /**
     * Retain form data for register form
     */
    private void retainRegisterFormData(HttpServletRequest request, String username, 
                                       String name, String email, String role) {
        request.setAttribute("username", username);
        request.setAttribute("name", name);
        request.setAttribute("email", email);
        request.setAttribute("role", role);
    }
}
