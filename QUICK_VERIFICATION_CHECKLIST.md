# Quick Verification Checklist
## Authentication Implementation - Smart WMS

**Purpose:** Quick reference for verifying authentication features work correctly  
**Use:** Check off items as you verify them manually or automatically

---

## Pre-Verification Setup ✅

- [ ] Database running (SQL Server)
- [ ] Database schema initialized (`database/schema.sql`)
- [ ] Test users created (admin, manager, staff, sales)
- [ ] Application built successfully (`mvn clean package`)
- [ ] Application deployed to Tomcat
- [ ] Application accessible at `http://localhost:8080/buildms/`

---

## UC-AUTH-001: User Login ✅

### Core Functionality
- [ ] Can login with valid username and password
- [ ] Redirected to appropriate dashboard based on role
- [ ] Session created and stored in cookies
- [ ] LastLogin timestamp updated in database

### Error Handling
- [ ] Empty username shows: "Username and password are required"
- [ ] Empty password shows: "Username and password are required"
- [ ] Invalid username shows: "Invalid username or password"
- [ ] Wrong password shows: "Invalid username or password"
- [ ] Inactive account rejected (shows error)

### Security
- [ ] Password not visible in clear text in browser
- [ ] No sensitive data in error messages
- [ ] Session has 30-minute timeout

**Status:** ⬜ Not Tested | ✅ Pass | ❌ Fail

---

## UC-AUTH-002: User Registration (Admin Only) ✅

### Core Functionality
- [ ] Admin can access user creation page
- [ ] Can create user with all required fields
- [ ] New user appears in user list
- [ ] Password is hashed in database (not plain text)

### Validation
- [ ] Username must be 3-50 characters
- [ ] Email must be valid format
- [ ] Password must be at least 6 characters
- [ ] Password confirmation must match
- [ ] Duplicate username shows: "Username is already taken"
- [ ] Duplicate email shows: "Email is already registered"

### Access Control
- [ ] Non-admin users cannot access user management
- [ ] Non-admin gets 403 error when trying to access `/user`

**Status:** ⬜ Not Tested | ✅ Pass | ❌ Fail

---

## UC-AUTH-003: Change Password ✅

### Core Functionality
- [ ] Any authenticated user can access change password page
- [ ] Can change password successfully
- [ ] Can login with new password
- [ ] Cannot login with old password

### Validation
- [ ] Current password must be correct
- [ ] New password must be at least 6 characters
- [ ] New password confirmation must match
- [ ] New password must be different from current
- [ ] Incorrect current password shows: "Current password is incorrect"

### Security
- [ ] New salt generated for new password
- [ ] Password updated in database with new hash

**Status:** ⬜ Not Tested | ✅ Pass | ❌ Fail

---

## UC-AUTH-004: Admin Reset Password ✅

### Core Functionality
- [ ] Admin can access reset password feature
- [ ] Can reset any user's password
- [ ] Reset user can login with new password
- [ ] Reset user cannot login with old password

### Validation
- [ ] New password must be at least 6 characters
- [ ] Password confirmation must match

### Access Control
- [ ] Non-admin cannot access password reset
- [ ] Non-admin gets 403 error

### Known Issues
- ⚠️ Sessions not invalidated after reset (marked as TODO)

**Status:** ⬜ Not Tested | ✅ Pass | ❌ Fail

---

## UC-AUTH-005: User Logout ✅

### Core Functionality
- [ ] Can logout from any authenticated page
- [ ] Redirected to login page after logout
- [ ] Success message shown: "You have been successfully logged out"
- [ ] Cannot access protected pages after logout
- [ ] Must login again to access protected pages

### Session Handling
- [ ] Session invalidated on logout
- [ ] Session cookie removed/cleared
- [ ] Back button doesn't show protected content

**Status:** ⬜ Not Tested | ✅ Pass | ❌ Fail

---

## UC-AUTH-006: Session Timeout ✅

### Core Functionality
- [ ] Session times out after 30 minutes of inactivity
- [ ] Timeout message shown: "Your session has expired. Please login again."
- [ ] Redirected to login page on timeout
- [ ] Must login again to continue

### Timer Behavior
- [ ] Activity resets the timeout timer
- [ ] No activity for 30+ minutes triggers timeout
- [ ] Can login again after timeout

### Testing Note
For faster testing, temporarily change timeout to 1-2 minutes in `web.xml`:
```xml
<session-timeout>1</session-timeout>
```

**Status:** ⬜ Not Tested | ✅ Pass | ❌ Fail

---

## Security Verification ✅

### Password Security
- [ ] Passwords hashed in database (not plain text)
- [ ] Each user has different salt
- [ ] Salt format: `salt:hash` (Base64 encoded)
- [ ] SHA-256 algorithm used

### Session Security
- [ ] Session cookies are HTTPOnly
- [ ] Session timeout configured (30 minutes)
- [ ] Sessions invalidated on logout
- [ ] Expired sessions detected and handled

