# UC-AUTH Quick Reference Guide

## For Developers Using the Authentication Module

### 1. Checking if User is Logged In (in JSP)

```jsp
<%@ page import="vn.edu.fpt.swp.model.User" %>
<%
    HttpSession userSession = request.getSession(false);
    if (userSession == null || userSession.getAttribute("user") == null) {
        response.sendRedirect(request.getContextPath() + "/auth?action=login");
        return;
    }
    
    User currentUser = (User) userSession.getAttribute("user");
%>
```

### 2. Checking User Role

```jsp
<% 
    User user = (User) session.getAttribute("user");
    String role = user.getRole(); // Admin, Manager, Staff, Sales
    
    if ("Admin".equals(role)) {
        // Admin-only code
    }
%>
```

### 3. Getting User Information

```jsp
<%
    User user = (User) session.getAttribute("user");
%>

User ID: <%= user.getId() %>
Username: <%= user.getUsername() %>
Name: <%= user.getName() %>
Email: <%= user.getEmail() %>
Role: <%= user.getRole() %>
```

### 4. Creating Protected Servlets

```java
@WebServlet("/your-protected-route")
public class YourController extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // AuthFilter already validates session
        // Just get user from session
        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute("user");
        
        // Your logic here
    }
}
```

### 5. Role-Based Access in Servlets

```java
// Check role before processing
HttpSession session = request.getSession(false);
User user = (User) session.getAttribute("user");

if (!"Admin".equals(user.getRole())) {
    response.sendRedirect(request.getContextPath() + "/error/403.jsp");
    return;
}

// Admin-only logic here
```

### 6. Adding New Protected Routes to AuthFilter

Edit `AuthFilter.java`:

```java
private static final Map<String, List<String>> ROLE_ACCESS_MAP = new HashMap<>();

static {
    // Add your new route with allowed roles
    ROLE_ACCESS_MAP.put("/your-route", Arrays.asList("Admin", "Manager"));
}
```

### 7. Making a Route Public (No Authentication)

Edit `AuthFilter.java`:

```java
private static final List<String> PUBLIC_PATHS = Arrays.asList(
    "/auth",
    "/assets",
    "/your-public-path"  // Add here
);
```

### 8. Authentication URLs

| Action | URL | Method | Access |
|--------|-----|--------|--------|
| Login Page | `/auth?action=login` | GET | Public |
| Login Submit | `/auth` | POST | Public |
| Register Page | `/auth?action=register` | GET | Admin Only |
| Register Submit | `/auth` | POST | Admin Only |
| Change Password Page | `/auth?action=change-password` | GET | Authenticated |
| Change Password Submit | `/auth` | POST | Authenticated |
| Logout | `/auth?action=logout` | GET | Authenticated |

### 9. Session Attributes Available

After successful login, these attributes are set:

```java
session.getAttribute("user");      // User object
session.getAttribute("userId");    // Long
session.getAttribute("username");  // String
session.getAttribute("role");      // String
```

### 10. Typical Page Structure

```jsp
<%@ page contentType="text/html;charset=UTF-8"  %>
<%@ page import="vn.edu.fpt.swp.model.User" %>
<%
    // Session check
    HttpSession userSession = request.getSession(false);
    if (userSession == null || userSession.getAttribute("user") == null) {
        response.sendRedirect(request.getContextPath() + "/auth?action=login");
        return;
    }
    User currentUser = (User) userSession.getAttribute("user");
    
    // Optional: Role check
    if (!"Admin".equals(currentUser.getRole())) {
        response.sendRedirect(request.getContextPath() + "/error/403.jsp");
        return;
    }
%>
<!doctype html>
<html lang="en">
<head>
    <!-- Standard head content -->
</head>
<body>
    <!-- Your page content -->
</body>
</html>
```

### 11. Using AuthService in Your Code

```java
import vn.edu.fpt.swp.service.AuthService;

AuthService authService = new AuthService();

// Authenticate user
User user = authService.authenticate(username, password);

// Register new user (admin only)
User newUser = new User();
newUser.setUsername("johndoe");
newUser.setEmail("john@example.com");
newUser.setRole("Staff");
User created = authService.registerUser(newUser, plainPassword);

// Change password
boolean success = authService.changePassword(userId, currentPassword, newPassword);

// Reset password (admin only)
boolean reset = authService.resetPassword(userId, newPassword);
```

### 12. Using UserDAO Directly

```java
import vn.edu.fpt.swp.dao.UserDAO;

UserDAO userDAO = new UserDAO();

// Find by username
User user = userDAO.findByUsername("admin");

// Find by email
User user = userDAO.findByEmail("admin@example.com");

// Find by ID
User user = userDAO.findById(1L);

// Update last login
boolean updated = userDAO.updateLastLogin(userId);

// Update password
boolean updated = userDAO.updatePassword(userId, newHashedPassword);
```

### 13. Password Handling

```java
import vn.edu.fpt.swp.util.PasswordUtil;

// Hash a password
String hashedPassword = PasswordUtil.hashPassword("plainPassword");

// Verify a password
boolean isValid = PasswordUtil.verifyPassword("plainPassword", storedHash);
```

### 14. Common Error Handling Pattern

```jsp
<%
    String error = (String) request.getAttribute("error");
    String success = (String) request.getAttribute("success");
%>

<% if (error != null) { %>
<div class="alert alert-danger alert-dismissible" role="alert">
    <%= error %>
    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
</div>
<% } %>

<% if (success != null) { %>
<div class="alert alert-success alert-dismissible" role="alert">
    <%= success %>
    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
</div>
<% } %>
```

### 15. Session Timeout Behavior

- Timeout: 30 minutes of inactivity
- Automatic redirect to login page
- Error message: "Your session has expired. Please login again."
- Last activity tracked on every request

---

## Test Credentials

After running `database/user_seed.sql`:

| Username | Password | Role |
|----------|----------|------|
| admin | password123 | Admin |
| manager | password123 | Manager |
| staff | password123 | Staff |
| sales | password123 | Sales |

---

## Common Issues & Solutions

### Issue: Session not persisting
**Solution:** Ensure cookies are enabled in browser

### Issue: 403 Forbidden on protected route
**Solution:** Check role in `AuthFilter.ROLE_ACCESS_MAP`

### Issue: Login successful but redirect fails
**Solution:** Ensure `/dashboard` route exists or change redirect URL

### Issue: Session expires too quickly
**Solution:** Check `web.xml` session-timeout configuration (default 30 minutes)

### Issue: Password verification fails
**Solution:** Ensure password was hashed with `PasswordUtil.hashPassword()`

---

## Best Practices

1. **Always check session in JSP pages**
2. **Use PreparedStatement for SQL queries**
3. **Never store plain text passwords**
4. **Use try-with-resources for database connections**
5. **Validate input on both client and server side**
6. **Use generic error messages for authentication failures**
7. **Keep session attributes minimal**
8. **Update lastAccessTime for activity tracking**

---

**This authentication module is production-ready for the academic project scope.**
