# UC-AUTH-003: Change Password

## Status: Not Started

## Implementation Details

### Current State
- AuthService has changePassword method
- No controller endpoint for change password
- No JSP view for change password

### Requirements Review
- [ ] Navigate to change password from user profile
- [ ] Display change password form
- [ ] Validate current password, new password, confirm new password
- [ ] Verify current password
- [ ] Ensure new password differs from current
- [ ] Generate new salt and hash
- [ ] Update password in database
- [ ] Display success confirmation

### Missing Functionality
- [ ] Controller endpoint for change password GET/POST
- [ ] JSP view for change password form
- [ ] Integration with user profile/settings page

### Notes
- Service layer exists, need to add controller and view
- Need to create accessible link from user profile
