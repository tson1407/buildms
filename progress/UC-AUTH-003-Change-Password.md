# UC-AUTH-003: Change Password

## Status: Completed

## Implementation Details

### Completed Features
- [x] Change password endpoint in AuthController
- [x] Display change password form - change-password.jsp
- [x] Validate current password, new password, confirm - Lines 236-282 in AuthController
- [x] Verify current password - authService.changePassword()
- [x] Ensure new password differs from current - Line 271 in AuthController
- [x] Generate new salt and hash - PasswordUtil.hashPassword() in AuthService
- [x] Update password in database - userDAO.updatePassword()
- [x] Display success confirmation - Success alert in JSP

### Files
- Controller: src/main/java/vn/edu/fpt/swp/controller/AuthController.java
- Service: src/main/java/vn/edu/fpt/swp/service/AuthService.java
- View: src/main/webapp/views/auth/change-password.jsp
- DAO: src/main/java/vn/edu/fpt/swp/dao/UserDAO.java

### Alternative Flows Implemented
- A1: Input validation failed - All error messages displayed
- A2: Current password incorrect - Error message: "Current password is incorrect"

### Verification
- Accessible to all authenticated users
- Proper validation at each step
- Error messages match specifications
