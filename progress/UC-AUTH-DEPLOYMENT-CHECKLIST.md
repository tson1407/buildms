# UC-AUTH Deployment Checklist

## Pre-Deployment Steps

### 1. Database Configuration
- [ ] SQL Server is running
- [ ] Database `smartwms_db` exists
- [ ] Run `database/schema.sql` to create tables
- [ ] Run `database/user_seed.sql` to create test users (optional)
- [ ] Verify Users table exists with correct schema

### 2. Application Configuration
- [ ] Update `DBConnection.java` with correct database credentials:
  ```java
  private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=smartwms_db;...";
  private static final String USERNAME = "your_username";
  private static final String PASSWORD = "your_password";
  ```

### 3. Build Application
```bash
cd /path/to/buildms
mvn clean package
```
- [ ] Build completes successfully
- [ ] `target/buildms.war` file generated
- [ ] No compilation errors

### 4. Apache Tomcat Setup
- [ ] Tomcat 10+ installed
- [ ] Tomcat is stopped
- [ ] Delete old deployment (if exists):
  - Delete `webapps/buildms.war`
  - Delete `webapps/buildms/` folder
  - Delete `work/Catalina/localhost/buildms/` folder

### 5. Deploy Application
- [ ] Copy `target/buildms.war` to Tomcat `webapps/` directory
- [ ] Start Tomcat
- [ ] Wait for automatic deployment
- [ ] Check logs for deployment success:
  - `logs/catalina.out` (no exceptions)
  - Look for "Deployment of web application archive ... has finished"

---

## Post-Deployment Verification

### 6. Basic Access Test
- [ ] Navigate to: `http://localhost:8080/buildms/`
- [ ] Should redirect to login page or show welcome page
- [ ] Navigate to: `http://localhost:8080/buildms/auth?action=login`
- [ ] Login page displays correctly
- [ ] CSS/JS assets load properly (check browser console)

### 7. Login Test (UC-AUTH-001)
- [ ] Enter test credentials:
  - Username: `admin`
  - Password: `password123`
- [ ] Click "Login" button
- [ ] Should redirect to dashboard
- [ ] User information displays correctly
- [ ] No errors in browser console
- [ ] No errors in Tomcat logs

### 8. Session Test (UC-AUTH-006)
- [ ] After login, wait 2-3 minutes
- [ ] Navigate to another page
- [ ] Session should still be valid
- [ ] User information still available
- [ ] Check session timeout is set to 30 minutes

### 9. Logout Test (UC-AUTH-005)
- [ ] Click "Logout" button or navigate to `/auth?action=logout`
- [ ] Should redirect to login page
- [ ] Success message displays: "You have been logged out successfully"
- [ ] Try accessing dashboard - should redirect to login
- [ ] Session is destroyed

### 10. Registration Test (UC-AUTH-002) - Admin Only
- [ ] Login as admin
- [ ] Navigate to `/auth?action=register`
- [ ] Fill in new user details:
  - Username: `testuser`
  - Email: `test@example.com`
  - Password: `test123`
  - Confirm Password: `test123`
  - Role: `Staff`
- [ ] Click "Create User"
- [ ] Success message displays
- [ ] Logout and login with new credentials
- [ ] Login successful

### 11. Change Password Test (UC-AUTH-003)
- [ ] Login as any user
- [ ] Navigate to `/auth?action=change-password`
- [ ] Fill in form:
  - Current Password: (correct password)
  - New Password: `newpass123`
  - Confirm New Password: `newpass123`
- [ ] Click "Change Password"
- [ ] Success message displays
- [ ] Logout
- [ ] Login with new password
- [ ] Login successful

### 12. Access Control Test
- [ ] Login as `staff` user (not admin)
- [ ] Try to access `/auth?action=register`
- [ ] Should redirect to 403.jsp error page
- [ ] Role-based access control working

### 13. Validation Test
Login page:
- [ ] Submit empty form - shows "Username and password are required"
- [ ] Submit wrong credentials - shows "Invalid username or password"

Registration page:
- [ ] Submit with short username - shows validation error
- [ ] Submit with invalid email - shows validation error
- [ ] Submit with short password - shows validation error
- [ ] Submit with non-matching passwords - shows validation error
- [ ] Submit with existing username - shows "Username is already taken"
- [ ] Submit with existing email - shows "Email is already registered"

Change Password page:
- [ ] Submit with wrong current password - shows error
- [ ] Submit with short new password - shows validation error
- [ ] Submit with non-matching passwords - shows validation error
- [ ] Submit with same password as current - shows error

