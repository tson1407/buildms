package vn.edu.fpt.swp.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.edu.fpt.swp.model.User;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Filter for authentication and authorization
 * Checks if user is logged in and has required permissions for requested resources
 */
@WebFilter("/*")
public class AuthFilter implements Filter {
    
    // Public URLs that don't require authentication
    private static final List<String> PUBLIC_URLS = Arrays.asList(
        "/auth",
        "/assets",
        "/css",
        "/js",
        "/libs",
        "/fonts",
        "/favicon.ico"
    );
    
    // Role-based access control map
    private static final Map<String, List<String>> ROLE_ACCESS_MAP = new HashMap<>();
    
    static {
        // Admin has access to all resources
        ROLE_ACCESS_MAP.put("/admin", Arrays.asList("Admin"));
        
        // Manager has access to manager, warehouse, and product resources
        ROLE_ACCESS_MAP.put("/manager", Arrays.asList("Admin", "Manager"));
        ROLE_ACCESS_MAP.put("/warehouse", Arrays.asList("Admin", "Manager", "Staff"));
        ROLE_ACCESS_MAP.put("/product", Arrays.asList("Admin", "Manager", "Staff"));
        ROLE_ACCESS_MAP.put("/inventory", Arrays.asList("Admin", "Manager", "Staff"));
        ROLE_ACCESS_MAP.put("/request", Arrays.asList("Admin", "Manager", "Staff"));
        
        // Staff has access to warehouse operations
        ROLE_ACCESS_MAP.put("/staff", Arrays.asList("Admin", "Manager", "Staff"));
        
        // Sales has access to sales resources
        ROLE_ACCESS_MAP.put("/sales", Arrays.asList("Admin", "Sales", "Manager"));
        ROLE_ACCESS_MAP.put("/salesorder", Arrays.asList("Admin", "Sales", "Manager"));
        ROLE_ACCESS_MAP.put("/customer", Arrays.asList("Admin", "Sales", "Manager"));
    }
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialization logic if needed
    }
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        String requestURI = httpRequest.getRequestURI();
        String contextPath = httpRequest.getContextPath();
        String path = requestURI.substring(contextPath.length());
        
        // Allow public URLs
        if (isPublicUrl(path)) {
            chain.doFilter(request, response);
            return;
        }
        
        // Check if user is authenticated
        HttpSession session = httpRequest.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;
        
        if (user == null) {
            // User not authenticated, redirect to login
            // Check if session existed but expired
            if (session != null) {
                session.invalidate();
                httpResponse.sendRedirect(contextPath + "/auth?action=login&expired=true&redirect=" + 
                                         java.net.URLEncoder.encode(path, "UTF-8"));
            } else {
                httpResponse.sendRedirect(contextPath + "/auth?action=login&redirect=" + 
                                         java.net.URLEncoder.encode(path, "UTF-8"));
            }
            return;
        }
        
        // Check if user has required role for the resource
        if (!hasAccess(path, user.getRole())) {
            // User doesn't have permission, show error
            httpRequest.setAttribute("error", "You don't have permission to access this resource");
            httpRequest.getRequestDispatcher("/views/error/403.jsp").forward(httpRequest, httpResponse);
            return;
        }
        
        // User is authenticated and authorized, continue
        chain.doFilter(request, response);
    }
    
    @Override
    public void destroy() {
        // Cleanup logic if needed
    }
    
    /**
     * Check if URL is public (doesn't require authentication)
     * @param path Request path
     * @return true if public, false otherwise
     */
    private boolean isPublicUrl(String path) {
        if (path.equals("/") || path.equals("/index.jsp")) {
            return true;
        }
        
        for (String publicUrl : PUBLIC_URLS) {
            if (path.startsWith(publicUrl)) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Check if user role has access to the requested resource
     * @param path Request path
     * @param userRole User's role
     * @return true if has access, false otherwise
     */
    private boolean hasAccess(String path, String userRole) {
        // Extract the base path (e.g., /product from /product/list)
        String basePath = extractBasePath(path);
        
        // Get allowed roles for this path
        List<String> allowedRoles = ROLE_ACCESS_MAP.get(basePath);
        
        // If no specific rule, allow access (default open)
        if (allowedRoles == null) {
            return true;
        }
        
        // Check if user's role is in allowed roles
        return allowedRoles.contains(userRole);
    }
    
    /**
     * Extract base path from full path
     * @param path Full request path
     * @return Base path
     */
    private String extractBasePath(String path) {
        if (path.startsWith("/")) {
            int secondSlash = path.indexOf("/", 1);
            if (secondSlash > 0) {
                return path.substring(0, secondSlash);
            }
        }
        return path;
    }
}
