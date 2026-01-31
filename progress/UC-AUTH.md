# UC-AUTH Implementation Progress

## Status: ✅ Completed
**Last Updated:** January 31, 2026

---

## Overview

All UC-AUTH (Authentication) use cases have been implemented following the detail design specifications.

---

## UC-AUTH-001: User Login
**Status:** ✅ Completed

### Implementation Details:
- **Controller:** `AuthController.java` - handles `/auth?action=login`
- **Service:** `AuthService.authenticate()` - validates credentials
- **DAO:** `UserDAO.findByUsername()` - retrieves user from database
- **View:** `login.jsp` - login form using Bootstrap template
- **Utility:** `PasswordUtil.verifyPassword()` - SHA-256 password verification

### Features Implemented:
- [x] Display login form with username/password fields
- [x] Input validation (empty field check)
- [x] User authentication via SHA-256 with salt
- [x] Active user status validation
- [x] Session creation with 30-minute timeout
- [x] Update last login timestamp
- [x] Redirect to dashboard after successful login
- [x] Error messages without revealing username/password specifics (BR-AUTH-003)
- [x] Password visibility toggle
- [x] Retain username on validation failure

### Error Handling:
- [x] A1: Input Validation Failed - "Username and password are required"
- [x] A2: User Not Found - "Invalid username or password"
- [x] A3: User Account Inactive - "Your account has been deactivated. Please contact administrator"
- [x] A4: Password Incorrect - "Invalid username or password"

---

## UC-AUTH-002: Change Password
**Status:** ✅ Completed

### Implementation Details:
- **Controller:** `AuthController.java` - handles `/auth?action=changePassword`
- **Service:** `AuthService.changePassword()` - validates and updates password
- **DAO:** `UserDAO.updatePassword()` - updates password hash in database
- **View:** `change-password.jsp` - change password form with layout

### Features Implemented:
- [x] Change password form with current/new/confirm fields
- [x] Current password verification
- [x] New password minimum 6 characters validation
- [x] Password confirmation matching
- [x] New password must differ from current password
- [x] Generate new salt for new password
- [x] Session remains active after password change
- [x] Success/error message display
- [x] Password visibility toggle for all fields
- [x] Client-side validation for password match

### Error Handling:
- [x] A1: Input Validation Failed - specific error messages
- [x] A2: Current Password Incorrect - "Current password is incorrect"

---

## UC-AUTH-003: Admin Reset Password
**Status:** ✅ Completed (Service Layer)

### Implementation Details:
- **Service:** `AuthService.resetPassword()` - resets user password

### Features Implemented:
- [x] Password validation (minimum 6 characters)
- [x] New password hashing with fresh salt
- [x] Service method ready for integration

### Note:
Full UI implementation is part of the User Management module (UC-USER). The service layer is complete and functional for Admin to reset any user's password.

---

## UC-AUTH-004: User Logout
**Status:** ✅ Completed

### Implementation Details:
- **Controller:** `AuthController.java` - handles `/auth?action=logout`
- **Filter:** `AuthFilter.java` - validates session on protected routes

### Features Implemented:
- [x] Logout link in navbar dropdown
- [x] Complete session invalidation
- [x] Clear all session attributes (user, userId, role, lastActivityTime)
- [x] Redirect to login page after logout
- [x] Success message: "You have been logged out successfully"

---

## UC-AUTH-005: Session Timeout
**Status:** ✅ Completed

### Implementation Details:
- **Filter:** `AuthFilter.java` - WebFilter for all requests (`/*`)
- **Configuration:** `web.xml` - 30-minute session timeout

### Features Implemented:
- [x] Track last activity timestamp for each session
- [x] Update activity timestamp on each request
- [x] 30-minute (1800 seconds) inactivity timeout
- [x] Session validation on every request
- [x] Public paths bypass authentication
- [x] Role-based access control (ROLE_ACCESS_MAP)
- [x] Expired session message: "Your session has expired. Please login again."
- [x] AJAX request handling (401 Unauthorized response)
- [x] Regular request handling (redirect to login)

### Public Paths (No Authentication Required):
- `/auth` - Authentication pages
- `/assets` - Static assets
- `/css`, `/js`, `/images`, `/libs`, `/fonts` - Static resources
- Static file extensions (.css, .js, .png, etc.)

### Role-Based Access Control:
| Route | Allowed Roles |
|-------|---------------|
| `/user`, `/users` | Admin |
| `/warehouse`, `/location` | Admin, Manager |
| `/category/add,edit,delete` | Admin, Manager |
| `/product/add,edit,toggle` | Admin, Manager |
| `/inbound`, `/outbound`, `/transfer`, `/movement`, `/inventory` | Admin, Manager, Staff |
| `/sales-order`, `/customer` | Admin, Manager, Sales |
| `/dashboard`, `/profile`, `/product`, `/category` | All Roles |