### Access Control
- [ ] Public URLs accessible without login (`/auth`, `/assets`)
- [ ] Protected URLs require authentication
- [ ] Role-based access enforced (admin, manager, staff, sales)
- [ ] Unauthorized access shows 403 error

### Input Validation
- [ ] Server-side validation implemented
- [ ] SQL injection prevented (PreparedStatement used)
- [ ] XSS protection (output encoding)

**Status:** ⬜ Not Tested | ✅ Pass | ❌ Fail

---

## UI/UX Verification ✅

### Template Usage
- [ ] Login page uses Bootstrap 5 template
- [ ] User management pages use Bootstrap 5
- [ ] Forms use Bootstrap styling
- [ ] Consistent look and feel across pages

### Error Messages
- [ ] Error messages displayed clearly (Bootstrap alerts)
- [ ] Success messages displayed clearly
- [ ] Field-level validation errors shown
- [ ] Messages match detail design specifications

### User Experience
- [ ] Forms are intuitive and easy to use
- [ ] Navigation is clear
- [ ] Loading states indicated (if applicable)
- [ ] Responsive design works on different screen sizes

**Status:** ⬜ Not Tested | ✅ Pass | ❌ Fail

---

## Build and Deployment ✅

### Build
- [ ] `mvn clean package` succeeds
- [ ] No compilation errors
- [ ] WAR file created in `target/`

### Deployment
- [ ] WAR deploys to Tomcat successfully
- [ ] Application starts without errors
- [ ] Application accessible via browser
- [ ] Static assets load correctly (CSS, JS, images)

### Code Quality
- [ ] No CodeQL security alerts
- [ ] Code follows project conventions
- [ ] Proper error handling implemented
- [ ] Try-with-resources used for JDBC

**Status:** ⬜ Not Tested | ✅ Pass | ❌ Fail

---

## Overall Status Summary

| Component | Status | Notes |
|-----------|--------|-------|
| UC-AUTH-001 | ⬜ | User Login |
| UC-AUTH-002 | ⬜ | User Registration |
| UC-AUTH-003 | ⬜ | Change Password |
| UC-AUTH-004 | ⬜ | Admin Reset Password |
| UC-AUTH-005 | ⬜ | User Logout |
| UC-AUTH-006 | ⬜ | Session Timeout |
| Security | ⬜ | Security features |
| UI/UX | ⬜ | User interface |
| Build | ⬜ | Build and deployment |

**Legend:**
- ⬜ Not Tested
- ✅ Pass
- ❌ Fail
- ⚠️ Pass with issues

---

## Known Issues and Recommendations

### Critical Issues ❌
None identified

### Minor Issues ⚠️
1. **UC-AUTH-001:** Inactive account shows generic error instead of specific message
   - **Impact:** Low - Users cannot distinguish inactive from wrong password
   - **Recommendation:** Update to show specific "Account deactivated" message

2. **UC-AUTH-004:** Session invalidation on password reset marked as TODO
   - **Impact:** Medium - User can continue using old session after password reset
   - **Recommendation:** Implement session tracking and invalidation

### Optional Features Not Implemented ℹ️
1. Welcome email for new users (UC-AUTH-002)
2. Password reset notification email (UC-AUTH-004)
3. Logout confirmation dialog (UC-AUTH-005)
4. Session timeout warning (UC-AUTH-006)

**Status:** Acceptable for academic project scope

---

## Quick Test Commands

### Database Check
```sql
-- Check user exists
SELECT Username, Role, Status, LastLogin FROM Users WHERE Username = 'admin';

-- Check password is hashed
SELECT PasswordHash FROM Users WHERE Username = 'admin';
-- Should see format: salt:hash (Base64)

-- Create test inactive user
UPDATE Users SET Status = 'Inactive' WHERE Username = 'testuser';
```

### Browser Dev Tools
```javascript
// Check session storage
sessionStorage

// Check cookies
document.cookie

// Check for user session
// Look for JSESSIONID cookie
```

### Test URLs
- Login: `http://localhost:8080/buildms/auth?action=login`
- User List: `http://localhost:8080/buildms/user`
- Create User: `http://localhost:8080/buildms/user?action=create`
- Change Password: `http://localhost:8080/buildms/auth?action=changePassword`
- Logout: `http://localhost:8080/buildms/auth?action=logout`

---

## Sign-Off

**Tested By:** _______________________  
**Date:** _______________________  
**Overall Status:** ⬜ Pass | ⬜ Pass with Issues | ⬜ Fail  
**Ready for Production:** ⬜ Yes | ⬜ No | ⬜ With Conditions

**Comments:**
_____________________________________________________________________________
_____________________________________________________________________________
_____________________________________________________________________________
_____________________________________________________________________________

---

**Document Version:** 1.0  
**Last Updated:** January 30, 2026  
**Next Review:** After manual testing completion
