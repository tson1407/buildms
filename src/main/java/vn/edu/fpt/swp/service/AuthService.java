package vn.edu.fpt.swp.service;

import vn.edu.fpt.swp.dao.UserDAO;
import vn.edu.fpt.swp.model.User;
import vn.edu.fpt.swp.util.PasswordUtil;

/**
 * Service class for authentication operations
 */
public class AuthService {
    private final UserDAO userDAO;
    
    public AuthService() {
        this.userDAO = new UserDAO();
    }
    
    /**
     * Authenticate user with username and password
     * @param username Username
     * @param password Plain text password
     * @return User object if authentication successful, null otherwise
     */
    public User authenticate(String username, String password) {
        // Validate input
        if (username == null || username.trim().isEmpty() ||
            password == null || password.trim().isEmpty()) {
            return null;
        }
        
        // Find user by username
        User user = userDAO.findByUsername(username.trim());
        
        // Check if user exists and is active
        if (user == null || !"Active".equals(user.getStatus())) {
            return null;
        }
        
        // Verify password
        if (PasswordUtil.verifyPassword(password, user.getPasswordHash())) {
            // Update last login
            userDAO.updateLastLogin(user.getId());
            return user;
        }
        
        return null;
    }
    
    /**
     * Register a new user
     * @param username Username
     * @param password Plain text password
     * @param name Full name
     * @param email Email address
     * @param role User role
     * @return User object if registration successful, null otherwise
     */
    public User register(String username, String password, String name, String email, String role) {
        // Validate input
        if (username == null || username.trim().isEmpty() ||
            password == null || password.trim().isEmpty() ||
            name == null || name.trim().isEmpty() ||
            email == null || email.trim().isEmpty() ||
            role == null || role.trim().isEmpty()) {
            return null;
        }
        
        // Check if username already exists
        if (userDAO.usernameExists(username.trim())) {
            return null;
        }
        
        // Check if email already exists
        if (userDAO.emailExists(email.trim())) {
            return null;
        }
        
        // Validate role
        if (!isValidRole(role)) {
            return null;
        }
        
        // Create new user
        User user = new User(username.trim(), name.trim(), email.trim(), role.trim());
        user.setPasswordHash(PasswordUtil.hashPassword(password));
        
        // Save to database
        if (userDAO.create(user)) {
            return user;
        }
        
        return null;
    }
    
    /**
     * Change user password
     * @param userId User ID
     * @param oldPassword Current password
     * @param newPassword New password
     * @return true if successful, false otherwise
     */
    public boolean changePassword(Long userId, String oldPassword, String newPassword) {
        // Validate input
        if (userId == null || oldPassword == null || oldPassword.trim().isEmpty() ||
            newPassword == null || newPassword.trim().isEmpty()) {
            return false;
        }
        
        // Get user
        User user = userDAO.findById(userId);
        if (user == null) {
            return false;
        }
        
        // Verify old password
        if (!PasswordUtil.verifyPassword(oldPassword, user.getPasswordHash())) {
            return false;
        }
        
        // Hash new password
        String newPasswordHash = PasswordUtil.hashPassword(newPassword);
        
        // Update password
        return userDAO.updatePassword(userId, newPasswordHash);
    }
    
    /**
     * Reset user password (admin function)
     * @param userId User ID
     * @param newPassword New password
     * @return true if successful, false otherwise
     */
    public boolean resetPassword(Long userId, String newPassword) {
        // Validate input
        if (userId == null || newPassword == null || newPassword.trim().isEmpty()) {
            return false;
        }
        
        // Hash new password
        String newPasswordHash = PasswordUtil.hashPassword(newPassword);
        
        // Update password
        return userDAO.updatePassword(userId, newPasswordHash);
    }
    
    /**
     * Check if user has required role
     * @param user User object
     * @param requiredRole Required role
     * @return true if user has required role, false otherwise
     */
    public boolean hasRole(User user, String requiredRole) {
        if (user == null || requiredRole == null) {
            return false;
        }
        return requiredRole.equals(user.getRole());
    }
    
    /**
     * Check if user has any of the required roles
     * @param user User object
     * @param requiredRoles Array of required roles
     * @return true if user has any of the required roles, false otherwise
     */
    public boolean hasAnyRole(User user, String... requiredRoles) {
        if (user == null || requiredRoles == null || requiredRoles.length == 0) {
            return false;
        }
        
        for (String role : requiredRoles) {
            if (role.equals(user.getRole())) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Validate role string
     * @param role Role string to validate
     * @return true if valid, false otherwise
     */
    private boolean isValidRole(String role) {
        return "Admin".equals(role) || "Manager".equals(role) || 
               "Staff".equals(role) || "Sales".equals(role);
    }
}
