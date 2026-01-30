# UC-AUTH-003: Change Password

## Status
Completed

## Implementation Tasks
- [x] Add updatePassword method to UserDAO
- [x] Add changePassword logic to AuthService
- [x] Add change password handler to AuthController
- [x] Create change-password.jsp view
- [x] Implement validation (minimum 6 chars, passwords match)
- [x] Verify current password before allowing change

## Files Created/Modified
- Modified: UserDAO.java (added updatePassword, findById methods)
- Modified: AuthService.java (added changePassword method)
- Modified: AuthController.java (added change password handlers)
- Created: change-password.jsp

## Notes
- Available to all authenticated users
- Must verify current password
- Generate new salt for new password
- All validation implemented as per detail design
