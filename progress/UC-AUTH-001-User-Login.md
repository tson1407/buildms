# UC-AUTH-001: User Login

## Status: Completed

## Implementation Details

### Completed Features
- [x] Display login form with username/password fields
- [x] Validate input fields (not empty) - Line 99-104 in AuthController
- [x] Authenticate user by username - AuthService.authenticate()
- [x] Check user status (Active/Inactive) - Line 34 in AuthService
- [x] Verify password using SHA-256 with salt - PasswordUtil.verifyPassword()
- [x] Create user session with timeout - Line 111-118 in AuthController
- [x] Update last login timestamp - Line 41 in AuthService
- [x] Redirect to role-appropriate dashboard - getRedirectUrlByRole()
- [x] Handle alternative flows:
  - A1: Empty fields - Error message displayed
  - A2: User not found - Returns null from authenticate
  - A3: Inactive account - Checked in AuthService line 34
  - A4: Incorrect password - Returns null from authenticate

### Files
- Controller: src/main/java/vn/edu/fpt/swp/controller/AuthController.java
- Service: src/main/java/vn/edu/fpt/swp/service/AuthService.java
- View: src/main/webapp/views/auth/login.jsp
- DAO: src/main/java/vn/edu/fpt/swp/dao/UserDAO.java
- Util: src/main/java/vn/edu/fpt/swp/util/PasswordUtil.java

### Verification
- Implementation matches all requirements from detail design
- Error messages follow specifications
- Session timeout set to 30 minutes
- Password hashing with SHA-256 and salt
