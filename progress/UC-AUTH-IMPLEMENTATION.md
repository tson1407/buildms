# UC-AUTH Implementation Progress

## Module Overview
**Module:** Authentication (AUTH)  
**Total Use Cases:** 5  
**Completed:** 5  
**Status:** ✅ Complete  
**Implementation Date:** January 30, 2026

## Use Cases Implemented

### UC-AUTH-001: User Login
**Status:** ✅ Completed

**Description:** Authenticate users with username and password credentials.

**Files Modified/Created:**
- `src/main/java/vn/edu/fpt/swp/controller/AuthController.java` - Login servlet handling
- `src/main/java/vn/edu/fpt/swp/service/AuthService.java` - Authentication logic
- `src/main/java/vn/edu/fpt/swp/dao/UserDAO.java` - User lookup by username
- `src/main/webapp/WEB-INF/views/auth/login.jsp` - Login form view

**Features:**
- Username/password validation
- Password verification using `PasswordUtil`
- Session creation on successful login
- Role-based redirect after login
- Account status check (inactive accounts blocked)
- Error messages for invalid credentials

**Business Rules Implemented:**
- BR-AUTH-001: Valid username required
- BR-AUTH-002: Valid password required
- BR-AUTH-003: Account must be active
- BR-AUTH-004: Session created with user info

---

### UC-AUTH-002: Change Password
**Status:** ✅ Completed

**Description:** Allow authenticated users to change their own password.

**Files Modified/Created:**
- `src/main/java/vn/edu/fpt/swp/controller/AuthController.java` - Change password handling
- `src/main/java/vn/edu/fpt/swp/service/AuthService.java` - Password change logic
- `src/main/webapp/WEB-INF/views/auth/change-password.jsp` - Password change form

**Features:**
- Current password verification
- New password validation (length, complexity)
- Password confirmation matching
- Password hashing before storage
- Success/error feedback

**Business Rules Implemented:**
- BR-AUTH-005: Current password must be correct
- BR-AUTH-006: New password minimum 8 characters
- BR-AUTH-007: Password confirmation must match

---

### UC-AUTH-003: Admin Reset Password
**Status:** ✅ Completed

**Description:** Allow Admin users to reset passwords for other users.

**Files Modified/Created:**
- `src/main/java/vn/edu/fpt/swp/controller/UserController.java` - Reset password action
- `src/main/java/vn/edu/fpt/swp/service/UserService.java` - Password reset logic

**Features:**
- Admin-only access control
- Reset to default password or specified password
- Password hashing
- Audit logging of reset action

**Access Control:**
- Admin only

---

### UC-AUTH-004: User Logout
**Status:** ✅ Completed

**Description:** Allow users to securely log out and terminate their session.

**Files Modified/Created:**
- `src/main/java/vn/edu/fpt/swp/controller/AuthController.java` - Logout action

**Features:**
- Session invalidation
- Redirect to login page
- Clear session attributes

---

### UC-AUTH-005: Session Timeout
**Status:** ✅ Completed

**Description:** Automatic session expiration after inactivity period.

**Files Modified/Created:**
- `src/main/webapp/WEB-INF/web.xml` - Session timeout configuration
- `src/main/java/vn/edu/fpt/swp/filter/AuthFilter.java` - Session validation

**Features:**
- 30-minute session timeout configured in web.xml
- AuthFilter validates session on each request
- Redirect to login on session expiration
- Session expiry message displayed

---

## Technical Implementation Details

### Authentication Flow
1. User submits credentials via login form
2. AuthController receives POST request
3. AuthService validates credentials via UserDAO
4. PasswordUtil verifies hashed password
5. Session created with user object on success
6. Redirect to dashboard based on role

### Security Measures
- Password hashing with SHA-256 + salt
- Session-based authentication
- CSRF protection via form tokens (where applicable)
- AuthFilter intercepts all protected routes
- Role-based access control

### Key Classes
| Class | Purpose |
|-------|---------|
| `AuthController` | Handles login, logout, change password routes |
| `AuthService` | Business logic for authentication |
| `UserDAO` | Database operations for user lookup |
| `PasswordUtil` | Password hashing and verification |
| `AuthFilter` | Request interception for authentication |

### Session Attributes
| Attribute | Type | Description |
|-----------|------|-------------|
| `user` | User | Full user object |
| `userId` | Long | User ID for quick access |
| `role` | String | User role for authorization |

---

## Testing Notes
- Test all roles can login successfully
- Test invalid username shows error
- Test invalid password shows error
- Test inactive account is blocked
- Test session timeout after 30 minutes
- Test logout clears session

## References
- [UC-AUTH-001-User-Login.md](../document/detail-design/UC-AUTH-001-User-Login.md)
- [UC-AUTH-002-Change-Password.md](../document/detail-design/UC-AUTH-002-Change-Password.md)
- [UC-AUTH-003-Admin-Reset-Password.md](../document/detail-design/UC-AUTH-003-Admin-Reset-Password.md)
- [UC-AUTH-004-User-Logout.md](../document/detail-design/UC-AUTH-004-User-Logout.md)
- [UC-AUTH-005-Session-Timeout.md](../document/detail-design/UC-AUTH-005-Session-Timeout.md)