---

## Troubleshooting

### Problem: Login page doesn't display
**Check:**
1. WAR deployed correctly in `webapps/`
2. Tomcat started without errors
3. Check `logs/catalina.out` for exceptions
4. Verify URL: `http://localhost:8080/buildms/auth?action=login`

### Problem: Database connection error
**Check:**
1. SQL Server is running
2. Database name is correct: `smartwms_db`
3. Credentials in `DBConnection.java` are correct
4. SQL Server accepts TCP/IP connections on port 1433
5. Check Tomcat logs for `SQLException`

### Problem: 404 Not Found on assets
**Check:**
1. Assets copied from `template/assets/` to `src/main/webapp/assets/`
2. Files included in WAR: `jar -tf target/buildms.war | grep assets`
3. Browser network tab shows correct asset URLs

### Problem: Session expires immediately
**Check:**
1. `web.xml` has correct session timeout (30 minutes)
2. AuthFilter not interfering with session creation
3. Browser cookies enabled

### Problem: Authentication works but filter redirects
**Check:**
1. Public paths in AuthFilter include `/auth`
2. Session attributes set correctly after login
3. Check `lastAccessTime` is being updated

### Problem: Password verification fails
**Check:**
1. Test users created with `user_seed.sql`
2. Passwords hashed with `PasswordUtil.hashPassword()`
3. Hash format in database: `salt:hash` (Base64)

---

## Known Limitations

1. **UC-AUTH-004 Admin Reset Password:** Service layer complete, but dedicated UI not implemented (will be part of User Management module)
2. **Email notifications:** Not implemented (future enhancement)
3. **Password strength indicator:** Not implemented (future enhancement)
4. **Remember me:** Not implemented (session-based only)
5. **Multi-session per user:** Not tracked (future enhancement)

---

## Security Notes

✅ **Implemented:**
- SHA-256 password hashing with salt
- Session timeout (30 minutes)
- Role-based access control
- SQL injection prevention (PreparedStatement)
- Input validation
- Generic authentication error messages

⚠️ **Not Implemented (Academic Scope):**
- HTTPS enforcement
- CSRF tokens
- Rate limiting
- Account lockout after failed attempts
- Password history
- Two-factor authentication

---

## Files Checklist

### Must Exist After Deployment

**Java Classes:**
```
vn/edu/fpt/swp/controller/AuthController.class
vn/edu/fpt/swp/controller/DashboardController.class
vn/edu/fpt/swp/dao/UserDAO.class
vn/edu/fpt/swp/service/AuthService.class
vn/edu/fpt/swp/filter/AuthFilter.class
vn/edu/fpt/swp/model/User.class
vn/edu/fpt/swp/util/PasswordUtil.class
vn/edu/fpt/swp/util/DBConnection.class
```

**JSP Files:**
```
views/auth/login.jsp
views/auth/register.jsp
views/auth/change-password.jsp
views/error/403.jsp
dashboard.jsp
```

**Configuration:**
```
WEB-INF/web.xml
```

**Assets:**
```
assets/vendor/css/core.css
assets/vendor/js/bootstrap.js
assets/js/main.js
(and all other template assets)
```

---

## Success Criteria

✅ All 6 UC-AUTH use cases working:
- UC-AUTH-001: User Login
- UC-AUTH-002: User Registration
- UC-AUTH-003: Change Password
- UC-AUTH-004: Admin Reset Password (service layer)
- UC-AUTH-005: User Logout
- UC-AUTH-006: Session Timeout

✅ All validation rules enforced
✅ All error messages display correctly
✅ Role-based access control working
✅ Session management working
✅ Password security implemented

---

## Rollback Plan

If deployment fails:

1. Stop Tomcat
2. Delete `webapps/buildms.war`
3. Delete `webapps/buildms/` folder
4. Delete `work/Catalina/localhost/buildms/` folder
5. Check logs for root cause
6. Fix issue in source code
7. Rebuild: `mvn clean package`
8. Redeploy

---

## Support

For issues, check:
1. Tomcat logs: `logs/catalina.out`
2. Browser console (F12)
3. Network tab in browser dev tools
4. Database connection status
5. Detail design documents in `document/detail-design/`
6. Progress tracking in `progress/`

---

**Deployment Status: Ready for Testing**

Date: January 30, 2026
Version: 1.0
Module: UC-AUTH (Complete)
