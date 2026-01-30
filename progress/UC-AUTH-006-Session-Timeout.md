# UC-AUTH-006: Session Timeout

## Status: In Progress

## Implementation Details

### Current State
- Session timeout set to 30 minutes in AuthController
- AuthFilter intercepts all requests
- Need to verify timeout configuration in web.xml

### Requirements Review
- [ ] Track session activity timestamp
- [ ] Update activity on each request
- [ ] Check session validity on each request
- [ ] 30-minute timeout configured
- [ ] Redirect to login on expired session
- [ ] Display "session expired" message

### Missing Functionality
- [ ] Need to verify web.xml has session timeout configured
- [ ] Need to ensure AuthFilter handles expired sessions properly
- [ ] Need to verify error message for expired sessions

### Notes
- Session timeout set programmatically in controller
- Need to verify timeout enforcement in filter
- Need to check web.xml configuration
