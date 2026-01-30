# UC-AUTH-006: Session Timeout

## Status: Completed

## Implementation Details

### Completed Features
- [x] 30-minute timeout configured - web.xml line 19
- [x] Session timeout also set programmatically - AuthController line 118
- [x] AuthFilter handles expired sessions - Checks session existence
- [x] Redirect to login on expired session - With expired parameter
- [x] Display "session expired" message - login.jsp lines 80-85

### Files
- Configuration: src/main/webapp/WEB-INF/web.xml
- Filter: src/main/java/vn/edu/fpt/swp/filter/AuthFilter.java
- Controller: src/main/java/vn/edu/fpt/swp/controller/AuthController.java
- View: src/main/webapp/views/auth/login.jsp

### Technical Implementation
- Session timeout: 30 minutes (1800 seconds)
- Configured in web.xml and programmatically
- AuthFilter checks session validity on each request
- Enhanced to detect expired sessions and show appropriate message

### Verification
- Timeout enforced at container level (web.xml)
- User-friendly error message on expiration
- Automatic redirect to login page
