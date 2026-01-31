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
 * Authentication and Authorization Filter
 * Handles session validation, timeout, and role-based access control
 */
@WebFilter("/*")
public class AuthFilter implements Filter {
    
    // Public URLs that bypass authentication
    private static final List<String> PUBLIC_PATHS = Arrays.asList(
        "/auth",
        "/assets",
        "/css",
        "/js",
        "/libs",
        "/fonts",
        "/dist",
        "/error"
    );
    
    // Role-based access control map
    private static final Map<String, List<String>> ROLE_ACCESS_MAP = new HashMap<>();
    
    static {
        // Admin has access to everything
        ROLE_ACCESS_MAP.put("/user", Arrays.asList("Admin"));
        ROLE_ACCESS_MAP.put("/category", Arrays.asList("Admin", "Manager"));
        ROLE_ACCESS_MAP.put("/product", Arrays.asList("Admin", "Manager"));
        ROLE_ACCESS_MAP.put("/customer", Arrays.asList("Admin", "Manager", "Sales"));
        ROLE_ACCESS_MAP.put("/warehouse", Arrays.asList("Admin", "Manager"));
        ROLE_ACCESS_MAP.put("/location", Arrays.asList("Admin", "Manager", "Staff"));
        ROLE_ACCESS_MAP.put("/inventory", Arrays.asList("Admin", "Manager", "Staff"));
        ROLE_ACCESS_MAP.put("/request", Arrays.asList("Admin", "Manager", "Staff"));
        ROLE_ACCESS_MAP.put("/sales-order", Arrays.asList("Admin", "Manager", "Sales"));
    }
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialization logic if needed
    }
    
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) 
            throws IOException, ServletException {
        
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        
        String path = request.getRequestURI().substring(request.getContextPath().length());
        
        // Allow public paths
        if (isPublicPath(path)) {
            chain.doFilter(request, response);
            return;
        }
        
        // Check session
        HttpSession session = request.getSession(false);
        
        // No session or no user in session
        if (session == null || session.getAttribute("user") == null) {
            // Check if it's an AJAX request
            if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("{\"error\": \"Session expired\"}");
                return;
            }
            
            // Redirect to login
            response.sendRedirect(request.getContextPath() + "/auth?action=login");
            return;
        }
        
        // Session exists, check if expired (UC-AUTH-006)
        Long lastAccessTime = (Long) session.getAttribute("lastAccessTime");
        long currentTime = System.currentTimeMillis();
        
        if (lastAccessTime != null) {
            long elapsedTime = (currentTime - lastAccessTime) / 1000; // in seconds
            
            // 30 minutes = 1800 seconds
            if (elapsedTime > 1800) {
                // Session expired
                session.invalidate();
                
                if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write("{\"error\": \"Your session has expired. Please login again.\"}");
                    return;
                }
                
                request.setAttribute("error", "Your session has expired. Please login again.");
                request.getRequestDispatcher("/views/auth/login.jsp").forward(request, response);
                return;
            }
        }
        
        // Update last access time
        session.setAttribute("lastAccessTime", currentTime);
        
        // Check role-based access control
        User user = (User) session.getAttribute("user");
        String userRole = user.getRole();
        
        if (!hasAccess(path, userRole)) {
            response.sendRedirect(request.getContextPath() + "/views/error/403.jsp");
            return;
        }
        
        // Continue with the request
        chain.doFilter(request, response);
    }
    
    @Override
    public void destroy() {
        // Cleanup logic if needed
    }
    
    /**
     * Check if path is public (no authentication required)
     */
    private boolean isPublicPath(String path) {
        // Root path
        if (path.equals("/") || path.equals("/index.jsp")) {
            return false;
        }
        
        // Check if path starts with any public path
        for (String publicPath : PUBLIC_PATHS) {
            if (path.startsWith(publicPath)) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Check if user role has access to path
     */
    private boolean hasAccess(String path, String role) {
        // Admin has access to everything
        if ("Admin".equals(role)) {
            return true;
        }
        
        // Check role-based access for specific paths
        for (Map.Entry<String, List<String>> entry : ROLE_ACCESS_MAP.entrySet()) {
            if (path.startsWith(entry.getKey())) {
                return entry.getValue().contains(role);
            }
        }
        
        // Default: allow access (for dashboard, profile, etc.)
        return true;
    }
}
