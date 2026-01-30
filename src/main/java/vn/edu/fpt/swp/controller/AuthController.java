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
 * Controller for authentication operations (login, logout, register)
 */
@WebServlet("/auth")
public class AuthController extends HttpServlet {
    private static final long serialVersionUID = 1L;
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
                showLoginPage(request, response);
                break;
            case "register":
                showRegisterPage(request, response);
                break;
            case "logout":
                logout(request, response);
                break;
            default:
                showLoginPage(request, response);
                break;
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
            default:
                showLoginPage(request, response);
                break;
        }
    }
    
    /**
     * Show login page
     */
    private void showLoginPage(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.setAttribute("pageTitle", "Login");
        request.getRequestDispatcher("/views/auth/login.jsp").forward(request, response);
    }
    
    /**
     * Show registration page
     */
    private void showRegisterPage(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.setAttribute("pageTitle", "Register");
        request.getRequestDispatcher("/views/auth/register.jsp").forward(request, response);
    }
    
    /**
     * Handle login request
     */
    private void handleLogin(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        // Validate input
        if (username == null || username.trim().isEmpty() ||
            password == null || password.trim().isEmpty()) {
            request.setAttribute("error", "Username and password are required");
            showLoginPage(request, response);
            return;
        }
        
        // Authenticate user
        User user = authService.authenticate(username, password);
        
        if (user != null) {
            // Create session
            HttpSession session = request.getSession(true);
            session.setAttribute("user", user);
            session.setAttribute("userId", user.getId());
            session.setAttribute("username", user.getUsername());
            session.setAttribute("userRole", user.getRole());
            
            // Set session timeout (30 minutes)
            session.setMaxInactiveInterval(30 * 60);
            
            // Redirect to appropriate page based on role
            String redirectUrl = getRedirectUrlByRole(user.getRole());
            response.sendRedirect(request.getContextPath() + redirectUrl);
        } else {
            request.setAttribute("error", "Invalid username or password");
            request.setAttribute("username", username);
            showLoginPage(request, response);
        }
    }
    
    /**
     * Handle registration request
     */
    private void handleRegister(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String role = request.getParameter("role");
        
        // Validate input
        if (username == null || username.trim().isEmpty() ||
            password == null || password.trim().isEmpty() ||
            name == null || name.trim().isEmpty() ||
            email == null || email.trim().isEmpty() ||
            role == null || role.trim().isEmpty()) {
            request.setAttribute("error", "All fields are required");
            request.setAttribute("username", username);
            request.setAttribute("name", name);
            request.setAttribute("email", email);
            request.setAttribute("role", role);
            showRegisterPage(request, response);
            return;
        }
        
        // Validate password confirmation
        if (!password.equals(confirmPassword)) {
            request.setAttribute("error", "Passwords do not match");
            request.setAttribute("username", username);
            request.setAttribute("name", name);
            request.setAttribute("email", email);
            request.setAttribute("role", role);
            showRegisterPage(request, response);
            return;
        }
        
        // Validate password strength
        if (password.length() < 6) {
            request.setAttribute("error", "Password must be at least 6 characters long");
            request.setAttribute("username", username);
            request.setAttribute("name", name);
            request.setAttribute("email", email);
            request.setAttribute("role", role);
            showRegisterPage(request, response);
            return;
        }
        
        // Register user
        User user = authService.register(username, password, name, email, role);
        
        if (user != null) {
            response.sendRedirect(request.getContextPath() + "/auth?action=login&success=registered");
        } else {
            request.setAttribute("error", "Registration failed. Username or email may already exist");
            request.setAttribute("username", username);
            request.setAttribute("name", name);
            request.setAttribute("email", email);
            request.setAttribute("role", role);
            showRegisterPage(request, response);
        }
    }
    
    /**
     * Handle logout request
     */
    private void logout(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        response.sendRedirect(request.getContextPath() + "/auth?action=login&success=logout");
    }
    
    /**
     * Get redirect URL based on user role
     * @param role User role
     * @return Redirect URL
     */
    private String getRedirectUrlByRole(String role) {
        switch (role) {
            case "Admin":
                return "/admin/dashboard";
            case "Manager":
                return "/manager/dashboard";
            case "Staff":
                return "/staff/dashboard";
            case "Sales":
                return "/sales/dashboard";
            default:
                return "/";
        }
    }
}