---

## Files Created/Modified

### New Files:
1. `src/main/java/vn/edu/fpt/swp/controller/AuthController.java`
2. `src/main/java/vn/edu/fpt/swp/filter/AuthFilter.java`

### Modified Files:
1. `src/main/java/vn/edu/fpt/swp/controller/DashboardController.java` - Added implementation
2. `src/main/java/vn/edu/fpt/swp/service/AuthService.java` - Added `findByUsername()` and `findById()` methods
3. `src/main/webapp/WEB-INF/views/auth/login.jsp` - Full login page implementation
4. `src/main/webapp/WEB-INF/views/auth/change-password.jsp` - Full change password page implementation

### Existing Files (Already Implemented):
1. `src/main/java/vn/edu/fpt/swp/dao/UserDAO.java` - Complete
2. `src/main/java/vn/edu/fpt/swp/service/AuthService.java` - Complete with `authenticate()`, `changePassword()`, `resetPassword()`
3. `src/main/java/vn/edu/fpt/swp/util/PasswordUtil.java` - Complete with SHA-256 hashing
4. `src/main/java/vn/edu/fpt/swp/model/User.java` - Complete
5. `src/main/webapp/WEB-INF/web.xml` - 30-minute session timeout configured
6. `src/main/webapp/WEB-INF/views/dashboard.jsp` - Dashboard view
7. `src/main/webapp/WEB-INF/common/*.jsp` - Layout components

---

## Testing Checklist

### Login (UC-AUTH-001):
- [ ] Navigate to `/auth?action=login`
- [ ] Login form displays correctly
- [ ] Empty fields show validation error
- [ ] Invalid username shows generic error
- [ ] Invalid password shows generic error
- [ ] Inactive user shows deactivation message
- [ ] Valid credentials redirect to dashboard
- [ ] Success message displays on dashboard

### Change Password (UC-AUTH-002):
- [ ] Navigate to `/auth?action=changePassword` when logged in
- [ ] Form displays with all three password fields
- [ ] Wrong current password shows error
- [ ] Short new password shows validation error
- [ ] Mismatched passwords show error
- [ ] Same new/current password shows error
- [ ] Successful change shows success message
- [ ] Can login with new password

### Logout (UC-AUTH-004):
- [ ] Click logout in navbar dropdown
- [ ] Redirected to login page
- [ ] Success message displays
- [ ] Cannot access protected pages after logout

### Session Timeout (UC-AUTH-005):
- [ ] Session expires after 30 minutes of inactivity
- [ ] Expired session shows warning on login page
- [ ] Protected routes redirect to login when not authenticated
- [ ] Role-based access control works correctly

---

## Test Credentials

| Username | Password | Role |
|----------|----------|------|
| admin | password123 | Admin |
| manager | password123 | Manager |
| staff | password123 | Staff |
| sales | password123 | Sales |

---

## Business Rules Compliance

| Rule ID | Description | Status |
|---------|-------------|--------|
| BR-AUTH-001 | All users must authenticate before accessing any system feature | ✅ Implemented via AuthFilter |
| BR-AUTH-002 | Passwords are verified using SHA-256 hash with salt comparison | ✅ Implemented in PasswordUtil |
| BR-AUTH-003 | Error messages must not reveal whether username or password was incorrect | ✅ Generic "Invalid username or password" message |
| BR-AUTH-004 | Session timeout is 30 minutes of inactivity | ✅ Configured in web.xml and AuthFilter |
| BR-PWD-001 | User must verify identity by entering current password | ✅ Implemented in changePassword |
| BR-PWD-002 | New password must be minimum 6 characters | ✅ Validated in controller and service |
| BR-PWD-003 | New password must be hashed with SHA-256 and new random salt | ✅ Implemented in PasswordUtil |
| BR-PWD-004 | New password cannot be the same as current password | ✅ Validated in controller |
| BR-RST-001 | Only Admin role can reset other users' passwords | ✅ Service ready, UI in UC-USER |
| BR-RST-002 | Reset password must meet minimum 6 character requirement | ✅ Validated in service |
| BR-SES-001 | Session timeout is 30 minutes of inactivity | ✅ Implemented |
| BR-SES-002 | Any user request resets the inactivity timer | ✅ Implemented in AuthFilter |
| BR-SES-003 | Expired sessions must be completely invalidated | ✅ Implemented |
| BR-SES-004 | Users must re-authenticate after session expiration | ✅ Implemented |
